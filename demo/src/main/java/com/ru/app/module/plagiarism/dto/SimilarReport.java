package com.ru.app.module.plagiarism.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 单个相似报告的信息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SimilarReport {

    /**
     * 相似报告的ID
     */
    private Long reportId;


    private String uploadUserAccount;
    private String uploadRealName;

    /**
     * 与目标报告的相似度 (值范围：0.0 ~ 1.0)
     */
    private double similarity;
}