package com.ru.app.module.plagiarism.service;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;

@Service
public class DocumentReaderService {
    public String readContent(File file) throws Exception {
        String fileName = file.getName();
        if (fileName.endsWith(".docx")) {
            return readDocx(file);
        } else if (fileName.endsWith(".doc")) {
            return readDoc(file);
        } else {
            throw new IllegalArgumentException("Unsupported file format: " + fileName);
        }
    }
    private String readDocx(File file) throws Exception {
        try (XWPFDocument doc = new XWPFDocument(new FileInputStream(file))) {
            XWPFWordExtractor extractor = new XWPFWordExtractor(doc);
            return extractor.getText().trim();
        }
    }
    private String readDoc(File file) throws Exception {
        try (HWPFDocument doc = new HWPFDocument(new FileInputStream(file))) {
            WordExtractor extractor = new WordExtractor(doc);
            return extractor.getText().trim();
        }
    }
}
