package org.jaguar.core.base.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.session.Session;
import org.jaguar.commons.utils.IdentifyingCode;
import org.jaguar.core.base.BaseController;
import org.jaguar.core.exception.CheckedException;
import org.jaguar.core.web.LoginUtil;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static org.jaguar.core.Constant.PIC_VERIFICATION_CODE;

/**
 * @author lvws
 * @since 2020/5/21
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/verify_code")
@Api(tags = "验证码管理")
public class VerifyCodeController extends BaseController {

    @ApiOperation(value = "获取图片验证码")
    @GetMapping
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
    public static void verifyCode(String verifyCode) {
        Session session = LoginUtil.getSession();
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

}
