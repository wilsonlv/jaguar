package com.jaguar.process.model.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.jaguar.core.base.BaseModel;
import com.jaguar.process.enums.DefinitionType;

/**
 * <p>
 * <p>
 * </p>
 *
 * @author lvws
 * @since 2019-03-06
 */
@TableName("draft_definition")
public class DraftDefinition extends BaseModel {

    private static final long serialVersionUID = 1L;

    /**
     * 名称
     */
    @TableField("element_id")
    private String elementId;
    /**
     * 名称
     */
    @TableField("name_")
    private String name;
    /**
     * 类型（FORM：表单，FLOW：流程）
     */
    @TableField("definition_type")
    private DefinitionType definitionType;
    /**
     * 内容
     */
    @TableField("context_")
    private String context;
    /**
     * 版本
     */
    @TableField("version_")
    private Integer version;

    public String getElementId() {
        return elementId;
    }

    public void setElementId(String elementId) {
        this.elementId = elementId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DefinitionType getDefinitionType() {
        return definitionType;
    }

    public void setDefinitionType(DefinitionType definitionType) {
        this.definitionType = definitionType;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

}