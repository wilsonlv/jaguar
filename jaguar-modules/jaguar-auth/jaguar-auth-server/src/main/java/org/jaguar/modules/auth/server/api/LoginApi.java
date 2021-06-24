package org.jaguar.modules.auth.server.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jaguar.commons.web.JsonResult;
import org.jaguar.commons.web.ResultCode;
import org.jaguar.commons.web.exception.impl.CheckedException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;

/**
 * @author lvws
 * @since 2021/6/8
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/login")
public class LoginApi {

    private static final String TOKEN_PREFIX = "Bearer ";

    private final TokenStore tokenStore;

    @GetMapping("/401")
    public JsonResult<Void> unAuthorized() {
        return new JsonResult<>(ResultCode.UNAUTHORIZED);
    }

    @GetMapping("/info")
    public JsonResult<UserDetails> info(@RequestHeader(value = "Authorization") @NotBlank String bearerToken) {
        if (!bearerToken.startsWith(TOKEN_PREFIX)) {
            throw new CheckedException("Authorization must start with " + TOKEN_PREFIX);
        }

        String token = bearerToken.substring(7);

        OAuth2Authentication authentication = tokenStore.readAuthentication(token);
        if (authentication == null) {
            throw new CheckedException("无效的token");
        }

        return JsonResult.success((User) authentication.getPrincipal());
    }

    @GetMapping("/123")
    public String info() {
        return "123";
    }

}
