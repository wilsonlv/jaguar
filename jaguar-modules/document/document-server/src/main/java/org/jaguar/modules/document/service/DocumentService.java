package org.jaguar.modules.document.service;

import org.jaguar.modules.document.interfaces.DocumentPersistence;
import org.jaguar.modules.document.interfaces.DocumentPersistenceService;
import org.jaguar.modules.document.mapper.DocumentMapper;
import org.apache.commons.lang3.StringUtils;
import org.jaguar.commons.mybatisplus.extension.JaguarLambdaQueryWrapper;
import org.jaguar.commons.utils.DateUtil;
import org.jaguar.core.base.BaseService;
import org.jaguar.core.exception.CheckedException;
import org.jaguar.modules.document.config.DocumentProperties;
import org.jaguar.modules.document.model.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * 文档上传表
 *
 * @author lvws
 * @date 2019-11-01 10:23:09
 */
@Service
public class DocumentService extends BaseService<Document, DocumentMapper> implements DocumentPersistenceService {

    @Autowired
    private DocumentProperties documentProperties;

    public Document getByAbsolutePath(String absolutePath) {
        return this.unique(JaguarLambdaQueryWrapper.<Document>newInstance()
                .eq(Document::getAbsolutePath, absolutePath));
    }

    /**
     * 创建并返回文件路径
     */
    public String createFilePath(String fileName) {
        String parentFileDir = documentProperties.getFilePath() + File.separator + LocalDate.now() + File.separator;
        File parent = new File(parentFileDir);
        if (parent.exists() || parent.mkdirs()) {
            return parentFileDir + DateUtil.format(new Date(), DateUtil.DateTimePattern.HHMMSS) + fileName;
        } else {
            throw new CheckedException("目录创建失败！");
        }
    }

    /**
     * 创建并返回文件路径
     */
    public String createTempFilePath(String fileName) {
        String parentFileDir = documentProperties.getTempDir() + File.separator + LocalDate.now() + File.separator;
        File parent = new File(parentFileDir);
        if (parent.exists() || parent.mkdirs()) {
            return parentFileDir + DateUtil.format(new Date(), DateUtil.DateTimePattern.HHMMSS) + fileName;
        } else {
            throw new CheckedException("目录创建失败！");
        }
    }

    public Document upload(MultipartFile file) {
        return this.upload(Collections.singletonList(file)).get(0);
    }

    public List<Document> upload(List<MultipartFile> files) {
        List<Document> documentList = new ArrayList<>();
        for (MultipartFile file : files) {
            if (file.isEmpty()) {
                throw new CheckedException("文件为非空！");
            }

            String originalFilename = file.getOriginalFilename();
            if (StringUtils.isBlank(originalFilename)) {
                continue;
            }

            String filePath = this.createFilePath(originalFilename);
            try {
                file.transferTo(new File(filePath));
            } catch (IOException e) {
                throw new CheckedException(e);
            }

            Document document = new Document();
            document.setOriginalName(originalFilename);
            document.setExtension(originalFilename.split("\\.")[1]);
            document.setAbsolutePath(filePath);
            document.setTotalSpace(file.getSize());
            document = this.insert(document);

            documentList.add(document);
        }
        return documentList;
    }

    @Override
    public DocumentPersistence persist(MultipartFile file) {
        return this.upload(file);
    }
}
