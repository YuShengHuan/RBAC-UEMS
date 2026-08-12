package com.ru.app.common.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.*;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
public class FileService {
    @Value("${file.upload.path}")
    private String UploadPath;

    @Value("${file.upload.access-path}")
    private String AccessPath;
    private static final List<String> acceptFileExtension=List.of(
            ".docx",".doc"
    );
    public String save(MultipartFile file,String fileName) throws Exception {
        // 1. 创建存储目录
        Path uploadPath = Paths.get(UploadPath);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
        // 2. 生成唯一文件名
        String originalFilename = file.getOriginalFilename();
        String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
        if(!acceptFileExtension.contains(fileExtension)){
            throw new Exception("不接收此类文件");
        }
        String newFilename = UUID.randomUUID().toString().replace("-", "") + fileExtension;
        if(fileName!=null){
            newFilename = UUID.randomUUID().toString().replace("-", "")+"@"+fileName + fileExtension;
        }
        // 3. 保存文件
        Path filePath = uploadPath.resolve(newFilename);
        Files.copy(file.getInputStream(), filePath);

        String encodedUrl= ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(AccessPath.replace("**",""))
                .path(UriComponentsBuilder.fromPath(newFilename).encode(StandardCharsets.UTF_8).build().toString())
                .build()
                .toUriString();
        return URLDecoder.decode(encodedUrl, StandardCharsets.UTF_8);
    }
    public String saveFile(MultipartFile file) throws Exception {
        return save(file,null);
    }
    public String saveFile(MultipartFile file,String fileName) throws Exception {
       return save(file,fileName);
    }
    public boolean deleteFile(String url){
        try{
            String fileRealPath=urlToRealPath(url);
            Path uploadFilePath = Paths.get(fileRealPath);
            Files.deleteIfExists(uploadFilePath);
            return true;
        } catch (Exception e){
            return false;
        }
    }
    public String urlToRealPath(String url){
        try {
            return UploadPath+url.substring(url.lastIndexOf("/")+1);
        }catch (Exception e){
            return null;
        }
    }
    public void compressFilesToZip(List<File> files, File zipFile) {
        try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipFile))) {
            byte[] buffer = new byte[1024];
            for (File file : files) {
                try (FileInputStream fis = new FileInputStream(file)) {
                    zos.putNextEntry(new ZipEntry(file.getName()));
                    int len;
                    while ((len = fis.read(buffer)) > 0) {
                        zos.write(buffer, 0, len);
                    }
                    zos.closeEntry();
                }
            }
            System.out.println("多文件压缩完成！");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
