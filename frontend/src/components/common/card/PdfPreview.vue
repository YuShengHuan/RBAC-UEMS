<template>
  <el-dialog
      v-model="state.visible"
      width="80%"
      title="文档预览"
      class="no-scroll-dialog"
  >
  <div class="pdf-preview">
    <!-- 加载状态提示 -->
    <div v-if="state.isLoading" class="loading-mask">
      <div class="loading-spinner"></div>
      <p class="loading-text">文档 加载中...</p>
    </div>
    <!-- 错误状态提示 -->
    <div v-else-if="state.error" class="error-mask">
      <div class="error-spinner"></div>
      <p class="error-text">{{ state.error }}</p>
    </div>
    <!-- PDF 内容区域（父容器用于缩放） -->
    <div v-else class="pdf-container">
      <div class="pdf-wrap" :style="containerStyle">
        <vue-pdf-embed
            :source="state.source"
            :page="state.pageNum"
            :scale="2.0"
            class="vue-pdf-embed"
            @loaded="handlePdfLoaded"
            @loading="handlePdfLoading"
            @error="handlePdfError"
            ref="pdfRef"
        />
      </div>
    </div>
    <!-- 工具栏 -->
    <div class="page-tool">
      <div class="page-tool-item" @click="lastPage">
        <span class="icon">←</span> 上一页
      </div>

      <div class="page-tool-item" @click="nextPage">
        下一页 <span class="icon">→</span>
      </div>

      <div class="page-input-group">
        <input
            type="number"
            v-model.number="pageInput"
            @change="goToPage"
            :min="1"
            :max="state.numPages"
            class="page-input"
        />
        <span class="page-separator">/</span>
        <span class="total-pages">{{ state.numPages }}</span>
      </div>

      <div class="page-tool-item" @click="pageZoomOut">
        <span class="icon">+</span> 放大
      </div>

      <div class="page-tool-item" @click="pageZoomIn">
        <span class="icon">-</span> 缩小
      </div>
    </div>
  </div>
  </el-dialog>
</template>

<script setup lang="ts">
import { reactive,computed,ref, onUnmounted ,defineExpose} from "vue";
import VuePdfEmbed from "vue-pdf-embed";

// 2. 响应式状态管理
const state = reactive({
  source:'',
  pageNum: 1,
  scale: 1,
  numPages: 0,
  isLoading: true,
  visible:false,
  error: "",
  pdfDoc: null as any,
});
const pageInput = ref(1);
const pdfRef = ref<VuePdfEmbed | null>(null);

// 3. 父容器缩放样式（核心：用 transform 缩放父容器）
const containerStyle = computed(() => ({
  transform: `scale(${state.scale})`,
  transformOrigin: "center top",
  transition: "transform 0.3s ease",
  width: `${100 / state.scale}%`, // 关键：宽度反向缩放，保持容器自适应
  margin: "0 auto",
}));
const setPdfUrl=(url)=>{
  if (state.source) {
    URL.revokeObjectURL(state.source);
  }
    state.source = url;
    state.pageNum = 1;
    pageInput.value = 1;
    state.error = "";
    state.isLoading = false;
    state.pdfDoc = null;
}
const setLoad=(val)=>{
    state.visible=true
    state.isLoading=val
}
const setError=(val:string)=>{
  state.isLoading=false
  state.error=val
}
defineExpose(
    {
      setPdfUrl,
      setLoad,
      setError
    }
)
// 5. PDF 加载成功后获取总页数
const handlePdfLoaded = (pdf: any) => {
  state.pdfDoc = pdf;
  state.numPages = pdf.numPages;
  state.isLoading = false;
};

// 6. 加载状态变化
const handlePdfLoading = (isLoading: boolean) => {
  state.isLoading = isLoading;
};

// 7. 加载失败
const handlePdfError = (error: Error) => {
  console.error("PDF 加载失败:", error);
  state.error = `PDF 加载失败: ${error.message}`;
  state.isLoading = false;
};


