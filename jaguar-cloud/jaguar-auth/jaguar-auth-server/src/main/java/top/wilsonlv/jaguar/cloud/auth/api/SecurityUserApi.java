package top.wilsonlv.jaguar.cloud.auth.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.wilsonlv.jaguar.commons.web.response.JsonResult;
import top.wilsonlv.jaguar.oauth2.model.SecurityUser;
import top.wilsonlv.jaguar.oauth2.util.SecurityUtil;

/**
 * @author lvws
 * @since 2021/12/17
 */
@RestController
@RequestMapping("/security/user")
public class SecurityUserApi {

    @GetMapping
    public JsonResult<SecurityUser> securityUser() {
        return JsonResult.success(SecurityUtil.getCurrentUser());
    }

}
