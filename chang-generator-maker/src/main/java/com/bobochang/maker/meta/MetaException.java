package com.bobochang.maker.meta;

/**
 * @author bobochang
 * @Description 元信息输入错误异常处理类
 * @Date 2024/1/9 - 10:22
 */
public class MetaException extends RuntimeException {
    public MetaException(String message) {
        super(message);
    }

    public MetaException(String message, Throwable cause) {
        super(message, cause);
    }
}
