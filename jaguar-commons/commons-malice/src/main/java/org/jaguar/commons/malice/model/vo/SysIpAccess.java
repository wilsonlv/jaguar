package org.jaguar.commons.malice.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by lvws on 2018/11/9.
 */
@Data
public class SysIpAccess implements Serializable {

    /**
     * 地址
     */
    private String address;

    /**
     * 冻结IP
     */
    private Integer freezing;

    /**
     * 冻结时间
     */
    private Date freezingTime;

}
