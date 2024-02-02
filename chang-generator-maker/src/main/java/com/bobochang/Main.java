package com.bobochang;

import com.bobochang.maker.generator.main.GenerateTemplate;
import com.bobochang.maker.generator.main.MainGenerator;
import com.bobochang.maker.generator.main.ZipGenerator;
import freemarker.template.TemplateException;

import java.io.IOException;

/**
 * @author bobochang
 * @Description test file
 * @Date 2023/12/8 - 15:48
 */
public class Main {
    public static void main(String[] args) throws TemplateException, IOException, InterruptedException {
        // GenerateTemplate mainGenerator = new MainGenerator();
        GenerateTemplate zipGenerator = new ZipGenerator();
        zipGenerator.doGenerate();
    }
}