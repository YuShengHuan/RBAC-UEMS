<template>
  <el-dialog
      v-model="visible"
      :title="currentFormStatus ? '更新数据' : '创建数据'"
      :show-close="true"
      width="95%"
  >
    <div class="review-container">
      <!-- 顶部信息栏 -->
      <div class="review-header">
        <div class="header-main">
          <h2 class="review-title">实验报告批改</h2>
        </div>
        <div class="header-actions">
          <el-button
              type="primary"
              @click="handlePreview"
          >
            预览报告
          </el-button>
        </div>
      </div>
      <!-- 核心内容区 -->
      <div class="review-content">
        <!-- 左侧：核心操作区 -->
        <div class="content-primary">
          <!-- 查重结果卡片 -->
          <div class="plagiarism-card">
            <div class="plagiarism-overview">
              <div class="plagiarism-rate">
                <span class="rate-label">整体相似度：</span>
                <span class="rate-value" v-if="plagiarismResult.plagiarismRate !== null">{{ plagiarismResult.plagiarismRate*100 }}%</span>
                <span class="rate-value" v-else>--</span>
              </div>
              <div class="plagiarism-status" v-if="!isCheckingPlagiarism">
                <span v-if="plagiarismResult.similarReports.length > 0">
                  <i class="el-icon-warning"></i> 发现 {{ plagiarismResult.similarReports.length }} 篇相似报告
                </span>
                <span v-if="plagiarismResult.similarReports.length === 0">
                  <i class="el-icon-success"></i> 未发现相似报告
                </span>
              </div>
            </div>

            <!-- 相似报告表格 -->
            <div class="similar-reports-table" v-if="!isCheckingPlagiarism">
              <el-table
                  :data="plagiarismResult.similarReports"
                  border
                  class="el-table"
                  :max-height="tableMaxHeight"
                  style="width: 100%;"
                  empty-text="未发现与本班同学报告有明显相似内容"
              >
                <el-table-column
                    prop="uploadUserAccount"
                    label="学号"
                    align="center"
                />
                <el-table-column
                    prop="uploadRealName"
                    label="学生姓名"
                    align="center"
                />
                <el-table-column
                    prop="similarity"
                    label="相似度"
                    align="center"
                    width="120"
                >
                  <template #default="scope">
                    <span :class="scope.row.similarity*100 > 50 ? 'high-similarity' : 'low-similarity'">
                      {{ (scope.row.similarity*100).toFixed(2) }}%
                    </span>
                  </template>
                </el-table-column>
                <el-table-column
                    label="操作"
                    align="center"
                    width="120"
                >
                  <template #default="scope">
                    <el-button type="primary" @click="convertWordToPdf(scope.row.reportId)">查看</el-button>
                  </template>
                </el-table-column>
              </el-table>
            </div>
            <div class="checking-placeholder" v-else>
              <el-icon size="small"><Loading/></el-icon>
              <span>正在进行查重分析，请稍候...</span>
            </div>
          </div>

          <!-- 分数卡片 -->
          <div class="score-card">
            <p class="card-label">最终成绩</p>
            <div class="score-input">
              <el-input
                  style="height: 50px;"
                  v-model.number="formData.score"
                  type="number"
                  :min="0"
                  :max="100"
                  placeholder="输入成绩"
              ></el-input>
            </div>
          </div>

          <!-- 评论卡片 -->
          <div class="comment-card">
            <p class="card-label">批阅意见</p>
            <el-input
                v-model="formData.reviewComment"
                type="textarea"
                :rows="5"
                placeholder="请输入对该报告的批阅意见..."
                maxlength="500"
            ></el-input>
            <div class="char-count">
              {{ formData.reviewComment.length }} / 500
            </div>
          </div>
        </div>
      </div>

      <!-- 底部操作栏 -->
      <div class="review-actions">
        <el-button
            type="primary"
            class="action-btn save-btn"
            :loading="isSaving"
            @click="handleSave"
        >
          <i class="el-icon-check"></i>
          保存批改结果
        </el-button>
        <el-button
            class="action-btn cancel-btn"
            @click="handleCancel"
        >
          <i class="el-icon-close"></i>
          取消
        </el-button>
      </div>
    </div>
  </el-dialog>
  <PdfPreview
      ref="pdfPreView"
  />
</template>

