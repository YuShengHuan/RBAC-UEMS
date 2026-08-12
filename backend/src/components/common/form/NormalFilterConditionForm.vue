<template>
  <div class="search-area">
    <div class="search-el-item">
      <label style="width: 100px;">选择学期：</label>
      <SemesterPicker v-model="filterForm['semester']" />
    </div>
    <div class="search-el-item">
      <el-input
          class="search-input"
          v-model="filterForm['searchContent']"
          type="text"
          :placeholder="placeholder"
          clearable
      >
        <template #prefix>
          <el-icon><Search/></el-icon>
        </template>
      </el-input>
      <el-button-group class="btn-group-operate">
        <el-button
            class="btn-search"
            size="default"
            @click="handleSearch"
        >
          搜索
        </el-button>
        <el-button
            class="btn-clear"
            size="default"
            @click="handleClearSearch"
        >
          清除
        </el-button>
      </el-button-group>
    </div>
  </div>
</template>

<script setup lang="ts">
import {Search}  from '@element-plus/icons-vue'
import SemesterPicker  from '../form/SemesterPicker.vue'
import {ref, defineProps, defineEmits, watch} from "vue";
const props=defineProps({
  modelValue:{
    type:Object,
    required:true,
    default:()=>({})
  },
  placeholder:{
    type:String
  }
})
const filterForm=ref({...props.modelValue})
watch(
    filterForm,
    (val)=>{
      emit('update:modelValue',{...val})
    },{
      deep:true
    }
)
const emit=defineEmits([
  'update:modelValue',
  'search',
  'clear'
])
const handleSearch=()=>{
  emit('search')
}
const handleClearSearch=()=>{
  filterForm.value['searchContent']=''
  emit('update:modelValue',{...filterForm.value})
  emit('clear')
}
const setFilterForm=(condition)=>{
  filterForm.value={...condition}
}
defineExpose(
    {
      setFilterForm
    }
)
</script>

<style scoped>
/* 外层容器优化：增加白背景和阴影，提升层次感 */
.search-area{
  display: flex;
  align-items: center;
  flex-direction: column;
  justify-content: center;
  margin: 20px;
  border-radius: 8px;
  width: calc(100% - 40px);
}

/* 搜索项行容器：增加间距，优化对齐 */
.search-el-item{
  display: flex;
  align-items: center;
  width: 100%;
  margin-bottom: 20px;
  gap: 8px;
}

/* 输入框/选择器统一样式：柔和边框，hover效果 */
.search-input{
  height: 38px;
  flex: 1;
  border-radius: 4px !important;
  transition: all 0.2s ease;
}

/* 选择器样式优化 */
.search-select :deep(.el-select__wrapper){
  border: 1px solid #dcdfe6 !important;
  box-shadow: none !important;
  border-radius: 4px !important;
}
.search-select :deep(.el-select__wrapper:hover){
  border-color: #c0c4cc !important;
}
.search-select :deep(.el-select__wrapper:focus-within){
  border-color: #409eff !important;
  outline: none;
  box-shadow: 0 0 0 2px rgba(64, 158, 255, 0.2) !important;
}

/* 输入框样式优化 */
.search-input :deep(.el-input__wrapper){
  border: 1px solid #dcdfe6 !important;
  box-shadow: none !important;
  border-radius: 4px !important;
}
.search-input :deep(.el-input__wrapper:hover){
  border-color: #c0c4cc !important;
}
.search-input :deep(.el-input__wrapper.is-focus){
  border-color: #409eff !important;
  box-shadow: 0 0 0 2px rgba(64, 158, 255, 0.2) !important;
}
/* 搜索/清除按钮组：优化布局和样式 */
.btn-group-operate{
  align-self: flex-end;
  gap: 12px;
}
.btn-search{
  background-color: #42b983 !important;
  color: #FFFFFF !important;
  border-radius: 4px !important;
  height: 38px !important;
  width: 120px !important;
  font-size: 14px !important;
  transition: all 0.2s ease;
}
.btn-search:hover{
  background-color: #38a169 !important;
  transform: translateY(-1px);
  box-shadow: 0 2px 8px rgba(66, 185, 131, 0.3);
}
.btn-clear{
  background-color: #fc0404 !important;
  color: #FFFFFF !important;
  border-radius: 4px !important;
  height: 38px !important;
  font-size: 14px !important;
  transition: all 0.2s ease;
}
.btn-clear:hover{
  opacity: 0.5;
}

/* 响应式适配：小屏幕自动换行 */
@media (max-width: 768px) {
  .search-el-item{
    flex-direction: column;
    align-items: flex-start;
  }
  .search-input, .search-select, .search-switch-btn{
    width: 100% !important;
  }
  .btn-group-operate{
    align-self: center;
    width: 100%;
    justify-content: center;
  }
  .btn-search{
    width: 45% !important;
  }
}
</style>