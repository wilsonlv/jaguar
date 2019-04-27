package com.jaguar.process.model.vo.component;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lvws on 2019/4/26.
 */
@Data
public class PageChoiceConfig extends ComponentConfig {

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
    private String valueKey;
    /**
     * 展示列
     */
    private List<FieldColumn> showColumns = new ArrayList<>();
    /**
     * 查询参数
     */
    private List<FieldColumn> searchParams = new ArrayList<>();

}
