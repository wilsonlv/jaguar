package org.jaguar.modules.jasperreport.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jaguar.core.base.BaseModel;
import org.jaguar.modules.document.model.Document;

import javax.validation.constraints.NotBlank;

/**
 * <p>
 * jasperReport模板表
 * </p>
 *
 * @author lvws
 * @since 2020-06-03
 */
@Data
@ApiModel
@EqualsAndHashCode(callSuper = true)
@TableName("jaguar_modules_jasper_report_template")
public class Template extends BaseModel {

    private static final long serialVersionUID = 1L;

    /**
     * 模板名称
     */
    @NotBlank(message = "模板名称为非空")
    @ApiModelProperty(value = "模板名称", required = true)
    @TableField("template_name")
    private String templateName;
    /**
     * 模板类型
     */
    @NotBlank(message = "模板类型为非空")
    @ApiModelProperty(value = "模板类型", required = true)
    @TableField("template_type")
    private String templateType;
    /**
     * 文档ID
     */
    @ApiModelProperty(value = "文档ID")
    @TableField("document_id")
    private Long documentId;

    @TableField(exist = false)
    private Document document;

}