package com.bobochang.maker.generator.main;


import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.resource.ClassPathResource;
import cn.hutool.core.util.StrUtil;
import com.bobochang.maker.generator.JarGenerator;
import com.bobochang.maker.generator.ScriptGenerator;
import com.bobochang.maker.generator.file.DynamicFileGenerator;
import com.bobochang.maker.meta.Meta;
import com.bobochang.maker.meta.MetaManager;
import freemarker.template.TemplateException;

import java.io.File;
import java.io.IOException;

/**
 * @author bobochang
 * @Description 生成器抽象类
 * @Date 2024/1/9 - 14:51
 */
public abstract class GenerateTemplate {
    public void doGenerate() throws TemplateException, IOException, InterruptedException {
        Meta meta = MetaManager.getMetaObject();
        System.out.println(meta);
        // 0.文件输出根路径
        String projectPath = System.getProperty("user.dir");
        // ./chang-generator-maker/generated/acm-template-pro-generator
        String outputPath = projectPath + File.separator + "generated" + File.separator + meta.getName();
        if (!FileUtil.exist(outputPath)) {
            FileUtil.mkdir(outputPath);
        }
        // 1.将原始模板文件复制到生成代码的目标路径包中
        String sourceCopyDestPath = copySource(meta, outputPath);
        // 2.生成代码
        generateCode(meta, outputPath);
        // 3.构建 Jar 包
        String jarPath = buildJar(outputPath, meta);
        // 4.封装脚本
        String shellOutputPath = buildScript(outputPath, jarPath);
        // 5.生成精简版程序（产物包）
        buildDist(outputPath, jarPath, shellOutputPath, sourceCopyDestPath);
    }

    /**
     * 复制原始文件方法
     *
     * @param meta
     * @param outputPath
     * @return
     */
    protected String copySource(Meta meta, String outputPath) {
        String sourceRootPath = meta.getFileConfig().getSourceRootPath();
        String sourceCopyDestPath = outputPath + File.separator + ".source";
        FileUtil.copy(sourceRootPath, sourceCopyDestPath, false);
        return sourceCopyDestPath;
    }

