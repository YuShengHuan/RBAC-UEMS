<template>
  <el-dialog
      class="project-detail-page"
      v-model="visible"
      :show-close="true"
      width="95%"
  >
    <!-- 头部：返回按钮+标题 -->
    <div class="detail-header">
      <h1 class="page-title">{{ detail.projectName }}</h1>
    </div>

    <!-- 核心详情区：带柔和背景，一行一条布局 -->
    <div class="detail-content">
      <div class="detail-list">
        <!-- 项目编码 -->
        <div class="list-item">
          <span class="item-label">项目编码：</span>
          <span class="item-value">{{ detail.projectCode }}</span>
        </div>
        <!-- 项目名称 -->
        <div class="list-item">
          <span class="item-label">项目名称：</span>
          <span class="item-value">{{ detail.projectName }}</span>
        </div>
        <!-- 学期 -->
        <div class="list-item">
          <span class="item-label">学期：</span>
          <span class="item-value">{{ detail.semester }}</span>
        </div>
        <div class="list-item">
          <span class="item-label">工号：</span>
          <span class="item-value">{{ detail.userAccount }}</span>
        </div>
        <!-- 授课教师 -->
        <div class="list-item">
          <span class="item-label">授课教师：</span>
          <span class="item-value">{{ detail.realName }}</span>
        </div>
        <!-- 课程名称 -->
        <div class="list-item">
          <span class="item-label">课程名称：</span>
          <span class="item-value">{{ detail.courseName }}</span>
        </div>
        <!-- 班级名称 -->
        <div class="list-item">
          <span class="item-label">班级名称：</span>
          <span class="item-value">{{ detail.className }}</span>
        </div>
        <!-- 授课周次 -->
        <div class="list-item">
          <span class="item-label">授课周次：</span>
          <span class="item-value">{{ detail.projectWeekRange }}</span>
        </div>
        <!-- 周学时数 -->
        <div class="list-item">
          <span class="item-label">周学时数：</span>
          <span class="item-value">{{ detail.weeklyHours }}</span>
        </div>
        <!-- 计划学时数 -->
        <div class="list-item">
          <span class="item-label">计划学时数：</span>
          <span class="item-value">{{ detail.planHours }}</span>
        </div>
        <!-- 实际学时数 -->
        <div class="list-item">
          <span class="item-label">实际学时数：</span>
          <span class="item-value">{{ detail.actualHours }}</span>
        </div>
        <!-- 实验类别 -->
        <div class="list-item">
          <span class="item-label">实验类别：</span>
          <span class="item-value">{{ findLabelByValue(expCategoryOptions, detail.expCategory) }}</span>
        </div>
        <!-- 实验类型 -->
        <div class="list-item">
          <span class="item-label">实验类型：</span>
          <span class="item-value">{{ findLabelByValue(expTypeOptions, detail.expType) }}</span>
        </div>
        <!-- 所属学科 -->
        <div class="list-item">
          <span class="item-label">所属学科：</span>
          <span class="item-value">{{ findLabelByValue(subjectOptions, detail.subject) }}</span>
        </div>
        <!-- 分组人数 -->
        <div class="list-item">
          <span class="item-label">分组人数：</span>
          <span class="item-value">{{ detail.groupNum }}</span>
        </div>
        <!-- 实验者类别 -->
        <div class="list-item">
          <span class="item-label">实验者类别：</span>
          <span class="item-value">{{ findLabelByValue(expPersonTypeOptions, detail.expPersonType) }}</span>
        </div>
        <!-- 实验要求 -->
        <div class="list-item">
          <span class="item-label">实验要求：</span>
          <span class="item-value">{{ findLabelByValue(expRequirementOptions, detail.expRequirement) }}</span>
        </div>
        <!-- 备注 -->
        <div class="list-item">
          <span class="item-label">备注：</span>
          <span class="item-value">{{ detail.remark || '无' }}</span>
        </div>
        <!-- 创建时间 -->
        <div class="list-item">
          <span class="item-label">创建时间：</span>
          <span class="item-value">{{ detail.createAt }}</span>
        </div>
        <!-- 更新时间 -->
        <div class="list-item">
          <span class="item-label">更新时间：</span>
          <span class="item-value">{{ detail.updateAt }}</span>
        </div>
      </div>
    </div>
  </el-dialog>
