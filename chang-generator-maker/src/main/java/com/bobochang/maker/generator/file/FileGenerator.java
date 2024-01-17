package com.bobochang.maker.generator.file;

import freemarker.template.TemplateException;

import java.io.File;
import java.io.IOException;

/**
 * @author bobochang
 * @Description
 * @Date 2023/12/11 - 15:09
 */
public class FileGenerator {

    public static void doGenerate(Object model) throws IOException, TemplateException {
        // 1 生成静态文件
        // 获取当前打开项目的根路径
        String projectPath = System.getProperty("user.dir");
        // 输入路径：ACM示例代码模板目录
        String staticInputPath = new File(projectPath, "chang-generator-demo-projects/acm-template").getAbsolutePath();
        // 输出路径：直接输出到项目的根目录
        String staticOutputPath = projectPath;
        StaticFileGenerator.copyFilesByHutool(staticInputPath, staticOutputPath);

        // 2 生成动态文件
        String dynamicProjectPath = System.getProperty("user.dir");
        String dynamicInputPath = dynamicProjectPath + File.separator + "chang-generator-maker" + File.separator + "/src/main/resources/templates/MainTemplate.java.ftl";
        String dynamicOutputPath = dynamicProjectPath + File.separator + "acm-template/src/com/bobochang/acm/MainTemplate.java";
        DynamicFileGenerator.doGenerate(dynamicInputPath, dynamicOutputPath, model);
    }
}
