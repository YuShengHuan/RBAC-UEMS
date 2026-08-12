package com.ru.app.common.service;

import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

@Service
public class PandocService {

    private static String saveFilePath="C:/Users/梦/Desktop";

    public static String convertFile(File inputFile, String fromFormat, String toFormat)
            throws IOException, InterruptedException {

        // 生成唯一输出文件名
        String pandocPath = "D:/pandoc-3.6.4/pandoc.exe"; // Windows
        // String pandocPath = "/opt/pandoc/bin/pandoc"; // Linux/macOS

        // 2. 验证 Pandoc 可执行文件是否存在
        Path pandocExecutable = Paths.get(pandocPath);
        if (!Files.exists(pandocExecutable)) {
            throw new IOException("Pandoc 可执行文件未找到: " + pandocPath);
        }

        // 3. 生成输出文件路径
        String outputFileName = "download_" + System.currentTimeMillis() + "." + toFormat;
        Path outputPath = Paths.get(saveFilePath, outputFileName);

        // 4. 构建命令列表
        List<String> command = Arrays.asList(
                pandocPath,  // 使用绝对路径
                "-f", fromFormat,
                "-t", toFormat,
                "-o", outputPath.toString(),
                inputFile.getAbsolutePath()
        );

        // 执行命令
        Process process = new ProcessBuilder(command).start();
        int exitCode = process.waitFor();

        if (exitCode != 0) {
            throw new IOException("Pandoc 转换失败，错误码: " + exitCode);
        }

        return outputFileName;
    }
}
