package top.wilsonlv.jaguar.commons.feign.component;

import feign.FeignException;
import feign.Request;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import top.wilsonlv.jaguar.commons.web.JsonResult;
import top.wilsonlv.jaguar.commons.web.ResultCode;

import java.nio.ByteBuffer;
import java.util.Optional;

/**
 * @author lvws
 * @since 2021/4/5
 */
@Slf4j
@Configuration
@RestControllerAdvice
public class FeignExceptionHandler {

    @ResponseBody
    @ExceptionHandler(value = FeignException.class)
    public JsonResult<FeignExceptionInfo> feignExceptionHandler(FeignException exception) {
        Request request = exception.request();
        Optional<ByteBuffer> byteBuffer = exception.responseBody();

        String url = request.url();
        int start = url.indexOf("//") + 2;
        int serverEnd = url.indexOf('/', start);
        int methodEnd = url.indexOf('?', serverEnd);

        FeignExceptionInfo feignExceptionInfo = new FeignExceptionInfo();
        feignExceptionInfo.setUrl(url);
        feignExceptionInfo.setServer(url.substring(start, serverEnd));
        feignExceptionInfo.setMethod(url.substring(serverEnd, methodEnd));
        byteBuffer.ifPresent(buffer -> feignExceptionInfo.setBody(new String(buffer.array())));

        return new JsonResult<>(ResultCode.fromValue(exception.status()), feignExceptionInfo, exception.getMessage());
    }

}
