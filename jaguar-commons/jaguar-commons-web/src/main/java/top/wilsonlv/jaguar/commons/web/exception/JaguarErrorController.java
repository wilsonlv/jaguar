package top.wilsonlv.jaguar.commons.web.exception;

import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.web.servlet.error.AbstractErrorController;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.wilsonlv.jaguar.commons.web.JsonResult;
import top.wilsonlv.jaguar.commons.web.ResultCode;

import javax.annotation.Resource;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;
import java.util.Map;

/**
 * @author lvws
 * @since 2021/11/17
 */
@RestController
@RequestMapping("${server.error.path:${error.path:/error}}")
@AutoConfigureBefore(ErrorMvcAutoConfiguration.class)
public class JaguarErrorController extends AbstractErrorController {

    private final ErrorAttributes errorAttributes;

    public JaguarErrorController(ErrorAttributes errorAttributes) {
        super(errorAttributes);
        this.errorAttributes = errorAttributes;
    }

    @Override
    @Deprecated
    public String getErrorPath() {
        return null;
    }

    protected ResultCode getResultCode(HttpServletRequest request) {
        Integer statusCode = (Integer) request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        if (statusCode == null) {
            return ResultCode.INTERNAL_SERVER_ERROR;
        }
        try {
            return ResultCode.fromValue(statusCode);
        } catch (Exception ex) {
            return ResultCode.INTERNAL_SERVER_ERROR;
        }
    }

    @RequestMapping
    public JsonResult<?> error(HttpServletRequest request, HttpServletResponse response) {
        ResultCode resultCode = getResultCode(request);
        response.setStatus(ResultCode.OK.getValue());
        return new JsonResult<>(resultCode, null, null);
    }

}
