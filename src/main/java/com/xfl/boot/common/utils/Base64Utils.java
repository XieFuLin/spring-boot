/**
 *
 */
package com.xfl.boot.common.utils;

import org.apache.commons.codec.binary.Base64;

/**
 *
 */
public class Base64Utils {

    /**
     * <p>
     * BASE64字符串解码为二进制数据
     * </p>
     *
     * @param base64
     * @return
     * @throws Exception
     */
    public static byte[] decode(String base64) {
        return Base64.decodeBase64(base64.getBytes());
    }

    /**
     * <p>
     * 二进制数据编码为BASE64字节数组
     * </p>
     *
     * @param bytes
     * @return
     * @throws Exception
     */
    public static byte[] encode(byte[] bytes) {
        return Base64.encodeBase64(bytes);
    }

    /**
     * <p>
     * 二进制数据编码为BASE64字符串
     * </p>
     *
     * @param bytes
     * @return
     * @throws Exception
     */
    public static String encodeToString(byte[] bytes) {
        return Base64.encodeBase64String(bytes);
    }


}
