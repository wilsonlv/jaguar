package org.jaguar.modules.document.interfaces;

import java.io.Serializable;

/**
 * @author lvws
 * @since 2020/6/25
 */
public interface DocumentPersistence extends Serializable {

    /**
     * 获取文件ID
     *
     * @return 文件ID
     */
    Long getId();

    /**
     * 获取文件路径
     *
     * @return 文件路径
     */
    String getFilePath();

}
