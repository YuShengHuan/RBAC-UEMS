package com.ru.app.module.base.service;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ru.app.common.dto.CommonBaseDeptDTO;
import com.ru.app.common.dto.CommonExpLabDTO;
import com.ru.app.common.entity.ExpLab;
import com.ru.app.common.service.AuthorityService;
import com.ru.app.module.base.dto.AdminBaseDeptQueryDTO;
import com.ru.app.module.base.mapper.AdminBaseDeptMapper;
import com.ru.app.common.dto.SelectOptionDTO;
import com.ru.app.common.entity.BaseDept;
import com.ru.app.common.service.BaseDatabaseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class AdminBaseDeptService extends BaseDatabaseService<AdminBaseDeptMapper, BaseDept> {
    private final AuthorityService authorityService;
    AdminBaseDeptService(
            AuthorityService authorityService
    ){
        this.authorityService=authorityService;
    }
    public ResponseEntity<?> selectOption(){
        try {
            QueryWrapper<BaseDept> wrapper = new QueryWrapper<>();
            applyRoleFilterForOption(wrapper);
            List<CommonBaseDeptDTO> list=baseMapper.queryAll(wrapper);
            List<SelectOptionDTO> selectOptionDTOS=new ArrayList<>();
            for(CommonBaseDeptDTO adminBaseDeptDTO:list){
                SelectOptionDTO selectOptionDTO=new SelectOptionDTO();
                selectOptionDTO.setLabel(
                        adminBaseDeptDTO.getDeptName()+
                                "["+
                                adminBaseDeptDTO.getDeptCode()+
                                "]"
                );
                selectOptionDTO.setValue(
                        adminBaseDeptDTO.getId().toString()
                );
                selectOptionDTOS.add(selectOptionDTO);
            }
            return ResponseEntity.ok(
                    selectOptionDTOS
            );
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("服务器错误："+e.getMessage());
        }
    }
    public ResponseEntity<?> queryPage(AdminBaseDeptQueryDTO queryDTO) {
        try {
            // 构建分页参数
            Page<CommonBaseDeptDTO> page = new Page<>(queryDTO.getPageNum(), queryDTO.getPageSize());

            QueryWrapper<BaseDept> wrapper = new QueryWrapper<>();
            if(StringUtils.hasText(queryDTO.getDeptCode())){
                wrapper.like("d.dept_code",queryDTO.getDeptCode());
            }
            if(StringUtils.hasText(queryDTO.getDeptName())){
                wrapper.like("d.dept_name",queryDTO.getDeptName());
            }
            if(queryDTO.getCampus()!=null){
                wrapper.eq("d.campus",queryDTO.getCampus());
            }
            applyRoleFilterForQuery(wrapper);
            return ResponseEntity.ok(
                    baseMapper.queryPage(page, wrapper)
            );
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("服务器错误："+e.getMessage());
        }
    }
    public ResponseEntity<?> batchInsert(List<BaseDept> baseDeptList) {
        try {
            QueryWrapper<BaseDept> wrapper=new QueryWrapper<>();
            List<CommonBaseDeptDTO> commonExpLabDTOList=baseMapper.queryAll(wrapper);
            //以DeptName为主键，先排查存在的，不存在的放一边
            List<BaseDept> noExitInDb=baseDeptList.stream().filter(
                    item-> commonExpLabDTOList.stream().noneMatch(
                            w-> w.getDeptName().equals(item.getDeptName())
                    )
            ).toList();
            List<BaseDept> exitInDb=baseDeptList.stream().filter(
                    item-> commonExpLabDTOList.stream().anyMatch(
                            w-> w.getDeptName().equals(item.getDeptName())
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
    private void applyRoleFilterForQuery(QueryWrapper<BaseDept> wrapper) throws Exception {
        if(authorityService.hasRole(AuthorityService.ROOT)){

        }
        else if (authorityService.hasRole(AuthorityService.ADMIN)){
            Long deptId=authorityService.selectUserRoleByUserAccountAndRoleCode().getDeptId();
            wrapper.eq("d.id",deptId);
        }
        else{
            throw new Exception("权限不足");
        }
    }
    private void applyRoleFilterForOption(QueryWrapper<BaseDept> wrapper) throws Exception {
        if(authorityService.hasRole(AuthorityService.ROOT)){

        }
        else if (authorityService.hasRole(AuthorityService.ADMIN)){
            Long deptId=authorityService.selectUserRoleByUserAccountAndRoleCode().getDeptId();
            wrapper.eq("d.id",deptId);
        }else if (authorityService.hasRole(AuthorityService.TEACHER)){

        }else if (authorityService.hasRole(AuthorityService.STUDENT)){

        }
        else{
            throw new Exception("权限不足");
        }
    }
}
