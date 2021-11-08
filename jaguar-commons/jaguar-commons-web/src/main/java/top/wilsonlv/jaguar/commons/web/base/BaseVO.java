package top.wilsonlv.jaguar.commons.web.base;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import top.wilsonlv.jaguar.commons.web.exception.impl.CheckedException;

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
    @ApiModelProperty(value = "实体ID")
    private Long id;
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;
    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

}
