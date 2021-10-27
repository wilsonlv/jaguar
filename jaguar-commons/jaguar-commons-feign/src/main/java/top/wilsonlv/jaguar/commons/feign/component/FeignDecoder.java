package top.wilsonlv.jaguar.commons.feign.component;

import feign.FeignException;
import feign.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.SpringDecoder;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import top.wilsonlv.jaguar.commons.web.JsonResult;
import top.wilsonlv.jaguar.commons.web.exception.impl.CheckedException;

import java.io.IOException;
import java.lang.reflect.Type;

/**
 * @author lvws
 * @since 2021/8/18
 */
@Slf4j
@Configuration
public class FeignDecoder extends SpringDecoder {

    public FeignDecoder(@Autowired ObjectFactory<HttpMessageConverters> messageConverters) {
        super(messageConverters);
    }

    @Override
    public Object decode(Response response, Type type) throws IOException, FeignException {
        Object decode = super.decode(response, type);
        JsonResult<?> result = (JsonResult<?>) decode;
        if (result.getResultCode() != HttpStatus.OK.value()) {
            throw new CheckedException(result.getMessage());
        }
        return result;
    }

}
