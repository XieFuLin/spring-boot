package com.xfl.boot.common.utils;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * AES 跟 RSA 工具类
 * <p>
 * 生成密钥对 解密unionrsa.js加密后的密文
 */
public class CryptAESAndRSAUtils {

    //private static Logger log = LoggerFactory.getLogger(CryptAESAndRSAUtils.class);

    private static final String AESTYPE = "AES/ECB/PKCS5Padding";

    /**
     * 随机生成AES密钥
     *
     * @param keyLength 需要生成的密钥位数
     * @return
     */
    public static String randomKey() {
        String key = null;
        try {
            KeyGenerator kg = KeyGenerator.getInstance("AES");
            kg.init(128);
            SecretKey sk = kg.generateKey();
            byte[] b = sk.getEncoded();
            key = Base64.encodeBase64String(b);
        } catch (NoSuchAlgorithmException e) {
            //log.error(e.getMessage(), e);
            System.out.println("没有此算法。");
        }
        return key;
    }

    public static String createSignatureAESForParameters(String privateKey, Map<String, ?> paramMap) {
        if ((paramMap == null) || (paramMap.size() == 0) || (privateKey == null)) {
            return "";
        }
        String content = getParameterString(paramMap);
        return AES_Encrypt(privateKey, content);
    }

    public static String AES_Encrypt(String keyStr, String plainText) {
        byte[] encrypt = null;
        try {
            Key key = generateKey(keyStr);
            Cipher cipher = Cipher.getInstance(AESTYPE);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            encrypt = cipher.doFinal(plainText.getBytes());
        } catch (Exception e) {
            //log.error(e.getMessage(), e);
            // logger.error("AES_Encrypt异常：" + e.getMessage(), e);
        }
        return new String(Base64.encodeBase64(encrypt));
    }

    public static String AES_Decrypt(String keyStr, String encryptData) {
        byte[] decrypt = null;
        try {
            Key key = generateKey(keyStr);
            Cipher cipher = Cipher.getInstance(AESTYPE);
            cipher.init(Cipher.DECRYPT_MODE, key);
            decrypt = cipher.doFinal(Base64.decodeBase64(encryptData));
        } catch (Exception e) {
            //log.error(e.getMessage(), e);
        }
        return new String(decrypt).trim();
    }

    private static Key generateKey(String key) throws Exception {
        try {
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            random.setSeed(key.getBytes());
            KeyGenerator kgen = KeyGenerator.getInstance("AES");
            kgen.init(128, random);
            SecretKey skey = kgen.generateKey();
            byte[] raw = skey.getEncoded();
            SecretKeySpec keySpec = new SecretKeySpec(raw, "AES");
            return keySpec;
        } catch (Exception e) {
            //log.error(e.getMessage(), e);
            throw e;
        }
    }

    public static String createSignatureForParameters(String privateKey, Map<String, ?> paramMap) throws Exception {
        if ((paramMap == null) || (paramMap.size() == 0) || (privateKey == null)) {
            return "";
        }
        String content = getParameterString(paramMap);
        return createSignature(privateKey, content);
    }

    public static String createSignature(String privateKey, String content) throws Exception {
        byte[] digest = DigestUtils.sha256(content.getBytes("UTF-8"));
        byte[] encryptedDigest = privateEncrypt(privateKey, digest);
        return Base64.encodeBase64String(encryptedDigest);
    }

