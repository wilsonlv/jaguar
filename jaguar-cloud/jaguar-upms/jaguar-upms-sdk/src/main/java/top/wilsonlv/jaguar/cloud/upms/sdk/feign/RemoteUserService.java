package top.wilsonlv.jaguar.cloud.upms.sdk.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import top.wilsonlv.jaguar.cloud.services.JaguarServerName;
import top.wilsonlv.jaguar.cloud.services.JaguarServiceName;
import top.wilsonlv.jaguar.cloud.upms.sdk.vo.MenuVO;
import top.wilsonlv.jaguar.cloud.upms.sdk.vo.UserVO;
import top.wilsonlv.jaguar.commons.web.JsonResult;

import javax.validation.constraints.NotBlank;
import java.util.List;


/**
 * @author lvws
 * @since 2021/6/29
 */
@FeignClient(value = JaguarServerName.JAGUAR_UPMS_SERVER, contextId = JaguarServiceName.REMOTE_USER_SERVICE)
public interface RemoteUserService {

    @GetMapping("/feign/user/loadUserByUsername")
    JsonResult<UserVO> loadUserByUsername(@RequestParam("username") String username);

}
