package com.ru.app.module.plagiarism.service;

import com.ru.app.module.exp.mapper.AdminExpReportMapper;
import com.ru.app.module.plagiarism.dto.SimilarReport;
import com.ru.app.common.dto.CommonExpReportDTO;
import com.ru.app.common.service.FileService;
import com.ru.app.module.plagiarism.dto.PlagiarismResult;
import org.springframework.stereotype.Service;
import java.io.File;
import java.util.*;

@Service
public class PlagiarismCheckService {
    private final FileService fileService;
    private final DocumentReaderService documentReaderService;
    private final TextPreprocessService textPreprocessService;
    private final AdminExpReportMapper adminExpReportMapper;
    PlagiarismCheckService( FileService fileService,
                            DocumentReaderService documentReaderService,
                            TextPreprocessService textPreprocessService,
                            AdminExpReportMapper adminExpReportMapper
    ){
       this.fileService=fileService;
       this.documentReaderService=documentReaderService;
       this.textPreprocessService=textPreprocessService;
       this.adminExpReportMapper=adminExpReportMapper;
    }
    /**
     * 查重核心方法：对比目标报告与同班其他报告的相似度
     * @param targetReportId 目标报告ID（要查重的报告）
     * @return 查重结果（含整体查重率、相似报告列表）
     * @throws Exception 异常（文件读取、分词、计算失败等）
     */
    public PlagiarismResult checkPlagiarism(Long targetReportId) throws Exception {
        // 1. 获取目标报告信息
        CommonExpReportDTO targetReport = adminExpReportMapper.findByReportId(targetReportId)
                .orElseThrow(() -> new RuntimeException("目标报告不存在：" + targetReportId));
        Long targetClassId = targetReport.getClassId(); // 目标报告的班级ID（限定查重范围）

        // 2. 获取同班所有其他报告（排除目标报告自身）
        List<CommonExpReportDTO> classReports =adminExpReportMapper.findByClassIdProjectId(targetClassId,targetReport.getProjectId());
        classReports.removeIf(report -> report.getId().equals(targetReportId));
        if (classReports.isEmpty()) {
            return new PlagiarismResult(targetReportId, 0.0, Collections.emptyList());
        }
        System.out.println(targetReport);
        System.out.println(classReports);

        // 3. 读取目标报告内容并预处理
        File targetFile = new File(fileService.urlToRealPath(targetReport.getFilePath()));
        String targetText = documentReaderService.readContent(targetFile);
        List<String> targetWords = textPreprocessService.preprocess(targetText);

        // 4. 批量处理同班其他报告，计算相似度
        List<SimilarReport> similarReports = new ArrayList<>();
        double maxSimilarity = 0.0; // 记录最高相似度（作为整体查重率）

        for (CommonExpReportDTO report : classReports) {
            try {
                // 读取对比报告内容并预处理
                File compareFile = new File(fileService.urlToRealPath(report.getFilePath()));
                String compareText = documentReaderService.readContent(compareFile);
                List<String> compareWords = textPreprocessService.preprocess(compareText);

                // 计算相似度（TF-IDF + 余弦相似度）
                double similarity = calculateSimilarity(targetWords, compareWords);
                if (similarity > 0.01) { // 过滤极低相似度（避免误判）
                    similarReports.add(new SimilarReport(
                            report.getId(),
                            report.getUploadUserAccount(),
                            report.getUploadRealName(),
                            similarity
                    ));
                    // 更新最高相似度（整体查重率取与其他报告的最高相似度）
                    maxSimilarity = Math.max(maxSimilarity, similarity);
                }
            } catch (Exception e) {
                System.out.println("处理报告 [{}] 时出错："+report.getFilePath()+"=="+report.getId()+ e.getMessage());
                // 单个报告处理失败不影响整体查重，跳过即可
            }
        }

        // 5. 排序：按相似度降序
        similarReports.sort((a, b) -> Double.compare(b.getSimilarity(), a.getSimilarity()));

        // 6. 返回结果（整体查重率保留2位小数）
        return new PlagiarismResult(
                targetReportId,
                Math.round(maxSimilarity * 100.0) / 100.0,
                similarReports
        );
    }

