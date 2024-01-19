package com.bobochang.maker.template.filter;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import com.bobochang.maker.template.config.FileFilterConfig;
import com.bobochang.maker.template.enums.FileFilterRangeEnum;
import com.bobochang.maker.template.enums.FileFilterRuleEnum;

import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author bobochang
 * @Description
 * @Date 2024/1/19 - 11:28
 */
public class FileFilter {

    /**
     * 对文件或目录进行过滤
     *
     * @param filePath             文件路径
     * @param fileFilterConfigList 过滤规则
     * @return 过滤后文件列表
     */
    public static List<File> doFilter(String filePath, List<FileFilterConfig> fileFilterConfigList) {
        // 根据路径遍历获取所有文件
        List<File> fileList = FileUtil.loopFiles(filePath);
        return fileList.stream()
                .filter(file -> doSingleFileFilter(fileFilterConfigList, file))
                .collect(Collectors.toList());
    }

    /**
     * 单个文件过滤方法
     *
     * @param fileFilterConfigList 过滤规则
     * @param file                 单个文件
     * @return 过滤结果(是否保留 ）
     */
    public static boolean doSingleFileFilter(List<FileFilterConfig> fileFilterConfigList, File file) {
        String fileName = file.getName();
        String fileContent = FileUtil.readUtf8String(file);
        // 所有过滤器校验的结果
        boolean result = true;
        if (CollUtil.isEmpty(fileFilterConfigList)) {
            return true;
        }
        // 获取过滤规则
        for (FileFilterConfig fileFilterConfig : fileFilterConfigList) {
            String range = fileFilterConfig.getRange();
            String rule = fileFilterConfig.getRule();
            String value = fileFilterConfig.getValue();
            // 根据过滤范围规则进行校验
            FileFilterRangeEnum fileFilterRangeEnum = FileFilterRangeEnum.getEnumByValue(range);
            if (fileFilterRangeEnum == null) {
                continue;
            }
            // 需要过滤的原内容
            String content = fileName;
            switch (fileFilterRangeEnum) {
                case FILE_NAME:
                    content = fileName;
                    break;
                case FILE_CONTENT:
                    content = fileContent;
                    break;
                default:
                    break;
            }
            // 根据过滤规则进行校验
            FileFilterRuleEnum fileFilterRuleEnum = FileFilterRuleEnum.getEnumByValue(rule);
            if (fileFilterRuleEnum == null) {
                continue;
            }
            switch (fileFilterRuleEnum) {
                case CONTAINS:
                    result = content.contains(value);
                    break;
                case STARTS_WITH:
                    result = content.startsWith(value);
                    break;
                case ENDS_WITH:
                    result = content.endsWith(value);
                    break;
                case REGEX:
                    result = content.matches(value);
                    break;
                case EQUALS:
                    result = content.equals(value);
                    break;
            }
            // 如果有一个过滤器校验失败，则直接返回false
            if (!result) {
                return false;
            }
        }
        // 均满足过滤条件
        return true;
    }
}
