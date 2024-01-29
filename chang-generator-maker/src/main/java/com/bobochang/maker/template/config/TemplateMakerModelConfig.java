package com.bobochang.maker.template.config;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author bobochang
 * @Description 模板制作模型配置
 * @Date 2024/1/19 - 11:05
 */
@Data
public class TemplateMakerModelConfig {

    private List<ModelInfoConfig> models;

    private ModelGroupConfig modelGroupConfig;

    @Data
    @NoArgsConstructor
    public static class ModelInfoConfig {
        private String fieldName;
        private String type;
        private String description;
        private Object defaultValue;
        private String abbr;
        // 用于替换文本的值
        private String replaceText;
    }

    @Data
    @NoArgsConstructor
    public static class ModelGroupConfig {
        private String condition;
        private String groupKey;
        private String groupName;
        private String type;
        private String description;
    }
}
