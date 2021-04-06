package org.jaguar.commons.springsecurity.tokenauth.handler;

import org.jaguar.commons.springsecurity.tokenauth.TokenFactory;
import org.jaguar.commons.springsecurity.tokenauth.config.SecurityTokenProperties;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.concurrent.TimeUnit;

/**
 * @author lvws
 * @since 2021/4/4
 */
public class TokenLoginSuccessHandler implements AuthenticationSuccessHandler {

    private TokenFactory tokenFactory;
    private RedisTemplate<String, Serializable> redisTemplate;
    private SecurityTokenProperties securityTokenProperties;

    public TokenLoginSuccessHandler(TokenFactory tokenFactory, RedisTemplate<String, Serializable> redisTemplate,
                                    SecurityTokenProperties securityTokenProperties) {
        this.tokenFactory = tokenFactory;
        this.redisTemplate = redisTemplate;
        this.securityTokenProperties = securityTokenProperties;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetails principal = (UserDetails) authentication.getPrincipal();
        principal.getAuthorities().clear();

        String token = tokenFactory.createToken(authentication.getName());
        redisTemplate.boundValueOps("spring:security:token:" + authentication.getName() + ":" + token)
                .set(principal, securityTokenProperties.getTimeout(), TimeUnit.SECONDS);

        try (PrintWriter writer = response.getWriter()) {
            writer.write(token);
        }
    }
}
