package com.ru.app.module.exp.controller;
import com.ru.app.module.exp.dto.AdminExpTeachingCoreQueryDTO;
import com.ru.app.module.exp.service.AdminExpTeachingCoreService;
import com.ru.app.common.entity.ExpTeachingCore;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/teaching-core")
public class AdminExpTeachingCoreController {
    private final AdminExpTeachingCoreService adminExpTeachingCoreService;

    public AdminExpTeachingCoreController(AdminExpTeachingCoreService adminExpTeachingCoreService) {
        this.adminExpTeachingCoreService=adminExpTeachingCoreService;
    }
    @GetMapping("/select")
    public ResponseEntity<?>select(
            @RequestParam(
                    value = "semester",
                    required = false
            ) String semester
    ){
        return adminExpTeachingCoreService.selectOption(semester);
    }
    @PostMapping("/page")
    public ResponseEntity<?> page(@RequestBody AdminExpTeachingCoreQueryDTO queryDTO){
        return adminExpTeachingCoreService.queryPage(queryDTO);
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id){
        return adminExpTeachingCoreService.delete(id);
    }
    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody ExpTeachingCore expTeachingCore){
        return adminExpTeachingCoreService.create(expTeachingCore);
    }
    @PutMapping("/update")
    public ResponseEntity<?> update(@RequestBody ExpTeachingCore expTeachingCore){
        return adminExpTeachingCoreService.update(expTeachingCore);
    }
    @GetMapping("/detail/{teachingCoreId}")
    public ResponseEntity<?> detail(@PathVariable Long teachingCoreId){
        return adminExpTeachingCoreService.queryDetail(teachingCoreId);
    }
}
