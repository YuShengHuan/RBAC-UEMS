package com.ru.app.common.entity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data  // Lombok 注解，自动生成 getter/setter
@TableName("base_major")
public class BaseMajor {
    @TableId(type = IdType.AUTO)
    private Long id;// '专业ID',
    private String majorCode;// '专业编码',
    private String majorName;// '专业名称',
    private Long deptId;// '所属学院ID',
    private LocalDateTime createAt;// '创建时间
    private LocalDateTime updateAt;// '更新时间'
}
