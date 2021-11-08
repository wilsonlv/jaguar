package top.wilsonlv.jaguar.commons.basecrud;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.beans.BeanUtils;
import top.wilsonlv.jaguar.commons.web.exception.impl.CheckedException;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author lvws
 * @since 2019/5/6.
 */
@Data
@EqualsAndHashCode(callSuper = false)
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class BaseModel implements Serializable {

    @TableId(value = "id_", type = IdType.ASSIGN_ID)
    private Long id;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableField("remark_")
    private String remark;

    @JsonIgnore
    @TableField("deleted_")
    @TableLogic
    private Boolean deleted;

    public <V> V toVo(Class<V> clazz) {
        V vo;
        try {
            vo = clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new CheckedException(e);
        }
        BeanUtils.copyProperties(this, vo);
        return vo;
    }

}
