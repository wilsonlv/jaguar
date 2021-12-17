package top.wilsonlv.jaguar.cloud.upms.sdk.dto;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import top.wilsonlv.jaguar.cloud.upms.sdk.enums.ClientType;
import top.wilsonlv.jaguar.cloud.upms.sdk.enums.UserType;

import java.io.Serializable;
import java.util.Map;

/**
 * @author lvws
 * @since 2021/11/5
 */
@Data
public class OAuthClientAdditionalInfo implements Serializable {

    private Boolean thirdParty;

    private ClientType clientType;

    private UserType userType;

    private Boolean enable;

    public String toJsonString() {
        return JSONObject.toJSONString(this);
    }

    public static OAuthClientAdditionalInfo parse(String additionalInformation) {
        return JSONObject.parseObject(additionalInformation, OAuthClientAdditionalInfo.class);
    }

    public static OAuthClientAdditionalInfo parse(Map<String, Object> additionalInformation) {
        return parse(JSONObject.toJSONString(additionalInformation));
    }

}
