package com.bobochang.maker.template;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.bobochang.maker.meta.Meta;
import com.bobochang.maker.meta.enums.FileGenerateTypeEnum;
import com.bobochang.maker.meta.enums.FileTypeEnum;
import com.bobochang.maker.template.config.*;
import com.bobochang.maker.template.enums.FileFilterRangeEnum;
import com.bobochang.maker.template.enums.FileFilterRuleEnum;
import com.bobochang.maker.template.filter.FileFilter;
import com.bobochang.maker.template.utils.TemplateMakerUtils;

import java.io.File;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author bobochang
 * @Description 模板制作工具
 * @Date 2024/1/17 - 11:07
 */
public class TemplateMaker {

    public static void main(String[] args) {
        // 1.项目基本信息
        Meta meta = new Meta();
        meta.setName("acm-template-generator");
        meta.setDescription("ACM示例模板生成器");
        // 2.指定原始项目路径
        String projectPath = System.getProperty("user.dir");
        String originalProjectPath = FileUtil.getAbsolutePath(new File(projectPath).getParentFile()) + File.separator + "chang-generator-demo-projects/springboot-init";
        String fileInputPath1 = "src/main/java/com/yupi/springbootinit/common";
        String fileInputPath2 = "src/main/resources/application.yml";

        // 3.输入模型参数信息
        Meta.ModelConfig.ModelsInfo modelInfo = new Meta.ModelConfig.ModelsInfo();
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

        TemplateMakerFileConfig templateMakerFileConfig = new TemplateMakerFileConfig();
        // 对文件进行过滤
        // 对 fileInputPath1 文件进行配置
        TemplateMakerFileConfig.FileInfoConfig fileInfoConfig1 = new TemplateMakerFileConfig.FileInfoConfig();
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
        TemplateMakerFileConfig.FileInfoConfig fileInfoConfig2 = new TemplateMakerFileConfig.FileInfoConfig();
        fileInfoConfig2.setPath(fileInputPath2);
        // 设置多组文件进行配置
        List<TemplateMakerFileConfig.FileInfoConfig> fileInfoConfigList = (List<TemplateMakerFileConfig.FileInfoConfig>) Arrays.asList(fileInfoConfig1, fileInfoConfig2);
        templateMakerFileConfig.setFiles(fileInfoConfigList);
        // 设置分组配置
        TemplateMakerFileConfig.FileGroupConfig fileGroupConfig = new TemplateMakerFileConfig.FileGroupConfig();
        fileGroupConfig.setGroupKey("testEdit");
        fileGroupConfig.setGroupName("测试修改分组");
        fileGroupConfig.setCondition("outputText");
        templateMakerFileConfig.setFileGroupConfig(fileGroupConfig);
        // 制作模板
        TemplateMaker.makeTemplate(meta, originalProjectPath, templateMakerFileConfig, templateMakerModelConfig, null, null);
    }

    public static long makeTemplate(TemplateMakerConfig templateMakerConfig) {
        Long id = templateMakerConfig.getId();
        Meta meta = templateMakerConfig.getMeta();
        String originProjectPath = templateMakerConfig.getOriginProjectPath();
        TemplateMakerFileConfig fileConfig = templateMakerConfig.getFileConfig();
        TemplateMakerModelConfig modelConfig = templateMakerConfig.getModelConfig();
        TemplateMakerOutputConfig outputConfig = templateMakerConfig.getOutputConfig();
        return makeTemplate(meta, originProjectPath, fileConfig, modelConfig, outputConfig, id);
    }

