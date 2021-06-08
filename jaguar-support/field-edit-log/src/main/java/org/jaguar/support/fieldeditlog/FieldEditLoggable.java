package org.jaguar.support.fieldeditlog;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jaguar.commons.basecrud.BaseModel;

import java.time.LocalDateTime;

/**
 * @author lvws
 * @since 2021/3/30.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class FieldEditLoggable extends BaseModel {

    @TableField("create_by")
    private Long createBy;

    @TableField("update_by")
    private Long updateBy;

}
