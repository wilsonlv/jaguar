package top.wilsonlv.jaguar.commons.web.base;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author lvws
 * @since 2021/8/17
 */
@Data
public class BaseVO implements Serializable {

    /**
     * 实体ID
     */
    private Long id;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

}