<script setup lang="ts">
import {computed, reactive, ref, defineEmits, defineExpose, nextTick, onMounted, onBeforeUnmount} from 'vue';
import PdfPreview from '../../../../components/common/card/PdfPreview.vue'
import { ElMessage} from 'element-plus';
import  {Loading} from '@element-plus/icons-vue'
import request from "../../../../request/index.js";
import {copyChangeKey, copySameKey,wait} from "../../../../utils/commonUtil";
import {baseApi} from "../../../../config/module/AdminExpReportReviewConfig";
const tableMaxHeight = ref(0)
// 计算表格最大高度
const calcTableHeight = () => {
  const windowHeight = window.innerHeight
  const tableTop = document.querySelector('.el-table').getBoundingClientRect().top
  // 留出 50px 边距
  tableMaxHeight.value = windowHeight - tableTop - 50
}

onMounted(() => {
  calcTableHeight()
  window.addEventListener('resize', calcTableHeight)
})

onBeforeUnmount(() => {
  window.removeEventListener('resize', calcTableHeight)
})
const MAX_TRY_COUNT=5
const tryConvertCount=ref(0)
const pdfPreView=ref(null)
const convertWordToPdf = async (reportId) => {
  while(tryConvertCount.value<MAX_TRY_COUNT){
    try {
      await nextTick()
      pdfPreView.value.setLoad(true)
      const res = await request.get('/api/admin/report/word-to-pdf/'+reportId,{
        responseType: 'blob'
      });

      const blob = new Blob([res.data], { type: 'application/pdf' });
      pdfPreView.value.setPdfUrl(URL.createObjectURL(blob));
      break

    } catch (err) {
      tryConvertCount.value+=1
      pdfPreView.value.setError("转换失败:"+err)
      await wait(2000)
    }
  }
  tryConvertCount.value=0
};
const currentFormStatus = computed(() => {
  return formData.id ? 1 : 0;
});
// 弹窗显示状态
const visible = ref(false);

// 设置弹窗显示/隐藏
const setVisible = (val: boolean) => {
  visible.value = val;
};

const formData = reactive({
  id: undefined,
  reportId: '',
  reviewUserId:'',
  score: null,
  plagiarismRate:0,
  reviewComment: ''
});

const initialFormData=reactive({...formData})
// 重置表单
const resetForm = () => {
  formRef.value?.clearValidate();
  Object.assign(formData, { ...initialFormData });
};
const initSourceData=reactive({})
// 初始化表单数据（从父组件传入，适配实体字段）
const initFormData=(source)=>{
  copySameKey(formData,source)
  Object.assign(initSourceData, { ...source });
  autoCheckPlagiarism()
}
const emit=defineEmits(
    ['success-form']
)
// 保存表单数据（提交前转换字段类型对齐实体）
const handleSave = async () => {
  try {
    if (formData.score === null || formData.score === undefined) {
      return ElMessage.warning("请输入成绩！");
    }
    if (formData.score < 0 || formData.score > 100) {
      return ElMessage.warning("成绩必须在0-100之间！");
    }
    let res=null
    if(currentFormStatus.value==0){
      res=await request.post(`${baseApi}/create`,formData)
    }else if(currentFormStatus.value==1){
      let changeFormData=copyChangeKey(formData,initSourceData)
      if(changeFormData!=null){
        res=await request.put(`${baseApi}/update`,changeFormData)
      }
    }
    if(res?.status===200 || res?.status===201){
      setVisible(false);
      emit('success-form')
    }
  } catch (error) {
    // 校验失败，不提交
    ElMessage.error('表单校验失败:'+error);
  }
};
// 暴露给父组件的方法
defineExpose({
  setVisible,
  initFormData
});
// 查重结果结构
const plagiarismResult = reactive({
  plagiarismRate: null,
  similarReports: []
});

const isCheckingPlagiarism = ref(false);
const isSaving = ref(false);


const handlePreview = () => {
  convertWordToPdf(formData.reportId)
};