    private static String getParameterString(Map<String, ?> paramMap) {
        List<String> keys = new ArrayList<String>(paramMap.keySet());
        Collections.sort(keys);
        StringBuffer sbContent = new StringBuffer(256);
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            sbContent.append((i == 0) ? "" : "&").append(key).append("=").append((String) paramMap.get(key));
        }
        return sbContent.toString();
    }

    public static byte[] privateEncrypt(PrivateKey privateKey, byte[] content) throws Exception {
        if (privateKey == null) {
            throw new IllegalArgumentException("加密私钥为空");
        }

        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(1, privateKey);
        byte[] output = cipher.doFinal(content);
        return output;
    }

    public static byte[] privateEncrypt(String strPrivateKey, byte[] content) throws Exception {
        byte[] keyBytes = base64Decode(strPrivateKey);
        return privateEncrypt(getPrivateKey(keyBytes), content);
    }

    public static String privateEncrypt(String strPrivateKey, String content) throws Exception {
        byte[] keyBytes = base64Decode(strPrivateKey);
        byte[] encryptData = privateEncrypt(getPrivateKey(keyBytes), content.getBytes("UTF-8"));
        return base64Encode(encryptData);
    }

    public static byte[] publicEncrypt(PublicKey publicKey, byte[] content) throws Exception {
        if (publicKey == null) {
            throw new IllegalArgumentException("加密公钥为空");
        }

        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(1, publicKey);
        byte[] output = cipher.doFinal(content);
        return output;
    }

    public static byte[] publicEncrypt(String strPublicKey, byte[] content) throws Exception {
        byte[] keyBytes = base64Decode(strPublicKey);
        return publicEncrypt(getPublicKey(keyBytes), content);
    }

    public static String publicEncrypt(String strPublicKey, String content) throws Exception {
        byte[] keyBytes = base64Decode(strPublicKey);
        byte[] encryptData = publicEncrypt(getPublicKey(keyBytes), content.getBytes("UTF-8"));
        return base64Encode(encryptData);
    }

    public static byte[] privateDecrypt(PrivateKey privateKey, byte[] encryptData) throws Exception {
        if (privateKey == null) {
            throw new IllegalArgumentException("解密私钥为空");
        }

        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(2, privateKey);
        byte[] output = cipher.doFinal(encryptData);
        return output;
    }

    public static byte[] privateDecrypt(String strPrivateKey, byte[] encryptData) throws Exception {
        byte[] keyBytes = base64Decode(strPrivateKey);
        return privateDecrypt(getPrivateKey(keyBytes), encryptData);
    }

    public static String privateDecrypt(String strPrivateKey, String encryptData) throws Exception {
        byte[] keyBytes = base64Decode(strPrivateKey);
        byte[] content = privateDecrypt(getPrivateKey(keyBytes), base64Decode(encryptData));
        return new String(content, "UTF-8");
    }

    public static byte[] publicDecrypt(PublicKey publicKey, byte[] encryptData) throws Exception {
        if (publicKey == null) {
            throw new IllegalArgumentException("解密公钥为空");
        }

        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(2, publicKey);
        byte[] output = cipher.doFinal(encryptData);
        return output;
    }

    public static byte[] publicDecrypt(String strPublicKey, byte[] encryptData) throws Exception {
        byte[] keyBytes = base64Decode(strPublicKey);
        return publicDecrypt(getPublicKey(keyBytes), encryptData);
    }

    public static String publicDecrypt(String strPublicKey, String encryptData) throws Exception {
        byte[] keyBytes = base64Decode(strPublicKey);
        byte[] data = publicDecrypt(getPublicKey(keyBytes), base64Decode(encryptData));
        return new String(data, "UTF-8");
    }

    private static PublicKey getPublicKey(byte[] keyBytes) throws InvalidKeySpecException, NoSuchAlgorithmException {
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey publicKey = keyFactory.generatePublic(keySpec);
        return publicKey;
    }

    private static PrivateKey getPrivateKey(byte[] keyBytes) throws NoSuchAlgorithmException, InvalidKeySpecException {
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
        return privateKey;
    }

    private static String base64Encode(byte[] data) {
        if (data == null)
            return "";

        return Base64.encodeBase64String(data);
    }

    private static byte[] base64Decode(String base64String) {
        if (base64String == null)
            return null;

        return Base64.decodeBase64(base64String);
    }

    public static Map<String, String> securityAESKey(String privateKey) throws Exception {
        Map<String, String> keyMap = new HashMap<String, String>();
        String randomKey = randomKey();
        keyMap.put("randomKey", randomKey);
        keyMap.put("securityKey", privateEncrypt(privateKey, randomKey));
        return keyMap;
    }

    public static void main(String[] args) {
        testSyncOrder();
    }

    public static void decryptData(Map<String, String> paramMap, String publicKey) {
        String securityKey = paramMap.get("securityKey");
        String securityValue = paramMap.get("securityValue");
        securityKey = URLEncodedUtils.decode(securityKey, "UTF-8");
        securityValue = URLEncodedUtils.decode(securityValue, "UTF-8");
        try {
            String AESkey = publicDecrypt(publicKey, securityKey);
            System.out.println("RSA解密AES的随机秘钥:" + AESkey);
            String context = AES_Decrypt(AESkey, securityValue);

            System.out.println("AES对原文解密：" + context);
            System.out.println(URLEncodedUtils.decode(context, "UTF-8"));
            System.out.println("-------------解密结束------------");
        } catch (Exception e) {
            System.out.println(e);
        }

    }

    public static void testSyncOrder() {
        // RSA 共钥
        String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDBHT4KN7kbIsxRhjhYq5ywCQ9On0bQHfh1XK8SCh6Mm51SyCmwg9krymi6bZUScQ9RfehKrXMTtSvgb6AuY5WJ26yKU5269x3lPguYbWoP+3nCty+w7YGHGM68cKpSHXRqmM1Iu3i+jOIbjDdE3k0+uIevzuKWUpXJyVfOQK0+XwIDAQAB";
        try {

            System.out.println("-------------解密开始------------");
            String securityKey = "Gz9VZiA%2Bw7GystTgeutkSyksb34yHssH4YIhx4DTVGMRPAdkVUnVCVgvLIakEmcDsZCF7kPifk%2FW3oPhn4gGiJgROZ0%2BYAyrc4kmxyyu0uvryXZpaikxL08IhZEmPunXXVIF2SJh4cnhWN5zMahwGetblxi9qOmCHyDBcqMV%2FuU%3D";
            String securityValue = "4Kwewbm8TWkubGxptO69I7WX34TWRHsRLNwQJilLDrmpeyX37DEhyx5Q6zneb4iZNp96si%2BuGoI4fhxX7VnM%2B3QBKTAsaWNwV6eO4vRv0xzn4dBwfCyduQqowOGhYZ43NuATH2Ztsw913dqyyB4cEr3NMi7bgNuVgIWS8NuZ2ZZxxJl5iYrrEvOH%2FWP8oYLdg4TVelT1nwltXnscgpt33b4NIZ8ear5dL9PXActoO3QWZQDGx7j4z7Hk%2BNBpXpoN6K66H%2Bf2%2Bo4E4AstYx%2F1ADRtJlH0N5lOhcwy6UsdTnwHDf%2Fkmaq%2B55%2B0zV1%2FELma%2FX%2FZAhrMfVIqVZxEsASk6TQF8yeqFBPG41SIoo790Fw9wfMPySmMo5Alb%2BYIsKBH06L6so9Hqeu5uj4nYnk2jo6bVEWv7wSk9GViHPfPF0LqUo5%2Fd6ZdqkokSkJsWGM8TgPF3Fhci1mT0QY0TEz%2FEFe9d%2BC7XhNvg31hMl31FTgf4cSuRM89PNcbsD7OneI1U8D6O%2BNxr76hpAc%2Bkv13ZDFA7EjTIlASbkF5ihnhejIqasDC6Ym%2FQB6yFUJxgHyM1iDXS7CUcWE9saeXCVlWxEviUaZYPYcAQkS%2BVB%2Ff1Y4hwF1YziZ6EIklRH0Knu3j39b%2FHRA1CXMHqRt3Ks37O%2BxwG4YKh5A4WpxeJUHNusefeiSD0uROlt%2BiL6BBVhSwnzil4fGqG8QrjUbTz008a30F%2FsAsxaKCsjUc3iQYIajXYvxBRkQM42lEuqveGfmiFV1r1%2BdECjSxv78g3gaUPpAGIF24xQAg9K5EWxxJaLRuIeyKC5%2BcFBXobJemrTm1s0Acg5V4mjTxfmro854uVu7p%2FQ3yWfR7SdMMhhgGNdHTTcZf9fqIoN5BtEY0dBZUdOdDRMxxISmvQRD9J63xx5HrVFsLO%2Fd%2BLEHnvd%2F4XVzRCXA42rz9cMRTmaZusOr0dwDn3PB22JDaZDTVOueVyX6yxKXrfgzTBNUAhTwKAZ91dnE4k6ThxsEh3H6ihz2%2FD9gT58QJaQePyp%2BjOGcqpqL2B0V0NZoU2bGxszjn87oNCjuEBiht%2FpzHS9AU99XiaodAA12X4qjZ13iGE3ZMIC11Zcs%2BhjEM%2BLfbybqZ%2BEB5YtbH4wY6XiFAcPtuRSAfrbh5wwvT7z11dFz2Tc%2B9A15%2F6J5X4Yd1PBH%2B1viPD36g%2F%2FD0IrR8iOPheMW%2Bbpr4u1ZoAebUpCrpjtsS3%2F7%2BRwjNyexnOg0hUzJGcdmtfoOnntaOwyRFBz8ZLk0NbZPBREk9wnae6UcDVeWVePqT781RNmCOKCk9Q0QjoVE0zNBvdx%2FUmQfeth1CK4ux99lWACGKbsYV8CexAZB5r4Zo0jxCZaAf0D4JWhXiuHuUpsLv%2Fd9dp6R0LZGmVFyadEydWXEd1guBCteK5eBkDaltbgZFRd%2FDEVE%2BtJ%2FdD3zMCjjTTcZf9fqIoN5BtEY0dBZUZnt4WXuAYtMV%2Fr3MK8S%2Bm5HrVFsLO%2Fd%2BLEHnvd%2F4XVzRCXA42rz9cMRTmaZusOr0dwDn3PB22JDaZDTVOueVyUPFS1LiK4EekTwIBPUKGN%2F8LnLBahdxqilFcoxM1Qmf03EFLAsTP990JiI478AZf8s%2BZ03%2BkYa1X%2BmY4d72Og8%2FmxKh6K5rkh44QPi42XrsRE8%2BXFwBhszL20P1FfYWT5dHAFL69CLkEpMLoJagj%2FiyEw4IRT0WCRxRY0QknrNBFRqEr3JO0S7qHNS%2FxGiD5cvpuEaT4VTKkxZtCe60EqJN0Y7d8ceGht4ElcpSQu2nsmvn35maWV%2F44du7HIgJWLAmM%2BjIqZoiei%2Fr0WpitAFzaoSrUZTDcneI7vlHDfN%2BWNUvaylhCPPKLzMbtFP0bWdeKh6Tl76kaHR5B0kov20h2NKQLDxU54zRnabNrdDNTqlw%2FAP77AEQqT%2FnLNxT15DMO5WMdGDaBWY7B7AnjuT1lxhDS8TNIjDa6tM5AM0mP5xRpXL0CgFutHVZk0g0je3F6G9sePTS8023mW3Krb1bKn28Sp0vemqLuf3M7k8zq1HTKHfF58HGwcwf1k%2FwpTAE48aSRTDXLRsn2ltfIsGq7jDo4GHgj%2FlPCz9wHI%2BRk52vSpTnIFFG0efq1qLsJS%2FM8covzoZ64KE6u7AzzMr0B9dn0hD%2Fv2IOs2UvIaoqkRka4Ie8%2B8hNbeFlNSLAm8ionV5nKfdtwEuWUktWVmuNLCDwRyC7zTiFesXa2OeyveTCvW%2FE6rp%2BPfMnVR4RMv2ffDq3BvxSDREFLGczqhRs%2BPeMYcgdHqR8NChHRTFTkGDduc%2BkX%2FX9gl0RZqY7SB5ayTH6S2iv2pyK6FV1wQD7%2FSOkxhyUKRCeqUc3Y051jAeGVFPLWOX5byYruFJU1N6%2FP4ymmwLXCttIE9gSYC3dyZBVfRVY9WCqChEYRWuiEdKOYbAV0eqfoFBgoszqls2nsj27SnK9H5lEYlrcVv7GwBWzjFkE4as9Lw8GbVGpIAWpdffr4WRgwl6PjsPXPfgI%2BU4RpAYu2lGL7di%2Bc4T6P6O7Kg4CQlZgu7Ugp0bAQwJoJuTB9veJPBvecVJvr78TvPUkt2SZ1AHyYPuVaUIwK7EpshG7C3KOQkd%2FTU3WRqS1u2cGbDHhINHarZoD4E8UzXpy66O35rox84mlSF%2FRQghq1DB%2F%2FL%2FBH7qUsXwe%2Fz%2FxkCawJDB5Rz9lyeRPCumkTA9SPNxjk8ybShSaMBBFFTT7cegb2L7%2F1AFnBTAMtoBJfnqPzJkeIjdmd8Tdy6aDCx7wzU3Ok8HGZTpBOGOGXmdjjpoRwGFDMVglmv6G0cDIBABKrXsZGNS3R2UUzS%2FM8covzoZ64KE6u7AzzMpAiZKPhy2jb6yTimHnCwmYGD7n4CyJbCTxsG9Lhig%2FF57TztzQGyDT944x9EghxL2NLCDwRyC7zTiFesXa2OeyveTCvW%2FE6rp%2BPfMnVR4RMv2ffDq3BvxSDREFLGczqhRs%2BPeMYcgdHqR8NChHRTFTkGDduc%2BkX%2FX9gl0RZqY7SB5ayTH6S2iv2pyK6FV1wQAukO5714lNqZ0ycnajmVbGsOG4E0b7Fj4DyKV6qP04O72DvgAb2UdnZdnKiTFE0FfcmPDzfWYfIOqCKKo5qprIg7nEuF3tde3FcmOOY%2BCRCpBNyiLTxovVQRu3%2F2IaJdpc0Jo892syWSpHd7GoJmB%2FknmcSz5W5lPwKqOVWcdDcgkSx6syHpcUnvU%2BnGA01b7Hr2WFUtuAwvtNsilAlM9B5GmH%2BW0%2BHN2p6M7%2BUHd5CFMN%2FN1PyCmvDNzoNPN490DxT%2FoPoYvRMSwwelmqL4WSQf%2BB7Ei9huDEYIlpmU2Yue%2FhCyIhK1l1BTTZ3fYWpiVL72%2FMO%2BbwDtHdn0Mybv4R2BG%2FFpNgOrcoHkyffVg1kgqDxjRdS%2BuWw8FthMeNbuNHKaztLXg0ku%2BL5AqcPOBdoZYtXxCgHn2XUeavkut3D8X3ytmOTkWm6K5qPaCqksN5QpzAppO9L370D1tJj%2BJuVJO2djyD2qYxYYunMFzWXUfy1pTvOC6RzwDxqyzFVUxbKn28Sp0vemqLuf3M7k8zJzpAS0297Al4DvtKzE%2BzPgAhim7GFfAnsQGQea%2BGaNI8QmWgH9A%2BCVoV4rh7lKbCzDZkOWA3WAGSrj%2FMuySSPlAs5%2BKe%2FRLXlWFUq4y7bp3Pd1Rb5Kucaztq%2BLs51G%2BxJoGfoY5UDRB3%2BxGRUDk2P4Wzi44xKSHb%2BDVtalNG%2FIsah3dLaL4JXq%2F%2FmCaII1JC92EaoD9faHq4p7KRLF%2FG3E2f1BYasRNRD82ap2SvUYsVuW6gddcp6SsEZLJUDPtwzyQ2EmqHgVwweRiNm9819BMBAhKimL6XKm6vksCnKugmPvkIu%2BsJYpww0mfnVr22IRKzcX0fbZsc%2BN1zapGbB4Dd1WOKF3QCnZVoFru4nMOKA%2Fhu8RPK%2FyKqfsNU5oQam0Ggg48qlfB84Lins1ttMbVq0ZN%2FyDF89u%2BV2XrsNcyXYi1KK42I%2FVWrlOmUtZ155GmH%2BW0%2BHN2p6M7%2BUHd5CDVrpWTiXPIEPzMplPkSnnkHPIfPHh9QxfhkE10LwHsEdycVGPrlOqvnLii45A1E2VxqoKuhQJuUCK3iGD8zreF76rl0dPczWKO%2Fm4TW%2FWLUogJUFUIxj%2BMv4a7dtrY5Jm93H9SZB962HUIri7H32VYAIYpuxhXwJ7EBkHmvhmjSPEJloB%2FQPglaFeK4e5SmwqEAD7J3Sy8aStHmF%2BqvOdpZcR3WC4EK14rl4GQNqW1uW%2BIDqcUypbtnPZqmx13oSNNNxl%2F1%2Boig3kG0RjR0FlR%2FLHjDMUXmLoskE54UQys0ketUWws7934sQee93%2FhdXCfRWeroL4Mc1nOYzovhaqu1wV4L1pfz3aq4kSTrmZe207bzTrE2ZgYm12MdG4ulm1jA1j06hCQA6I%2BxJwExhD2WY7JRiWNAMqt2HGAC1YW1nEJKelmXnQ%2ByaYV8f5%2FUncFoW1c6uS1BrgOK1e2BcELJLIVw2chUeX34XawSw%2Bwyct5BcDVHluVY4pdOZjFjyAaC%2BrCBCUq8JNZcDYaH9C1B%2F4HsSL2G4MRgiWmZTZi5untwTETr8TzzGMQOIMjzSurgwm0PjoI%2F39wzkIBJ%2FC6ujDBfliGbKzkvIGZryDIHqN0buDqfTG3iPuGLQFhQ0HsvdBZEhkJf9AFEuJu8z0tUHHXqBmsalAuii%2BW9xvJMnPfP%2BuB4ZOh%2FnkcwlluXNW4yO8Vy%2FrvPi%2BoMF%2FN7xCojDYqEE%2FlkswYsnfooNlfznJs%2BEqXw75PC%2BKuaahHJSNGJovDYZOgxyqGLmSwjS2oOsY%2FVes%2FfqRzJn3ho4emITcQEQfxbNgdiRaFDY8CQgZj5QVPc3l8w0q2kMnknFDUW3NduglikkffsYyDyIIA7MI9wyPgwe7pWLo0qjN4NmvnVFhpZnJYCHS3tIZgsBJ5LUHF%2Fa1U5B%2FhyxnnjPHq1%2BjwnA3czYxpAmqqNmIyYln65TgjSDCEf6xWwhK1gkojUgZ%2B1IHVfc8m8tnm9W6thdnUkxYHIV0EIyhA3ytdbzJkcYbQsVgmyR7JA1sC00H5ITvsZsRlneSlSDko%2Bb6pkF6YqAeW3FoRacGUoOfBLlbl%2FeVjZfh7jJzk7PFr62pwJ88GZxj3GmO8eKoy9pfDySjXAUo7fAj2lqL7HWWeE5YC7HeMPYJK4CfXZbtu3MRFzjZwQLwFx3LyGIM6zLqjd%2BtUWJMR5z8455iG%2FA7h5X3yO8PEzrocJuBMliWlPq9yeRDgMQboz7g4wlx7HUHVLAkhMW11QDG0H1%2BllPcaZJnTnQ0TMcSEpr0EQ%2FSet8cfGjyFlRgptux2T2ObkA7irA0a3uK1c8A2RnVY65mp7rqwy9EvoI%2BJYWIsPxudSGZHtusek3s7slHYJipQMKXuldIEviLcwznk78a7DJRM9Zs7d7BwouAdoNGkr0V0DwVTiQ8ZVtqOtaBggzpH%2Bi9m7AzSM0O1yL8MXlUuBahvRhmOU8NFQT%2FDP1jUq7NqvnnJi%2Fgxz6RZXI2B8ry0gKXTOc42cEC8Bcdy8hiDOsy6o3frVFiTEec%2FOOeYhvwO4eV98jvDxM66HCbgTJYlpT6vcnkQ4DEG6M%2B4OMJcex1B1SwJITFtdUAxtB9fpZT3GmSZ050NEzHEhKa9BEP0nrfHHxo8hZUYKbbsdk9jm5AO4qwNGt7itXPANkZ1WOuZqe66sMvRL6CPiWFiLD8bnUhmR7brHpN7O7JR2CYqUDCl7pXSBL4i3MM55O%2FGuwyUTPWbC7ji9okZUKJYActSyNStVyJZ0v3ic9o844lUSvkJXsSMN9tXh3SfAU%2FivetxBNB5OuMIbLYhEEwU6eepNrO8aSwatv1IzW0dkVgY9RGf2lY%2FzKZWiSpxaq%2FzjY05Fzi2i6OYDI1WZWQZMdtsd5rFvCkSA7nsvl4zMrj9mTRVAX2iXVcP%2FI4kr2aJPottqmXFX2XPoAgF%2FpPadmESBQUEd97mZTo8rknjWy180VDk4r%2FT5EQPH89ypgzH%2F87KYelJk1bvi7qO3p1E2hnaPIgZGF%2BlgLargnbDPSzTODiMaxoFROFVE7ECvpuRAsJWeI1bHB8HFmSjyKJ1lwbfPoJlli6PK8POuPszwitaPh%2FodGM4ZyW9jht46bMXUSZahhzTNXXf%2F4HbqMMpym6P9Sk7ODgrcXdrRHo3bXxYRYMTwUzcP7lpZdsjEbcC%2B5CXdqfVQa18mL0r2qYMrqzZcA1EJQ%2FbFqVYPQ%2Fggl1DJ7UFU1O80HvHqZEAhIFAnYsNV5v7jPkkWwfgZX0OeJrkqbSbFonISJyviOBMgETdcBFeFiVODZEySnOHved%2BjzS0m%2FtF1YWaMjY%2BpOdk80HPkoJQ8N95MZHZviypjqQOHZHp60V4FpREMUHT%2BLVUlZ4U7HItyVox4h6KEuJSxlj0Si7D2ObbFlpNPwwClnuH3PseKnMJ6xzDQTqr1wAbIjo8OFxFjEOz47A4tOQwIRfO%2BZalGtY9WtCTpe1nmwqoQPJrHeXIeyNfECMMgvAbVioVWDByBOuWJAWhrK47vPlhkcMce6E%2BuVVPh2qtJn7eeVWjsp7hP%2FMPJuC6JFIksdWxQogAfBOhpAx44Jn4pbuxPJYAfen6Nze2JEwCZm42qHI9lkgKOKNTpkcVPXiR5AIDHjCwm1TDjQU1XIm2be8PAG9YFjTlQPGwHgnc%2FwNa05UMEahO7U0M7Y81o%2FHQ2gHeevyT33pQeKom0x%2BMbUg9lwMkMJlYEhliOUygETrg%2Baha5A08weXjxR%2BH4DI5wd8pem2t7hIaaqJWYOtoeKe7M6UJFMHNvMCpByk5Hu2ATgBlVLbk5gX6RByIEPiSEVaXjwnC9mDLFErvv2e4oB3nfgtWpRsN0FTY2eA%2FLIUidgW90BCD9KarjofVMeYt1LkXT%2BFF2yU9k1eBRCwVwvOo553iTN6wqehkXuLkbeRV2MJPFC%2B%2FzDK8Xd8LlnW8njcx7%2FSFvdx%2FUmQfeth1CK4ux99lWACGKbsYV8CexAZB5r4Zo0jxCZaAf0D4JWhXiuHuUpsK5ynW%2FXXsq34uG5Xw74CV6WXEd1guBCteK5eBkDaltbh%2F9Qmx%2Bn4XtPQS8PpJp9geOtgLONeDTytzE2Of6EqhpekqMEnIAmhlCD5ZKidIHaljVL2spYQjzyi8zG7RT9G1nXioek5e%2BpGh0eQdJKL9tIdjSkCw8VOeM0Z2mza3QzYeLaphE48Y1oo0rfyTEjji45ITvJqPqRCILb5m1jUex2lr8PzE6fpEKhbdb%2BRotvj%2BGbTTkwgCB0c2SSdTl22MN0wdafPEM7QyP6Rw3KilKDQSQfVYz7JwcOrAjS52fLReVjyftglMg7KFW%2FomdTzppz50ecNEyaXKUjstZAFCm6%2FVZcjd4nuJFZmLjc3pz8FwYlt6Y5%2BNv9L3vo3nKDg8V4f3dZp5vD4fbQ6RYpNSiRsN0FTY2eA%2FLIUidgW90BCD9KarjofVMeYt1LkXT%2BFF2yU9k1eBRCwVwvOo553iTa4iOP9OV6OT0KkHzoJXggtgKaD6WhOT6k0IlHfGOQcY0omesIiiluVqqdNzH6nQxRRU0%2B3HoG9i%2B%2F9QBZwUwDOrFmGOkYqLa9V994dBai1A7nyzpA9A0uLI54IgIGV%2BH%2BD7vbN2iQqKI6H3wDhr02ljA1j06hCQA6I%2BxJwExhD2WY7JRiWNAMqt2HGAC1YW1nEJKelmXnQ%2ByaYV8f5%2FUnQ9jJe2anvY2VE1CfnR1lSnJLIVw2chUeX34XawSw%2Bwyr7Rc6SrX%2FNic4%2FfFCbzYrBybMintczLNHOSx147eoNtB%2F4HsSL2G4MRgiWmZTZi5xJV12HLhwvMaP4tLCuKgFEcprO0teDSS74vkCpw84F2hli1fEKAefZdR5q%2BS63cPxffK2Y5ORabormo9oKqSw3lCnMCmk70vfvQPW0mP4m5Uk7Z2PIPapjFhi6cwXNZdEcnwGlTj4ZOLblOd3E0J0RYcelCMecrZl7wYHeXbqoxOcG5N28t6O9mXhr83SBZYx5Y4c1ZUTth8mrLgHcF0%2BWgc3oq0XCzFDqqwj4LtoQd0GW522BGcXJ8YQj7%2B9YDlp8Vg7Kr5%2Bqg7E8aQ3ehk5%2BRph%2FltPhzdqejO%2FlB3eQgKy%2FGuJoWyozSdI%2B15oCi0AwZeZ%2FOWpS45DgNlocdceTlSaA7p7XrjYoBK2xfRafojSa1FuClvDxfHA9fI7OCoYsYb7DqnD0hcRELuFInQ1eJu5FKgaiXqd3HRKx37SNMDM4QSkj4J5RQxAXrrjfdOGvneA0gdU4tqVf9ePJi1Vphp2RyG3Mm8Wy2ykQ5pLa8oDQRJycyOliqgT1hefaP6toBJfnqPzJkeIjdmd8Tdy6aDCx7wzU3Ok8HGZTpBOGOGXmdjjpoRwGFDMVglmv6G0cDIBABKrXsZGNS3R2UUzS%2FM8covzoZ64KE6u7AzzMo3hcEVqP8SM7zRbf8HZfdtDSkQFBT6U%2FJW1MUqvLcIi9jue3rXcmuIaKaZm2ohKFBM8I6%2FXt1rSZCPwPIPTsMrO7Vmqd5uap79WHufg5VQc3rpH%2BFSMNkdPxtFLvDw%2FvHZHK2DJTFz6AwXoYq1nHjthay0F836ZDRoeiTb2FaMZ9xK4FKg3Ygsa0SEkPqCV9N0iTPWFZZBTvtrfrp%2Fjk35xkLKa6qj9%2F%2FnNwkCaXj187IkaA6klr8Eguns57QI4BB1dnE4k6ThxsEh3H6ihz2%2FVboGMkeDL5CJJwx3%2BBmQM5J5nEs%2BVuZT8CqjlVnHQ3IJEserMh6XFJ71PpxgNNW%2Bx69lhVLbgML7TbIpQJTPQeRph%2FltPhzdqejO%2FlB3eQjPgOwPQE15oDQsID9EiGlXAwZeZ%2FOWpS45DgNlocdceSf%2FNePYVdOVDhXc5PosRecjSa1FuClvDxfHA9fI7OCoYsYb7DqnD0hcRELuFInQ1eJu5FKgaiXqd3HRKx37SNMDM4QSkj4J5RQxAXrrjfdOGvneA0gdU4tqVf9ePJi1Vr7mtEH4JW6DQZt9Bo2144zKl2mfSW8d85SX8rS1ObaZcnsMgyyhMd2twNxFLrty03osbm5XymjAROzNm%2BnPRrEcCNyU95W4OYLwEUSFJ5Vf21Fe%2Bd9wLqK2cim%2FqvKt%2FNUijwWLu659qV5a4W5u%2Bt0wBOPGkkUw1y0bJ9pbXyLBNVnHfluaqvat7g8ymkbXQZf1t8At6VMIAhsuShMTe3CIOzfSi88efRT2NlaFNRoGkrlYbrB1akHPqmED7EpP%2BT7z52EmOBEEroixjy2pVt5ek%2F6x73xrXU3BWZ5c5wp7Hm9NqOpCUdCajYZ7iRoXcVyHUV2qV4r2oEEdW1hX9zOsWOnZOASkO4DLQC55%2B0fHvUmfEwXdl4LXyjghJE335eHDNKsfj2Nh39FDdBMTybNYL80pkz5jRGSq48bdYyh55CxItcZrpen0Do3nYyQ2NQj3UKNpQMOZchWaFw3YhYPoFE%2F8Nea%2BXdLdumtUQ2r9hd7ptM1NzD%2BR8vQRHcSE4XfMKMfv3ENb7eifZG8h%2F%2BOuvzG8zpT%2FmVDxekGeGNMaDdMHWnzxDO0Mj%2BkcNyopSg0EkH1WM%2BycHDqwI0udny2rGR0jSgq2OC7xG27CzvykYr5Pdt2n8xoqWDw0CJ96F9GJovDYZOgxyqGLmSwjS2oOsY%2FVes%2FfqRzJn3ho4emITcQEQfxbNgdiRaFDY8CQgZj5QVPc3l8w0q2kMnknFDUW3NduglikkffsYyDyIIA7MI9wyPgwe7pWLo0qjN4NmvnVFhpZnJYCHS3tIZgsBJ788YZiGXNkTERwrLqDHhtwoPLH7oS86qDWUqHw6cOF7FBXlxPTV1sWWZ9XssOLWhwwtxIWYrnPfajIB8NmuvYEgauWcuukuCHau2hu52D3kr6t%2FwPUoazgm2Z6146nB8qk9ctcXCOZZOBfWlUAmZq1Z6BXA%2FYEmqT3H1OJzERWPE21BsIVNeXkWOCKIyf39cHkPMJaIJdwJ1w5xPBpMZqtwu2N71YfsRyuHGFIkP2HMzFSG7Zzaxs9C7LwMov77A0KkkdBEfBiTuiyWkquxs72rVkhwQ8085WcT5RDymFMmj7qpwIorL1EQt0xAoWpWkNad8PYwg4zlqXhBq0p5mW7mvKZlNye4%2B2rBLKEZ%2BrmvA3TB1p88QztDI%2FpHDcqKUoNBJB9VjPsnBw6sCNLnZ8teKRJrqqNOLjCJU1%2Bgy5mVmnPnR5w0TJpcpSOy1kAUKbr9VlyN3ie4kVmYuNzenPw9ZqQDh%2Fb4%2FJzshRx9mNyzEoTzJyQwcfxH3C8%2FwOPpTtGw3QVNjZ4D8shSJ2Bb3QEcUcnK5DWIOcRt38eSTncVytIljIYqAK6dDk1ioVH2KJSq4pilru55KJKFl%2BFj7Qcneqfp25Lg0cyoyjs%2FVved5ANWfTTrdd3m9uP1JbgpcYUEIdWpapbZWzR7wIaQZBLlAhNwuX29vcPv0r5teIf5ylAJed7Nywf8T88K2RVZpumlknCDfsEhPlIqTy6Ewo4PvPnYSY4EQSuiLGPLalW3vCe4bVLPH6WhPtFnV4GJoQeb02o6kJR0JqNhnuJGhdxSBCw18qCsLT3sR%2Bi4FZaElE5wqhipToHbh2fdGLqp%2BFYrGDfjJmRU03v8nBRirucccJD6zXZGg0MFOypJ%2Br5xpYoc7R3JAgSKrF3mOrYqMvk3boRUN%2BMMX3CpCnbisnsZK6jAibqBWyWrvvO63gh8LtA0SkKdXVk%2BRwMMXMxJIhdVs%2BAtRWs2XzPCK3Atf3xNXabPwzLjPh7HBtcSnfYLm4VSXMqTfLxAbZWLMoQxe1auNLndPrVOQSBDq5IJ2HD1aeLV30QHvBo0nOccaxTG%2BI0K83erCK6Yr4uGSDKyMw7002L1t8W2g%2BC5kpPuybppUh9SdFQFoxTBu9LMn16AfsGAgncQr7FT2nNseTPYuv4SNkQdDE9e8cGL2H3LOboGeDX6wYb2j%2B%2BVN8YoTQLZJ27jTdbJnKgNcwhOPnpIhkzyt6avpRVJVLXSUKyUJvNZL8OwJ1TRGqpnWcmpXFqtaH1Kjk4C8sKi1FVCqwZFQq47tA3XiEmgkQ%2F6rikHlTQCFHxeuw6HD0MDa%2B1gFChDdLLFLmsieor7M5z8bcnDPSmf4qIBVIdgbwvJ7jzl4nioLVoUmPDuk%2F3uuLf5Zvy0R3Ih%2BagY3G5dWzNzX%2BhU908qJmjk0msxzf7DiO8tqdKejPIwoQQTucfwlrmxnVG63gWFKGEZdsQjU07vo2PYR1gZiI1CA7ZUCgkoMDNFQfcMDCZetfxLZR7RV17RyoaE5I0QKIp4lyaDlc1iKzqjYEJCezrK0VTvVbuwEUCOrnJtJ14r%2BT6ODL%2FNiEUEEFDS%2BYWovqNtSYoeZ6z0%2FP%2FRTriaHPRXUlZTTO5l5Epd6ofkrpIv13%2Bwdi6MUGPrNbfigz10HLu5%2Bmk6tRufF5G5zHy6Z%2B%2BtuVvCC5gIgJhA7F9DW%2FveU6nCzi8lO8KXyx7MwdtxH1AGRl6Xbc1oS0XL3L5llR%2BlvkVWK%2FgA9ciBYJ9PKiZo5NJrMc3%2Bw4jvLanSnozyMKEEE7nH8Ja5sZ1Rut4FhShhGXbEI1NO76Nj2EdYGYiNQgO2VAoJKDAzRUH3DAwmXrX8S2Ue0Vde0cqGhOSNECiKeJcmg5XNYis6o2BCQns6ytFU71W7sBFAjq5ybSdeK%2Fk%2Bjgy%2FzYhFBBBQ0vmFqL6jbUmKHmes9Pz%2F0U64mhz0V1JWU0zuZeRKXeqH5K6SL9d%2FsHYujFBj6zW34qkVFjirPRB2%2FIheK6uVbWKO0Q5GYff2ou7aGbaH1mJtVPP9UlpJOHqny2UvHdFgCKluWpvtopElzlR9vSFARZ3%2Bqv5cSHhZLmMxKLZWOBp69Dil5bAQYIQ3mwBIFTfK%2FSISjJ2MrM0jMk%2FSnQ8%2BTuHTnShQIYzZIXnbdmjeV7s0N8X9XgjCLZByT9bytEgogo%3D";

            securityKey = URLEncodedUtils.decode(securityKey, "UTF-8");
            securityValue = URLEncodedUtils.decode(securityValue, "UTF-8");

            String AESkey = publicDecrypt(publicKey, securityKey);
            System.out.println("RSA解密AES的随机秘钥:" + AESkey);
            String context = AES_Decrypt(AESkey, securityValue);

            System.out.println("AES对原文解密：" + context);
            System.out.println(URLEncodedUtils.decode(context, "UTF-8"));
            System.out.println("-------------解密结束------------");
        } catch (Exception e) {
            System.out.println(e);
        }
    }

}