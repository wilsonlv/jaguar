package org.jaguar.cloud.upms.server.auth;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jaguar.cloud.upms.server.config.SystemMgmProperties;
import org.jaguar.cloud.upms.server.mapper.UserMapper;
import org.jaguar.cloud.upms.server.model.Login;
import org.jaguar.cloud.upms.server.model.User;
import org.jaguar.cloud.upms.server.service.LoginService;
import org.jaguar.commons.basecrud.BaseController;
import org.jaguar.commons.web.JsonResult;
import org.jaguar.commons.web.exception.impl.CheckedException;
import org.jaguar.commons.web.util.IpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.time.LocalDateTime;

/**
 * @author lvws
 * @since 2019/11/15
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/auth")
@Api(tags = "个人登录和权限管理")
public class AuthController extends BaseController<User, UserMapper, AuthService> {


    public static final String PIC_VERIFICATION_CODE = "PIC_VERIFICATION_CODE";

    @Autowired
    private ServerProperties serverProperties;
    @Autowired
    private SystemMgmProperties systemMgmProperties;
    //    @Autowired
//    private AuthenticationManager authenticationManager;
    @Autowired
    private LoginService loginService;

    /**
     * 验证图片验证码
     */
    public static void verifyCode(HttpServletRequest request, String verifyCode) {
        HttpSession session = request.getSession();
        Object verificationCode = session.getAttribute(PIC_VERIFICATION_CODE);
        if (verificationCode == null) {
            throw new CheckedException("请获取图片验证码！");
        }

        session.removeAttribute(PIC_VERIFICATION_CODE);

        if (log.isDebugEnabled()) {
            log.debug("sessionId：{}，验证码：{}", session.getId(), verificationCode);
        }

        if (!verifyCode.equalsIgnoreCase((String) verificationCode)) {
            throw new CheckedException("验证码错误！");
        }
    }

    @ApiOperation(value = "登录")
    @PostMapping(value = "/login")
    public JsonResult<User> login(HttpServletRequest request,
                                  @ApiParam("登录信息") @RequestBody @Valid Login login) throws Throwable {

        if (systemMgmProperties.getVerifyCodeEnable()) {
            if (StringUtils.isBlank(login.getVerifyCode())) {
                throw new CheckedException("验证码为非空");
            }
            verifyCode(request, login.getVerifyCode());
        }

        login.setPasswordFree(false);
        login.setLoginIp(IpUtil.getHost(request));
        login.setLoginTime(LocalDateTime.now());
        login.setSessionId(request.getSession().getId());
        login.setResultCode(HttpStatus.OK.value());
        login.setSystemName(serverProperties.getServlet().getApplicationDisplayName());

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(login.getPrincipal(), login.getCredentials());
        try {
//            Authentication authentication = authenticationManager.authenticate(token);
//            SecurityContextHolder.getContext().setAuthentication(authentication);
//
//            User user = (User) authentication.getPrincipal();
//            login.setUserId(user.getId());
        } finally {
            loginService.asyncSave(login);
        }

//        return getPersonalInfo();
        return null;
    }

    @ApiOperation(value = "退出登录")
    @PostMapping(value = "/logout")
    public JsonResult<?> logout() {
        SecurityContextHolder.clearContext();
        return success();
    }

    @ApiOperation(value = "用户信息")
    @GetMapping(value = "/info")
    public JsonResult<String> getPersonalInfo() {
//        User user = service.getDetail(SecurityUtil.getCurrentUser());
//        return success(user);
        return JsonResult.success("123");
    }

    @ApiOperation(value = "修改密码")
    @PostMapping(value = "/modify_password")
    public JsonResult<?> modifyPassword(
            @ApiParam(value = "旧密码", required = true) @RequestParam String oldPassword,
            @ApiParam(value = "新密码", required = true) @RequestParam String newPassword) {

        synchronized (this) {
            service.modifyPassword(SecurityUtil.getCurrentUserId(), oldPassword, newPassword);
        }
        return success();
    }
}
