package org.jaguar.core;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * 尽量使用Charsets.UTF8而不是"UTF-8"，减少JDK里的Charset查找消耗.
 * <p>
 * 使用JDK7的StandardCharsets，同时留了标准名称的字符串
 *
 * @author lvws
 */
public interface Charsets {

    Charset UTF_8 = StandardCharsets.UTF_8;
    Charset US_ASCII = StandardCharsets.US_ASCII;
    Charset ISO_8859_1 = StandardCharsets.ISO_8859_1;
    Charset GB18030 = Charset.forName("GB18030");

    String UTF_8_NAME = UTF_8.name();
    String ASCII_NAME = US_ASCII.name();
    String ISO_8859_1_NAME = ISO_8859_1.name();
    String GB18030_NAME = GB18030.name();


}
