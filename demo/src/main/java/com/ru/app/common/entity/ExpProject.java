package com.ru.app.common.entity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 实验业务-实验项目表
 */
@Data
@TableName("exp_project") // 对应数据库表名
public class ExpProject {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long teachingCoreId;
    private String projectCode;
    private String projectName;
    private Integer projectWeekStart;
    private Integer projectWeekEnd;
    private Integer weeklyHours;
    private Integer planHours;
    private Integer actualHours;
    private Integer expCategory;
    private Integer expType;
    private Integer subject;
    private Integer groupNum;
    private Integer expPersonType;
    private Integer expRequirement;
    private String remark;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
}