    /**
     * 制作模板
     *
     * @param newMeta                  输入制作的模型信息
     * @param originalProjectPath      模板项目路径
     * @param templateMakerFileConfig  模板文件配置
     * @param templateMakerModelConfig 模型模型配置
     * @param id                       项目空间中具体项目 id
     * @return
     */
    public static long makeTemplate(Meta newMeta, String originalProjectPath,
                                    TemplateMakerFileConfig templateMakerFileConfig, TemplateMakerModelConfig templateMakerModelConfig,
                                    TemplateMakerOutputConfig templateMakerOutputConfig, Long id) {
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
        // 获取工作目录下的第一个层级的文件夹
        String sourceRootPath = FileUtil.loopFiles(new File(templatePath), 1, null)
                .stream()
                .filter(File::isDirectory)
                .findFirst()
                .orElseThrow(RuntimeException::new)
                .getAbsolutePath();
        // 获取文件参数配置信息 制作文件模板
        List<Meta.FileConfig.FileInfo> newFileInfoList = getFileInfoList(templateMakerFileConfig, templateMakerModelConfig, sourceRootPath);
        // 获取模型参数配置信息 制作模型模板
        List<Meta.ModelConfig.ModelsInfo> newModelInfoList = getModelInfoList(templateMakerModelConfig);
        // 将 meta.json 文件生成到上一级目录
        String metaOutputPath = templatePath + File.separator + "meta.json";
        // 判断是否已存在 meta.json文件，若有则表示不是第一次生成制作，则在目前文件上进行追加修改
        if (FileUtil.exist(metaOutputPath)) {
            Meta oldMeta = JSONUtil.toBean(FileUtil.readUtf8String(new File(metaOutputPath)), Meta.class);
            BeanUtil.copyProperties(newMeta, oldMeta, CopyOptions.create().ignoreNullValue());
            newMeta = oldMeta;
            // 1.追加配置参数
            List<Meta.FileConfig.FileInfo> fileInfoList = newMeta.getFileConfig().getFiles();
            fileInfoList.addAll(newFileInfoList);
            List<Meta.ModelConfig.ModelsInfo> modelInfoList = newMeta.getModelConfig().getModels();
            modelInfoList.addAll(newModelInfoList);
            // 2.配置去重
            newMeta.getFileConfig().setFiles(distinctFiles(fileInfoList));
            newMeta.getModelConfig().setModels(distinctModels(modelInfoList));
        } else {
            Meta.FileConfig fileConfig = new Meta.FileConfig();
            Meta.ModelConfig modelConfig = new Meta.ModelConfig();
            List<Meta.FileConfig.FileInfo> fileInfoList = new ArrayList<>();
            List<Meta.ModelConfig.ModelsInfo> modelInfoList = new ArrayList<>();

            fileConfig.setSourceRootPath(sourceRootPath);
            fileInfoList.addAll(newFileInfoList);
            fileConfig.setFiles(fileInfoList);
            newMeta.setFileConfig(fileConfig);

            modelInfoList.addAll(newModelInfoList);
            modelConfig.setModels(modelInfoList);
            newMeta.setModelConfig(modelConfig);

        }
        // 判断是否存在额外的输出配置
        if (templateMakerOutputConfig != null) {
            if (templateMakerOutputConfig.isRemoveGroupFileFromRoot()) {
                List<Meta.FileConfig.FileInfo> fileInfoList = newMeta.getFileConfig().getFiles();
                newMeta.getFileConfig().setFiles(TemplateMakerUtils.removeGroupFilesFromRoot(fileInfoList));
            }
        }
        // 7.输出文件
        FileUtil.writeUtf8String(JSONUtil.toJsonPrettyStr(newMeta), metaOutputPath);
        return id;
    }

    private static List<Meta.ModelConfig.ModelsInfo> getModelInfoList(TemplateMakerModelConfig templateMakerModelConfig) {
        // 定义新列表存储新增的模型配置信息
        List<Meta.ModelConfig.ModelsInfo> newModelInfoList = new ArrayList<>();
        // 非空校验
        if (templateMakerModelConfig == null) {
            return newModelInfoList;
        }
        // 处理模型信息
        List<TemplateMakerModelConfig.ModelInfoConfig> models = templateMakerModelConfig.getModels();
        // 非空校验
        if (CollUtil.isEmpty(models)) {
            return newModelInfoList;
        }
        // 转换为 ModelInfo 对象
        List<Meta.ModelConfig.ModelsInfo> inputModelInfoList = models.stream().map(
                modelInfoConfig -> {
                    Meta.ModelConfig.ModelsInfo modelInfo = new Meta.ModelConfig.ModelsInfo();
                    BeanUtil.copyProperties(modelInfoConfig, modelInfo);
                    return modelInfo;
                }).collect(Collectors.toList());
        // 判断是否为模型组
        TemplateMakerModelConfig.ModelGroupConfig modelGroupConfig = templateMakerModelConfig.getModelGroupConfig();
        if (modelGroupConfig != null) {
            Meta.ModelConfig.ModelsInfo groupModelInfo = new Meta.ModelConfig.ModelsInfo();
            BeanUtil.copyProperties(modelGroupConfig, groupModelInfo);
            // 相同模型放在同一个分组内
            groupModelInfo.setModels(inputModelInfoList);
            newModelInfoList.add(groupModelInfo);
        } else {
            // 不分组 将所有模型放在同一个分组内
            newModelInfoList.addAll(inputModelInfoList);
        }
        return newModelInfoList;
    }

