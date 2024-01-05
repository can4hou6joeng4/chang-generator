package com.bobochang.maker.cli.command;

import cn.hutool.core.bean.BeanUtil;
import com.bobochang.maker.model.DataModel;
import lombok.Data;
import picocli.CommandLine.*;
import com.bobochang.maker.generator.file.FileGenerator;

import java.util.concurrent.Callable;

/**
 * @author bobochang
 * @Description
 * @Date 2023/12/20 - 17:19
 */
@Command(name = "generator", description = "生成代码", mixinStandardHelpOptions = true)
@Data
public class GenerateCommand implements Callable<Integer> {


    @Option(names = {"-l", "--loop"}, arity = "0..1", description = "是否循环", prompt = "是否需要循环，请输入 true or false：", interactive = true, echo = true)
    private boolean loop;

    @Option(names = {"-a", "--author"}, arity = "0..1", description = "作者", prompt = "请输入作者昵称：", interactive = true, echo = true)
    private String author = "bobochang";

    @Option(names = {"-o", "--outputText"}, arity = "0..1", description = "输出文本", prompt = "请输入输出文本：", interactive = true, echo = true)
    private String outputText = "example output txt =  ";

    public Integer call() throws Exception {
        DataModel mainTemplateConfig = new DataModel();
        BeanUtil.copyProperties(this, mainTemplateConfig);
        System.out.println("配置信息：" + mainTemplateConfig);
        FileGenerator.doGenerate(mainTemplateConfig);
        return 0;
    }
}
