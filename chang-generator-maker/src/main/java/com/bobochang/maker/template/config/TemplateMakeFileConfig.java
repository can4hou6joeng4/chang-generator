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
public class TemplateMakeFileConfig {

    private List<FileInfoConfig> file;

    @Data
    @NoArgsConstructor
    public static class FileInfoConfig {
        private String path;
        private List<FileFilterConfig> filterConfigList;
    }
}
