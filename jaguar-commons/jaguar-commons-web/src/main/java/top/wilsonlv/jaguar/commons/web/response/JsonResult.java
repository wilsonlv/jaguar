package top.wilsonlv.jaguar.commons.web.response;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

/**
 * @author lvws
 * @since 2019/4/16.
 */
@Data
public class JsonResult<T> implements Serializable {

    public static final String SUCCESS_MSG = "成功";

    public static JsonResult<Void> success() {
        return new JsonResult<>(ResultCode.OK, null, SUCCESS_MSG);
    }

    public static <T> JsonResult<T> success(T data) {
        return new JsonResult<>(ResultCode.OK, data, SUCCESS_MSG);
    }

    public static JsonResult<Void> fail(String message) {
        return new JsonResult<>(ResultCode.CONFLICT, null, message);
    }

    private ResultCode resultCode = ResultCode.OK;

    private T data;

    private String message;

    private final Long timestamp = System.currentTimeMillis();

    public JsonResult() {
    }

    public JsonResult(ResultCode resultCode) {
        this.resultCode = resultCode;
    }

    public JsonResult(ResultCode resultCode, T data, String message) {
        this.resultCode = resultCode;
        this.data = data;
        this.message = message;
    }

    public JsonResult<T> setData(T data) {
        this.data = data;
        return this;
    }

    public JsonResult<T> setMessage(String message) {
        this.message = message;
        return this;
    }

    public int getResultCode() {
        return resultCode.getValue();
    }

    public String getMessage() {
        return StringUtils.isBlank(this.message) ? resultCode.getReasonPhrase() : this.message;
    }

    public String toJsonStr() {
        return JSONObject.toJSONString(this);
    }

}
