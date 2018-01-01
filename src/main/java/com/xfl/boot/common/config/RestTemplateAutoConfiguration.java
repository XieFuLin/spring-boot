
package com.xfl.boot.common.config;

import com.xfl.boot.common.resttemplate.HttpRequestHelper;
import com.xfl.boot.common.resttemplate.MyHttpComponentsClientHttpRequestFactory;
import org.apache.http.client.HttpClient;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.TrustStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

/**
 *
 */
@Configuration
@ConditionalOnClass(RestTemplate.class)
@EnableConfigurationProperties(RestTemplateProperties.class)
public class RestTemplateAutoConfiguration {
    private static final Logger log = LoggerFactory.getLogger(RestTemplateAutoConfiguration.class);
    @Autowired
    private RestTemplateProperties properties;

    @Bean
    @ConditionalOnMissingBean
    public HttpComponentsClientHttpRequestFactory createClientHttpRequestFactory() {
        try {
            HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
            SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
                public boolean isTrusted(X509Certificate[] arg0, String arg1) {
                    return true;
                }
            }).build();
            httpClientBuilder.setSSLContext(sslContext);
            HostnameVerifier hostnameVerifier = NoopHostnameVerifier.INSTANCE;
            SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(sslContext,
                    hostnameVerifier);
            Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder
                    .<ConnectionSocketFactory>create()
                    .register("http", PlainConnectionSocketFactory.getSocketFactory())
                    .register("https", sslConnectionSocketFactory).build();// 注册http和https请求
            PoolingHttpClientConnectionManager poolingHttpClientConnectionManager = new PoolingHttpClientConnectionManager(
                    socketFactoryRegistry);// 开始设置连接池
            poolingHttpClientConnectionManager.setMaxTotal(properties.getConnMaxTotal());
            poolingHttpClientConnectionManager.setDefaultMaxPerRoute(properties.getMaxPerRoute());
            httpClientBuilder.setConnectionManager(poolingHttpClientConnectionManager);
            httpClientBuilder.setRetryHandler(new DefaultHttpRequestRetryHandler(properties.getRetryTime(), true));// 重试次数
            HttpClient httpClient = httpClientBuilder.build();
            MyHttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new MyHttpComponentsClientHttpRequestFactory(
                    httpClient);// httpClient连接配置
            clientHttpRequestFactory.setConnectTimeout(properties.getConnectTimeout());
            clientHttpRequestFactory.setReadTimeout(properties.getReadTimeout());
            clientHttpRequestFactory.setConnectionRequestTimeout(properties.getConnectionRequestTimeout());
            return clientHttpRequestFactory;
        } catch (KeyManagementException | NoSuchAlgorithmException | KeyStoreException e) {
            log.error("createClientHttpRequestFactory exception,msg=" + e.getMessage(), e);
        }
        return null;
    }

    @Bean
    @ConditionalOnMissingBean
    public RestTemplate restTemplate(HttpComponentsClientHttpRequestFactory requestFactory) {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setRequestFactory(requestFactory);
        restTemplate.setErrorHandler(new DefaultResponseErrorHandler());
        return restTemplate;
    }

    @Bean
    @ConditionalOnMissingBean
    public HttpRequestHelper httpRequestHelper(RestTemplate restTemplate) {
        HttpRequestHelper httpRequestHelper = new HttpRequestHelper(restTemplate);
        return httpRequestHelper;
    }
}
