/*
 Navicat Premium Data Transfer

 Source Server         : mysql
 Source Server Type    : MySQL
 Source Server Version : 80032
 Source Host           : localhost:3306
 Source Schema         : experiment

 Target Server Type    : MySQL
 Target Server Version : 80032
 File Encoding         : 65001

 Date: 12/12/2025 22:22:40
*/
CREATE DATABASE IF NOT EXISTS experiment;
USE experiment;

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for base_class
-- ----------------------------
DROP TABLE IF EXISTS `base_class`;
CREATE TABLE `base_class`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '班级ID（主键）',
  `class_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '班级编码（唯一，如：202301）',
  `class_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '班级名称',
  `major_id` bigint(0) NOT NULL COMMENT '所属专业ID（关联base_major.id）',
  `grade` int(0) NOT NULL COMMENT '年级（如：2023级）',
  `student_count` int(0) NULL DEFAULT 0 COMMENT '班级人数',
  `create_at` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_at` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_class_code`(`class_code`) USING BTREE,
  INDEX `idx_major_id`(`major_id`) USING BTREE,
  INDEX `idx_grade`(`grade`) USING BTREE,
  CONSTRAINT `fk_base_class_major` FOREIGN KEY (`major_id`) REFERENCES `base_major` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 25 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '基础班级表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of base_class
-- ----------------------------
INSERT INTO `base_class` VALUES (1, '10234', '22计科本2', 1, 2022, 40, '2025-11-20 15:29:41', '2025-11-20 15:29:41');
INSERT INTO `base_class` VALUES (3, '102377', '电工班2', 2, 2023, 4, '2025-11-20 15:30:14', '2025-11-20 15:30:54');
INSERT INTO `base_class` VALUES (4, '10111', '22数据本2', 1, 2022, 40, '2025-11-28 18:06:45', '2025-11-28 18:06:45');
INSERT INTO `base_class` VALUES (5, NULL, '班级1', 3, 2022, 0, '2025-11-28 20:28:25', '2025-11-28 20:28:25');
INSERT INTO `base_class` VALUES (6, NULL, '班级2', 4, 2022, 0, '2025-11-28 20:28:25', '2025-11-28 20:28:25');
INSERT INTO `base_class` VALUES (7, NULL, '班级3', 5, 2022, 0, '2025-11-28 20:28:25', '2025-11-28 20:28:25');
INSERT INTO `base_class` VALUES (8, NULL, '班级4', 6, 2022, 0, '2025-11-28 20:28:25', '2025-11-28 20:28:25');
INSERT INTO `base_class` VALUES (9, NULL, '班级5', 7, 2022, 0, '2025-11-28 20:28:25', '2025-11-28 20:28:25');
INSERT INTO `base_class` VALUES (10, NULL, '班级6', 8, 2022, 0, '2025-11-28 20:28:25', '2025-11-28 20:28:25');
INSERT INTO `base_class` VALUES (11, NULL, '班级7', 9, 2022, 0, '2025-11-28 20:28:25', '2025-11-28 20:28:25');
INSERT INTO `base_class` VALUES (12, NULL, '班级8', 10, 2022, 0, '2025-11-28 20:28:25', '2025-11-28 20:28:25');
INSERT INTO `base_class` VALUES (13, NULL, '班级9', 11, 2022, 0, '2025-11-28 20:28:25', '2025-11-28 20:28:25');
INSERT INTO `base_class` VALUES (14, NULL, '班级10', 12, 2022, 0, '2025-11-28 20:28:25', '2025-11-28 20:28:25');
INSERT INTO `base_class` VALUES (15, NULL, '班级11', 13, 2022, 0, '2025-11-28 20:28:25', '2025-11-28 20:28:25');
INSERT INTO `base_class` VALUES (16, NULL, '班级12', 14, 2022, 0, '2025-11-28 20:28:25', '2025-11-28 20:28:25');
INSERT INTO `base_class` VALUES (17, NULL, '班级13', 15, 2022, 0, '2025-11-28 20:28:25', '2025-11-28 20:28:25');
INSERT INTO `base_class` VALUES (18, NULL, '班级14', 16, 2022, 0, '2025-11-28 20:28:25', '2025-11-28 20:28:25');
INSERT INTO `base_class` VALUES (19, NULL, '班级15', 17, 2022, 0, '2025-11-28 20:28:25', '2025-11-28 20:28:25');
INSERT INTO `base_class` VALUES (20, NULL, '班级16', 18, 2022, 0, '2025-11-28 20:28:25', '2025-11-28 20:28:25');
INSERT INTO `base_class` VALUES (21, '', '22数据本', 1, 2022, 0, '2025-11-29 00:22:18', '2025-11-29 00:22:18');
INSERT INTO `base_class` VALUES (23, '1022', '22计科本1', 1, 2022, 0, '2025-11-29 00:22:43', '2025-11-29 00:22:43');
INSERT INTO `base_class` VALUES (24, '10245', '22计科本2', 1, 202, 0, '2025-11-29 00:23:02', '2025-11-29 00:23:02');

