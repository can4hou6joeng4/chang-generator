package com.bobochang.maker.template;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.bobochang.maker.meta.Meta;
import com.bobochang.maker.meta.enums.FileGenerateTypeEnum;
import com.bobochang.maker.meta.enums.FileTypeEnum;
import com.bobochang.maker.template.config.FileFilterConfig;
import com.bobochang.maker.template.config.TemplateMakeFileConfig;
import com.bobochang.maker.template.enums.FileFilterRangeEnum;
import com.bobochang.maker.template.enums.FileFilterRuleEnum;
import com.bobochang.maker.template.filter.FileFilter;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author bobochang
 * @Description 模板制作工具
 * @Date 2024/1/17 - 11:07
 */
public class TemplateMaker {

    /**
     * 制作模板
     *
     * @param newMeta                输入制作的模型信息
     * @param originalProjectPath    模板项目路径
     * @param templateMakeFileConfig 模板文件路径
     * @param modelInfo              模型信息
     * @param searchStr              挖坑文本
     * @param id                     项目空间中具体项目 id
     * @return
     */
    public static long makeTemplate(Meta newMeta, String originalProjectPath, TemplateMakeFileConfig templateMakeFileConfig, Meta.ModelConfig.ModelsInfo modelInfo, String searchStr, Long id) {
        // 判断是否已存在：有无 id 没有则返回生成 id
        if (id == null) {
            id = IdUtil.getSnowflakeNextId();
        }
        // 1.指定原始项目路径
        String projectPath = System.getProperty("user.dir");
        // 2.复制目录到指定目录
        String tempDirPath = projectPath + File.separator + ".temp";
        String templatePath = tempDirPath + File.separator + id;
        // 判断目录是否存在
        if (!FileUtil.exist(templatePath)) {
            FileUtil.mkdir(templatePath);
            FileUtil.copy(originalProjectPath, templatePath, true);
        }
        // 3.输入信息
        String sourceRootPath = templatePath + File.separator + FileUtil.getLastPathEle(Paths.get(originalProjectPath)).toString();
        ArrayList<Meta.FileConfig.FileInfo> newFileInfoList = new ArrayList<>();
        // 获取 模板制作配置文件信息
        List<TemplateMakeFileConfig.FileInfoConfig> fileInfoConfigList = templateMakeFileConfig.getFile();
        for (TemplateMakeFileConfig.FileInfoConfig fileInfoConfig : fileInfoConfigList) {
            String fileInputPath = fileInfoConfig.getPath();
            String fileInputAbsolutePath = sourceRootPath + File.separator + fileInputPath;
            // 传入绝对路径 对文件进行过滤
            List<File> fileList = FileFilter.doFilter(fileInputAbsolutePath, fileInfoConfig.getFilterConfigList());
            for (File file : fileList) {
                Meta.FileConfig.FileInfo fileInfo = makeFileTemplate(modelInfo, searchStr, sourceRootPath, file);
                newFileInfoList.add(fileInfo);
            }
        }

        String metaOutputPath = sourceRootPath + File.separator + "meta.json";
        // 判断是否已存在 meta.json文件，若有则表示不是第一次生成制作，则在目前文件上进行追加修改
        if (FileUtil.exist(metaOutputPath)) {
            Meta oldMeta = JSONUtil.toBean(FileUtil.readUtf8String(new File(metaOutputPath)), Meta.class);
            // 1.追加配置参数
            List<Meta.FileConfig.FileInfo> fileInfoList = oldMeta.getFileConfig().getFiles();
            fileInfoList.addAll(newFileInfoList);
            List<Meta.ModelConfig.ModelsInfo> modelInfoList = oldMeta.getModelConfig().getModels();
            modelInfoList.add(modelInfo);
            // 2.配置去重
            oldMeta.getFileConfig().setFiles(distinctFiles(fileInfoList));
            oldMeta.getModelConfig().setModels(distinctModels(modelInfoList));
            // 3.输出元信息文件
            FileUtil.writeUtf8String(JSONUtil.toJsonPrettyStr(oldMeta), metaOutputPath);
        } else {
            Meta.FileConfig fileConfig = new Meta.FileConfig();
            Meta.ModelConfig modelConfig = new Meta.ModelConfig();
            List<Meta.FileConfig.FileInfo> fileInfoList = new ArrayList<>();
            List<Meta.ModelConfig.ModelsInfo> modelInfoList = new ArrayList<>();

            fileConfig.setSourceRootPath(sourceRootPath);
            fileInfoList.addAll(newFileInfoList);
            fileConfig.setFiles(fileInfoList);
            newMeta.setFileConfig(fileConfig);

            modelInfoList.add(modelInfo);
            modelConfig.setModels(modelInfoList);
            newMeta.setModelConfig(modelConfig);

            // 7.输出文件
            FileUtil.writeUtf8String(JSONUtil.toJsonPrettyStr(newMeta), metaOutputPath);
        }

        return id;
    }

