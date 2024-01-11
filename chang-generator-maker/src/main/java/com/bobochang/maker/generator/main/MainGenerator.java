package com.bobochang.maker.generator.main;

import freemarker.template.TemplateException;

import java.io.IOException;

/**
 * @author bobochang
 * @Description
 * @Date 2024/1/4 - 09:59
 */
public class MainGenerator extends GenerateTemplate {
    @Override
    protected void buildDist(String outputPath, String jarPath, String shellOutputPath, String sourceCopyDestPath) {
        System.out.println("不要给我输出dist");
    }
}
