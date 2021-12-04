package top.wilsonlv.jaguar.cloud.upms.feign;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import top.wilsonlv.jaguar.cloud.upms.sdk.feign.RemoteUserService;
import top.wilsonlv.jaguar.cloud.upms.sdk.vo.MenuVO;
import top.wilsonlv.jaguar.cloud.upms.sdk.vo.UserVO;
import top.wilsonlv.jaguar.cloud.upms.service.PersonalService;
import top.wilsonlv.jaguar.cloud.upms.service.UserService;
import top.wilsonlv.jaguar.commons.web.response.JsonResult;

import javax.validation.constraints.NotBlank;

/**
 * @author lvws
 * @since 2021/6/29
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/feign/user")
public class RemoteUserServiceImpl implements RemoteUserService {

    private final PersonalService personalService;

    @Override
    @GetMapping("/loadUserByUsername")
    public JsonResult<UserVO> loadUserByUsername(@RequestParam @NotBlank String username) {
        return JsonResult.success(personalService.getByPrincipalWithRoleAndPermission(username));
    }

}
