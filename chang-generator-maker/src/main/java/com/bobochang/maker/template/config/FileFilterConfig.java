package com.bobochang.maker.template.config;

import lombok.Builder;
import lombok.Data;

/**
 * @author bobochang
 * @Description 文件过滤配置
 * @Date 2024/1/19 - 11:00
 */
@Data
@Builder
public class FileFilterConfig {
    /**
     * 过滤范围
     */
    private String range;
    /**
     * 过滤规则
     */
    private String rule;
    /**
     * 过滤值
     */
    private String value;
}
