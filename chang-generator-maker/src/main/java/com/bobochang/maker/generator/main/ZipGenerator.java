package com.bobochang.maker.generator.main;

/**
 * @author bobochang
 * @Description 生成代码生成器压缩包
 * @Date 2024/1/4 - 09:59
 */
public class ZipGenerator extends GenerateTemplate {
    @Override
    protected String buildDist(String outputPath, String jarPath, String shellOutputPath, String sourceCopyDestPath) {
        String distPath = super.buildDist(outputPath, jarPath, shellOutputPath, sourceCopyDestPath);
        return super.buildZip(distPath);
    }
}
