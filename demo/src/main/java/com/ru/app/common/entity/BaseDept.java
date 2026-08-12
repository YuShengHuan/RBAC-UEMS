package com.ru.app.common.entity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data  // Lombok 注解，自动生成 getter/setter
@TableName("base_dept")
public class BaseDept {
    @TableId(type = IdType.AUTO)
    private Long id;//  '学院ID',
    private String deptCode;// '学院编码',
    private String deptName;// '学院名称',
    private LocalDateTime createAt;// '创建时间
    private LocalDateTime updateAt;// '更新时间'
}
