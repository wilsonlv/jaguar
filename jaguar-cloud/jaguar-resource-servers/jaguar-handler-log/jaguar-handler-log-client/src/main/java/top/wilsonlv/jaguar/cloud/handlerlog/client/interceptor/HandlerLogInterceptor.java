package top.wilsonlv.jaguar.cloud.handlerlog.client.interceptor;

import cn.hutool.core.exceptions.ExceptionUtil;
import com.alibaba.fastjson.JSONObject;
import cz.mallat.uasparser.OnlineUpdater;
import cz.mallat.uasparser.UASparser;
import cz.mallat.uasparser.UserAgentInfo;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.NamedThreadLocal;
import org.springframework.http.HttpHeaders;
import org.springframework.jms.JmsException;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import top.wilsonlv.jaguar.cloud.handlerlog.client.HandlerLogConstant;
import top.wilsonlv.jaguar.cloud.handlerlog.client.dto.HandlerLogSaveDTO;
import top.wilsonlv.jaguar.commons.web.response.JsonResult;
import top.wilsonlv.jaguar.commons.web.response.JsonResultResponseAdvice;
import top.wilsonlv.jaguar.commons.web.util.WebUtil;
import top.wilsonlv.jaguar.oauth2.model.SecurityUser;
import top.wilsonlv.jaguar.oauth2.util.SecurityUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;


/**
 * @author lvws
 * @since 2018/11/12.
 */
@Slf4j
@Component
@ConditionalOnProperty(prefix = "jaguar.handler-log", name = "enable", havingValue = "true", matchIfMissing = true)
public class HandlerLogInterceptor implements HandlerInterceptor {

    /**
     * 当前线程访问信息
     */
    public static final ThreadLocal<HandlerLogSaveDTO> HANDLER_LOG = new NamedThreadLocal<>("HANDLER_LOG");

    private static final String ACTUATOR = "/actuator";
    private static final String ERROR = "/error";

    private final UASparser uasParser;
    private final JmsTemplate jmsQueueTemplate;

    public HandlerLogInterceptor(JmsTemplate jmsQueueTemplate) throws IOException {
        this.uasParser = new UASparser(OnlineUpdater.getVendoredInputStream());
        this.jmsQueueTemplate = jmsQueueTemplate;
    }

    private String getUserAgent(HttpServletRequest request) {
        UserAgentInfo userAgentInfo;
        try {
            userAgentInfo = uasParser.parse(request.getHeader("user-agent"));
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            return null;
        }

        return userAgentInfo.getOsName() + " " + userAgentInfo.getType() + " " + userAgentInfo.getUaName();
    }

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) {
        String requestUri = request.getRequestURI();
        if (handler instanceof HandlerMethod && !request.getRequestURI().startsWith(ACTUATOR) && !ERROR.equals(requestUri)) {
            ApiOperation apiOperation = ((HandlerMethod) handler).getMethod().getAnnotation(ApiOperation.class);

            String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
            if (!StringUtils.hasText(authorization)) {
                authorization = request.getParameter("access_token");
            }

            HttpSession session = request.getSession(false);

            HandlerLogSaveDTO handlerLog = new HandlerLogSaveDTO();
            handlerLog.setSessionId(session != null ? session.getId() : null);
            handlerLog.setAuthorization(authorization);
            handlerLog.setAccessTime(LocalDateTime.now());
            handlerLog.setClientHost(WebUtil.getHost(request));
            handlerLog.setRequestUri(request.getServletPath());
            handlerLog.setApiOperation(apiOperation != null ? apiOperation.value() : null);
            handlerLog.setMethod(request.getMethod());
            handlerLog.setUserAgent(getUserAgent(request));
            handlerLog.setParameters(JSONObject.toJSONString(request.getParameterMap()));

            SecurityUser currentUser = SecurityUtil.getCurrentUser();
            if (currentUser != null) {
                handlerLog.setCreateBy(currentUser.getUsername());
                handlerLog.setCreateUserId(currentUser.getId());
                log.debug("用户id: {}", handlerLog.getCreateBy());
            } else {
                log.debug("用户[{}@{}]匿名请求", handlerLog.getClientHost(), handlerLog.getUserAgent());
            }
            HANDLER_LOG.set(handlerLog);
        }
        return true;
    }

    @Override
    public void afterCompletion(@NonNull final HttpServletRequest request, @NonNull final HttpServletResponse response,
                                @NonNull Object handler, final Exception handlerException) {
        String requestUri = request.getRequestURI();
        if (handler instanceof HandlerMethod && !requestUri.startsWith(ACTUATOR) && !ERROR.equals(requestUri)) {
            HandlerLogSaveDTO handlerLog = HANDLER_LOG.get();
            HANDLER_LOG.remove();

            long duration = Duration.between(handlerLog.getAccessTime(), LocalDateTime.now()).toMillis();
            handlerLog.setDuration(duration);
            handlerLog.setStatus(response.getStatus());
            handlerLog.setErrorMsg(ExceptionUtil.getMessage(handlerException));
            JsonResult<?> jsonResult = JsonResultResponseAdvice.JSON_RESULT.get();
            if (jsonResult != null) {
                handlerLog.setJsonResult(jsonResult.toJsonStr());
            }

            try {
                log.debug("send handler log");
                jmsQueueTemplate.convertAndSend(HandlerLogConstant.DESTINATION_HANDLER_LOG, handlerLog);
            } catch (JmsException e) {
                log.debug(e.getMessage());
            }
        }

        JsonResultResponseAdvice.JSON_RESULT.remove();
    }

}
