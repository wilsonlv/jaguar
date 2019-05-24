package org.jaguar.modules.workflow.model.vo.component;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created by lvws on 2019/4/25.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class TreeChoiceConfig extends ComponentConfig {

    /**
     * 数据源接口
     */
    private String dataSource;
    /**
     * 是否多选
     */
    private boolean multipart;
    /**
     * 字段值的取值列
     */
    private String valueColumn;
    /**
     * 字段标签的取值列
     */
    private String labelColumn;
    /**
     * 字段备注的取值列
     */
    private String remarkColumn;
    /**
     * 孩子的取值列
     */
    private String childrenColumn;
    /**
     * 叶子结点的取值列
     */
    private String leafColumn;
    /*
     * 叶子结点样式
     */
    private String leafClass;
    /**
     * 根节点样式
     */
    private String rootClass;

}