    private static Meta.FileConfig.FileInfo makeFileTemplate(Meta.ModelConfig.ModelsInfo modelInfo, String searchStr, String sourceRootPath, File inputFile) {
        // 指定需要挖坑的文件
        String fileInputPath = inputFile.getAbsolutePath().replace(sourceRootPath + File.separator, "");
        String fileOutputPath = fileInputPath + ".ftl";
        // 4.字符串替换
        String fileInputAbsolutePath = inputFile.getAbsolutePath();
        // 5.生成模板文件
        String fileOutputAbsolutePath = inputFile.getAbsolutePath() + ".ftl";
        String fileContent;
        // 判断是否已有模板文件：有则表示不是第一次生成制作 便在生成的模板文件中继续挖坑
        if (FileUtil.exist(fileOutputAbsolutePath)) {
            fileContent = FileUtil.readUtf8String(new File(fileOutputAbsolutePath));
        } else {
            fileContent = FileUtil.readUtf8String(new File(fileInputAbsolutePath));
        }
        String replacement = String.format("${%s}", modelInfo.getFieldName());
        String newFileContent = StrUtil.replace(fileContent, searchStr, replacement);
        // 6.配置文件信息
        Meta.FileConfig.FileInfo fileInfo = new Meta.FileConfig.FileInfo();
        fileInfo.setInputPath(fileInputPath);
        fileInfo.setOutputPath(fileOutputPath);
        fileInfo.setType(FileTypeEnum.FILE.getValue());
        // 判断文件是否存在挖坑 没有则为静态文件
        if (newFileContent.equals(fileContent)) {
            // 静态文件 输出路径 = 输入路径
            fileInfo.setOutputPath(fileInputPath);
            fileInfo.setGenerateType(FileGenerateTypeEnum.STATIC.getValue());
        } else {
            // 动态文件 输出模板文件
            fileInfo.setGenerateType(FileGenerateTypeEnum.DYNAMIC.getValue());
            FileUtil.writeUtf8String(newFileContent, fileOutputAbsolutePath);
        }
        return fileInfo;
    }

    public static void main(String[] args) {
        // 1.项目基本信息
        Meta meta = new Meta();
        meta.setName("acm-template-generator");
        meta.setDescription("ACM示例模板生成器");
        // 2.指定原始项目路径
        String projectPath = System.getProperty("user.dir");
        String originalProjectPath = FileUtil.getAbsolutePath(new File(projectPath).getParentFile()) + File.separator + "chang-generator-demo-projects/springboot-init";
        String fileInputPath1 = "src/main/java/com/yupi/springbootinit/common";
        String fileInputPath2 = "src/main/java/com/yupi/springbootinit/controller";

        // 3.输入模型参数信息
        Meta.ModelConfig.ModelsInfo modelInfo = new Meta.ModelConfig.ModelsInfo();
        /*
        * first init
        modelInfo.setFieldName("outputText");
        modelInfo.setType("String");

        * second init
        modelInfo.setFieldName("className");
        modelInfo.setType("String");
        modelInfo.setDefaultValue("MainTemplate");
        */
        modelInfo.setFieldName("className");
        modelInfo.setType("String");
        modelInfo.setDefaultValue("BaseResponse");

        TemplateMakeFileConfig templateMakeFileConfig = new TemplateMakeFileConfig();
        // 对文件进行过滤
        // 对 fileInputPath1 文件进行配置
        TemplateMakeFileConfig.FileInfoConfig fileInfoConfig1 = new TemplateMakeFileConfig.FileInfoConfig();
        List<FileFilterConfig> fileFilterConfigList = new ArrayList<>();
        FileFilterConfig fileFilterConfig = FileFilterConfig.builder()
                // 定义过滤规则(范围/规则/值)
                .range(FileFilterRangeEnum.FILE_NAME.getValue())
                .rule(FileFilterRuleEnum.CONTAINS.getValue())
                .value("Base")
                .build();
        fileFilterConfigList.add(fileFilterConfig);
        fileInfoConfig1.setPath(fileInputPath1);
        fileInfoConfig1.setFilterConfigList(fileFilterConfigList);
        // 对 fileInputPath2 文件进行配置
        TemplateMakeFileConfig.FileInfoConfig fileInfoConfig2 = new TemplateMakeFileConfig.FileInfoConfig();
        fileInfoConfig2.setPath(fileInputPath2);
        // 设置多组文件进行配置
        List<TemplateMakeFileConfig.FileInfoConfig> fileInfoConfigList = (List<TemplateMakeFileConfig.FileInfoConfig>) Arrays.asList(fileInfoConfig1, fileInfoConfig2);
        templateMakeFileConfig.setFile(fileInfoConfigList);
        // 制作模板
        TemplateMaker.makeTemplate(meta, originalProjectPath, templateMakeFileConfig, modelInfo, "BaseResponse", null);
    }

    /**
     * 文件去重
     *
     * @param fileInfoList 所需去重的文件
     * @return 去重后的文件
     */
    private static List<Meta.FileConfig.FileInfo> distinctFiles(List<Meta.FileConfig.FileInfo> fileInfoList) {
        return new ArrayList<>(
                fileInfoList.stream()
                        .collect(Collectors.toMap(Meta.FileConfig.FileInfo::getInputPath, fileInfo -> fileInfo, (oldFile, replaceFile) -> replaceFile))
                        .values());
    }

    /**
     * 模型去重
     *
     * @param modelInfoList 所需去重的模型
     * @return 去重后的模型
     */
    private static List<Meta.ModelConfig.ModelsInfo> distinctModels(List<Meta.ModelConfig.ModelsInfo> modelInfoList) {
        return new ArrayList<>(
                modelInfoList.stream()
                        .collect(Collectors.toMap(Meta.ModelConfig.ModelsInfo::getFieldName, modelInfo -> modelInfo, (oldModel, replaceModel) -> replaceModel))
                        .values());
    }
}
