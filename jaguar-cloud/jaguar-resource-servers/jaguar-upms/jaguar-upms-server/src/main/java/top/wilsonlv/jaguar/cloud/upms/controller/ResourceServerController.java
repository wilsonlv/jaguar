package top.wilsonlv.jaguar.cloud.upms.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;
import top.wilsonlv.jaguar.basecrud.BaseController;
import top.wilsonlv.jaguar.cloud.upms.controller.dto.ResourceServerCreateDTO;
import top.wilsonlv.jaguar.cloud.upms.controller.dto.ResourceServerModifyDTO;
import top.wilsonlv.jaguar.cloud.upms.entity.ResourceServer;
import top.wilsonlv.jaguar.cloud.upms.mapper.ResourceServerMapper;
import top.wilsonlv.jaguar.cloud.upms.sdk.vo.ResourceServerVO;
import top.wilsonlv.jaguar.cloud.upms.service.ResourceServerService;
import top.wilsonlv.jaguar.commons.mybatisplus.extension.JaguarLambdaQueryWrapper;
import top.wilsonlv.jaguar.commons.web.response.JsonResult;

import javax.validation.Valid;

/**
 * <p>
 * 资源服务  前端控制器
 * </p>
 *
 * @author lvws
 * @since 2021-12-02
 */
@Validated
@RestController
@RequestMapping("/admin/resourceServer")
@Api(tags = "资源服务管理")
public class ResourceServerController extends BaseController<ResourceServer, ResourceServerMapper, ResourceServerService> {

    @ApiOperation(value = "分页查询资源服务")
    @PreAuthorize("hasAuthority('资源服务管理')")
    @GetMapping(value = "/page")
    public JsonResult<Page<ResourceServerVO>> page(@ApiIgnore Page<ResourceServer> page,
                                                   @ApiParam("服务ID") @RequestParam(required = false) String serverId,
                                                   @ApiParam("服务名称") @RequestParam(required = false) String serverName,
                                                   @ApiParam("服务密钥") @RequestParam(required = false) String serverSecret) {
        LambdaQueryWrapper<ResourceServer> wrapper = JaguarLambdaQueryWrapper.<ResourceServer>newInstance()
                .eq(ResourceServer::getServerId, serverId)
                .eq(ResourceServer::getServerName, serverName)
                .eq(ResourceServer::getServerSecret, serverSecret);
        return success(service.queryResourceServer(page, wrapper));
    }

    @ApiOperation(value = "资源服务详情")
    @PreAuthorize("hasAuthority('资源服务管理')")
    @GetMapping(value = "/{id}")
    public JsonResult<ResourceServerVO> detail(@PathVariable Long id) {
        return success(service.getDetail(id));
    }

    @ApiOperation(value = "新增资源服务")
    @PreAuthorize("hasAuthority('资源服务管理')")
    @PostMapping
    public JsonResult<String> create(@RequestBody @Valid ResourceServerCreateDTO resourceServer) {
        return success(service.create(resourceServer));
    }

    @ApiOperation(value = "修改资源服务")
    @PreAuthorize("hasAuthority('资源服务管理')")
    @PutMapping
    public JsonResult<Void> modify(@RequestBody @Valid ResourceServerModifyDTO resourceServer) {
        service.modify(resourceServer);
        return success();
    }

    @ApiOperation(value = "重置密钥")
    @PreAuthorize("hasAuthority('资源服务管理')")
    @PostMapping("/resetSecret")
    public JsonResult<String> resetSecret(@RequestParam Long id) {
        return success(service.resetSecret(id));
    }

    @ApiOperation(value = "删除资源服务")
    @PreAuthorize("hasAuthority('资源服务管理')")
    @DeleteMapping(value = "/{id}")
    public JsonResult<Void> del(@PathVariable Long id) {
        service.checkAndDelete(id);
        return success();
    }

}