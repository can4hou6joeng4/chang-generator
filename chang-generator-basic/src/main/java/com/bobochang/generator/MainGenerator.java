package com.bobochang.generator;

import com.bobochang.model.MainTemplateConfig;
import freemarker.template.TemplateException;

import java.io.File;
import java.io.IOException;

/**
 * @author bobochang
 * @Description
 * @Date 2023/12/11 - 15:09
 */
public class MainGenerator {

    public static void main(String[] args) throws TemplateException, IOException {
        MainTemplateConfig mainTemplateConfig = new MainTemplateConfig();
        mainTemplateConfig.setAuthor("bobochang");
        mainTemplateConfig.setOutputText("this is first outputText");
        mainTemplateConfig.setLoop(true);
        doGenerate(mainTemplateConfig);
    }

    private static void doGenerate(Object model) throws IOException, TemplateException {
        // 1 生成静态文件
        // 获取当前打开项目的根路径
        String projectPath = System.getProperty("user.dir");
        // 输入路径：ACM示例代码模板目录
        String staticInputPath = new File(projectPath, "chang-generator-demo-projects/acm-template").getAbsolutePath();
        // 输出路径：直接输出到项目的根目录
        String staticOutputPath = projectPath;
        StaticGenerator.copyFilesByHutool(staticInputPath, staticOutputPath);

        // 2 生成动态文件
        String dynamicProjectPath = System.getProperty("user.dir");
        String dynamicInputPath = dynamicProjectPath + File.separator + "chang-generator-basic" + File.separator + "/src/main/resources/templates/MainTemplate.java.ftl";
        String dynamicOutputPath = dynamicProjectPath + File.separator + "acm-template/src/com/bobochang/acm/MainTemplate.java";
        DynamicGenerator.doGenerate(dynamicInputPath, dynamicOutputPath, model);
    }
}
