package com.bobochang.maker.template.config;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author bobochang
 * @Description 模板制作文件配置
 * @Date 2024/1/19 - 11:05
 */
@Data
public class TemplateMakerFileConfig {

    private List<FileInfoConfig> files;

    private FileGroupConfig fileGroupConfig;

    @Data
    @NoArgsConstructor
    public static class FileInfoConfig {
        private String path;
        private String condition;
        private List<FileFilterConfig> filterConfigList;
    }

    @Data
    @NoArgsConstructor
    public static class FileGroupConfig {
        private String condition;
        private String groupKey;
        private String groupName;

    }
}
