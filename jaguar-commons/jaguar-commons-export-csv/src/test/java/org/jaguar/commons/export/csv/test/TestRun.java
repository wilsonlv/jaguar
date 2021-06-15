package org.jaguar.commons.export.csv.test;

import junit.framework.TestCase;
import org.jaguar.commons.export.csv.ExportColumn;
import org.jaguar.commons.export.csv.ExportManager;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lvws
 * @since 2020/12/26
 */
public class TestRun extends TestCase {

    public void testExportManager() throws IOException {
        List<ExportColumn> exportColumnList = new ArrayList<>();
        exportColumnList.add(new ExportColumn("name", "学生姓名"));
        exportColumnList.add(new ExportColumn("sex", "学生性别"));
        exportColumnList.add(new ExportColumn("num", "学号"));

        try (ExportManager exportManager = new ExportManager(new FileOutputStream("demo.csv"), exportColumnList)) {
            List<DemoExportData> data = new ArrayList<>();
            data.add(new DemoExportData("张三", 1, 321324321312332L));
            data.add(new DemoExportData("李四", 0, 53454432345435L));
            exportManager.flush(data);

//            data = new ArrayList<>();
//            data.add(new DemoExportData("王五", 0));
//            data.add(new DemoExportData("赵六", 1));
//            exportManager.flush(data);
        }
    }

}
