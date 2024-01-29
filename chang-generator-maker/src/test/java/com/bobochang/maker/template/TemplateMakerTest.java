package com.bobochang.maker.template;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.json.JSONUtil;
import com.bobochang.maker.meta.Meta;
import com.bobochang.maker.template.config.TemplateMakerConfig;
import com.bobochang.maker.template.config.TemplateMakerFileConfig;
import com.bobochang.maker.template.config.TemplateMakerModelConfig;
import org.junit.Test;

import java.io.File;
import java.util.Arrays;
import java.util.List;

/**
 * @author bobochang
 * @Description
 * @Date 2024/1/24 - 11:45
 */
public class TemplateMakerTest {

    @Test
    public void testMakerTemplateBug1() {
        // 1.项目基本信息
        Meta meta = new Meta();
        meta.setName("acm-template-generator");
        meta.setDescription("ACM示例模板生成器");
        // 2.指定原始项目路径
        String projectPath = System.getProperty("user.dir");
        String originalProjectPath = FileUtil.getAbsolutePath(new File(projectPath).getParentFile()) + File.separator + "chang-generator-demo-projects/springboot-init";
        // 文件参数配置
        String inputFilePath = "src/main/resources/application.yml";
        TemplateMakerFileConfig templateMakerFileConfig = new TemplateMakerFileConfig();
        TemplateMakerFileConfig.FileInfoConfig fileInfoConfig = new TemplateMakerFileConfig.FileInfoConfig();
        fileInfoConfig.setPath(inputFilePath);
        templateMakerFileConfig.setFiles(Arrays.asList(fileInfoConfig));
        // 模型参数配置
        TemplateMakerModelConfig templateMakerModelConfig = new TemplateMakerModelConfig();
        // - 模型组配置
        TemplateMakerModelConfig.ModelGroupConfig modelGroupConfig = new TemplateMakerModelConfig.ModelGroupConfig();
        modelGroupConfig.setGroupKey("mysql");
        modelGroupConfig.setGroupName("数据库配置");
        templateMakerModelConfig.setModelGroupConfig(modelGroupConfig);
        // - 模型配置
        TemplateMakerModelConfig.ModelInfoConfig modelInfoConfig1 = new TemplateMakerModelConfig.ModelInfoConfig();
        modelInfoConfig1.setFieldName("url");
        modelInfoConfig1.setType("String");
        modelInfoConfig1.setDefaultValue("jdbc:mysql://localhost:3306/my_db");
        modelInfoConfig1.setReplaceText("jdbc:mysql://localhost:3306/my_db");
        TemplateMakerModelConfig.ModelInfoConfig modelInfoConfig2 = new TemplateMakerModelConfig.ModelInfoConfig();
        modelInfoConfig2.setFieldName("username");
        modelInfoConfig2.setType("String");
        modelInfoConfig2.setDefaultValue("root");
        modelInfoConfig2.setReplaceText("root");

        List<TemplateMakerModelConfig.ModelInfoConfig> modelInfoConfigList = Arrays.asList(modelInfoConfig1, modelInfoConfig2);
        templateMakerModelConfig.setModels(modelInfoConfigList);
        long id = TemplateMaker.makeTemplate(meta, originalProjectPath, templateMakerFileConfig, templateMakerModelConfig, null, 1750003512571494400L);
        System.out.println(id);
    }

