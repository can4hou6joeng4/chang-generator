package com.bobochang.maker.cli.command;

import cn.hutool.core.util.ReflectUtil;
import com.bobochang.maker.model.DataModel;
import picocli.CommandLine.*;

import java.lang.reflect.Field;

/**
 * @author bobochang
 * @Description
 * @Date 2023/12/21 - 14:17
 */
@Command(name = "config", description = "查看参数信息", mixinStandardHelpOptions = true)
public class ConfigCommand implements Runnable {

    public void run() {
        // 实现 config 命令的逻辑
        System.out.println("-------查看参数信息-------");

//        // 获取要打印属性信息的类
//        Class<?> myClass = DataModel.java.ftl.class;
//        // 获取类的所有字段
//        Field[] fields = myClass.getDeclaredFields();

        Field[] fields = ReflectUtil.getFields(DataModel.class);

        // 遍历并打印每个字段的信息
        for (Field field : fields) {
            System.out.println("\t字段名称：" + field.getName());
            System.out.println("\t字段类型：" + field.getType().getSimpleName());
//            System.out.println("Modifiers: " + java.lang.reflect.Modifier.toString(field.getModifiers()));
            System.out.println("------------------------");
        }
    }
}
