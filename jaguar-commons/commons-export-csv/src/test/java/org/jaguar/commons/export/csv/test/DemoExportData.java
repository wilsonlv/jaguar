package org.jaguar.commons.export.csv.test;

import lombok.Data;

import java.io.Serializable;

/**
 * @author lvws
 * @since 2020/12/25
 */
@Data
public class DemoExportData implements Serializable {

    private String name;
    private Integer sex;

    public DemoExportData(String name, Integer sex) {
        this.name = name;
        this.sex = sex;
    }
}
