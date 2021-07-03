package org.jaguar.cloud.auth.server.api;

import org.jaguar.commons.web.JsonResult;
import org.springframework.security.oauth2.provider.AuthorizationRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttributes;

/**
 * @author lvws
 * @since 2021/7/3
 */
@RestController
@RequestMapping("/auth/confirm_access")
@SessionAttributes("authorizationRequest")
public class ConfirmAccessApi {

    @GetMapping
    public JsonResult<?> authorize(AuthorizationRequest authorizationRequest) {
        return JsonResult.success(authorizationRequest);
    }

}