</template>

<script setup lang="ts">
import {defineExpose, onMounted, ref} from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { findLabelByValue } from "../../../../utils/commonUtil";

import { baseApi } from "../../../../config/module/AdminExpProjectConfig";
import request from "../../../../request";

import {
  expCategoryOptions,
  expTypeOptions,
  subjectOptions,
  expPersonTypeOptions,
  expRequirementOptions
} from "../../../../utils/globalOptionsUtil";
import {ElMessage} from "element-plus";

// 类型定义：根据 tableDataConfig 生成
interface ProjectDetail {
  id: string;
  projectCode: string;
  projectName: string;
  semester: string;
  userAccount:string;
  realName: string;
  courseName: string;
  className: string;
  projectWeekRange: string;
  weeklyHours: number;
  planHours: number;
  actualHours: number;
  expCategory: number | string;
  expType: number | string;
  subject: number | string;
  groupNum: number;
  expPersonType: number | string;
  expRequirement: number | string;
  remark: string;
  createAt: string;
  updateAt: string;
}

// 项目详情数据
const detail = ref<ProjectDetail>({} as ProjectDetail);
const router = useRouter();
// 弹窗显示状态
const visible = ref(false);

// 设置弹窗显示/隐藏
const setVisible = async (val: boolean,id:number) => {
  visible.value = val;
  try {
    if (isNaN(Number(id)) || Number(id) <= 0) {
      throw new Error("参数错误");
    }
    // 请求项目详情数据
    const res = await request.get(`${baseApi}/detail/${id}`);
    if (res.status === 200) {
      detail.value = res.data;
    } else {
      throw new Error("请求失败");
    }
  } catch (e) {
    ElMessage.error("页面加载失败，参数错误")
    visible.value=false
  }

};
// 暴露给父组件的方法
defineExpose({
  setVisible
});
</script>

<style scoped>
/* 全局样式：柔和背景渐变 */
.project-detail-page {
  padding: 20px 30px;
  width: calc(100% - 60px);
  height: calc(100% - 40px);
  overflow: auto;
  font-family: "Microsoft YaHei", Arial, sans-serif;
  background: linear-gradient(180deg, #f8f9fa 0%, #ffffff 100%);
}

/* 头部样式 */
.detail-header {
  display: flex;
  align-items: center;
  gap: 15px;
  margin-bottom: 20px;
  padding-bottom: 12px;
  border-bottom: 1px solid #e9ecef;
}

.page-title {
  font-size: 20px;
  font-weight: 600;
  color: #2d3748;
  margin: 0;
}

/* 详情内容区 */
.detail-content {
  margin: 0 auto;
  padding: 25px;
  border-radius: 10px;
  background-color: rgba(255, 255, 255, 0.85);
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.03);
}

/* 列表容器 */
.detail-list {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

/* 列表项 */
.list-item {
  display: flex;
  align-items: flex-start; /* 改为flex-start以适应多行文本 */
  font-size: 14px;
  padding-bottom: 12px;
  border-bottom: 1px solid #f0f0f0;
}

.list-item:last-child {
  border-bottom: none;
  padding-bottom: 0;
}

/* 标签 */
.item-label {
  width: 110px;
  color: #6c757d;
  text-align: right;
  margin-right: 15px;
  flex-shrink: 0;
  font-weight: 500;
  padding-top: 2px; /* 轻微调整垂直对齐 */
}

/* 内容 */
.item-value {
  color: #2d3748;
  flex: 1;
  line-height: 1.6;
  word-break: break-word; /* 处理长文本换行 */
}

/* 响应式调整 */
@media (max-width: 768px) {
  .project-detail-page {
    padding: 15px;
    width: calc(100% - 30px);
  }
  .detail-content {
    padding: 15px;
  }
  .list-item {
    flex-direction: column;
    align-items: flex-start;
    gap: 4px;
    padding-bottom: 15px;
  }
  .item-label {
    width: auto;
    text-align: left;
    margin-right: 0;
    color: #868e96;
  }
}
</style>