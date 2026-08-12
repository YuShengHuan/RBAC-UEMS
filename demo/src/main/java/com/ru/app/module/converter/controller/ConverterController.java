package com.ru.app.module.converter.controller;

import com.ru.app.module.converter.service.ConverterService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
@Controller
@RequestMapping("/api/converter")
public class ConverterController {
    private final ConverterService converterService;
    ConverterController(ConverterService converterService){
        this.converterService=converterService;
    }
    @PostMapping(value = "/word-to-pdf", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> convertWordToPdf(@RequestParam("correctionReport") MultipartFile correctionReport) {
       return converterService.convertWordToPdf(correctionReport);
    }
}
