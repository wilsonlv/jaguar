package top.wilsonlv.jaguar.commons.web.advice;

import org.springframework.core.MethodParameter;
import org.springframework.core.NamedThreadLocal;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
import top.wilsonlv.jaguar.commons.web.JsonResult;

/**
 * @author lvws
 * @since 2021/8/10
 */
@ControllerAdvice
public class JsonResultResponseAdvice implements ResponseBodyAdvice<Object> {

    public static final ThreadLocal<JsonResult<?>> JSON_RESULT = new NamedThreadLocal<>("JsonResult");

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  ServerHttpRequest request, ServerHttpResponse response) {
        if (!(body instanceof JsonResult)) {
            body = JsonResult.success(body);
        }
        JSON_RESULT.set((JsonResult<?>) body);
        return body;
    }
}
