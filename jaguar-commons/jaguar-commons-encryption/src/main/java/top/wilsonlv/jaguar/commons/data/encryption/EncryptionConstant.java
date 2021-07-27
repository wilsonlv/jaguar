package top.wilsonlv.jaguar.commons.data.encryption;

/**
 * @author lvws
 * @since 2019/4/16.
 */
public interface EncryptionConstant {

    /**
     * des
     */
    String ALGORITHM_DES = "DES";

    /**
     * rsa
     */
    String ALGORITHM_RSA = "RSA";

    /**
     * 加密/解密算法 / 工作模式 / 填充方式
     */
    String CIPHER_ALGORITHM = "DES/ECB/PKCS5PADDING";

    /**
     * 数字签名 签名/验证算法
     */
    String SIGNATURE_ALGORITHM = "SHA1withRSA";

    /**
     * 公钥
     */
    String PUBLIC_KEY = "RSAPublicKey";

    /**
     * 私钥
     */
    String PRIVATE_KEY = "RSAPrivateKey";

    /**
     * RSA密钥长度 默认1024位， 密钥长度必须是64的倍数， 范围在512至65536位之间。
     */
    int KEY_SIZE = 2048;

}
