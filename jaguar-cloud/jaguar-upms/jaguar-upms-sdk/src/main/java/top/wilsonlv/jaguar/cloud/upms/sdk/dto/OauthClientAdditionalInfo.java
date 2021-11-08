package top.wilsonlv.jaguar.cloud.upms.sdk.dto;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import top.wilsonlv.jaguar.commons.enums.ClientType;
import top.wilsonlv.jaguar.commons.enums.UserType;

import java.io.Serializable;
import java.util.Map;

/**
 * @author lvws
 * @since 2021/11/5
 */
@Data
public class OauthClientAdditionalInfo implements Serializable {

    private Boolean builtIn;

    private Boolean enable;

    private Boolean captcha;

    private ClientType clientType;

    private UserType userType;

    public static OauthClientAdditionalInfo parse(String additionalInformation) {
        return JSONObject.parseObject(additionalInformation, OauthClientAdditionalInfo.class);
    }

    public static OauthClientAdditionalInfo parse(Map<String, Object> additionalInformation) {
        return parse(JSONObject.toJSONString(additionalInformation));
    }

}
