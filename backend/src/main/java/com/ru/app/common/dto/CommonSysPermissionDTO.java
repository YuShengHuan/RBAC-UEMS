package com.ru.app.common.dto;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommonSysPermissionDTO {
    private Long id;//  '权限ID',
    private String parentCode;//  '父权限编码',
    private String permGroup;//父权限ID
    private String permCode;// '权限编码
    private String permName;//  '权限名称'
    private Integer permType; //'权限类型(1:视图,2:操作)'
    private String requestUri;//请求的路径
    private Integer permStatus;//状态
    private LocalDateTime createAt;// '创建时间
    private LocalDateTime updateAt;// '更新时间'
    private String remark;// '备注'
}
