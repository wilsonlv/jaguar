package org.jaguar.cloud.auth.server.component.oauth.exception;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;

/**
 * @author lvws
 * @since 2021/7/3
 */
@JsonSerialize(using = JaguarOauth2ExceptionSerializer.class)
public class JaguarOauth2Exception extends OAuth2Exception {

    public JaguarOauth2Exception(String msg) {
        super(msg);
    }

}
