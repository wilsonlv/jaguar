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
import top.wilsonlv.jaguar.cloud.upms.controller.dto.OauthClientCreateDTO;
import top.wilsonlv.jaguar.cloud.upms.controller.dto.OauthClientModifyDTO;
import top.wilsonlv.jaguar.cloud.upms.entity.OauthClient;
import top.wilsonlv.jaguar.cloud.upms.mapper.ClientMapper;
import top.wilsonlv.jaguar.cloud.upms.sdk.vo.OauthClientVO;
import top.wilsonlv.jaguar.cloud.upms.service.OauthClientService;
import top.wilsonlv.jaguar.commons.basecrud.BaseController;
import top.wilsonlv.jaguar.commons.mybatisplus.extension.JaguarLambdaQueryWrapper;
import top.wilsonlv.jaguar.commons.web.JsonResult;

import javax.validation.Valid;

/**
 * @author lvws
 * @since 2021-07-27
 */
@Validated
@RestController
@RequestMapping("/admin/oauthClient")
@Api(tags = "oauth2客户端管理")
public class OauthClientController extends BaseController<OauthClient, ClientMapper, OauthClientService> {

    @ApiOperation(value = "分页查询oauth2客户端")
    @PreAuthorize("hasAuthority('oauth2客户端管理')")
    @GetMapping(value = "/page")
    public JsonResult<Page<OauthClientVO>> page(
            @ApiIgnore Page<OauthClient> page,
            @ApiParam(value = "模糊用户信息") @RequestParam(required = false) String fuzzyClientId) {

        LambdaQueryWrapper<OauthClient> wrapper = JaguarLambdaQueryWrapper.<OauthClient>newInstance()
                .like(OauthClient::getClientId, fuzzyClientId);
        return success(service.queryOauthClient(page, wrapper));
    }

    @ApiOperation(value = "oauth2客户端详情")
    @PreAuthorize("hasAuthority('oauth2客户端管理')")
    @GetMapping(value = "/{id}")
    public JsonResult<OauthClientVO> detail(@PathVariable Long id) {
        return success(service.getDetail(id));
    }

    @ApiOperation(value = "新增oauth2客户端")
    @PreAuthorize("hasAuthority('oauth2客户端管理')")
    @PostMapping
    public JsonResult<Void> create(@RequestBody @Valid OauthClientCreateDTO oauthClient) {
        service.create(oauthClient);
        return success();
    }

    @ApiOperation(value = "修改oauth2客户端")
    @PreAuthorize("hasAuthority('oauth2客户端管理')")
    @PutMapping
    public JsonResult<Void> modify(@RequestBody @Valid OauthClientModifyDTO oauthClient) {
        service.modify(oauthClient);
        return success();
    }

    @ApiOperation(value = "删除oauth2客户端")
    @PreAuthorize("hasAuthority('oauth2客户端管理')")
    @DeleteMapping(value = "/{id}")
    public JsonResult<Void> del(@PathVariable Long id) {
        service.checkAndDelete(id);
        return success();
    }

}