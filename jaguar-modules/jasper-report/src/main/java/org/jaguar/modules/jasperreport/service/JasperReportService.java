package org.jaguar.modules.jasperreport.service;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import org.jaguar.core.exception.Assert;
import org.jaguar.core.exception.CheckedException;
import org.jaguar.modules.document.interfaces.DocumentPersistence;
import org.jaguar.modules.jasperreport.model.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

/**
 * Created by lvws on 2019/10/16.
 */
@Service
public class JasperReportService {

    @Autowired
    private DataSource dataSource;
    @Autowired
    private TemplateService templateService;

    @Transactional
    public JasperPrint print(Long templateId, Map<String, Object> params) throws JRException {
        Template template = templateService.getById(templateId);
        Assert.validateId(template, "模版", templateId);

        if (template.getFileId() == null) {
            throw new CheckedException("请先上传jasper模版");
        }

        DocumentPersistence documentPersistence = templateService.getDocumentPersistence(template);
        JasperReport jasperReport = (JasperReport) JRLoader.loadObject(new File(documentPersistence.getFilePath()));

        JasperPrint jasperPrint;
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            jasperPrint = JasperFillManager.fillReport(jasperReport, params, connection);
        } catch (SQLException e) {
            throw new CheckedException(e);
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return jasperPrint;
    }
}
