package org.jaguar.modules.codegen.service;


import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.ZipUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;
import org.jaguar.commons.web.exception.impl.CheckedException;
import org.jaguar.modules.codegen.constant.EnvVariable;
import org.jaguar.modules.codegen.controller.dto.CodegenDTO;
import org.jaguar.modules.codegen.controller.dto.PreviewDTO;
import org.jaguar.modules.codegen.controller.vo.ColumnVO;
import org.jaguar.modules.codegen.controller.vo.TableVO;
import org.jaguar.modules.codegen.enums.CodeTemplateType;
import org.jaguar.modules.codegen.mapper.CodeGeneratorMapper;
import org.jaguar.modules.codegen.properties.CodegenProperties;
import org.jaguar.modules.codegen.velocity.CodeTemplateResourceLoader;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

/**
 * @author lvws
 * @since 2019/4/30.
 */
@Service
@RequiredArgsConstructor
public class CodeGeneratorService {

    public final static ThreadLocal<String> TEMPLATE_PREVIEW_FILE = new ThreadLocal<>();

    private final static String TEMP_DIR = "temp";

    private final static Set<String> IGNORE_COLUMNS = new HashSet<>();

    static {
        IGNORE_COLUMNS.add("id_");
        IGNORE_COLUMNS.add("create_by");
        IGNORE_COLUMNS.add("create_time");
        IGNORE_COLUMNS.add("update_by");
        IGNORE_COLUMNS.add("update_time");
        IGNORE_COLUMNS.add("remark_");
        IGNORE_COLUMNS.add("deleted_");
    }

    private final CodeGeneratorMapper mapper;

    private final DataSourceService dataSourceService;

    private final CodegenProperties codegenProperties;


    public TableVO getTableInfo(String schema, String tableName) {
        return this.mapper.getTableInfo(schema, tableName);
    }

    private List<ColumnVO> listColumnInfo(String schema, String tableName) {
        return this.mapper.listColumnInfo(schema, tableName);
    }

    @DS("#dataSourceName")
    public Page<TableVO> showTables(Page<TableVO> page, String dataSourceName, String fuzzyTableName) {
        String schema = dataSourceService.getSchema(dataSourceName);
        return this.mapper.showTables(page, schema, fuzzyTableName);
    }

    private VelocityEngine getVelocityEngine() {
        Properties properties = new Properties();
        properties.setProperty("file.resource.loader.class", CodeTemplateResourceLoader.class.getName());
        properties.setProperty(Velocity.INPUT_ENCODING, StandardCharsets.UTF_8.name());
        properties.setProperty(Velocity.OUTPUT_ENCODING, StandardCharsets.UTF_8.name());
        VelocityEngine engine = new VelocityEngine(properties);
        engine.init();
        return engine;
    }

    private VelocityContext getVelocityContext(CodegenDTO codegen) {
        String schema = dataSourceService.getSchema(codegen.getDataSourceName());

        TableVO tableInfo = this.getTableInfo(schema, codegen.getTableName());

        List<ColumnVO> columnInfos = this.listColumnInfo(schema, codegen.getTableName());
        int dateTimeScore = this.strengthColumnInfo(columnInfos);

        String parentPackage = StringUtils.isNotBlank(codegen.getModuleName()) ?
                codegen.getParentPackage() + '.' + codegen.getModuleName() : codegen.getParentPackage();

        String entityCamelCase;
        if (StringUtils.isNotBlank(codegen.getTablePrefix())) {
            String[] split = tableInfo.getTableName().split(codegen.getTablePrefix());
            entityCamelCase = split.length == 1 ? split[0] : split[1];
        } else {
            entityCamelCase = tableInfo.getTableName();
        }
        entityCamelCase = StrUtil.toCamelCase(entityCamelCase);

        Map<String, Object> envVariables = new HashMap<>();
        envVariables.put(EnvVariable.AUTHOR, codegen.getAuthor());
        envVariables.put(EnvVariable.DATE, LocalDate.now());
        envVariables.put(EnvVariable.TABLE, tableInfo);
        envVariables.put(EnvVariable.COLUMNS, columnInfos);
        envVariables.put(EnvVariable.PACKAGE, parentPackage);
        envVariables.put(EnvVariable.MODULE, codegen.getModuleName());
        envVariables.put(EnvVariable.ENTITY, StrUtil.upperFirst(entityCamelCase));
        envVariables.put(EnvVariable.ENTITY_NAME, entityCamelCase);
        envVariables.put(EnvVariable.DATE_TIME_SCORE, dateTimeScore);

        System.out.println(JSONObject.toJSONString(envVariables));

        return new VelocityContext(envVariables);
    }

