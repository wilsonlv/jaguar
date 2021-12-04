package top.wilsonlv.jaguar.cloud.upms.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import top.wilsonlv.jaguar.basecrud.BaseModel;


/**
 * <p>
 * 资源服务
 * </p>
 *
 * @author lvws
 * @since 2021-12-02
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("jaguar_cloud_upms_resource_server")
public class ResourceServer extends BaseModel {

    /**
     * 服务ID
     */
    @TableField("server_id")
    private String serverId;

    /**
     * 服务名称
     */
    @TableField("server_name")
    private String serverName;

    /**
     * 服务密钥
     */
    @TableField("server_secret")
    private String serverSecret;

    /**
     * 是否展示菜单
     */
    @TableField("server_menu")
    private Boolean serverMenu;

    /**
     * 服务网址
     */
    @TableField("server_url")
    private String serverUrl;

}