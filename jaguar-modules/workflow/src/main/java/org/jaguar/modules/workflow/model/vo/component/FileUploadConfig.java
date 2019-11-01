package org.jaguar.modules.workflow.model.vo.component;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created by lvws on 2019/7/31.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class FileUploadConfig extends ComponentConfig {

    /**
     * 最大文件数量
     */
    private Integer limit;
    /**
     * 支持多文件上传
     */
    private Boolean multiple;
    /**
     * 是否为图片类型
     */
    private Boolean isPicture;
    /**
     * 选择的文件类型
     */
    private String accept;

}
