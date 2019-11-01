package org.jaguar.modules.workflow.enums;

/**
 * Created by lvws on 2019/7/29.
 */
public enum ReviewType {

    APPROVAL("批准"),
    REJECT("驳回");

    private String name;

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
