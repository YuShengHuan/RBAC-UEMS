package com.ru.app.module.exp.service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ru.app.common.dto.*;
import com.ru.app.common.entity.*;
import com.ru.app.common.service.AuthorityService;
import com.ru.app.module.base.service.AdminBaseClassService;
import com.ru.app.module.exp.dto.AdminExpCourseScheduleQueryDTO;
import com.ru.app.module.exp.mapper.AdminExpCourseScheduleMapper;
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
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class AdminExpCourseScheduleService extends BaseDatabaseService<AdminExpCourseScheduleMapper, ExpCourseSchedule> {
    private final AuthorityService authorityService;
    private final AdminExpTeachingCoreService adminExpTeachingCoreService;
    private final AdminSysUserService adminSysUserService;
    private final AdminExpCourseService adminExpCourseService;
    private final AdminBaseClassService adminBaseClassService;
    private final AdminExpLabService adminExpLabService;
    AdminExpCourseScheduleService(
            AuthorityService authorityService,
            AdminExpTeachingCoreService adminExpTeachingCoreService,
            AdminSysUserService adminSysUserService,
            AdminExpCourseService adminExpCourseService,
            AdminBaseClassService adminBaseClassService,
            AdminExpLabService adminExpLabService
    ){
        this.authorityService=authorityService;
        this.adminExpTeachingCoreService=adminExpTeachingCoreService;
        this.adminSysUserService=adminSysUserService;
        this.adminExpCourseService=adminExpCourseService;
        this.adminBaseClassService=adminBaseClassService;
        this.adminExpLabService=adminExpLabService;
    }
    public ResponseEntity<?> selectOption(){
        try {
            QueryWrapper<ExpCourseSchedule> wrapper = new QueryWrapper<>();

            applyRoleFilterForOption(wrapper);
            List<CommonExpCourseScheduleDTO> list=baseMapper.queryAll(wrapper).stream().map(CommonExpCourseScheduleDTO::convertDTO).toList();
            List<SelectOptionDTO> selectOptionDTOS=new ArrayList<>();
            for(CommonExpCourseScheduleDTO adminExpCourseScheduleDTO:list){
                SelectOptionDTO selectOptionDTO=new SelectOptionDTO();
                selectOptionDTO.setLabel(
                        adminExpCourseScheduleDTO.getCourseName()+
                                "["+
                                adminExpCourseScheduleDTO.getSemester()+","+
                                adminExpCourseScheduleDTO.getClassName()+","+
                                adminExpCourseScheduleDTO.getLabLocation()+","+
                                adminExpCourseScheduleDTO.getUserAccount()+","+
                                adminExpCourseScheduleDTO.getRealName()+
                                "]"
                );
                selectOptionDTO.setValue(
                        adminExpCourseScheduleDTO.getId().toString()
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
    public ResponseEntity<?> queryPage(AdminExpCourseScheduleQueryDTO queryDTO) {
        try {
            // 构建分页参数
            Page<CommonExpCourseScheduleDTO> page = new Page<>(queryDTO.getPageNum(), queryDTO.getPageSize());

            QueryWrapper<ExpCourseSchedule> wrapper = new QueryWrapper<>();
            if(StringUtils.hasText(queryDTO.getSemester())){
                wrapper.eq("etc.semester",queryDTO.getSemester());
            }
            if(queryDTO.getIsReport()!=null){
                wrapper.eq("ecs.is_report",queryDTO.getIsReport());
            }
            if(StringUtils.hasText(queryDTO.getClassName())){
                wrapper.like("c.class_name",queryDTO.getClassName());
            }
            if(StringUtils.hasText(queryDTO.getCourseName())){
                wrapper.like("ec.course_name",queryDTO.getCourseName());
            }
            if(StringUtils.hasText(queryDTO.getLabLocation())){
                wrapper.like("b.lab_location",queryDTO.getLabLocation());
            }
            if(StringUtils.hasText(queryDTO.getRealName())){
                wrapper.like("u.real_name",queryDTO.getRealName());
            }
            applyRoleFilterForQuery(wrapper);
            IPage<CommonExpCourseScheduleDTO> pageResult = baseMapper.queryPage(page, wrapper);
            pageResult.getRecords().forEach(CommonExpCourseScheduleDTO::convertDTO);
            return ResponseEntity.ok(
                    pageResult
            );
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("服务器错误："+e.getMessage());
        }
    }
    public ResponseEntity<?> queryDetail(Long scheduleId) {
        try {
            QueryWrapper<ExpCourseSchedule> wrapper = new QueryWrapper<>();
            wrapper.eq("ecs.id",scheduleId);
            CommonExpCourseScheduleDTO detail=baseMapper.queryDetail(wrapper).convertDTO();
            return ResponseEntity.ok(
                    detail
            );
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("服务器错误："+e.getMessage());
        }
    }

    @Transactional
    public ResponseEntity<?> batchInsert(List<CommonExpCourseScheduleDTO> commonExpCourseScheduleDTOList) {
        try {

            QueryWrapper<ExpTeachingCore> teachingCoreQueryWrapper=new QueryWrapper<>();
            adminExpTeachingCoreService.applyRoleFilterForOption(teachingCoreQueryWrapper);
            List<CommonExpTeachingCoreDTO> commonExpTeachingCoreDTOList=adminExpTeachingCoreService.getBaseMapper().queryAll(teachingCoreQueryWrapper);

            QueryWrapper<SysUser> sysUserQueryWrapper=new QueryWrapper<>();
            adminSysUserService.applyRoleFilterForOption(sysUserQueryWrapper);
            List<CommonSysUserDTO> commonSysUserDTOList=adminSysUserService.getBaseMapper().queryAll(sysUserQueryWrapper);

            QueryWrapper<ExpCourse> expCourseQueryWrapper=new QueryWrapper<>();
            adminExpCourseService.applyRoleFilterForOption(expCourseQueryWrapper);
            List<CommonExpCourseDTO> commonExpCourseDTOList=adminExpCourseService.getBaseMapper().queryAll(expCourseQueryWrapper);

            QueryWrapper<BaseClass> baseClassQueryWrapper=new QueryWrapper<>();
            adminBaseClassService.applyRoleFilterForOption(baseClassQueryWrapper);
            List<CommonBaseClassDTO> commonBaseClassDTOList=adminBaseClassService.getBaseMapper().queryAll(baseClassQueryWrapper);

            QueryWrapper<ExpLab> expLabQueryWrapper=new QueryWrapper<>();
            adminExpLabService.applyRoleFilterForOption(expLabQueryWrapper);
            List<CommonExpLabDTO> commonExpLabDTOList=adminExpLabService.getBaseMapper().queryAll(expLabQueryWrapper);

            QueryWrapper<ExpCourseSchedule> courseScheduleQueryWrapper=new QueryWrapper<>();
            applyRoleFilterForOption(courseScheduleQueryWrapper);
            List<CommonExpCourseScheduleDTO> dbCommonExpCourseScheduleDTOList=baseMapper.queryAll(courseScheduleQueryWrapper).stream().peek(
                    CommonExpCourseScheduleDTO::convertDTO
            ).toList();
            //以permCode为主键，先排查存在的，不存在的放一边
            List<CommonExpCourseScheduleDTO> noExitInDb=commonExpCourseScheduleDTOList.stream().filter(
                    item-> dbCommonExpCourseScheduleDTOList.stream().noneMatch(
                            w-> w.getSemester().equals(item.getSemester())&&
                                w.getUserAccount().equals(item.getUserAccount())&&
                                w.getCourseName().equals(item.getCourseName())&&
                                w.getClassName().equals(item.getClassName())&&
                                w.getLabLocation().equals(item.getLabLocation())&&
                                w.getWeekRange().equals(item.getWeekRange())&&
                                w.getClassPeriod().equals(item.getClassPeriod())
                    )
            ).toList();
            List<CommonExpCourseScheduleDTO> exitInDb=commonExpCourseScheduleDTOList.stream().filter(
                    item-> dbCommonExpCourseScheduleDTOList.stream().anyMatch(
                            w-> w.getSemester().equals(item.getSemester())&&
                                w.getUserAccount().equals(item.getUserAccount())&&
                                w.getCourseName().equals(item.getCourseName())&&
                                w.getClassName().equals(item.getClassName())&&
                                w.getLabLocation().equals(item.getLabLocation())&&
                                w.getWeekRange().equals(item.getWeekRange())&&
                                w.getClassPeriod().equals(item.getClassPeriod())
                    )
            ).toList();

            System.out.println("不存在的数量："+noExitInDb.size());
            System.out.println("存在的数量："+exitInDb.size());
            if(noExitInDb.isEmpty()){
                return ResponseEntity.badRequest().body("插入数据已存在");
            }

            List<ExpCourseSchedule> saveCourseScheduleList=noExitInDb.stream().map(
                    et->{
                        Optional<CommonExpTeachingCoreDTO> optionalCommonExpTeachingCoreDTO=
                                commonExpTeachingCoreDTOList.stream().filter(
                                        tc-> (tc.getSemester().equals(et.getSemester()) &&
                                                    tc.getCourseName().equals(et.getCourseName())
                                                &&tc.getClassName().equals(et.getClassName())
                                                &&tc.getUserAccount().equals(et.getUserAccount())
                                        )
                                ).findFirst();
                        ExpCourseSchedule courseSchedule=new ExpCourseSchedule();
                        et.convertEntity();
                        optionalCommonExpTeachingCoreDTO.ifPresent(commonExpTeachingCoreDTO -> {
                            et.setTeachingCoreId(commonExpTeachingCoreDTO.getId());
                        });
                        Optional<CommonExpLabDTO> optionalCommonExpLabDTO=
                                commonExpLabDTOList.stream().filter(
                                        tc-> tc.getLabLocation().equals(et.getLabLocation())
                                ).findFirst();
                        optionalCommonExpLabDTO.ifPresent(
                                commonExpLabDTO -> et.setLabId(commonExpLabDTO.getId())
                        );
                        BeanUtils.copyProperties(et, courseSchedule);
                        if(courseSchedule.getTeachingCoreId()!=null){
                            return courseSchedule;
                        }
                        ExpTeachingCore expTeachingCore=new ExpTeachingCore();
                        expTeachingCore.setSemester(et.getSemester());
                        Optional<CommonExpCourseDTO> optionalCommonExpCourseDTO=
                                commonExpCourseDTOList.stream().filter(
                                        tc-> tc.getCourseName().equals(et.getCourseName())
                                ).findFirst();
                        optionalCommonExpCourseDTO.ifPresent(
                                commonExpCourseDTO -> expTeachingCore.setCourseId(commonExpCourseDTO.getId())
                        );
                        Optional<CommonSysUserDTO> optionalCommonSysUserDTO=
                                commonSysUserDTOList.stream().filter(
                                        tc-> tc.getUserAccount().equals(et.getUserAccount())
                                ).findFirst();
                        optionalCommonSysUserDTO.ifPresent(
                                commonSysUserDTO -> expTeachingCore.setUserId(commonSysUserDTO.getId())
                        );
                        Optional<CommonBaseClassDTO> optionalCommonBaseClassDTO=
                                commonBaseClassDTOList.stream().filter(
                                        tc-> tc.getClassName().equals(et.getClassName())
                                ).findFirst();
                        optionalCommonBaseClassDTO.ifPresent(
                                commonBaseClassDTO -> expTeachingCore.setClassId(commonBaseClassDTO.getId())
                        );


                        boolean saveStatus=adminExpTeachingCoreService.save(expTeachingCore);
                        if(saveStatus){
                            courseSchedule.setTeachingCoreId(expTeachingCore.getId());
                            CommonExpTeachingCoreDTO commonExpTeachingCoreDTO=new CommonExpTeachingCoreDTO();
                            BeanUtils.copyProperties(et, commonExpTeachingCoreDTO);
                            BeanUtils.copyProperties(expTeachingCore, commonExpTeachingCoreDTO);
                            commonExpTeachingCoreDTOList.add(
                                    commonExpTeachingCoreDTO
                            );
                            return courseSchedule;
                        }
                        return courseSchedule;
                    }
            ).filter(item->item.getTeachingCoreId()!=null||StringUtils.hasText(String.valueOf(item.getTeachingCoreId()))).toList();


            boolean status= saveBatch(saveCourseScheduleList);
            if(status){
                return ResponseEntity.ok().body("插入成功");
            }
            return ResponseEntity.badRequest().body("插入失败");


        }catch (Exception e){
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("服务器错误");
        }
    }

    private void applyRoleFilterForQuery(QueryWrapper<ExpCourseSchedule> wrapper) throws Exception {
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
    private void applyRoleFilterForOption(QueryWrapper<ExpCourseSchedule> wrapper) throws Exception {
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
