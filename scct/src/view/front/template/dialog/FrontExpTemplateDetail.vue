<template>
  <el-dialog
      class="project-detail-page"
      v-model="visible"
      :show-close="true"
      width="95%"
  >
    <!-- 头部：返回按钮+标题，简洁分隔 -->
    <div class="detail-header">
      <h1 class="page-title">{{ detail.projectName }}</h1>
    </div>
    <!-- 核心详情区：带柔和背景，一行一条布局 -->
    <div class="detail-content">
      <div class="detail-list">
        <div class="list-item">
          <span class="item-label">学期：</span>
          <span class="item-value">{{ detail.semester }}</span>
        </div>
        <div class="list-item">
          <span class="item-label">课程名称：</span>
          <span class="item-value">{{ detail.courseName }}</span>
        </div>
        <div class="list-item">
          <span class="item-label">项目名称：</span>
          <span class="item-value">{{ detail.projectName }}</span>
        </div>
        <div class="list-item">
          <span class="item-label">班级名称：</span>
          <span class="item-value">{{ detail.className }}</span>
        </div>
        <div class="list-item">
          <span class="item-label">上传用户：</span>
          <span class="item-value">{{ detail.uploadRealName }}</span>
        </div>
        <div class="list-item">
          <span class="item-label">用户类型：</span>
          <span class="item-value">{{ findLabelByValue(userTypeOptions,detail.uploadUserType) }}</span>
        </div>
        <div class="list-item">
          <span class="item-label">文件路径：</span>
          <span class="item-value">{{ detail.filePath }}</span>
          <el-button class="download-button" type="primary"
                     @click="downloadFile(detail.filePath,detail.courseName+'+'+detail.projectName+'+'+findLabelByValue(attachmentTypeOptions, detail.attachmentType))">下载</el-button>
        </div>
        <div class="list-item">
          <span class="item-label">附件类型：</span>
          <span class="item-value">{{ findLabelByValue(attachmentTypeOptions, detail.attachmentType) }}</span>
        </div>
        <div class="list-item">
          <span class="item-label">备注：</span>
          <span class="item-value">{{ detail.remark || '无' }}</span>
        </div>
        <div class="list-item">
          <span class="item-label">创建时间：</span>
          <span class="item-value">{{ detail.createAt }}</span>
        </div>
        <div class="list-item">
          <span class="item-label">更新时间：</span>
          <span class="item-value">{{ detail.updateAt }}</span>
        </div>
      </div>
    </div>
  </el-dialog>
</template>

<script setup lang="ts">
import {defineExpose,ref} from 'vue';
import {attachmentTypeOptions,userTypeOptions} from "../../../../utils/globalOptionsUtil"
import { findLabelByValue,downloadFile } from "../../../../utils/commonUtil";
import { baseApi } from "../../../../config/module/AdminExpReportConfig";
import request from "../../../../request";
import {ElMessage} from "element-plus";

// 类型定义
// 类型定义：对应报告模板数据结构
interface ReportDetail {
  semester: string;
  courseName: string;
  projectName: string;
  className: string;
  uploadRealName: string;
  uploadUserType:number;
  filePath: string;
  attachmentType: number | string;
  remark: string;
  createAt: string;
  updateAt: string;
}

// 模拟从API获取的数据
const detail = ref<ReportDetail>({} as ReportDetail);
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