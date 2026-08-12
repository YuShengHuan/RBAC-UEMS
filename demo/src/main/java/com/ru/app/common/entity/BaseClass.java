package com.ru.app.common.entity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data  // Lombok 注解，自动生成 getter/setter
@TableName("base_class")
public class BaseClass {
    @TableId(type = IdType.AUTO)
    private Long id;// '班级ID',
    private String classCode;// '班级编码',
    private String className;// '班级名称',
    private Long majorId;// '专业ID',
    private Integer grade; //'年级(如:2023级)',
    private Integer studentCount;// '班级人数',
    private LocalDateTime createAt;// '创建时间
    private LocalDateTime updateAt;// '更新时间'
}