const autoCheckPlagiarism = async () => {
  if (isCheckingPlagiarism.value) return;

  isCheckingPlagiarism.value = true;
  try {
    // 模拟调用后端查重接口
    const response = await request.get(`/api/admin/report/plagiarism/${formData.reportId}`);
    let data = response.data;
    plagiarismResult.plagiarismRate = data?.plagiarismRate||0;
    formData.plagiarismRate=data?.plagiarismRate ||0;
    plagiarismResult.similarReports = data?.similarReports || [];
    ElMessage.success("查重分析完成！");
  } catch (error) {
    console.error("查重失败:", error);
    ElMessage.error("查重失败，请稍后重试。");
  } finally {
    isCheckingPlagiarism.value = false;
  }
};

const handleCancel = () => {

};
</script>

<style scoped>
.review-page {
  background-color: #f5f7fa;
  padding: 30px;
  overflow: auto;
  height: calc(100% - 60px);
}

.review-container {
  background-color: #ffffff;
  border-radius: 16px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.05);
  padding: 30px;
}

/* 顶部信息栏 */
.review-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 30px;
  flex-wrap: wrap;
  gap: 20px;
}

.header-main {
  flex: 1;
  min-width: 300px;
}

.review-title {
  font-size: 24px;
  font-weight: 600;
  color: #1f2937;
  margin-bottom: 12px;
}
.header-actions {
  display: flex;
  gap: 16px;
}

.action-btn {
  padding: 10px 20px;
  border-radius: 8px;
  font-weight: 500;
  font-size: 14px;
  transition: all 0.2s ease;
}

/* 核心内容区 */
.review-content {
  display: flex;
  margin-bottom: 30px;
}

.content-primary {
  width: 100%;
  display: flex;
  flex-direction: column;
  gap: 24px;
}

/* 通用卡片样式 */
.card-label {
  font-size: 16px;
  font-weight: 500;
  color: #1f2937;
  margin-bottom: 16px;
  display: block;
  position: relative;
}


/* 分数卡片 */
.score-card {
  background-color: #f0f9ff;
  padding: 10px;
}

.score-input {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 16px;
}

.score-input .el-input__inner {
  font-size: 32px;
  font-weight: 600;
  color: #0ea5e9;
  height: 60px;
  text-align: center;
  border-color: #bae6fd;
}

/* 查重结果卡片 */
.plagiarism-card {
  background-color: #fef7fb;
  padding: 10px;
}
.plagiarism-overview {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
  flex-wrap: wrap;
  gap: 10px;
}
.plagiarism-rate .rate-label {
  font-size: 14px;
  color: #6b7280;
  margin-right: 8px;
}
.plagiarism-rate .rate-value {
  font-size: 24px;
  font-weight: 600;
  color: #db2777;
}
.plagiarism-status {
  font-size: 14px;
  color: #4b5563;
}
.plagiarism-status .el-icon-warning { color: #f59e0b; }
.plagiarism-status .el-icon-success { color: #10b981; }
.plagiarism-status .el-icon-info { color: #3b82f6; }

/* 相似报告表格 */
.similar-reports-table .el-table{
   height: 140px;
}
.similar-reports-table .el-table__empty-text {
  color: #9ca3af;
}
.high-similarity {
  color: #dc2626;
  font-weight: 500;
}
.low-similarity {
  color: #f59e0b;
}

/* 查重加载时的占位符 */
.checking-placeholder {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 40px;
  color: #6b7280;
  gap: 10px;
}

/* 评论卡片 */
.comment-card {
  background-color: #ffffff;
  border: 1px solid #e5e7eb;
  padding: 10px;
}

.comment-card .el-textarea__inner {
  font-size: 14px;
  line-height: 1.8;
  border-color: #e5e7eb;
  border-radius: 8px;
  resize: none;
}

.char-count {
  text-align: right;
  font-size: 12px;
  color: #9ca3af;
  margin-top: 8px;
}

/* 底部操作栏 */
.review-actions {
  display: flex;
  flex-wrap: wrap;
  gap: 20px;
  padding-top: 20px;
  border-top: 1px solid #f3f4f6;
}

.save-btn {
  background-color: #4f46e5;
  border-color: #4f46e5;
  padding: 12px 30px;
  font-size: 16px;
}

.save-btn:hover {
  background-color: #4338ca;
  border-color: #4338ca;
}

.cancel-btn {
  background-color: #ffffff;
  border-color: #d1d5db;
  color: #374151;
  padding: 12px 30px;
  font-size: 16px;
}

.cancel-btn:hover {
  background-color: #f3f4f6;
  border-color: #9ca3af;
}
</style>