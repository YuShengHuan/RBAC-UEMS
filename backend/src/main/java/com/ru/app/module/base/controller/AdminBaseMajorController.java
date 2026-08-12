package com.ru.app.module.base.controller;

import com.ru.app.common.dto.CommonBaseClassDTO;
import com.ru.app.common.dto.CommonBaseMajorDTO;
import com.ru.app.module.base.dto.AdminBaseMajorQueryDTO;
import com.ru.app.module.base.service.AdminBaseMajorService;
import com.ru.app.common.entity.BaseMajor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/major")
public class AdminBaseMajorController {
    private final AdminBaseMajorService adminBaseMajorService;

    public AdminBaseMajorController(AdminBaseMajorService adminBaseMajorService) {
        this.adminBaseMajorService=adminBaseMajorService;
    }
    @GetMapping("/select")
    public ResponseEntity<?>select(){
        return adminBaseMajorService.selectOption();
    }
    @PostMapping("/page")
    public ResponseEntity<?> page(@RequestBody AdminBaseMajorQueryDTO queryDTO){
        return adminBaseMajorService.queryPage(queryDTO);
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id){
        return adminBaseMajorService.delete(id);
    }
    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody BaseMajor baseMajor){
        return adminBaseMajorService.create(baseMajor);
    }
    @PutMapping("/update")
    public ResponseEntity<?> update(@RequestBody BaseMajor baseMajor){
        return adminBaseMajorService.update(baseMajor);
    }
    @PostMapping("/batch-insert")
    public ResponseEntity<?> batchInset(@RequestBody List<CommonBaseMajorDTO> commonBaseMajorDTOList){
        return adminBaseMajorService.batchInsert(commonBaseMajorDTOList);
    }
    @DeleteMapping("/batch-delete")
    public ResponseEntity<?> delete(@RequestBody List<Long> ids){
        return adminBaseMajorService.batchDelete(ids);
    }
}
