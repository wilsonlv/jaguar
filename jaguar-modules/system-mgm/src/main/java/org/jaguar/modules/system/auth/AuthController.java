package org.jaguar.modules.system.auth;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.jaguar.commons.enums.ClientType;
import org.jaguar.commons.utils.ExecutorServiceUtil;
import org.jaguar.commons.utils.IdentifyingCode;
import org.jaguar.core.base.AbstractController;
import org.jaguar.core.exception.CheckedException;
import org.jaguar.core.web.JsonResult;
import org.jaguar.core.web.LoginUtil;
import org.jaguar.modules.handlerlog.intercepter.HandlerLogInterceptor;
import org.jaguar.modules.handlerlog.model.HandlerLog;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.List;
import java.util.Set;

import static org.jaguar.modules.system.Constant.PIC_VERIFICATION_CODE;

/**
 * @author lvws
 * @since 2019/11/15
 */
@Validated
@RestController
@RequestMapping("/auth")
@Api(value = "个人登录和权限管理", description = "个人登录和权限管理")
public class AuthController extends AbstractController<User, UserMapper, UserService> {

    @Autowired
    private LoginService loginService;
    @Autowired
    private RoleMenuService roleMenuService;

    @ApiOperation(value = "获取图片验证码")
    @GetMapping(value = "/pic_verification_code")
    public void randomImage(HttpServletResponse response, HttpServletRequest request) throws IOException {
        //图片宽度
        int width = 200;
        //图片高度
        int height = 80;
        //字符串个数
        int randomStrNum = 4;

        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setContentType("image/jpeg");

        //生成验证码
        String verifyCode = IdentifyingCode.generate(randomStrNum);
        //保存验证码
        HttpSession session = request.getSession();
        session.setAttribute(PIC_VERIFICATION_CODE, verifyCode.toLowerCase());
        //回传
        IdentifyingCode.outputImage(width, height, response.getOutputStream(), verifyCode);
    }

    /**
     * 验证图片验证码
     */
    private void verificationCode(String verifyCode) {
        Session session = LoginUtil.getSession();
        Object verificationCode = session.getAttribute(PIC_VERIFICATION_CODE);
        if (verificationCode == null) {
            throw new CheckedException("请获取图片验证码！");
        }

        session.removeAttribute(PIC_VERIFICATION_CODE);

        logger.info("sessionId：{}，验证码：{}", session.getId(), verificationCode);
        if (!verifyCode.equalsIgnoreCase((String) verificationCode)) {
            throw new CheckedException("验证码错误！");
        }
    }

    @ApiOperation(value = "登录")
    @PostMapping(value = "/login")
    public ResponseEntity<JsonResult<User>> login(
            @ApiParam(value = "登陆主体", required = true) @RequestParam @NotBlank String principal,
            @ApiParam(value = "登陆凭证", required = true) @RequestParam @NotBlank String credentials,
            @ApiParam(value = "验证码") @RequestParam(required = false) String verifyCode,
            @ApiParam(value = "客服端类型", required = true) @RequestParam @NotNull ClientType clientType,
            @ApiParam(value = "客户端版本", required = true) @RequestParam @NotNull String clientVersion,
            @ApiParam(value = "设备型号") @RequestParam(required = false) String deviceModel,
            @ApiParam(value = "设备系统版本") @RequestParam(required = false) String deviceSysVersion,
            @ApiParam(value = "设备唯一标示") @RequestParam(required = false) String deviceImei) {

//        this.verificationCode(verifyCode);

        HandlerLog handlerLog = HandlerLogInterceptor.HANDLER_LOG.get();

        final Login login = new Login();
        login.setPrincipal(principal);
        login.setCredentials(credentials);
        login.setClientType(clientType);
        login.setClientVersion(clientVersion);
        login.setDeviceModel(deviceModel);
        login.setDeviceSysVersion(deviceSysVersion);
        login.setDeviceImei(deviceImei);
        login.setLoginIp(handlerLog.getClientHost());
        login.setLoginTime(handlerLog.getAccessTime());
        login.setSessionId(handlerLog.getSessionId());
        login.setResultCode(HttpStatus.OK.value());

        synchronized (principal.intern()) {
            try {
                Subject subject = SecurityUtils.getSubject();
                subject.login(login);
            } catch (Exception e) {
                login.setResultCode(HttpStatus.CONFLICT.value());
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
    public ResponseEntity<JsonResult> logout() {

        SecurityUtils.getSubject().logout();
        return success();
    }

    @ApiOperation(value = "用户信息")
    @GetMapping(value = "/info")
    public ResponseEntity<JsonResult<User>> getPersonalInfo() {

        User user = service.getPersonalInfo(getCurrentUser());
        return success(user);
    }

    @ApiOperation(value = "获取当前用户授权菜单")
    @GetMapping(value = "/menu/tree")
    public ResponseEntity<JsonResult<List<Menu>>> menuTree() {

        List<Menu> menuTree = roleMenuService.treeMenuByUserId(getCurrentUser());
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
    public ResponseEntity<JsonResult> modifyPassword(
            @ApiParam(value = "旧密码", required = true) @RequestParam String oldPassword,
            @ApiParam(value = "新密码", required = true) @RequestParam String newPassword) {

        service.modifyPassword(getCurrentUser(), oldPassword, newPassword);
        return success();
    }
}
