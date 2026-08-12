package com.ru.app.module.authority.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ru.app.common.dto.CommonSysUserRoleDTO;
import com.ru.app.common.entity.SysRolePermission;
import com.ru.app.module.authority.dto.AdminSysUserRoleQueryDTO;
import com.ru.app.module.authority.mapper.AdminSysUserRoleMapper;
import com.ru.app.common.entity.SysUserRole;
import com.ru.app.common.service.BaseDatabaseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class AdminSysUserRoleService extends BaseDatabaseService<AdminSysUserRoleMapper, SysUserRole> {
    public ResponseEntity<?> queryPage(AdminSysUserRoleQueryDTO queryDTO) {
        try {
            // 构建分页参数
            Page<CommonSysUserRoleDTO> page = new Page<>(queryDTO.getPageNum(), queryDTO.getPageSize());

            QueryWrapper<SysUserRole> wrapper = new QueryWrapper<>();
            if(StringUtils.hasText(queryDTO.getRoleCode())){
                wrapper.like("r.role_code",queryDTO.getRoleCode());
            }
            if(StringUtils.hasText(queryDTO.getRoleName())){
                wrapper.like("r.role_name",queryDTO.getRoleName());
            }
            if(StringUtils.hasText(queryDTO.getUserAccount())){
                wrapper.like("u.user_account",queryDTO.getUserAccount());
            }
            if(StringUtils.hasText(queryDTO.getRealName())){
                wrapper.like("u.real_name",queryDTO.getRealName());
            }
            return ResponseEntity.ok(
                    baseMapper.queryPage(page, wrapper)
            );
        }catch (Exception e){
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("服务器错误");
        }
    }
}
