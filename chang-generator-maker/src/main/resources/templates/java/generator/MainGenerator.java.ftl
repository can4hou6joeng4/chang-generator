package ${basePackage}.generator;

import ${basePackage}.model.DataModel;
import freemarker.template.TemplateException;

import java.io.File;
import java.io.IOException;

public class MainGenerator {

    public static void doGenerate(Object model) throws IOException, TemplateException {
        String inputRootPath = "${fileConfig.inputRootPath}";
        String outputRootPath = "${fileConfig.outputRootPath}";
        String inputPath;
        String outputPath;
    <#list fileConfig.files as fileInfo>
        inputPath = new File(inputRootPath,"${fileInfo.inputPath}").getAbsolutePath();
        outputPath = new File(outputRootPath,"${fileInfo.outputPath}").getAbsolutePath();
        <#if fileInfo.generateType == "static">
        StaticGenerator.copyFilesByHutool(inputPath,outputPath);
        <#else>
        DynamicGenerator.doGenerate(inputPath,outputPath,model);
        </#if>
    </#list>
    }
}