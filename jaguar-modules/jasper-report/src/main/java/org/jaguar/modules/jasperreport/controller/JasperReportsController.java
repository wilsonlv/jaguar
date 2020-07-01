package org.jaguar.modules.jasperreport.controller;


import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.HtmlExporter;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.*;
import org.jaguar.commons.enums.ExportType;
import org.jaguar.core.Charsets;
import org.jaguar.core.base.BaseController;
import org.jaguar.modules.jasperreport.service.JasperReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.Collections;

/**
 * @author lvws
 * @since 2019/10/16.
 */
@Validated
@RestController
@RequestMapping("/jasper_reports")
@Api(value = "JasperReport报表管理")
public class JasperReportsController extends BaseController {

    private static final SimpleHtmlReportConfiguration SIMPLE_HTML_REPORT_CONFIGURATION = new SimpleHtmlReportConfiguration();

    private static final SimpleXlsxReportConfiguration SIMPLE_XLSX_REPORT_CONFIGURATION = new SimpleXlsxReportConfiguration();

    static {
        SIMPLE_HTML_REPORT_CONFIGURATION.setEmbedImage(true);
        SIMPLE_XLSX_REPORT_CONFIGURATION.setWhitePageBackground(false);
        SIMPLE_XLSX_REPORT_CONFIGURATION.setDetectCellType(true);
    }

    @Autowired
    private JasperReportService jasperReportService;

    @ApiOperation(value = "预览")
    @GetMapping("/preview/{templateId}")
    public void preview(HttpServletResponse response,
                        @ApiParam(value = "模版ID", required = true) @PathVariable Long templateId,
                        @ApiParam(value = "导出类型") @RequestParam(required = false, defaultValue = "PDF") ExportType exportType,
                        @ApiParam(value = "参数", required = true) @RequestParam @NotBlank String jsonParamStr) throws JRException, IOException {

        JSONObject jsonParams = JSONObject.parseObject(jsonParamStr);
        JasperPrint jasperPrint = jasperReportService.print(templateId, jsonParams);

        ServletOutputStream outputStream = response.getOutputStream();
        this.jasperExport(jasperPrint, outputStream, exportType);
        outputStream.close();
    }

    @ApiOperation(value = "导出")
    @GetMapping("/export/{templateId}")
    public void export(HttpServletResponse response,
                       @ApiParam(value = "模版ID", required = true) @PathVariable Long templateId,
                       @ApiParam(value = "文件名称", required = true) @RequestParam @NotBlank String fileName,
                       @ApiParam(value = "导出类型", required = true) @RequestParam @NotNull ExportType exportType,
                       @ApiParam(value = "参数", required = true) @RequestParam @NotBlank String jsonParamStr) throws JRException, IOException {

        JSONObject jsonParams = JSONObject.parseObject(jsonParamStr);
        JasperPrint jasperPrint = jasperReportService.print(templateId, jsonParams);

        response.setContentType("multipart/form-data;charset=" + Charsets.UTF_8_NAME);
        response.setHeader("Content-Disposition", "attachment;fileName=" + URLEncoder.encode(fileName, Charsets.UTF_8_NAME));

        ServletOutputStream outputStream = response.getOutputStream();
        this.jasperExport(jasperPrint, outputStream, exportType);
        outputStream.close();
    }

    private void jasperExport(JasperPrint jasperPrint, OutputStream outputStream, ExportType exportType) throws JRException {
        switch (exportType) {
            case PDF: {
                JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);
                break;
            }
            case WORD: {
                JRDocxExporter jrDocxExporter = new JRDocxExporter();
                jrDocxExporter.setExporterInput(SimpleExporterInput.getInstance(Collections.singletonList(jasperPrint)));
                jrDocxExporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outputStream));
                jrDocxExporter.exportReport();
                break;
            }
            case HTML: {
                HtmlExporter htmlExporter = new HtmlExporter();
                htmlExporter.setConfiguration(SIMPLE_HTML_REPORT_CONFIGURATION);
                htmlExporter.setExporterInput(new SimpleExporterInput(jasperPrint));
                htmlExporter.setExporterOutput(new SimpleHtmlExporterOutput(outputStream));
                htmlExporter.exportReport();
                break;
            }
            case EXCEL: {
                JRXlsxExporter jrXlsxExporter = new JRXlsxExporter();
                jrXlsxExporter.setConfiguration(SIMPLE_XLSX_REPORT_CONFIGURATION);
                jrXlsxExporter.setExporterInput(new SimpleExporterInput(jasperPrint));
                jrXlsxExporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outputStream));
                jrXlsxExporter.exportReport();
                break;
            }
            default:
        }
    }

}
