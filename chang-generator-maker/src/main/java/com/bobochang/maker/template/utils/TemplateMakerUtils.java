package com.bobochang.maker.template.utils;

import cn.hutool.core.util.StrUtil;
import com.bobochang.maker.meta.Meta;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author bobochang
 * @Description 模板制作工具类
 * @Date 2024/1/26 - 11:12
 */
public class TemplateMakerUtils {
    /**
     * 从未分组的文件中移除组内的文件
     *
     * @param fileInfoList 未进行移除的文件列表
     * @return 已移除重复且未分组的组内文件
     */
    public static List<Meta.FileConfig.FileInfo> removeGroupFilesFromRoot(List<Meta.FileConfig.FileInfo> fileInfoList) {
        // 先获取所有分组
        List<Meta.FileConfig.FileInfo> groupFileInfoList = fileInfoList.stream()
                // 根据是否存在 groupKey 进行过滤
                .filter(fileInfo -> StrUtil.isNotBlank(fileInfo.getGroupKey()))
                // 将过滤得到的内容存储为 list
                .collect(Collectors.toList());
        // 获取所有分组的文件列表
        List<Meta.FileConfig.FileInfo> groupInnerFileInfoList = groupFileInfoList.stream()
                .flatMap(fileInfo -> fileInfo.getFiles().stream())
                .collect(Collectors.toList());
        // 获取所有分组文件列表中的输入路径集合
        Set<String> fileInputPathSet = groupInnerFileInfoList.stream()
                // 根据输入路径存储为 map 集合
                .map(Meta.FileConfig.FileInfo::getInputPath)
                // 将 map 集合存储为 Set 集合
                .collect(Collectors.toSet());
        // 移除所有在集合内的外层文件
        return fileInfoList.stream()
                .filter(fileInfo -> !fileInputPathSet.contains(fileInfo.getInputPath()))
                .collect(Collectors.toList());
    }
}
