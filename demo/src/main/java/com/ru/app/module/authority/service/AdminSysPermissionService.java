package com.ru.app.module.authority.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ru.app.common.dto.CommonSysPermissionDTO;
import com.ru.app.common.service.AuthorityService;
import com.ru.app.module.authority.dto.AdminSysPermissionQueryDTO;
import com.ru.app.module.authority.dto.SysPermissionTreeDTO;
import com.ru.app.module.authority.dto.SysPermissionWithOwnDTO;
import com.ru.app.module.authority.mapper.AdminSysPermissionMapper;
import com.ru.app.common.dto.SelectOptionDTO;
import com.ru.app.common.entity.SysPermission;
import com.ru.app.common.service.BaseDatabaseService;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminSysPermissionService extends BaseDatabaseService<AdminSysPermissionMapper, SysPermission> {
    private final AuthorityService authorityService;
    AdminSysPermissionService(
            AuthorityService authorityService
    ){
        this.authorityService=authorityService;
    }
    public ResponseEntity<?> selectOption(Integer isTop){
        try {
            QueryWrapper<SysPermission> wrapper=new QueryWrapper<>();

            if(isTop!=null){
                wrapper.and(w -> w.isNull("p.parent_code").or().eq("p.parent_code", ""));
            }
            List<CommonSysPermissionDTO> list=baseMapper.queryAll(wrapper);
            List<SelectOptionDTO> selectOptionDTOS=new ArrayList<>();

            for(CommonSysPermissionDTO adminSysPermissionDTO:list){
                SelectOptionDTO selectOptionDTO=new SelectOptionDTO();
                selectOptionDTO.setLabel(
                        adminSysPermissionDTO.getPermName()+
                                "["+
                                adminSysPermissionDTO.getPermCode()+
                                "]"
                );
                selectOptionDTO.setValue(
                        isTop!=null?adminSysPermissionDTO.getPermCode():adminSysPermissionDTO.getId().toString()
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
    public ResponseEntity<?> queryPage(AdminSysPermissionQueryDTO queryDTO) {
        try {
            // 构建分页参数
            Page<CommonSysPermissionDTO> page = new Page<>(queryDTO.getPageNum(), queryDTO.getPageSize());

            QueryWrapper<SysPermission> wrapper = new QueryWrapper<>();
            if(StringUtils.hasText(queryDTO.getPermCode())){
                wrapper.like("p.perm_code",queryDTO.getPermCode());
            }
            if(StringUtils.hasText(queryDTO.getPermName())){
                wrapper.like("p.perm_name",queryDTO.getPermName());
            }
            if(queryDTO.getPermType()!=null){
                wrapper.eq("p.perm_type",queryDTO.getPermType());
            }
            return ResponseEntity.ok(
                    baseMapper.queryPage(page, wrapper)
            );
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("服务器错误");
        }
    }
    public ResponseEntity<?> batchInsert(List<SysPermission> sysPermissionList) {
        try {
            //先获取所有权限
            QueryWrapper<SysPermission> wrapper=new QueryWrapper<>();
            List<CommonSysPermissionDTO> commonSysPermissionDTOList=baseMapper.queryAll(wrapper);
            //以permCode为主键，先排查存在的，不存在的放一边
            List<SysPermission> noExitInDb=sysPermissionList.stream().filter(
                    item-> commonSysPermissionDTOList.stream().noneMatch(
                            w->w.getPermCode().equals(item.getPermCode())
                    )
            ).toList();
            List<SysPermission> exitInDb=sysPermissionList.stream().filter(
                    item-> commonSysPermissionDTOList.stream().anyMatch(
                            w->w.getPermCode().equals(item.getPermCode())
                    )
            ).toList();
            System.out.println("不存在的数量："+noExitInDb.size());
            System.out.println("存在的数量："+exitInDb.size());
            boolean status= saveBatch(noExitInDb);
            if(status){
                return ResponseEntity.ok().body("插入成功");
            }
            return ResponseEntity.badRequest().body("插入失败");
        }catch (Exception e){
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("服务器错误");
        }
    }
    public List<SysPermissionTreeDTO> buildPermissionTreeWithOwnFlag(Long roleId) {
        // 1. 一次查询获取全量权限+isOwn标记
        List<SysPermissionWithOwnDTO> permissionWithOwnList = baseMapper.selectAllPermissionsWithOwnFlag(roleId);
        if (permissionWithOwnList.isEmpty()) {
            return new ArrayList<>();
        }

        // 2. 转换为树形DTO（SysPermissionTreeDTO）
        List<SysPermissionTreeDTO> allTreeNodes = permissionWithOwnList.stream()
                .map(permWithOwn -> {
                    SysPermissionTreeDTO treeNode = new SysPermissionTreeDTO();
                    BeanUtils.copyProperties(permWithOwn, treeNode); // 复制所有字段（含isOwn）
                    treeNode.setChildren(new ArrayList<>()); // 初始化子节点
                    return treeNode;
                })
                .collect(Collectors.toList());

        // 3. 递归构建树形结构（复用原有逻辑）
        return allTreeNodes.stream()
                .filter(node -> isTopNode(node.getParentCode()))
                .peek(topNode -> topNode.setChildren(findChildren(topNode.getPermCode(), allTreeNodes)))
                .collect(Collectors.toList());
    }

    // 保留原有辅助方法
    private boolean isTopNode(String parentCode) {
        return parentCode == null || parentCode.trim().isEmpty();
    }

    private List<SysPermissionTreeDTO> findChildren(String parentCode, List<SysPermissionTreeDTO> allNodes) {
        return allNodes.stream()
                .filter(node -> parentCode.equals(node.getParentCode()))
                .peek(childNode -> childNode.setChildren(findChildren(childNode.getPermCode(), allNodes)))
                .collect(Collectors.toList());
    }
}
