package com.bobochang.maker.template.config;

import lombok.Data;

/**
 * @author bobochang
 * @Description 模板文件输出配置
 * @Date 2024/1/26 - 11:05
 */
@Data
public class TemplateMakerOutputConfig {

    // 从未分组的文件中移除组内的同名文件
    private boolean removeGroupFileFromRoot = true;
}
