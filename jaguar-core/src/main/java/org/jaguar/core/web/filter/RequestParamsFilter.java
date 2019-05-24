package org.jaguar.core.web.filter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;

/**
 * Created by lvws on 2017/3/28.
 */
@WebFilter(urlPatterns = "/*")
public class RequestParamsFilter implements Filter {

    private static final Logger logger = LogManager.getLogger(RequestParamsFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        logger.info("====================请求来啦====================");
        logger.info("请求uri：{}", request.getRequestURI());
        logger.info("请求参数：");
        Enumeration<String> parameterNames = servletRequest.getParameterNames();
        while (parameterNames.hasMoreElements()) {
            String name = parameterNames.nextElement();
            logger.info(name + "==" + servletRequest.getParameter(name));
        }
        filterChain.doFilter(servletRequest, servletResponse);

        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Headers", "*");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Methods", "POST,GET,PUT,OPTIONS,DELETE");

        logger.info("====================完成响应====================");
    }

    @Override
    public void destroy() {

    }
}
