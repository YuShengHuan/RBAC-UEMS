package com.ru.app.common.dto;
import com.ru.app.common.entity.ExpCourseSchedule;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Data
public class CommonExpCourseScheduleDTO {

    private Long id; // 课程安排ID
    private Long teachingCoreId;
    private Long labId; // 实验室ID
    private Integer weekStart; // 周次开始
    private Integer weekEnd; // 周次结束
    private Integer weekType; // 周次类型（1-连续周，2-单周，3-双周，4-自定义）
    private String weekCustom; // 自定义周次（如：1,3,5）
    private Integer weekDay; // 星期（1-周一，2-周二...7-周日）
    private Integer periodStart; // 节次开始
    private Integer periodEnd; // 节次结束

    private Integer isReport; // 是否有报告（0-有，1-无）
    private String semester; // 学期（如：2023-2024-1）
    private LocalDateTime createAt; // 创建时间
    private LocalDateTime updateAt; // 更新时间

    // 关联字段（新增，用于前端展示）
    private String courseName; // 课程名字
    private String className; // 班级名字
    private String userAccount;//工号
    private String realName; // （教师姓名）
    private String labLocation; // 实验地点
    private String weekRange; // 周次范围(2-17周)
    private String classPeriod; // 节次(1-2节)
    private Integer classHours; // 总学时

    // 转换函数1：将周次相关字段转换为友好的 weekRange
    public void convertWeekRange() {
        if (weekType == null) {
            this.weekRange = "";
            return;
        }
        switch (weekType) {
            case 1: // 连续周
                if (weekStart != null && weekEnd != null) {
                    this.weekRange = weekStart + "-" + weekEnd + "周";
                }
                break;
            case 2: // 单周
                if (weekStart != null && weekEnd != null) {
                    this.weekRange = weekStart + "-" + weekEnd + "周(单)";
                }
                break;
            case 3: // 双周
                if (weekStart != null && weekEnd != null) {
                    this.weekRange = weekStart + "-" + weekEnd + "周(双)";
                }
                break;
            case 4: // 自定义周
                if (weekCustom != null && !weekCustom.isEmpty()) {
                    this.weekRange = weekCustom;
                }
                break;
            default:
                this.weekRange = "";
        }
    }

    // 转换函数2：将节次相关字段转换为友好的 classPeriod
    public void convertClassPeriod() {
        if (periodStart != null && periodEnd != null) {
            this.classPeriod = periodStart + "-" + periodEnd + "节";
        }else {
            this.classPeriod = "";
        }
    }

    // 合并转换函数：一次性调用所有转换
    public CommonExpCourseScheduleDTO convertDTO() {
        convertWeekRange();
        convertClassPeriod();
        return this;
    }
    public ExpCourseSchedule convertEntity() {
        ExpCourseSchedule entity=new ExpCourseSchedule();

        Pattern pattern1 = Pattern.compile("^(\\d+)-(\\d+)周$");
        Pattern pattern2 = Pattern.compile("^(\\d+)-(\\d+)周\\(单\\)$");
        Pattern pattern3 = Pattern.compile("^(\\d+)-(\\d+)周\\(双\\)$");
        Pattern pattern4 = Pattern.compile("^(\\d+)-(\\d+)节$");

        Matcher matcher1=pattern1.matcher(this.weekRange);
        Matcher matcher2=pattern2.matcher(this.weekRange);
        Matcher matcher3=pattern3.matcher(this.weekRange);
        Matcher matcher4=pattern4.matcher(this.classPeriod);
        if(matcher1.matches()){
            this.weekType=1;
            this.weekStart=Integer.parseInt(matcher1.group(1));
            this.weekEnd=Integer.parseInt(matcher1.group(2));
        }
        else if(matcher2.matches()){
            this.weekType=2;
            this.weekStart=Integer.parseInt(matcher2.group(1));
            this.weekEnd=Integer.parseInt(matcher2.group(2));
        }
        else if(matcher3.matches()){
            this.weekType=3;
            this.weekStart=Integer.parseInt(matcher3.group(1));
            this.weekEnd=Integer.parseInt(matcher3.group(2));
        }
        else{
            this.weekType=4;
            this.weekCustom=this.weekRange;
        }

        if(matcher4.matches()){
            this.periodStart=Integer.parseInt(matcher4.group(1));
            this.periodEnd=Integer.parseInt(matcher4.group(2));
        }
        BeanUtils.copyProperties(this,entity);
        return entity;
    }
}

