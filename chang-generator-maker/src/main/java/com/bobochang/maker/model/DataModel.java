package com.bobochang.maker.model;

import lombok.Data;

/**
 * 动态模版配置
 */
@Data
public class DataModel {

    /**
     * 是否生成 .gitignore 文件
     */
    public boolean needGit = false;

    /**
     * 是否生成循环
     */
    public boolean loop = false;
    /**
     * 作者注释
     */
    public String author = "bobochang";

    /**
     * 输出信息
     */
    public String outputText = "example output txt = ";
}