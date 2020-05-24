package org.jaguar.modules.system.mgm.enums;

/**
 * @author lvws
 * @since 2019/11/15
 */
public enum RoleMenuPermission {

    /**
     * 查看
     */
    VIEW("查看"),
    /**
     * 读取
     */
    READ("读取"),
    /**
     * 新增编辑
     */
    UPDATE("新增编辑"),
    /**
     * 删除
     */
    DEL("删除");

    private final String name;

    RoleMenuPermission(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
