package org.jaguar.modules.workflow.model.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * Created by lvws on 2019/5/16.
 */
@Data
public class FormDataDTO implements Serializable {

    @NotBlank(message = "表单字段key为非空")
    private String key;
    @NotBlank(message = "表单字段value为非空")
    private String value;

}