// 9. 翻页
const lastPage = () => {
  if (state.pageNum > 1) {
    state.pageNum--;
    pageInput.value = state.pageNum;
  }
};

const nextPage = () => {
  if (state.pageNum < state.numPages) {
    state.pageNum++;
    pageInput.value = state.pageNum;
  }
};

// 10. 页码跳转
const goToPage = () => {
  let page = Math.floor(pageInput.value);
  if (page < 1) page = 1;
  if (page > state.numPages) page = state.numPages;
  state.pageNum = page;
  pageInput.value = page;
};

// 11. 缩放（核心：只修改 scale 值，父容器自动缩放）
const pageZoomOut = () => {
    state.scale += 0.1;
};

const pageZoomIn = () => {
    state.scale -= 0.1;

};
// 16. 组件卸载时释放资源
onUnmounted(() => {
  if (state.source) {
    URL.revokeObjectURL(state.source);
  }
});
</script>

<style lang="css" scoped>
/* 容器样式 */
.pdf-preview {
  position: relative;
  box-sizing: border-box;
  border: 1px solid #e5e5e5;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: #f5f5f5;
  overflow: hidden;
}
/* PDF 外层容器（控制滚动） */
.pdf-container {
  overflow: auto;
  width: 100%;
  height: 100% ;
}
/* PDF 包裹层（用于缩放） */
.pdf-wrap {
  width: 100%;
  display: flex;
  justify-content: center;
}

/* PDF 渲染容器（保持原始尺寸） */
.vue-pdf-embed {
  text-align: center;
  background: #fff;
  width: 100%;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
}

/* 加载遮罩 */
.loading-mask {
  width: 100%;
  height: 100%;
  padding: 20px;
  background-color: rgba(255, 255, 255, 0.8);
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
}

.loading-spinner {
  width: 40px;
  height: 40px;
  border: 4px solid #f3f3f3;
  border-top: 4px solid #3498db;
  border-radius: 50%;
  animation: spin 1s linear infinite;
  margin-bottom: 16px;
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

.loading-text {
  color: #666;
  font-size: 14px;
}

/* 错误遮罩 */
.error-mask {
  width: 100%;
  height: 100%;
  background-color: #fff;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  padding: 20px;
  text-align: center;
}
.error-spinner{
  width: 40px;
  height: 40px;
  border: 4px solid #ffffff;
  border-top: 4px solid #fc0404;
  display: flex;
  align-items: center;
  justify-content: center;
  animation: spin 1s linear infinite;
  border-radius: 50%;
  color: #e74c3c;
  margin-bottom: 16px;
}
.error-text {
  color: #e74c3c;
  font-size: 16px;
  margin-bottom: 20px;
}
/* 工具栏样式 */
.page-tool {
  user-select: none;
  position: fixed;
  top: 10px;
  align-self: flex-end;
  display: flex;
  align-items: center;
  background: #f3f5f5;
  border-radius: 30px;
  padding: 8px 20px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  z-index: 100;
  gap: 4px;
}

.page-tool-item {
  padding: 8px 16px;
  cursor: pointer;
  font-size: 14px;
  color: #333;
  border-radius: 20px;
  transition: background-color 0.2s;
  display: flex;
  align-items: center;
  gap: 4px;
}

.page-tool-item:hover {
  color: #1e6eb4;
}

.page-tool-item:disabled {
  cursor: not-allowed;
  color: #ccc;
}

.icon {
  font-size: 16px;
}

.page-input-group {
  display: flex;
  align-items: center;
  padding: 0 10px;
  color: #666;
}

.page-input {
  width: 50px;
  padding: 6px 8px;
  text-align: center;
  font-size: 14px;
  margin: 0 4px;
  border: none;
}

.page-input:focus {
  outline: none;
  border-color: #3498db;
}

/* 响应式样式 */
@media (max-width: 768px) {
  .page-tool {
    padding: 6px 12px;
  }

  .page-tool-item {
    padding: 6px 12px;
    font-size: 13px;
  }

  .page-input {
    width: 40px;
    padding: 4px 6px;
  }
}
</style>