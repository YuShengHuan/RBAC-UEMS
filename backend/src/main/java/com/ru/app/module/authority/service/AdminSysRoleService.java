package com.ru.app.module.authority.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ru.app.common.dto.CommonSysPermissionDTO;
import com.ru.app.common.dto.CommonSysRoleDTO;
import com.ru.app.common.entity.SysPermission;
import com.ru.app.module.authority.dto.AdminSysRoleQueryDTO;
import com.ru.app.module.authority.mapper.AdminSysRoleMapper;
import com.ru.app.common.dto.SelectOptionDTO;
import com.ru.app.common.entity.SysRole;
import com.ru.app.common.service.BaseDatabaseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class AdminSysRoleService extends BaseDatabaseService<AdminSysRoleMapper, SysRole> {
    public ResponseEntity<?> selectOption(){
        try {
            QueryWrapper<SysRole> wrapper = new QueryWrapper<>();
            List<CommonSysRoleDTO> list=baseMapper.queryAll(wrapper);
            List<SelectOptionDTO> selectOptionDTOS=new ArrayList<>();
            for(CommonSysRoleDTO adminSysRoleDTO:list){
                SelectOptionDTO selectOptionDTO=new SelectOptionDTO();
                selectOptionDTO.setLabel(
                        adminSysRoleDTO.getRoleName()+
                                "["+
                                adminSysRoleDTO.getRoleCode()+
                                "]"
                );
                selectOptionDTO.setValue(
                        adminSysRoleDTO.getId().toString()
                );
                selectOptionDTOS.add(selectOptionDTO);
            }
            return ResponseEntity.ok(
                    selectOptionDTOS
            );
        }catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("服务器错误");
        }
    }
    public ResponseEntity<?> queryPage(AdminSysRoleQueryDTO queryDTO) {
        try {
            // 构建分页参数
            Page<CommonSysRoleDTO> page = new Page<>(queryDTO.getPageNum(), queryDTO.getPageSize());

            QueryWrapper<SysRole> wrapper = new QueryWrapper<>();
            if(StringUtils.hasText(queryDTO.getRoleCode())){
                wrapper.like("r.role_code",queryDTO.getRoleCode());
            }
            if(StringUtils.hasText(queryDTO.getRoleName())){
                wrapper.like("r.role_name",queryDTO.getRoleName());
            }
            return ResponseEntity.ok(
                    baseMapper.queryPage(page, wrapper)
            );
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("服务器错误");
        }
    }
    public ResponseEntity<?> batchInsert(List<SysRole> sysRoleList) {
        try {
            //先获取所有权限
            QueryWrapper<SysRole> wrapper=new QueryWrapper<>();
            List<CommonSysRoleDTO> commonSysPermissionDTOList=baseMapper.queryAll(wrapper);
            //以roleCode为主键，先排查存在的，不存在的放一边
            List<SysRole> noExitInDb=sysRoleList.stream().filter(
                    item-> commonSysPermissionDTOList.stream().noneMatch(
                            w->w.getRoleCode().equals(item.getRoleCode())
                    )
            ).toList();
            List<SysRole> exitInDb=sysRoleList.stream().filter(
                    item-> commonSysPermissionDTOList.stream().anyMatch(
                            w->w.getRoleCode().equals(item.getRoleCode())
                    )
            ).toList();
            System.out.println("不存在的数量："+noExitInDb.size());
            System.out.println("存在的数量："+exitInDb.size());
            boolean status= saveBatch(noExitInDb);
            if(status){
                return ResponseEntity.ok().body("插入成功");
            }else{
                return ResponseEntity.badRequest().body("插入失败");
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("服务器错误");
        }
    }
}
