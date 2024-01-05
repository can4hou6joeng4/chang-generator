package com.bobochang.maker.cli.command;

import cn.hutool.core.io.FileUtil;
import picocli.CommandLine.*;

import java.io.File;
import java.util.List;

/**
 * @author bobochang
 * @Description
 * @Date 2023/12/21 - 14:15
 */
@Command(name = "list", description = "查看文件列表", mixinStandardHelpOptions = true)
public class ListCommand implements Runnable {

    public void run() {
        String projectPath = System.getProperty("user.dir");
        // 整个项目的根路径
        File parentFile = new File(projectPath);
        // 输入路径
        String inputPath = new File(parentFile, "chang-generator-demo-projects/acm-template").getAbsolutePath();
        List<File> files = FileUtil.loopFiles(inputPath);
        for (File file : files) {
            System.out.println(file);
        }
    }

}
