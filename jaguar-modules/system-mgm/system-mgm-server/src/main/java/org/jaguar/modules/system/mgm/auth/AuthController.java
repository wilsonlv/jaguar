package org.jaguar.modules.system.mgm.auth;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.subject.Subject;
import org.jaguar.commons.utils.ExecutorServiceUtil;
import org.jaguar.core.base.AbstractController;
import org.jaguar.core.base.controller.VerifyCodeController;
import org.jaguar.core.exception.CheckedException;
import org.jaguar.core.web.JsonResult;
import org.jaguar.modules.handlerlog.intercepter.HandlerLogInterceptor;
import org.jaguar.modules.handlerlog.model.HandlerLog;
import org.jaguar.modules.system.mgm.config.SystemMgmProperties;
import org.jaguar.modules.system.mgm.mapper.UserMapper;
import org.jaguar.modules.system.mgm.model.Login;
import org.jaguar.modules.system.mgm.model.Menu;
import org.jaguar.modules.system.mgm.model.User;
import org.jaguar.modules.system.mgm.service.LoginService;
import org.jaguar.modules.system.mgm.service.RoleMenuService;
import org.jaguar.modules.system.mgm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;

/**
 * @author lvws
 * @since 2019/11/15
 */
@Validated
@RestController
@RequestMapping("/auth")
@Api(value = "个人登录和权限管理")
public class AuthController extends AbstractController<User, UserMapper, UserService> {

    @Autowired
    private SystemMgmProperties systemMgmProperties;

    @Autowired
    private LoginService loginService;
    @Autowired
    private RoleMenuService roleMenuService;

    @ApiOperation(value = "登录")
    @PostMapping(value = "/login")
    public ResponseEntity<JsonResult<User>> login(@ApiParam("登录信息") @RequestBody @Valid Login login) throws Throwable {

        if (systemMgmProperties.getVerfiyCodeEnable()) {
            if (StringUtils.isBlank(login.getVerifyCode())) {
                throw new CheckedException("验证码为非空");
            }
            VerifyCodeController.verificationCode(login.getVerifyCode());
        }

        HandlerLog handlerLog = HandlerLogInterceptor.HANDLER_LOG.get();

        login.setLoginIp(handlerLog.getClientHost());
        login.setLoginTime(handlerLog.getAccessTime());
        login.setSessionId(handlerLog.getSessionId());
        login.setResultCode(HttpStatus.OK.value());

        synchronized (login.getPrincipal().intern()) {
            try {
                Subject subject = SecurityUtils.getSubject();
                subject.login(login);
            } catch (AuthenticationException e) {
                login.setResultCode(HttpStatus.CONFLICT.value());
                throw e.getCause();
            } catch (Exception e) {
                login.setResultCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
                throw e;
            } finally {
                ExecutorServiceUtil.execute(() -> loginService.insert(login));
            }
        }

        User user = service.getById(getCurrentUser());
        return success(user);
    }

    @ApiOperation(value = "退出登陆")
    @PostMapping(value = "/logout")
    public ResponseEntity<JsonResult<?>> logout() {

        SecurityUtils.getSubject().logout();
        return success();
    }

    @ApiOperation(value = "用户信息")
    @GetMapping(value = "/info")
    public ResponseEntity<JsonResult<User>> getPersonalInfo() {

        User user = service.getDetail(getCurrentUser());
        return success(user);
    }

    @ApiOperation(value = "获取当前用户授权菜单")
    @GetMapping(value = "/menu/tree/view_permission")
    public ResponseEntity<JsonResult<List<Menu>>> menuTreeViewPermission() {

        List<Menu> menuTree = roleMenuService.menuTreeViewPermissionByUserId(getCurrentUser());
        return success(menuTree);
    }

    @ApiOperation(value = "获取当前用户接口权限")
    @GetMapping(value = "/permission/list")
    public ResponseEntity<JsonResult<Set<String>>> permissionList() {

        Set<String> permissions = roleMenuService.listPermissionByUserId(getCurrentUser());
        return success(permissions);
    }

    @ApiOperation(value = "修改密码")
    @PostMapping(value = "/modify_password")
    public ResponseEntity<JsonResult<?>> modifyPassword(
            @ApiParam(value = "旧密码", required = true) @RequestParam String oldPassword,
            @ApiParam(value = "新密码", required = true) @RequestParam String newPassword) {

        synchronized (this) {
            service.modifyPassword(getCurrentUser(), oldPassword, newPassword);
        }
        return success();
    }
}
