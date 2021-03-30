package org.jaguar.core.base;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author lvws
 * @since 2019/5/6.
 */
@Data
@EqualsAndHashCode
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class BaseModel implements Serializable {

    @TableId(value = "id_", type = IdType.ASSIGN_ID)
    private Long id;

    @TableField("create_time")
    private LocalDateTime createTime;

    @TableField("remark_")
    private String remark;

    @JsonIgnore
    @TableField("deleted_")
    @TableLogic
    private Boolean deleted;

}
