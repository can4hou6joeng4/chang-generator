package com.bobochang.maker.meta;

import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.json.JSONUtil;

/**
 * @author bobochang
 * @Description 双检锁单例模式实例化 Meta 数据
 * @Date 2023/12/21 - 17:11
 */
public class MetaManager {

    private static volatile Meta meta;

    private MetaManager() {
        // 私有构造函数，防止外部实例化
    }

    /**
     * 获取 Meta 对象方法
     * 双检锁单例模式实例化
     *
     * @return 返回 Meta 对象
     */
    public static Meta getMetaObject() {
        if (meta == null) {
            synchronized (MetaManager.class) {
                if (meta == null) {
                    meta = initMeta();
                }
            }
        }
        return meta;
    }

    private static Meta initMeta() {
        String metaJson = ResourceUtil.readUtf8Str("meta.json");
        Meta newMeta = JSONUtil.toBean(metaJson, Meta.class);
        MetaValidator.doValidAndFill(newMeta);
        return newMeta;
    }
}
