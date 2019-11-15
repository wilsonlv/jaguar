package org.jaguar.modules.system.enums;

/**
 * @author lvws
 * @since 2019/11/15
 */
public enum RoleMenuPermission {

    VIEW("查看"),
    READ("读取"),
    UPDATE("新增编辑"),
    DEL("删除");

    private String name;

    RoleMenuPermission(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
