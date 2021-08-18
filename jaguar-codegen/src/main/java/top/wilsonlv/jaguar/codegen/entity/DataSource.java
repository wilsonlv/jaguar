package top.wilsonlv.jaguar.codegen.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import top.wilsonlv.jaguar.commons.basecrud.BaseModel;

import javax.validation.constraints.NotBlank;

/**
 * 数据源
 *
 * @author lvws
 * @since 2021/6/16
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("jaguar_codegen_datasource")
public class DataSource extends BaseModel {

    private static final long serialVersionUID = 1L;

    /**
     * 数据源名称
     */
    @NotBlank
    @TableField("name_")
    private String name;

    /**
     * host
     */
    @NotBlank
    @TableField("host_")
    private String host;

    /**
     * port
     */
    @NotBlank
    @TableField("port_")
    private String port;

    /**
     * schema
     */
    @NotBlank
    @TableField("schema_")
    private String schema;

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
