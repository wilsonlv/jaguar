package top.wilsonlv.jaguar.cloud.handlerlog.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;
import top.wilsonlv.jaguar.cloud.handlerlog.mapper.LoginLogMapper;
import top.wilsonlv.jaguar.cloud.handlerlog.model.LoginLog;
import top.wilsonlv.jaguar.cloud.upms.sdk.enums.UserType;
import top.wilsonlv.jaguar.cloud.handlerlog.service.LoginLogService;
import top.wilsonlv.jaguar.commons.basecrud.BaseController;
import top.wilsonlv.jaguar.commons.mybatisplus.extension.JaguarLambdaQueryWrapper;
import top.wilsonlv.jaguar.commons.web.JsonResult;

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
@RequestMapping("/admin/loginLog")
@Api(tags = "系统登录日志表管理")
public class LoginLogController extends BaseController<LoginLog, LoginLogMapper, LoginLogService> {

    @ApiOperation(value = "查询系统登录日志表")
    @PreAuthorize("hasAuthority('登录日志管理')")
    @GetMapping(value = "/page")
    public JsonResult<Page<LoginLog>> page(
            @ApiIgnore Page<LoginLog> page,
            @ApiParam(value = "模糊登录主体") @RequestParam(required = false) String fuzzyPrincipal,
            @ApiParam(value = "用户类型") @RequestParam(required = false) UserType loginUserType,
            @ApiParam(value = "登录IP") @RequestParam(required = false) String loginIp) {

        LambdaQueryWrapper<LoginLog> wrapper = JaguarLambdaQueryWrapper.<LoginLog>newInstance()
                .like(LoginLog::getPrincipal, fuzzyPrincipal)
                .eq(LoginLog::getLoginUserType, loginUserType)
                .eq(LoginLog::getLoginIp, loginIp);
        return super.query(page, wrapper);
    }

}