    private static List<Meta.FileConfig.FileInfo> getFileInfoList(TemplateMakerFileConfig templateMakerFileConfig, TemplateMakerModelConfig templateMakerModelConfig, String sourceRootPath) {
        List<Meta.FileConfig.FileInfo> newFileInfoList = new ArrayList<>();
        if (templateMakerFileConfig == null) {
            return newFileInfoList;
        }
        List<TemplateMakerFileConfig.FileInfoConfig> fileInfoConfigList = templateMakerFileConfig.getFiles();
        if (CollUtil.isEmpty(fileInfoConfigList)) {
            return newFileInfoList;
        }
        TemplateMakerFileConfig.FileGroupConfig fileGroupConfig = templateMakerFileConfig.getFileGroupConfig();
        // 获取模板制作配置文件信息
        for (TemplateMakerFileConfig.FileInfoConfig fileInfoConfig : fileInfoConfigList) {
            String fileInputPath = fileInfoConfig.getPath();
            String fileInputAbsolutePath = sourceRootPath + File.separator + fileInputPath;
            // 传入绝对路径 对文件进行过滤
            List<File> fileList = FileFilter.doFilter(fileInputAbsolutePath, fileInfoConfig.getFilterConfigList());
            // 对过滤后的文件再次进行过滤 对 FTL 模板进行过滤
            fileList = fileList.stream()
                    .filter(file -> !file.getAbsolutePath().endsWith(".ftl"))
                    .collect(Collectors.toList());
            for (File file : fileList) {
                Meta.FileConfig.FileInfo fileInfo = makeFileTemplate(templateMakerModelConfig, fileInfoConfig, sourceRootPath, file);
                newFileInfoList.add(fileInfo);
            }
        }
        // 判断是否为文件组
        if (fileGroupConfig != null) {
            String condition = fileGroupConfig.getCondition();
            String groupKey = fileGroupConfig.getGroupKey();
            String groupName = fileGroupConfig.getGroupName();
            Meta.FileConfig.FileInfo groupFileInfo = new Meta.FileConfig.FileInfo();
            groupFileInfo.setType(FileTypeEnum.GROUP.getValue());
            groupFileInfo.setCondition(condition);
            groupFileInfo.setGroupKey(groupKey);
            groupFileInfo.setGroupName(groupName);
            // 将文件全放到一个组内
            groupFileInfo.setFiles(newFileInfoList);
            newFileInfoList = new ArrayList<>();
            newFileInfoList.add(groupFileInfo);
        }
        return newFileInfoList;
    }

    private static Meta.FileConfig.FileInfo makeFileTemplate(TemplateMakerModelConfig templateMakerModelConfig, TemplateMakerFileConfig.FileInfoConfig fileInfoConfig, String sourceRootPath, File inputFile) {
        // 指定需要挖坑的文件
        String fileInputPath = inputFile.getAbsolutePath().replace(sourceRootPath + File.separator, "");
        String fileOutputPath = fileInputPath + ".ftl";
        // 4.字符串替换
        String fileInputAbsolutePath = inputFile.getAbsolutePath();
        // 5.生成模板文件
        String fileOutputAbsolutePath = inputFile.getAbsolutePath() + ".ftl";
        String fileContent;
        // 判断是否已有模板文件：有则表示不是第一次生成制作 便在生成的模板文件中继续挖坑
        boolean hasTemplate = FileUtil.exist(fileOutputAbsolutePath);
        if (hasTemplate) {
            fileContent = FileUtil.readUtf8String(new File(fileOutputAbsolutePath));
        } else {
            fileContent = FileUtil.readUtf8String(new File(fileInputAbsolutePath));
        }
        // 支持多模型配置信息：对同一个文件内容挖坑 进行多次替换 遍历模型进行多轮替换进而实现多模型挖坑
        TemplateMakerModelConfig.ModelGroupConfig modelGroupConfig = templateMakerModelConfig.getModelGroupConfig();
        String newFileContent = fileContent;
        String replacement;
        // 判断当前模型是为存在分组
        for (TemplateMakerModelConfig.ModelInfoConfig modelInfoConfig : templateMakerModelConfig.getModels()) {
            String fieldName = modelInfoConfig.getFieldName();
            // 模型配置
            // 判断是否为分组
            if (modelGroupConfig == null) {
                replacement = String.format("${%s}", fieldName);
            } else {
                String groupKey = modelGroupConfig.getGroupKey();
                replacement = String.format("${%s.%s}", groupKey, fieldName);
            }
            newFileContent = StrUtil.replace(newFileContent, modelInfoConfig.getReplaceText(), replacement);
        }
        // 6.配置文件信息
        Meta.FileConfig.FileInfo fileInfo = new Meta.FileConfig.FileInfo();
        fileInfo.setInputPath(fileOutputPath);
        fileInfo.setOutputPath(fileInputPath);
        fileInfo.setCondition(fileInfoConfig.getCondition());
        fileInfo.setType(FileTypeEnum.FILE.getValue());
        fileInfo.setGenerateType(FileGenerateTypeEnum.DYNAMIC.getValue());
        // 判断是否存在模板文件
        boolean contentEquals = newFileContent.equals(fileContent);
        if (!hasTemplate) {
            // 判断是否更改文件内容
            if (contentEquals) {
                // 静态文件 输出路径 = 输入路径
                fileInfo.setInputPath(fileInputPath);
                fileInfo.setGenerateType(FileGenerateTypeEnum.STATIC.getValue());
            } else {
                // 没模板要挖坑
                FileUtil.writeUtf8String(newFileContent, fileOutputAbsolutePath);
            }
        } else if (!contentEquals) {
            // 有模板 有新坑
            FileUtil.writeUtf8String(newFileContent, fileOutputAbsolutePath);
        }
        return fileInfo;
    }

