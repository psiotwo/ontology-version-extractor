package cz.sio2.obo;

import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.core5.http.HttpHeaders;

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

    public Version fetch(final URL url, final int maxBytes) {
        final HttpClientBuilder httpClientBuilder = createBuilder();
        final CloseableHttpClient httpClient = httpClientBuilder.build();
        final HttpGet httpGet = new HttpGet(url.toString());
        httpGet.addHeader(HttpHeaders.RANGE, "bytes=0-" + maxBytes);
        final CloseableHttpResponse response;
        try {
            response = httpClient.execute(httpGet);
            if (response.getCode() >= 200 && response.getCode() < 300) {
                final String s = new String(response.getEntity().getContent().readNBytes(maxBytes - 1), StandardCharsets.UTF_8);
                final Version version = new Version();
                for (final VersionExtractor e : extractors) {
                    if (e.extract(s, version)) {
                        return version;
                    }
                }
                return null;
            }
        } catch (IOException e) {
            log.info("An error occurred during fetching ontology from URL " + url, e);
        }
        return null;
    }
}
