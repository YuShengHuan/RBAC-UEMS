package com.ru.app.module.base.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ru.app.common.dto.CommonBaseClassDTO;
import com.ru.app.common.dto.CommonBaseMajorDTO;
import com.ru.app.common.entity.BaseDept;
import com.ru.app.common.entity.BaseMajor;
import com.ru.app.common.service.AuthorityService;
import com.ru.app.module.base.dto.AdminBaseClassQueryDTO;
import com.ru.app.module.base.mapper.AdminBaseClassMapper;
import com.ru.app.common.dto.SelectOptionDTO;
import com.ru.app.common.entity.BaseClass;
import com.ru.app.common.service.BaseDatabaseService;
import com.ru.app.module.base.mapper.AdminBaseMajorMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AdminBaseClassService extends BaseDatabaseService<AdminBaseClassMapper, BaseClass> {
    private final AuthorityService authorityService;
    private final AdminBaseMajorMapper adminBaseMajorMapper;
    AdminBaseClassService(
            AuthorityService authorityService,
            AdminBaseMajorMapper adminBaseMajorMapper
    ){
        this.authorityService=authorityService;
        this.adminBaseMajorMapper=adminBaseMajorMapper;
    }
    public ResponseEntity<?> selectOption(){
        try {
            QueryWrapper<BaseClass> wrapper = new QueryWrapper<>();

            applyRoleFilterForOption(wrapper);

            List<CommonBaseClassDTO> list=baseMapper.queryAll(wrapper);
            List<SelectOptionDTO> selectOptionDTOS=new ArrayList<>();
            for(CommonBaseClassDTO adminBaseClassDTO:list){
                 SelectOptionDTO selectOptionDTO=new SelectOptionDTO();
                 selectOptionDTO.setLabel(
                         adminBaseClassDTO.getClassName()+
                         "["+
                                 adminBaseClassDTO.getClassCode()+","+
                                 adminBaseClassDTO.getGrade()+","+
                                 adminBaseClassDTO.getDeptName()+","+
                                 adminBaseClassDTO.getMajorName()+
                         "]"
                 );
                 selectOptionDTO.setValue(
                         adminBaseClassDTO.getId().toString()
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
    public ResponseEntity<?> queryPage(AdminBaseClassQueryDTO queryDTO) {
        try {
            // 构建分页参数
            Page<CommonBaseClassDTO> page = new Page<>(queryDTO.getPageNum(), queryDTO.getPageSize());

            QueryWrapper<BaseClass> wrapper = new QueryWrapper<>();
            if(StringUtils.hasText(queryDTO.getClassCode())){
                wrapper.like("c.class_code",queryDTO.getClassCode());
            }
            if(StringUtils.hasText(queryDTO.getClassName())){
                wrapper.like("d.class_name",queryDTO.getClassName());
            }

            applyRoleFilterForQuery(wrapper);
            return ResponseEntity.ok(
                    baseMapper.queryPage(page, wrapper)
            );
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("服务器错误："+e.getMessage());
        }
    }
    public ResponseEntity<?> batchInsert(List<CommonBaseClassDTO> commonBaseClassDTOList) {
        try {
            if(authorityService.hasRole(AuthorityService.ADMIN)) {
                Long deptId = authorityService.selectUserRoleByUserAccountAndRoleCode().getDeptId();

                QueryWrapper<BaseMajor> baseMajorQueryWrapper=new QueryWrapper<>();
                baseMajorQueryWrapper.eq("m.dept_id",deptId);
                List<CommonBaseMajorDTO> ownCommonBaseMajorDTOList=adminBaseMajorMapper.queryAll(baseMajorQueryWrapper);

                QueryWrapper<BaseClass> baseClassQueryWrapper = new QueryWrapper<>();
                baseClassQueryWrapper.eq("m.dept_id", deptId);
                List<CommonBaseClassDTO> dbDTOList = baseMapper.queryAll(baseClassQueryWrapper);


                //以ClassName为主键，先排查存在的，不存在的放一边
                List<CommonBaseClassDTO> noExitInDb = commonBaseClassDTOList.stream().filter(
                        item -> dbDTOList.stream().noneMatch(
                                w -> w.getClassName().equals(item.getClassName())
                        )
                ).toList();
                List<CommonBaseClassDTO> exitInDb = commonBaseClassDTOList.stream().filter(
                        item -> dbDTOList.stream().anyMatch(
                                w -> w.getClassName().equals(item.getClassName())
                        )
                ).toList();

                List<BaseClass> saveBaseMajorList = noExitInDb.stream().map(
                        item -> {

                            if(StringUtils.hasText(item.getMajorName())){
                                Optional<CommonBaseMajorDTO>
                                        commonBaseMajorDTO=ownCommonBaseMajorDTOList.stream().filter(
                                                m->m.getMajorName().equals(item.getMajorName())
                                ).findFirst();
                                commonBaseMajorDTO.ifPresent(dto->item.setMajorId(dto.getId()));
                            }
                            BaseClass baseClass = new BaseClass();
                            BeanUtils.copyProperties(item, baseClass);
                            return baseClass;
                        }
                ).toList();
                boolean status = saveBatch(saveBaseMajorList);
                if (status) {
                    return ResponseEntity.ok().body("插入成功");
                } else {
                    return ResponseEntity.badRequest().body("插入失败");
                }
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("没得权限干这事");
        }catch (Exception e){
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("服务器错误");
        }
    }
    private void applyRoleFilterForQuery(QueryWrapper<BaseClass> wrapper) throws Exception {
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
    public void applyRoleFilterForOption(QueryWrapper<BaseClass> wrapper) throws Exception {
        if(authorityService.hasRole(AuthorityService.ROOT)){

        }
        else if (authorityService.hasRole(AuthorityService.ADMIN)){
             Long deptId=authorityService.selectUserRoleByUserAccountAndRoleCode().getDeptId();
             wrapper.eq("d.id",deptId);
        }else if (authorityService.hasRole(AuthorityService.TEACHER)){
             Long deptId=authorityService.getSysUser().getDeptId();
             wrapper.eq("d.id",deptId);
        }else if (authorityService.hasRole(AuthorityService.STUDENT)){

        }
        else{
            throw new Exception("权限不足");
        }
    }
}
