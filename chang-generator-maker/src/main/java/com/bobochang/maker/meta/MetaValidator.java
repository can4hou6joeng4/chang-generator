package com.bobochang.maker.meta;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.bobochang.maker.meta.enums.FileGenerateTypeEnum;
import com.bobochang.maker.meta.enums.FileTypeEnum;
import com.bobochang.maker.meta.enums.ModelTypeEnum;

import java.io.File;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

/**
 * @author bobochang
 * @Description 元信息校验规则类
 * @Date 2024/1/9 - 10:26
 */
public class MetaValidator {
    public static void doValidAndFill(Meta meta) {
        // 基础信息校验和填充默认值
        validAndFillMetaRoot(meta);
        // fileConfig校验和填充默认值
        validAndFillFileConfig(meta);
        // modelConfig校验和填充默认值
        validAndFillModelConfig(meta);
    }

    private static void validAndFillModelConfig(Meta meta) {
        Meta.ModelConfig modelConfig = meta.getModelConfig();
        if (modelConfig == null) {
            return;
        }
        // modelConfig 默认值
        List<Meta.ModelConfig.ModelsInfo> modelsInfoList = modelConfig.getModels();
        if (CollectionUtil.isEmpty(modelsInfoList)) {
            return;
        }
        for (Meta.ModelConfig.ModelsInfo modelsInfo : modelsInfoList) {
            String fieldName = modelsInfo.getFieldName();
            if (StrUtil.isBlank(fieldName)) {
                throw new MetaException("未填写 fieldName");
            }
            String modelInfoType = modelsInfo.getType();
            if (StrUtil.isEmpty(modelInfoType)) {
                modelsInfo.setType(ModelTypeEnum.STRING.getValue());
            }
        }
    }

    private static void validAndFillFileConfig(Meta meta) {
        // fileConfig 默认值
        Meta.FileConfig fileConfig = meta.getFileConfig();
        if (fileConfig == null) {
            return;
        }
        // sourceRootPath 必填
        String sourceRootPath = fileConfig.getSourceRootPath();
        if (StrUtil.isBlank(sourceRootPath)) {
            throw new MetaException("未填写 sourceRootPath");
        }
        // inputPath: .source + sourceRootPath 的最后一个层级
        String inputRootPath = fileConfig.getInputRootPath();
        String defaultInputPath = ".source" + File.separator + FileUtil.getLastPathEle(Paths.get(sourceRootPath)).getFileName().toString();
        if (StrUtil.isEmpty(inputRootPath)) {
            fileConfig.setInputRootPath(defaultInputPath);
        }
        // outputPath: 默认当前路径下的 generated
        String outputRootPath = fileConfig.getOutputRootPath();
        String defaultOutputRootPath = "generated";
        if (StrUtil.isEmpty(outputRootPath)) {
            fileConfig.setOutputRootPath(defaultOutputRootPath);
        }
        // fileType 默认值：dir
        String fileType = fileConfig.getType();
        String defaultFileType = FileTypeEnum.DIR.getValue();
        if (fileType.isEmpty()) {
            fileConfig.setType(defaultFileType);
        }
        // fileInfo 默认值
        List<Meta.FileConfig.FileInfo> fileInfoList = fileConfig.getFiles();
        if (CollectionUtil.isEmpty(fileInfoList)) {
            return;
        }
        for (Meta.FileConfig.FileInfo fileInfo : fileInfoList) {
            // inputPath：必填
            String inputPath = fileInfo.getInputPath();
            if (StrUtil.isBlank(inputPath)) {
                throw new MetaException("未填写 inputPath");
            }
            // outputPath：默认等于 inputPath
            String outputPath = fileInfo.getOutputPath();
            if (StrUtil.isEmpty(outputPath)) {
                fileInfo.setOutputPath(inputPath);
            }
            // type：若inputPath有文件后缀.java 则为 Java 为 file，反之为 dir
            String type = fileInfo.getType();
            if (StrUtil.isBlank(type)) {
                if (StrUtil.isBlank(FileUtil.getSuffix(inputPath))) {
                    fileInfo.setType(FileTypeEnum.DIR.getValue());
                } else {
                    fileInfo.setType(FileTypeEnum.FILE.getValue());
                }
            }
            // generateType：如果文件结尾不为 ftl，generateType默认为 static，否则为 file
            String generateType = fileInfo.getGenerateType();
            if (StrUtil.isBlank(generateType)) {
                // 动态模板
                if (StrUtil.endWith(inputPath, ".ftl")) {
                    fileInfo.setGenerateType(FileGenerateTypeEnum.DYNAMIC.getValue());
                } else {
                    fileInfo.setGenerateType(FileGenerateTypeEnum.STATIC.getValue());
                }
            }
        }
    }

    private static void validAndFillMetaRoot(Meta meta) {
        String name = StrUtil.blankToDefault(meta.getName(), "my-generator");
        String description = StrUtil.emptyToDefault(meta.getDescription(), "我的模板生成器");
        String author = StrUtil.emptyToDefault(meta.getAuthor(), "bobochang");
        String basePackage = StrUtil.blankToDefault(meta.getBasePackage(), "com.bobochang");
        String version = StrUtil.emptyToDefault(meta.getVersion(), "1.0");
        String createTime = StrUtil.emptyToDefault(meta.getCreateTime(), DateUtil.now());
        meta.setName(name);
        meta.setName(description);
        meta.setName(author);
        meta.setName(basePackage);
        meta.setName(version);
        meta.setName(createTime);
    }
}
