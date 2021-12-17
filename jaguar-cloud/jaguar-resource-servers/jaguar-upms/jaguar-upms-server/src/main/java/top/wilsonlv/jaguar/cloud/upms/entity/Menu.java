package top.wilsonlv.jaguar.cloud.upms.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import top.wilsonlv.jaguar.datamodifylog.entity.DataModifyLoggable;

/**
 * <p>
 * 菜单表
 * </p>
 *
 * @author lvws
 * @since 2021-08-16
 */
@Data
@TableName("jaguar_cloud_upms_menu")
@EqualsAndHashCode(callSuper = true)
public class Menu extends DataModifyLoggable {

    /**
     * 资源服务ID
     */
    @TableField(value = "resource_server_id")
    private Long resourceServerId;
    /**
     * 父ID
     */
    @TableField(value = "parent_id")
    private Long parentId;
    /**
     * 名称
     */
    @TableField("menu_name")
    private String menuName;
    /**
     * 权限
     */
    @TableField("menu_permission")
    private String menuPermission;
    /**
     * 排序
     */
    @TableField("menu_order")
    private Integer menuOrder;
    /**
     * 是否为按钮
     */
    @TableField("menu_button")
    private Boolean menuButton;
    /**
     * 图标
     */
    @TableField("menu_icon")
    private String menuIcon;
    /**
     * 展示页面
     */
    @TableField("menu_page")
    private String menuPage;

}