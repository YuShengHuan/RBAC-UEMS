package com.ru.app.common.entity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data  // Lombok 注解，自动生成 getter/setter
//教授课程的核心辅助表
@TableName("exp_teaching_core")
public class ExpTeachingCore {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String semester;
    private Long courseId;
    private Long classId;
    private Long userId;
    private LocalDateTime createAt;// '创建时间
    private LocalDateTime updateAt;// '更新时间'
}
