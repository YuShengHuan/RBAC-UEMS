package com.ru.app.module.exp.controller;
import com.ru.app.common.dto.CommonExpCourseScheduleDTO;
import com.ru.app.common.dto.CommonExpProjectDTO;
import com.ru.app.module.exp.dto.AdminExpProjectQueryDTO;
import com.ru.app.module.exp.service.AdminExpProjectService;
import com.ru.app.common.entity.ExpProject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/project")
public class AdminExpProjectController {
    private final AdminExpProjectService adminExpProjectService;

    public AdminExpProjectController(AdminExpProjectService adminExpProjectService) {
        this.adminExpProjectService=adminExpProjectService;
    }
    @GetMapping("/select")
    public ResponseEntity<?>select(){
        return adminExpProjectService.selectOption();
    }
    @PostMapping("/page")
    public ResponseEntity<?> page(@RequestBody AdminExpProjectQueryDTO queryDTO){
        return adminExpProjectService.queryPage(queryDTO);
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id){
        return adminExpProjectService.delete(id);
    }
    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody ExpProject project){
        return adminExpProjectService.create(project);
    }
    @PutMapping("/update")
    public ResponseEntity<?> update(@RequestBody ExpProject project){
        return adminExpProjectService.update(project);
    }
    @GetMapping("/detail/{projectId}")
    public ResponseEntity<?> detail(@PathVariable Long projectId){
        return adminExpProjectService.queryDetail(projectId);
    }
    @PostMapping("/batch-insert")
    public ResponseEntity<?> batchInset(@RequestBody List<CommonExpProjectDTO> commonExpProjectDTOList){
        return adminExpProjectService.batchInsert(commonExpProjectDTOList);
    }
}