    @DS("#codegen.dataSourceName")
    public void generate(CodegenDTO codegen, HttpServletResponse response) throws IOException {
        VelocityContext variables = getVelocityContext(codegen);

        VelocityEngine engine = getVelocityEngine();

        String tempDir = TEMP_DIR + File.separator + System.currentTimeMillis() + File.separator + codegen.getTableName();

        String parentPackage = StringUtils.join(((String) variables.get(EnvVariable.PACKAGE)).split("\\."), File.separator);

        for (CodeTemplateType value : CodeTemplateType.values()) {
            String filePath = tempDir + File.separator + parentPackage + File.separator + value.getPath() + File.separator + variables.get("entityName") + value.getFileNameSuffix();
            File file = new File(filePath);
            if (!file.getParentFile().exists() && !file.getParentFile().mkdirs()) {
                throw new CheckedException("创建临时文件夹失败");
            }
            try (OutputStreamWriter outputStreamWriter = new OutputStreamWriter(new FileOutputStream(file))) {
                Template template = engine.getTemplate(value.name());
                template.merge(variables, outputStreamWriter);
            }
        }

        File file = new File(tempDir);
        File zip = ZipUtil.zip(file);

        response.setHeader("Content-Disposition", "attachment;filename=" + zip.getName());

        try (FileInputStream inputStream = new FileInputStream(zip);
             ServletOutputStream outputStream = response.getOutputStream()) {
            IOUtils.copy(inputStream, outputStream);
        }

        FileUtils.deleteDirectory(file);
        FileUtils.deleteQuietly(zip);
    }

    public String preview(PreviewDTO preview) throws IOException {
        VelocityContext variables = getVelocityContext(preview);

        VelocityEngine engine = getVelocityEngine();

        TEMPLATE_PREVIEW_FILE.set(preview.getCodeTemplateFile());

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try (OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream)) {
            Template template = engine.getTemplate(preview.getCodeTemplateType().name());
            template.merge(variables, outputStreamWriter);
        }

        TEMPLATE_PREVIEW_FILE.remove();

        return outputStream.toString();
    }

    private int strengthColumnInfo(List<ColumnVO> columnInfos) {
        boolean date = false;
        boolean datetime = false;

        Iterator<ColumnVO> iterator = columnInfos.iterator();
        while (iterator.hasNext()) {
            ColumnVO columnInfo = iterator.next();
            if (IGNORE_COLUMNS.contains(columnInfo.getColumnName())) {
                iterator.remove();
                continue;
            }

            String camelCase = StrUtil.toCamelCase(columnInfo.getColumnName());
            columnInfo.setFieldName(camelCase);
            String fieldType = codegenProperties.getColumnTypeMapping().get(columnInfo.getDataType().toLowerCase());
            columnInfo.setFiledType(fieldType);

            if (columnInfo.getFiledType().equals(LocalDate.class.getSimpleName())) {
                date = true;
            } else if (columnInfo.getFiledType().equals(LocalDateTime.class.getSimpleName())) {
                datetime = true;
            }
        }

        int score = 0;
        if (date) {
            score += 1;
        }
        if (datetime) {
            score += 2;
        }
        return score;
    }

}
