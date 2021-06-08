package org.jaguar.modules.auth.server.openapi;

import org.jaguar.commons.web.JsonResult;
import org.jaguar.commons.web.ResultCode;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lvws
 * @since 2021/6/8
 */
@RestController
@RequestMapping("/login")
public class LoginPage {

    @GetMapping("/401")
    public JsonResult<Void> unAuthorized() {
        return new JsonResult<>(ResultCode.UNAUTHORIZED);
    }

}
