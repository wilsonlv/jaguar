package top.wilsonlv.jaguar.cloud.auth.component.oauth.exception;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import feign.FeignException;
import top.wilsonlv.jaguar.cloud.services.JaguarSever;
import top.wilsonlv.jaguar.commons.web.JsonResult;
import top.wilsonlv.jaguar.commons.web.ResultCode;

import java.io.IOException;

/**
 * @author lvws
 * @since 2021/7/3
 */
public class JaguarOauth2ExceptionSerializer extends StdSerializer<JaguarOauth2Exception> {

    protected JaguarOauth2ExceptionSerializer() {
        super(JaguarOauth2Exception.class);
    }

    @Override
    public void serialize(JaguarOauth2Exception exception, JsonGenerator gen, SerializerProvider provider) throws IOException {
        JsonResult<?> result = null;

        Throwable cause = exception.getCause().getCause();
        if (cause != null) {
            if (cause instanceof FeignException) {
                FeignException feignException = (FeignException) cause;
                String url = feignException.request().url();
                for (JaguarSever jaguarSever : JaguarSever.values()) {
                    if (url.contains(jaguarSever.getName())) {
                        result = new JsonResult<>(ResultCode.fromValue(feignException.status()), jaguarSever.getAlias(), exception.getMessage());
                        break;
                    }
                }
            }
        }

        if (result == null) {
            result = new JsonResult<>(ResultCode.CONFLICT, null, exception.getMessage());
        }
        gen.writeObject(result);
    }
}
