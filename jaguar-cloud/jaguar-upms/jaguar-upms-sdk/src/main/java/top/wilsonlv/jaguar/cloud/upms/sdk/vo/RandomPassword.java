package top.wilsonlv.jaguar.cloud.upms.sdk.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author lvws
 * @since 2021/11/5
 */
@Data
public class RandomPassword implements Serializable {

    private String password;

    private String encode;

    public RandomPassword(String password, String encode) {
        this.password = password;
        this.encode = encode;
    }
}