    /**
     * 计算两个文本的相似度：TF-IDF + 余弦相似度
     * @param words1 文本1的预处理词列表
     * @param words2 文本2的预处理词列表
     * @return 相似度（0~1）
     */
    private double calculateSimilarity(List<String> words1, List<String> words2) {
        if (words1.isEmpty() || words2.isEmpty()) {
            return 0.0;
        }

        // 1. 构建词袋（所有不重复的词）
        Set<String> allWords = new HashSet<>(words1);
        allWords.addAll(words2);

        // 2. 计算TF（词频）
        Map<String, Integer> tf1 = calculateTF(words1);
        Map<String, Integer> tf2 = calculateTF(words2);

        // 3. 计算IDF（逆文档频率）：这里简化为“语料库”=两个文本的词袋
        Map<String, Double> idf = calculateIDF(allWords, words1, words2);

        // 4. 计算TF-IDF向量
        Map<String, Double> tfIdf1 = calculateTFIDF(tf1, idf);
        Map<String, Double> tfIdf2 = calculateTFIDF(tf2, idf);

        // 5. 计算余弦相似度
        return calculateCosineSimilarity(tfIdf1, tfIdf2, allWords);
    }

    /**
     * 计算词频（TF）
     */
    private Map<String, Integer> calculateTF(List<String> words) {
        Map<String, Integer> tf = new HashMap<>();
        for (String word : words) {
            tf.put(word, tf.getOrDefault(word, 0) + 1);
        }
        return tf;
    }

    /**
     * 计算逆文档频率（IDF）
     * @param allWords 词袋
     * @param words1 文本1词列表
     * @param words2 文本2词列表
     * @return IDF映射
     */
    private Map<String, Double> calculateIDF(Set<String> allWords, List<String> words1, List<String> words2) {
        Map<String, Double> idf = new HashMap<>();
        int totalDocs = 2; // 语料库大小=2（目标文本+对比文本）

        for (String word : allWords) {
            // 统计包含该词的文档数
            int docCount = 0;
            if (words1.contains(word)) docCount++;
            if (words2.contains(word)) docCount++;

            // IDF公式：log(总文档数 / (包含该词的文档数 + 1)) + 1（避免分母为0）
            double idfValue = Math.log(totalDocs / (double) (docCount + 1)) + 1;
            idf.put(word, idfValue);
        }
        return idf;
    }

    /**
     * 计算TF-IDF
     */
    private Map<String, Double> calculateTFIDF(Map<String, Integer> tf, Map<String, Double> idf) {
        Map<String, Double> tfIdf = new HashMap<>();
        for (Map.Entry<String, Integer> entry : tf.entrySet()) {
            String word = entry.getKey();
            int tfValue = entry.getValue();
            double idfValue = idf.getOrDefault(word, 0.0);
            tfIdf.put(word, tfValue * idfValue);
        }
        return tfIdf;
    }

    /**
     * 计算余弦相似度
     * @param tfIdf1 文本1的TF-IDF向量
     * @param tfIdf2 文本2的TF-IDF向量
     * @param allWords 词袋（确保向量维度一致）
     * @return 相似度（0~1）
     */
    private double calculateCosineSimilarity(Map<String, Double> tfIdf1, Map<String, Double> tfIdf2, Set<String> allWords) {
        double dotProduct = 0.0; // 点积
        double norm1 = 0.0; // 向量1的模长
        double norm2 = 0.0; // 向量2的模长

        for (String word : allWords) {
            double v1 = tfIdf1.getOrDefault(word, 0.0);
            double v2 = tfIdf2.getOrDefault(word, 0.0);

            dotProduct += v1 * v2;
            norm1 += v1 * v1;
            norm2 += v2 * v2;
        }

        // 避免除以0
        if (norm1 == 0.0 || norm2 == 0.0) {
            return 0.0;
        }

        // 余弦相似度公式：dotProduct / (norm1 * norm2)
        return dotProduct / (Math.sqrt(norm1) * Math.sqrt(norm2));
    }
}