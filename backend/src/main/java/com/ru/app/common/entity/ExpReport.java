package com.ru.app.common.entity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data  // Lombok 注解，自动生成 getter/setter
@TableName("exp_report")
public class ExpReport {
    @TableId(type = IdType.AUTO)
    private Long id;// '报告ID',
    private Long projectId;// '实验项目ID',
    private Long uploadUserId;// '上传用户ID',
    private String filePath;// '文件路径',
    private Integer attachmentType;// （1-实验模板/2-实验报告）
    private String remark;// '备注',
    private LocalDateTime createAt;// '创建时间
    private LocalDateTime updateAt;// '更新时间'
}
