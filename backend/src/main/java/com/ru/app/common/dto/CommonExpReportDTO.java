package com.ru.app.common.dto;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommonExpReportDTO {
    private Long id;// '报告ID',
    private String semester;// '学期(如:2023-2024-1)',
    private String courseName;//课程名字
    private Long projectId;// '实验项目ID',
    private Long classId;
    private String projectName;// '实验项目ID',
    private String className;//班级名字
    private Long uploadUserId;// '上传用户ID',
    private String uploadUserAccount;// '上传用户账户',
    private String uploadRealName;// '上传用户的真实名',
    private Integer uploadUserType;//上传用户类型
    private String filePath;// '文件路径',
    private Integer attachmentType;// （1-实验模板/2-实验报告）
    private String remark;// '备注',
    private LocalDateTime createAt;// '创建时间
    private LocalDateTime updateAt;// '更新时间'
}
