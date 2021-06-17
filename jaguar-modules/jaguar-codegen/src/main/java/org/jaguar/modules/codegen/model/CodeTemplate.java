package org.jaguar.modules.codegen.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jaguar.commons.basecrud.BaseModel;
import org.jaguar.modules.codegen.enums.CodeTemplateType;

import javax.validation.constraints.NotBlank;

/**
 * 代码模板
 *
 * @author lvws
 * @since 2021/6/7
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("jaguar_modules_codegen_code_template")
public class CodeTemplate extends BaseModel {

    private static final long serialVersionUID = 1L;

    /**
     * 模板类型
     */
    @NotBlank
    @TableField("code_template_type")
    private CodeTemplateType codeTemplateType;

    /**
     * 模板文件
     */
    @NotBlank
    @TableField("code_template_file")
    private String codeTemplateFile;

    /**
     * 模板版本
     */
    @NotBlank
    @TableField("code_template_version")
    private Integer codeTemplateVersion;

}
