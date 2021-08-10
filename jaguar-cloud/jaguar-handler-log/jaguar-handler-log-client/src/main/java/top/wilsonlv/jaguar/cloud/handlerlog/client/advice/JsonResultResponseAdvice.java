package top.wilsonlv.jaguar.cloud.handlerlog.client.advice;

import org.springframework.core.MethodParameter;
import org.springframework.core.NamedThreadLocal;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
import top.wilsonlv.jaguar.commons.web.JsonResult;

import javax.annotation.Nonnull;

/**
 * @author lvws
 * @since 2021/8/10
 */
@ControllerAdvice
public class JsonResultResponseAdvice implements ResponseBodyAdvice<Object> {

    public static final ThreadLocal<JsonResult<?>> JSON_RESULT = new NamedThreadLocal<>("JsonResult");

    @Override
    public boolean supports(@Nonnull MethodParameter returnType, @Nonnull Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, @Nonnull MethodParameter returnType, @Nonnull MediaType selectedContentType,
                                  @Nonnull Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  @Nonnull ServerHttpRequest request, @Nonnull ServerHttpResponse response) {
        if (body instanceof JsonResult) {
            JSON_RESULT.set((JsonResult<?>) body);
        }
        return body;
    }
}
