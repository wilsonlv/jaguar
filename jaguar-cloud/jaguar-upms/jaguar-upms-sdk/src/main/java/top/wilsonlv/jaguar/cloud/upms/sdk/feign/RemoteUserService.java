package top.wilsonlv.jaguar.cloud.upms.sdk.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import top.wilsonlv.jaguar.cloud.upms.sdk.vo.MenuVO;
import top.wilsonlv.jaguar.cloud.upms.sdk.vo.UserVO;
import top.wilsonlv.jaguar.commons.web.JsonResult;

import java.util.List;


/**
 * @author lvws
 * @since 2021/6/29
 */
@FeignClient(value = "jaguar-upms-server", contextId = "remoteUserService")
public interface RemoteUserService {

    @GetMapping("/feign/user/loadUserByUsername")
    JsonResult<UserVO> loadUserByUsername(@RequestParam("username") String username);

    @GetMapping("/feign/user/getDetail")
    JsonResult<UserVO> getDetail(@RequestParam("userId") Long userId);

    @GetMapping("/feign/user/getUserMenus")
    JsonResult<List<MenuVO>> getUserMenus(@RequestParam("userId") Long userId);

}
