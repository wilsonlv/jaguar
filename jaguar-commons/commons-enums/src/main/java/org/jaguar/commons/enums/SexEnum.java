package org.jaguar.commons.enums;

/**
 * @author lvws
 * @since 2020/6/3
 */
public enum SexEnum {

    /**
     * 男
     */
    MALE("男"),
    /**
     * 女
     */
    FEMALE("女");

    private final String label;

    SexEnum(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
