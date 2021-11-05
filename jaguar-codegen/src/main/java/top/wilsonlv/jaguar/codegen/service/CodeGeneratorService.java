package top.wilsonlv.jaguar.codegen.service;


import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.ZipUtil;
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
import top.wilsonlv.jaguar.codegen.mapper.CodeGeneratorMapper;
import top.wilsonlv.jaguar.codegen.entity.CodeTemplate;
import top.wilsonlv.jaguar.codegen.velocity.CodeTemplateResourceLoader;
import top.wilsonlv.jaguar.commons.aviator.ExpressionUtil;
import top.wilsonlv.jaguar.commons.web.exception.impl.CheckedException;
import top.wilsonlv.jaguar.codegen.constant.EnvVariable;
import top.wilsonlv.jaguar.codegen.controller.dto.CodegenDTO;
import top.wilsonlv.jaguar.codegen.controller.dto.PreviewDTO;
import top.wilsonlv.jaguar.codegen.controller.vo.ColumnVO;
import top.wilsonlv.jaguar.codegen.controller.vo.TableVO;
import top.wilsonlv.jaguar.codegen.properties.CodegenProperties;
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

    private final static String TEMP_DIR = "temp";

    private final static Set<String> IGNORE_COLUMNS = new HashSet<>();

    static {
        IGNORE_COLUMNS.add("id_");
        IGNORE_COLUMNS.add("create_user_id");
        IGNORE_COLUMNS.add("create_by");
        IGNORE_COLUMNS.add("create_time");
        IGNORE_COLUMNS.add("update_user_id");
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

    private Map<String, Object> getFileEnvVariables(String parentPackage, String moduleName) {
        parentPackage = StringUtils.isNotBlank(parentPackage) ? parentPackage + '.' + moduleName : parentPackage;

        Map<String, Object> fileEnvVariables = new HashMap<>();
        fileEnvVariables.put(EnvVariable.JAVA_PATH, EnvVariable.JAVA_PATH_VALUE);
        fileEnvVariables.put(EnvVariable.PACKAGE, parentPackage);
        fileEnvVariables.put(EnvVariable.PACKAGE_PATH, StringUtils.join(parentPackage.split("\\."), '/'));
        fileEnvVariables.put(EnvVariable.MODULE, moduleName);
        return fileEnvVariables;
    }

    private Map<String, Object> getEntityVariables(String tableName, String tablePrefix) {
        String entityCamelCase;
        if (StringUtils.isNotBlank(tablePrefix)) {
            String[] split = tableName.split(tablePrefix);
            entityCamelCase = split.length == 1 ? split[0] : split[1];
        } else {
            entityCamelCase = tableName;
        }
        if (entityCamelCase.startsWith("_")) {
            entityCamelCase = entityCamelCase.substring(1);
        }
        entityCamelCase = StrUtil.toCamelCase(entityCamelCase);

        Map<String, Object> entityVariables = new HashMap<>();
        entityVariables.put(EnvVariable.ENTITY, StrUtil.upperFirst(entityCamelCase));
        entityVariables.put(EnvVariable.ENTITY_NAME, entityCamelCase);
        return entityVariables;
    }

    private Map<String, Object> getEnvVariables(CodegenDTO codegen) {
        String schema = dataSourceService.getSchema(codegen.getDataSourceName());

        TableVO tableInfo = this.getTableInfo(schema, codegen.getTableName());

        List<ColumnVO> columnInfos = this.listColumnInfo(schema, codegen.getTableName());
        int dateTimeScore = this.strengthColumnInfo(columnInfos);

        Map<String, Object> envVariables = new HashMap<>();
        envVariables.put(EnvVariable.AUTHOR, codegen.getAuthor());
        envVariables.put(EnvVariable.DATE, LocalDate.now());
        envVariables.put(EnvVariable.TABLE, tableInfo);
        envVariables.put(EnvVariable.COLUMNS, columnInfos);
        envVariables.put(EnvVariable.DATE_TIME_SCORE, dateTimeScore);

        envVariables.putAll(getEntityVariables(codegen.getTableName(), codegen.getTablePrefix()));
        envVariables.putAll(getFileEnvVariables(codegen.getParentPackage(), codegen.getModuleName()));

        return envVariables;
    }

    @DS("#codegen.dataSourceName")
    public void generate(CodegenDTO codegen, HttpServletResponse response) throws IOException {
        Map<String, Object> envVariables = getEnvVariables(codegen);
        VelocityContext variables = new VelocityContext(envVariables);

        VelocityEngine engine = getVelocityEngine();

        String tempDir = TEMP_DIR + File.separator + System.currentTimeMillis() + File.separator + codegen.getTableName();

        for (CodeTemplate codeTemplate : CodeTemplateService.CODE_TEMPLATE_DATA_BASE.values()) {
            String filePath = (String) ExpressionUtil.executeText(codeTemplate.getFilePath(), envVariables);
            filePath = filePath.replaceAll("//", File.separator);

            String fileName = (String) ExpressionUtil.executeText(codeTemplate.getFileName(), envVariables);

            File file = new File(tempDir + File.separator + filePath + File.separator + fileName);
            if (!file.getParentFile().exists() && !file.getParentFile().mkdirs()) {
                throw new CheckedException("创建临时文件夹失败");
            }
            try (OutputStreamWriter outputStreamWriter = new OutputStreamWriter(new FileOutputStream(file))) {
                Template template = engine.getTemplate(codeTemplate.getCodeTemplateFile());
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

        FileUtils.deleteDirectory(file.getParentFile());
        FileUtils.deleteQuietly(zip);
    }

    public String preview(PreviewDTO preview) throws IOException {
        VelocityContext variables = new VelocityContext(getEnvVariables(preview));

        VelocityEngine engine = getVelocityEngine();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try (OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream)) {
            Template template = engine.getTemplate(preview.getCodeTemplateFile());
            template.merge(variables, outputStreamWriter);
        }

        return outputStream.toString();
    }

    public String previewFileName(String tableName, String tablePrefix, String fileName) {
        Map<String, Object> entityEnvVariables = this.getEntityVariables(tableName, tablePrefix);
        return (String) ExpressionUtil.executeText(fileName, entityEnvVariables);
    }

    public String previewFilePath(String tableName, String tablePrefix, String parentPackage, String moduleName, String filePath) {
        Map<String, Object> envVariables = this.getEntityVariables(tableName, tablePrefix);
        Map<String, Object> fileEnvVariables = this.getFileEnvVariables(parentPackage, moduleName);
        envVariables.putAll(fileEnvVariables);
        return (String) ExpressionUtil.executeText(filePath, envVariables);
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
