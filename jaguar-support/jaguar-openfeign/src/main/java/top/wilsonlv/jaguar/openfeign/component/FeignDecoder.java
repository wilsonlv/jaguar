package top.wilsonlv.jaguar.openfeign.component;

import feign.FeignException;
import feign.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.SpringDecoder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import top.wilsonlv.jaguar.commons.web.response.JsonResult;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;

/**
 * @author lvws
 * @since 2021/8/18
 */
@Slf4j
@Component
public class FeignDecoder extends SpringDecoder {

    public FeignDecoder(@Autowired ObjectFactory<HttpMessageConverters> messageConverters) {
        super(messageConverters);
    }

    @Override
    public Object decode(Response response, Type type) throws IOException, FeignException {
        Object decode = super.decode(response, type);
        JsonResult<?> result = (JsonResult<?>) decode;
        if (result.getResultCode() != HttpStatus.OK.value()) {
            throw new FeignException.BadGateway(result.getMessage(), response.request(), result.toJsonStr().getBytes(StandardCharsets.UTF_8));
        }
        return result;
    }

}