    /**
     * 文件去重
     *
     * @param fileInfoList 所需去重的文件
     * @return 去重后的文件
     */
    private static List<Meta.FileConfig.FileInfo> distinctFiles(List<Meta.FileConfig.FileInfo> fileInfoList) {
        // 策略: 同分组内文件合并 merge 不同分组保留
        // 1.将所有文件配置(fileInfo)分为有分组和没分组(有无 groupKey)
        // 示例：{"groupKey":"a","files":[1,2]},{"groupKey":"a","files":[2,3]},{"groupKey":"b","files":[4,5]}
        // => {"groupKey":"a","files":[[1,2],[2,3]]},{"groupKey":"b","files":[4,5]}
        Map<String, List<Meta.FileConfig.FileInfo>> groupKeyFileInfoListMap = fileInfoList.stream()
                // 先根据是否存在 groupKey 进行过滤
                .filter(fileInfo -> StrUtil.isNotBlank(fileInfo.getGroupKey()))
                // 将过滤后的内容 以相同 groupKey 进行分组
                // 分组后得到的对象转换为 Map 以 groupKey 为 key 值，以 List<Meta.FileConfig.FileInfo> 为 value 值
                .collect(Collectors.groupingBy(Meta.FileConfig.FileInfo::getGroupKey));
        // 2.对于有分组的文件配置 如果有相同的分组 分组内的文件进行合并 不同分组同时保留
        HashMap<String, Meta.FileConfig.FileInfo> groupKeyMergedFileInfoMap = new HashMap<>();
        // 对同分组的进行 merge
        // 示例：{"groupKey":"a","files":[[1,2],[2,3]]} => {"groupKey":"a","files":[1,2,3]}
        for (Map.Entry<String, List<Meta.FileConfig.FileInfo>> entry : groupKeyFileInfoListMap.entrySet()) {
            // 获取同分组下的文件配置列表 (示例中的[[1,2],[2,3]])
            List<Meta.FileConfig.FileInfo> tempFileInfoList = entry.getValue();
            // 相同分组下的文件合并
            List<Meta.FileConfig.FileInfo> newFileInfoList = new ArrayList<>(tempFileInfoList.stream()
                    // 展开文件配置列表 (示例中的[[1,2],[2,3]] => [1,2,2,3] )
                    .flatMap(fileInfo -> fileInfo.getFiles().stream())
                    // 合并相同的文件配置 (示例中的[1,2,2,3] => [1,2,3])
                    .collect(Collectors.toMap(Meta.FileConfig.FileInfo::getOutputPath, fileInfo -> fileInfo, (oldFile, replaceFile) -> replaceFile))
                    // 将合并后的文件配置列表转换为 List<Meta.FileConfig.FileInfo> (示例中的[1,2,3] => {"groupKey":"a","files":[1,2,3]})
                    .values());
            // 将合并后的文件配置列表添加到新的 group 配置中
            Meta.FileConfig.FileInfo newFileInfo = CollUtil.getLast(tempFileInfoList);
            newFileInfo.setFiles(newFileInfoList);
            String groupKey = entry.getKey();
            groupKeyMergedFileInfoMap.put(groupKey, newFileInfo);
        }
        // 3.创建新的文件配置列表(结果列表) 先将合并后的分组添加到结果列表
        List<Meta.FileConfig.FileInfo> reslutList = new ArrayList<>(groupKeyMergedFileInfoMap.values());
        // 4.再将无分组的文件配置列表添加到结果列表
        reslutList.addAll(new ArrayList<>(fileInfoList.stream()
                // 过滤没有 groupKey 的文件配置
                .filter(fileInfo -> StrUtil.isEmpty(fileInfo.getGroupKey()))
                // 合并 inputPath 相同的文件配置
                .collect(Collectors.toMap(Meta.FileConfig.FileInfo::getOutputPath, fileInfo -> fileInfo, (oldFile, replaceFile) -> replaceFile))
                .values()));
        return reslutList;
    }

