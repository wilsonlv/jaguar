package top.wilsonlv.jaguar.openfeign.component;

import lombok.Data;

import java.io.Serializable;

/**
 * @author lvws
 * @since 2021/11/17
 */
@Data
public class FeignExceptionInfo implements Serializable {

    private String url;

    private String server;

    private String method;

    private String body;

}
