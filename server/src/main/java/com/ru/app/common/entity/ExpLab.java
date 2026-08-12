package com.ru.app.common.entity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data  // Lombok 注解，自动生成 getter/setter
@TableName("exp_lab")
public class ExpLab {
    @TableId(type = IdType.AUTO)
    private Long id;// '实验室ID',
    private String labName;// '实验室名称',
    private String labCode;// '实验室编码',
    private String labLocation;// '实验室地点',
    private Long deptId;//分院ID
    private LocalDateTime createAt;// '创建时间
    private LocalDateTime updateAt;// '更新时间'
}
