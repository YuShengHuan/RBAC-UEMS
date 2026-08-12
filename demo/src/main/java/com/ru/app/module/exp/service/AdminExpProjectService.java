package com.ru.app.module.exp.service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ru.app.common.dto.*;
import com.ru.app.common.entity.*;
import com.ru.app.common.service.AuthorityService;
import com.ru.app.module.base.service.AdminBaseClassService;
import com.ru.app.module.exp.dto.AdminExpProjectQueryDTO;
import com.ru.app.module.exp.mapper.AdminExpProjectMapper;
import com.ru.app.common.service.BaseDatabaseService;
import com.ru.app.module.user.service.AdminSysUserService;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class AdminExpProjectService extends BaseDatabaseService<AdminExpProjectMapper, ExpProject> {
    private final AuthorityService authorityService;
    private final AdminExpTeachingCoreService adminExpTeachingCoreService;

    AdminExpProjectService(
            AuthorityService authorityService,
            AdminExpTeachingCoreService adminExpTeachingCoreService
    ){
        this.authorityService=authorityService;
        this.adminExpTeachingCoreService=adminExpTeachingCoreService;

    }
    public ResponseEntity<?> selectOption(){
        try {
            QueryWrapper<ExpProject> wrapper = new QueryWrapper<>();

            applyRoleFilterForOption(wrapper);

            List<CommonExpProjectDTO> list=baseMapper.queryAll(wrapper);
            List<SelectOptionDTO> selectOptionDTOS=new ArrayList<>();
            for(CommonExpProjectDTO adminExpProjectDTO:list){
                SelectOptionDTO selectOptionDTO=new SelectOptionDTO();
                selectOptionDTO.setLabel(
                        adminExpProjectDTO.getProjectName()+
                                "["+
                                adminExpProjectDTO.getSemester()+","+
                                adminExpProjectDTO.getCourseName()+","+
                                adminExpProjectDTO.getUserAccount()+","+
                                adminExpProjectDTO.getRealName()+","+
                                adminExpProjectDTO.getClassName()+
                                "]"
                );
                selectOptionDTO.setValue(
                        adminExpProjectDTO.getId().toString()
                );
                selectOptionDTOS.add(selectOptionDTO);
            }
            return ResponseEntity.ok(
                    selectOptionDTOS
            );
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("服务器错误");
        }
    }
    public ResponseEntity<?> queryPage(AdminExpProjectQueryDTO queryDTO) {
        try {
            // 构建分页参数
            Page<CommonExpProjectDTO> page = new Page<>(queryDTO.getPageNum(), queryDTO.getPageSize());

            QueryWrapper<ExpProject> wrapper = new QueryWrapper<>();
            if(StringUtils.hasText(queryDTO.getSemester())){
                wrapper.eq("etc.semester",queryDTO.getSemester());
            }
            if(StringUtils.hasText(queryDTO.getProjectCode())){
                wrapper.like("ep.project_code",queryDTO.getProjectCode());
            }
            if(StringUtils.hasText(queryDTO.getProjectName())){
                wrapper.like("ep.project_name",queryDTO.getProjectName());
            }
            if(StringUtils.hasText(queryDTO.getCourseName())){
                wrapper.like("ec.course_name",queryDTO.getCourseName());
            }

            applyRoleFilterForQuery(wrapper);

            IPage<CommonExpProjectDTO> pageResult=baseMapper.queryPage(page, wrapper);
            pageResult.getRecords().forEach(CommonExpProjectDTO::convertDTO);
            return ResponseEntity.ok(
                    pageResult
            );
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("服务器错误："+e.getMessage());
        }
    }
    @Transactional
    public ResponseEntity<?> batchInsert(List<CommonExpProjectDTO> commonExpProjectDTOList) {
        try {

            QueryWrapper<ExpTeachingCore> teachingCoreQueryWrapper=new QueryWrapper<>();
            adminExpTeachingCoreService.applyRoleFilterForOption(teachingCoreQueryWrapper);
            List<CommonExpTeachingCoreDTO> commonExpTeachingCoreDTOList=adminExpTeachingCoreService.getBaseMapper().queryAll(teachingCoreQueryWrapper);

            QueryWrapper<ExpProject> expProjectQueryWrapper=new QueryWrapper<>();
            applyRoleFilterForOption(expProjectQueryWrapper);
            List<CommonExpProjectDTO> commonExpProjectDTOS=baseMapper.queryAll(expProjectQueryWrapper).stream().peek(
                    CommonExpProjectDTO::convertDTO
            ).toList();
            //以permCode为主键，先排查存在的，不存在的放一边
            List<CommonExpProjectDTO> noExitInDb=commonExpProjectDTOList.stream().filter(
                    item-> commonExpProjectDTOS.stream().noneMatch(
                            w-> w.getSemester().equals(item.getSemester())&&
                                    w.getUserAccount().equals(item.getUserAccount())&&
                                    w.getCourseName().equals(item.getCourseName())&&
                                    w.getClassName().equals(item.getClassName())&&
                                    w.getProjectName().equals(item.getProjectName())&&
                                    w.getProjectWeekRange().equals(item.getProjectWeekRange())
                    )
            ).toList();
            List<CommonExpProjectDTO> exitInDb=commonExpProjectDTOList.stream().filter(
                    item-> commonExpProjectDTOS.stream().anyMatch(
                            w-> w.getSemester().equals(item.getSemester())&&
                                    w.getUserAccount().equals(item.getUserAccount())&&
                                    w.getCourseName().equals(item.getCourseName())&&
                                    w.getClassName().equals(item.getClassName())&&
                                    w.getProjectName().equals(item.getProjectName())&&
                                    w.getProjectWeekRange().equals(item.getProjectWeekRange())
                    )
            ).toList();

            System.out.println("不存在的数量："+noExitInDb.size());
            System.out.println("存在的数量："+exitInDb.size());
            if(noExitInDb.isEmpty()){
                return ResponseEntity.badRequest().body("插入数据已存在");
            }

            List<ExpProject> saveProjectList= noExitInDb.stream().map(
                    et -> {
                        Optional<CommonExpTeachingCoreDTO> optionalCommonExpTeachingCoreDTO =
                                commonExpTeachingCoreDTOList.stream().filter(
                                        tc -> (tc.getSemester().equals(et.getSemester()) &&
                                                tc.getCourseName().equals(et.getCourseName())
                                                && tc.getClassName().equals(et.getClassName())
                                                && tc.getUserAccount().equals(et.getUserAccount())
                                        )
                                ).findFirst();
                        ExpProject project = new ExpProject();
                        et.convertEntity();
                        optionalCommonExpTeachingCoreDTO.ifPresent(commonExpTeachingCoreDTO -> {
                            et.setTeachingCoreId(commonExpTeachingCoreDTO.getId());
                            BeanUtils.copyProperties(et, project);
                        });
                        if (et.getTeachingCoreId() != null) {
                            return project;
                        }
                        return null;
                    }
            ).filter(Objects::nonNull).filter(item->item.getTeachingCoreId()!=null).toList();

            boolean status= saveBatch(saveProjectList);
            if(status){
                return ResponseEntity.ok().body("插入成功");
            }
            return ResponseEntity.badRequest().body("插入失败");


        }catch (Exception e){
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("服务器错误");
        }
    }
    public ResponseEntity<?> queryDetail(Long projectId) {
        try {
            QueryWrapper<ExpProject> wrapper = new QueryWrapper<>();
            wrapper.eq("ep.id",projectId);
            CommonExpProjectDTO detail=baseMapper.queryDetail(wrapper).convertDTO();
            return ResponseEntity.ok(
                    detail
            );
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("服务器错误："+e.getMessage());
        }
    }
    private void applyRoleFilterForQuery(QueryWrapper<ExpProject> wrapper) throws Exception {
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
    private void applyRoleFilterForOption(QueryWrapper<ExpProject> wrapper) throws Exception {
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
