package top.wilsonlv.jaguar.cloud.upms.sdk.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import top.wilsonlv.jaguar.cloud.services.JaguarServerName;
import top.wilsonlv.jaguar.cloud.services.JaguarServiceName;
import top.wilsonlv.jaguar.cloud.upms.sdk.vo.ResourceServerVO;
import top.wilsonlv.jaguar.commons.web.response.JsonResult;


/**
 * @author lvws
 * @since 2021/6/29
 */
@FeignClient(value = JaguarServerName.JAGUAR_UPMS_SERVER, contextId = JaguarServiceName.REMOTE_RESOURCE_SERVER_SERVICE)
public interface RemoteResourceServiceService {

    @GetMapping("/feign/resourceServer/loadByServerId")
    JsonResult<ResourceServerVO> loadByServerId(@RequestParam("serverId") String serverId);

}
