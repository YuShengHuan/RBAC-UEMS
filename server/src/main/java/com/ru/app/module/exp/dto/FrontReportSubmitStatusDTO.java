package com.ru.app.module.exp.dto;

import lombok.Data;

@Data
public class FrontReportSubmitStatusDTO{
    private Long projectId;//项目id
    private Long reportId;//报告id
    // 课程名称
    private String courseName;
    // 实验项目名称
    private String projectName;
    // 学生班级
    private String className;
    private String reviewUserId;
    // 教师姓名
    private String reviewRealName;
    // 教师账号
    private String reviewUserAccount;
    private String uploadUserId;
    // 学生姓名
    private String uploadRealName;
    // 学生账号
    private String uploadUserAccount;

    private String filePath;
}