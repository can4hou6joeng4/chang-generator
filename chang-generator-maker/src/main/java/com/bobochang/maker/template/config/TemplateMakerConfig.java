package com.bobochang.maker.template.config;

import com.bobochang.maker.meta.Meta;
import lombok.Data;

/**
 * @author bobochang
 * @Description
 * @Date 2024/1/24 - 16:06
 */
@Data
public class TemplateMakerConfig {
    private Long id;
    private Meta meta = new Meta();
    private String originProjectPath;
    private TemplateMakerFileConfig fileConfig = new TemplateMakerFileConfig();
    private TemplateMakerModelConfig modelConfig = new TemplateMakerModelConfig();
    private TemplateMakerOutputConfig outputConfig = new TemplateMakerOutputConfig();
}
