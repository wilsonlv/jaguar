package org.jaguar.commons.web.mvc;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

/**
 * @author lvws
 * @since 2021/4/5
 */
public class Long2StringSerializer extends JsonSerializer<Long> {

    public static final Long2StringSerializer INSTANCE = new Long2StringSerializer();

    public static final Long JS_ACCURACY = 100000000L;

    @Override
    public void serialize(Long lang, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        if (lang < JS_ACCURACY) {
            jsonGenerator.writeNumber(lang);
        } else {
            jsonGenerator.writeString(lang.toString());
        }
    }
}