    /**
     * 模型去重
     *
     * @param modelInfoList 所需去重的模型
     * @return 去重后的模型
     */
    private static List<Meta.ModelConfig.ModelsInfo> distinctModels(List<Meta.ModelConfig.ModelsInfo> modelInfoList) {
        // 策略: 同分组内模型合并 merge 不同分组保留
        // 1.将所有模型配置(modelInfo)分为有分组和没分组(有无 groupKey)
        // 示例：{"groupKey":"a","models":[1,2]},{"groupKey":"a","models":[2,3]},{"groupKey":"b","models":[4,5]}
        // => {"groupKey":"a","models":[[1,2],[2,3]]},{"groupKey":"b","models":[4,5]}
        Map<String, List<Meta.ModelConfig.ModelsInfo>> groupKeyModelsInfoListMap = modelInfoList.stream()
                // 先根据是否存在 groupKey 进行过滤
                .filter(modelInfo -> StrUtil.isNotBlank(modelInfo.getGroupKey()))
                // 将过滤后的内容 以相同 groupKey 进行分组
                // 分组后得到的对象转换为 Map 以 groupKey 为 key 值，以 List<Meta.ModelConfig.ModelsInfo> 为 value 值
                .collect(Collectors.groupingBy(Meta.ModelConfig.ModelsInfo::getGroupKey));
        // 2.对于有分组的模型配置 如果有相同的分组 分组内的模型进行合并 不同分组同时保留
        HashMap<String, Meta.ModelConfig.ModelsInfo> groupKeyMergedModelsInfoMap = new HashMap<>();
        // 对同分组的进行 merge
        // 示例：{"groupKey":"a","models":[[1,2],[2,3]]} => {"groupKey":"a","models":[1,2,3]}
        for (Map.Entry<String, List<Meta.ModelConfig.ModelsInfo>> entry : groupKeyModelsInfoListMap.entrySet()) {
            // 获取同分组下的模型配置列表 (示例中的[[1,2],[2,3]])
            List<Meta.ModelConfig.ModelsInfo> tempModelsInfoList = entry.getValue();
            // 相同分组下的模型合并
            List<Meta.ModelConfig.ModelsInfo> newModelsInfoList = new ArrayList<>(tempModelsInfoList.stream()
                    // 展开模型配置列表 (示例中的[[1,2],[2,3]] => [1,2,2,3] )
                    .flatMap(modelInfo -> modelInfo.getModels().stream())
                    // 合并相同的模型配置 (示例中的[1,2,2,3] => [1,2,3])
                    .collect(Collectors.toMap(Meta.ModelConfig.ModelsInfo::getFieldName, modelInfo -> modelInfo, (oldModel, replaceModel) -> replaceModel))
                    // 将合并后的模型配置列表转换为 List<Meta.ModelConfig.ModelsInfo> (示例中的[1,2,3] => {"groupKey":"a","models":[1,2,3]})
                    .values());
            // 将合并后的模型配置列表添加到新的 group 配置中
            Meta.ModelConfig.ModelsInfo newModelsInfo = CollUtil.getLast(tempModelsInfoList);
            newModelsInfo.setModels(newModelsInfoList);
            String groupKey = entry.getKey();
            groupKeyMergedModelsInfoMap.put(groupKey, newModelsInfo);
        }
        // 3.创建新的模型配置列表(结果列表) 先将合并后的分组添加到结果列表
        List<Meta.ModelConfig.ModelsInfo> reslutList = new ArrayList<>(groupKeyMergedModelsInfoMap.values());
        // 4.再将无分组的模型配置列表添加到结果列表
        reslutList.addAll(new ArrayList<>(modelInfoList.stream()
                // 过滤没有 groupKey 的模型配置
                .filter(modelInfo -> StrUtil.isEmpty(modelInfo.getGroupKey()))
                // 合并 inputPath 相同的模型配置
                .collect(Collectors.toMap(Meta.ModelConfig.ModelsInfo::getFieldName, modelInfo -> modelInfo, (oldModel, replaceModel) -> replaceModel))
                .values()));
        return reslutList;
    }
}