-- ----------------------------
-- Table structure for base_dept
-- ----------------------------
DROP TABLE IF EXISTS `base_dept`;
CREATE TABLE `base_dept`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '学院ID（主键）',
  `dept_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '学院编码（唯一，如：01）',
  `dept_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '学院名称',
  `create_at` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_at` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_dept_name`(`dept_name`) USING BTREE,
  UNIQUE INDEX `uk_dept_code`(`dept_code`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 21 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '基础学院表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of base_dept
-- ----------------------------
INSERT INTO `base_dept` VALUES (1, '1011', '大数据', '2025-11-20 15:27:46', '2025-11-20 15:27:46');
INSERT INTO `base_dept` VALUES (2, '1012', '电子', '2025-11-20 15:27:58', '2025-11-20 15:27:58');
INSERT INTO `base_dept` VALUES (3, NULL, '学院1', '2025-11-28 20:24:12', '2025-11-28 20:24:12');
INSERT INTO `base_dept` VALUES (4, NULL, '学院2', '2025-11-28 20:24:12', '2025-11-28 20:24:12');
INSERT INTO `base_dept` VALUES (5, NULL, '学院3', '2025-11-28 20:24:12', '2025-11-28 20:24:12');
INSERT INTO `base_dept` VALUES (6, NULL, '学院4', '2025-11-28 20:24:12', '2025-11-28 20:24:12');
INSERT INTO `base_dept` VALUES (7, NULL, '学院5', '2025-11-28 20:24:12', '2025-11-28 20:24:12');
INSERT INTO `base_dept` VALUES (8, NULL, '学院6', '2025-11-28 20:24:12', '2025-11-28 20:24:12');
INSERT INTO `base_dept` VALUES (9, NULL, '学院7', '2025-11-28 20:24:12', '2025-11-28 20:24:12');
INSERT INTO `base_dept` VALUES (10, NULL, '学院8', '2025-11-28 20:24:12', '2025-11-28 20:24:12');
INSERT INTO `base_dept` VALUES (11, NULL, '学院9', '2025-11-28 20:24:12', '2025-11-28 20:24:12');
INSERT INTO `base_dept` VALUES (12, NULL, '学院10', '2025-11-28 20:24:12', '2025-11-28 20:24:12');
INSERT INTO `base_dept` VALUES (13, NULL, '学院11', '2025-11-28 20:24:12', '2025-11-28 20:24:12');
INSERT INTO `base_dept` VALUES (14, NULL, '学院12', '2025-11-28 20:24:12', '2025-11-28 20:24:12');
INSERT INTO `base_dept` VALUES (15, NULL, '学院13', '2025-11-28 20:24:12', '2025-11-28 20:24:12');
INSERT INTO `base_dept` VALUES (16, NULL, '学院14', '2025-11-28 20:24:12', '2025-11-28 20:24:12');
INSERT INTO `base_dept` VALUES (17, NULL, '学院15', '2025-11-28 20:24:12', '2025-11-28 20:24:12');
INSERT INTO `base_dept` VALUES (18, NULL, '学院16', '2025-11-28 20:24:12', '2025-11-28 20:24:12');
INSERT INTO `base_dept` VALUES (19, NULL, '学院17', '2025-11-28 20:24:12', '2025-11-28 20:24:12');
INSERT INTO `base_dept` VALUES (20, NULL, '学院18', '2025-11-28 20:24:12', '2025-11-28 20:24:12');

-- ----------------------------
-- Table structure for base_major
-- ----------------------------
DROP TABLE IF EXISTS `base_major`;
CREATE TABLE `base_major`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '专业ID（主键）',
  `major_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '专业编码（唯一，如：080901）',
  `major_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '专业名称',
  `dept_id` bigint(0) NOT NULL COMMENT '所属学院ID（关联base_dept.id）',
  `create_at` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_at` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_major_code`(`major_code`) USING BTREE,
  INDEX `idx_dept_id`(`dept_id`) USING BTREE,
  CONSTRAINT `fk_base_major_dept` FOREIGN KEY (`dept_id`) REFERENCES `base_dept` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 19 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '基础专业表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of base_major
-- ----------------------------
INSERT INTO `base_major` VALUES (1, '1023', '计算机', 1, '2025-11-20 15:28:47', '2025-11-20 15:28:47');
INSERT INTO `base_major` VALUES (2, '20145', '电工', 2, '2025-11-20 15:30:36', '2025-11-20 15:30:36');
INSERT INTO `base_major` VALUES (3, NULL, '专业1', 1, '2025-11-28 20:24:49', '2025-11-28 20:24:49');
INSERT INTO `base_major` VALUES (4, NULL, '专业2', 1, '2025-11-28 20:24:49', '2025-11-28 20:24:49');
INSERT INTO `base_major` VALUES (5, NULL, '专业3', 1, '2025-11-28 20:24:49', '2025-11-28 20:24:49');
INSERT INTO `base_major` VALUES (6, NULL, '专业4', 1, '2025-11-28 20:24:49', '2025-11-28 20:24:49');
INSERT INTO `base_major` VALUES (7, NULL, '专业5', 1, '2025-11-28 20:24:49', '2025-11-28 20:24:49');
INSERT INTO `base_major` VALUES (8, NULL, '专业6', 1, '2025-11-28 20:24:49', '2025-11-28 20:24:49');
INSERT INTO `base_major` VALUES (9, NULL, '专业7', 1, '2025-11-28 20:24:49', '2025-11-28 20:24:49');
INSERT INTO `base_major` VALUES (10, NULL, '专业8', 1, '2025-11-28 20:24:49', '2025-11-28 20:24:49');
INSERT INTO `base_major` VALUES (11, NULL, '专业9', 1, '2025-11-28 20:24:49', '2025-11-28 20:24:49');
INSERT INTO `base_major` VALUES (12, NULL, '专业10', 1, '2025-11-28 20:24:49', '2025-11-28 20:24:49');
INSERT INTO `base_major` VALUES (13, NULL, '专业11', 1, '2025-11-28 20:24:49', '2025-11-28 20:24:49');
INSERT INTO `base_major` VALUES (14, NULL, '专业12', 1, '2025-11-28 20:24:49', '2025-11-28 20:24:49');
INSERT INTO `base_major` VALUES (15, NULL, '专业13', 1, '2025-11-28 20:24:49', '2025-11-28 20:24:49');
INSERT INTO `base_major` VALUES (16, NULL, '专业14', 1, '2025-11-28 20:24:49', '2025-11-28 20:24:49');
INSERT INTO `base_major` VALUES (17, NULL, '专业15', 1, '2025-11-28 20:24:49', '2025-11-28 20:24:49');
INSERT INTO `base_major` VALUES (18, NULL, '专业16', 1, '2025-11-28 20:24:49', '2025-11-28 20:24:49');

-- ----------------------------
-- Table structure for exp_course
-- ----------------------------
DROP TABLE IF EXISTS `exp_course`;
CREATE TABLE `exp_course`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '课程ID（主键）',
  `course_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '课程编码（唯一，如：CS101）',
  `course_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '课程名称',
  `course_type` tinyint(0) NOT NULL COMMENT '课程类型(1-必修,2-选修)',
  `dept_id` bigint(0) NOT NULL COMMENT '所属学院ID（关联base_dept.id）',
  `create_at` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_at` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_course_code`(`course_code`) USING BTREE,
  INDEX `idx_dept_id`(`dept_id`) USING BTREE,
  INDEX `idx_course_name`(`course_name`) USING BTREE,
  CONSTRAINT `fk_exp_course_dept` FOREIGN KEY (`dept_id`) REFERENCES `base_dept` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 40 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '实验业务-课程表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of exp_course
-- ----------------------------
INSERT INTO `exp_course` VALUES (1, '2024', '数据库原理和设计', 1, 1, '2025-11-20 15:32:06', '2025-11-20 16:14:31');
INSERT INTO `exp_course` VALUES (2, '10145', '高等数学', 1, 1, '2025-11-27 18:11:30', '2025-11-27 18:11:30');
INSERT INTO `exp_course` VALUES (3, NULL, '数据库组成原理1', 1, 1, '2025-11-28 17:23:34', '2025-11-28 17:23:34');
INSERT INTO `exp_course` VALUES (4, NULL, '数据库组成原理2', 2, 1, '2025-11-28 17:23:34', '2025-11-28 17:23:34');
INSERT INTO `exp_course` VALUES (5, NULL, '数据库组成原理3', 3, 1, '2025-11-28 17:23:34', '2025-11-28 17:23:34');
INSERT INTO `exp_course` VALUES (6, NULL, '数据库组成原理4', 3, 1, '2025-11-28 17:23:34', '2025-11-28 17:23:34');
INSERT INTO `exp_course` VALUES (7, NULL, '数据库组成原理5', 3, 1, '2025-11-28 17:23:34', '2025-11-28 17:23:34');
INSERT INTO `exp_course` VALUES (8, NULL, '数据库组成原理6', 3, 1, '2025-11-28 17:23:34', '2025-11-28 17:23:34');
INSERT INTO `exp_course` VALUES (9, NULL, '数据库组成原理7', 3, 1, '2025-11-28 17:23:34', '2025-11-28 17:23:34');
INSERT INTO `exp_course` VALUES (10, NULL, '数据库组成原理8', 3, 1, '2025-11-28 17:23:34', '2025-11-28 17:23:34');
INSERT INTO `exp_course` VALUES (11, NULL, '数据库组成原理9', 3, 1, '2025-11-28 17:23:34', '2025-11-28 17:23:34');
INSERT INTO `exp_course` VALUES (12, NULL, '数据库组成原理10', 3, 1, '2025-11-28 17:23:34', '2025-11-28 17:23:34');
INSERT INTO `exp_course` VALUES (13, NULL, '数据库组成原理11', 3, 1, '2025-11-28 17:23:34', '2025-11-28 17:23:34');
INSERT INTO `exp_course` VALUES (14, NULL, '数据库组成原理12', 3, 1, '2025-11-28 17:23:34', '2025-11-28 17:23:34');
INSERT INTO `exp_course` VALUES (15, NULL, '数据库组成原理13', 3, 1, '2025-11-28 17:23:34', '2025-11-28 17:23:34');
INSERT INTO `exp_course` VALUES (16, NULL, '数据库组成原理14', 3, 1, '2025-11-28 17:23:34', '2025-11-28 17:23:34');
INSERT INTO `exp_course` VALUES (17, NULL, '数据库组成原理15', 3, 1, '2025-11-28 17:23:34', '2025-11-28 17:23:34');
INSERT INTO `exp_course` VALUES (18, NULL, '数据库组成原理16', 3, 1, '2025-11-28 17:23:34', '2025-11-28 17:23:34');
INSERT INTO `exp_course` VALUES (19, NULL, '数据库组成原理17', 3, 1, '2025-11-28 17:23:34', '2025-11-28 17:23:34');
INSERT INTO `exp_course` VALUES (20, NULL, '数据库组成原理18', 3, 1, '2025-11-28 17:23:34', '2025-11-28 17:23:34');
INSERT INTO `exp_course` VALUES (21, NULL, '数据库组成原理19', 3, 1, '2025-11-28 17:23:34', '2025-11-28 17:23:34');
INSERT INTO `exp_course` VALUES (22, NULL, '数据库组成原理20', 3, 1, '2025-11-28 17:23:34', '2025-11-28 17:23:34');
INSERT INTO `exp_course` VALUES (23, NULL, '数据库组成原理21', 3, 1, '2025-11-28 17:23:34', '2025-11-28 17:23:34');
INSERT INTO `exp_course` VALUES (24, NULL, '数据库组成原理22', 3, 1, '2025-11-28 17:23:34', '2025-11-28 17:23:34');
INSERT INTO `exp_course` VALUES (25, NULL, '数据库组成原理23', 3, 1, '2025-11-28 17:23:34', '2025-11-28 17:23:34');
INSERT INTO `exp_course` VALUES (26, NULL, '数据库组成原理24', 3, 1, '2025-11-28 17:23:34', '2025-11-28 17:23:34');
INSERT INTO `exp_course` VALUES (27, NULL, '数据库组成原理25', 3, 1, '2025-11-28 17:23:34', '2025-11-28 17:23:34');
INSERT INTO `exp_course` VALUES (28, NULL, '数据库组成原理26', 3, 1, '2025-11-28 17:23:34', '2025-11-28 17:23:34');
INSERT INTO `exp_course` VALUES (29, NULL, '数据库组成原理27', 3, 1, '2025-11-28 17:23:34', '2025-11-28 17:23:34');
INSERT INTO `exp_course` VALUES (30, NULL, '数据库组成原理28', 3, 1, '2025-11-28 17:23:34', '2025-11-28 17:23:34');
INSERT INTO `exp_course` VALUES (31, NULL, '数据库组成原理29', 3, 1, '2025-11-28 17:23:34', '2025-11-28 17:23:34');
INSERT INTO `exp_course` VALUES (32, NULL, '数据库组成原理30', 3, 1, '2025-11-28 17:23:34', '2025-11-28 17:23:34');
INSERT INTO `exp_course` VALUES (33, NULL, '数据库组成原理31', 3, 1, '2025-11-28 17:23:34', '2025-11-28 17:23:34');
INSERT INTO `exp_course` VALUES (34, NULL, '数据库组成原理32', 3, 1, '2025-11-28 17:23:34', '2025-11-28 17:23:34');
INSERT INTO `exp_course` VALUES (35, NULL, '数据库组成原理33', 3, 1, '2025-11-28 17:23:34', '2025-11-28 17:23:34');
INSERT INTO `exp_course` VALUES (36, NULL, '数据库组成原理34', 3, 1, '2025-11-28 17:23:34', '2025-11-28 17:23:34');
INSERT INTO `exp_course` VALUES (37, NULL, '数据库组成原理35', 3, 1, '2025-11-28 17:23:34', '2025-11-28 17:23:34');
INSERT INTO `exp_course` VALUES (38, '10235', '移动应用开发实验', 1, 1, '2025-11-29 00:20:28', '2025-11-29 00:20:28');
INSERT INTO `exp_course` VALUES (39, '10244', '移动应用开发', 1, 1, '2025-11-29 00:20:38', '2025-11-29 00:20:38');

-- ----------------------------
-- Table structure for exp_course_schedule
-- ----------------------------
DROP TABLE IF EXISTS `exp_course_schedule`;
CREATE TABLE `exp_course_schedule`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '排课ID（主键）',
  `teaching_core_id` bigint(0) NOT NULL COMMENT '辅助表（关联exp_teaching_core.id）',
  `lab_id` bigint(0) NOT NULL COMMENT '实验室ID（关联exp_lab.id）',
  `week_start` int(0) NULL DEFAULT NULL COMMENT '周次范围-开始周（如：2）',
  `week_end` int(0) NULL DEFAULT NULL COMMENT '周次范围-结束周（如：17）',
  `week_type` tinyint(0) NOT NULL DEFAULT 1 COMMENT '周次类型（1-连续周，2-单周，3-双周，4-自定义）',
  `week_custom` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '自定义周次（如：1,3,5），仅week_type=3时使用',
  `week_day` tinyint(0) NOT NULL COMMENT '星期（1-周一，2-周二，...，7-周日）',
  `period_start` int(0) NULL DEFAULT NULL COMMENT '节次-开始节（如：1）',
  `period_end` int(0) NULL DEFAULT NULL COMMENT '节次-结束节（如：2）',
  `is_report` tinyint(0) NOT NULL DEFAULT 0 COMMENT '是否有报告（0-无，1-有）',
  `create_at` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_at` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `class_hours` int(0) NULL DEFAULT 0 COMMENT '总学时',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_schedule_unique`(`teaching_core_id`, `week_day`, `period_start`, `period_end`, `lab_id`) USING BTREE COMMENT '唯一约束：同一时间、同一实验室不能重复排课',
  INDEX `idx_lab_id`(`lab_id`) USING BTREE,
  INDEX `idx_semester_week`(`teaching_core_id`, `week_start`, `week_end`) USING BTREE,
  CONSTRAINT `fk_exp_schedule_course` FOREIGN KEY (`teaching_core_id`) REFERENCES `exp_teaching_core` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `fk_exp_schedule_lab` FOREIGN KEY (`lab_id`) REFERENCES `exp_lab` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 16 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '实验业务-课程安排表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of exp_course_schedule
-- ----------------------------
INSERT INTO `exp_course_schedule` VALUES (4, 43, 38, 1, 2, 1, '', 1, 7, 8, 0, '2025-11-28 16:46:59', '2025-12-02 19:37:50', 64);
INSERT INTO `exp_course_schedule` VALUES (5, 24, 38, 1, 2, 1, '', 1, 7, 8, 0, '2025-11-29 01:20:38', '2025-11-29 01:20:38', 20);
INSERT INTO `exp_course_schedule` VALUES (12, 35, 48, 2, 17, 1, NULL, 7, 1, 2, 0, '2025-11-29 01:48:22', '2025-11-29 01:48:22', 0);
INSERT INTO `exp_course_schedule` VALUES (13, 36, 48, 2, 17, 1, NULL, 7, 1, 2, 0, '2025-11-29 01:48:22', '2025-11-29 01:48:22', 0);
INSERT INTO `exp_course_schedule` VALUES (14, 37, 45, 2, 17, 1, NULL, 7, 3, 4, 1, '2025-11-29 01:48:22', '2025-11-29 01:48:22', 0);
INSERT INTO `exp_course_schedule` VALUES (15, 37, 48, NULL, NULL, 4, '2-14周(双),17周', 7, 5, 6, 1, '2025-11-29 01:48:22', '2025-11-29 01:48:22', 0);

-- ----------------------------
-- Table structure for exp_lab
-- ----------------------------
DROP TABLE IF EXISTS `exp_lab`;
CREATE TABLE `exp_lab`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '实验室ID（主键）',
  `lab_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '实验室编码（唯一，如：L0101）',
  `lab_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '实验室名称',
  `lab_location` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '位置（如：1号楼101室）',
  `dept_id` bigint(0) NOT NULL COMMENT '所属学院ID（关联base_dept.id）',
  `create_at` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_at` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_lab_code`(`lab_code`) USING BTREE,
  INDEX `idx_lab_name`(`lab_name`) USING BTREE,
  INDEX `idx_dept_id`(`dept_id`) USING BTREE,
  CONSTRAINT `fk_exp_lab_dept` FOREIGN KEY (`dept_id`) REFERENCES `base_dept` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 76 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '实验业务-实验室表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of exp_lab
-- ----------------------------
INSERT INTO `exp_lab` VALUES (38, NULL, 'UPS电源房', '车库', 1, '2025-11-28 16:46:05', '2025-11-28 16:46:05');
INSERT INTO `exp_lab` VALUES (39, NULL, '计算机基础实验室（7）', '实13412', 1, '2025-11-28 16:46:05', '2025-11-28 16:46:05');
INSERT INTO `exp_lab` VALUES (40, NULL, '计算机仿真实验室', '实13509', 1, '2025-11-28 16:46:05', '2025-11-28 16:46:05');
INSERT INTO `exp_lab` VALUES (41, NULL, '数字媒体技术绘画实验室', '实13510', 1, '2025-11-28 16:46:05', '2025-11-28 16:46:05');
INSERT INTO `exp_lab` VALUES (42, NULL, '计算机网络实验室', '实2108', 1, '2025-11-28 16:46:05', '2025-11-28 16:46:05');
INSERT INTO `exp_lab` VALUES (43, NULL, '物联网+嵌入式线上线下实验室（1）', '实2109A', 1, '2025-11-28 16:46:05', '2025-11-28 16:46:05');
INSERT INTO `exp_lab` VALUES (44, NULL, '物联网+嵌入式线上线下实验室（2）', '实2109B', 1, '2025-11-28 16:46:05', '2025-11-28 16:46:05');
INSERT INTO `exp_lab` VALUES (45, NULL, '大数据应用创新中心实验室（1）', '实2110', 1, '2025-11-28 16:46:05', '2025-11-28 16:46:05');
INSERT INTO `exp_lab` VALUES (46, NULL, '大数据应用创新中心实验室（2）', '实2111', 1, '2025-11-28 16:46:05', '2025-11-28 16:46:05');
INSERT INTO `exp_lab` VALUES (47, NULL, '大数据应用创新中心机房', '实2112', 1, '2025-11-28 16:46:05', '2025-11-28 16:46:05');
INSERT INTO `exp_lab` VALUES (48, NULL, '大数据应用创新中心实验室（3）', '实2114', 1, '2025-11-28 16:46:05', '2025-11-28 16:46:05');
INSERT INTO `exp_lab` VALUES (49, NULL, '大数据创客区', '实2115', 1, '2025-11-28 16:46:05', '2025-11-28 16:46:05');
INSERT INTO `exp_lab` VALUES (50, NULL, '大数据展厅', '实2116', 1, '2025-11-28 16:46:05', '2025-11-28 16:46:05');
INSERT INTO `exp_lab` VALUES (51, NULL, '数字电路技术实验室', '实2201', 1, '2025-11-28 16:46:05', '2025-11-28 16:46:05');
INSERT INTO `exp_lab` VALUES (52, NULL, '模拟电路技术实验室', '实2202', 1, '2025-11-28 16:46:05', '2025-11-28 16:46:05');
INSERT INTO `exp_lab` VALUES (53, NULL, '计算机组成原理实验室', '实2205', 1, '2025-11-28 16:46:05', '2025-11-28 16:46:05');
INSERT INTO `exp_lab` VALUES (54, NULL, '数字媒体技术综合实验室', '实2210', 1, '2025-11-28 16:46:05', '2025-11-28 16:46:05');
INSERT INTO `exp_lab` VALUES (55, NULL, '数字媒体技术专业实验室', '实2211', 1, '2025-11-28 16:46:05', '2025-11-28 16:46:05');
INSERT INTO `exp_lab` VALUES (56, NULL, '计算机实训室', '实2211B', 1, '2025-11-28 16:46:05', '2025-11-28 16:46:05');
INSERT INTO `exp_lab` VALUES (57, NULL, '计算机软件实验室', '实2212', 1, '2025-11-28 16:46:05', '2025-11-28 16:46:05');
INSERT INTO `exp_lab` VALUES (58, NULL, '计算机基础实验室（5）', '实2213', 1, '2025-11-28 16:46:05', '2025-11-28 16:46:05');
INSERT INTO `exp_lab` VALUES (59, NULL, '数据科学与大数据实验室', '实2214', 1, '2025-11-28 16:46:05', '2025-11-28 16:46:05');
INSERT INTO `exp_lab` VALUES (60, NULL, '计算机维护实验室', '实2218', 1, '2025-11-28 16:46:05', '2025-11-28 16:46:05');
INSERT INTO `exp_lab` VALUES (61, NULL, '计算机基础实验室（6）', '实2219', 1, '2025-11-28 16:46:05', '2025-11-28 16:46:05');
INSERT INTO `exp_lab` VALUES (62, NULL, '计算机基础实验室（1）', '实2220', 1, '2025-11-28 16:46:05', '2025-11-28 16:46:05');
INSERT INTO `exp_lab` VALUES (63, NULL, '计算机基础实验室（2）', '实2313', 1, '2025-11-28 16:46:05', '2025-11-28 16:46:05');
INSERT INTO `exp_lab` VALUES (64, NULL, '计算机基础实验室（3）', '实2314', 1, '2025-11-28 16:46:05', '2025-11-28 16:46:05');
INSERT INTO `exp_lab` VALUES (65, NULL, '计算机基础实验室（4）', '实2315', 1, '2025-11-28 16:46:05', '2025-11-28 16:46:05');
INSERT INTO `exp_lab` VALUES (66, NULL, '电子商务综合实验室', '实2316', 1, '2025-11-28 16:46:05', '2025-11-28 16:46:05');
INSERT INTO `exp_lab` VALUES (67, NULL, '传感器实验室', '实2317', 1, '2025-11-28 16:46:05', '2025-11-28 16:46:05');
INSERT INTO `exp_lab` VALUES (68, NULL, '大学生创新创业实训中心', '实2320', 1, '2025-11-28 16:46:05', '2025-11-28 16:46:05');
INSERT INTO `exp_lab` VALUES (69, NULL, '物联网工程实验室', '实2321', 1, '2025-11-28 16:46:05', '2025-11-28 16:46:05');
INSERT INTO `exp_lab` VALUES (70, NULL, '计算机PCB制作实验室', '实2322', 1, '2025-11-28 16:46:05', '2025-11-28 16:46:05');
INSERT INTO `exp_lab` VALUES (71, NULL, '物联网综合实验室', '实2323', 1, '2025-11-28 16:46:05', '2025-11-28 16:46:05');
INSERT INTO `exp_lab` VALUES (72, NULL, '计算机微机原理及接口实验室', '实2324', 1, '2025-11-28 16:46:05', '2025-11-28 16:46:05');

-- ----------------------------
-- Table structure for exp_project
-- ----------------------------
DROP TABLE IF EXISTS `exp_project`;
CREATE TABLE `exp_project`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '项目ID（主键）',
  `teaching_core_id` bigint(0) NOT NULL COMMENT '辅助表（关联exp_teaching_core.id）',
  `project_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '实验编码（如：CS101-01，非必需）',
  `project_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '实验项目名称',
  `project_week_start` int(0) NULL DEFAULT NULL COMMENT '授课周次-开始周（如：5）',
  `project_week_end` int(0) NULL DEFAULT NULL COMMENT '授课周次-结束周（如：6）',
  `weekly_hours` int(0) NOT NULL COMMENT '周学时数',
  `plan_hours` int(0) NOT NULL COMMENT '计划总学时（= weekly_hours × 周数）',
  `actual_hours` int(0) NOT NULL DEFAULT 0 COMMENT '实际总学时',
  `exp_category` tinyint(0) NOT NULL COMMENT '实验类别（1-基础/2-专业基础/3-专业/4-其他）',
  `exp_type` tinyint(0) NOT NULL COMMENT '实验类型（1-演示性/2-验证性/3-综合性/4-设计研究）',
  `subject` tinyint(0) NOT NULL COMMENT '所属学科（1-计算机类/2-电子信息类等）',
  `group_num` int(0) NOT NULL DEFAULT 1 COMMENT '分组人数',
  `exp_person_type` tinyint(0) NOT NULL COMMENT '实验者类别（1-本科生/2-专科生）',
  `exp_requirement` tinyint(0) NOT NULL COMMENT '实验要求（1-必修/2-选修/3-其他）',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `create_at` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_at` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_project_code`(`project_code`) USING BTREE COMMENT '实验编码唯一（若启用）',
  INDEX `idx_teaching_core_id`(`teaching_core_id`) USING BTREE,
  INDEX `idx_category_type`(`exp_category`, `exp_type`) USING BTREE,
  INDEX `idx_project_week`(`project_week_start`, `project_week_end`) USING BTREE,
  CONSTRAINT `fk_exp_project_teaching_core` FOREIGN KEY (`teaching_core_id`) REFERENCES `exp_teaching_core` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '实验业务-实验项目表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of exp_project
-- ----------------------------
INSERT INTO `exp_project` VALUES (2, 2, '10256', '实验一', 5, 5, 2, 4, 4, 1, 1, 1, 1, 1, 1, '', '2025-11-20 15:39:36', '2025-11-22 17:59:09');
INSERT INTO `exp_project` VALUES (3, 43, '1023', '实验一', 7, 7, 7, 7, 7, 2, 2, 1, 1, 2, 2, '', '2025-11-20 16:12:29', '2025-12-02 19:37:59');
INSERT INTO `exp_project` VALUES (4, 43, '10234', '实验二', 4, 5, 4, 4, 4, 1, 1, 1, 1, 1, 1, '', '2025-11-21 16:43:45', '2025-12-03 02:39:59');

-- ----------------------------
-- Table structure for exp_report
-- ----------------------------
DROP TABLE IF EXISTS `exp_report`;
CREATE TABLE `exp_report`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '报告ID（主键）',
  `project_id` bigint(0) NOT NULL COMMENT '实验项目ID（关联exp_project.id）',
  `upload_user_id` bigint(0) NOT NULL COMMENT '上传用户ID（关联sys_user.id，user_type=2）',
  `file_path` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '报告文件路径（服务器存储地址）',
  `attachment_type` tinyint(0) NOT NULL COMMENT '附件类型（1-实验模板/2-实验报告）',
  `create_at` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '上传时间',
  `update_at` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注（如：补交报告）',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_project_id`(`project_id`) USING BTREE COMMENT '项目ID索引，查项目下报告',
  INDEX `idx_upload_user`(`upload_user_id`) USING BTREE COMMENT '上传用户索引，查学生提交记录',
  INDEX `idx_attachment_type`(`attachment_type`) USING BTREE COMMENT '附件类型索引，筛选模板/报告',
  CONSTRAINT `fk_exp_report_project` FOREIGN KEY (`project_id`) REFERENCES `exp_project` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `fk_exp_report_user` FOREIGN KEY (`upload_user_id`) REFERENCES `sys_user` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 19 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '实验业务-实验报告表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of exp_report
-- ----------------------------
INSERT INTO `exp_report` VALUES (2, 2, 1, 'http://localhost:8089/api/public/upload/35cb64ff01c143c2885da1c08a915628@龙祖焕-本科毕业论文（设计）开题报告.docx', 1, '2025-11-20 15:40:28', '2025-11-20 15:40:28', NULL);
INSERT INTO `exp_report` VALUES (4, 3, 1, 'http://localhost:8089/api/public/upload/c6a30a856b0e4069835d33b59e671439@本科毕业论文（设计）开题报告.docx', 1, '2025-11-20 18:44:49', '2025-11-20 18:44:49', NULL);
INSERT INTO `exp_report` VALUES (5, 3, 2, 'http://localhost:8089/api/public/upload/59ff4ad2ee7b47869f5bb00bf04a40c4@实验项目管理系统参考图.docx', 2, '2025-11-20 19:31:48', '2025-11-20 19:31:48', NULL);
INSERT INTO `exp_report` VALUES (6, 3, 1, 'http://localhost:8089/api/public/upload/7908451de3c84df98eab46bf6a81d4a3@龙祖涣-选题参考.docx', 1, '2025-11-20 21:23:29', '2025-11-20 21:23:29', NULL);
INSERT INTO `exp_report` VALUES (10, 4, 2, 'http://localhost:8089/api/public/upload/17d52a9153d74c0fbc1b88f0413c9b02@龙祖焕-本科毕业论文（设计）开题报告.docx', 2, '2025-11-23 17:54:03', '2025-11-23 17:54:03', NULL);
INSERT INTO `exp_report` VALUES (11, 4, 3, 'http://localhost:8089/api/public/upload/9981f6680b55446c9e7803588d99bd9e@重庆信科招聘简章2025年1031.docx', 2, '2025-11-23 19:43:04', '2025-11-23 19:43:04', NULL);
INSERT INTO `exp_report` VALUES (12, 4, 3, 'http://localhost:8089/api/public/upload/6541b0a00a634d5e901d9369d245a589@简历.doc', 2, '2025-11-23 21:45:20', '2025-11-23 21:45:20', NULL);
INSERT INTO `exp_report` VALUES (13, 4, 2, 'http://localhost:8089/api/public/upload/20fb96027cb5422e93bf279293b39a32@签到表.doc', 2, '2025-11-23 21:45:34', '2025-11-23 21:45:34', NULL);
INSERT INTO `exp_report` VALUES (14, 4, 4, 'http://localhost:8089/api/public/upload/2d68aee65f6b42c1b42138834fa65335@简历.doc', 2, '2025-11-23 21:45:56', '2025-11-23 21:45:56', NULL);
INSERT INTO `exp_report` VALUES (15, 4, 29, 'http://localhost:8089/api/public/upload/0f843b3513a24ef3a4f18da8029cb23f@龙祖焕-本科毕业论文（设计）开题报告.docx', 2, '2025-11-23 21:46:35', '2025-11-23 21:46:35', NULL);
INSERT INTO `exp_report` VALUES (16, 4, 31, 'http://localhost:8089/api/public/upload/dfc3f964cb8a4a9fa60c4bc14b170db5@简历.doc', 2, '2025-11-23 21:46:49', '2025-11-23 21:46:49', NULL);
INSERT INTO `exp_report` VALUES (17, 3, 29, 'http://localhost:8089/api/public/upload/3043bb99c25a4366a72e57505c87950f@简历.doc', 2, '2025-11-23 22:15:14', '2025-11-23 22:15:14', NULL);
INSERT INTO `exp_report` VALUES (18, 3, 4, 'http://localhost:8089/api/public/upload/2d94f62cf96c4a8389b7ee38d4c4037a@学习路线规划.docx', 2, '2025-12-12 15:27:39', '2025-12-12 15:27:39', NULL);

-- ----------------------------
-- Table structure for exp_report_review
-- ----------------------------
DROP TABLE IF EXISTS `exp_report_review`;
CREATE TABLE `exp_report_review`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '批改ID（主键）',
  `report_id` bigint(0) NOT NULL COMMENT '实验报告ID（关联exp_report.id）',
  `review_user_id` bigint(0) NOT NULL COMMENT '批改教师ID（关联sys_user.id，user_type=1）',
  `score` decimal(5, 2) NULL DEFAULT NULL COMMENT '报告成绩（如：85.50）',
  `review_comment` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '批改意见',
  `plagiarism_rate` decimal(5, 2) NULL DEFAULT 0.00 COMMENT '查重率（百分比，如：5.20）',
  `create_at` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '批改时间',
  `update_at` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_report_id`(`report_id`) USING BTREE COMMENT '一个报告仅一条批改记录',
  INDEX `idx_review_user`(`review_user_id`) USING BTREE COMMENT '批改教师索引，查教师批改记录',
  INDEX `idx_score`(`score`) USING BTREE COMMENT '成绩索引，按成绩筛选/统计',
  CONSTRAINT `fk_exp_review_report` FOREIGN KEY (`report_id`) REFERENCES `exp_report` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `fk_exp_review_user` FOREIGN KEY (`review_user_id`) REFERENCES `sys_user` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '实验业务-报告批改表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of exp_report_review
-- ----------------------------
INSERT INTO `exp_report_review` VALUES (6, 11, 1, 100.00, '', 0.08, '2025-12-03 02:53:10', '2025-12-03 02:53:10');

-- ----------------------------
-- Table structure for exp_teaching_core
-- ----------------------------
DROP TABLE IF EXISTS `exp_teaching_core`;
CREATE TABLE `exp_teaching_core`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '核心关联ID（主键）',
  `semester` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '学期（如：2023-2024-1）',
  `course_id` bigint(0) NOT NULL COMMENT '课程ID（关联exp_course.id）',
  `class_id` bigint(0) NOT NULL COMMENT '班级ID（关联base_class.id）',
  `user_id` bigint(0) NOT NULL COMMENT '教师ID（关联sys_user.id，user_type=1）',
  `create_at` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_at` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_core_unique`(`semester`, `course_id`, `class_id`, `user_id`) USING BTREE,
  INDEX `idx_semester`(`semester`) USING BTREE,
  INDEX `idx_course_class`(`course_id`, `class_id`) USING BTREE,
  INDEX `idx_teacher`(`user_id`) USING BTREE,
  INDEX `fk_core_class`(`class_id`) USING BTREE,
  CONSTRAINT `fk_core_class` FOREIGN KEY (`class_id`) REFERENCES `base_class` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_core_course` FOREIGN KEY (`course_id`) REFERENCES `exp_course` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_core_teacher` FOREIGN KEY (`user_id`) REFERENCES `sys_user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 44 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '教学核心关联表（学期+课程+班级+教师）' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of exp_teaching_core
-- ----------------------------
INSERT INTO `exp_teaching_core` VALUES (1, '2025-2026-1', 1, 3, 1, '2025-11-20 15:32:22', '2025-11-28 13:49:57');
INSERT INTO `exp_teaching_core` VALUES (2, '2025-2026-2', 1, 3, 1, '2025-11-20 15:32:34', '2025-11-20 15:32:34');
INSERT INTO `exp_teaching_core` VALUES (3, '2025-2026-1', 2, 3, 41, '2025-11-27 18:11:48', '2025-11-27 18:11:48');
INSERT INTO `exp_teaching_core` VALUES (24, '2025-2026-1', 3, 1, 2, '2025-11-29 01:20:23', '2025-11-29 01:20:23');
INSERT INTO `exp_teaching_core` VALUES (35, '2024-2025-2', 38, 1, 2, '2025-11-29 01:32:31', '2025-11-29 01:32:31');
INSERT INTO `exp_teaching_core` VALUES (36, '2024-2025-2', 38, 23, 2, '2025-11-29 01:32:31', '2025-11-29 01:32:31');
INSERT INTO `exp_teaching_core` VALUES (37, '2024-2025-2', 39, 21, 2, '2025-11-29 01:32:31', '2025-11-29 01:32:31');
INSERT INTO `exp_teaching_core` VALUES (43, '2025-2026-1', 2, 1, 1, '2025-12-02 19:37:20', '2025-12-02 19:37:20');

-- ----------------------------
-- Table structure for sys_dict
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict`;
CREATE TABLE `sys_dict`  (
  `id` bigint(0) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '字典ID（主键）',
  `dict_group` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '字典分组（如：exp_category-实验类别）',
  `dict_key` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '字典键（类型内唯一，如：basic-基础实验）',
  `dict_value` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '字典值（展示用，如：基础实验）',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '描述（说明业务含义）',
  `create_at` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_at` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_dict_type_key`(`dict_group`, `dict_key`, `dict_value`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 101 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '系统通用字典表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_dict
-- ----------------------------
INSERT INTO `sys_dict` VALUES (51, 'sys_user_userType', '管理员', '1', '', '2025-11-28 14:36:34', '2025-11-28 14:36:34');
INSERT INTO `sys_dict` VALUES (52, 'sys_user_userType', '教师', '2', '', '2025-11-28 14:36:34', '2025-11-28 14:36:34');
INSERT INTO `sys_dict` VALUES (53, 'sys_user_userType', '学生', '3', '', '2025-11-28 14:36:34', '2025-11-28 14:36:34');
INSERT INTO `sys_dict` VALUES (54, 'sys_user_gender', '男', '1', '', '2025-11-28 14:36:34', '2025-11-28 14:36:34');
INSERT INTO `sys_dict` VALUES (55, 'sys_user_gender', '女', '2', '', '2025-11-28 14:36:34', '2025-11-28 14:36:34');
INSERT INTO `sys_dict` VALUES (56, 'sys_user_userStatus', '启用', '0', '', '2025-11-28 14:36:34', '2025-11-28 14:36:34');
INSERT INTO `sys_dict` VALUES (57, 'sys_user_userStatus', '禁用', '1', '', '2025-11-28 14:36:34', '2025-11-28 14:36:34');
INSERT INTO `sys_dict` VALUES (58, 'sys_permission_permType', '视图', '1', '', '2025-11-28 14:36:34', '2025-11-28 14:36:34');
INSERT INTO `sys_dict` VALUES (59, 'sys_permission_permType', '操作', '2', '', '2025-11-28 14:36:34', '2025-11-28 14:36:34');
INSERT INTO `sys_dict` VALUES (60, 'sys_permission_permType', '其他', '3', '', '2025-11-28 14:36:34', '2025-11-28 14:36:34');
INSERT INTO `sys_dict` VALUES (61, 'exp_course_schedule_weekDay', '星期一', '1', '', '2025-11-28 14:36:34', '2025-11-28 14:36:34');
INSERT INTO `sys_dict` VALUES (62, 'exp_course_schedule_weekDay', '星期二', '2', '', '2025-11-28 14:36:34', '2025-11-28 14:36:34');
INSERT INTO `sys_dict` VALUES (63, 'exp_course_schedule_weekDay', '星期三', '3', '', '2025-11-28 14:36:34', '2025-11-28 14:36:34');
INSERT INTO `sys_dict` VALUES (64, 'exp_course_schedule_weekDay', '星期四', '4', '', '2025-11-28 14:36:34', '2025-11-28 14:36:34');
INSERT INTO `sys_dict` VALUES (65, 'exp_course_schedule_weekDay', '星期五', '5', '', '2025-11-28 14:36:34', '2025-11-28 14:36:34');
INSERT INTO `sys_dict` VALUES (66, 'exp_course_schedule_weekDay', '星期六', '6', '', '2025-11-28 14:36:34', '2025-11-28 14:36:34');
INSERT INTO `sys_dict` VALUES (67, 'exp_course_schedule_weekDay', '星期日', '7', '', '2025-11-28 14:36:34', '2025-11-28 14:36:34');
INSERT INTO `sys_dict` VALUES (68, 'exp_course_schedule_isReport', '需要', '1', '', '2025-11-28 14:36:34', '2025-11-28 14:36:34');
INSERT INTO `sys_dict` VALUES (69, 'exp_course_schedule_isReport', '不需要', '0', '', '2025-11-28 14:36:34', '2025-11-28 14:36:34');
INSERT INTO `sys_dict` VALUES (70, 'exp_course_courseType', '必修', '1', '', '2025-11-28 14:36:34', '2025-11-28 14:36:34');
INSERT INTO `sys_dict` VALUES (71, 'exp_course_courseType', '选修', '2', '', '2025-11-28 14:36:34', '2025-11-28 14:36:34');
INSERT INTO `sys_dict` VALUES (72, 'exp_course_courseType', '其他', '3', '', '2025-11-28 14:36:34', '2025-11-28 14:36:34');
INSERT INTO `sys_dict` VALUES (73, 'exp_project_expCategory', '基础', '1', '', '2025-11-28 14:36:34', '2025-11-28 14:36:34');
INSERT INTO `sys_dict` VALUES (74, 'exp_project_expCategory', '专业基础', '2', '', '2025-11-28 14:36:34', '2025-11-28 14:36:34');
INSERT INTO `sys_dict` VALUES (75, 'exp_project_expCategory', '专业', '3', '', '2025-11-28 14:36:34', '2025-11-28 14:36:34');
INSERT INTO `sys_dict` VALUES (76, 'exp_project_expCategory', '其他', '4', '', '2025-11-28 14:36:34', '2025-11-28 14:36:34');
INSERT INTO `sys_dict` VALUES (77, 'exp_project_expType', '演示性', '1', '', '2025-11-28 14:36:34', '2025-11-28 14:36:34');
INSERT INTO `sys_dict` VALUES (78, 'exp_project_expType', '验证性', '2', '', '2025-11-28 14:36:34', '2025-11-28 14:36:34');
INSERT INTO `sys_dict` VALUES (79, 'exp_project_expType', '综合性', '3', '', '2025-11-28 14:36:34', '2025-11-28 14:36:34');
INSERT INTO `sys_dict` VALUES (80, 'exp_project_expType', '设计研究', '4', '', '2025-11-28 14:36:34', '2025-11-28 14:36:34');
INSERT INTO `sys_dict` VALUES (81, 'exp_project_expType', '其他', '5', '', '2025-11-28 14:36:34', '2025-11-28 14:36:34');
INSERT INTO `sys_dict` VALUES (82, 'exp_project_subject', '计算机类', '1', '', '2025-11-28 14:36:34', '2025-11-28 14:36:34');
INSERT INTO `sys_dict` VALUES (83, 'exp_project_subject', '电子信息', '2', '', '2025-11-28 14:36:34', '2025-11-28 14:36:34');
INSERT INTO `sys_dict` VALUES (84, 'exp_project_subject', '其他', '3', '', '2025-11-28 14:36:34', '2025-11-28 14:36:34');
INSERT INTO `sys_dict` VALUES (85, 'exp_project_expPersonType', '本科生', '1', '', '2025-11-28 14:36:34', '2025-11-28 14:36:34');
INSERT INTO `sys_dict` VALUES (86, 'exp_project_expPersonType', '专科生', '2', '', '2025-11-28 14:36:34', '2025-11-28 14:36:34');
INSERT INTO `sys_dict` VALUES (87, 'exp_project_expRequirement', '必修', '1', '', '2025-11-28 14:36:34', '2025-11-28 14:36:34');
INSERT INTO `sys_dict` VALUES (88, 'exp_project_expRequirement', '选修', '2', '', '2025-11-28 14:36:34', '2025-11-28 14:36:34');
INSERT INTO `sys_dict` VALUES (89, 'exp_project_expRequirement', '其他', '3', '', '2025-11-28 14:36:34', '2025-11-28 14:36:34');
INSERT INTO `sys_dict` VALUES (90, 'exp_report_attachmentType', '实验模板', '1', '', '2025-11-28 14:36:34', '2025-11-28 14:36:34');
INSERT INTO `sys_dict` VALUES (91, 'exp_report_attachmentType', '实验报告', '2', '', '2025-11-28 14:36:34', '2025-11-28 14:36:34');
INSERT INTO `sys_dict` VALUES (92, 'sys_notice_noticeType', '系统通知', '1', '', '2025-11-28 14:36:34', '2025-11-28 14:36:34');
INSERT INTO `sys_dict` VALUES (93, 'sys_notice_noticeType', '分院通知', '2', '', '2025-11-28 14:36:34', '2025-11-28 14:36:34');
INSERT INTO `sys_dict` VALUES (94, 'sys_notice_noticeType', '班级通知', '3', '', '2025-11-28 14:36:34', '2025-11-28 14:36:34');
INSERT INTO `sys_dict` VALUES (95, 'sys_notice_noticeType', '个人通知', '4', '', '2025-11-28 14:36:34', '2025-11-28 14:36:34');
INSERT INTO `sys_dict` VALUES (96, 'sys_notice_noticeStatus', '启用', '0', '', '2025-11-28 14:36:34', '2025-11-28 14:36:34');
INSERT INTO `sys_dict` VALUES (97, 'sys_notice_noticeStatus', '禁用', '1', '', '2025-11-28 14:36:34', '2025-11-28 14:36:34');
INSERT INTO `sys_dict` VALUES (98, 'exp_course_schedule_weekType', '连续周', '1', '', '2025-11-28 14:36:34', '2025-11-28 14:36:34');
INSERT INTO `sys_dict` VALUES (99, 'exp_course_schedule_weekType', '单周', '2', '', '2025-11-28 14:36:34', '2025-11-28 14:36:34');
INSERT INTO `sys_dict` VALUES (100, 'exp_course_schedule_weekType', '双周', '3', '', '2025-11-28 14:36:34', '2025-11-28 14:36:34');
INSERT INTO `sys_dict` VALUES (101, 'exp_course_schedule_weekType', '自定义', '4', '', '2025-11-28 14:36:34', '2025-11-28 14:36:34');

-- ----------------------------
-- Table structure for sys_notice
-- ----------------------------
DROP TABLE IF EXISTS `sys_notice`;
CREATE TABLE `sys_notice`  (
  `id` bigint(0) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '通知ID（主键）',
  `notice_title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '通知标题',
  `notice_content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '通知内容',
  `notice_type` tinyint(0) NOT NULL COMMENT '类型（0-系统,2-学院，3-班级，4-个人）',
  `sender_id` bigint(0) UNSIGNED NULL DEFAULT NULL COMMENT '发送者ID（系统填0，关联sys_user.id）',
  `target_id` bigint(0) UNSIGNED NULL DEFAULT NULL COMMENT '目标ID（系统/学院/班级/用户ID）',
  `notice_status` tinyint(0) NULL DEFAULT 1 COMMENT '状态（1-启用，0-禁用）',
  `create_at` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_at` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_notice_type_target`(`notice_type`, `target_id`) USING BTREE,
  INDEX `idx_create_at`(`create_at`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '系统通知表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_notice_confirm
-- ----------------------------
DROP TABLE IF EXISTS `sys_notice_confirm`;
CREATE TABLE `sys_notice_confirm`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '确认ID（主键）',
  `notice_id` bigint(0) UNSIGNED NOT NULL COMMENT '通知ID（关联sys_notice.id）',
  `user_id` bigint(0) NOT NULL COMMENT '用户ID（关联sys_user.id）',
  `create_at` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间（已读时间）',
  `update_at` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_notice_user`(`notice_id`, `user_id`) USING BTREE,
  CONSTRAINT `fk_sys_notice_confirm_notice` FOREIGN KEY (`notice_id`) REFERENCES `sys_notice` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '通知确认表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_permission
-- ----------------------------
DROP TABLE IF EXISTS `sys_permission`;
CREATE TABLE `sys_permission`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '权限ID（主键）',
  `perm_group` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '权限分组',
  `perm_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '权限名称（如：提交报告）',
  `perm_code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '权限编码（唯一，如：exp_report:submit）',
  `perm_type` tinyint(0) NOT NULL COMMENT '权限类型（1-菜单，2-按钮）',
  `create_at` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_at` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `parent_code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `request_uri` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `perm_status` tinyint(0) NULL DEFAULT 1,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_perm_code`(`perm_code`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1380 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '系统权限表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_permission
-- ----------------------------
INSERT INTO `sys_permission` VALUES (1202, '通用管理', '通用管理', 'common', 1, '2025-11-27 20:24:32', '2025-11-27 20:24:32', NULL, '', '', 0);
INSERT INTO `sys_permission` VALUES (1203, '通用管理', '查询个人信息', 'common:info:query', 2, '2025-11-27 20:24:32', '2025-11-27 20:24:32', NULL, 'common', '/api/user-info', 0);
INSERT INTO `sys_permission` VALUES (1204, '通用管理', '修改基础信息', 'common:basic:update', 2, '2025-11-27 20:24:32', '2025-11-27 20:24:32', NULL, 'common', '/api/basic-info/update', 0);
INSERT INTO `sys_permission` VALUES (1205, '通用管理', '修改密码', 'common:password:update', 2, '2025-11-27 20:24:32', '2025-11-27 20:24:32', NULL, 'common', '/api/password/update', 0);
INSERT INTO `sys_permission` VALUES (1206, '通用管理', '发送邮件', 'common:email:send', 2, '2025-11-27 20:24:32', '2025-11-27 20:24:32', NULL, 'common', '/api/email', 0);
INSERT INTO `sys_permission` VALUES (1207, '通用管理', '修改邮箱', 'common:email:update', 2, '2025-11-27 20:24:32', '2025-11-27 20:24:32', NULL, 'common', '/api/email/update', 0);
INSERT INTO `sys_permission` VALUES (1208, '通用管理', '变更当前用户角色', 'common:current:role:update', 2, '2025-11-27 20:24:32', '2025-11-27 20:24:32', NULL, 'common', '/api/current-role/update', 0);
INSERT INTO `sys_permission` VALUES (1209, '通用管理', '忘记密码', 'common:password:forgot', 2, '2025-11-27 20:24:32', '2025-11-27 20:24:32', NULL, 'common', '/api/forgot-password', 0);
INSERT INTO `sys_permission` VALUES (1210, '通用管理', '系统登录', 'common:login', 2, '2025-11-27 20:24:32', '2025-11-27 20:24:32', NULL, 'common', '/api/login', 0);
INSERT INTO `sys_permission` VALUES (1211, '用户管理', '用户管理', 'user', 1, '2025-11-27 20:24:32', '2025-11-27 20:24:32', NULL, '', '', 1);
INSERT INTO `sys_permission` VALUES (1212, '用户管理', '查询用户列表', 'user:page', 1, '2025-11-27 20:24:32', '2025-11-27 20:24:32', NULL, 'user', '/api/admin/user/page', 1);
INSERT INTO `sys_permission` VALUES (1213, '用户管理', '创建用户', 'user:create', 2, '2025-11-27 20:24:32', '2025-11-27 20:24:32', NULL, 'user', '/api/admin/user/create', 1);
INSERT INTO `sys_permission` VALUES (1214, '用户管理', '修改用户', 'user:update', 2, '2025-11-27 20:24:32', '2025-11-27 20:24:32', NULL, 'user', '/api/admin/user/update', 1);
INSERT INTO `sys_permission` VALUES (1215, '用户管理', '删除用户', 'user:delete', 2, '2025-11-27 20:24:32', '2025-11-27 20:24:32', NULL, 'user', '/api/admin/user/delete/{id}', 1);
INSERT INTO `sys_permission` VALUES (1216, '用户管理', '单个查询用户', 'user:query', 2, '2025-11-27 20:24:32', '2025-11-27 20:24:32', NULL, 'user', '/api/admin/user/select', 1);
INSERT INTO `sys_permission` VALUES (1217, '用户管理', '批量新增用户', 'user:batchInsert', 2, '2025-11-27 20:24:32', '2025-11-27 20:24:32', NULL, 'user', '/api/admin/user/batch-insert', 1);
INSERT INTO `sys_permission` VALUES (1218, '用户管理', '导出用户列表', 'user:export', 2, '2025-11-27 20:24:32', '2025-11-27 20:24:32', NULL, 'user', '/api/admin/user/page', 1);
INSERT INTO `sys_permission` VALUES (1219, '用户管理', '重置用户密码', 'user:resetPwd', 2, '2025-11-27 20:24:32', '2025-11-27 20:24:32', NULL, 'user', '/api/admin/user/reset-password/{id}', 1);
INSERT INTO `sys_permission` VALUES (1220, '角色管理', '角色管理', 'role', 1, '2025-11-27 20:24:32', '2025-11-27 20:24:32', NULL, '', '', 1);
INSERT INTO `sys_permission` VALUES (1221, '角色管理', '查询角色列表', 'role:page', 1, '2025-11-27 20:24:32', '2025-11-27 20:24:32', NULL, 'role', '/api/admin/role/page', 1);
INSERT INTO `sys_permission` VALUES (1222, '角色管理', '创建角色', 'role:create', 2, '2025-11-27 20:24:32', '2025-11-27 20:24:32', NULL, 'role', '/api/admin/role/create', 1);
INSERT INTO `sys_permission` VALUES (1223, '角色管理', '修改角色', 'role:update', 2, '2025-11-27 20:24:32', '2025-11-27 20:24:32', NULL, 'role', '/api/admin/role/update', 1);
INSERT INTO `sys_permission` VALUES (1224, '角色管理', '删除角色', 'role:delete', 2, '2025-11-27 20:24:32', '2025-11-27 20:24:32', NULL, 'role', '/api/admin/role/delete/{id}', 1);
INSERT INTO `sys_permission` VALUES (1225, '角色管理', '单个查询角色', 'role:query', 2, '2025-11-27 20:24:32', '2025-11-27 20:24:32', NULL, 'role', '/api/admin/role/select', 1);
INSERT INTO `sys_permission` VALUES (1226, '角色管理', '批量新增角色', 'role:batchInsert', 2, '2025-11-27 20:24:32', '2025-11-27 20:24:32', NULL, 'role', '/api/admin/role/batch-insert', 1);
INSERT INTO `sys_permission` VALUES (1227, '角色管理', '导出角色列表', 'role:export', 2, '2025-11-27 20:24:32', '2025-11-27 20:24:32', NULL, 'role', '/api/admin/role/page', 1);
INSERT INTO `sys_permission` VALUES (1228, '用户角色关联', '用户角色关联管理', 'userRole', 1, '2025-11-27 20:24:32', '2025-11-27 20:24:32', NULL, '', '', 1);
INSERT INTO `sys_permission` VALUES (1229, '用户角色关联', '查询用户角色关联列表', 'userRole:page', 1, '2025-11-27 20:24:32', '2025-11-27 20:24:32', NULL, 'userRole', '/api/admin/user-role/page', 1);
INSERT INTO `sys_permission` VALUES (1230, '用户角色关联', '创建用户角色关联', 'userRole:create', 2, '2025-11-27 20:24:32', '2025-11-27 20:24:32', NULL, 'userRole', '/api/admin/user-role/create', 1);
INSERT INTO `sys_permission` VALUES (1231, '用户角色关联', '修改用户角色关联', 'userRole:update', 2, '2025-11-27 20:24:32', '2025-11-27 20:24:32', NULL, 'userRole', '/api/admin/user-role/update', 1);
INSERT INTO `sys_permission` VALUES (1232, '用户角色关联', '删除用户角色关联', 'userRole:delete', 2, '2025-11-27 20:24:32', '2025-11-27 20:24:32', NULL, 'userRole', '/api/admin/user-role/delete/{id}', 1);
INSERT INTO `sys_permission` VALUES (1233, '权限管理', '权限管理', 'perm', 1, '2025-11-27 20:24:32', '2025-11-27 20:24:32', NULL, '', '', 1);
INSERT INTO `sys_permission` VALUES (1234, '权限管理', '查询权限列表', 'perm:page', 1, '2025-11-27 20:24:32', '2025-11-27 20:24:32', NULL, 'perm', '/api/admin/perm/page', 1);
INSERT INTO `sys_permission` VALUES (1235, '权限管理', '创建权限', 'perm:create', 2, '2025-11-27 20:24:32', '2025-11-27 20:24:32', NULL, 'perm', '/api/admin/perm/create', 1);
INSERT INTO `sys_permission` VALUES (1236, '权限管理', '修改权限', 'perm:update', 2, '2025-11-27 20:24:32', '2025-11-27 20:24:32', NULL, 'perm', '/api/admin/perm/update', 1);
INSERT INTO `sys_permission` VALUES (1237, '权限管理', '删除权限', 'perm:delete', 2, '2025-11-27 20:24:32', '2025-11-27 20:24:32', NULL, 'perm', '/api/admin/perm/delete/{id}', 1);
INSERT INTO `sys_permission` VALUES (1238, '权限管理', '单个查询权限', 'perm:query', 2, '2025-11-27 20:24:32', '2025-11-27 20:24:32', NULL, 'perm', '/api/admin/perm/select', 1);
INSERT INTO `sys_permission` VALUES (1239, '权限管理', '批量新增权限', 'perm:batchInsert', 2, '2025-11-27 20:24:32', '2025-11-27 20:24:32', NULL, 'perm', '/api/admin/perm/batch-insert', 1);
INSERT INTO `sys_permission` VALUES (1240, '权限管理', '批量删除权限', 'perm:batchDelete', 2, '2025-11-27 20:24:32', '2025-11-27 20:24:32', NULL, 'perm', '/api/admin/perm/batch-delete', 1);
INSERT INTO `sys_permission` VALUES (1241, '权限管理', '构建角色权限树', 'perm:tree', 2, '2025-11-27 20:24:32', '2025-11-27 20:24:32', NULL, 'perm', '/api/admin/perm/tree/{roleId}', 1);
INSERT INTO `sys_permission` VALUES (1242, '权限管理', '导出权限列表', 'perm:export', 2, '2025-11-27 20:24:33', '2025-11-27 20:24:33', NULL, 'perm', '/api/admin/perm/page', 1);
INSERT INTO `sys_permission` VALUES (1243, '角色权限关联', '角色权限关联管理', 'rolePerm', 1, '2025-11-27 20:24:33', '2025-11-27 20:24:33', NULL, '', '', 1);
INSERT INTO `sys_permission` VALUES (1244, '角色权限关联', '查询角色权限关联列表', 'rolePerm:page', 1, '2025-11-27 20:24:33', '2025-11-27 20:24:33', NULL, 'rolePerm', '/api/admin/role-perm/page', 1);
INSERT INTO `sys_permission` VALUES (1245, '角色权限关联', '创建角色权限关联', 'rolePerm:create', 2, '2025-11-27 20:24:33', '2025-11-27 20:24:33', NULL, 'rolePerm', '/api/admin/role-perm/create', 1);
INSERT INTO `sys_permission` VALUES (1246, '角色权限关联', '修改角色权限关联', 'rolePerm:update', 2, '2025-11-27 20:24:33', '2025-11-27 20:24:33', NULL, 'rolePerm', '/api/admin/role-perm/update', 1);
INSERT INTO `sys_permission` VALUES (1247, '角色权限关联', '删除角色权限关联', 'rolePerm:delete', 2, '2025-11-27 20:24:33', '2025-11-27 20:24:33', NULL, 'rolePerm', '/api/admin/role-perm/delete/{id}', 1);
INSERT INTO `sys_permission` VALUES (1248, '角色权限关联', '批量增加角色权限关联', 'rolePerm:batchInsert', 2, '2025-11-27 20:24:33', '2025-11-27 20:24:33', NULL, 'rolePerm', '/api/admin/role-perm/{roleId}/batch-insert', 1);
INSERT INTO `sys_permission` VALUES (1249, '分院管理', '分院管理', 'dept', 1, '2025-11-27 20:24:33', '2025-11-27 20:24:33', NULL, '', '', 1);
INSERT INTO `sys_permission` VALUES (1250, '分院管理', '查询分院列表', 'dept:page', 1, '2025-11-27 20:24:33', '2025-11-27 20:24:33', NULL, 'dept', '/api/admin/dept/page', 1);
INSERT INTO `sys_permission` VALUES (1251, '分院管理', '创建分院', 'dept:create', 2, '2025-11-27 20:24:33', '2025-11-27 20:24:33', NULL, 'dept', '/api/admin/dept/create', 1);
INSERT INTO `sys_permission` VALUES (1252, '分院管理', '修改分院', 'dept:update', 2, '2025-11-27 20:24:33', '2025-11-27 20:24:33', NULL, 'dept', '/api/admin/dept/update', 1);
INSERT INTO `sys_permission` VALUES (1253, '分院管理', '删除分院', 'dept:delete', 2, '2025-11-27 20:24:33', '2025-11-27 20:24:33', NULL, 'dept', '/api/admin/dept/delete/{id}', 1);
INSERT INTO `sys_permission` VALUES (1254, '分院管理', '单个查询分院', 'dept:query', 2, '2025-11-27 20:24:33', '2025-11-27 20:24:33', NULL, 'dept', '/api/admin/dept/select', 1);
INSERT INTO `sys_permission` VALUES (1255, '分院管理', '批量新增分院', 'dept:batchInsert', 2, '2025-11-27 20:24:33', '2025-11-27 20:24:33', NULL, 'dept', '/api/admin/dept/batch-insert', 1);
INSERT INTO `sys_permission` VALUES (1256, '分院管理', '导出分院数据', 'dept:export', 2, '2025-11-27 20:24:33', '2025-11-27 20:24:33', NULL, 'dept', '/api/admin/dept/page', 1);
INSERT INTO `sys_permission` VALUES (1257, '专业管理', '专业管理', 'major', 1, '2025-11-27 20:24:33', '2025-11-27 20:24:33', NULL, '', '', 1);
INSERT INTO `sys_permission` VALUES (1258, '专业管理', '查询专业列表', 'major:page', 1, '2025-11-27 20:24:33', '2025-11-27 20:24:33', NULL, 'major', '/api/admin/major/page', 1);
INSERT INTO `sys_permission` VALUES (1259, '专业管理', '创建专业', 'major:create', 2, '2025-11-27 20:24:33', '2025-11-27 20:24:33', NULL, 'major', '/api/admin/major/create', 1);
INSERT INTO `sys_permission` VALUES (1260, '专业管理', '修改专业', 'major:update', 2, '2025-11-27 20:24:33', '2025-11-27 20:24:33', NULL, 'major', '/api/admin/major/update', 1);
INSERT INTO `sys_permission` VALUES (1261, '专业管理', '删除专业', 'major:delete', 2, '2025-11-27 20:24:33', '2025-11-27 20:24:33', NULL, 'major', '/api/admin/major/delete/{id}', 1);
INSERT INTO `sys_permission` VALUES (1262, '专业管理', '单个查询专业', 'major:query', 2, '2025-11-27 20:24:33', '2025-11-27 20:24:33', NULL, 'major', '/api/admin/major/select', 1);
INSERT INTO `sys_permission` VALUES (1263, '专业管理', '批量增加专业', 'major:batchInsert', 2, '2025-11-27 20:24:33', '2025-11-27 20:24:33', NULL, 'major', '/api/admin/major/batch-insert', 1);
INSERT INTO `sys_permission` VALUES (1264, '专业管理', '导出专业列表', 'major:export', 2, '2025-11-27 20:24:33', '2025-11-27 20:24:33', NULL, 'major', '/api/admin/major/page', 1);
INSERT INTO `sys_permission` VALUES (1265, '班级管理', '班级管理', 'class', 1, '2025-11-27 20:24:33', '2025-11-27 20:24:33', NULL, '', '', 1);
INSERT INTO `sys_permission` VALUES (1266, '班级管理', '查询班级列表', 'class:page', 1, '2025-11-27 20:24:33', '2025-11-27 20:24:33', NULL, 'class', '/api/admin/class/page', 1);
INSERT INTO `sys_permission` VALUES (1267, '班级管理', '创建班级', 'class:create', 2, '2025-11-27 20:24:33', '2025-11-27 20:24:33', NULL, 'class', '/api/admin/class/create', 1);
INSERT INTO `sys_permission` VALUES (1268, '班级管理', '修改班级', 'class:update', 2, '2025-11-27 20:24:33', '2025-11-27 20:24:33', NULL, 'class', '/api/admin/class/update', 1);
INSERT INTO `sys_permission` VALUES (1269, '班级管理', '删除班级', 'class:delete', 2, '2025-11-27 20:24:33', '2025-11-27 20:24:33', NULL, 'class', '/api/admin/class/delete/{id}', 1);
INSERT INTO `sys_permission` VALUES (1270, '班级管理', '单个查询班级', 'class:query', 2, '2025-11-27 20:24:33', '2025-11-27 20:24:33', NULL, 'class', '/api/admin/class/select', 1);
INSERT INTO `sys_permission` VALUES (1271, '班级管理', '批量增加班级', 'class:batchInsert', 2, '2025-11-27 20:24:33', '2025-11-27 20:24:33', NULL, 'class', '/api/admin/class/batch-insert', 1);
INSERT INTO `sys_permission` VALUES (1272, '班级管理', '导出班级列表', 'class:export', 2, '2025-11-27 20:24:33', '2025-11-27 20:24:33', NULL, 'class', '/api/admin/class/page', 1);
INSERT INTO `sys_permission` VALUES (1273, '课程管理', '课程管理', 'course', 1, '2025-11-27 20:24:33', '2025-11-27 20:24:33', NULL, '', '', 1);
INSERT INTO `sys_permission` VALUES (1274, '课程管理', '查询课程列表', 'course:page', 1, '2025-11-27 20:24:33', '2025-11-27 20:24:33', NULL, 'course', '/api/admin/course/page', 1);
INSERT INTO `sys_permission` VALUES (1275, '课程管理', '创建课程', 'course:create', 2, '2025-11-27 20:24:33', '2025-11-27 20:24:33', NULL, 'course', '/api/admin/course/create', 1);
INSERT INTO `sys_permission` VALUES (1276, '课程管理', '修改课程', 'course:update', 2, '2025-11-27 20:24:33', '2025-11-27 20:24:33', NULL, 'course', '/api/admin/course/update', 1);
INSERT INTO `sys_permission` VALUES (1277, '课程管理', '删除课程', 'course:delete', 2, '2025-11-27 20:24:33', '2025-11-27 20:24:33', NULL, 'course', '/api/admin/course/delete/{id}', 1);
INSERT INTO `sys_permission` VALUES (1278, '课程管理', '单个查询课程', 'course:query', 2, '2025-11-27 20:24:33', '2025-11-27 20:24:33', NULL, 'course', '/api/admin/course/select', 1);
INSERT INTO `sys_permission` VALUES (1279, '课程管理', '导出课程列表', 'course:export', 2, '2025-11-27 20:24:33', '2025-11-27 20:24:33', NULL, 'course', '/api/admin/course/page', 1);
INSERT INTO `sys_permission` VALUES (1280, '课程管理', '批量插入课程列表', 'course:batchInsert', 2, '2025-11-27 20:24:33', '2025-11-27 20:24:33', NULL, 'course', '/api/admin/course/batch-insert', 1);
INSERT INTO `sys_permission` VALUES (1281, '排课管理', '排课管理', 'courseSchedule', 1, '2025-11-27 20:24:33', '2025-11-27 20:24:33', NULL, '', '', 1);
INSERT INTO `sys_permission` VALUES (1282, '排课管理', '查询排课列表', 'courseSchedule:page', 1, '2025-11-27 20:24:33', '2025-11-27 20:24:33', NULL, 'courseSchedule', '/api/admin/course-schedule/page', 1);
INSERT INTO `sys_permission` VALUES (1283, '排课管理', '创建排课', 'courseSchedule:create', 2, '2025-11-27 20:24:33', '2025-11-27 20:24:33', NULL, 'courseSchedule', '/api/admin/course-schedule/create', 1);
INSERT INTO `sys_permission` VALUES (1284, '排课管理', '修改排课', 'courseSchedule:update', 2, '2025-11-27 20:24:33', '2025-11-27 20:24:33', NULL, 'courseSchedule', '/api/admin/course-schedule/update', 1);
INSERT INTO `sys_permission` VALUES (1285, '排课管理', '删除排课', 'courseSchedule:delete', 2, '2025-11-27 20:24:33', '2025-11-27 20:24:33', NULL, 'courseSchedule', '/api/admin/course-schedule/delete/{id}', 1);
INSERT INTO `sys_permission` VALUES (1286, '排课管理', '单个查询排课', 'courseSchedule:query', 2, '2025-11-27 20:24:33', '2025-11-27 20:24:33', NULL, 'courseSchedule', '/api/admin/course-schedule/select', 1);
INSERT INTO `sys_permission` VALUES (1287, '排课管理', '查看排课详情', 'courseSchedule:detail', 2, '2025-11-27 20:24:33', '2025-11-27 20:24:33', NULL, 'courseSchedule', '/api/admin/course-schedule/detail/{scheduleId}', 1);
INSERT INTO `sys_permission` VALUES (1288, '排课管理', '导出排课列表', 'courseSchedule:export', 2, '2025-11-27 20:24:33', '2025-11-27 20:24:33', NULL, 'courseSchedule', '/api/admin/course-schedule/page', 1);
INSERT INTO `sys_permission` VALUES (1289, '排课管理', '批量增加排课列表', 'courseSchedule:batchInsert', 2, '2025-11-27 20:24:33', '2025-11-27 20:24:33', NULL, 'courseSchedule', '/api/admin/course-schedule/batch-insert', 1);
INSERT INTO `sys_permission` VALUES (1290, '实验室管理', '实验室管理', 'lab', 1, '2025-11-27 20:24:33', '2025-11-27 20:24:33', NULL, '', '', 1);
INSERT INTO `sys_permission` VALUES (1291, '实验室管理', '查询实验室列表', 'lab:page', 1, '2025-11-27 20:24:33', '2025-11-27 20:24:33', NULL, 'lab', '/api/admin/lab/page', 1);
INSERT INTO `sys_permission` VALUES (1292, '实验室管理', '创建实验室', 'lab:create', 2, '2025-11-27 20:24:33', '2025-11-27 20:24:33', NULL, 'lab', '/api/admin/lab/create', 1);
INSERT INTO `sys_permission` VALUES (1293, '实验室管理', '修改实验室', 'lab:update', 2, '2025-11-27 20:24:33', '2025-11-27 20:24:33', NULL, 'lab', '/api/admin/lab/update', 1);
INSERT INTO `sys_permission` VALUES (1294, '实验室管理', '删除实验室', 'lab:delete', 2, '2025-11-27 20:24:33', '2025-11-27 20:24:33', NULL, 'lab', '/api/admin/lab/delete/{id}', 1);
INSERT INTO `sys_permission` VALUES (1295, '实验室管理', '单个查询实验室', 'lab:query', 2, '2025-11-27 20:24:33', '2025-11-27 20:24:33', NULL, 'lab', '/api/admin/lab/select', 1);
INSERT INTO `sys_permission` VALUES (1296, '实验室管理', '导出实验室列表', 'lab:export', 2, '2025-11-27 20:24:33', '2025-11-27 20:24:33', NULL, 'lab', '/api/admin/lab/page', 1);
INSERT INTO `sys_permission` VALUES (1297, '实验室管理', '批量增加实验室列表', 'lab:batchInsert', 2, '2025-11-27 20:24:33', '2025-11-27 20:24:33', NULL, 'lab', '/api/admin/lab/batch-insert', 1);
INSERT INTO `sys_permission` VALUES (1298, '项目管理', '项目管理', 'project', 1, '2025-11-27 20:24:33', '2025-11-27 20:24:33', NULL, '', '', 1);
INSERT INTO `sys_permission` VALUES (1299, '项目管理', '查询项目列表', 'project:page', 1, '2025-11-27 20:24:33', '2025-11-27 20:24:33', NULL, 'project', '/api/admin/project/page', 1);
INSERT INTO `sys_permission` VALUES (1300, '项目管理', '创建项目', 'project:create', 2, '2025-11-27 20:24:33', '2025-11-27 20:24:33', NULL, 'project', '/api/admin/project/create', 1);
INSERT INTO `sys_permission` VALUES (1301, '项目管理', '修改项目', 'project:update', 2, '2025-11-27 20:24:33', '2025-11-27 20:24:33', NULL, 'project', '/api/admin/project/update', 1);
INSERT INTO `sys_permission` VALUES (1302, '项目管理', '删除项目', 'project:delete', 2, '2025-11-27 20:24:33', '2025-11-27 20:24:33', NULL, 'project', '/api/admin/project/delete/{id}', 1);
INSERT INTO `sys_permission` VALUES (1303, '项目管理', '单个查询项目', 'project:query', 2, '2025-11-27 20:24:33', '2025-11-27 20:24:33', NULL, 'project', '/api/admin/project/select', 1);
INSERT INTO `sys_permission` VALUES (1304, '项目管理', '查看项目详情', 'project:detail', 2, '2025-11-27 20:24:33', '2025-11-27 20:24:33', NULL, 'project', '/api/admin/project/detail/{projectId}', 1);
INSERT INTO `sys_permission` VALUES (1305, '项目管理', '导出项目', 'project:export', 2, '2025-11-27 20:24:33', '2025-11-27 20:24:33', NULL, 'project', '/api/admin/project/page', 1);
INSERT INTO `sys_permission` VALUES (1306, '教学核心管理', '教学核心管理', 'teachingCore', 1, '2025-11-27 20:24:33', '2025-11-27 20:24:33', NULL, '', '', 1);
INSERT INTO `sys_permission` VALUES (1307, '教学核心管理', '查询教学核心列表', 'teachingCore:page', 1, '2025-11-27 20:24:33', '2025-11-27 20:24:33', NULL, 'teachingCore', '/api/admin/teaching-core/page', 1);
INSERT INTO `sys_permission` VALUES (1308, '教学核心管理', '创建教学核心', 'teachingCore:create', 2, '2025-11-27 20:24:33', '2025-11-27 20:24:33', NULL, 'teachingCore', '/api/admin/teaching-core/create', 1);
INSERT INTO `sys_permission` VALUES (1309, '教学核心管理', '修改教学核心', 'teachingCore:update', 2, '2025-11-27 20:24:33', '2025-11-27 20:24:33', NULL, 'teachingCore', '/api/admin/teaching-core/update', 1);
INSERT INTO `sys_permission` VALUES (1310, '教学核心管理', '删除教学核心', 'teachingCore:delete', 2, '2025-11-27 20:24:33', '2025-11-27 20:24:33', NULL, 'teachingCore', '/api/admin/teaching-core/delete/{id}', 1);
INSERT INTO `sys_permission` VALUES (1311, '教学核心管理', '单个查询教学核心', 'teachingCore:query', 2, '2025-11-27 20:24:33', '2025-11-27 20:24:33', NULL, 'teachingCore', '/api/admin/teaching-core/select', 1);
INSERT INTO `sys_permission` VALUES (1312, '教学核心管理', '导出教学核心列表', 'teachingCore:export', 2, '2025-11-27 20:24:33', '2025-11-27 20:24:33', NULL, 'teachingCore', '/api/admin/teaching-core/page', 1);
INSERT INTO `sys_permission` VALUES (1314, '报告管理', '报告管理', 'report', 1, '2025-11-27 20:24:33', '2025-11-27 20:24:33', NULL, '', '', 1);
INSERT INTO `sys_permission` VALUES (1315, '报告管理', '查询所有报告列表', 'report:page', 1, '2025-11-27 20:24:33', '2025-11-27 20:24:33', NULL, 'report', '/api/admin/report/page', 1);
INSERT INTO `sys_permission` VALUES (1316, '报告管理', '查询已提交报告列表', 'report:page:submitted', 1, '2025-11-27 20:24:33', '2025-11-27 20:24:33', NULL, 'report', '/api/admin/report/page/submitted', 1);
INSERT INTO `sys_permission` VALUES (1317, '报告管理', '导出已提交报告列表', 'report:page:submitted:export', 1, '2025-11-27 20:24:33', '2025-11-27 20:40:28', NULL, 'report', '/api/admin/report/page/submitted', 1);
INSERT INTO `sys_permission` VALUES (1318, '报告管理', '查询未提交报告列表', 'report:page:unSubmitted', 1, '2025-11-27 20:24:33', '2025-11-27 20:24:33', NULL, 'report', '/api/admin/report/page/un-submitted', 1);
INSERT INTO `sys_permission` VALUES (1319, '报告管理', '导出未提交报告列表', 'report:page:unSubmitted:export', 1, '2025-11-27 20:24:33', '2025-11-27 20:24:33', NULL, 'report', '/api/admin/report/page/un-submitted', 1);
INSERT INTO `sys_permission` VALUES (1320, '报告管理', '上传未提交报告', 'report:page:unSubmitted:upload', 1, '2025-11-27 20:24:33', '2025-11-27 20:24:33', NULL, 'report', '/api/admin/report/create', 1);
INSERT INTO `sys_permission` VALUES (1321, '报告管理', '查询报告模板列表', 'report:page:template', 1, '2025-11-27 20:24:33', '2025-11-27 20:24:33', NULL, 'report', '/api/admin/report/page/template', 1);
INSERT INTO `sys_permission` VALUES (1322, '报告管理', '创建报告', 'report:create', 2, '2025-11-27 20:24:33', '2025-11-27 20:24:33', NULL, 'report', '/api/admin/report/create', 1);
INSERT INTO `sys_permission` VALUES (1323, '报告管理', '修改报告', 'report:update', 2, '2025-11-27 20:24:33', '2025-11-27 20:24:33', NULL, 'report', '/api/admin/report/update', 1);
INSERT INTO `sys_permission` VALUES (1324, '报告管理', '删除报告', 'report:delete', 2, '2025-11-27 20:24:33', '2025-11-27 20:24:33', NULL, 'report', '/api/admin/report/delete/{id}', 1);
INSERT INTO `sys_permission` VALUES (1325, '报告管理', '单个查询报告', 'report:query', 2, '2025-11-27 20:24:33', '2025-11-27 20:24:33', NULL, 'report', '/api/admin/report/select', 1);
INSERT INTO `sys_permission` VALUES (1326, '报告管理', '查看报告详情', 'report:detail', 2, '2025-11-27 20:24:33', '2025-11-27 20:24:33', NULL, 'report', '/api/admin/report/detail/{reportId}', 1);
INSERT INTO `sys_permission` VALUES (1327, '报告管理', '下载报告', 'report:download', 2, '2025-11-27 20:24:33', '2025-11-27 20:24:33', NULL, 'report', '/api/public/upload/**', 1);
INSERT INTO `sys_permission` VALUES (1328, '报告管理', '导出报告列表', 'report:export', 2, '2025-11-27 20:24:33', '2025-11-27 20:24:33', NULL, 'report', '/api/admin/report/page', 1);
INSERT INTO `sys_permission` VALUES (1329, '报告管理', '检测报告查重', 'report:plagiarism', 2, '2025-11-27 20:24:33', '2025-11-27 20:24:33', NULL, 'report', '/api/admin/report/plagiarism/{reportId}', 1);
INSERT INTO `sys_permission` VALUES (1330, '报告管理', '报告 Word 转 PDF', 'report:wordToPdf', 2, '2025-11-27 20:24:33', '2025-11-27 20:24:33', NULL, 'report', '/api/admin/report/word-to-pdf/{reportId}', 1);
INSERT INTO `sys_permission` VALUES (1331, '报告批阅管理', '报告批阅管理', 'reportReview', 1, '2025-11-27 20:24:33', '2025-11-27 20:24:33', NULL, '', '', 1);
INSERT INTO `sys_permission` VALUES (1332, '报告批阅管理', '查询所有批阅列表', 'reportReview:page', 1, '2025-11-27 20:24:33', '2025-11-27 20:24:33', NULL, 'reportReview', '/api/admin/report-review/page', 1);
INSERT INTO `sys_permission` VALUES (1333, '报告批阅管理', '查询已批阅列表', 'reportReview:page:reviewed', 2, '2025-11-27 20:24:33', '2025-11-27 20:24:33', NULL, 'reportReview', '/api/admin/report-review/page/reviewed', 1);
INSERT INTO `sys_permission` VALUES (1334, '报告批阅管理', '查询未批阅列表', 'reportReview:page:unReviewed', 2, '2025-11-27 20:24:33', '2025-11-27 20:24:33', NULL, 'reportReview', '/api/admin/report-review/page/un-reviewed', 1);
INSERT INTO `sys_permission` VALUES (1335, '报告批阅管理', '批改未批改的报告', 'reportReview:unReview:review', 2, '2025-11-27 20:24:33', '2025-11-27 20:24:33', NULL, 'reportReview', '/api/admin/report-review/create', 1);
INSERT INTO `sys_permission` VALUES (1336, '报告批阅管理', '创建批阅记录', 'reportReview:create', 2, '2025-11-27 20:24:33', '2025-11-27 20:24:33', NULL, 'reportReview', '/api/admin/report-review/create', 1);
INSERT INTO `sys_permission` VALUES (1337, '报告批阅管理', '修改批阅记录', 'reportReview:update', 2, '2025-11-27 20:24:33', '2025-11-27 20:24:33', NULL, 'reportReview', '/api/admin/report-review/update', 1);
INSERT INTO `sys_permission` VALUES (1338, '报告批阅管理', '删除批阅记录', 'reportReview:delete', 2, '2025-11-27 20:24:33', '2025-11-27 20:24:33', NULL, 'reportReview', '/api/admin/report-review/delete/{id}', 1);
INSERT INTO `sys_permission` VALUES (1339, '报告批阅管理', '查看批阅详情', 'reportReview:detail', 2, '2025-11-27 20:24:33', '2025-11-27 20:24:33', NULL, 'reportReview', '/api/admin/report-review/detail/{reviewId}', 1);
INSERT INTO `sys_permission` VALUES (1340, '报告批阅管理', '导出报告批阅列表', 'reportReview:export', 2, '2025-11-27 20:24:33', '2025-11-27 20:24:33', NULL, 'reportReview', '/api/admin/report-review/page', 1);
INSERT INTO `sys_permission` VALUES (1341, '通知管理', '通知管理', 'notice', 1, '2025-11-27 20:24:33', '2025-11-27 20:24:33', NULL, '', '', 1);
INSERT INTO `sys_permission` VALUES (1342, '通知管理', '查询通知列表', 'notice:page', 1, '2025-11-27 20:24:33', '2025-11-27 20:24:33', NULL, 'notice', '/api/admin/notice/page', 1);
INSERT INTO `sys_permission` VALUES (1343, '通知管理', '创建通知', 'notice:create', 2, '2025-11-27 20:24:33', '2025-11-27 20:24:33', NULL, 'notice', '/api/admin/notice/create', 1);
INSERT INTO `sys_permission` VALUES (1344, '通知管理', '修改通知', 'notice:update', 2, '2025-11-27 20:24:33', '2025-11-27 20:24:33', NULL, 'notice', '/api/admin/notice/update', 1);
INSERT INTO `sys_permission` VALUES (1345, '通知管理', '删除通知', 'notice:delete', 2, '2025-11-27 20:24:33', '2025-11-27 20:24:33', NULL, 'notice', '/api/admin/notice/delete/{id}', 1);
INSERT INTO `sys_permission` VALUES (1346, '通知管理', '单个查询通知', 'notice:query', 2, '2025-11-27 20:24:33', '2025-11-27 20:24:33', NULL, 'notice', '/api/admin/notice/select', 1);
INSERT INTO `sys_permission` VALUES (1347, '通知管理', '导出通知列表', 'notice:export', 2, '2025-11-27 20:24:33', '2025-11-27 20:24:33', NULL, 'notice', '/api/admin/notice/page', 1);
INSERT INTO `sys_permission` VALUES (1348, '通知确认管理', '通知确认管理', 'noticeConfirm', 1, '2025-11-27 20:24:33', '2025-11-27 20:24:33', NULL, '', '', 1);
INSERT INTO `sys_permission` VALUES (1349, '通知确认管理', '查询通知确认列表', 'noticeConfirm:page', 1, '2025-11-27 20:24:33', '2025-11-27 20:24:33', NULL, 'noticeConfirm', '/api/admin/notice-confirm/page', 1);
INSERT INTO `sys_permission` VALUES (1350, '通知确认管理', '创建通知确认', 'noticeConfirm:create', 2, '2025-11-27 20:24:33', '2025-11-27 20:24:33', NULL, 'noticeConfirm', '/api/admin/notice-confirm/create', 1);
INSERT INTO `sys_permission` VALUES (1351, '通知确认管理', '修改通知确认', 'noticeConfirm:update', 2, '2025-11-27 20:24:33', '2025-11-27 20:24:33', NULL, 'noticeConfirm', '/api/admin/notice-confirm/update', 1);
INSERT INTO `sys_permission` VALUES (1352, '通知确认管理', '删除通知确认', 'noticeConfirm:delete', 2, '2025-11-27 20:24:33', '2025-11-27 20:24:33', NULL, 'noticeConfirm', '/api/admin/notice-confirm/delete/{id}', 1);
INSERT INTO `sys_permission` VALUES (1353, '通知确认管理', '导出通知确定列表', 'noticeConfirm:export', 2, '2025-11-27 20:24:33', '2025-11-27 20:24:33', NULL, 'noticeConfirm', '/api/admin/notice-confirm/page', 1);
INSERT INTO `sys_permission` VALUES (1354, '字典管理', '字典管理', 'dict', 1, '2025-11-27 20:24:33', '2025-11-27 20:24:33', NULL, '', '', 1);
INSERT INTO `sys_permission` VALUES (1355, '字典管理', '查询字典列表', 'dict:page', 1, '2025-11-27 20:24:33', '2025-11-27 20:24:33', NULL, 'dict', '/api/admin/dict/page', 1);
INSERT INTO `sys_permission` VALUES (1356, '字典管理', '创建字典', 'dict:create', 2, '2025-11-27 20:24:33', '2025-11-27 20:24:33', NULL, 'dict', '/api/admin/dict/create', 1);
INSERT INTO `sys_permission` VALUES (1357, '字典管理', '修改字典', 'dict:update', 2, '2025-11-27 20:24:33', '2025-11-27 20:24:33', NULL, 'dict', '/api/admin/dict/update', 1);
INSERT INTO `sys_permission` VALUES (1358, '字典管理', '删除字典', 'dict:delete', 2, '2025-11-27 20:24:33', '2025-11-27 20:24:33', NULL, 'dict', '/api/admin/dict/delete/{id}', 1);
INSERT INTO `sys_permission` VALUES (1359, '字典管理', '导出字典列表', 'dict:export', 2, '2025-11-27 20:24:33', '2025-11-27 20:24:33', NULL, 'dict', '/api/admin/dict/page', 1);
INSERT INTO `sys_permission` VALUES (1360, '字典管理', '批量新增字典', 'dict:batchInsert', 2, '2025-11-27 20:24:33', '2025-11-27 20:24:33', NULL, 'dict', '/api/admin/dict/batch-insert', 1);
INSERT INTO `sys_permission` VALUES (1361, '转换管理', '转换管理', 'converter', 1, '2025-11-27 20:24:33', '2025-11-27 20:24:33', NULL, '', '', 1);
INSERT INTO `sys_permission` VALUES (1362, '转换管理', '通用 Word 转 PDF', 'converter:wordToPdf', 2, '2025-11-27 20:24:33', '2025-11-27 20:24:33', NULL, 'converter', '/api/converter/word-to-pdf', 1);
INSERT INTO `sys_permission` VALUES (1363, '教学核心管理', '查询详细', 'teachingCore:detail', 2, '2025-11-28 13:36:09', '2025-11-28 13:41:01', NULL, 'teachingCore', '/api/admin/teaching-core/detail/{teachingCoreId}', 1);
INSERT INTO `sys_permission` VALUES (1373, '字典管理', '批量删除字典', 'dict:batchDelete', 2, '2025-11-28 14:35:13', '2025-11-28 14:35:13', NULL, 'dict', '/api/admin/dict/batch-delete', 1);
INSERT INTO `sys_permission` VALUES (1374, '实验室管理', '批量删除实验室', 'lab:batchDelete', 2, '2025-11-28 16:44:27', '2025-11-28 16:44:27', NULL, 'lab', '/api/admin/lab/batch-delete', 1);
INSERT INTO `sys_permission` VALUES (1375, '课程管理', '批量删除课程', 'course:batchDelete', 2, '2025-11-28 17:03:21', '2025-11-28 17:03:21', NULL, 'course', '/api/admin/course/batch-delete', 1);
INSERT INTO `sys_permission` VALUES (1376, '用户管理', '批量删除用户', 'user:batchDelete', 2, '2025-11-28 18:39:48', '2025-11-28 18:39:48', NULL, 'user', '/api/admin/user/batch-delete', 1);
INSERT INTO `sys_permission` VALUES (1377, '分院管理', '批量删除分院', 'dept:batchDelete', 2, '2025-11-28 20:22:37', '2025-11-28 20:22:37', NULL, 'dept', '/api/admin/dept/batch-delete', 1);
INSERT INTO `sys_permission` VALUES (1378, '专业管理', '批量删除专业', 'major:batchDelete', 2, '2025-11-28 20:22:37', '2025-11-28 20:22:37', NULL, 'major', '/api/admin/major/batch-delete', 1);
INSERT INTO `sys_permission` VALUES (1379, '班级管理', '批量删除班级', 'class:batchDelete', 2, '2025-11-28 20:22:37', '2025-11-28 20:22:37', NULL, 'class', '/api/admin/class/batch-delete', 1);

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '角色ID（主键）',
  `role_group` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '角色分组',
  `role_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '角色编码（唯一，如：exp_teacher）',
  `role_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '角色名称（如：实验教师）',
  `create_at` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_at` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_role_name`(`role_name`) USING BTREE,
  UNIQUE INDEX `uk_role_code`(`role_code`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '系统角色表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES (1, 'root', 'R0000', '超级管理员', '2025-11-20 15:26:34', '2025-11-20 15:26:34', NULL);
INSERT INTO `sys_role` VALUES (2, 'admin', 'A1000', '分院管理员', '2025-11-20 15:26:34', '2025-11-27 16:19:11', NULL);
INSERT INTO `sys_role` VALUES (3, 'teacher', 'T2000', '教师', '2025-11-20 15:26:34', '2025-11-20 15:26:34', NULL);
INSERT INTO `sys_role` VALUES (4, 'student', 'S3000', '学生', '2025-11-20 15:26:34', '2025-11-20 15:26:34', NULL);
INSERT INTO `sys_role` VALUES (5, 'vistor', 'V4000', '游客', '2025-11-20 15:26:34', '2025-11-20 15:26:34', NULL);

-- ----------------------------
-- Table structure for sys_role_permission
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_permission`;
CREATE TABLE `sys_role_permission`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '关联ID（主键）',
  `role_id` bigint(0) NOT NULL COMMENT '角色ID（关联sys_role.id）',
  `perm_id` bigint(0) NOT NULL COMMENT '权限ID（关联sys_permission.id）',
  `create_at` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_at` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_role_perm`(`role_id`, `perm_id`) USING BTREE,
  INDEX `idx_perm_id`(`perm_id`) USING BTREE,
  CONSTRAINT `fk_sys_role_perm_perm` FOREIGN KEY (`perm_id`) REFERENCES `sys_permission` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `fk_sys_role_perm_role` FOREIGN KEY (`role_id`) REFERENCES `sys_role` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 3034 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '角色-权限关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role_permission
-- ----------------------------
INSERT INTO `sys_role_permission` VALUES (1826, 3, 1265, '2025-11-28 13:41:28', '2025-11-28 13:41:28');
INSERT INTO `sys_role_permission` VALUES (1827, 3, 1270, '2025-11-28 13:41:28', '2025-11-28 13:41:28');
INSERT INTO `sys_role_permission` VALUES (1828, 3, 1273, '2025-11-28 13:41:28', '2025-11-28 13:41:28');
INSERT INTO `sys_role_permission` VALUES (1829, 3, 1278, '2025-11-28 13:41:28', '2025-11-28 13:41:28');
INSERT INTO `sys_role_permission` VALUES (1830, 3, 1281, '2025-11-28 13:41:28', '2025-11-28 13:41:28');
INSERT INTO `sys_role_permission` VALUES (1831, 3, 1289, '2025-11-28 13:41:28', '2025-11-28 13:41:28');
INSERT INTO `sys_role_permission` VALUES (1832, 3, 1283, '2025-11-28 13:41:28', '2025-11-28 13:41:28');
INSERT INTO `sys_role_permission` VALUES (1833, 3, 1285, '2025-11-28 13:41:28', '2025-11-28 13:41:28');
INSERT INTO `sys_role_permission` VALUES (1834, 3, 1287, '2025-11-28 13:41:28', '2025-11-28 13:41:28');
INSERT INTO `sys_role_permission` VALUES (1835, 3, 1288, '2025-11-28 13:41:28', '2025-11-28 13:41:28');
INSERT INTO `sys_role_permission` VALUES (1836, 3, 1282, '2025-11-28 13:41:28', '2025-11-28 13:41:28');
INSERT INTO `sys_role_permission` VALUES (1837, 3, 1286, '2025-11-28 13:41:28', '2025-11-28 13:41:28');
INSERT INTO `sys_role_permission` VALUES (1838, 3, 1284, '2025-11-28 13:41:28', '2025-11-28 13:41:28');
INSERT INTO `sys_role_permission` VALUES (1839, 3, 1249, '2025-11-28 13:41:28', '2025-11-28 13:41:28');
INSERT INTO `sys_role_permission` VALUES (1840, 3, 1254, '2025-11-28 13:41:28', '2025-11-28 13:41:28');
INSERT INTO `sys_role_permission` VALUES (1841, 3, 1290, '2025-11-28 13:41:28', '2025-11-28 13:41:28');
INSERT INTO `sys_role_permission` VALUES (1842, 3, 1295, '2025-11-28 13:41:28', '2025-11-28 13:41:28');
INSERT INTO `sys_role_permission` VALUES (1843, 3, 1257, '2025-11-28 13:41:28', '2025-11-28 13:41:28');
INSERT INTO `sys_role_permission` VALUES (1844, 3, 1262, '2025-11-28 13:41:28', '2025-11-28 13:41:28');
INSERT INTO `sys_role_permission` VALUES (1845, 3, 1298, '2025-11-28 13:41:28', '2025-11-28 13:41:28');
INSERT INTO `sys_role_permission` VALUES (1846, 3, 1300, '2025-11-28 13:41:28', '2025-11-28 13:41:28');
INSERT INTO `sys_role_permission` VALUES (1847, 3, 1302, '2025-11-28 13:41:28', '2025-11-28 13:41:28');
INSERT INTO `sys_role_permission` VALUES (1848, 3, 1304, '2025-11-28 13:41:28', '2025-11-28 13:41:28');
INSERT INTO `sys_role_permission` VALUES (1849, 3, 1305, '2025-11-28 13:41:28', '2025-11-28 13:41:28');
INSERT INTO `sys_role_permission` VALUES (1850, 3, 1299, '2025-11-28 13:41:28', '2025-11-28 13:41:28');
INSERT INTO `sys_role_permission` VALUES (1851, 3, 1303, '2025-11-28 13:41:28', '2025-11-28 13:41:28');
INSERT INTO `sys_role_permission` VALUES (1852, 3, 1301, '2025-11-28 13:41:28', '2025-11-28 13:41:28');
INSERT INTO `sys_role_permission` VALUES (1853, 3, 1314, '2025-11-28 13:41:28', '2025-11-28 13:41:28');
INSERT INTO `sys_role_permission` VALUES (1854, 3, 1322, '2025-11-28 13:41:28', '2025-11-28 13:41:28');
INSERT INTO `sys_role_permission` VALUES (1855, 3, 1324, '2025-11-28 13:41:28', '2025-11-28 13:41:28');
INSERT INTO `sys_role_permission` VALUES (1856, 3, 1326, '2025-11-28 13:41:28', '2025-11-28 13:41:28');
INSERT INTO `sys_role_permission` VALUES (1857, 3, 1327, '2025-11-28 13:41:28', '2025-11-28 13:41:28');
INSERT INTO `sys_role_permission` VALUES (1858, 3, 1328, '2025-11-28 13:41:28', '2025-11-28 13:41:28');
INSERT INTO `sys_role_permission` VALUES (1859, 3, 1316, '2025-11-28 13:41:28', '2025-11-28 13:41:28');
INSERT INTO `sys_role_permission` VALUES (1860, 3, 1317, '2025-11-28 13:41:28', '2025-11-28 13:41:28');
INSERT INTO `sys_role_permission` VALUES (1861, 3, 1321, '2025-11-28 13:41:28', '2025-11-28 13:41:28');
INSERT INTO `sys_role_permission` VALUES (1862, 3, 1318, '2025-11-28 13:41:28', '2025-11-28 13:41:28');
INSERT INTO `sys_role_permission` VALUES (1863, 3, 1319, '2025-11-28 13:41:28', '2025-11-28 13:41:28');
INSERT INTO `sys_role_permission` VALUES (1864, 3, 1320, '2025-11-28 13:41:28', '2025-11-28 13:41:28');
INSERT INTO `sys_role_permission` VALUES (1865, 3, 1329, '2025-11-28 13:41:28', '2025-11-28 13:41:28');
INSERT INTO `sys_role_permission` VALUES (1866, 3, 1325, '2025-11-28 13:41:28', '2025-11-28 13:41:28');
INSERT INTO `sys_role_permission` VALUES (1867, 3, 1330, '2025-11-28 13:41:28', '2025-11-28 13:41:28');
INSERT INTO `sys_role_permission` VALUES (1868, 3, 1331, '2025-11-28 13:41:28', '2025-11-28 13:41:28');
INSERT INTO `sys_role_permission` VALUES (1869, 3, 1336, '2025-11-28 13:41:28', '2025-11-28 13:41:28');
INSERT INTO `sys_role_permission` VALUES (1870, 3, 1339, '2025-11-28 13:41:28', '2025-11-28 13:41:28');
INSERT INTO `sys_role_permission` VALUES (1871, 3, 1333, '2025-11-28 13:41:28', '2025-11-28 13:41:28');
INSERT INTO `sys_role_permission` VALUES (1872, 3, 1334, '2025-11-28 13:41:28', '2025-11-28 13:41:28');
INSERT INTO `sys_role_permission` VALUES (1873, 3, 1335, '2025-11-28 13:41:28', '2025-11-28 13:41:28');
INSERT INTO `sys_role_permission` VALUES (1874, 3, 1337, '2025-11-28 13:41:28', '2025-11-28 13:41:28');
INSERT INTO `sys_role_permission` VALUES (1875, 3, 1306, '2025-11-28 13:41:28', '2025-11-28 13:41:28');
INSERT INTO `sys_role_permission` VALUES (1876, 3, 1308, '2025-11-28 13:41:28', '2025-11-28 13:41:28');
INSERT INTO `sys_role_permission` VALUES (1877, 3, 1310, '2025-11-28 13:41:28', '2025-11-28 13:41:28');
INSERT INTO `sys_role_permission` VALUES (1878, 3, 1363, '2025-11-28 13:41:28', '2025-11-28 13:41:28');
INSERT INTO `sys_role_permission` VALUES (1879, 3, 1307, '2025-11-28 13:41:28', '2025-11-28 13:41:28');
INSERT INTO `sys_role_permission` VALUES (1880, 3, 1311, '2025-11-28 13:41:28', '2025-11-28 13:41:28');
INSERT INTO `sys_role_permission` VALUES (1881, 3, 1309, '2025-11-28 13:41:28', '2025-11-28 13:41:28');
INSERT INTO `sys_role_permission` VALUES (1882, 3, 1211, '2025-11-28 13:41:28', '2025-11-28 13:41:28');
INSERT INTO `sys_role_permission` VALUES (1883, 3, 1216, '2025-11-28 13:41:28', '2025-11-28 13:41:28');
INSERT INTO `sys_role_permission` VALUES (2347, 1, 1265, '2025-11-28 20:23:00', '2025-11-28 20:23:00');
INSERT INTO `sys_role_permission` VALUES (2348, 1, 1379, '2025-11-28 20:23:00', '2025-11-28 20:23:00');
INSERT INTO `sys_role_permission` VALUES (2349, 1, 1271, '2025-11-28 20:23:00', '2025-11-28 20:23:00');
INSERT INTO `sys_role_permission` VALUES (2350, 1, 1267, '2025-11-28 20:23:00', '2025-11-28 20:23:00');
INSERT INTO `sys_role_permission` VALUES (2351, 1, 1269, '2025-11-28 20:23:00', '2025-11-28 20:23:00');
INSERT INTO `sys_role_permission` VALUES (2352, 1, 1272, '2025-11-28 20:23:00', '2025-11-28 20:23:00');
INSERT INTO `sys_role_permission` VALUES (2353, 1, 1266, '2025-11-28 20:23:00', '2025-11-28 20:23:00');
INSERT INTO `sys_role_permission` VALUES (2354, 1, 1270, '2025-11-28 20:23:00', '2025-11-28 20:23:00');
INSERT INTO `sys_role_permission` VALUES (2355, 1, 1268, '2025-11-28 20:23:00', '2025-11-28 20:23:00');
INSERT INTO `sys_role_permission` VALUES (2356, 1, 1361, '2025-11-28 20:23:00', '2025-11-28 20:23:00');
INSERT INTO `sys_role_permission` VALUES (2357, 1, 1362, '2025-11-28 20:23:00', '2025-11-28 20:23:00');
INSERT INTO `sys_role_permission` VALUES (2358, 1, 1273, '2025-11-28 20:23:00', '2025-11-28 20:23:00');
INSERT INTO `sys_role_permission` VALUES (2359, 1, 1280, '2025-11-28 20:23:00', '2025-11-28 20:23:00');
INSERT INTO `sys_role_permission` VALUES (2360, 1, 1275, '2025-11-28 20:23:00', '2025-11-28 20:23:00');
INSERT INTO `sys_role_permission` VALUES (2361, 1, 1277, '2025-11-28 20:23:00', '2025-11-28 20:23:00');
INSERT INTO `sys_role_permission` VALUES (2362, 1, 1279, '2025-11-28 20:23:00', '2025-11-28 20:23:00');
INSERT INTO `sys_role_permission` VALUES (2363, 1, 1274, '2025-11-28 20:23:00', '2025-11-28 20:23:00');
INSERT INTO `sys_role_permission` VALUES (2364, 1, 1278, '2025-11-28 20:23:00', '2025-11-28 20:23:00');
INSERT INTO `sys_role_permission` VALUES (2365, 1, 1276, '2025-11-28 20:23:00', '2025-11-28 20:23:00');
INSERT INTO `sys_role_permission` VALUES (2366, 1, 1281, '2025-11-28 20:23:00', '2025-11-28 20:23:00');
INSERT INTO `sys_role_permission` VALUES (2367, 1, 1289, '2025-11-28 20:23:00', '2025-11-28 20:23:00');
INSERT INTO `sys_role_permission` VALUES (2368, 1, 1283, '2025-11-28 20:23:00', '2025-11-28 20:23:00');
INSERT INTO `sys_role_permission` VALUES (2369, 1, 1285, '2025-11-28 20:23:00', '2025-11-28 20:23:00');
INSERT INTO `sys_role_permission` VALUES (2370, 1, 1287, '2025-11-28 20:23:00', '2025-11-28 20:23:00');
INSERT INTO `sys_role_permission` VALUES (2371, 1, 1288, '2025-11-28 20:23:00', '2025-11-28 20:23:00');
INSERT INTO `sys_role_permission` VALUES (2372, 1, 1282, '2025-11-28 20:23:00', '2025-11-28 20:23:00');
INSERT INTO `sys_role_permission` VALUES (2373, 1, 1286, '2025-11-28 20:23:00', '2025-11-28 20:23:00');
INSERT INTO `sys_role_permission` VALUES (2374, 1, 1284, '2025-11-28 20:23:00', '2025-11-28 20:23:00');
INSERT INTO `sys_role_permission` VALUES (2375, 1, 1249, '2025-11-28 20:23:00', '2025-11-28 20:23:00');
INSERT INTO `sys_role_permission` VALUES (2376, 1, 1377, '2025-11-28 20:23:00', '2025-11-28 20:23:00');
INSERT INTO `sys_role_permission` VALUES (2377, 1, 1255, '2025-11-28 20:23:00', '2025-11-28 20:23:00');
INSERT INTO `sys_role_permission` VALUES (2378, 1, 1251, '2025-11-28 20:23:00', '2025-11-28 20:23:00');
INSERT INTO `sys_role_permission` VALUES (2379, 1, 1253, '2025-11-28 20:23:00', '2025-11-28 20:23:00');
INSERT INTO `sys_role_permission` VALUES (2380, 1, 1256, '2025-11-28 20:23:00', '2025-11-28 20:23:00');
INSERT INTO `sys_role_permission` VALUES (2381, 1, 1250, '2025-11-28 20:23:00', '2025-11-28 20:23:00');
INSERT INTO `sys_role_permission` VALUES (2382, 1, 1254, '2025-11-28 20:23:00', '2025-11-28 20:23:00');
INSERT INTO `sys_role_permission` VALUES (2383, 1, 1252, '2025-11-28 20:23:00', '2025-11-28 20:23:00');
INSERT INTO `sys_role_permission` VALUES (2384, 1, 1354, '2025-11-28 20:23:00', '2025-11-28 20:23:00');
INSERT INTO `sys_role_permission` VALUES (2385, 1, 1373, '2025-11-28 20:23:00', '2025-11-28 20:23:00');
INSERT INTO `sys_role_permission` VALUES (2386, 1, 1360, '2025-11-28 20:23:00', '2025-11-28 20:23:00');
INSERT INTO `sys_role_permission` VALUES (2387, 1, 1356, '2025-11-28 20:23:00', '2025-11-28 20:23:00');
INSERT INTO `sys_role_permission` VALUES (2388, 1, 1358, '2025-11-28 20:23:00', '2025-11-28 20:23:00');
INSERT INTO `sys_role_permission` VALUES (2389, 1, 1359, '2025-11-28 20:23:00', '2025-11-28 20:23:00');
INSERT INTO `sys_role_permission` VALUES (2390, 1, 1355, '2025-11-28 20:23:00', '2025-11-28 20:23:00');
INSERT INTO `sys_role_permission` VALUES (2391, 1, 1357, '2025-11-28 20:23:00', '2025-11-28 20:23:00');
INSERT INTO `sys_role_permission` VALUES (2392, 1, 1290, '2025-11-28 20:23:00', '2025-11-28 20:23:00');
INSERT INTO `sys_role_permission` VALUES (2393, 1, 1297, '2025-11-28 20:23:00', '2025-11-28 20:23:00');
INSERT INTO `sys_role_permission` VALUES (2394, 1, 1292, '2025-11-28 20:23:00', '2025-11-28 20:23:00');
INSERT INTO `sys_role_permission` VALUES (2395, 1, 1294, '2025-11-28 20:23:00', '2025-11-28 20:23:00');
INSERT INTO `sys_role_permission` VALUES (2396, 1, 1296, '2025-11-28 20:23:00', '2025-11-28 20:23:00');
INSERT INTO `sys_role_permission` VALUES (2397, 1, 1291, '2025-11-28 20:23:00', '2025-11-28 20:23:00');
INSERT INTO `sys_role_permission` VALUES (2398, 1, 1295, '2025-11-28 20:23:00', '2025-11-28 20:23:00');
INSERT INTO `sys_role_permission` VALUES (2399, 1, 1293, '2025-11-28 20:23:00', '2025-11-28 20:23:00');
INSERT INTO `sys_role_permission` VALUES (2400, 1, 1257, '2025-11-28 20:23:00', '2025-11-28 20:23:00');
INSERT INTO `sys_role_permission` VALUES (2401, 1, 1378, '2025-11-28 20:23:00', '2025-11-28 20:23:00');
INSERT INTO `sys_role_permission` VALUES (2402, 1, 1263, '2025-11-28 20:23:00', '2025-11-28 20:23:00');
INSERT INTO `sys_role_permission` VALUES (2403, 1, 1259, '2025-11-28 20:23:00', '2025-11-28 20:23:00');
INSERT INTO `sys_role_permission` VALUES (2404, 1, 1261, '2025-11-28 20:23:00', '2025-11-28 20:23:00');
INSERT INTO `sys_role_permission` VALUES (2405, 1, 1264, '2025-11-28 20:23:00', '2025-11-28 20:23:00');
INSERT INTO `sys_role_permission` VALUES (2406, 1, 1258, '2025-11-28 20:23:00', '2025-11-28 20:23:00');
INSERT INTO `sys_role_permission` VALUES (2407, 1, 1262, '2025-11-28 20:23:00', '2025-11-28 20:23:00');
INSERT INTO `sys_role_permission` VALUES (2408, 1, 1260, '2025-11-28 20:23:00', '2025-11-28 20:23:00');
INSERT INTO `sys_role_permission` VALUES (2409, 1, 1341, '2025-11-28 20:23:00', '2025-11-28 20:23:00');
INSERT INTO `sys_role_permission` VALUES (2410, 1, 1343, '2025-11-28 20:23:00', '2025-11-28 20:23:00');
INSERT INTO `sys_role_permission` VALUES (2411, 1, 1345, '2025-11-28 20:23:00', '2025-11-28 20:23:00');
INSERT INTO `sys_role_permission` VALUES (2412, 1, 1347, '2025-11-28 20:23:00', '2025-11-28 20:23:00');
INSERT INTO `sys_role_permission` VALUES (2413, 1, 1342, '2025-11-28 20:23:00', '2025-11-28 20:23:00');
INSERT INTO `sys_role_permission` VALUES (2414, 1, 1346, '2025-11-28 20:23:00', '2025-11-28 20:23:00');
INSERT INTO `sys_role_permission` VALUES (2415, 1, 1344, '2025-11-28 20:23:00', '2025-11-28 20:23:00');
INSERT INTO `sys_role_permission` VALUES (2416, 1, 1348, '2025-11-28 20:23:00', '2025-11-28 20:23:00');
INSERT INTO `sys_role_permission` VALUES (2417, 1, 1350, '2025-11-28 20:23:00', '2025-11-28 20:23:00');
INSERT INTO `sys_role_permission` VALUES (2418, 1, 1352, '2025-11-28 20:23:00', '2025-11-28 20:23:00');
INSERT INTO `sys_role_permission` VALUES (2419, 1, 1353, '2025-11-28 20:23:00', '2025-11-28 20:23:00');
INSERT INTO `sys_role_permission` VALUES (2420, 1, 1349, '2025-11-28 20:23:00', '2025-11-28 20:23:00');
INSERT INTO `sys_role_permission` VALUES (2421, 1, 1351, '2025-11-28 20:23:00', '2025-11-28 20:23:00');
INSERT INTO `sys_role_permission` VALUES (2422, 1, 1233, '2025-11-28 20:23:00', '2025-11-28 20:23:00');
INSERT INTO `sys_role_permission` VALUES (2423, 1, 1240, '2025-11-28 20:23:00', '2025-11-28 20:23:00');
INSERT INTO `sys_role_permission` VALUES (2424, 1, 1239, '2025-11-28 20:23:00', '2025-11-28 20:23:00');
INSERT INTO `sys_role_permission` VALUES (2425, 1, 1235, '2025-11-28 20:23:00', '2025-11-28 20:23:00');
INSERT INTO `sys_role_permission` VALUES (2426, 1, 1237, '2025-11-28 20:23:00', '2025-11-28 20:23:00');
INSERT INTO `sys_role_permission` VALUES (2427, 1, 1242, '2025-11-28 20:23:00', '2025-11-28 20:23:00');
INSERT INTO `sys_role_permission` VALUES (2428, 1, 1234, '2025-11-28 20:23:00', '2025-11-28 20:23:00');
INSERT INTO `sys_role_permission` VALUES (2429, 1, 1238, '2025-11-28 20:23:00', '2025-11-28 20:23:00');
INSERT INTO `sys_role_permission` VALUES (2430, 1, 1241, '2025-11-28 20:23:00', '2025-11-28 20:23:00');
INSERT INTO `sys_role_permission` VALUES (2431, 1, 1236, '2025-11-28 20:23:00', '2025-11-28 20:23:00');
INSERT INTO `sys_role_permission` VALUES (2432, 1, 1298, '2025-11-28 20:23:00', '2025-11-28 20:23:00');
INSERT INTO `sys_role_permission` VALUES (2433, 1, 1300, '2025-11-28 20:23:00', '2025-11-28 20:23:00');
INSERT INTO `sys_role_permission` VALUES (2434, 1, 1302, '2025-11-28 20:23:00', '2025-11-28 20:23:00');
INSERT INTO `sys_role_permission` VALUES (2435, 1, 1304, '2025-11-28 20:23:00', '2025-11-28 20:23:00');
INSERT INTO `sys_role_permission` VALUES (2436, 1, 1305, '2025-11-28 20:23:00', '2025-11-28 20:23:00');
INSERT INTO `sys_role_permission` VALUES (2437, 1, 1299, '2025-11-28 20:23:00', '2025-11-28 20:23:00');
INSERT INTO `sys_role_permission` VALUES (2438, 1, 1303, '2025-11-28 20:23:00', '2025-11-28 20:23:00');
INSERT INTO `sys_role_permission` VALUES (2439, 1, 1301, '2025-11-28 20:23:00', '2025-11-28 20:23:00');
INSERT INTO `sys_role_permission` VALUES (2440, 1, 1314, '2025-11-28 20:23:00', '2025-11-28 20:23:00');
INSERT INTO `sys_role_permission` VALUES (2441, 1, 1322, '2025-11-28 20:23:00', '2025-11-28 20:23:00');
INSERT INTO `sys_role_permission` VALUES (2442, 1, 1324, '2025-11-28 20:23:00', '2025-11-28 20:23:00');
INSERT INTO `sys_role_permission` VALUES (2443, 1, 1326, '2025-11-28 20:23:00', '2025-11-28 20:23:00');
INSERT INTO `sys_role_permission` VALUES (2444, 1, 1327, '2025-11-28 20:23:00', '2025-11-28 20:23:00');
INSERT INTO `sys_role_permission` VALUES (2445, 1, 1328, '2025-11-28 20:23:00', '2025-11-28 20:23:00');
INSERT INTO `sys_role_permission` VALUES (2446, 1, 1315, '2025-11-28 20:23:00', '2025-11-28 20:23:00');
INSERT INTO `sys_role_permission` VALUES (2447, 1, 1316, '2025-11-28 20:23:00', '2025-11-28 20:23:00');
INSERT INTO `sys_role_permission` VALUES (2448, 1, 1317, '2025-11-28 20:23:00', '2025-11-28 20:23:00');
INSERT INTO `sys_role_permission` VALUES (2449, 1, 1321, '2025-11-28 20:23:00', '2025-11-28 20:23:00');
INSERT INTO `sys_role_permission` VALUES (2450, 1, 1318, '2025-11-28 20:23:00', '2025-11-28 20:23:00');
INSERT INTO `sys_role_permission` VALUES (2451, 1, 1319, '2025-11-28 20:23:00', '2025-11-28 20:23:00');
INSERT INTO `sys_role_permission` VALUES (2452, 1, 1320, '2025-11-28 20:23:00', '2025-11-28 20:23:00');
INSERT INTO `sys_role_permission` VALUES (2453, 1, 1329, '2025-11-28 20:23:00', '2025-11-28 20:23:00');
INSERT INTO `sys_role_permission` VALUES (2454, 1, 1325, '2025-11-28 20:23:00', '2025-11-28 20:23:00');
INSERT INTO `sys_role_permission` VALUES (2455, 1, 1323, '2025-11-28 20:23:00', '2025-11-28 20:23:00');
INSERT INTO `sys_role_permission` VALUES (2456, 1, 1330, '2025-11-28 20:23:00', '2025-11-28 20:23:00');
INSERT INTO `sys_role_permission` VALUES (2457, 1, 1331, '2025-11-28 20:23:00', '2025-11-28 20:23:00');
INSERT INTO `sys_role_permission` VALUES (2458, 1, 1336, '2025-11-28 20:23:00', '2025-11-28 20:23:00');
INSERT INTO `sys_role_permission` VALUES (2459, 1, 1338, '2025-11-28 20:23:00', '2025-11-28 20:23:00');
INSERT INTO `sys_role_permission` VALUES (2460, 1, 1339, '2025-11-28 20:23:00', '2025-11-28 20:23:00');
INSERT INTO `sys_role_permission` VALUES (2461, 1, 1340, '2025-11-28 20:23:00', '2025-11-28 20:23:00');
INSERT INTO `sys_role_permission` VALUES (2462, 1, 1332, '2025-11-28 20:23:00', '2025-11-28 20:23:00');
INSERT INTO `sys_role_permission` VALUES (2463, 1, 1333, '2025-11-28 20:23:00', '2025-11-28 20:23:00');
INSERT INTO `sys_role_permission` VALUES (2464, 1, 1334, '2025-11-28 20:23:00', '2025-11-28 20:23:00');
INSERT INTO `sys_role_permission` VALUES (2465, 1, 1335, '2025-11-28 20:23:00', '2025-11-28 20:23:00');
INSERT INTO `sys_role_permission` VALUES (2466, 1, 1337, '2025-11-28 20:23:00', '2025-11-28 20:23:00');
INSERT INTO `sys_role_permission` VALUES (2467, 1, 1220, '2025-11-28 20:23:00', '2025-11-28 20:23:00');
INSERT INTO `sys_role_permission` VALUES (2468, 1, 1226, '2025-11-28 20:23:00', '2025-11-28 20:23:00');
INSERT INTO `sys_role_permission` VALUES (2469, 1, 1222, '2025-11-28 20:23:00', '2025-11-28 20:23:00');
INSERT INTO `sys_role_permission` VALUES (2470, 1, 1224, '2025-11-28 20:23:00', '2025-11-28 20:23:00');
INSERT INTO `sys_role_permission` VALUES (2471, 1, 1227, '2025-11-28 20:23:00', '2025-11-28 20:23:00');
INSERT INTO `sys_role_permission` VALUES (2472, 1, 1221, '2025-11-28 20:23:00', '2025-11-28 20:23:00');
INSERT INTO `sys_role_permission` VALUES (2473, 1, 1225, '2025-11-28 20:23:00', '2025-11-28 20:23:00');
INSERT INTO `sys_role_permission` VALUES (2474, 1, 1223, '2025-11-28 20:23:00', '2025-11-28 20:23:00');
INSERT INTO `sys_role_permission` VALUES (2475, 1, 1243, '2025-11-28 20:23:00', '2025-11-28 20:23:00');
INSERT INTO `sys_role_permission` VALUES (2476, 1, 1248, '2025-11-28 20:23:00', '2025-11-28 20:23:00');
INSERT INTO `sys_role_permission` VALUES (2477, 1, 1245, '2025-11-28 20:23:00', '2025-11-28 20:23:00');
INSERT INTO `sys_role_permission` VALUES (2478, 1, 1247, '2025-11-28 20:23:00', '2025-11-28 20:23:00');
INSERT INTO `sys_role_permission` VALUES (2479, 1, 1244, '2025-11-28 20:23:00', '2025-11-28 20:23:00');
INSERT INTO `sys_role_permission` VALUES (2480, 1, 1246, '2025-11-28 20:23:00', '2025-11-28 20:23:00');
INSERT INTO `sys_role_permission` VALUES (2481, 1, 1306, '2025-11-28 20:23:00', '2025-11-28 20:23:00');
INSERT INTO `sys_role_permission` VALUES (2483, 1, 1308, '2025-11-28 20:23:00', '2025-11-28 20:23:00');
INSERT INTO `sys_role_permission` VALUES (2484, 1, 1310, '2025-11-28 20:23:00', '2025-11-28 20:23:00');
INSERT INTO `sys_role_permission` VALUES (2485, 1, 1363, '2025-11-28 20:23:00', '2025-11-28 20:23:00');
INSERT INTO `sys_role_permission` VALUES (2486, 1, 1312, '2025-11-28 20:23:00', '2025-11-28 20:23:00');
INSERT INTO `sys_role_permission` VALUES (2487, 1, 1307, '2025-11-28 20:23:00', '2025-11-28 20:23:00');
INSERT INTO `sys_role_permission` VALUES (2488, 1, 1311, '2025-11-28 20:23:00', '2025-11-28 20:23:00');
INSERT INTO `sys_role_permission` VALUES (2489, 1, 1309, '2025-11-28 20:23:00', '2025-11-28 20:23:00');
INSERT INTO `sys_role_permission` VALUES (2490, 1, 1211, '2025-11-28 20:23:00', '2025-11-28 20:23:00');
INSERT INTO `sys_role_permission` VALUES (2491, 1, 1217, '2025-11-28 20:23:00', '2025-11-28 20:23:00');
INSERT INTO `sys_role_permission` VALUES (2492, 1, 1213, '2025-11-28 20:23:00', '2025-11-28 20:23:00');
INSERT INTO `sys_role_permission` VALUES (2493, 1, 1215, '2025-11-28 20:23:00', '2025-11-28 20:23:00');
INSERT INTO `sys_role_permission` VALUES (2494, 1, 1218, '2025-11-28 20:23:00', '2025-11-28 20:23:00');
INSERT INTO `sys_role_permission` VALUES (2495, 1, 1212, '2025-11-28 20:23:00', '2025-11-28 20:23:00');
INSERT INTO `sys_role_permission` VALUES (2496, 1, 1216, '2025-11-28 20:23:00', '2025-11-28 20:23:00');
INSERT INTO `sys_role_permission` VALUES (2497, 1, 1219, '2025-11-28 20:23:00', '2025-11-28 20:23:00');
INSERT INTO `sys_role_permission` VALUES (2498, 1, 1214, '2025-11-28 20:23:00', '2025-11-28 20:23:00');
INSERT INTO `sys_role_permission` VALUES (2499, 1, 1228, '2025-11-28 20:23:00', '2025-11-28 20:23:00');
INSERT INTO `sys_role_permission` VALUES (2500, 1, 1230, '2025-11-28 20:23:00', '2025-11-28 20:23:00');
INSERT INTO `sys_role_permission` VALUES (2501, 1, 1232, '2025-11-28 20:23:00', '2025-11-28 20:23:00');
INSERT INTO `sys_role_permission` VALUES (2502, 1, 1229, '2025-11-28 20:23:00', '2025-11-28 20:23:00');
INSERT INTO `sys_role_permission` VALUES (2503, 1, 1231, '2025-11-28 20:23:00', '2025-11-28 20:23:00');
INSERT INTO `sys_role_permission` VALUES (2904, 2, 1265, '2025-11-28 20:59:45', '2025-11-28 20:59:45');
INSERT INTO `sys_role_permission` VALUES (2905, 2, 1271, '2025-11-28 20:59:45', '2025-11-28 20:59:45');
INSERT INTO `sys_role_permission` VALUES (2906, 2, 1267, '2025-11-28 20:59:45', '2025-11-28 20:59:45');
INSERT INTO `sys_role_permission` VALUES (2907, 2, 1269, '2025-11-28 20:59:45', '2025-11-28 20:59:45');
INSERT INTO `sys_role_permission` VALUES (2908, 2, 1272, '2025-11-28 20:59:45', '2025-11-28 20:59:45');
INSERT INTO `sys_role_permission` VALUES (2909, 2, 1266, '2025-11-28 20:59:45', '2025-11-28 20:59:45');
INSERT INTO `sys_role_permission` VALUES (2910, 2, 1270, '2025-11-28 20:59:45', '2025-11-28 20:59:45');
INSERT INTO `sys_role_permission` VALUES (2911, 2, 1268, '2025-11-28 20:59:45', '2025-11-28 20:59:45');
INSERT INTO `sys_role_permission` VALUES (2912, 2, 1273, '2025-11-28 20:59:45', '2025-11-28 20:59:45');
INSERT INTO `sys_role_permission` VALUES (2913, 2, 1280, '2025-11-28 20:59:45', '2025-11-28 20:59:45');
INSERT INTO `sys_role_permission` VALUES (2914, 2, 1275, '2025-11-28 20:59:45', '2025-11-28 20:59:45');
INSERT INTO `sys_role_permission` VALUES (2915, 2, 1277, '2025-11-28 20:59:45', '2025-11-28 20:59:45');
INSERT INTO `sys_role_permission` VALUES (2916, 2, 1279, '2025-11-28 20:59:45', '2025-11-28 20:59:45');
INSERT INTO `sys_role_permission` VALUES (2917, 2, 1274, '2025-11-28 20:59:45', '2025-11-28 20:59:45');
INSERT INTO `sys_role_permission` VALUES (2918, 2, 1278, '2025-11-28 20:59:45', '2025-11-28 20:59:45');
INSERT INTO `sys_role_permission` VALUES (2919, 2, 1276, '2025-11-28 20:59:45', '2025-11-28 20:59:45');
INSERT INTO `sys_role_permission` VALUES (2920, 2, 1281, '2025-11-28 20:59:45', '2025-11-28 20:59:45');
INSERT INTO `sys_role_permission` VALUES (2921, 2, 1289, '2025-11-28 20:59:45', '2025-11-28 20:59:45');
INSERT INTO `sys_role_permission` VALUES (2922, 2, 1283, '2025-11-28 20:59:45', '2025-11-28 20:59:45');
INSERT INTO `sys_role_permission` VALUES (2923, 2, 1285, '2025-11-28 20:59:45', '2025-11-28 20:59:45');
INSERT INTO `sys_role_permission` VALUES (2924, 2, 1287, '2025-11-28 20:59:45', '2025-11-28 20:59:45');
INSERT INTO `sys_role_permission` VALUES (2925, 2, 1288, '2025-11-28 20:59:45', '2025-11-28 20:59:45');
INSERT INTO `sys_role_permission` VALUES (2926, 2, 1282, '2025-11-28 20:59:45', '2025-11-28 20:59:45');
INSERT INTO `sys_role_permission` VALUES (2927, 2, 1286, '2025-11-28 20:59:45', '2025-11-28 20:59:45');
INSERT INTO `sys_role_permission` VALUES (2928, 2, 1284, '2025-11-28 20:59:45', '2025-11-28 20:59:45');
INSERT INTO `sys_role_permission` VALUES (2929, 2, 1249, '2025-11-28 20:59:45', '2025-11-28 20:59:45');
INSERT INTO `sys_role_permission` VALUES (2930, 2, 1254, '2025-11-28 20:59:45', '2025-11-28 20:59:45');
INSERT INTO `sys_role_permission` VALUES (2931, 2, 1290, '2025-11-28 20:59:45', '2025-11-28 20:59:45');
INSERT INTO `sys_role_permission` VALUES (2932, 2, 1297, '2025-11-28 20:59:45', '2025-11-28 20:59:45');
INSERT INTO `sys_role_permission` VALUES (2933, 2, 1292, '2025-11-28 20:59:45', '2025-11-28 20:59:45');
INSERT INTO `sys_role_permission` VALUES (2934, 2, 1294, '2025-11-28 20:59:45', '2025-11-28 20:59:45');
INSERT INTO `sys_role_permission` VALUES (2935, 2, 1296, '2025-11-28 20:59:45', '2025-11-28 20:59:45');
INSERT INTO `sys_role_permission` VALUES (2936, 2, 1291, '2025-11-28 20:59:45', '2025-11-28 20:59:45');
INSERT INTO `sys_role_permission` VALUES (2937, 2, 1295, '2025-11-28 20:59:45', '2025-11-28 20:59:45');
INSERT INTO `sys_role_permission` VALUES (2938, 2, 1293, '2025-11-28 20:59:45', '2025-11-28 20:59:45');
INSERT INTO `sys_role_permission` VALUES (2939, 2, 1257, '2025-11-28 20:59:45', '2025-11-28 20:59:45');
INSERT INTO `sys_role_permission` VALUES (2940, 2, 1263, '2025-11-28 20:59:45', '2025-11-28 20:59:45');
INSERT INTO `sys_role_permission` VALUES (2941, 2, 1259, '2025-11-28 20:59:45', '2025-11-28 20:59:45');
INSERT INTO `sys_role_permission` VALUES (2942, 2, 1261, '2025-11-28 20:59:45', '2025-11-28 20:59:45');
INSERT INTO `sys_role_permission` VALUES (2943, 2, 1264, '2025-11-28 20:59:45', '2025-11-28 20:59:45');
INSERT INTO `sys_role_permission` VALUES (2944, 2, 1258, '2025-11-28 20:59:45', '2025-11-28 20:59:45');
INSERT INTO `sys_role_permission` VALUES (2945, 2, 1262, '2025-11-28 20:59:45', '2025-11-28 20:59:45');
INSERT INTO `sys_role_permission` VALUES (2946, 2, 1260, '2025-11-28 20:59:45', '2025-11-28 20:59:45');
INSERT INTO `sys_role_permission` VALUES (2947, 2, 1341, '2025-11-28 20:59:45', '2025-11-28 20:59:45');
INSERT INTO `sys_role_permission` VALUES (2948, 2, 1343, '2025-11-28 20:59:45', '2025-11-28 20:59:45');
INSERT INTO `sys_role_permission` VALUES (2949, 2, 1345, '2025-11-28 20:59:45', '2025-11-28 20:59:45');
INSERT INTO `sys_role_permission` VALUES (2950, 2, 1347, '2025-11-28 20:59:45', '2025-11-28 20:59:45');
INSERT INTO `sys_role_permission` VALUES (2951, 2, 1342, '2025-11-28 20:59:45', '2025-11-28 20:59:45');
INSERT INTO `sys_role_permission` VALUES (2952, 2, 1346, '2025-11-28 20:59:45', '2025-11-28 20:59:45');
INSERT INTO `sys_role_permission` VALUES (2953, 2, 1344, '2025-11-28 20:59:45', '2025-11-28 20:59:45');
INSERT INTO `sys_role_permission` VALUES (2954, 2, 1348, '2025-11-28 20:59:45', '2025-11-28 20:59:45');
INSERT INTO `sys_role_permission` VALUES (2955, 2, 1350, '2025-11-28 20:59:45', '2025-11-28 20:59:45');
INSERT INTO `sys_role_permission` VALUES (2956, 2, 1352, '2025-11-28 20:59:45', '2025-11-28 20:59:45');
INSERT INTO `sys_role_permission` VALUES (2957, 2, 1353, '2025-11-28 20:59:45', '2025-11-28 20:59:45');
INSERT INTO `sys_role_permission` VALUES (2958, 2, 1349, '2025-11-28 20:59:45', '2025-11-28 20:59:45');
INSERT INTO `sys_role_permission` VALUES (2959, 2, 1351, '2025-11-28 20:59:45', '2025-11-28 20:59:45');
INSERT INTO `sys_role_permission` VALUES (2960, 2, 1298, '2025-11-28 20:59:45', '2025-11-28 20:59:45');
INSERT INTO `sys_role_permission` VALUES (2961, 2, 1300, '2025-11-28 20:59:45', '2025-11-28 20:59:45');
INSERT INTO `sys_role_permission` VALUES (2962, 2, 1302, '2025-11-28 20:59:45', '2025-11-28 20:59:45');
INSERT INTO `sys_role_permission` VALUES (2963, 2, 1304, '2025-11-28 20:59:45', '2025-11-28 20:59:45');
INSERT INTO `sys_role_permission` VALUES (2964, 2, 1305, '2025-11-28 20:59:45', '2025-11-28 20:59:45');
INSERT INTO `sys_role_permission` VALUES (2965, 2, 1299, '2025-11-28 20:59:45', '2025-11-28 20:59:45');
INSERT INTO `sys_role_permission` VALUES (2966, 2, 1303, '2025-11-28 20:59:45', '2025-11-28 20:59:45');
INSERT INTO `sys_role_permission` VALUES (2967, 2, 1301, '2025-11-28 20:59:45', '2025-11-28 20:59:45');
INSERT INTO `sys_role_permission` VALUES (2968, 2, 1314, '2025-11-28 20:59:45', '2025-11-28 20:59:45');
INSERT INTO `sys_role_permission` VALUES (2969, 2, 1322, '2025-11-28 20:59:45', '2025-11-28 20:59:45');
INSERT INTO `sys_role_permission` VALUES (2970, 2, 1324, '2025-11-28 20:59:45', '2025-11-28 20:59:45');
INSERT INTO `sys_role_permission` VALUES (2971, 2, 1326, '2025-11-28 20:59:45', '2025-11-28 20:59:45');
INSERT INTO `sys_role_permission` VALUES (2972, 2, 1327, '2025-11-28 20:59:45', '2025-11-28 20:59:45');
INSERT INTO `sys_role_permission` VALUES (2973, 2, 1328, '2025-11-28 20:59:45', '2025-11-28 20:59:45');
INSERT INTO `sys_role_permission` VALUES (2974, 2, 1315, '2025-11-28 20:59:45', '2025-11-28 20:59:45');
INSERT INTO `sys_role_permission` VALUES (2975, 2, 1316, '2025-11-28 20:59:45', '2025-11-28 20:59:45');
INSERT INTO `sys_role_permission` VALUES (2976, 2, 1317, '2025-11-28 20:59:45', '2025-11-28 20:59:45');
INSERT INTO `sys_role_permission` VALUES (2977, 2, 1321, '2025-11-28 20:59:45', '2025-11-28 20:59:45');
INSERT INTO `sys_role_permission` VALUES (2978, 2, 1318, '2025-11-28 20:59:45', '2025-11-28 20:59:45');
INSERT INTO `sys_role_permission` VALUES (2979, 2, 1319, '2025-11-28 20:59:45', '2025-11-28 20:59:45');
INSERT INTO `sys_role_permission` VALUES (2980, 2, 1320, '2025-11-28 20:59:45', '2025-11-28 20:59:45');
INSERT INTO `sys_role_permission` VALUES (2981, 2, 1329, '2025-11-28 20:59:45', '2025-11-28 20:59:45');
INSERT INTO `sys_role_permission` VALUES (2982, 2, 1325, '2025-11-28 20:59:45', '2025-11-28 20:59:45');
INSERT INTO `sys_role_permission` VALUES (2983, 2, 1323, '2025-11-28 20:59:45', '2025-11-28 20:59:45');
INSERT INTO `sys_role_permission` VALUES (2984, 2, 1330, '2025-11-28 20:59:45', '2025-11-28 20:59:45');
INSERT INTO `sys_role_permission` VALUES (2985, 2, 1331, '2025-11-28 20:59:45', '2025-11-28 20:59:45');
INSERT INTO `sys_role_permission` VALUES (2986, 2, 1336, '2025-11-28 20:59:45', '2025-11-28 20:59:45');
INSERT INTO `sys_role_permission` VALUES (2987, 2, 1338, '2025-11-28 20:59:45', '2025-11-28 20:59:45');
INSERT INTO `sys_role_permission` VALUES (2988, 2, 1339, '2025-11-28 20:59:45', '2025-11-28 20:59:45');
INSERT INTO `sys_role_permission` VALUES (2989, 2, 1340, '2025-11-28 20:59:45', '2025-11-28 20:59:45');
INSERT INTO `sys_role_permission` VALUES (2990, 2, 1332, '2025-11-28 20:59:45', '2025-11-28 20:59:45');
INSERT INTO `sys_role_permission` VALUES (2991, 2, 1333, '2025-11-28 20:59:45', '2025-11-28 20:59:45');
INSERT INTO `sys_role_permission` VALUES (2992, 2, 1334, '2025-11-28 20:59:45', '2025-11-28 20:59:45');
INSERT INTO `sys_role_permission` VALUES (2993, 2, 1335, '2025-11-28 20:59:45', '2025-11-28 20:59:45');
INSERT INTO `sys_role_permission` VALUES (2994, 2, 1337, '2025-11-28 20:59:45', '2025-11-28 20:59:45');
INSERT INTO `sys_role_permission` VALUES (2995, 2, 1306, '2025-11-28 20:59:45', '2025-11-28 20:59:45');
INSERT INTO `sys_role_permission` VALUES (2996, 2, 1308, '2025-11-28 20:59:45', '2025-11-28 20:59:45');
INSERT INTO `sys_role_permission` VALUES (2997, 2, 1310, '2025-11-28 20:59:45', '2025-11-28 20:59:45');
INSERT INTO `sys_role_permission` VALUES (2998, 2, 1312, '2025-11-28 20:59:45', '2025-11-28 20:59:45');
INSERT INTO `sys_role_permission` VALUES (2999, 2, 1307, '2025-11-28 20:59:45', '2025-11-28 20:59:45');
INSERT INTO `sys_role_permission` VALUES (3000, 2, 1311, '2025-11-28 20:59:45', '2025-11-28 20:59:45');
INSERT INTO `sys_role_permission` VALUES (3001, 2, 1309, '2025-11-28 20:59:45', '2025-11-28 20:59:45');
INSERT INTO `sys_role_permission` VALUES (3002, 2, 1211, '2025-11-28 20:59:45', '2025-11-28 20:59:45');
INSERT INTO `sys_role_permission` VALUES (3003, 2, 1217, '2025-11-28 20:59:45', '2025-11-28 20:59:45');
INSERT INTO `sys_role_permission` VALUES (3004, 2, 1213, '2025-11-28 20:59:45', '2025-11-28 20:59:45');
INSERT INTO `sys_role_permission` VALUES (3005, 2, 1215, '2025-11-28 20:59:45', '2025-11-28 20:59:45');
INSERT INTO `sys_role_permission` VALUES (3006, 2, 1218, '2025-11-28 20:59:45', '2025-11-28 20:59:45');
INSERT INTO `sys_role_permission` VALUES (3007, 2, 1212, '2025-11-28 20:59:45', '2025-11-28 20:59:45');
INSERT INTO `sys_role_permission` VALUES (3008, 2, 1216, '2025-11-28 20:59:45', '2025-11-28 20:59:45');
INSERT INTO `sys_role_permission` VALUES (3009, 2, 1219, '2025-11-28 20:59:45', '2025-11-28 20:59:45');
INSERT INTO `sys_role_permission` VALUES (3010, 2, 1214, '2025-11-28 20:59:45', '2025-11-28 20:59:45');
INSERT INTO `sys_role_permission` VALUES (3011, 4, 1265, '2025-11-28 21:32:49', '2025-11-28 21:32:49');
INSERT INTO `sys_role_permission` VALUES (3012, 4, 1270, '2025-11-28 21:32:49', '2025-11-28 21:32:49');
INSERT INTO `sys_role_permission` VALUES (3013, 4, 1281, '2025-11-28 21:32:49', '2025-11-28 21:32:49');
INSERT INTO `sys_role_permission` VALUES (3014, 4, 1287, '2025-11-28 21:32:49', '2025-11-28 21:32:49');
INSERT INTO `sys_role_permission` VALUES (3015, 4, 1282, '2025-11-28 21:32:49', '2025-11-28 21:32:49');
INSERT INTO `sys_role_permission` VALUES (3016, 4, 1286, '2025-11-28 21:32:49', '2025-11-28 21:32:49');
INSERT INTO `sys_role_permission` VALUES (3017, 4, 1249, '2025-11-28 21:32:49', '2025-11-28 21:32:49');
INSERT INTO `sys_role_permission` VALUES (3018, 4, 1254, '2025-11-28 21:32:49', '2025-11-28 21:32:49');
INSERT INTO `sys_role_permission` VALUES (3019, 4, 1298, '2025-11-28 21:32:49', '2025-11-28 21:32:49');
INSERT INTO `sys_role_permission` VALUES (3020, 4, 1304, '2025-11-28 21:32:49', '2025-11-28 21:32:49');
INSERT INTO `sys_role_permission` VALUES (3021, 4, 1299, '2025-11-28 21:32:49', '2025-11-28 21:32:49');
INSERT INTO `sys_role_permission` VALUES (3022, 4, 1303, '2025-11-28 21:32:49', '2025-11-28 21:32:49');
INSERT INTO `sys_role_permission` VALUES (3023, 4, 1314, '2025-11-28 21:32:49', '2025-11-28 21:32:49');
INSERT INTO `sys_role_permission` VALUES (3024, 4, 1326, '2025-11-28 21:32:49', '2025-11-28 21:32:49');
INSERT INTO `sys_role_permission` VALUES (3025, 4, 1316, '2025-11-28 21:32:49', '2025-11-28 21:32:49');
INSERT INTO `sys_role_permission` VALUES (3026, 4, 1321, '2025-11-28 21:32:49', '2025-11-28 21:32:49');
INSERT INTO `sys_role_permission` VALUES (3027, 4, 1318, '2025-11-28 21:32:49', '2025-11-28 21:32:49');
INSERT INTO `sys_role_permission` VALUES (3028, 4, 1320, '2025-11-28 21:32:49', '2025-11-28 21:32:49');
INSERT INTO `sys_role_permission` VALUES (3029, 4, 1325, '2025-11-28 21:32:49', '2025-11-28 21:32:49');
INSERT INTO `sys_role_permission` VALUES (3030, 4, 1331, '2025-11-28 21:32:49', '2025-11-28 21:32:49');
INSERT INTO `sys_role_permission` VALUES (3031, 4, 1339, '2025-11-28 21:32:49', '2025-11-28 21:32:49');
INSERT INTO `sys_role_permission` VALUES (3032, 4, 1333, '2025-11-28 21:32:49', '2025-11-28 21:32:49');
INSERT INTO `sys_role_permission` VALUES (3033, 4, 1334, '2025-11-28 21:32:49', '2025-11-28 21:32:49');

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '用户ID（主键）',
  `user_account` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '账号（工号/学号，唯一）',
  `user_password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '加密密码（bcrypt）',
  `real_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '真实姓名',
  `gender` tinyint(0) NULL DEFAULT NULL COMMENT '性别（1-男，2-女）',
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '联系电话',
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '电子邮箱',
  `user_type` tinyint(0) NOT NULL COMMENT '用户类型（0-管理员，1-教师，2-学生）',
  `dept_id` bigint(0) NULL DEFAULT NULL COMMENT '所属学院ID（教师用，关联base_dept.id）',
  `class_id` bigint(0) NULL DEFAULT NULL COMMENT '所属班级ID（学生用，关联base_class.id）',
  `user_status` tinyint(0) NULL DEFAULT 1 COMMENT '状态（1-启用，0-禁用）',
  `create_at` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_at` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_user_account`(`user_account`) USING BTREE,
  INDEX `idx_user_type_status`(`user_type`, `user_status`) USING BTREE,
  INDEX `idx_dept_id`(`dept_id`) USING BTREE,
  INDEX `idx_class_id`(`class_id`) USING BTREE,
  CONSTRAINT `fk_sys_user_class` FOREIGN KEY (`class_id`) REFERENCES `base_class` (`id`) ON DELETE SET NULL ON UPDATE RESTRICT,
  CONSTRAINT `fk_sys_user_dept` FOREIGN KEY (`dept_id`) REFERENCES `base_dept` (`id`) ON DELETE SET NULL ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 137 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '系统用户表（含管理员/教师/学生）' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES (1, '2022402093', '$2a$10$HJfpo9XqnDPPe.jeOSIwiee27yTHkB7po0gWL2zDkOUMXd43Wclx2', '龙某人', 1, NULL, '2055174347@qq.com', 2, 1, 1, 1, '2025-11-20 15:17:27', '2025-11-21 14:00:19', NULL);
INSERT INTO `sys_user` VALUES (2, '2022021230', '$2a$10$ZdwAIbkHMFglzMgYW.IjS.ZFK3H0OBXTSjf2QuHokCXzeI9IXOPNq', '杨飞燕', NULL, '', '', 2, 1, 1, 1, '2025-11-20 17:31:02', '2025-11-29 00:21:48', '');
INSERT INTO `sys_user` VALUES (3, '2036', '$2a$10$3bCWQocWml61WV5GzV.Gv.QC8zA6POkbQI6Y.TrWhQkg1I5CDUeRa', '李某', NULL, '', '', 3, NULL, 1, 1, '2025-11-20 17:31:22', '2025-12-03 02:38:19', '');
INSERT INTO `sys_user` VALUES (4, '24445', '$2a$10$/mfW9Q369ONJS.A0IERo3u5ooeJYRHYvEx52AZJ4.GgE9U6uuPa/i', '张某', NULL, '', '', 3, NULL, 1, 1, '2025-11-20 17:31:40', '2025-11-20 17:31:40', '');
INSERT INTO `sys_user` VALUES (29, '123456', '$2a$10$xl.kgWf/wyp7QHiYX5EsGu7LhKNKoSTMFtyb/rRyNYfJJAusjDfAO', '万一', NULL, NULL, NULL, 3, NULL, 1, 1, '2025-11-23 00:06:45', '2025-11-23 21:46:08', NULL);
INSERT INTO `sys_user` VALUES (31, '123458', '$2a$10$giVDFcfyhPaRp0rAgetP7OdWCCu9yJ3nS2E3wca6B06Bi.donwyUy', '啊啊', NULL, '10086', NULL, 3, NULL, NULL, 1, '2025-11-23 00:06:45', '2025-11-27 16:13:02', NULL);
INSERT INTO `sys_user` VALUES (32, '123459', '$2a$10$z5Hi36G2x6IY/bmumiaJ3e7P7ioEzOZ4y5mCeyM26zS0lR/ZjnVlW', '问问', NULL, NULL, NULL, 3, NULL, NULL, 1, '2025-11-23 00:06:45', '2025-11-23 00:06:45', NULL);
INSERT INTO `sys_user` VALUES (33, '123460', '$2a$10$Wh2ffY0dYZZJnVrXg7H2De7ftEI7AMfVv2EO70Qs4SNCLzS.wIrvG', '语言', NULL, NULL, NULL, 3, NULL, NULL, 1, '2025-11-23 00:06:45', '2025-11-23 00:06:45', NULL);
INSERT INTO `sys_user` VALUES (34, '123461', '$2a$10$T.0OpqQiUamdY6csowRO/uuCpr3dJpOu.MTfe2Oyv8G/yoZNcj5XK', '他他', NULL, NULL, NULL, 3, NULL, NULL, 1, '2025-11-23 00:06:45', '2025-11-23 00:06:45', NULL);
INSERT INTO `sys_user` VALUES (35, '123462', '$2a$10$x9REwpWbKF2Gnw.sv6ybIuRsAdU3jflvwO4RrRoPl8Hz5jImuIzTS', '55个', NULL, NULL, NULL, 3, NULL, NULL, 1, '2025-11-23 00:06:45', '2025-11-23 00:06:45', NULL);
INSERT INTO `sys_user` VALUES (36, '123463', '$2a$10$MmgL7J7UfJot50PxY1AhHuyLVR3Vm.picARhVQRvqGGE0Ia7G6y/K', ' 同时', NULL, NULL, NULL, 3, NULL, NULL, 1, '2025-11-23 00:06:45', '2025-11-23 00:06:45', NULL);
INSERT INTO `sys_user` VALUES (37, '123464', '$2a$10$Da8vNq6sKtgNjv3akQaGkuF3EnMEkKW.f3DuUCiK.0RMxvdm1.KKe', '虽说', NULL, NULL, NULL, 3, NULL, NULL, 1, '2025-11-23 00:06:45', '2025-11-23 00:06:45', NULL);
INSERT INTO `sys_user` VALUES (38, '123465', '$2a$10$W4VX3AQquJJAhF4hFB6ouupOV8JnKMuoM/ugbrSbffyhshpSlSxa.', '虽说让人', NULL, NULL, NULL, 3, NULL, NULL, 1, '2025-11-23 00:06:45', '2025-11-23 00:06:45', NULL);
INSERT INTO `sys_user` VALUES (39, '123466', '$2a$10$tYm.7wb11CXu0LhtxeJmBu4SvaMjWudKmF3S8w2FUS3WAGXqWlM5W', '万一', NULL, NULL, NULL, 3, NULL, NULL, 1, '2025-11-23 00:06:45', '2025-11-23 00:06:45', NULL);
INSERT INTO `sys_user` VALUES (40, '123467', '$2a$10$TfG/7/8QDSWt0U8q8VzPYeX0dTQl2EbpLib2sZQpwoA0GFSLsip8C', '万一', NULL, NULL, NULL, 3, NULL, NULL, 1, '2025-11-23 00:06:45', '2025-11-23 00:06:45', NULL);
INSERT INTO `sys_user` VALUES (41, '24567', '$2a$10$has1T7WbFhMPSuPqboF6uuO3GJ47oFR6EG0AEjUw44Lv4yn2iJZ1C', '荔湾2', NULL, NULL, NULL, 2, NULL, NULL, 1, '2025-11-23 00:06:45', '2025-11-23 00:06:45', NULL);
INSERT INTO `sys_user` VALUES (42, '24568', '$2a$10$jGomHZ2lhv9mWeIqZvHxqe9qlelXUhzz2KXKYCiyPCNpd9qTlmA8W', '3问问', NULL, NULL, NULL, 2, NULL, NULL, 1, '2025-11-23 00:06:45', '2025-11-23 00:06:45', NULL);
INSERT INTO `sys_user` VALUES (43, '24569', '$2a$10$/rs/oTJMXDGB2y4XObA1NudAy.YNCVrKks9Zo4vINY3SMxYu7/iL6', '荔湾1', NULL, NULL, NULL, 2, NULL, NULL, 1, '2025-11-23 00:06:45', '2025-11-23 00:06:45', NULL);
INSERT INTO `sys_user` VALUES (44, '24570', '$2a$10$L62oFCpWQ4IkA89wTVu7..2ZIDIHzPwQ5nb7jrlufQIacoNicfWne', '荔湾22', NULL, NULL, NULL, 2, NULL, NULL, 1, '2025-11-23 00:06:45', '2025-11-23 00:06:45', NULL);
INSERT INTO `sys_user` VALUES (45, '24571', '$2a$10$gLj7dv0lxkaxITLoBQpczuNQzwDQQKZa8gygqe7EFxjkQ/F0CYy2.', '荔湾333', NULL, NULL, NULL, 2, NULL, NULL, 1, '2025-11-23 00:06:45', '2025-11-23 00:06:45', NULL);
INSERT INTO `sys_user` VALUES (46, '24572', '$2a$10$.cGmOrEXLCVVdxGcHGBl8.Jz7rJKpIhtYDTzKm/O0pbrPyRcPBffq', '荔湾88', NULL, NULL, NULL, 2, NULL, NULL, 1, '2025-11-23 00:06:45', '2025-11-23 00:06:45', NULL);
INSERT INTO `sys_user` VALUES (47, '24573', '$2a$10$JU5T5eyg5mGtYnVWJy2LpuBCbWYj2ZDv87xqfNIu01taLKQ4/Aot.', '荔湾00', NULL, NULL, NULL, 2, NULL, NULL, 1, '2025-11-23 00:06:45', '2025-11-23 00:06:45', NULL);
INSERT INTO `sys_user` VALUES (48, '24574', '$2a$10$yovHHlzBCk4Ti78REYWUJeQ1Mk3xSmbJwSKhu4vGwNbiZ9n51vUq6', '荔湾88', NULL, NULL, NULL, 2, NULL, NULL, 1, '2025-11-23 00:06:45', '2025-11-23 00:06:45', NULL);
INSERT INTO `sys_user` VALUES (49, '24575', '$2a$10$HRwRxIwAUZGfIS7fBII6DO17cRU6xmBQQbAmXDDDoWTWW/GzCxNVy', '荔湾6', NULL, NULL, NULL, 2, NULL, NULL, 1, '2025-11-23 00:06:45', '2025-11-23 00:06:45', NULL);
INSERT INTO `sys_user` VALUES (50, '24576', '$2a$10$d.TJ5XSMS2MYor8MyY3kJuxT7sC0Wo0yY9XAUVW6mjWEl0BSenOIi', '荔湾与', NULL, NULL, NULL, 2, NULL, NULL, 1, '2025-11-23 00:06:45', '2025-11-23 00:06:45', NULL);
INSERT INTO `sys_user` VALUES (51, '24577', '$2a$10$9U.yDJyzHOWKHM43TAkKYO3LzfNOl5DUtowfxicr3yHuzZyCZ.Uc2', '荔湾让人', NULL, NULL, NULL, 2, NULL, NULL, 1, '2025-11-23 00:06:45', '2025-11-23 00:06:45', NULL);
INSERT INTO `sys_user` VALUES (52, '24578', '$2a$10$mEl3zbyOgzbzf920eswTGe29aAcH0KG7VzYmQrcgbakSqAbS3mtpW', '荔湾让人', NULL, NULL, NULL, 2, NULL, NULL, 1, '2025-11-23 00:06:45', '2025-11-23 00:06:45', NULL);
INSERT INTO `sys_user` VALUES (113, 'w1000001', '$2a$10$/jVAM/sskJFGhKEr4rC6N.thQpcFi8xe0iFFKBoorAH8pgN.AJDvm', '测试用户1', NULL, NULL, NULL, 3, NULL, 4, 1, '2025-11-28 18:45:42', '2025-11-28 18:45:42', NULL);
INSERT INTO `sys_user` VALUES (114, 'w1000002', '$2a$10$LCSZO5ebiOCRBMMqyuBYP.Wzc5IQXobeGKZglFzdnFDvyg656quNK', '测试用户2', NULL, NULL, NULL, 3, NULL, 4, 1, '2025-11-28 18:45:42', '2025-11-28 18:45:42', NULL);
INSERT INTO `sys_user` VALUES (115, 'w1000003', '$2a$10$xsfanS0J9QAOudGIBttKVuQ.dT1gGNpKsnzmV7/vISTqby8PB7hfW', '测试用户3', NULL, NULL, NULL, 3, NULL, 4, 1, '2025-11-28 18:45:42', '2025-11-28 18:45:42', NULL);
INSERT INTO `sys_user` VALUES (116, 'w1000004', '$2a$10$4OMhElSh1LuHE5UTcMnm0eeoYfbPvrInbnCXD3vu5iY8.lwAlqZFa', '测试用户4', NULL, NULL, NULL, 3, NULL, 4, 1, '2025-11-28 18:45:42', '2025-11-28 18:45:42', NULL);
INSERT INTO `sys_user` VALUES (117, 'w1000005', '$2a$10$P.O1CCp/Q4afvxG3zuLO0OV1frjtLGLgAG2L/K3SXsGQCuUsqCHDK', '测试用户5', NULL, NULL, NULL, 3, NULL, 4, 1, '2025-11-28 18:45:42', '2025-11-28 18:45:42', NULL);
INSERT INTO `sys_user` VALUES (118, 'w1000006', '$2a$10$7v7v8HGgZ47ZE2951wxhpuBq.TFG9LRE.uKb3ebrqB/gPaYifHDea', '测试用户6', NULL, NULL, NULL, 3, NULL, 4, 1, '2025-11-28 18:45:42', '2025-11-28 18:45:42', NULL);
INSERT INTO `sys_user` VALUES (119, 'w1000007', '$2a$10$6slPU.6/Ye3.gk7nN1vFy.86O3njan1LYt.1ipKz7xqRV34dh094C', '测试用户7', NULL, NULL, NULL, 3, NULL, 4, 1, '2025-11-28 18:45:42', '2025-11-28 18:45:42', NULL);
INSERT INTO `sys_user` VALUES (120, 'w1000008', '$2a$10$UKhxEiyiMvUuiJKygbAKHe444tTGIyONoReB5SzvFzEAmS1WAsxq6', '测试用户8', NULL, NULL, NULL, 3, NULL, 4, 1, '2025-11-28 18:45:42', '2025-11-28 18:45:42', NULL);
INSERT INTO `sys_user` VALUES (121, 'w1000009', '$2a$10$uV/a99cC5QFkEf.hG1s/tudGGSGuf1xpgMN2b7518.yuP9Q4qYFXW', '测试用户9', NULL, NULL, NULL, 3, NULL, 4, 1, '2025-11-28 18:45:42', '2025-11-28 18:45:42', NULL);
INSERT INTO `sys_user` VALUES (122, 'w1000010', '$2a$10$GRCIVQeVSc3h3b8Okj0eLuWkET5icghgg2UfW74kOP6KwfL68Zu1q', '测试用户10', NULL, NULL, NULL, 3, NULL, 4, 1, '2025-11-28 18:45:42', '2025-11-28 18:45:42', NULL);
INSERT INTO `sys_user` VALUES (123, 'w1000011', '$2a$10$DMkBnqnPDmD3Tws4CT9HN.bayd5pQJu/szq/WwLwqI1iUKfcDJnAO', '测试用户11', NULL, NULL, NULL, 3, NULL, 4, 1, '2025-11-28 18:45:42', '2025-11-28 18:45:42', NULL);
INSERT INTO `sys_user` VALUES (124, 'w1000012', '$2a$10$asch5RkS6UHIa6MisZlbC.2/dh.LEFxLZhT9Z283kkgvBjxLyo5jy', '测试用户12', NULL, NULL, NULL, 3, NULL, 4, 1, '2025-11-28 18:45:42', '2025-11-28 18:45:42', NULL);
INSERT INTO `sys_user` VALUES (125, 'w1000013', '$2a$10$mrch5tyTXAmePeZOGdfo8egNiBVjBUavZbOG81jKy92//Tj16DQtK', '测试用户13', NULL, NULL, NULL, 3, NULL, 4, 1, '2025-11-28 18:45:42', '2025-11-28 18:45:42', NULL);
INSERT INTO `sys_user` VALUES (126, 'w1000014', '$2a$10$jR0zZK4mu/raT10SSjo0oOHy.U7CBYnvkwmc3vBI9qmCeh0XZyyYy', '测试用户14', NULL, NULL, NULL, 2, 1, NULL, 1, '2025-11-28 18:45:42', '2025-11-28 18:45:42', NULL);
INSERT INTO `sys_user` VALUES (127, 'w1000015', '$2a$10$5oADu7BwuMext2dio9PbfOhYUiVygF/kJjDKw4JbFA3YFrRf4KjHm', '测试用户15', NULL, NULL, NULL, 2, 1, NULL, 1, '2025-11-28 18:45:42', '2025-11-28 18:45:42', NULL);
INSERT INTO `sys_user` VALUES (128, 'w1000016', '$2a$10$DmRIB7I2YYeeogfmg9PR3OfSIT06whBhLgO0GQkye3GvIHDZAF1eq', '测试用户16', NULL, NULL, NULL, 2, 1, NULL, 1, '2025-11-28 18:45:42', '2025-11-28 18:45:42', NULL);
INSERT INTO `sys_user` VALUES (129, 'w1000017', '$2a$10$q3OUYrbOGXNrXqKnqWrEduRV/wcZcH9u3UUwZTbW9Amg3.wrLKkQO', '测试用户17', NULL, NULL, NULL, 2, 1, NULL, 1, '2025-11-28 18:45:42', '2025-11-28 18:45:42', NULL);
INSERT INTO `sys_user` VALUES (130, 'w1000018', '$2a$10$RqLtNh39nVl1IVsSlxDqpuoNoUWbPFEUXtdR2kOsQ.4ekjC7mbCW2', '测试用户18', NULL, NULL, NULL, 2, 1, NULL, 1, '2025-11-28 18:45:42', '2025-11-28 18:45:42', NULL);
INSERT INTO `sys_user` VALUES (131, 'w1000019', '$2a$10$DDYPM/Ddxfjo187um95yeu9IEwUF9dfMWeEl9x7MC7JJG48IQb6VW', '测试用户19', NULL, NULL, NULL, 2, 1, NULL, 1, '2025-11-28 18:45:42', '2025-11-28 18:45:42', NULL);
INSERT INTO `sys_user` VALUES (132, 'w1000020', '$2a$10$yrSIz2v7NS7/X42EymWfFOOcDzNU7ju8tMgNILzdANbj4O90b.9yS', '测试用户20', NULL, NULL, NULL, 2, 1, NULL, 1, '2025-11-28 18:45:42', '2025-11-28 18:45:42', NULL);
INSERT INTO `sys_user` VALUES (133, 'w1000021', '$2a$10$5Xk67NCVu3sDF1cXJgt1IeMjMk/PqxjBaqmemVVZV2jFOP.1tms6e', '测试用户21', NULL, NULL, NULL, 2, 1, NULL, 1, '2025-11-28 18:45:42', '2025-11-28 18:45:42', NULL);
INSERT INTO `sys_user` VALUES (134, 'w1000022', '$2a$10$mdRlGboJkXyEehZ.vsxZJeH4Xp/v0xFuEM0RehUNTwKxKP62HBjkS', '测试用户22', NULL, NULL, NULL, 2, 1, NULL, 1, '2025-11-28 18:45:42', '2025-11-28 18:45:42', NULL);
INSERT INTO `sys_user` VALUES (135, 'w1000023', '$2a$10$p/jlA7RLWWzXvs3TfLjl2epWihnMAW7TzBUZzRmMIwpOABEVAM11a', '测试用户23', NULL, NULL, NULL, 2, 1, NULL, 1, '2025-11-28 18:45:42', '2025-11-28 18:45:42', NULL);
INSERT INTO `sys_user` VALUES (136, 'w1000024', '$2a$10$/nD8sVdRq68ZYI6w7Ee0he77jPDQ1rn.GsZJRFw4KEeaUiFNK/qKW', '测试用户24', NULL, NULL, NULL, 2, 1, NULL, 1, '2025-11-28 18:45:42', '2025-11-28 18:45:42', NULL);

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '关联ID（主键）',
  `user_id` bigint(0) NOT NULL COMMENT '用户ID（关联sys_user.id）',
  `role_id` bigint(0) NOT NULL COMMENT '角色ID（关联sys_role.id）',
  `create_at` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_at` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `dept_id` bigint(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_user_role`(`user_id`, `role_id`) USING BTREE,
  INDEX `idx_role_id`(`role_id`) USING BTREE,
  INDEX `fk_sys_user_role_dept`(`dept_id`) USING BTREE,
  CONSTRAINT `fk_sys_user_role_dept` FOREIGN KEY (`dept_id`) REFERENCES `base_dept` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_sys_user_role_role` FOREIGN KEY (`role_id`) REFERENCES `sys_role` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `fk_sys_user_role_user` FOREIGN KEY (`user_id`) REFERENCES `sys_user` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户-角色关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
INSERT INTO `sys_user_role` VALUES (1, 1, 1, '2025-11-20 15:26:47', '2025-11-20 15:26:47', NULL);
INSERT INTO `sys_user_role` VALUES (2, 1, 2, '2025-11-20 15:26:54', '2025-11-26 14:31:10', 1);
INSERT INTO `sys_user_role` VALUES (3, 1, 3, '2025-11-20 15:26:58', '2025-11-20 15:26:58', NULL);
INSERT INTO `sys_user_role` VALUES (4, 1, 4, '2025-11-20 15:27:03', '2025-11-20 15:27:03', NULL);
INSERT INTO `sys_user_role` VALUES (5, 2, 3, '2025-11-29 01:03:20', '2025-11-29 01:03:20', 1);
INSERT INTO `sys_user_role` VALUES (6, 3, 4, '2025-12-03 02:39:05', '2025-12-03 02:39:05', 1);

SET FOREIGN_KEY_CHECKS = 1;
