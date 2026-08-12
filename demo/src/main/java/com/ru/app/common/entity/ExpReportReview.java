package com.ru.app.common.entity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data  // Lombok 注解，自动生成 getter/setter
@TableName("exp_report_review")
public class ExpReportReview {
    @TableId(type = IdType.AUTO)
    private Long id;// '批改ID',
    private Long reportId;// '实验报告ID',
    private Long reviewUserId;// '批阅用户ID',
    private Float score;// '成绩',
    private String reviewComment;// '批阅意见',
    private Float plagiarismRate;// '查重率',
    private LocalDateTime createAt;// '创建时间
    private LocalDateTime updateAt;// '更新时间'
}
