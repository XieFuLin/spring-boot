package com.xfl.boot.common.resttemplate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

public class HttpRequestHelper {
    private static Logger log = LoggerFactory.getLogger(HttpRequestHelper.class);
    private RestTemplate template;

    /**
     *
     */
    public HttpRequestHelper(RestTemplate restTemplate) {
        this.template = restTemplate;
    }

    /**
     * <pre>
     * for example:
     * HttpHeaders headers = new HttpHeaders();
     * headers.set("channelId", "1");
     * User user = post(url,headers,httpBody,User.class)
     * </pre>
     *
     * @param url
     * @param httpHeaders
     * @param responseType
     * @return
     */
    public <T> T get(String url, HttpHeaders httpHeaders, Class<T> responseType) {
        T t = null;
        if (StringUtils.isEmpty(url)) {
            return t;
        }
        HttpEntity<Object> requestEntity = new HttpEntity<Object>(httpHeaders);
        try {
            t = template.exchange(url, HttpMethod.GET, requestEntity, responseType).getBody();
            log.debug("HttpRequestHelper-get|url={},params={},result={}", url, requestEntity,
                    t == null ? t : t.toString());
        } catch (RestClientException e) {
            log.error("HttpRequestHelper-get RestClientException,url={},params={}", url, requestEntity, e);
        }
        return t;
    }

    /**
     * <pre>
     * for example:
     * HttpHeaders headers = new HttpHeaders();
     * headers.set("channelId", "1");
     * MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
     * headers.setContentType(type);
     * headers.add("Accept", MediaType.APPLICATION_JSON.toString());
     * Map<String,String> httpBody = new HashMap<>();
     * httpBody.put("a", "1234");
     * User user = post(url,headers,httpBody,User.class)
     * </pre>
     *
     * @param url
     * @param httpHeaders
     * @param httpBody
     * @param responseType
     * @return
     */
    public <T> T post(String url, HttpHeaders httpHeaders, Object httpBody, Class<T> responseType) {
        T t = null;
        if (StringUtils.isEmpty(url)) {
            return t;
        }
        HttpEntity<Object> requestEntity = new HttpEntity<Object>(httpBody, httpHeaders);
        try {
            t = template.exchange(url, HttpMethod.POST, requestEntity, responseType).getBody();
            log.info("HttpRequestHelper-post|url={},params={},result={}", url, requestEntity,
                    t == null ? t : t.toString());

        } catch (RestClientException e) {
            log.error("HttpRequestHelper-post RestClientException,url={},params={}", url, requestEntity, e);
        }
        return t;
    }

    /**
     * 表单形式提交
     *
     * @param url
     * @param httpHeaders   请求头
     * @param requestData   请求数据
     * @param typeReference 返回类型
     * @return
     */
    public <T> T post(String url, HttpHeaders httpHeaders, MultiValueMap<String, String> requestData,
                      Class<T> responseType) {
        T t = null;
        if (StringUtils.isEmpty(url)) {
            return t;
        }
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<MultiValueMap<String, String>>(
                requestData, httpHeaders);
        try {
            ResponseEntity<T> resultEntity = template.postForEntity(url, requestEntity, responseType);
            if (resultEntity != null) {
                t = resultEntity.getBody();
                log.info("HttpRequestHelper-post|url={}", url);
            } else {
                log.error("HttpRequestHelper-post No Data Response,url={}", url);
            }
        } catch (Exception e) {
            log.error("HttpRequestHelper-post RestClientException,url={},msg={}", url, e);
        }
        return t;
    }
}
