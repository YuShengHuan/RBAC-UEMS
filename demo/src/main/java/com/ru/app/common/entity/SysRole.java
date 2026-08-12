package com.ru.app.common.entity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data  // Lombok 注解，自动生成 getter/setter
@TableName("sys_role")
public class SysRole {
    @TableId(type = IdType.AUTO)
    private Long id;// '角色ID',
    private String roleGroup; //角色分组
    private String roleCode;// '角色编码',
    private String roleName;// '角色名称',
    private LocalDateTime createAt;// '创建时间
    private LocalDateTime updateAt;// '更新时间'
    private String remark;// '备注'
}
