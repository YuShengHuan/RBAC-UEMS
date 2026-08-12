package com.ru.app.module.plagiarism.service;

import org.springframework.stereotype.Service;
import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;

import java.io.StringReader;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TextPreprocessService {
    // 中文停用词库（可自定义，比如实验报告常见无意义词）
    private static final Set<String> STOP_WORDS = new HashSet<>();

    // 初始化停用词库
    static {
        STOP_WORDS.add("的");
        STOP_WORDS.add("了");
        STOP_WORDS.add("是");
        STOP_WORDS.add("在");
        STOP_WORDS.add("和");
        STOP_WORDS.add("实验");
        STOP_WORDS.add("分析");
        STOP_WORDS.add("数据");
        STOP_WORDS.add("结果");
        STOP_WORDS.add("讨论");
        STOP_WORDS.add("结论");
    }

    /**
     * 文本预处理：统一格式 + 分词 + 过滤停用词
     * @param text 原始文本
     * @return 预处理后的词列表
     * @throws Exception 分词异常
     */
    public List<String> preprocess(String text) throws Exception {
        if (text == null || text.trim().isEmpty()) {
            return Collections.emptyList();
        }

        // 1. 统一格式：转小写、去除标点、空格、换行符
        String normalizedText = text.toLowerCase()
                .replaceAll("[\\p{Punct}\\s\\n\\r]", " ") // 去除标点、空格、换行
                .replaceAll("\\s+", " "); // 多个空格合并为一个

        // 2. 中文分词（用 IKAnalyzer）
        List<String> words =IKAnalyzerText(normalizedText);

        // 3. 过滤停用词和长度小于2的词

        return words.stream()
                .filter(word -> word.length() >= 2)
                .filter(word -> !STOP_WORDS.contains(word))
                .collect(Collectors.toList());
    }

    /**
     * 中文分词（IKAnalyzer）
     */
    private List<String> IKAnalyzerText(String text) throws Exception {
        List<String> words = new ArrayList<>();
        // 关闭智能分词（精准分词，适合短文本）
        IKSegmenter segmenter = new IKSegmenter(new StringReader(text), false);
        Lexeme lexeme;
        while ((lexeme = segmenter.next()) != null) {
            words.add(lexeme.getLexemeText());
        }
        return words;
    }
}
