package com.bobochang.maker.generator;

import java.io.*;
import java.util.Map;

/**
 * @author bobochang
 * @Description
 * @Date 2024/1/4 - 15:54
 */
public class JarGenerator {

    public static void doGenerate(String projectDir) throws IOException, InterruptedException {
        // 清理之前的构建并打包
        // 注意不同操作系统，执行的命令不同
        String windowsMavenCmd = "mvn.cmd clean package -DskipTests=true";
        String otherOSMavenCmd = "mvn clean package -DskipTests=true";
        String mavenCommand = otherOSMavenCmd;
        // 拆分 Maven 命令
        ProcessBuilder processBuilder = new ProcessBuilder(mavenCommand.split(" "));
        processBuilder.directory(new File(projectDir));
        Map<String, String> environment = processBuilder.environment();
        System.out.println(environment);
        Process process = processBuilder.start();
        // 读取命令
        InputStream inputStream = process.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
        }
        // 等待命令执行完成
        int exitCode = process.waitFor();
        System.out.println("命令执行结束，退出码：" + exitCode);
    }
}
