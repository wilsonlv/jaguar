package org.jaguar.commons.springsecurity.tokenauth;

import org.jaguar.commons.data.encription.SecurityUtil;
import org.jaguar.commons.springsecurity.tokenauth.config.SecurityTokenProperties;
import org.jaguar.commons.springsecurity.tokenauth.exception.TokenExpireException;
import org.jaguar.commons.springsecurity.tokenauth.exception.TokenInvalidException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.util.Random;

/**
 * @author lvws
 * @since 2021/4/4
 */
@Configuration
public class TokenFactory {

    private static final String TOKEN_FORMAT = "username=%s&expire=%s&random=%s";

    @Autowired
    private SecurityTokenProperties securityTokenProperties;


    public String createToken(String principal) {
        Random random = new Random(System.currentTimeMillis());
        String data = String.format(TOKEN_FORMAT, principal, System.currentTimeMillis() + securityTokenProperties.getTimeout() * 1000, random.nextInt(100000000));
        return SecurityUtil.encryptRsaPublic(data, securityTokenProperties.getRsaPublicKey());
    }

    public String getPrincipal(String token) {
        String data;
        try {
            data = SecurityUtil.decryptRsaPrivate(token, securityTokenProperties.getRsaPrivateKey());
        } catch (Exception e) {
            throw new TokenInvalidException();
        }

        String[] params = data.split("&");

        Long expire = Long.parseLong(params[1].split("=")[1]);
        if (System.currentTimeMillis() > expire) {
            throw new TokenExpireException();
        }

        return params[0].split("=")[1];
    }

}
