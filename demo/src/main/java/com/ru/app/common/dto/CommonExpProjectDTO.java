package com.ru.app.common.dto;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.ru.app.common.entity.ExpProject;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.time.LocalDateTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Data
public class CommonExpProjectDTO {
    // 基础字段（来自 ExpProject）
    @TableId(type = IdType.AUTO)
    private Long id; // 项目ID
    private Long teachingCoreId;
    private String projectCode; // 实验编码
    private String projectName; // 实验名称
    private Integer projectWeekStart; // 授课周次开始
    private Integer projectWeekEnd; // 授课周次结束
    private Integer weeklyHours; // 周次学时数
    private Integer planHours; // 计划学时数
    private Integer actualHours; // 实际学时数
    private Integer expCategory; // 实验类别（1-基础/2-专业基础/3-专业/4-其他）
    private Integer expType; // 实验类型（1-演示性/2-验证性/3-综合性/4-设计研究）
    private Integer subject; // 实验所属学科（1-计算机类/2-电子信息类/3-电子商务类/4-财政学类/5-其他）
    private Integer groupNum; // 实验分组人数
    private Integer expPersonType; // 实验者类别（1-本科生/2-专科生）
    private Integer expRequirement; // 实验要求（1-选修/2-必修/3-其他）
    private String remark; // 备注
    private LocalDateTime createAt; // 创建时间
    private LocalDateTime updateAt; // 更新时间

    // 关联字段（新增，用于前端展示）
    private String semester; // 学期（如：2023-2024-1）
    private String courseName; // 课程名字
    private String userAccount;//工号
    private String realName; // 教师名字
    private String className; // 班级名字
    private String projectWeekRange; // 授课周次(5周/5-6周)

    // 转换函数：自动将 projectWeekStart/End 合并为 projectWeekRange
    public CommonExpProjectDTO convertDTO() {
        this.convertProjectWeekRange();
        return this;
    }
    public void convertProjectWeekRange(){
        if (projectWeekStart != null && projectWeekEnd != null) {
            this.projectWeekRange = projectWeekStart + "-" + projectWeekEnd + "周";
        } else if (projectWeekStart != null) {
            this.projectWeekRange = projectWeekStart + "周";
        } else {
            this.projectWeekRange = "";
        }
    }
    public ExpProject convertEntity() {
        ExpProject entity=new ExpProject();

        Pattern pattern1 = Pattern.compile("^(\\d+)(-(\\d+))?周$");
        Matcher matcher1=pattern1.matcher(this.projectWeekRange);
        if (matcher1.matches()) {
            // 提取开始周（group(1) 直接是纯数字）
            this.projectWeekStart = Integer.parseInt(matcher1.group(1));

            if (matcher1.group(3) != null) {
                this.projectWeekEnd = Integer.parseInt(matcher1.group(3));
            }
        }
        BeanUtils.copyProperties(this,entity);
        return entity;
    }

}


