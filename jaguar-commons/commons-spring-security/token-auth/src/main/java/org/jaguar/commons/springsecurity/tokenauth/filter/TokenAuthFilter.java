package org.jaguar.commons.springsecurity.tokenauth.filter;


import org.apache.commons.lang3.StringUtils;
import org.jaguar.commons.springsecurity.tokenauth.TokenFactory;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;

/**
 * @author lvws
 * @since 2021/4/4
 */
public class TokenAuthFilter extends BasicAuthenticationFilter {

    private RedisTemplate<String, Serializable> redisTemplate;
    private TokenFactory tokenFactory;

    public TokenAuthFilter(AuthenticationManager authenticationManager, AuthenticationEntryPoint authenticationEntryPoint,
                           RedisTemplate<String, Serializable> redisTemplate, TokenFactory tokenFactory) {
        super(authenticationManager, authenticationEntryPoint);
        this.redisTemplate = redisTemplate;
        this.tokenFactory = tokenFactory;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String token = request.getHeader("token");
        if (StringUtils.isBlank(token)) {
            chain.doFilter(request, response);
            return;
        }

        String principal = tokenFactory.getPrincipal(token);
        Authentication authentication = (Authentication) redisTemplate.boundValueOps("spring:security:token:" + principal + ":" + token).get();
        if (authentication == null) {
            chain.doFilter(request, response);
            return;
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(request, response);
    }
}
