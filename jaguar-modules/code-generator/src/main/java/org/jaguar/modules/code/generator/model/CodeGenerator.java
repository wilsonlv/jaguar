package org.jaguar.modules.code.generator.model;

import lombok.EqualsAndHashCode;
import org.jaguar.core.base.BaseModel;
import lombok.Data;

/**
 * Created by lvws on 2019/4/30.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CodeGenerator extends BaseModel {

    private String tableName;
    private String tableComment;
    private String moduleName;
    private String parentPackage;
    private String author;
    private String outputDir;

}
