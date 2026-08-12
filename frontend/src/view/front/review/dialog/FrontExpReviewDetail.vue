<template>
  <el-dialog
      class="project-detail-page"
      v-model="visible"
      :show-close="true"
      width="95%"
  >
    <!-- 头部：返回按钮 + 标题 -->
    <div class="detail-header">
      <h1 class="page-title">{{ detail.projectName }} <span style="margin-left: 10px;">批改详情</span></h1>
    </div>

    <!-- 核心详情区 -->
    <div class="detail-content">
      <div class="detail-list">
        <!-- 基础信息 -->
        <div class="list-item">
          <span class="item-label">学期：</span>
          <span class="item-value">{{ detail.semester }}</span>
        </div>
        <div class="list-item">
          <span class="item-label">课程名称：</span>
          <span class="item-value">{{ detail.courseName }}</span>
        </div>
        <div class="list-item">
          <span class="item-label">实验项目：</span>
          <span class="item-value">{{ detail.projectName }}</span>
        </div>
        <div class="list-item">
          <span class="item-label">班级：</span>
          <span class="item-value">{{ detail.className }}</span>
        </div>
        <!-- 提交与批改信息 -->
        <div class="list-item">
          <span class="item-label">提交学生：</span>
          <span class="item-value">{{ detail.uploadRealName }}</span>
        </div>
        <div class="list-item">
          <span class="item-label">批改教师：</span>
          <span class="item-value">{{ detail.reviewRealName }}</span>
        </div>
        <div class="list-item">
          <span class="item-label">成绩：</span>
          <span class="item-value score-value">{{ detail.score }}</span>
        </div>
        <div class="list-item">
          <span class="item-label">查重率：</span>
          <span class="item-value plagiarism-value" :class="{'high-plagiarism': detail.plagiarismRate > 30}">
            {{ (detail.plagiarismRate*100).toFixed(2) }}%
          </span>
        </div>
        <div class="list-item review-comment-item">
          <span class="item-label">批阅意见：</span>
          <div class="item-value comment-content">
            <span v-if="detail.reviewComment">{{ detail.reviewComment }}</span>
            <span v-else class="no-comment">无</span>
          </div>
        </div>
        <!-- 时间信息 -->
        <div class="list-item">
          <span class="item-label">创建时间：</span>
          <span class="item-value">{{ formatDateTime(detail.createAt) }}</span>
        </div>
        <div class="list-item">
          <span class="item-label">更新时间：</span>
          <span class="item-value">{{ formatDateTime(detail.updateAt) }}</span>
        </div>
      </div>
    </div>
  </el-dialog>
</template>

<script setup lang="ts">
import {defineExpose,ref} from 'vue';
import request from "../../../../request";
import {ElMessage} from "element-plus";
import {formatDateTime} from '../../../../utils/commonUtil'
import { baseApi } from "../../../../config/module/AdminExpReportReviewConfig"; // 改为报告相关API配置
interface ReportReviewDetail {
  id: number;
  semester: string;
  courseName: string;
  projectName: string;
  className: string;
  uploadRealName: string;
  reviewRealName: string;
  score: number;
  reviewComment: string;
  plagiarismRate: number;
  createAt: string; // 学生提交时间
  updateAt: string; // 批改更新时间
}

// 响应式数据
const detail = ref<ReportReviewDetail>({} as ReportReviewDetail);
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