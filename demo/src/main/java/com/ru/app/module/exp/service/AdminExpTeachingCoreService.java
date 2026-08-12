package com.ru.app.module.exp.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ru.app.common.service.AuthorityService;
import com.ru.app.module.exp.dto.AdminExpTeachingCoreQueryDTO;
import com.ru.app.module.exp.mapper.AdminExpTeachingCoreMapper;
import com.ru.app.common.dto.CommonExpTeachingCoreDTO;
import com.ru.app.common.dto.SelectOptionDTO;
import com.ru.app.common.entity.ExpTeachingCore;
import com.ru.app.common.service.BaseDatabaseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class AdminExpTeachingCoreService extends BaseDatabaseService<AdminExpTeachingCoreMapper, ExpTeachingCore> {
    private final AuthorityService authorityService;
    AdminExpTeachingCoreService(
            AuthorityService authorityService
    ){
        this.authorityService=authorityService;
    }
    public ResponseEntity<?> selectOption(String semester){
        try {
            QueryWrapper<ExpTeachingCore> wrapper = new QueryWrapper<>();
            if(StringUtils.hasText(semester)){
                wrapper.eq("etc.semester",semester);
            }
            applyRoleFilterForOption(wrapper);
            List<CommonExpTeachingCoreDTO> list=baseMapper.queryAll(wrapper);
            List<SelectOptionDTO> selectOptionDTOS=new ArrayList<>();
            for(CommonExpTeachingCoreDTO commonExpTeachingCoreDTO:list){
                SelectOptionDTO selectOptionDTO=new SelectOptionDTO();
                selectOptionDTO.setLabel(
                        commonExpTeachingCoreDTO.getCourseName()+
                                "["+
                                commonExpTeachingCoreDTO.getSemester()+","+
                                commonExpTeachingCoreDTO.getUserAccount()+","+
                                commonExpTeachingCoreDTO.getRealName()+","+
                                commonExpTeachingCoreDTO.getClassName()+
                                "]"
                );
                selectOptionDTO.setValue(
                        commonExpTeachingCoreDTO.getId().toString()
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
    public ResponseEntity<?> queryPage(AdminExpTeachingCoreQueryDTO queryDTO) {
        try {
            // 构建分页参数
            Page<CommonExpTeachingCoreDTO> page = new Page<>(queryDTO.getPageNum(), queryDTO.getPageSize());

            QueryWrapper<ExpTeachingCore> wrapper = new QueryWrapper<>();
            if(StringUtils.hasText(queryDTO.getSemester())){
                wrapper.eq("etc.semester",queryDTO.getSemester());
            }
            if(StringUtils.hasText(queryDTO.getClassName())){
                wrapper.like("c.class_name",queryDTO.getClassName());
            }
            if(StringUtils.hasText(queryDTO.getCourseName())){
                wrapper.like("ec.course_name",queryDTO.getCourseName());
            }
            if(StringUtils.hasText(queryDTO.getRealName())){
                wrapper.like("u.real_name",queryDTO.getRealName());
            }
            applyRoleFilterForQuery(wrapper);
            return ResponseEntity.ok(
                    baseMapper.queryPage(page, wrapper)
            );
        }catch (Exception e){
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("服务器错误："+e.getMessage());
        }
    }
    public ResponseEntity<?> queryDetail(Long teachingCoreId) {
        try {
            QueryWrapper<ExpTeachingCore> wrapper = new QueryWrapper<>();
            wrapper.eq("etc.id",teachingCoreId);
            CommonExpTeachingCoreDTO detail=baseMapper.queryDetail(wrapper);
            return ResponseEntity.ok(
                    detail
            );
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("服务器错误："+e.getMessage());
        }
    }
    public void applyRoleFilterForQuery(QueryWrapper<ExpTeachingCore> wrapper) throws Exception {
        if(authorityService.hasRole(AuthorityService.ROOT)){

        }
        else if (authorityService.hasRole(AuthorityService.ADMIN)){
            Long deptId=authorityService.selectUserRoleByUserAccountAndRoleCode().getDeptId();
            wrapper.eq("ec.dept_id",deptId);
        }else if(authorityService.hasRole(AuthorityService.TEACHER)){
            Long userId=authorityService.getSysUser().getId();
            wrapper.eq("u.id",userId);
        }
        else if(authorityService.hasRole(AuthorityService.STUDENT)){
            Long classId=authorityService.getSysUser().getClassId();
            wrapper.eq("c.id",classId);
        }
        else{
            throw new Exception("权限不足");
        }
    }
    public void applyRoleFilterForOption(QueryWrapper<ExpTeachingCore> wrapper) throws Exception {
        if(authorityService.hasRole(AuthorityService.ROOT)){

        }
        else if (authorityService.hasRole(AuthorityService.ADMIN)){
            Long deptId=authorityService.selectUserRoleByUserAccountAndRoleCode().getDeptId();
            wrapper.eq("ec.dept_id",deptId);
        }else if(authorityService.hasRole(AuthorityService.TEACHER)){
            Long userId=authorityService.getSysUser().getId();
            wrapper.eq("u.id",userId);
        }
        else if(authorityService.hasRole(AuthorityService.STUDENT)){
            Long classId=authorityService.getSysUser().getClassId();
            wrapper.eq("c.id",classId);
        }
        else{
            throw new Exception("权限不足");
        }
    }
}
