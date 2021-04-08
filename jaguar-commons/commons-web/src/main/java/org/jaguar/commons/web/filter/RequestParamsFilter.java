package org.jaguar.commons.web.filter;

import lombok.extern.slf4j.Slf4j;
import org.jaguar.commons.utils.DateUtil;
import org.jaguar.commons.utils.IpUtil;
import org.springframework.core.annotation.Order;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Enumeration;

/**
 * @author lvws
 * @since 2017/3/28.
 */
@Slf4j
@Order(value = 1)
@WebFilter(urlPatterns = "/*")
public class RequestParamsFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        LocalDateTime accessTime = LocalDateTime.now();

        log.info("====================请求来啦====================");
        log.info("请求uri：{}", request.getRequestURI());
        log.info("请求方式：{}", request.getMethod());
        log.info("客户端ip: {}", IpUtil.getHost(request));

        Cookie[] cookies = request.getCookies();
        if (cookies != null && cookies.length > 0) {
            for (Cookie cookie : cookies) {
                log.info("请求cookie：{}，ID：{}", cookie.getName(), cookie.getValue());
            }
        }

        log.info("请求参数：");
        Enumeration<String> parameterNames = servletRequest.getParameterNames();
        while (parameterNames.hasMoreElements()) {
            String name = parameterNames.nextElement();
            log.info(name + "==" + servletRequest.getParameter(name));
        }
        filterChain.doFilter(servletRequest, servletResponse);

        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Headers", "*");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Methods", "POST,GET,PUT,OPTIONS,DELETE");

        LocalDateTime endTime = LocalDateTime.now();
        long duration = Duration.between(accessTime, endTime).toMillis();

        String message = "响应uri: {}; 响应状态码：{}; 开始时间: {}; 结束时间: {}; 耗时: {}s;";
        log.info(message, request.getRequestURI(), response.getStatus(),
                DateUtil.formatDateTime(accessTime, DateUtil.DateTimePattern.YYYY_MM_DD_HH_MM_SS_SSS),
                DateUtil.formatDateTime(endTime, DateUtil.DateTimePattern.YYYY_MM_DD_HH_MM_SS_SSS),
                duration / 1000.000);
        log.info("====================完成响应====================");
    }

    @Override
    public void destroy() {

    }
}
