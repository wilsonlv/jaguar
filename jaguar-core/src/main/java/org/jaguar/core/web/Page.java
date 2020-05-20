package org.jaguar.core.web;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.Data;

import java.util.List;

/**
 * @author lvws
 * @since 2019/5/7.
 */
@Data
public class Page<T> {

    private List<T> records;
    private int total;
    private int size;
    private int current;

    public static <T> Page<T> convert(IPage<T> data) {
        if (data == null) {
            return null;
        }

        Page<T> page = new Page<>();
        page.setRecords(data.getRecords());
        page.setCurrent(((Long) data.getCurrent()).intValue());
        page.setSize(((Long) data.getSize()).intValue());
        page.setTotal(((Long) data.getTotal()).intValue());
        return page;
    }
}
