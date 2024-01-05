package com.bobochang;

import com.bobochang.maker.cli.CommandExecutor;

/**
 * @author bobochang
 * @Description test file
 * @Date 2023/12/8 - 15:48
 */
public class Main {
    public static void main(String[] args) {
        CommandExecutor commandExecutor = new CommandExecutor();
        commandExecutor.doExecute(args);
    }
}