package org.jaguar.modules.code.generator.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jaguar.core.base.BaseModel;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

/**
 * Created by lvws on 2019/4/30.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CodeGenerator extends BaseModel {

    @NotBlank(message = "表名为非空")
    private String tableName;
    @NotBlank(message = "作者为非空")
    private String author;

    private String tableComment;
    private String moduleName;
    private String parentPackage;
    private String outputDir;

    private LocalDateTime createTime;

}
