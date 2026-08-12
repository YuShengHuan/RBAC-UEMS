package com.ru.app.module.plagiarism.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

/**
 * 查重结果的总封装类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlagiarismResult {

    /**
     * 目标报告的ID
     */
    private Long targetReportId;

    /**
     * 整体查重率 (与班级内其他报告的最高相似度)
     */
    private double plagiarismRate;

    /**
     * 相似报告的列表
     */
    private List<SimilarReport> similarReports;

}