/*
 * Copyright 2009-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.security.oauth2.provider.endpoint;

import lombok.Setter;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.error.DefaultWebResponseExceptionTranslator;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;
import org.springframework.security.oauth2.provider.token.AccessTokenConverter;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * Controller which decodes access tokens for clients who are not able to do so (or where opaque token values are used).
 *
 * @author Luke Taylor
 * @author Joel D'sa
 */
@FrameworkEndpoint
public class CheckTokenEndpoint {

    private final ResourceServerTokenServices resourceServerTokenServices;

    @Setter
    private AccessTokenConverter accessTokenConverter = new CheckTokenAccessTokenConverter();

    @Setter
    private WebResponseExceptionTranslator<OAuth2Exception> exceptionTranslator = new DefaultWebResponseExceptionTranslator();

    public CheckTokenEndpoint(ResourceServerTokenServices resourceServerTokenServices) {
        this.resourceServerTokenServices = resourceServerTokenServices;
    }


    @RequestMapping(value = "/oauth/check_token")
    @ResponseBody
    public Map<String, ?> checkToken(@RequestParam("token") String value) {

        OAuth2AccessToken token = resourceServerTokenServices.readAccessToken(value);
        if (token == null) {
            throw new InvalidTokenException("Token was not recognised");
        }

        if (token.isExpired()) {
            throw new InvalidTokenException("Token has expired");
        }

        OAuth2Authentication authentication = resourceServerTokenServices.loadAuthentication(token.getValue());

        return accessTokenConverter.convertAccessToken(token, authentication);
    }

    static class CheckTokenAccessTokenConverter implements AccessTokenConverter {
        private final AccessTokenConverter accessTokenConverter;

        CheckTokenAccessTokenConverter() {
            this(new DefaultAccessTokenConverter());
        }

        CheckTokenAccessTokenConverter(AccessTokenConverter accessTokenConverter) {
            this.accessTokenConverter = accessTokenConverter;
        }

        @Override
        public Map<String, ?> convertAccessToken(OAuth2AccessToken token, OAuth2Authentication authentication) {
            @SuppressWarnings("unchecked") Map<String, Object> claims = (Map<String, Object>)
                    this.accessTokenConverter.convertAccessToken(token, authentication);

            // gh-1070
            claims.put("active", true);        // Always true if token exists and not expired

            return claims;
        }

        @Override
        public OAuth2AccessToken extractAccessToken(String value, Map<String, ?> map) {
            return this.accessTokenConverter.extractAccessToken(value, map);
        }

        @Override
        public OAuth2Authentication extractAuthentication(Map<String, ?> map) {
            return this.accessTokenConverter.extractAuthentication(map);
        }
    }
}
