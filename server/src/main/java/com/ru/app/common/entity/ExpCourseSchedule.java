package com.ru.app.common.entity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("exp_course_schedule") // 对应数据库表名
public class ExpCourseSchedule {
    @TableId(type = IdType.AUTO) // 自增主键，与数据库一致
    private Long id;
    private Long teachingCoreId;
    private Long labId;
    private Integer weekStart;
    private Integer weekEnd;
    private Integer weekType;
    private String weekCustom;
    private Integer weekDay;
    private Integer periodStart;
    private Integer periodEnd;
    private Integer classHours;
    private Integer isReport;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
}