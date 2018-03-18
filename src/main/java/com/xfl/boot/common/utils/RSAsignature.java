package com.xfl.boot.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * Created by XFL
 * time on 2017/6/17 20:24
 * description:
 */
public class RSAsignature {

    private static final Logger log = LoggerFactory.getLogger(RSAsignature.class);
    /**
     * 加密算法RSA
     */
    public static final String KEY_ALGORITHM = "RSA";

    public enum SignatureAlgorithm {
        MD5_WITH_RSA("MD5withRSA"), SHA1_WITH_RSA("SHA1WithRSA"), SHA256_WITH_RSA("SHA256WithRSA");
        private String name;

        SignatureAlgorithm(String name) {
            this.name = name;
        }

        /**
         * @return the name
         */
        public String getName() {
            return name;
        }
    }

    /**
     * <p>
     * 用私钥对信息生成数字签名
     * </p>
     *
     * @param data 已加密数据
     * @param privateKey 私钥(BASE64编码)
     *
     * @return
     * @throws Exception
     */
    public static String sign(byte[] data, String privateKey, SignatureAlgorithm signatureAlgorithm) {
        String val = "";
        try {
            byte[] keyBytes = Base64Utils.decode(privateKey);
            PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
            PrivateKey privateK = keyFactory.generatePrivate(pkcs8KeySpec);
            Signature signature = Signature.getInstance(signatureAlgorithm.getName());
            signature.initSign(privateK);
            signature.update(data);
            val = Base64Utils.encodeToString(signature.sign());
        } catch (InvalidKeyException | NoSuchAlgorithmException | InvalidKeySpecException | SignatureException e) {
            log.error("sign exception,errormsg={}", e.getMessage());
        }
        return val;
    }

    /**
     * <p>
     * 校验数字签名
     * </p>
     *
     * @param data      已加密数据
     * @param publicKey 公钥(BASE64编码)
     * @param sign      数字签名
     * @return
     * @throws Exception
     */
    public static boolean verify(byte[] data, String publicKey, String sign, SignatureAlgorithm signatureAlgorithm) {
        boolean flag = false;
        try {
            byte[] keyBytes = Base64Utils.decode(publicKey);
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
            PublicKey publicK = keyFactory.generatePublic(keySpec);
            Signature signature = Signature.getInstance(signatureAlgorithm.getName());
            signature.initVerify(publicK);
            signature.update(data);
            flag = signature.verify(Base64Utils.decode(sign));
        } catch (InvalidKeyException | NoSuchAlgorithmException | InvalidKeySpecException | SignatureException e) {
            log.error("verify exception,errormsg={}", e.getMessage());
        }
        return flag;
    }
}
