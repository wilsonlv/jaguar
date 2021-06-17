package org.jaguar.modules.codegen.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jaguar.commons.basecrud.BaseModel;

import javax.validation.constraints.NotBlank;

/**
 * @author lvws
 * @since 2021/6/16
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("jaguar_modules_codegen_datasource")
public class DataSource extends BaseModel {

    private static final long serialVersionUID = 1L;

    /**
     * 数据源名称
     */
    @NotBlank
    @TableField("name_")
    private String name;
    /**
     * jdbcUrl
     */
    @NotBlank
    @TableField("jdbc_url")
    private String jdbcUrl;
    /**
     * 用户名
     */
    @NotBlank
    @TableField("username_")
    private String username;
    /**
     * 密码
     */
    @NotBlank
    @TableField("password_")
    private String password;

}
