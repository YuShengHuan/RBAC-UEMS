package com.ru.app.common.entity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data  // Lombok 注解，自动生成 getter/setter
@TableName("sys_permission")
public class SysPermission {
    @TableId(type = IdType.AUTO)
    private Long id;//  '权限ID',
    private String parentCode;//  '父权限编码',
    private String permGroup;//  '权限分组'
    private String permName;//  '权限名称'
    private String permCode;// '权限编码(如:experiment:add)'
    private Integer permType; //'权限类型(1:视图,2:操作)'
    private String requestUri;//请求的路径
    private Integer permStatus;//状态
    private LocalDateTime createAt;// '创建时间
    private LocalDateTime updateAt;// '更新时间'
    private String remark;// '备注'
}