    /**
     * 生成代码方法
     *
     * @param meta
     * @param outputPath
     * @throws IOException
     * @throws TemplateException
     */
    protected void generateCode(Meta meta, String outputPath) throws IOException, TemplateException {
        // 获取模板文件
        String inputFilePath;
        String outputFilePath;
        // 读取 resources 目录
        ClassPathResource classPathResource = new ClassPathResource(" ");
        String inputResourcePath = classPathResource.getAbsolutePath();
        // Java 包基础路径
        String outputBasePackage = meta.getBasePackage(); // com.bobochang
        String outputBasePackagePath = StrUtil.join("/", StrUtil.split(outputBasePackage, ".")); // com/bobochang
        String outputBaseJavaPackagePath = outputPath + File.separator + "src/main/java" + File.separator + outputBasePackagePath;
        // 获取模板路径
        inputFilePath = inputResourcePath + File.separator + "templates/java/model/DataModel.java.ftl";
        outputFilePath = outputBaseJavaPackagePath + File.separator + "model/DataModel.java";
        DynamicFileGenerator.doGenerate(inputFilePath, outputFilePath, meta);
        // cli.command.ConfigCommand
        inputFilePath = inputResourcePath + File.separator + "templates/java/cli/command/ConfigCommand.java.ftl";
        outputFilePath = outputBaseJavaPackagePath + File.separator + "cli/command/ConfigCommand.java";
        DynamicFileGenerator.doGenerate(inputFilePath, outputFilePath, meta);
        // cli.command.GenerateCommand
        inputFilePath = inputResourcePath + File.separator + "templates/java/cli/command/GenerateCommand.java.ftl";
        outputFilePath = outputBaseJavaPackagePath + File.separator + "cli/command/GenerateCommand.java";
        DynamicFileGenerator.doGenerate(inputFilePath, outputFilePath, meta);
        // cli.command.ListCommand
        inputFilePath = inputResourcePath + File.separator + "templates/java/cli/command/ListCommand.java.ftl";
        outputFilePath = outputBaseJavaPackagePath + File.separator + "cli/command/ListCommand.java";
        DynamicFileGenerator.doGenerate(inputFilePath, outputFilePath, meta);
        // cli.CommandExecutor
        inputFilePath = inputResourcePath + File.separator + "templates/java/cli/CommandExecutor.java.ftl";
        outputFilePath = outputBaseJavaPackagePath + File.separator + "cli/CommandExecutor.java";
        DynamicFileGenerator.doGenerate(inputFilePath, outputFilePath, meta);
        // Main
        inputFilePath = inputResourcePath + File.separator + "templates/java/Main.java.ftl";
        outputFilePath = outputBaseJavaPackagePath + File.separator + "Main.java";
        DynamicFileGenerator.doGenerate(inputFilePath, outputFilePath, meta);
        // generator.DynamicGenerator
        inputFilePath = inputResourcePath + File.separator + "templates/java/generator/DynamicGenerator.java.ftl";
        outputFilePath = outputBaseJavaPackagePath + File.separator + "generator/DynamicGenerator.java";
        DynamicFileGenerator.doGenerate(inputFilePath, outputFilePath, meta);
        // generator.MainGenerator
        inputFilePath = inputResourcePath + File.separator + "templates/java/generator/MainGenerator.java.ftl";
        outputFilePath = outputBaseJavaPackagePath + File.separator + "generator/MainGenerator.java";
        DynamicFileGenerator.doGenerate(inputFilePath, outputFilePath, meta);
        // generator.StaticGenerator
        inputFilePath = inputResourcePath + File.separator + "templates/java/generator/StaticGenerator.java.ftl";
        outputFilePath = outputBaseJavaPackagePath + File.separator + "generator/StaticGenerator.java";
        DynamicFileGenerator.doGenerate(inputFilePath, outputFilePath, meta);
        // pom.xml.ftl
        inputFilePath = inputResourcePath + File.separator + "templates/pom.xml.ftl";
        outputFilePath = outputPath + File.separator + "pom.xml";
        DynamicFileGenerator.doGenerate(inputFilePath, outputFilePath, meta);
    }

    /**
     * 构建 Jar 包方法
     *
     * @param outputPath
     * @param meta
     * @return
     * @throws IOException
     * @throws InterruptedException
     */
    protected String buildJar(String outputPath, Meta meta) throws IOException, InterruptedException {
        // 构建 jar 包
        JarGenerator.doGenerate(outputPath);
        String jarName = String.format("%s-%s-jar-with-dependencies.jar", meta.getName(), meta.getVersion());
        String jarPath = "target/" + jarName;
        return jarPath;
    }

    /**
     * 构建脚本方法
     *
     * @param outputPath
     * @param jarPath
     * @return
     * @throws IOException
     */
    protected String buildScript(String outputPath, String jarPath) throws IOException {
        // 构建脚本
        String shellOutputPath = outputPath + File.separator + "generator";
        ScriptGenerator.doGenerate(shellOutputPath, jarPath);
        return shellOutputPath;
    }

    /**
     * 构建精简版程序方法
     *
     * @param outputPath
     * @param jarPath
     * @param shellOutputPath
     * @param sourceCopyDestPath
     */
    protected void buildDist(String outputPath, String jarPath, String shellOutputPath, String sourceCopyDestPath) {
        // 生成精简版 -dist
        String distOutputPath = outputPath + "-dist";
        // 拷贝 jar 包
        String targetAbsolutePath = distOutputPath + File.separator + "target";
        FileUtil.mkdir(targetAbsolutePath);
        String jarAbsolutePath = outputPath + File.separator + jarPath;
        FileUtil.copy(jarAbsolutePath, targetAbsolutePath, true);
        // 拷贝脚本文件
        FileUtil.copy(shellOutputPath, distOutputPath, true);
        FileUtil.copy(shellOutputPath + ".bat", distOutputPath, true);
        // 拷贝源模板文件
        FileUtil.copy(sourceCopyDestPath, distOutputPath, true);
    }
}
