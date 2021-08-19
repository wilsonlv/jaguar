package top.wilsonlv.jaguar.commons.feign.component;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import feign.FeignException;
import feign.Response;
import feign.Util;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @author lvws
 * @since 2021/8/18
 */
@Slf4j
@Configuration
public class FeignErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String methodKey, Response response) {
        try {
            // 这里直接拿到我们抛出的异常信息
            String message = Util.toString(response.body().asReader(StandardCharsets.UTF_8));
            try {
                JSONObject jsonObject = JSONObject.parseObject(message);
                return  FeignException.errorStatus(methodKey, response);
            } catch (JSONException e) {
                log.error(e.getMessage(), e);

            }
        } catch (IOException ignored) {
        }
        return decode(methodKey, response);
    }
}
