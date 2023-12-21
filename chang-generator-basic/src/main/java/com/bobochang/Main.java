package com.bobochang;

import com.bobochang.cli.CommandExecutor;

/**
 * @author bobochang
 * @Description test file
 * @Date 2023/12/8 - 15:48
 */
public class Main {
    public static void main(String[] args) {
//        args = new String[]{"--help"};
//        args = new String[]{"generator", "-l", "-a", "-o"};
//        args = new String[]{"config"};
//        args = new String[]{"list"};
        CommandExecutor commandExecutor = new CommandExecutor();
        commandExecutor.doExecute(args);
    }
}