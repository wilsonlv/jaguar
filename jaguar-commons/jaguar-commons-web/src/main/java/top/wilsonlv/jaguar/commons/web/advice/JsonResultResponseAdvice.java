package top.wilsonlv.jaguar.commons.web.advice;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.MethodParameter;
import org.springframework.core.NamedThreadLocal;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
import top.wilsonlv.jaguar.commons.web.JsonResult;
import top.wilsonlv.jaguar.commons.web.ResultCode;
import top.wilsonlv.jaguar.commons.web.properties.JaguarWebProperties;

import java.util.HashSet;
import java.util.Set;

/**
 * @author lvws
 * @since 2021/8/10
 */
@ControllerAdvice
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "jaguar.web", name = "json-result-response-enable", havingValue = "true", matchIfMissing = true)
public class JsonResultResponseAdvice implements ResponseBodyAdvice<Object>, InitializingBean {

    public static final ThreadLocal<JsonResult<?>> JSON_RESULT = new NamedThreadLocal<>("JsonResult");

    private final Set<String> ignoreUrls = new HashSet<>();

    private final JaguarWebProperties jaguarWebProperties;

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  ServerHttpRequest request, ServerHttpResponse response) {

        AntPathMatcher antPathMatcher = new AntPathMatcher();
        for (String ignoreUrl : ignoreUrls) {
            if (antPathMatcher.match(ignoreUrl, request.getURI().getPath())) {
                return body;
            }
        }

        ServletServerHttpResponse httpResponse = (ServletServerHttpResponse) response;
        int status = httpResponse.getServletResponse().getStatus();

        if (!(body instanceof JsonResult)) {
            ResultCode resultCode = ResultCode.fromValue(status);
            if (resultCode == null) {
                return body;
            } else {
                body = new JsonResult<>(resultCode, body, null);
            }
        }
        JSON_RESULT.set((JsonResult<?>) body);
        return body;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        ignoreUrls.add("/swagger-ui/**");
        ignoreUrls.add("/swagger-resources/**");
        ignoreUrls.add("/v2/**");
        ignoreUrls.add("/druid/**");
        ignoreUrls.add("/actuator/**");
        ignoreUrls.add("*.html");
        ignoreUrls.add("*.css");
        ignoreUrls.add("*.js");
        ignoreUrls.add("*.png");
        ignoreUrls.add("*.ico");
        ignoreUrls.add("*.ttf");
        ignoreUrls.add("*.woff");

        if (CollectionUtils.isEmpty(jaguarWebProperties.getJsonResultResponseIgnoreUrls())) {
            return;
        }
        ignoreUrls.addAll(jaguarWebProperties.getJsonResultResponseIgnoreUrls());
    }

}
