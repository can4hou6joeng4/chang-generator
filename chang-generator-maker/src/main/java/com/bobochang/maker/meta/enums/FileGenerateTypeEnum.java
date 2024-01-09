package com.bobochang.maker.meta.enums;

/**
 * @author bobochang
 * @Description 生成文件类型枚举类
 * @Date 2024/1/9 - 11:28
 */
public enum FileGenerateTypeEnum {

    DYNAMIC("动态", "dynamic"),
    STATIC("静态", "static");

    private final String text;

    private final String value;

    FileGenerateTypeEnum(String text, String value) {
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
