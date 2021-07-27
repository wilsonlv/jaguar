package org.jaguar.cloud.upms.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.jaguar.cloud.upms.mapper.ClientMapper;
import org.jaguar.cloud.upms.model.Client;
import org.jaguar.cloud.upms.service.ClientService;
import org.jaguar.commons.basecrud.BaseController;
import org.jaguar.commons.mybatisplus.extension.JaguarLambdaQueryWrapper;
import org.jaguar.commons.web.JsonResult;
import org.springframework.security.access.prepost.PreAuthorize;
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
public class ClientController extends BaseController<Client, ClientMapper, ClientService> {

    @ApiOperation(value = "分页查询oauth2客户端")
    @GetMapping(value = "/page")
    public JsonResult<Page<Client>> page(
            @ApiIgnore Page<Client> page,
            @ApiParam(value = "模糊用户信息") @RequestParam(required = false) String fuzzyClientId,
            @ApiParam(value = "启用状态") @RequestParam(required = false) Boolean clientEnable) {

        LambdaQueryWrapper<Client> wrapper = JaguarLambdaQueryWrapper.<Client>newInstance()
                .like(Client::getClientId, fuzzyClientId)
                .eq(Client::getClientEnable, clientEnable);
        return super.query(page, wrapper);
    }

    @ApiOperation(value = "oauth2客户端详情")
    @GetMapping(value = "/{id}")
    public JsonResult<Client> detail(@PathVariable Long id) {
        return super.getById(id);
    }

    @ApiOperation(value = "新增oauth2客户端")
    @PostMapping
    public JsonResult<Void> save(@RequestBody @Valid Client client) {
        return success();
    }

    @ApiOperation(value = "修改oauth2客户端")
    @PutMapping
    public JsonResult<Void> update(@RequestBody @Valid Client client) {
        return success();
    }

}