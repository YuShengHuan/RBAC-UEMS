package com.ru.app.common.entity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data  // Lombok 注解，自动生成 getter/setter
@TableName("exp_course")
public class ExpCourse {
    @TableId(type = IdType.AUTO)
    private Long id;// '课程ID（UUID）',
    private String courseCode;// '课程编码',
    private String courseName;// '课程名称',
    private Integer courseType;//课程类型(1-必修,2-选修)
    private Long deptId;// '所属学院id',
    private LocalDateTime createAt;// '创建时间
    private LocalDateTime updateAt;// '更新时间'
}
