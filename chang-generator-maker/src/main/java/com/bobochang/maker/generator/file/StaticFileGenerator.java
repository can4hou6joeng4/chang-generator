package com.bobochang.maker.generator.file;

import cn.hutool.core.io.FileUtil;

/**
 * @author bobochang
 * @Description 静态文件生成
 * @Date 2023/12/8 - 16:26
 */
public class StaticFileGenerator {

    /**
     * 拷贝文件(使用 Hutool 实现，将输入目录完整拷贝到输出目录下)
     *
     * @param inputPath  指定拷贝原文件的目录
     * @param outputPath 指定拷贝后文件的生成目录
     */
    public static void copyFilesByHutool(String inputPath, String outputPath) {
        FileUtil.copy(inputPath, outputPath, false);
    }
}
