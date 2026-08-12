package com.ru.app.module.converter.service;

import org.jodconverter.core.DocumentConverter;
import org.jodconverter.core.document.DefaultDocumentFormatRegistry;
import org.jodconverter.core.office.OfficeException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class ConverterService {

    private final DocumentConverter documentConverter;
    private final DockerOfficeConverter dockerOfficeConverter;
    @Value("${docker.enabled}")
    private boolean dockerEnabled; // 是否开启

    ConverterService(
            DocumentConverter documentConverter,
            DockerOfficeConverter dockerOfficeConverter
            ) {
       this.documentConverter=documentConverter;
       this.dockerOfficeConverter=dockerOfficeConverter;
    }

    public ResponseEntity<?> convertWordToPdf(MultipartFile correctionReport) {
        // 1. 校验文件是否为空
        if (correctionReport.isEmpty()) {
            return ResponseEntity.badRequest().body("上传文件不能为空".getBytes());
        }
        try {
            // 2. 调用服务层进行转换
            byte[] pdfBytes = convertWordToPdfBytes(correctionReport.getInputStream());

            // 3. 构建响应头，通知浏览器这是一个可下载的 PDF 文件
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "converted_report.pdf");
            headers.setContentLength(pdfBytes.length);

            // 4. 返回 PDF 文件流
            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("文件处理失败".getBytes());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage().getBytes());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("文件转换失败，请稍后重试".getBytes());
        }
    }
    /**
     * 将输入流（Word文件）转换为PDF字节数组。
     *
     * @param wordInputStream Word文件的输入流
     * @return PDF文件的字节数组
     * @throws IOException 如果文件操作失败
     */
    public byte[] convertWordToPdfBytes(InputStream wordInputStream) throws IOException {
        // 1. 创建临时目录用于存放源文件和目标文件
        Path tempDir = Files.createTempDirectory("jodconverter-");

        // 2. 创建临时源文件
        String sourceFileName = UUID.randomUUID() + ".docx"; // 后缀名不影响，JodConverter会自动识别
        Path sourceFilePath = Paths.get(tempDir.toString(), sourceFileName);
        File sourceFile = sourceFilePath.toFile();

        // 3. 将输入流写入临时源文件
        try (FileOutputStream out = new FileOutputStream(sourceFile)) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = wordInputStream.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
        }

        // 4. 创建临时目标文件
        String targetFileName = UUID.randomUUID() + ".pdf";
        Path targetFilePath = Paths.get(tempDir.toString(), targetFileName);
        File targetFile = targetFilePath.toFile();

        try {
            // 5. 执行转换
            if(dockerEnabled){
                dockerOfficeConverter.convertToPdf(
                        sourceFile,targetFile
                );
            }else{
                documentConverter.convert(sourceFile)
                        .to(targetFile)
                        .as(DefaultDocumentFormatRegistry.PDF)
                        .execute();
            }
            // 6. 将PDF文件读入字节数组并返回
            return Files.readAllBytes(targetFilePath);

        } catch (OfficeException | InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            // 7. 在finally块中清理临时文件，确保无论转换成功与否都能删除
            if (sourceFile.exists()) {
                if (sourceFile.delete()) {
                    System.out.println("删除临时源文件: {}"+sourceFile.getAbsolutePath());
                }
            }
            if (targetFile.exists()) {
                if (targetFile.delete()) {
                    System.out.println("删除临时目标文件: {}"+targetFile.getAbsolutePath());
                }
            }
            // 删除临时目录
            Files.deleteIfExists(tempDir);
            System.out.println("删除临时目录: {}"+tempDir);
        }
    }
}
