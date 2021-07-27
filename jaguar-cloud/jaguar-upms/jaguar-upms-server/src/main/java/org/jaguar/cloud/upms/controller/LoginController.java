package org.jaguar.cloud.upms.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jaguar.cloud.upms.model.Login;
import org.jaguar.cloud.upms.repository.LoginRepository;
import org.jaguar.commons.web.JsonResult;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
@RequestMapping("/login")
@Api(tags = "系统登录日志表管理")
public class LoginController {

    private LoginRepository loginRepository;

    @ApiOperation(value = "查询系统登录日志表")
    @PreAuthorize("hasAuthority('登录日志管理')")
    @GetMapping(value = "/page")
    public JsonResult<Page<Login>> page(@ApiIgnore PageRequest page) {
        Page<Login> all = loginRepository.findAll(page);
        return JsonResult.success(all);
    }

}