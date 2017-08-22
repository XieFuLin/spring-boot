package com.xfl.boot;

import com.xfl.boot.common.utils.RestTemplateUtil;
import org.springframework.http.HttpMethod;

/**
 * Created by XFL
 * time on 2017/6/17 9:59
 * description:
 */
public class TestMain {
    public static void main(String[] args) {
        String url = "http://icm.uat.52zzb.com/cm/channelService/getProviders";
        RestTemplateUtil restTemplateUtil = new RestTemplateUtil(url);
        restTemplateUtil.set("insureAreaCode", "441900");
        // String result = restTemplateUtil.post(String.class);
        String result = restTemplateUtil.exchange(url, HttpMethod.POST, String.class);
        System.out.println(result);
    }
}