    @Test
    public void testMakerTemplateBug2() {
        // 1.项目基本信息
        Meta meta = new Meta();
        meta.setName("acm-template-generator");
        meta.setDescription("ACM示例模板生成器");
        // 2.指定原始项目路径
        String projectPath = System.getProperty("user.dir");
        String originalProjectPath = FileUtil.getAbsolutePath(new File(projectPath).getParentFile()) + File.separator + "chang-generator-demo-projects/springboot-init";
        // 文件参数配置
        String inputFilePath = "src/main/java/com/yupi/springbootinit/common";
        TemplateMakerFileConfig templateMakerFileConfig = new TemplateMakerFileConfig();
        TemplateMakerFileConfig.FileInfoConfig fileInfoConfig = new TemplateMakerFileConfig.FileInfoConfig();
        fileInfoConfig.setPath(inputFilePath);
        templateMakerFileConfig.setFiles(Arrays.asList(fileInfoConfig));
        // 模型参数配置
        TemplateMakerModelConfig templateMakerModelConfig = new TemplateMakerModelConfig();
        // - 模型组配置
        TemplateMakerModelConfig.ModelGroupConfig modelGroupConfig = new TemplateMakerModelConfig.ModelGroupConfig();
        modelGroupConfig.setGroupKey("mysql");
        modelGroupConfig.setGroupName("数据库配置");
        templateMakerModelConfig.setModelGroupConfig(modelGroupConfig);
        // - 模型配置
        TemplateMakerModelConfig.ModelInfoConfig modelInfoConfig1 = new TemplateMakerModelConfig.ModelInfoConfig();
        modelInfoConfig1.setFieldName("className");
        modelInfoConfig1.setType("String");
        modelInfoConfig1.setReplaceText("BaseResponse");

        List<TemplateMakerModelConfig.ModelInfoConfig> modelInfoConfigList = Arrays.asList(modelInfoConfig1);
        templateMakerModelConfig.setModels(modelInfoConfigList);
        long id = TemplateMaker.makeTemplate(meta, originalProjectPath, templateMakerFileConfig, templateMakerModelConfig, null, 1750062731807096832L);
        System.out.println(id);
    }

    @Test
    public void testMakerTemplateWithJSON() {
        String rootPath = "examples/springboot-init/";
        String configStr = ResourceUtil.readUtf8Str(rootPath + "templateMakerTest.json");
        TemplateMakerConfig templateMakerConfig = JSONUtil.toBean(configStr, TemplateMakerConfig.class);
        long id = TemplateMaker.makeTemplate(templateMakerConfig);
        System.out.println(id);
    }

    @Test
    public void makeSpringbootTemplate() {
        String rootPath = "examples/springboot-init/";
        String configStr = ResourceUtil.readUtf8Str(rootPath + "templateMaker.json");
        TemplateMakerConfig templateMakerConfig = JSONUtil.toBean(configStr, TemplateMakerConfig.class);
        long id = TemplateMaker.makeTemplate(templateMakerConfig);

        configStr = ResourceUtil.readUtf8Str(rootPath + "templateMakerTest.json");
        templateMakerConfig = JSONUtil.toBean(configStr, TemplateMakerConfig.class);
        TemplateMaker.makeTemplate(templateMakerConfig);

        configStr = ResourceUtil.readUtf8Str(rootPath + "templateMakerTest2.json");
        templateMakerConfig = JSONUtil.toBean(configStr, TemplateMakerConfig.class);
        TemplateMaker.makeTemplate(templateMakerConfig);

        configStr = ResourceUtil.readUtf8Str(rootPath + "templateMakerTest3.json");
        templateMakerConfig = JSONUtil.toBean(configStr, TemplateMakerConfig.class);
        TemplateMaker.makeTemplate(templateMakerConfig);

        configStr = ResourceUtil.readUtf8Str(rootPath + "templateMakerTest4.json");
        templateMakerConfig = JSONUtil.toBean(configStr, TemplateMakerConfig.class);
        TemplateMaker.makeTemplate(templateMakerConfig);

        configStr = ResourceUtil.readUtf8Str(rootPath + "templateMakerTest5.json");
        templateMakerConfig = JSONUtil.toBean(configStr, TemplateMakerConfig.class);
        TemplateMaker.makeTemplate(templateMakerConfig);

        configStr = ResourceUtil.readUtf8Str(rootPath + "templateMakerTest6.json");
        templateMakerConfig = JSONUtil.toBean(configStr, TemplateMakerConfig.class);
        TemplateMaker.makeTemplate(templateMakerConfig);

        configStr = ResourceUtil.readUtf8Str(rootPath + "templateMakerTest7.json");
        templateMakerConfig = JSONUtil.toBean(configStr, TemplateMakerConfig.class);
        TemplateMaker.makeTemplate(templateMakerConfig);

        configStr = ResourceUtil.readUtf8Str(rootPath + "templateMakerTest8.json");
        templateMakerConfig = JSONUtil.toBean(configStr, TemplateMakerConfig.class);
        TemplateMaker.makeTemplate(templateMakerConfig);
        System.out.println(id);

    }
}