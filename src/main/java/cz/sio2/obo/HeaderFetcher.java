package cz.sio2.obo;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static cz.sio2.obo.Utils.createBuilder;

@Slf4j
public class HeaderFetcher {

    public final static Map<String,OntologyHeaderExtractor> ONTOLOGY_HEADER_EXTRACTORS = new HashMap<>();

    static {
        final ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        try {
            mapper.readValue(HeaderFetcher.class.getResourceAsStream("/extractors.yml"), new TypeReference<List<OntologyHeaderExtractor>>() {
            } ).forEach(a -> ONTOLOGY_HEADER_EXTRACTORS.put(a.getName(), a));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Fetches relevant parts of an ontology file which contain header information. Currently, it takes
     * - first maxBytes of the document
     * - last maxBytes of the document
     *
     * @param url      URL to fetch the document from
     * @param maxBytes maximal number of bytes to fetch from each side of the document
     * @return header information from the ontology
     */
    public OntologyHeader fetch(final URL url, final int maxBytes) {
        final RequestConfig cfg = RequestConfig.custom()
                .setRedirectsEnabled(true)
                .setConnectTimeout(Timeout.ofMinutes(1)).build();
        final HttpClientBuilder httpClientBuilder = createBuilder()
                .setDefaultRequestConfig(cfg);
        try (final CloseableHttpClient httpClient = httpClientBuilder.build()) {
            final boolean supportsRangeRequests = supportsRangeRequests(httpClient, url);
            if (supportsRangeRequests) {
                log.info("- range request (first part)");
                final String s1 = getRange(httpClient, url, maxBytes, true);
                if (s1 == null) {
                    log.debug(" - failed, skipping next part");
                    return null;
                }
                log.info("- range request (second part)");
                String s2 = getRange(httpClient, url, maxBytes, false);
                if (s2 == null) {
                    log.debug("- failed, not considering tail");
                    s2 = "";
                }
                log.info("- done, extracting");
                return extract(s1 + s2);
            } else {
                log.info("- not supporting range request, fetching the whole ontology.");
                HttpGet request1 = new HttpGet(url.toString());
                final String s1 = extractContentFromResponse(httpClient.execute(request1), maxBytes);
                log.info("- done, extracting");
                return extract(s1);
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
            return null;
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

    private OntologyHeader extract(final String content) {
        final Extractor e = new Extractor();
        for (final OntologyHeaderExtractor ohe : ONTOLOGY_HEADER_EXTRACTORS.values()) {
            final OntologyHeader header = e.extract(content, ohe);
            if (header != null) {
                return header;
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
