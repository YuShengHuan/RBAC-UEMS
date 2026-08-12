package com.ru.app.common.dto;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommonExpReportReviewDTO {
    private Long id;// '批改ID',
    private String semester;// '学期(如:2023-2024-1)',
    private String courseName;//课程名字
    private String projectName;// '实验项目ID',
    private String className;//班级名字

    private String uploadUserAccount;// '上传用户账户',
    private String uploadRealName;// '上传用户的真实名',
    private String uploadUserType;// '上传用户类型',
    private Long reportId;// '实验报告ID',
    private Long reviewUserId;// '批阅用户ID',
    private String reviewUserAccount;// '批阅用户账户',
    private String reviewRealName;// '批改用户的真实名',
    private String reviewUserType;// '批阅用户类型',
    private Float score;// '成绩',
    private String reviewComment;// '批阅意见',
    private Float plagiarismRate;// '查重率',
    private LocalDateTime createAt;// '创建时间
    private LocalDateTime updateAt;// '更新时间'
}
