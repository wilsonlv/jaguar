package top.wilsonlv.jaguar.commons.data.encryption.util;

import org.apache.commons.lang3.StringUtils;
import top.wilsonlv.jaguar.commons.data.encryption.coder.DesCoder;
import top.wilsonlv.jaguar.commons.data.encryption.coder.RsaCoder;
import org.springframework.util.Base64Utils;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;


/**
 * 数据加密辅助类(默认编码UTF-8)
 *
 * @author lvws
 * @since 2019-02-27
 */
public final class EncryptionUtil {

    private EncryptionUtil() {
    }

    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(.{6,20})$");

    private static final char[] HEX_DIGITS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    /**
     * BASE64编码
     */
    public static String encryptBase64(byte[] data) {
        try {
            return new String(Base64Utils.decode(data), StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException("加密错误，错误信息：", e);
        }
    }

    /**
     * BASE64解码
     */
    public static byte[] decryptBase64(String key) {
        try {
            return Base64Utils.decode(key.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            throw new RuntimeException("解密错误，错误信息：", e);
        }
    }

    /**
     * md5摘要
     *
     * @param value 数据
     * @return md5摘要
     */
    public static String md5Digest(String value) {
        if (StringUtils.isBlank(value)) {
            return null;
        }

        try {
            return DigestUtils.md5DigestAsHex(value.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * DES对称加密
     */
    public static String encryptDes(String data, byte[] key) {
        try {
            return encryptBase64(DesCoder.encrypt(data, key));
        } catch (Exception e) {
            throw new RuntimeException("加密错误，错误信息：", e);
        }
    }

    /**
     * DES对称解密
     */
    public static String decryptDes(String encryptData, byte[] key) {
        try {
            return DesCoder.decrypt(decryptBase64(encryptData), key);
        } catch (Exception e) {
            throw new RuntimeException("解密错误，错误信息：", e);
        }
    }

    /**
     * RSA私钥签名
     */
    public static String signRsa(String data, String privateKey) {
        try {
            byte[] sign = RsaCoder.sign(data.getBytes(StandardCharsets.UTF_8), decryptBase64(privateKey));
            return encryptBase64(sign);
        } catch (Exception e) {
            throw new RuntimeException("签名错误，错误信息：", e);
        }
    }

    /**
     * RSA公钥验签
     */
    public static boolean verifyRsa(String data, String publicKey, String sign) {
        try {
            return RsaCoder.verify(data.getBytes(StandardCharsets.UTF_8), decryptBase64(publicKey), decryptBase64(sign));
        } catch (Exception e) {
            throw new RuntimeException("验签错误，错误信息：", e);
        }
    }

    /**
     * RSA公钥加密
     */
    public static String encryptRsaPublic(String data, String publicKey) {
        try {
            return encryptBase64(RsaCoder.encryptByPublicKey(data.getBytes(StandardCharsets.UTF_8), decryptBase64(publicKey)));
        } catch (Exception e) {
            throw new RuntimeException("加密错误，错误信息：", e);
        }
    }

    /**
     * RSA私钥解密
     */
    public static String decryptRsaPrivate(String cryptData, String privateKey) {
        try {
            // 把字符串解码为字节数组，并解密
            return new String(RsaCoder.decryptByPrivateKey(decryptBase64(cryptData), decryptBase64(privateKey)));
        } catch (Exception e) {
            throw new RuntimeException("解密错误，错误信息：", e);
        }
    }

    /**
     * 验证密码格式
     */
    public static boolean passwordUnmatched(String password) {
        return !PASSWORD_PATTERN.matcher(password).matches();
    }

}
