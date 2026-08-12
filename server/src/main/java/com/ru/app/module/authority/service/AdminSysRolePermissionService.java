package com.ru.app.module.authority.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ru.app.common.dto.CommonSysRolePermissionDTO;
import com.ru.app.common.service.AuthorityService;
import com.ru.app.module.authority.dto.AdminSysRolePermissionQueryDTO;
import com.ru.app.module.authority.mapper.AdminSysRolePermissionMapper;
import com.ru.app.common.entity.SysRolePermission;
import com.ru.app.common.service.BaseDatabaseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class AdminSysRolePermissionService extends BaseDatabaseService<AdminSysRolePermissionMapper, SysRolePermission> {
    private final AuthorityService authorityService;
    AdminSysRolePermissionService(AuthorityService authorityService){
        this.authorityService=authorityService;
    }
    public ResponseEntity<?> queryPage(AdminSysRolePermissionQueryDTO queryDTO) {
        try {
            // 构建分页参数
            Page<CommonSysRolePermissionDTO> page = new Page<>(queryDTO.getPageNum(), queryDTO.getPageSize());

            QueryWrapper<SysRolePermission> wrapper = new QueryWrapper<>();
            if(StringUtils.hasText(queryDTO.getRoleCode())){
                wrapper.like("r.role_code",queryDTO.getRoleCode());
            }
            if(StringUtils.hasText(queryDTO.getRoleName())){
                wrapper.like("r.role_name",queryDTO.getRoleName());
            }
            if(StringUtils.hasText(queryDTO.getPermCode())){
                wrapper.like("p.perm_code",queryDTO.getPermCode());
            }
            if(StringUtils.hasText(queryDTO.getPermName())){
                wrapper.like("p.perm_name",queryDTO.getPermName());
            }
            return ResponseEntity.ok(
                    baseMapper.queryPage(page, wrapper)
            );
        }catch (Exception e){
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("服务器错误");
        }
    }
    @Transactional
    public ResponseEntity<?> batchInsert(
            Long roleId,
            List<SysRolePermission> sysRolePermissionList) {
        try {
            baseMapper.deleteByRoleId(roleId);
            boolean status= saveBatch(sysRolePermissionList);
            if(status){
                authorityService.initAllRolePermToRedis();
                return ResponseEntity.ok().body("插入成功");
            }
            return ResponseEntity.badRequest().body("插入失败");
        }catch (Exception e){
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("服务器错误");
        }
    }
}
