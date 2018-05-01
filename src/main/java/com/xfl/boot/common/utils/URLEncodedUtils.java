package com.xfl.boot.common.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

public class URLEncodedUtils {

    public URLEncodedUtils() {
    }


    public static String decode(String content, String encoding) {
        try {
            return URLDecoder.decode(content, encoding == null ? "ISO-8859-1" : encoding);
        } catch (UnsupportedEncodingException problem) {
            throw new IllegalArgumentException(problem);
        }
    }

    public static String encode(String content, String encoding) {
        try {
            return URLEncoder.encode(content, encoding == null ? "ISO-8859-1" : encoding);
        } catch (UnsupportedEncodingException problem) {
            throw new IllegalArgumentException(problem);
        }
    }
}


/*
	DECOMPILATION REPORT

	Decompiled from: D:\carnet\projects\iCore_pts_1.9.3\lib\httpclient-4.1.3.jar
	Total time: 29 ms
	Jad reported messages/errors:
	Exit status: 0
	Caught exceptions:
*/