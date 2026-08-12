package com.ru.app.module.exp.service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ru.app.module.converter.service.ConverterService;
import com.ru.app.module.exp.dto.*;
import com.ru.app.module.exp.mapper.AdminExpReportMapper;
import com.ru.app.module.plagiarism.service.PlagiarismCheckService;
import com.ru.app.common.dto.CommonExpReportDTO;
import com.ru.app.common.dto.CommonExpReportUploadDTO;
import com.ru.app.common.dto.SelectOptionDTO;
import com.ru.app.common.entity.ExpProject;
import com.ru.app.common.entity.ExpReport;
import com.ru.app.common.service.AuthorityService;
import com.ru.app.common.service.BaseDatabaseService;
import com.ru.app.common.service.FileService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.method.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.io.*;
import java.net.URLEncoder;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AdminExpReportService extends BaseDatabaseService<AdminExpReportMapper, ExpReport> {
    private final FileService fileService;
    private final AuthorityService authorityService;
    private final ConverterService converterService;
    private final PlagiarismCheckService plagiarismCheckService;
    AdminExpReportService(
            AuthorityService authorityService,
            FileService fileService,
            ConverterService converterService,
            PlagiarismCheckService plagiarismCheckService
    ){
        this.authorityService=authorityService;
        this.converterService=converterService;
        this.fileService=fileService;
        this.plagiarismCheckService=plagiarismCheckService;
    }
    public ResponseEntity<?> queryReportStatus(FrontExpReportQueryDTO queryDTO,boolean isSubmitted) {
        try {
            // 构建分页参数
            Page<FrontReportSubmitStatusDTO> page = new Page<>(queryDTO.getPageNum(), queryDTO.getPageSize());
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
            if(isSubmitted){
                wrapper.isNotNull("er.id");
            }
            else{
                wrapper.isNull("er.id");
            }
            applyRoleFilterForQuerySubmit(wrapper);
            return ResponseEntity.ok(
                    baseMapper.queryReportStatus(page, wrapper)
            );
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("服务器错误："+e.getMessage());
        }
    }
    public ResponseEntity<?> exportReportSubmitToZip(HttpServletResponse response,FrontExpReportQueryDTO queryDTO) {
        try {
            System.out.println("接收压缩请求");
            Page<FrontReportSubmitStatusDTO> page = new Page<>(queryDTO.getPageNum(), queryDTO.getPageSize());
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
            wrapper.isNotNull("er.id");

            Path tempDir = Files.createTempDirectory("fileToZip-");

            List<File> files = baseMapper.queryReportStatus(page, wrapper)
                    .getRecords()
                    .stream()
                    // 过滤空路径/空白路径
                    .filter(item -> StringUtils.hasText(item.getFilePath()))
                    .map(item -> {
                        try {
                            String filePathStr = item.getFilePath().trim();
                            String realPath = fileService.urlToRealPath(filePathStr);

                            // 空值兜底：真实路径无效则返回null
                            if (!StringUtils.hasText(realPath)) {
                                return null;
                            }

                            Path originalPath = Paths.get(realPath);
                            // 过滤不存在的原文件
                            if (!Files.exists(originalPath) || Files.isDirectory(originalPath)) {
                                System.err.println("原文件不存在或为目录：" + realPath);
                                return null;
                            }

                            // ========== 核心：生成新文件名（避免重复） ==========
                            // 原文件名（如：report.pdf）
                            String originalFileName = originalPath.getFileName().toString();
                            // 拆分文件名和后缀（处理无后缀的情况）
                            String fileNameSuffix = "";
                            int suffixIndex = originalFileName.lastIndexOf(".");
                            if (suffixIndex > 0) {
                                fileNameSuffix = originalFileName.substring(suffixIndex); // 包含"."，如：.pdf
                            } else {
                                return null;
                            }
                            // 新文件名规则：原前缀 + UUID + 原后缀（保证唯一，避免冲突）
                            String newFileName = item.getClassName()+"_"+item.getCourseName()+"_"+item.getProjectName()+"_"+item.getUploadRealName() + fileNameSuffix;

                            // ========== 核心：复制文件到临时目录 ==========
                            Path newFilePath = tempDir.resolve(newFileName); // 临时目录 + 新文件名
                            // 复制原文件到临时目录（覆盖已存在的同名文件）
                            Files.copy(originalPath, newFilePath, StandardCopyOption.REPLACE_EXISTING);

                            // 注册新文件JVM退出时删除（可选：增强清理）
                            newFilePath.toFile().deleteOnExit();

                            // 返回临时目录下的新文件
                            return newFilePath.toFile();

                        } catch (Exception e) {
                            // 单个文件处理失败不中断整体流程，仅打印日志+返回null
                            System.err.println("文件复制/重命名失败：" + item.getFilePath() + "，原因：" + e.getMessage());
                            return null;
                        }
                    }).collect(Collectors.toList());


            // 2. 创建临时源文件
            String sourceFileName = UUID.randomUUID() + ".zip"; // 后缀名不影响
            Path sourceFilePath = Paths.get(tempDir.toString(), sourceFileName);
            File sourceFile = sourceFilePath.toFile();
            try {
                // 2. 执行文件压缩
                fileService.compressFilesToZip(files, sourceFile);

                // 将文件写入输入流
                FileInputStream inputStream = new FileInputStream(sourceFile);
                byte[] data = new byte[inputStream.available()];
                inputStream.read(data);

                // 清空 response
                response.reset();
                response.setCharacterEncoding("UTF-8");

                // Content-Disposition的作用：告知浏览器以何种方式显示响应返回的文件，用浏览器打开还是以附件的形式下载到本地保存
                // attachment表示以附件方式下载   inline表示在线打开   "Content-Disposition: inline; filename=文件名.mp3"
                // filename表示文件的默认名称，因为网络传输只支持URL编码的相关支付，因此需要将文件名URL编码后进行传输,前端收到后需要反编码才能获取到真正的名称
                response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(sourceFile.getName(), StandardCharsets.UTF_8));
                // 告知浏览器文件的大小
                response.addHeader("Content-Length", "" + sourceFile.length());
                response.setContentType("application/octet-stream");
                OutputStream os = response.getOutputStream();

                os.write(data);
                //先声明的流后关掉！
                os.flush();
                os.close();
                inputStream.close();

                return ResponseEntity.ok("ok");

            } catch (Exception e) {
                throw new RuntimeException("文件压缩失败：" + e.getMessage(), e);
            } finally {
                Files.deleteIfExists(tempDir);
            }
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("服务器错误："+e.getMessage());
        }
    }

    public ResponseEntity<?> queryTemplate(FrontExpReportTemplateQueryDTO queryDTO) {
        try {
            // 构建分页参数
            Page<CommonExpReportDTO> page = new Page<>(queryDTO.getPageNum(), queryDTO.getPageSize());

            QueryWrapper<ExpReport> wrapper = new QueryWrapper<>();
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
                        .like("u.real_name", searchContent)
                );
            }
            wrapper.eq("er.attachment_type",1);

            applyRoleFilterForQuery(wrapper);

            return ResponseEntity.ok(
                    baseMapper.queryPage(page, wrapper)
            );
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("服务器错误："+e.getMessage());
        }
    }
    public ResponseEntity<?> selectOption(){
        try {
            QueryWrapper<ExpReport> wrapper = new QueryWrapper<>();

            applyRoleFilterForOption(wrapper);

            List<CommonExpReportDTO> list=baseMapper.queryAll(wrapper);
            List<SelectOptionDTO> selectOptionDTOS=new ArrayList<>();
            for(CommonExpReportDTO adminExpReportDTO:list){
                SelectOptionDTO selectOptionDTO=new SelectOptionDTO();
                selectOptionDTO.setLabel(
                        adminExpReportDTO.getProjectName()+
                                "["+
                                adminExpReportDTO.getSemester()+","+
                                adminExpReportDTO.getCourseName()+","+
                                adminExpReportDTO.getClassName()+","+
                                adminExpReportDTO.getUploadRealName()+
                                "]"
                );
                selectOptionDTO.setValue(
                        adminExpReportDTO.getId().toString()
                );
                selectOptionDTOS.add(selectOptionDTO);
            }
            return ResponseEntity.ok(
                    selectOptionDTOS
            );
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("服务器错误");
        }
    }
    public ResponseEntity<?> queryPage(AdminExpReportQueryDTO queryDTO) {
        try {
            // 构建分页参数
            Page<CommonExpReportDTO> page = new Page<>(queryDTO.getPageNum(), queryDTO.getPageSize());

            QueryWrapper<ExpReport> wrapper = new QueryWrapper<>();
            if(queryDTO.getAttachmentType()!=null){
                wrapper.eq("er.attachment_type",queryDTO.getAttachmentType());
            }
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
                wrapper.like("u.real_name",queryDTO.getUploadRealName());
            }
            if(queryDTO.getUploadUserType()!=null){
                wrapper.like("u.user_type",queryDTO.getUploadUserType());
            }

            applyRoleFilterForQuery(wrapper);
            return ResponseEntity.ok(
                    baseMapper.queryPage(page, wrapper)
            );
        }catch (Exception e){
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("服务器错误："+e.getMessage());
        }
    }
    @Transactional
    public ResponseEntity<?> create(CommonExpReportUploadDTO eu) throws Exception {
        try {
            ExpReport expReport=eu.toEntity();
            if(eu.getFile()!=null){
                String filePath = fileService.saveFile(eu.getFile(),eu.getFilePath().split("\\.")[0]);
                expReport.setFilePath(filePath);
                if(save(expReport)){
                    return ResponseEntity.status(HttpStatus.CREATED).body("创建成功");
                }
            }
            return ResponseEntity.badRequest().body("创建失败");
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("服务器错误："+e.getMessage());
        }
    }

    /**
     * 更新实验报告
     */
    @Transactional
    public ResponseEntity<?> update(CommonExpReportUploadDTO eu) throws Exception {
        try {
            ExpReport expReport=getById(eu.getId());
            if(eu.getId()==null){
                return ResponseEntity.badRequest().body("更新失败");
            }
            if(eu.getFile()!=null&&fileService.deleteFile(expReport.getFilePath())){
                String filePath = fileService.saveFile(eu.getFile(),eu.getFilePath().split("\\.")[0]);
                expReport.setFilePath(filePath);
            }
            if(updateById(expReport)){
                return ResponseEntity.ok("更新成功");
            }
            return ResponseEntity.badRequest().body("更新失败");
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("服务器错误："+e.getMessage());
        }

    }

    /**
     * 删除实验报告
     */
    @Transactional
    public ResponseEntity<?> delete(Long id) {
        try{
            ExpReport expReport=getById(id);
            // 1. 校验ID是否存在
            if (id == null ||  expReport== null) {
                return ResponseEntity.badRequest().body("实验报告不存在");
            }
            if(removeById(id)&&fileService.deleteFile(expReport.getFilePath())){
                return ResponseEntity.ok("删除成功");
            }
            return ResponseEntity.badRequest().body("删除失败");
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("服务器错误："+e.getMessage());
        }
    }

    public ResponseEntity<?> queryDetail(Long reportId) {
        try {
            QueryWrapper<ExpReport> wrapper = new QueryWrapper<>();
            wrapper.eq("er.id",reportId);
            CommonExpReportDTO detail=baseMapper.queryDetail(wrapper);
            return ResponseEntity.ok(
                    detail
            );
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("服务器错误："+e.getMessage());
        }
    }
    public ResponseEntity<?> convertWordToPdf(Long reportId) {
        // 1. 校验文件是否为空
        if (reportId==null) {
            return ResponseEntity.badRequest().body("报告id不能为空");
        }
        ExpReport expReport=getById(reportId);
        if (expReport == null) {
            return ResponseEntity.badRequest().body("报告不存在");
        }
        String fileRealPath= fileService.urlToRealPath(expReport.getFilePath());
        File file=new File(fileRealPath);
        if(file.exists()){
            System.out.println(file.getAbsolutePath());
        }
        try(InputStream is = new FileInputStream(file)) {
            // 2. 调用服务层进行转换
            byte[] pdfBytes = converterService.convertWordToPdfBytes(is);

            // 3. 构建响应头，通知浏览器这是一个可下载的 PDF 文件
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "converted_report.pdf");
            headers.setContentLength(pdfBytes.length);

            // 4. 返回 PDF 文件流
            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("文件处理失败".getBytes());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage().getBytes());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("文件转换失败，请稍后重试".getBytes());
        }
    }
    public ResponseEntity<?> plagiarism(Long reportId){
        try {
            return ResponseEntity.ok(
                    plagiarismCheckService.checkPlagiarism(reportId)
            );
        }catch (Exception exception){
            return ResponseEntity.badRequest().body("结果异常");
        }
    }
    private void applyRoleFilterForQuery(QueryWrapper<ExpReport> wrapper) throws Exception {
        if(authorityService.hasRole(AuthorityService.ROOT)){

        }
        else if (authorityService.hasRole(AuthorityService.ADMIN)){
            Long deptId=authorityService.selectUserRoleByUserAccountAndRoleCode().getDeptId();
            wrapper.eq("ec.dept_id",deptId);
        }else if(authorityService.hasRole(AuthorityService.TEACHER)){
            Long userId=authorityService.getSysUser().getId();
            wrapper.eq("u.id",userId);
        }
        else if(authorityService.hasRole(AuthorityService.STUDENT)){
            Long classId=authorityService.getSysUser().getClassId();
            wrapper.eq("c.id",classId);
        }
        else{
            throw new Exception("权限不足");
        }
    }
    private void applyRoleFilterForQuerySubmit(QueryWrapper<ExpProject> wrapper) throws Exception {
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
    private void applyRoleFilterForOption(QueryWrapper<ExpReport> wrapper) throws Exception {
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
