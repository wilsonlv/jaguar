package org.jaguar.modules.codegen.enums;

import lombok.Getter;

import java.io.File;

/**
 * @author lvws
 * @since 2021/6/17
 */
public enum CodeTemplateType {

    /**
     * 模板类型
     */
    ENTITY("entity",".java"),
    MAPPER("mapper","Mapper.java"),
    MAPPER_XML("mapper" + File.separator + "xml","Mapper.xml"),
    SERVICE("service","Service.java"),
    CONTROLLER("controller","Controller.java");

    @Getter
    private final String path;

    @Getter
    private final String fileNameSuffix;

    CodeTemplateType(String path,String fileNameSuffix){
        this.path = path;
        this.fileNameSuffix=fileNameSuffix;
    }

}
