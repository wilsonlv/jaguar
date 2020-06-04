package org.jaguar.modules.document.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jaguar.core.base.BaseModel;

/**
 * 文档上传表
 *
 * @author lvws
 * @date 2019-11-01 10:23:09
 */
@Data
@TableName("jaguar_modules_document")
@EqualsAndHashCode(callSuper = true)
public class Document extends BaseModel {

    /**
     * 原始文档名称
     */
    @TableField("original_name")
    private String originalName;
    /**
     * 文档拓展名
     */
    @TableField("extension_")
    private String extension;
    /**
     * 文档绝对路径
     */
    @TableField("absolute_path")
    private String absolutePath;
    /**
     * 文档占用空间
     */
    @TableField("total_space")
    private Long totalSpace;

}
