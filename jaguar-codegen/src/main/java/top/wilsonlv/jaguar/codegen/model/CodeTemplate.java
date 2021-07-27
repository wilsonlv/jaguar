package top.wilsonlv.jaguar.codegen.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import top.wilsonlv.jaguar.commons.basecrud.BaseModel;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

/**
 * 代码模板
 *
 * @author lvws
 * @since 2021/6/7
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("jaguar_codegen_code_template")
public class CodeTemplate extends BaseModel {

    private static final long serialVersionUID = 1L;

    /**
     * 模板名称
     */
    @NotBlank
    @TableField("code_template_name")
    private String codeTemplateName;

    /**
     * 模板文件
     */
    @NotBlank
    @TableField("code_template_file")
    private String codeTemplateFile;

    /**
     * 模板版本
     */
    @TableField("code_template_version")
    private Integer codeTemplateVersion;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private LocalDateTime createTime;

    /**
     * 文件名
     */
    @NotBlank
    @TableField("file_name")
    private String fileName;

    /**
     * 文件路径
     */
    @NotBlank
    @TableField("file_path")
    private String filePath;

}
