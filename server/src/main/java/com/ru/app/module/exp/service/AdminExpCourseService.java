package com.ru.app.module.exp.service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ru.app.common.dto.CommonExpCourseDTO;
import com.ru.app.common.dto.CommonExpLabDTO;
import com.ru.app.common.entity.ExpLab;
import com.ru.app.common.service.AuthorityService;
import com.ru.app.module.exp.dto.AdminExpCourseQueryDTO;
import com.ru.app.module.exp.mapper.AdminExpCourseMapper;
import com.ru.app.common.dto.SelectOptionDTO;
import com.ru.app.common.entity.ExpCourse;
import com.ru.app.common.service.BaseDatabaseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class AdminExpCourseService extends BaseDatabaseService<AdminExpCourseMapper, ExpCourse> {
    private final AuthorityService authorityService;
    AdminExpCourseService(
            AuthorityService authorityService
    ){
        this.authorityService=authorityService;
    }
    public ResponseEntity<?> selectOption(){
        try {
            QueryWrapper<ExpCourse> wrapper = new QueryWrapper<>();

            applyRoleFilterForOption(wrapper);

            List<CommonExpCourseDTO> list=baseMapper.queryAll(wrapper);
            List<SelectOptionDTO> selectOptionDTOS=new ArrayList<>();
            for(CommonExpCourseDTO adminExpCourseDTO:list){
                SelectOptionDTO selectOptionDTO=new SelectOptionDTO();
                selectOptionDTO.setLabel(
                        adminExpCourseDTO.getCourseName()+
                                "["+
                                adminExpCourseDTO.getCourseType()+","+
                                adminExpCourseDTO.getCourseCode()+
                                "]"
                );
                selectOptionDTO.setValue(
                        adminExpCourseDTO.getId().toString()
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
    public ResponseEntity<?> queryPage(AdminExpCourseQueryDTO queryDTO) {
        try {
            // 构建分页参数
            Page<CommonExpCourseDTO> page = new Page<>(queryDTO.getPageNum(), queryDTO.getPageSize());

            QueryWrapper<ExpCourse> wrapper = new QueryWrapper<>();
            if(StringUtils.hasText(queryDTO.getCourseCode())){
                wrapper.like("ec.course_code",queryDTO.getCourseCode());
            }
            if(StringUtils.hasText(queryDTO.getCourseCode())){
                wrapper.like("ec.course_name",queryDTO.getCourseName());
            }
            if(queryDTO.getCourseType()!=null){
                wrapper.eq("ec.course_type",queryDTO.getCourseType());
            }
            if(StringUtils.hasText(queryDTO.getDeptName())){
                wrapper.like("d.dept_name",queryDTO.getDeptName());
            }

            applyRoleFilterForQuery(wrapper);

            return ResponseEntity.ok(
                    baseMapper.queryPage(page, wrapper)
            );
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("服务器错误："+e.getMessage());
        }
    }
    public ResponseEntity<?> batchInsert(List<ExpCourse> expCourseList) {
        try {
            if(authorityService.hasRole(AuthorityService.ADMIN)){
                Long deptId=authorityService.selectUserRoleByUserAccountAndRoleCode().getDeptId();
                expCourseList=expCourseList.stream().peek(
                        item-> item.setDeptId(deptId)
                ).toList();
                //先获取所有课程，筛选出自己学院的课程
                QueryWrapper<ExpCourse> wrapper=new QueryWrapper<>();
                wrapper.eq("ec.dept_id",deptId);
                List<CommonExpCourseDTO> commonExpLabDTOList=baseMapper.queryAll(wrapper);
                //以courserName为主键，先排查存在的，不存在的放一边
                List<ExpCourse> noExitInDb=expCourseList.stream().filter(
                        item-> commonExpLabDTOList.stream().noneMatch(
                                w-> w.getCourseName().equals(item.getCourseName())
                        )
                ).toList();
                List<ExpCourse> exitInDb=expCourseList.stream().filter(
                        item-> commonExpLabDTOList.stream().anyMatch(
                                w-> w.getCourseName().equals(item.getCourseName())
                        )
                ).toList();
                System.out.println("不存在的数量："+noExitInDb.size());
                System.out.println("存在的数量："+exitInDb.size());
                boolean status= saveBatch(noExitInDb);
                if(status){
                    return ResponseEntity.ok().body("插入成功");
                }
                return ResponseEntity.badRequest().body("插入失败");
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("没得权限干这事");
        }catch (Exception e){
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("服务器错误");
        }
    }
    private void applyRoleFilterForQuery(QueryWrapper<ExpCourse> wrapper) throws Exception {
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
    public void applyRoleFilterForOption(QueryWrapper<ExpCourse> wrapper) throws Exception {
        if(authorityService.hasRole(AuthorityService.ROOT)){

        }
        else if (authorityService.hasRole(AuthorityService.ADMIN)){
            Long deptId=authorityService.selectUserRoleByUserAccountAndRoleCode().getDeptId();
            wrapper.eq("d.id",deptId);
        }else if (authorityService.hasRole(AuthorityService.TEACHER)){
            Long deptId=authorityService.getSysUser().getDeptId();
            wrapper.eq("d.id",deptId);
        }
        else{
            throw new Exception("权限不足");
        }
    }
}
