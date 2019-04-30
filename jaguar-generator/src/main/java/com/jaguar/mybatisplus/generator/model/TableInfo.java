package com.jaguar.mybatisplus.generator.model;

import com.jaguar.core.base.BaseModel;
import lombok.Data;

/**
 * Created by lvws on 2019/4/30.
 */
@Data
public class TableInfo extends BaseModel {

    private String tableName;
    private String tableComment;
    private String parentPackage;
    private String author;
    private String outputDir;

}
