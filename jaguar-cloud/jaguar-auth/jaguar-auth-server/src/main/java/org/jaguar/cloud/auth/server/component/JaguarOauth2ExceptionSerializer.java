package org.jaguar.cloud.auth.server.component;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.jaguar.commons.web.ResultCode;

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
        gen.writeStartObject();
        gen.writeNumberField("resultCode", ResultCode.CONFLICT.getValue());
        gen.writeStringField("message", value.getMessage());
        gen.writeNumberField("timestamp", System.currentTimeMillis());
        gen.writeEndObject();
    }
}
