package top.wilsonlv.jaguar.cloud.upms.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
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
import top.wilsonlv.jaguar.basecrud.BaseController;
import top.wilsonlv.jaguar.cloud.upms.sdk.enums.ClientType;
import top.wilsonlv.jaguar.cloud.upms.sdk.enums.UserType;
import top.wilsonlv.jaguar.commons.mybatisplus.extension.JaguarLambdaQueryWrapper;
import top.wilsonlv.jaguar.oauth2.properties.JaguarSecurityProperties;
import top.wilsonlv.jaguar.commons.web.response.JsonResult;

import javax.validation.Valid;
import java.util.Collection;
import java.util.Collections;

/**
 * @author lvws
 * @since 2021-07-27
 */
@Validated
@RestController
@RequestMapping("/admin/oauthClient")
@Api(tags = "oauth2客户端管理")
@RequiredArgsConstructor
public class OauthClientController extends BaseController<OauthClient, ClientMapper, OauthClientService> {

    private final JaguarSecurityProperties jaguarSecurityProperties;

    @ApiOperation(value = "分页查询oauth2客户端")
    @PreAuthorize("hasAuthority('oauth2客户端管理')")
    @GetMapping(value = "/page")
    public JsonResult<Page<OauthClientVO>> page(
            @ApiIgnore Page<OauthClient> page,
            @ApiParam(value = "模糊用户信息") @RequestParam(required = false) String fuzzyClientId,
            @ApiParam(value = "客户端类型") @RequestParam(required = false) ClientType clientType,
            @ApiParam(value = "用户类型") @RequestParam(required = false) UserType userType) {

        LambdaQueryWrapper<OauthClient> wrapper = JaguarLambdaQueryWrapper.<OauthClient>newInstance()
                .like(OauthClient::getClientId, fuzzyClientId)
                .eq(OauthClient::getClientType, clientType)
                .eq(OauthClient::getUserType, userType);
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
    public JsonResult<String> create(@RequestBody @Valid OauthClientCreateDTO oauthClient) {
        return success(service.create(oauthClient));
    }

    @ApiOperation(value = "修改oauth2客户端")
    @PreAuthorize("hasAuthority('oauth2客户端管理')")
    @PutMapping
    public JsonResult<Void> modify(@RequestBody @Valid OauthClientModifyDTO oauthClient) {
        service.modify(oauthClient);
        return success();
    }

    @ApiOperation(value = "重置密钥")
    @PreAuthorize("hasAuthority('oauth2客户端管理')")
    @PostMapping("/resetSecret")
    public JsonResult<String> resetSecret(@RequestParam Long id) {
        return success(service.resetSecret(id));
    }

    @ApiOperation(value = "删除oauth2客户端")
    @PreAuthorize("hasAuthority('oauth2客户端管理')")
    @DeleteMapping(value = "/{id}")
    public JsonResult<Void> del(@PathVariable Long id) {
        service.checkAndDelete(id);
        return success();
    }

    @ApiOperation(value = "查询oauth2 scope")
    @PreAuthorize("hasAuthority('oauth2客户端管理')")
    @GetMapping(value = "/scopes")
    public JsonResult<Collection<String>> scopes() {
        Collection<String> scopes;
        if (jaguarSecurityProperties.getScopeUrls() == null) {
            scopes = Collections.emptySet();
        } else {
            scopes = jaguarSecurityProperties.getScopeUrls().values();
        }
        return success(scopes);
    }
}