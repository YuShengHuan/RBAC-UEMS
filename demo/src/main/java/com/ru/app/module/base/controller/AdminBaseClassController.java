package com.ru.app.module.base.controller;

import com.ru.app.common.dto.CommonBaseClassDTO;
import com.ru.app.common.entity.BaseDept;
import com.ru.app.module.base.dto.AdminBaseClassQueryDTO;
import com.ru.app.module.base.service.AdminBaseClassService;
import com.ru.app.common.entity.BaseClass;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/class")
public class AdminBaseClassController {
    private final AdminBaseClassService adminBaseClassService;

    public AdminBaseClassController(AdminBaseClassService adminBaseClassService) {
        this.adminBaseClassService=adminBaseClassService;
    }
    @GetMapping("/select")
    public ResponseEntity<?>select(){
        return adminBaseClassService.selectOption();
    }
    @PostMapping("/page")
    public ResponseEntity<?> page(@RequestBody AdminBaseClassQueryDTO queryDTO){
        return adminBaseClassService.queryPage(queryDTO);
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id){
        return adminBaseClassService.delete(id);
    }
    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody BaseClass baseClass){
        return adminBaseClassService.create(baseClass);
    }
    @PutMapping("/update")
    public ResponseEntity<?> update(@RequestBody BaseClass baseClass){
        return adminBaseClassService.update(baseClass);
    }
    @PostMapping("/batch-insert")
    public ResponseEntity<?> batchInset(@RequestBody List<CommonBaseClassDTO> commonBaseClassDTOList){
        return adminBaseClassService.batchInsert(commonBaseClassDTOList);
    }
    @DeleteMapping("/batch-delete")
    public ResponseEntity<?> delete(@RequestBody List<Long> ids){
        return adminBaseClassService.batchDelete(ids);
    }

}
