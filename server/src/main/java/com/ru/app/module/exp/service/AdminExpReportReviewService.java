package com.ru.app.module.exp.service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ru.app.common.service.AuthorityService;
import com.ru.app.module.exp.dto.*;
import com.ru.app.module.exp.mapper.AdminExpReportReviewMapper;
import com.ru.app.common.dto.CommonExpReportReviewDTO;
import com.ru.app.common.entity.ExpProject;
import com.ru.app.common.entity.ExpReportReview;
import com.ru.app.common.service.BaseDatabaseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class AdminExpReportReviewService extends BaseDatabaseService<AdminExpReportReviewMapper, ExpReportReview> {
    private final AuthorityService authorityService;
    AdminExpReportReviewService(
            AuthorityService authorityService
    ){
        this.authorityService=authorityService;
    }
    public ResponseEntity<?> queryPage(AdminExpReportReviewQueryDTO queryDTO) {
        try {
            // 构建分页参数
            Page<CommonExpReportReviewDTO> page = new Page<>(queryDTO.getPageNum(), queryDTO.getPageSize());

            QueryWrapper<ExpReportReview> wrapper = new QueryWrapper<>();
            if(StringUtils.hasText(queryDTO.getSemester())){
                wrapper.eq("etc.semester",queryDTO.getSemester());
            }
            if(StringUtils.hasText(queryDTO.getCourseName())){
                wrapper.like("ec.course_name",queryDTO.getCourseName());
            }
            if(StringUtils.hasText(queryDTO.getProjectName())){
                wrapper.like("ep.project_name",queryDTO.getProjectName());
            }
            if(StringUtils.hasText(queryDTO.getClassName())){
                wrapper.like("c.class_name",queryDTO.getClassName());
            }
            if(StringUtils.hasText(queryDTO.getUploadRealName())){
                wrapper.like("u1.real_name",queryDTO.getUploadRealName());
            }
            if(StringUtils.hasText(queryDTO.getReviewRealName())){
                wrapper.like("u2.real_name",queryDTO.getReviewRealName());
            }
            applyRoleFilterForQuery(wrapper);
            return ResponseEntity.ok(
                    baseMapper.queryPage(page, wrapper)
            );
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("服务器错误："+e.getMessage());
        }
    }
    public ResponseEntity<?> queryReportReviewStatusPage(FrontExpReportReviewQueryDTO queryDTO, boolean isReview) {
        try {
            // 构建分页参数
            Page<FrontReportReviewStatusDTO> page = new Page<>(queryDTO.getPageNum(), queryDTO.getPageSize());

            QueryWrapper<ExpProject> wrapper = new QueryWrapper<>();
            if(StringUtils.hasText(queryDTO.getSemester())){
                wrapper.eq("etc.semester",queryDTO.getSemester());
            }
            if (StringUtils.hasText(queryDTO.getSearchContent())) {
                String searchContent = queryDTO.getSearchContent() ;

                wrapper.nested(w -> w.like("c.class_name", searchContent)
                        .or()
                        .like("ep.project_name", searchContent)
                        .or()
                        .like("ec.course_name", searchContent)
                        .or()
                        .like("u1.real_name", searchContent)
                        .or()
                        .like("u2.real_name", searchContent)
                );
            }
            if(isReview){
                wrapper.isNotNull("erw.id");
            }
            else{
                wrapper.isNull("erw.id");
            }
            applyRoleFilterForQueryReview(wrapper);
            return ResponseEntity.ok(
                    baseMapper.queryReportReviewStatus(page, wrapper)
            );
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("服务器错误："+e.getMessage());
        }
    }
    public ResponseEntity<?> queryDetail(Long reviewId) {
        try {
            System.out.println(reviewId);
            QueryWrapper<ExpReportReview> wrapper = new QueryWrapper<>();
            wrapper.eq("erw.id",reviewId);
            wrapper.eq("er.attachment_type",2);
            CommonExpReportReviewDTO detail=baseMapper.queryDetail(wrapper);
            return ResponseEntity.ok(
                    detail
            );
        }catch (Exception e){
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("服务器错误："+e.getMessage());
        }
    }

    private void applyRoleFilterForQuery(QueryWrapper<ExpReportReview> wrapper) throws Exception {
        if(authorityService.hasRole(AuthorityService.ROOT)){

        }
        else if (authorityService.hasRole(AuthorityService.ADMIN)){
            Long deptId=authorityService.selectUserRoleByUserAccountAndRoleCode().getDeptId();
            wrapper.eq("ec.dept_id",deptId);
        }else if(authorityService.hasRole(AuthorityService.TEACHER)){
            Long userId=authorityService.getSysUser().getId();
            wrapper.eq("u1.id",userId);
        }
        else if(authorityService.hasRole(AuthorityService.STUDENT)){
            Long userId=authorityService.getSysUser().getId();
            wrapper.eq("u2.id",userId);
        }
        else{
            throw new Exception("权限不足");
        }
    }
    private void applyRoleFilterForQueryReview(QueryWrapper<ExpProject> wrapper) throws Exception {
        if(authorityService.hasRole(AuthorityService.ROOT)){

        }
        else if (authorityService.hasRole(AuthorityService.ADMIN)){
            Long deptId=authorityService.selectUserRoleByUserAccountAndRoleCode().getDeptId();
            wrapper.eq("ec.dept_id",deptId);
        }else if(authorityService.hasRole(AuthorityService.TEACHER)){
            Long userId=authorityService.getSysUser().getId();
            wrapper.eq("u1.id",userId);
        }
        else if(authorityService.hasRole(AuthorityService.STUDENT)){
            Long userId=authorityService.getSysUser().getId();
            wrapper.eq("u2.id",userId);
        }
        else{
            throw new Exception("权限不足");
        }
    }
}
