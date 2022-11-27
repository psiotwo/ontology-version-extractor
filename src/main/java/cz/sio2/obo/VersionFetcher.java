package cz.sio2.obo;

import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpHead;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.core5.http.HttpHeaders;
import org.apache.hc.core5.http.HttpResponse;
import org.apache.hc.core5.http.ProtocolException;
import org.apache.hc.core5.util.Timeout;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static cz.sio2.obo.Utils.createBuilder;

@Slf4j
public class VersionFetcher {

    final static List<VersionExtractor> extractors = new ArrayList<>();

    static {
        extractors.add(new RDFXMLVersionExtractor());
        extractors.add(new FSVersionExtractor());
        extractors.add(new XMLVersionExtractor());
    }

    /**
     * Fetches relevant parts of an ontology file which contain version information. Currently, it takes
     * - first maxBytes of the document
     * - last maxBytes of the document
     *
     * @param url      URL to fetch the document from
     * @param maxBytes maximal number of bytes to fetch from each side of the document
     * @return Version information from the ontology
     */
    public Version fetch(final URL url, final int maxBytes) {
        final RequestConfig cfg = RequestConfig.custom().setConnectTimeout(Timeout.ofMinutes(1)).build();
        final HttpClientBuilder httpClientBuilder = createBuilder().setDefaultRequestConfig(cfg);
        try (final CloseableHttpClient httpClient = httpClientBuilder.build()) {
            final boolean supportsRangeRequests = supportsRangeRequests(httpClient, url);
            if (supportsRangeRequests) {
                log.info("- range request (first part)");
                final String s1 = getRange(httpClient, url, maxBytes, true);
                log.info("- range request (second part)");
                final String s2 = getRange(httpClient, url, maxBytes, false);
                log.info("- done, extracting");
                return extractVersion(s1 + s2);
            } else {
                log.info("- not supporting range request, fetching the whole ontology.");
                HttpGet request1 = new HttpGet(url.toString());
                final String s1 = extractContentFromResponse(httpClient.execute(request1), maxBytes);
                log.info("- done, extracting");
                return extractVersion(s1);
            }
        } catch (Exception e) {
            log.info("An error occurred during fetching ontology from URL " + url, e);
        }
        return null;
    }

    private String getRange(final CloseableHttpClient httpClient, final URL url, final int maxBytes, final boolean fromStart) {
        try {
            HttpGet req = new HttpGet(url.toString());
            req.addHeader(HttpHeaders.RANGE, "bytes=" + (fromStart ? "0":"") + "-" + maxBytes);
            req.addHeader(HttpHeaders.ACCEPT_ENCODING, "none");
            return extractContentFromResponse(httpClient.execute(req), maxBytes);
        } catch(Exception e) {
            return "";
        }
    }

    private String extractContentFromResponse(final CloseableHttpResponse response, final int maxBytes) throws IOException {
        try (response) {
            if (response.getCode() >= 200 && response.getCode() < 300) {
                return new String(response.getEntity().getContent().readNBytes(maxBytes), StandardCharsets.UTF_8);
            }
            return null;
        }
    }

    private Version extractVersion(final String content) {
        final Version version = new Version();
        for (final VersionExtractor e : extractors) {
            if (e.extract(content, version)) {
                return version;
            }
        }
        return null;
    }

    /**
     * Checks whether an url supports range requests.
     */
    private boolean supportsRangeRequests(final HttpClient httpClient, final URL url) throws IOException, ProtocolException {
        final HttpHead request = new HttpHead(url.toString());
        final HttpResponse response = httpClient.execute(request);
        return response.getCode() >= 200 && response.getCode() < 300 && response.getHeader(HttpHeaders.ACCEPT_RANGES) != null;
    }
}
