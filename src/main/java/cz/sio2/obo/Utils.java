package cz.sio2.obo;

import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.routing.DefaultProxyRoutePlanner;
import org.apache.hc.core5.http.HttpHost;
import org.apache.hc.core5.http.protocol.HttpContext;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
public class Utils {

    public static HttpClientBuilder createBuilder() {
        int timeout = 10;
        final RequestConfig config = RequestConfig.custom()
                .setConnectTimeout(timeout, TimeUnit.SECONDS)
                .setConnectionRequestTimeout(timeout, TimeUnit.SECONDS)
                .setResponseTimeout(timeout, TimeUnit.SECONDS)
                .setRedirectsEnabled(true)
                .build();

        final HttpClientBuilder httpClientBuilder = HttpClients
                .custom()
                .setDefaultRequestConfig(config);

        final String nonProxyHostsString = System.getProperty("http.nonProxyHosts");
        final List<String> nonProxyHosts = nonProxyHostsString != null ? Arrays.asList(nonProxyHostsString.split(",")) : Collections.emptyList();
        final String proxyHost = System.getProperty("http.proxyHost");
        if (proxyHost != null && !proxyHost.isEmpty()) {
            final String proxyPort = System.getProperty("http.proxyPort");
            final HttpHost proxy = new HttpHost(proxyHost, Integer.parseInt(proxyPort));
            final DefaultProxyRoutePlanner routePlanner = new DefaultProxyRoutePlanner(proxy) {

                public HttpHost determineProxy(
                        final HttpHost host,
                        final HttpContext context) {
                    final String hostname = host.getHostName();
                    if (nonProxyHosts.contains(hostname)) {
                        return null;
                    }
                    return proxy;
                }
            };

            httpClientBuilder.setRoutePlanner(routePlanner);
        }

        return httpClientBuilder;
    }
}
