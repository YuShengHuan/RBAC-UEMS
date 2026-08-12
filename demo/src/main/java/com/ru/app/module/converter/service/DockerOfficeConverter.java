package com.ru.app.module.converter.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Component // 必须加，否则@Value无法注入
public class DockerOfficeConverter {
    private static final Logger log = LoggerFactory.getLogger(DockerOfficeConverter.class);

    // ==================== 用@Value直接注入配置项（带默认值，防止配置缺失） ====================
    @Value("${docker.office.container-name}")
    private String dockerContainerName; // LibreOffice容器名

    @Value("${docker.office.host-docs-dir}")
    private String hostDocsDir; // SpringBoot容器内挂载的文档目录

    @Value("${docker.office.container-docs-dir}")
    private String containerDocsDir; // LibreOffice容器内的文档目录

    @Value("${docker.office.soffice-cmd}")
    private String sofficeCmd; // soffice命令全路径

    @Value("${docker.office.cmd-timeout}")
    private long cmdTimeout; // 命令执行超时时间（秒）

    /**
     * Office文件转PDF
     * @param sourceFile 待转换的源文件（如doc/docx/ppt/xlsx）
     * @param targetPdfFile 转换后的PDF文件（输出路径）
     * @throws IOException  文件操作异常
     * @throws InterruptedException 命令执行中断异常
     * @throws RuntimeException 转换失败异常
     */
    public void convertToPdf(File sourceFile, File targetPdfFile) throws IOException, InterruptedException {
        // 1. 生成唯一文件名（避免冲突）
        String fileExt = getFileExtension(sourceFile.getName());
        String uniqueFileName = UUID.randomUUID() + "." + fileExt;
        String uniquePdfName = uniqueFileName.replace("." + fileExt, ".pdf");

        // 2. 复制源文件到共享目录（用@Value注入的路径）
        File tempSourceFile = new File(hostDocsDir, uniqueFileName);
        Files.copy(sourceFile.toPath(), tempSourceFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

        try {
            // 3. 构建docker exec命令（全部用@Value注入的参数）
            ProcessBuilder processBuilder = new ProcessBuilder(
                    "docker", "exec", dockerContainerName,
                    sofficeCmd,
                    "--headless",
                    "--norestore",
                    "--nofirststartwizard",
                    "--convert-to", "pdf",
                    "--outdir", containerDocsDir,
                    containerDocsDir + "/" + uniqueFileName
            );

            // 配置命令执行环境（继承当前环境）
            processBuilder.environment().putAll(System.getenv());
            // 重定向错误输出到标准输出（方便排查问题）
            processBuilder.redirectErrorStream(true);

            log.info("执行转换命令：{}", String.join(" ", processBuilder.command()));

            // 4. 执行命令并等待完成（用@Value注入的超时时间）
            Process process = processBuilder.start();
            boolean isFinished = process.waitFor(cmdTimeout, TimeUnit.SECONDS);

            // 5. 检查命令执行结果
            int exitCode = process.exitValue();
            if (!isFinished || exitCode != 0) {
                // 读取命令输出（排查错误）
                String output = new String(process.getInputStream().readAllBytes());
                log.error("转换命令执行失败，退出码：{}，输出：{}", exitCode, output);
                throw new RuntimeException("Office转PDF失败：" + output);
            }

            // 6. 复制转换后的PDF到目标路径
            File tempPdfFile = new File(hostDocsDir, uniquePdfName);
            if (!tempPdfFile.exists()) {
                throw new RuntimeException("转换成功但未找到PDF文件：" + tempPdfFile.getAbsolutePath());
            }
            Files.copy(tempPdfFile.toPath(), targetPdfFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            log.info("PDF转换成功，输出路径：{}", targetPdfFile.getAbsolutePath());

        } finally {
            // 7. 清理临时文件（可选，节省空间）
            tempSourceFile = new File(hostDocsDir, uniqueFileName);
            File tempPdfFile = new File(hostDocsDir, uniquePdfName);
            if (tempSourceFile.exists()) {
                tempSourceFile.delete();
            }
            if (tempPdfFile.exists()) {
                tempPdfFile.delete();
            }
        }
    }

    /**
     * 获取文件扩展名（不含.）
     */
    private String getFileExtension(String fileName) {
        int lastDotIndex = fileName.lastIndexOf(".");
        if (lastDotIndex == -1 || lastDotIndex == fileName.length() - 1) {
            throw new RuntimeException("文件无扩展名：" + fileName);
        }
        return fileName.substring(lastDotIndex + 1).toLowerCase();
    }
}