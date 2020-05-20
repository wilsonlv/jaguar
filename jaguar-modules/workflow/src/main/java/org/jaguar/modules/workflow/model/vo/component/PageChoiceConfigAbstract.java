package org.jaguar.modules.workflow.model.vo.component;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lvws on 2019/4/26.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PageChoiceConfigAbstract extends AbstractComponentConfig {

    /**
     * 数据源接口
     */
    private String dataSource;
    /**
     * 是否多选
     */
    private boolean multipart;
    /**
     * 字段值的取值字段
     */
    private String valueColunm;
    /**
     * 展示列
     */
    private List<FieldColumn> showColumns = new ArrayList<>();
    /**
     * 查询参数
     */
    private List<FieldColumn> searchParams = new ArrayList<>();

}
