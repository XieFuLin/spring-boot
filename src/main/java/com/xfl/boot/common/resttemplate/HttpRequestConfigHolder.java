/**
 *
 */
package com.xfl.boot.common.resttemplate;

import org.apache.commons.lang.StringUtils;
import org.apache.http.client.config.RequestConfig;

import java.util.HashMap;
import java.util.Map;


public class HttpRequestConfigHolder {
    private static Map<String, RequestConfig> requestConfigMap = new HashMap<String, RequestConfig>();

    public static void configTimeout(String url, int readTimeout) {
        if (requestConfigMap.get(url) != null) {
            return;
        }
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(readTimeout).build();
        setConfig(url, requestConfig);
    }

    private static void setConfig(String url, RequestConfig requestConfig) {
        if (StringUtils.isEmpty(url) || null == requestConfig) {
            return;
        }
        RequestConfig.Builder builder = RequestConfig.copy(requestConfig);
        if (requestConfig.getConnectionRequestTimeout() <= 0) {
            builder.setConnectionRequestTimeout(500);
        }
        if (requestConfig.getConnectTimeout() <= 0) {
            builder.setConnectTimeout(3000);
        }
        RequestConfig myRequestConfig = builder.build();
        requestConfigMap.put(url, myRequestConfig);
    }

    public static RequestConfig getConfig(String url) {
        return requestConfigMap.get(url);
    }
}
