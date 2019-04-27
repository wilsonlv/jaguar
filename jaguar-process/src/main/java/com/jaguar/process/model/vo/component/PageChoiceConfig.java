package com.jaguar.process.model.vo.component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lvws on 2019/4/26.
 */
public class PageChoiceConfig extends ComponentConfig {

    /**
     * 数据源接口
     */
    private String dataSource;
    /**
     * 展示列
     */
    private List<FieldColumn> showColumns = new ArrayList<>();
    /**
     * 字段值的列key
     */
    private String valueColumnKey;
    /**
     * 查询参数
     */
    private List<FieldColumn> searchParams = new ArrayList<>();
    /**
     * 是否多选
     */
    private boolean multipart;


    public String getDataSource() {
        return dataSource;
    }

    public void setDataSource(String dataSource) {
        this.dataSource = dataSource;
    }

    public List<FieldColumn> getShowColumns() {
        return showColumns;
    }

    public void setShowColumns(List<FieldColumn> showColumns) {
        this.showColumns = showColumns;
    }

    public String getValueColumnKey() {
        return valueColumnKey;
    }

    public void setValueColumnKey(String valueColumnKey) {
        this.valueColumnKey = valueColumnKey;
    }

    public List<FieldColumn> getSearchParams() {
        return searchParams;
    }

    public void setSearchParams(List<FieldColumn> searchParams) {
        this.searchParams = searchParams;
    }

    public boolean isMultipart() {
        return multipart;
    }

    public void setMultipart(boolean multipart) {
        this.multipart = multipart;
    }

}
