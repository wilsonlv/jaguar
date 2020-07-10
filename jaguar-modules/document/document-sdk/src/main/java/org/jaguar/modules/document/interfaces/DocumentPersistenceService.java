package org.jaguar.modules.document.interfaces;

import org.springframework.web.multipart.MultipartFile;

/**
 * @author lvws
 * @since 2020/6/25
 */
public interface DocumentPersistenceService {

    /**
     * 持久化模板文件
     *
     * @param file 模板文件
     * @return 文件ID
     */
    DocumentPersistence persist(MultipartFile file);

    /**
     * 通过文件ID获取模板文件对象
     *
     * @param id 文件ID
     * @return 模板文件对象
     */
    DocumentPersistence getById(Long id);

}
