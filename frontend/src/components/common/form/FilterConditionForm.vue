<template>
  <div class="search-area">
    <template v-for="(ac,aIndex) in filterActiveCondition">
      <div class="search-el-item">
        <el-select
            v-if="ac?.options"
            class="search-select"
            v-model="filterForm[ac.prop]"
            :placeholder="ac.label"
            filterable
            clearable
        >
          <el-option
              v-for="opt in ac.options"
              :key="opt.value"
              :label="opt.label"
              :value="opt.value"
          />
        </el-select>
        <el-input
            v-else
            v-model="filterForm[ac.prop]"
            class="search-input"
            type="text"
            :placeholder="ac.label"
            clearable
        />
        <el-popover
            placement="bottom"
            :show-arrow="false"
            trigger="click"
            popper-style="min-width: 0px;padding: 0;width: auto;margin: 0;border-radius: 4px;box-shadow: 0 2px 12px rgba(0,0,0,0.1);overflow: hidden;"
            :popper-options="{
                    modifiers: [
                      {
                        name: 'offset',
                        options: {
                          offset: [0, 2]  // 微调偏移，避免紧贴按钮
                        }
                      }
                    ]
                  }"
        >
          <template #default>
            <div class="search-items">
              <template v-for="(c,cIndex) in filterFormConfig">
                <div :class="{'search-item':true,'search-item-active':ac.label===c.label}"
                     @click="handleSelectCondition(aIndex,cIndex)"
                >{{c.label}}
                </div>
              </template>
            </div>
          </template>
          <template #reference>
            <el-button
                class="search-switch-btn"
                type="info"
                :icon="Filter">{{ac.label}}</el-button>
          </template>
        </el-popover>
      </div>
    </template>
    <div class="btn-group">
      <el-button-group class="btn-group-add-del">
        <el-button
            :disabled="!(filterActiveCondition.length<filterFormConfig.length)"
            class="btn-add"
            size="small"
            @click="handleAddSearchCondition"
        >
          <el-icon :size="17"><ZoomIn/></el-icon>
        </el-button>
        <el-button
            :disabled="!(filterActiveCondition.length>1)"
            class="btn-del"
            size="small"
            @click="handleReduceSearchCondition"
        >
          <el-icon :size="17"><ZoomOut/></el-icon>
        </el-button>
      </el-button-group>
      <el-button-group class="btn-group-operate">
        <el-button
            class="btn-search"
            size="default"
            :icon="Search"
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
import {Search,Filter,ZoomIn,ZoomOut}  from '@element-plus/icons-vue'
import {ref, defineProps, defineEmits, watch} from "vue";
const props=defineProps({
  modelValue:{
    type:Object,
    required:true,
    default:()=>({})
  },
  filterFormConfig:{
    type:Array,
    default:()=>([])
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
const filterActiveCondition=ref([
  props.filterFormConfig[0]||{}
])
const handleAddSearchCondition=()=>{
  const activeLength=filterActiveCondition.value.length;
  const configLength=props.filterFormConfig.length;
  if(activeLength<configLength){
    filterActiveCondition.value.push(
        props.filterFormConfig[activeLength]
    )
  }
}
const handleReduceSearchCondition=()=>{
  const activeLength=filterActiveCondition.value.length;
  if(activeLength>1){
    filterActiveCondition.value.splice(activeLength-1,1);
  }
}
const handleSelectCondition=(aIndex,cIndex)=>{
  filterActiveCondition.value[aIndex]=props.filterFormConfig[cIndex]
  const acFields=filterActiveCondition.value.map(
      item=>item.prop
  )
  Object.keys(filterForm.value).forEach(
      item=>{
        if(!acFields.includes(item)){
          filterForm.value[item]=''
        }
      }
  )
}
const handleSearch=()=>{
  emit('search')
}
const handleClearSearch=()=>{
  filterForm.value={}
  emit('update:modelValue',{})
  emit('clear')
}
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
  margin-bottom: 12px;
  gap: 8px;
}

/* 输入框/选择器统一样式：柔和边框，hover效果 */
.search-input, .search-select{
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

/* 下拉选项容器：优化滚动条和边框 */
.search-items{
  width: 120px;
  overflow: auto;
  background-color: #ffffff;
}

/* 滚动条美化 */
.search-items::-webkit-scrollbar {
  width: 4px;
  height: 4px;
}
.search-items::-webkit-scrollbar-thumb {
  background-color: #c0c4cc;
  border-radius: 2px;
  transition: background-color 0.2s;
}
.search-items::-webkit-scrollbar-thumb:hover {
  background-color: #909399;
}
.search-items::-webkit-scrollbar-track {
  background-color: #f5f7fa;
  border-radius: 2px;
}

/* 选项按钮样式：渐变hover，优化选中态 */
.search-item{
  width: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  height: 36px;
  cursor: pointer;
  font-size: 14px;
  transition: all 0.2s ease;
  border-radius: 2px;
  margin: 2px 0;
}
.search-item:hover{
  background-color: #409eff;
  color: #FFFFFF;
  transform: translateX(2px);
}
.search-item-active{
  background-color: #409eff;
  color: #FFFFFF;
  pointer-events: none;
  font-weight: 500;
}

/* 筛选条件切换按钮 */
.search-switch-btn{
  background-color: #409eff !important;
  border-color: #409eff !important;
  color: #FFFFFF !important;
  height: 38px !important;
  width: 120px !important;
  min-width: 120px !important;
  font-size: 14px !important;
  border-radius: 4px !important;
  transition: all 0.2s ease;
}
.search-switch-btn:hover{
  background-color: #3386e6 !important;
  border-color: #3386e6 !important;
}
.btn-group{
  display: flex;
  align-self: center;
  width: 100%;
  justify-content:space-between;
}
/* 新增/删除按钮组：调整间距和样式 */
.btn-group-add-del{
  align-self: flex-start;
  margin-top: 8px;
  gap: 8px;
}
.btn-add{
  background-color: #409eff !important;
  color: #FFFFFF !important;
  border-radius: 4px !important;
  height: 34px !important;
  transition: background-color 0.2s ease;
}
.btn-add:hover{
  background-color: #3386e6 !important;
}
.btn-add:disabled{
  background-color: #a0cfff !important;
  cursor: not-allowed;
}
.btn-del{
  background-color: #f56c6c !important;
  color: #FFFFFF !important;
  border-radius: 4px !important;
  height: 34px !important;
  transition: background-color 0.2s ease;
}
.btn-del:hover{
  background-color: #e64e4e !important;
}
.btn-del:disabled{
  background-color: #fab6b6 !important;
  cursor: not-allowed;
}

/* 搜索/清除按钮组：优化布局和样式 */
.btn-group-operate{
  align-self: flex-end;
  margin-top: 16px;
  gap: 12px;
}
.btn-search{
  background-color: #42b983 !important;
  color: #FFFFFF !important;
  border-radius: 0 !important;
  height: 38px !important;
  width: 120px !important;
  font-size: 14px !important;
  margin-right: 1px !important;
  transition: all 0.2s ease;
}
.btn-search:hover{
  background-color: #38a169 !important;
  transform: translateY(-1px);
  box-shadow: 0 2px 8px rgba(66, 185, 131, 0.3);
}
.btn-clear{
  background-color: #f8f9fa !important;
  color: #606266 !important;
  border: 1px solid #dcdfe6 !important;
  border-radius: 0 !important;
  height: 38px !important;
  font-size: 14px !important;
  transition: all 0.2s ease;
}
.btn-clear:hover{
  background-color: #f1f3f5 !important;
  color: #303133 !important;
  border-color: #c0c4cc !important;
}
</style>