package top.wilsonlv.jaguar.cloud.auth.component.oauth.exception;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
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
    public void serialize(JaguarOauth2Exception value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeObject(new JsonResult<>(ResultCode.CONFLICT, null, value.getMessage()));
    }
}
