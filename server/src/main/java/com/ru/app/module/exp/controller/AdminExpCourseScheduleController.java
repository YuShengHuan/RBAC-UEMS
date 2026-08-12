package com.ru.app.module.exp.controller;
import com.ru.app.common.dto.CommonExpCourseScheduleDTO;
import com.ru.app.common.entity.ExpLab;
import com.ru.app.module.exp.dto.AdminExpCourseScheduleQueryDTO;
import com.ru.app.module.exp.service.AdminExpCourseScheduleService;
import com.ru.app.common.entity.ExpCourseSchedule;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/course-schedule")
public class AdminExpCourseScheduleController {
    private final AdminExpCourseScheduleService adminExpCourseScheduleService;

    public AdminExpCourseScheduleController(AdminExpCourseScheduleService adminExpCourseScheduleService) {
        this.adminExpCourseScheduleService=adminExpCourseScheduleService;
    }
    @GetMapping("/select")
    public ResponseEntity<?>select(){
        return adminExpCourseScheduleService.selectOption();
    }
    @PostMapping("/page")
    public ResponseEntity<?> page(@RequestBody AdminExpCourseScheduleQueryDTO queryDTO){
        return adminExpCourseScheduleService.queryPage(queryDTO);
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id){
        return adminExpCourseScheduleService.delete(id);
    }
    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody ExpCourseSchedule expCourseSchedule){
        return adminExpCourseScheduleService.create(expCourseSchedule);
    }
    @PutMapping("/update")
    public ResponseEntity<?> update(@RequestBody ExpCourseSchedule expCourseSchedule){
        return adminExpCourseScheduleService.update(expCourseSchedule);
    }
    @GetMapping("/detail/{scheduleId}")
    public ResponseEntity<?> detail(@PathVariable Long scheduleId){
        return adminExpCourseScheduleService.queryDetail(scheduleId);
    }
    @PostMapping("/batch-insert")
    public ResponseEntity<?> batchInset(@RequestBody List<CommonExpCourseScheduleDTO> commonExpCourseScheduleDTOList){
        return adminExpCourseScheduleService.batchInsert(commonExpCourseScheduleDTOList);
    }
}
