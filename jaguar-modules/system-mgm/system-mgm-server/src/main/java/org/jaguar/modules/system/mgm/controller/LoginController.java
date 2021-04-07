package org.jaguar.modules.system.mgm.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.jaguar.commons.basecrud.BaseController;
import org.jaguar.commons.mybatisplus.extension.JaguarLambdaQueryWrapper;
import org.jaguar.commons.web.JsonResult;
import org.jaguar.modules.system.mgm.mapper.LoginMapper;
import org.jaguar.modules.system.mgm.model.Login;
import org.jaguar.modules.system.mgm.service.LoginService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

/**
 * <p>
 * 系统登录日志表  前端控制器
 * </p>
 *
 * @author lvws
 * @since 2019-11-15
 */
@Validated
@RestController
@RequestMapping("/system/mgm/login")
@Api(tags = "系统登录日志表管理")
public class LoginController extends BaseController<Login, LoginMapper, LoginService> {

    @ApiOperation(value = "查询系统登录日志表")
    @PreAuthorize("hasAuthority('登录日志管理')")
    @GetMapping(value = "/page")
    public JsonResult<IPage<Login>> page(
            @ApiIgnore IPage<Login> page,
            @ApiParam(value = "查询信息") Login login) {

        LambdaQueryWrapper<Login> wrapper = new JaguarLambdaQueryWrapper<>();
        wrapper.setEntity(login);
        return super.query(page, wrapper);
    }

}