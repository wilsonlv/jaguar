package top.wilsonlv.jaguar.commons.web.base;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author lvws
 * @since 2021/8/16
 */
@Data
public abstract class BaseModifyDTO implements Serializable {

    @NotNull
    private Long id;

}
