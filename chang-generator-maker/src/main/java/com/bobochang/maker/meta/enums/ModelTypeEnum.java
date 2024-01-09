package com.bobochang.maker.meta.enums;

/**
 * @author bobochang
 * @Description 模型类型枚举类
 * @Date 2024/1/9 - 11:31
 */
public enum ModelTypeEnum {

    STRING("字符串", "String"),
    BOOLEAN("布尔", "boolean");

    private final String text;
    private final String value;

    ModelTypeEnum(String text, String value) {
        this.text = text;
        this.value = value;
    }

    public String getText() {
        return text;
    }

    public String getValue() {
        return value;
    }
}
