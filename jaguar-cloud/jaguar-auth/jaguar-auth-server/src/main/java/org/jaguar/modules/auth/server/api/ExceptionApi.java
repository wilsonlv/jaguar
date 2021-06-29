package org.jaguar.modules.auth.server.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jaguar.commons.web.JsonResult;
import org.jaguar.commons.web.ResultCode;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lvws
 * @since 2021/6/8
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class ExceptionApi {

    @GetMapping("/401")
    public JsonResult<Void> unAuthorized() {
        return new JsonResult<>(ResultCode.UNAUTHORIZED);
    }

}
