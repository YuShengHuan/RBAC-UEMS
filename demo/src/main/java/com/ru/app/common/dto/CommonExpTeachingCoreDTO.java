package com.ru.app.common.dto;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.time.LocalDateTime;


@Data
public class CommonExpTeachingCoreDTO {
    // 基础字段（来自 ExpProject）
    @TableId(type = IdType.AUTO)
    private Long id; // 课程安排ID
    private String semester; // 学期（如：2023-2024-1）
    private Long courseId; // 课程ID
    private Long classId; // 班级ID
    private String userAccount;//工号
    private Long userId; // 授课用户ID
    // 关联字段（新增，用于前端展示）
    private String courseName; // 课程名字
    private String className; // 班级名字
    private String realName; // 用户真实名（教师姓名）
    private LocalDateTime createAt; // 创建时间
    private LocalDateTime updateAt; // 更新时间
}


