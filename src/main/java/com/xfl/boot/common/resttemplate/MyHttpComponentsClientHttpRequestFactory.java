/**
 *
 */
package com.xfl.boot.common.resttemplate;

import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.methods.HttpUriRequest;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

import java.net.URI;


public class MyHttpComponentsClientHttpRequestFactory extends HttpComponentsClientHttpRequestFactory {

    /**
     *
     */
    public MyHttpComponentsClientHttpRequestFactory(HttpClient httpClient) {
        super(httpClient);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.springframework.http.client.HttpComponentsClientHttpRequestFactory
     * #postProcessHttpRequest(org.apache.http.client.methods.HttpUriRequest)
     */
    @Override
    protected void postProcessHttpRequest(HttpUriRequest request) {
        if (request instanceof HttpRequestBase) {
            HttpRequestBase httpRequestBase = (HttpRequestBase) request;
            URI uri = request.getURI();
            String url = uri.toString();
            RequestConfig requestConfig = HttpRequestConfigHolder.getConfig(url);
            if (requestConfig != null) {
                httpRequestBase.setConfig(requestConfig);
            }
        }
    }
}
