package com.ytl.crm.service.ws.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;

/**
 * AES加密
 */
@Slf4j
public class AESUtil {

    private static final String DEFAULT_CHARSET = "UTF-8";
    private static final String KEY_AES = "AES";
    private static final String KEY_MD5 = "MD5";

    private AESUtil() {

    }

    /**
     * 加密
     *
     * @param data 需要加密的内容
     * @param key  加密密码
     * @return
     */
    public static String encrypt(String data, String key) {
        return doAES(data, key, Cipher.ENCRYPT_MODE);
    }

    /**
     * 解密
     *
     * @param data 待解密内容
     * @param key  解密密钥
     * @return
     */
    public static String decrypt(String data, String key) {
        return doAES(data, key, Cipher.DECRYPT_MODE);
    }

    /**
     * 加解密
     *
     * @param data
     * @param key
     * @param mode
     * @return
     */
    private static String doAES(String data, String key, int mode) {
        try {
            if (StringUtils.isBlank(data) || StringUtils.isBlank(key)) {
                return null;
            }
            boolean encrypt = mode == Cipher.ENCRYPT_MODE;
            byte[] content;
            if (encrypt) {
                content = data.getBytes(DEFAULT_CHARSET);
            } else {
                content = Base64.decodeBase64(data);
            }
            SecretKeySpec keySpec = new SecretKeySpec(MessageDigest.getInstance(KEY_MD5)
                    .digest(key.getBytes(DEFAULT_CHARSET)), KEY_AES);
            // 创建密码器
            Cipher cipher = Cipher.getInstance(KEY_AES);
            // 初始化
            cipher.init(mode, keySpec);
            byte[] result = cipher.doFinal(content);
            if (encrypt) {
                return Base64.encodeBase64String(result);
            } else {
                return new String(result, DEFAULT_CHARSET);
            }
        } catch (Exception e) {
            log.error("AES 密文处理异常", e);
        }
        return null;
    }

    public static void main(String[] args) throws Exception {
        String key = "key001002";
        String data = "test data";
        String encrypted = AESUtil.encrypt(data, key);
        String decrypted = AESUtil.decrypt(encrypted, key);
        System.out.println("加密后的密文：\n" + encrypted);
        System.out.println("解密后的报文:\n" + decrypted);
    }
}
