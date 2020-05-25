package org.jaguar.modules.system.mgm.enums;

import java.util.Arrays;
import java.util.List;

/**
 * @author lvws
 * @since 2019/11/15
 */
public enum RoleMenuPermission {

    /**
     * 读取
     */
    READ("读取"),
    /**
     * 读取_查看
     */
    READ_VIEW("读取_查看"),
    /**
     * 读取_查看_新增编辑
     */
    READ_VIEW_UPDATE("读取_查看_新增编辑"),
    /**
     * 读取_查看_新增编辑_删除
     */
    READ_VIEW_UPDATE_DELETE("读取_查看_新增编辑_删除");

    private final String name;

    RoleMenuPermission(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }

    public List<String> permissions() {
        return Arrays.asList(this.toString().split("_"));
    }
}
