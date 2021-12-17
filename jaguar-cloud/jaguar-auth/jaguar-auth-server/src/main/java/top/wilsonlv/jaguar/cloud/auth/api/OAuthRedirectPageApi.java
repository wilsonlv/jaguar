package top.wilsonlv.jaguar.cloud.auth.api;

import top.wilsonlv.jaguar.commons.web.response.JsonResult;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.provider.AuthorizationRequest;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @author lvws
 * @since 2021/7/3
 */
@RestController
@RequestMapping("/oauthRedirectPage")
@SessionAttributes({OAuthRedirectPageApi.AUTHORIZATION_REQUEST_ATTR_NAME, OAuthRedirectPageApi.ORIGINAL_AUTHORIZATION_REQUEST_ATTR_NAME})
public class OAuthRedirectPageApi {

    static final String AUTHORIZATION_REQUEST_ATTR_NAME = "authorizationRequest";

    static final String ORIGINAL_AUTHORIZATION_REQUEST_ATTR_NAME = "org.springframework.security.oauth2.provider.endpoint.AuthorizationEndpoint.ORIGINAL_AUTHORIZATION_REQUEST";

    @GetMapping("/confirm_access")
    public JsonResult<AuthorizationRequest> confirmAccess(AuthorizationRequest authorizationRequest) {
        return JsonResult.success(authorizationRequest);
    }

    @GetMapping("/error")
    public Object error(HttpServletRequest request) {
        Object error = request.getAttribute("error");
        if (error instanceof OAuth2Exception) {
            OAuth2Exception oauthError = (OAuth2Exception) error;
            return new ResponseEntity<>(oauthError, HttpStatus.OK);
        }
        return JsonResult.fail(String.valueOf(error));
    }

}
