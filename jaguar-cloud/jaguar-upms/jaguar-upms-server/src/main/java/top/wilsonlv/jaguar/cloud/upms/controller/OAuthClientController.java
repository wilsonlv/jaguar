package top.wilsonlv.jaguar.cloud.upms.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import top.wilsonlv.jaguar.cloud.upms.mapper.ClientMapper;
import top.wilsonlv.jaguar.cloud.upms.model.OAuthClient;
import top.wilsonlv.jaguar.cloud.upms.service.OAuthClientService;
import top.wilsonlv.jaguar.commons.basecrud.BaseController;
import top.wilsonlv.jaguar.commons.mybatisplus.extension.JaguarLambdaQueryWrapper;
import top.wilsonlv.jaguar.commons.web.JsonResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;

/**
 * @author lvws
 * @since 2021-07-27
 */
@Validated
@RestController
@RequestMapping("/admin/client")
@Api(tags = "oauth2客户端管理")
public class OAuthClientController extends BaseController<OAuthClient, ClientMapper, OAuthClientService> {

    @ApiOperation(value = "分页查询oauth2客户端")
    @GetMapping(value = "/page")
    public JsonResult<Page<OAuthClient>> page(
            @ApiIgnore Page<OAuthClient> page,
            @ApiParam(value = "模糊用户信息") @RequestParam(required = false) String fuzzyClientId,
            @ApiParam(value = "启用状态") @RequestParam(required = false) Boolean clientEnable) {

        LambdaQueryWrapper<OAuthClient> wrapper = JaguarLambdaQueryWrapper.<OAuthClient>newInstance()
                .like(OAuthClient::getClientId, fuzzyClientId)
                .eq(OAuthClient::getClientEnable, clientEnable);
        return super.query(page, wrapper);
    }

    @ApiOperation(value = "oauth2客户端详情")
    @GetMapping(value = "/{id}")
    public JsonResult<OAuthClient> detail(@PathVariable Long id) {
        return super.getById(id);
    }

    @ApiOperation(value = "新增oauth2客户端")
    @PostMapping
    public JsonResult<Void> save(@RequestBody @Valid OAuthClient OAuthClient) {
        return success();
    }

    @ApiOperation(value = "修改oauth2客户端")
    @PutMapping
    public JsonResult<Void> update(@RequestBody @Valid OAuthClient OAuthClient) {
        return success();
    }

}