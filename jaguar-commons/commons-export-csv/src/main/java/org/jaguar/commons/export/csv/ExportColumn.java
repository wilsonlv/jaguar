package org.jaguar.commons.export.csv;

import lombok.Data;

import java.io.Serializable;

/**
 * @author lvws
 * @since 2020/12/25
 */
@Data
public class ExportColumn implements Serializable {

    private String key;
    private String label;

    public ExportColumn(String key, String label) {
        this.key = key;
        this.label = label;
    }
}
