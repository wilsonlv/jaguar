package org.jaguar.commons.data.encription;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.regex.Pattern;


/**
 * 数据加密辅助类(默认编码UTF-8)
 *
 * @author lvws
 * @since 2019-02-27
 */
public final class SecurityUtil {

    private SecurityUtil() {
    }

    public static Pattern PASSWORD_PATTERN = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(.{6,20})$");


    private static final char[] HEX_DIGITS = {'0', '1', '2', '3', '4', '5',
            '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    /**
     * DES对称加密默认密钥
     */
    private static final byte[] DES_DEFAULF_KEY = {56, 64, -38, 11, 98, -101, 32, -17};

    private static final String RSA_DEFAULF_PRIVITE_KEY = "MIIBVgIBADANBgkqhkiG9w0BAQEFAASCAUAwggE8AgEAAkEAsmPPdbWJvEahJ6mBRFshFXVLmc84" +
            "ZLw1R97xAdD9w03M89U5Z5pDr+onAILePWztx57vc4LLYHHL1m0B+kmmUQIDAQABAkBN46SmqZAt" +
            "I82jqrAlb4C/hMpwqvjdiwvciKxEdcMHLcn/9yTtGaz72RHUdRICwkWK3ECW0NFfPAT3Nn7HAIHt" +
            "AiEA8xErMLqAexfIWe8Z7//yqNda2fQdJTYqd0ABcc0wf7MCIQC74at78qKnM6evqBghw4Sys0Du" +
            "S0ozMe7nIHefeptf6wIhAI09kr1UAkiEv1UTDR1auuTFjvNLWIxA91goBy7brqzlAiEAnb1vS0ZA" +
            "N18QB1N6x0YvMBV9i1Pc/TSMHrLMnDHFJ3cCIQCiup8/98WKaGR9TEi8i306OBbO7KI9+xIHfOkb" +
            "l69ITg==";

    private static final String RSA_DEFAULF_PUBLICK_KEY = "MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBALJjz3W1ibxGoSepgURbIRV1S5nPOGS8NUfe8QHQ/cNN" +
            "zPPVOWeaQ6/qJwCC3j1s7cee73OCy2Bxy9ZtAfpJplECAwEAAQ==";


    private static String getFormattedText(byte[] bytes) {
        int len = bytes.length;
        StringBuilder buf = new StringBuilder(len * 2);
        for (byte aByte : bytes) {
            buf.append(HEX_DIGITS[(aByte >> 4) & 0x0f]);
            buf.append(HEX_DIGITS[aByte & 0x0f]);
        }
        return buf.toString();
    }

    /**
     * BASE64编码
     */
    public static String encryptBASE64(byte[] data) {
        try {
            return new BASE64Encoder().encode(data);
        } catch (Exception e) {
            throw new RuntimeException("加密错误，错误信息：", e);
        }
    }

    /**
     * BASE64解码
     */
    public static byte[] decryptBASE64(String key) {
        try {
            return new BASE64Decoder().decodeBuffer(key);
        } catch (Exception e) {
            throw new RuntimeException("解密错误，错误信息：", e);
        }
    }


    /**
     * DES对称加密，
     */
    public static String encryptDes(String data) {
        return encryptDes(data, DES_DEFAULF_KEY);
    }

    /**
     * DES对称加密
     */
    public static String encryptDes(String data, byte[] key) {
        try {
            byte[] encrypt = DESCoder.encrypt(data, key);
            return encryptBASE64(encrypt);
        } catch (Exception e) {
            throw new RuntimeException("加密错误，错误信息：", e);
        }
    }

    /**
     * 数据解密，算法（DES）
     */
    public static String decryptDes(String encryptData) {
        return decryptDes(encryptData, DES_DEFAULF_KEY);
    }

    /**
     * 数据解密，算法（DES）
     */
    public static String decryptDes(String encryptData, byte[] key) {
        try {
            return DESCoder.decrypt(decryptBASE64(encryptData), key);
        } catch (Exception e) {
            throw new RuntimeException("解密错误，错误信息：", e);
        }
    }


    public static String encodeMD5(String value) {
        if (value == null) {
            return null;
        }
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(value.getBytes());
            return getFormattedText(messageDigest.digest());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * RSA私钥签名
     */
    public static String signRSA(String data, String privateKey) {
        try {
            byte[] sign = RSACoder.sign(data.getBytes(StandardCharsets.UTF_8), decryptBASE64(privateKey));
            return encryptBASE64(sign);
        } catch (Exception e) {
            throw new RuntimeException("签名错误，错误信息：", e);
        }
    }

    /**
     * RSA公钥验签
     */
    public static boolean verifyRSA(String data, String publicKey, String sign) {
        try {
            return RSACoder.verify(data.getBytes(StandardCharsets.UTF_8), decryptBASE64(publicKey), decryptBASE64(sign));
        } catch (Exception e) {
            throw new RuntimeException("验签错误，错误信息：", e);
        }
    }

    /**
     * RSA公钥加密
     */
    public static String encryptRSAPublic(String data, String publicKey) {
        try {
            return encryptBASE64(RSACoder.encryptByPublicKey(data.getBytes(StandardCharsets.UTF_8), decryptBASE64(publicKey)));
        } catch (Exception e) {
            throw new RuntimeException("加密错误，错误信息：", e);
        }
    }

    /**
     * RSA私钥解密
     */
    public static String decryptRSAPrivate(String cryptData, String privateKey) {
        try {
            // 把字符串解码为字节数组，并解密
            return new String(RSACoder.decryptByPrivateKey(decryptBASE64(cryptData), decryptBASE64(privateKey)));
        } catch (Exception e) {
            throw new RuntimeException("解密错误，错误信息：", e);
        }
    }

    /**
     * 数据库密码加密
     */
    public static String encryptPassword(String password) {
        return encodeMD5(encryptDes(password));
    }

    /**
     * 验证密码格式
     */
    public static boolean checkPassword(String password) {
        return PASSWORD_PATTERN.matcher(password).matches();
    }

    public static void main(String[] args) {
//        System.out.println(encryptPassword("admin"));
        System.out.println(encryptPassword("NlYvNE"));
    }

}
