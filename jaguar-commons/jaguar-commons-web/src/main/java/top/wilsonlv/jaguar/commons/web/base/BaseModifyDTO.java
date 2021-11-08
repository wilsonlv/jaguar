package top.wilsonlv.jaguar.commons.web.base;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

/**
 * @author lvws
 * @since 2021/8/16
 */
@Data
@EqualsAndHashCode(callSuper = true)
public abstract class BaseModifyDTO extends BaseDTO {

    @NotNull
    private Long id;

}
