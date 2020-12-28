package org.jaguar.commons.export.csv;

import com.alibaba.fastjson.JSONObject;
import com.googlecode.aviator.AviatorEvaluator;

import java.io.Closeable;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @author lvws
 * @since 2020/12/25
 */
public class ExportManager implements Closeable {

    private final OutputStreamWriter writer;
    private final List<ExportColumn> exportColumnList;

    public ExportManager(OutputStream outputStream, List<ExportColumn> exportColumnList) throws IOException {
        this.writer = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8);
        this.exportColumnList = exportColumnList;

        this.build();
    }

    private void build() throws IOException {
        writer.write(new String(new byte[]{(byte) 0xEF, (byte) 0xBB, (byte) 0xBF}));

        for (int i = 0; i < exportColumnList.size(); i++) {
            ExportColumn exportColumn = exportColumnList.get(i);
            this.writer.append(exportColumn.getLabel());
            if (i != exportColumnList.size() - 1) {
                this.writer.append(',');
            } else {
                this.writer.append("\r\n");
            }
        }
    }

    public <T> void flush(List<T> data) throws IOException {
        for (Object itemObject : data) {
            JSONObject item = JSONObject.parseObject(JSONObject.toJSONString(itemObject));

            for (int i = 0; i < exportColumnList.size(); i++) {
                ExportColumn exportColumn = exportColumnList.get(i);
                Object result = AviatorEvaluator.execute(exportColumn.getKey(), item);
                String resultStr = String.valueOf(result).replaceAll(",", "，")
                        .replaceAll("'", "‘")
                        .replace("\"", "“");
                this.writer.write(resultStr + "\t");

                if (i != exportColumnList.size() - 1) {
                    this.writer.append(',');
                } else {
                    this.writer.append("\r\n");
                }
            }
        }
    }

    @Override
    public void close() throws IOException {
        writer.close();
    }

}
