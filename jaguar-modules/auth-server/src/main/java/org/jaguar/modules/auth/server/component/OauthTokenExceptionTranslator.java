package org.jaguar.modules.auth.server.component;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;
import org.springframework.stereotype.Component;

/**
 * @author lvws
 * @since 2021/6/8
 */
@Slf4j
@Component
public class OauthTokenExceptionTranslator implements WebResponseExceptionTranslator<OAuth2Exception> {

    @Override
    public ResponseEntity<OAuth2Exception> translate(Exception e) {
        OAuth2Exception oAuth2Exception = new OAuth2Exception(e.getMessage());
        return new ResponseEntity<>(oAuth2Exception, HttpStatus.OK);
    }
}
