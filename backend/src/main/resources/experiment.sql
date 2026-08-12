-- 1. 学院表
CREATE TABLE IF NOT EXISTS `base_dept`
(
    `id`        BIGINT       NOT NULL AUTO_INCREMENT COMMENT '学院ID ',
    `dept_code` VARCHAR(50) COMMENT ' 学院编码 ',
    `dept_name` VARCHAR(100) NOT NULL COMMENT ' 学院名称 ',
    `create_at` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT ' 创建时间 ',
    `update_at` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT ' 更新时间 ',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_dept_code` (`dept_code`),
    UNIQUE KEY `uk_dept_name` (`dept_name`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='基础学院表';
-- 2. 专业表
CREATE TABLE IF NOT EXISTS `base_major`
(
    `id`         BIGINT       NOT NULL AUTO_INCREMENT COMMENT ' 专业ID ',
    `major_code` VARCHAR(50) COMMENT ' 专业编码 ',
    `major_name` VARCHAR(100) NOT NULL COMMENT ' 专业名称 ',
    `dept_id`    BIGINT       NOT NULL COMMENT ' 所属学院ID ',
    `create_at`  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT ' 创建时间 ',
    `update_at`  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT ' 更新时间 ',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_major_code` (`major_code`),
    KEY `idx_dept_id` (`dept_id`),
    CONSTRAINT `fk_base_major_dept` FOREIGN KEY (`dept_id`) REFERENCES `base_dept` (`id`) ON DELETE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='基础专业表';
-- 3. 班级表
CREATE TABLE IF NOT EXISTS `base_class`
(
    `id`            BIGINT       NOT NULL AUTO_INCREMENT COMMENT ' 班级ID ',
    `class_code`    VARCHAR(50) COMMENT ' 班级编码 ',
    `class_name`    VARCHAR(100) NOT NULL COMMENT ' 班级名称 ',
    `major_id`      BIGINT       NOT NULL COMMENT ' 所属专业ID ',
    `grade`         INT          NOT NULL COMMENT ' 年级 ',
    `student_count` INT                   DEFAULT 0 COMMENT ' 班级人数 ',
    `create_at`     DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT ' 创建时间 ',
    `update_at`     DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT ' 更新时间 ',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_class_code` (`class_code`),
    KEY `idx_major_id` (`major_id`),
    KEY `idx_grade` (`grade`),
    CONSTRAINT `fk_base_class_major` FOREIGN KEY (`major_id`) REFERENCES `base_major` (`id`) ON DELETE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='基础班级表';
-- 一、系统权限模块
-- 1. 系统用户表
CREATE TABLE IF NOT EXISTS `sys_user`
(
    `id`            BIGINT       NOT NULL AUTO_INCREMENT COMMENT ' 用户ID ',
    `user_account`  VARCHAR(50)  NOT NULL COMMENT ' 账号 ',
    `user_password` VARCHAR(100) NOT NULL COMMENT ' 加密密码 ',
    `real_name`     VARCHAR(50)  NOT NULL COMMENT ' 真实姓名 ',
    `gender`        TINYINT COMMENT ' 性别 ',
    `phone`         VARCHAR(20) COMMENT ' 联系电话 ',
    `email`         VARCHAR(100) COMMENT ' 电子邮箱 ',
    `user_type`     TINYINT      COMMENT ' 用户类型 ',
    `dept_id`       BIGINT COMMENT ' 所属学院ID ',
    `class_id`      BIGINT COMMENT ' 所属班级ID ',
    `user_status`   TINYINT               DEFAULT 1 COMMENT ' 状态 ',
    `create_at`     DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT ' 创建时间 ',
    `update_at`     DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT ' 更新时间 ',
    `remark`        VARCHAR(500)          DEFAULT NULL COMMENT ' 备注 ',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_account` (`user_account`),
    KEY `idx_user_type_status` (`user_type`, `user_status`),
    KEY `idx_dept_id` (`dept_id`),
    KEY `idx_class_id` (`class_id`),
    CONSTRAINT `fk_sys_user_dept` FOREIGN KEY (`dept_id`) REFERENCES `base_dept` (`id`) ON DELETE SET NULL,
    CONSTRAINT `fk_sys_user_class` FOREIGN KEY (`class_id`) REFERENCES `base_class` (`id`) ON DELETE SET NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='系统用户表';
-- 2. 系统角色表
CREATE TABLE IF NOT EXISTS `sys_role`
(
    `id`         BIGINT       NOT NULL AUTO_INCREMENT COMMENT ' 角色ID ',
    `role_code`  VARCHAR(50) COMMENT ' 角色编码 ',
    `role_group` VARCHAR(100) NOT NULL COMMENT ' 角色分组 ',
    `role_name`  VARCHAR(50)  NOT NULL COMMENT ' 角色名称 ',
    `create_at`  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT ' 创建时间 ',
    `update_at`  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT ' 更新时间 ',
    `remark`     VARCHAR(500)          DEFAULT NULL COMMENT ' 备注 ',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_role_code` (`role_code`),
    UNIQUE KEY `uk_role_name` (`role_name`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='系统角色表';
-- 3. 用户-角色关联表
CREATE TABLE IF NOT EXISTS `sys_user_role`
(
    `id`        BIGINT   NOT NULL AUTO_INCREMENT COMMENT ' 关联ID ',
    `user_id`   BIGINT   NOT NULL COMMENT ' 用户ID ',
    `role_id`   BIGINT   NOT NULL COMMENT ' 角色ID ',
    `dept_id`   BIGINT COMMENT ' 分院ID只对分院管理员有效 ',
    `create_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT ' 创建时间 ',
    `update_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT ' 更新时间 ',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_role` (`user_id`, `role_id`),
    KEY `idx_role_id` (`role_id`),
    CONSTRAINT `fk_sys_user_role_user` FOREIGN KEY (`user_id`) REFERENCES `sys_user` (`id`) ON DELETE CASCADE,
    CONSTRAINT `fk_sys_user_role_role` FOREIGN KEY (`role_id`) REFERENCES `sys_role` (`id`) ON DELETE CASCADE,
    CONSTRAINT `fk_sys_user_role_dept` FOREIGN KEY (`dept_id`) REFERENCES `base_dept` (`id`) ON DELETE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='用户-角色关联表';
-- 4. 系统权限表
CREATE TABLE IF NOT EXISTS `sys_permission`
(
    `id`          BIGINT       NOT NULL AUTO_INCREMENT COMMENT ' 权限ID ',
    `parent_code` VARCHAR(100) COMMENT ' 父权限编码 ',
    `perm_code`   VARCHAR(100) COMMENT ' 权限编码 ',
    `perm_group`  VARCHAR(100) NOT NULL COMMENT ' 权限分组 ',
    `perm_name`   VARCHAR(100) NOT NULL COMMENT ' 权限名称 ',
    `perm_type`   TINYINT      NOT NULL COMMENT ' 权限类型 ',
    `request_uri` VARCHAR(500)          DEFAULT NULL COMMENT ' 权限对应请求路径 ',
    `perm_status` TINYINT      NOT NULL DEFAULT 1 COMMENT ' 权限状态 ',
    `create_at`   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT ' 创建时间 ',
    `update_at`   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT ' 更新时间 ',
    `remark`      VARCHAR(500)          DEFAULT NULL COMMENT ' 备注 ',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_perm_code` (`perm_code`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='系统权限表';
-- 5. 角色-权限关联表
CREATE TABLE IF NOT EXISTS `sys_role_permission`
(
    `id`        BIGINT   NOT NULL AUTO_INCREMENT COMMENT ' 关联ID ',
    `role_id`   BIGINT   NOT NULL COMMENT ' 角色ID ',
    `perm_id`   BIGINT   NOT NULL COMMENT ' 权限ID ',
    `create_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT ' 创建时间 ',
    `update_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT ' 更新时间 ',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_role_perm` (`role_id`, `perm_id`),
    KEY `idx_perm_id` (`perm_id`),
    CONSTRAINT `fk_sys_role_perm_role` FOREIGN KEY (`role_id`) REFERENCES `sys_role` (`id`) ON DELETE CASCADE,
    CONSTRAINT `fk_sys_role_perm_perm` FOREIGN KEY (`perm_id`) REFERENCES `sys_permission` (`id`) ON DELETE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='角色-权限关联表';
-- 6. 系统字典表
CREATE TABLE IF NOT EXISTS `sys_dict`
(
    `id`         BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT ' 字典ID ',
    `dict_group` VARCHAR(100)    NOT NULL COMMENT ' 字典分组 ',
    `dict_key`   VARCHAR(50)     NOT NULL COMMENT ' 字典键 ',
    `dict_value` VARCHAR(255)    NOT NULL COMMENT ' 字典值 ',
    `remark`     VARCHAR(255) COMMENT ' 描述 ',
    `create_at`  DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT ' 创建时间 ',
    `update_at`  DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT ' 更新时间 ',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_dict_group_key` (`dict_group`, `dict_key`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='系统通用字典表';
-- 7. 系统通知表
CREATE TABLE IF NOT EXISTS `sys_notice`
(
    `id`             BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT ' 通知ID ',
    `notice_title`   VARCHAR(255)    NOT NULL COMMENT ' 通知标题 ',
    `notice_content` TEXT            NOT NULL COMMENT ' 通知内容 ',
    `notice_type`    TINYINT         NOT NULL COMMENT ' 类型 ',
    `sender_id`      BIGINT UNSIGNED NULL COMMENT ' 发送者ID ',
    `target_id`      BIGINT UNSIGNED NULL COMMENT ' 目标ID ',
    `notice_status`  TINYINT                  DEFAULT 1 COMMENT ' 状态 ',
    `create_at`      DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT ' 创建时间 ',
    `update_at`      DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT ' 更新时间 ',
    PRIMARY KEY (`id`),
    KEY `idx_notice_type_target` (`notice_type`, `target_id`),
    KEY `idx_create_at` (`create_at`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='系统通知表';
-- 8. 通知确认表
CREATE TABLE IF NOT EXISTS `sys_notice_confirm`
(
    `id`        BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT ' 确认ID ',
    `notice_id` BIGINT UNSIGNED NOT NULL COMMENT ' 通知ID ',
    `user_id`   BIGINT          NOT NULL COMMENT ' 用户ID ',
    `create_at` DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT ' 创建时间 ',
    `update_at` DATETIME                 DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT ' 更新时间 ',
    UNIQUE KEY `uk_notice_user` (`notice_id`, `user_id`),
    CONSTRAINT `fk_sys_notice_confirm_notice` FOREIGN KEY (`notice_id`) REFERENCES `sys_notice` (`id`) ON DELETE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='通知确认表';

-- 1. 实验业务-实验室表
CREATE TABLE IF NOT EXISTS `exp_lab`
(
    `id`           BIGINT       NOT NULL AUTO_INCREMENT COMMENT ' 实验室ID ',
    `lab_code`     VARCHAR(50) COMMENT ' 实验室编码 ',
    `lab_name`     VARCHAR(100) NOT NULL COMMENT ' 实验室名称 ',
    `lab_location` VARCHAR(200) NOT NULL COMMENT ' 位置 ',
    `dept_id`      BIGINT       NOT NULL COMMENT ' 所属学院ID ',
    `create_at`    DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT ' 创建时间 ',
    `update_at`    DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT ' 更新时间 ',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_lab_code` (`lab_code`),
    KEY `idx_lab_name` (`lab_name`),
    KEY `idx_dept_id` (`dept_id`),
    CONSTRAINT `fk_exp_lab_dept` FOREIGN KEY (`dept_id`) REFERENCES `base_dept` (`id`) ON DELETE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='实验业务-实验室表';
-- 2. 实验业务-实验课程表
CREATE TABLE IF NOT EXISTS `exp_course`
(
    `id`          BIGINT       NOT NULL AUTO_INCREMENT COMMENT ' 课程ID ',
    `course_code` VARCHAR(50) COMMENT ' 课程编码 ',
    `course_name` VARCHAR(200) NOT NULL COMMENT ' 课程名称 ',
    `course_type` TINYINT      NOT NULL COMMENT ' 课程类型(1-必修,2-选修)',
    `dept_id`     BIGINT       NOT NULL COMMENT ' 所属学院ID ',
    `create_at`   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT ' 创建时间 ',
    `update_at`   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT ' 更新时间 ',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_course_code` (`course_code`),
    KEY `idx_dept_id` (`dept_id`),
    KEY `idx_course_name` (`course_name`),
    CONSTRAINT `fk_exp_course_dept` FOREIGN KEY (`dept_id`) REFERENCES `base_dept` (`id`) ON DELETE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='实验业务-课程表';

-- 辅助表：教学核心关联表
CREATE TABLE IF NOT EXISTS `exp_teaching_core`
(
    `id`        BIGINT      NOT NULL AUTO_INCREMENT COMMENT ' 核心关联ID ',
    `semester`  VARCHAR(20) NOT NULL COMMENT ' 学期 ',
    `course_id` BIGINT      NOT NULL COMMENT ' 课程ID ',
    `class_id`  BIGINT      NOT NULL COMMENT ' 班级ID ',
    `user_id`   BIGINT      NOT NULL COMMENT ' 教师ID ',
    `create_at` DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT ' 创建时间 ',
    `update_at` DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT ' 更新时间 ',
    PRIMARY KEY (`id`),
-- 唯一约束：同一学期+课程+班级+教师组合唯一，避免重复
    UNIQUE KEY `uk_core_unique` (`semester`, `course_id`, `class_id`, `user_id`),
-- 索引优化：高频查询字段加索引
    KEY `idx_semester` (`semester`),
    KEY `idx_course_class` (`course_id`, `class_id`),
    KEY `idx_teacher` (`user_id`),
-- 外键约束：基础数据删除受限，保证核心关联不失效
    CONSTRAINT `fk_core_course` FOREIGN KEY (`course_id`) REFERENCES `exp_course` (`id`) ON DELETE RESTRICT,
    CONSTRAINT `fk_core_class` FOREIGN KEY (`class_id`) REFERENCES `base_class` (`id`) ON DELETE RESTRICT,
    CONSTRAINT `fk_core_teacher` FOREIGN KEY (`user_id`) REFERENCES `sys_user` (`id`) ON DELETE RESTRICT
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='教学核心关联表';


-- 3. 实验业务-课程安排表
CREATE TABLE IF NOT EXISTS `exp_course_schedule`
(
    `id`               BIGINT   NOT NULL AUTO_INCREMENT COMMENT ' 排课ID ',
    `teaching_core_id` BIGINT   NOT NULL COMMENT ' 辅助表 ',
    `lab_id`           BIGINT   NOT NULL COMMENT ' 实验室ID ',
    `week_start`       INT COMMENT ' 周次范围-开始周 ',
    `week_end`         INT COMMENT ' 周次范围-结束周 ',
    `week_type`        TINYINT           DEFAULT 1 COMMENT ' 周次类型 ',
    `week_custom`      VARCHAR(50) COMMENT ' 自定义周次，仅week_type=3时使用 ',
    `week_day`         TINYINT  NOT NULL COMMENT ' 星期 ',
    `period_start`     INT COMMENT ' 节次-开始节 ',
    `period_end`       INT COMMENT ' 节次-结束节 ',
    `class_hours`      INT               DEFAULT 0 COMMENT ' 总学时 ',
    `is_report`        TINYINT  NOT NULL DEFAULT 0 COMMENT ' 是否有报告 ',
    `create_at`        DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT ' 创建时间 ',
    `update_at`        DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT ' 更新时间 ',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_schedule_unique` (`teaching_core_id`, `week_day`, `period_start`, `period_end`,
                                     `lab_id`) COMMENT '唯一约束：同一时间、同一实验室不能重复排课',
    KEY `idx_lab_id` (`lab_id`),
    KEY `idx_semester_week` (`teaching_core_id`, `week_start`, `week_end`),
    CONSTRAINT `fk_exp_schedule_course` FOREIGN KEY (`teaching_core_id`) REFERENCES `exp_teaching_core` (`id`) ON DELETE CASCADE,
    CONSTRAINT `fk_exp_schedule_lab` FOREIGN KEY (`lab_id`) REFERENCES `exp_lab` (`id`) ON DELETE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='实验业务-课程安排表';
-- 4. 实验业务-实验项目表
CREATE TABLE IF NOT EXISTS `exp_project`
(
    `id`                 BIGINT       NOT NULL AUTO_INCREMENT COMMENT ' 项目ID ',
    `teaching_core_id`   BIGINT       NOT NULL COMMENT ' 辅助表 ',
    `project_code`       VARCHAR(50) COMMENT ' 实验编码 ',
    `project_name`       VARCHAR(200) NOT NULL COMMENT ' 实验项目名称 ',
    `project_week_start` INT COMMENT ' 授课周次-开始周 ',
    `project_week_end`   INT COMMENT ' 授课周次-结束周 ',
    `weekly_hours`       INT          NOT NULL COMMENT ' 周学时数 ',
    `plan_hours`         INT          NOT NULL COMMENT ' 计划总学时 ',
    `actual_hours`       INT          NOT NULL DEFAULT 0 COMMENT ' 实际总学时 ',
    `exp_category`       TINYINT      NOT NULL COMMENT ' 实验类别 ',
    `exp_type`           TINYINT      NOT NULL COMMENT ' 实验类型 ',
    `subject`            TINYINT      NOT NULL COMMENT ' 所属学科 ',
    `group_num`          INT          NOT NULL DEFAULT 1 COMMENT ' 分组人数 ',
    `exp_person_type`    TINYINT      NOT NULL COMMENT ' 实验者类别 ',
    `exp_requirement`    TINYINT      NOT NULL COMMENT ' 实验要求 ',
    `remark`             VARCHAR(500)          DEFAULT NULL COMMENT ' 备注 ',
    `create_at`          DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT ' 创建时间 ',
    `update_at`          DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT ' 更新时间 ',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_project_code` (`project_code`) COMMENT '实验编码唯一',
    KEY `idx_teaching_core_id` (`teaching_core_id`),
    KEY `idx_category_type` (`exp_category`, `exp_type`),
    KEY `idx_project_week` (`project_week_start`, `project_week_end`),
    CONSTRAINT `fk_exp_project_teaching_core` FOREIGN KEY (`teaching_core_id`) REFERENCES `exp_teaching_core` (`id`) ON DELETE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='实验业务-实验项目表';
-- 5. 实验业务-实验报告表
CREATE TABLE IF NOT EXISTS `exp_report`
(
    `id`              BIGINT       NOT NULL AUTO_INCREMENT COMMENT ' 报告ID ',
    `project_id`      BIGINT       NOT NULL COMMENT ' 实验项目ID ',
    `upload_user_id`  BIGINT       NOT NULL COMMENT ' 上传用户ID ',
    `file_path`       VARCHAR(500) NOT NULL COMMENT ' 报告文件路径 ',
    `attachment_type` TINYINT      NOT NULL COMMENT ' 附件类型 ',
    `create_at`       DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT ' 上传时间 ',
    `update_at`       DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT ' 更新时间 ',
    `remark`          VARCHAR(500)          DEFAULT NULL COMMENT ' 备注 ',
    PRIMARY KEY (`id`),
    KEY `idx_project_id` (`project_id`) COMMENT '项目ID索引，查项目下报告',
    KEY `idx_upload_user` (`upload_user_id`) COMMENT '上传用户索引，查学生提交记录',
    KEY `idx_attachment_type` (`attachment_type`) COMMENT '附件类型索引，筛选模板/报告',
    CONSTRAINT `fk_exp_report_project` FOREIGN KEY (`project_id`) REFERENCES `exp_project` (`id`) ON DELETE CASCADE,
    CONSTRAINT `fk_exp_report_user` FOREIGN KEY (`upload_user_id`) REFERENCES `sys_user` (`id`) ON DELETE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='实验业务-实验报告表';
-- 6. 实验业务-报告批改表
CREATE TABLE IF NOT EXISTS `exp_report_review`
(
    `id`              BIGINT   NOT NULL AUTO_INCREMENT COMMENT ' 批改ID ',
    `report_id`       BIGINT   NOT NULL COMMENT ' 实验报告ID ',
    `review_user_id`  BIGINT   NOT NULL COMMENT ' 批改教师ID ',
    `score`           DECIMAL(5, 2)     DEFAULT NULL COMMENT ' 报告成绩 ',
    `review_comment`  TEXT              DEFAULT NULL COMMENT ' 批改意见 ',
    `plagiarism_rate` DECIMAL(5, 2)     DEFAULT 0 COMMENT ' 查重率 ',
    `create_at`       DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT ' 批改时间 ',
    `update_at`       DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT ' 更新时间 ',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_report_id` (`report_id`) COMMENT '一个报告仅一条批改记录',
    KEY `idx_review_user` (`review_user_id`) COMMENT '批改教师索引，查教师批改记录',
    KEY `idx_score` (`score`) COMMENT '成绩索引，按成绩筛选/统计',
    CONSTRAINT `fk_exp_review_report` FOREIGN KEY (`report_id`) REFERENCES `exp_report` (`id`) ON DELETE CASCADE,
    CONSTRAINT `fk_exp_review_user` FOREIGN KEY (`review_user_id`) REFERENCES `sys_user` (`id`) ON DELETE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='实验业务-报告批改表';

