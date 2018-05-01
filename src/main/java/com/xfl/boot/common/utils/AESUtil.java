package com.xfl.boot.common.utils;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import java.security.Key;
import java.security.SecureRandom;

/**
 * @author xiefulin
 * @time 2018年3月20日 下午5:14:34
 * @description:AES加密工具
 */
public class AESUtil {
    private static final Logger log = LoggerFactory.getLogger(AESUtil.class);
    private static final String KEY_ALGORITHM = "AES";
    private static final String DEFAULT_CIPHER_ALGORITHM = "AES";// 默认的加密算法
    private static final String charset = "UTF-8";

    /**
     * AES 加密操作
     *
     * @param content  待加密内容
     * @param password 加密密码
     * @return 返回Base64转码后的加密数据
     */
    public static String encrypt(String content, String password) {
        try {
            Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);// 创建密码器

            byte[] byteContent = content.getBytes(charset);

            cipher.init(Cipher.ENCRYPT_MODE, getSecretKey(password));// 初始化为加密模式的密码器

            byte[] result = cipher.doFinal(byteContent);// 加密

            return Base64.encodeBase64String(result);// 通过Base64转码返回
        } catch (Exception e) {
            log.error("AES encrypt exception content={},password={},msg={}", content, password, e);
        }

        return null;
    }

    /**
     * AES 解密操作
     *
     * @param content
     * @param password
     * @return
     */
    public static String decrypt(String content, String password) {
        try {
            // 实例化
            Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);

            // 使用密钥初始化，设置为解密模式
            cipher.init(Cipher.DECRYPT_MODE, getSecretKey(password));

            // 执行操作
            byte[] result = cipher.doFinal(Base64.decodeBase64(content));

            return new String(result, charset);
        } catch (Exception e) {
            log.error("AES decrypt exception content={},password={},msg={}", content, password, e);
        }

        return null;
    }

    /**
     * 生成加密秘钥
     *
     * @return
     */
    /**
     * 生成加密秘钥
     *
     * @return
     */
    private static Key getSecretKey(String password) {
        try {
            if (password == null) {
                password = "";
            }
            KeyGenerator generator = KeyGenerator.getInstance(KEY_ALGORITHM);
            SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
            //设置种子源 SecureRandom 实现完全隨操作系统本身的内部状态，除非调用方在调用 getInstance 方法之后又调用了 setSeed 方法；
            //该实现在 windows 上每次生成的 key 都相同，但是在 solaris 或部分 linux 系统上则不同
            secureRandom.setSeed(password.getBytes());
            generator.init(128, secureRandom);
            return generator.generateKey();
        } catch (Exception e) {
            log.error("AES getSecretKey exception password={},msg={}", password, e);
        }
        return null;
    }

}
