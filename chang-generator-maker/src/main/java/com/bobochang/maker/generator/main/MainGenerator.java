package com.bobochang.maker.generator.main;

import freemarker.template.TemplateException;

import java.io.IOException;

/**
 * @author bobochang
 * @Description
 * @Date 2024/1/4 - 09:59
 */
public class MainGenerator extends GenerateTemplate {
    public static void main(String[] args) throws TemplateException, IOException, InterruptedException {
        MainGenerator mainGenerator = new MainGenerator();
        mainGenerator.doGenerate();
    }
}
