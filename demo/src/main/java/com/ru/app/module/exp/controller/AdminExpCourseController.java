package com.ru.app.module.exp.controller;
import com.ru.app.common.entity.ExpLab;
import com.ru.app.module.exp.dto.AdminExpCourseQueryDTO;
import com.ru.app.module.exp.service.AdminExpCourseService;
import com.ru.app.common.entity.ExpCourse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/course")
public class AdminExpCourseController {
    private final AdminExpCourseService adminExpCourseService;

    public AdminExpCourseController(AdminExpCourseService adminExpCourseService) {
        this.adminExpCourseService=adminExpCourseService;
    }
    @GetMapping("/select")
    public ResponseEntity<?>select(){
        return adminExpCourseService.selectOption();
    }
    @PostMapping("/page")
    public ResponseEntity<?> page(@RequestBody AdminExpCourseQueryDTO queryDTO){
        return adminExpCourseService.queryPage(queryDTO);
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id){
        return adminExpCourseService.delete(id);
    }
    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody ExpCourse expCourse){
        return adminExpCourseService.create(expCourse);
    }
    @PutMapping("/update")
    public ResponseEntity<?> update(@RequestBody ExpCourse expCourse){
        return adminExpCourseService.update(expCourse);
    }
    @PostMapping("/batch-insert")
    public ResponseEntity<?> batchInset(@RequestBody List<ExpCourse> expCourseList){
        return adminExpCourseService.batchInsert(expCourseList);
    }
    @DeleteMapping("/batch-delete")
    public ResponseEntity<?> delete(@RequestBody List<Long> ids){
        return adminExpCourseService.batchDelete(ids);
    }
}
