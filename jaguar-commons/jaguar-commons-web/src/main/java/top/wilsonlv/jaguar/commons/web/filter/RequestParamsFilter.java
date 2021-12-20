package top.wilsonlv.jaguar.commons.web.filter;

import cn.hutool.core.date.DatePattern;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import top.wilsonlv.jaguar.commons.web.util.WebUtil;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Enumeration;

/**
 * @author lvws
 * @since 2017/3/28.
 */
@Slf4j
public class RequestParamsFilter implements Filter {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(DatePattern.ISO8601_PATTERN);

    private static final String ACTUATOR = "/actuator";

    private static final String ICO = ".ico";
    private static final String CSS = ".css";
    private static final String JS = ".js";
    private static final String HTML = ".html";

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String requestUri = request.getRequestURI();
        if (request.getMethod().equals(HttpMethod.OPTIONS.name())
                || requestUri.startsWith(ACTUATOR) || requestUri.endsWith(ICO)
                || requestUri.endsWith(CSS) || requestUri.endsWith(JS) || requestUri.endsWith(HTML)) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        LocalDateTime accessTime = LocalDateTime.now();

        log.info("====================请求来啦====================");
        log.info("请求uri：{}", request.getRequestURI());
        log.info("请求方式：{}", request.getMethod());
        log.info("客户端ip: {}", WebUtil.getHost(request));

        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.hasText(header)) {
            log.info("Authorization：{}", header);
        }

        if (log.isDebugEnabled()) {
            Cookie[] cookies = request.getCookies();
            if (cookies != null && cookies.length > 0) {
                for (Cookie cookie : cookies) {
                    log.debug("请求cookie：{}，ID：{}", cookie.getName(), cookie.getValue());
                }
            }
        }

        log.info("请求参数：");
        Enumeration<String> parameterNames = servletRequest.getParameterNames();
        while (parameterNames.hasMoreElements()) {
            String name = parameterNames.nextElement();
            log.info(name + "==" + servletRequest.getParameter(name));
        }

        filterChain.doFilter(servletRequest, servletResponse);

        LocalDateTime endTime = LocalDateTime.now();
        long duration = Duration.between(accessTime, endTime).toMillis();

        String message = "响应uri: {}; 响应状态码：{}; 开始时间: {}; 结束时间: {}; 耗时: {}s;";
        log.info(message, request.getRequestURI(), response.getStatus(),
                DATE_TIME_FORMATTER.format(accessTime),
                DATE_TIME_FORMATTER.format(endTime),
                duration / 1000.000);
        log.info("====================完成响应====================");
    }

}
