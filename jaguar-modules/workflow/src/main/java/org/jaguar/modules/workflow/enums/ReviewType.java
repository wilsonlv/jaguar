package org.jaguar.modules.workflow.enums;

/**
 * @author lvws
 * @since 2019/7/29.
 */
public enum ReviewType {

    /**
     * 批准
     */
    APPROVAL("批准"),
    /**
     * 驳回
     */
    REJECT("驳回");

    private final String name;

    ReviewType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return this.name;
    }

}
