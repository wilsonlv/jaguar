package org.jaguar.modules.workflow.model.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

/**
 * @author lvws
 * @since 2019/5/16.
 */
@Data
public class FormDataDTO implements Serializable {

    @NotEmpty(message = "表单字段key为非空")
    private String key;
    @NotEmpty(message = "表单字段value为非空")
    private String value;

}
