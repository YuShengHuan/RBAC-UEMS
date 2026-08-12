<style scoped>
.card-page{
  margin: 0% 2% 0% 2%;
}
</style>
<template>
  <div class="root">
    <div class="card-page">
      <FilterConditionForm
          v-model="filterForm"
          ref="filterConditionFormRef"
          :filter-form-config="filterFormConfig"
          @search="handleSearch"
          @clear="handleClear"
      ></FilterConditionForm>
      <TablePagination
          :table-data="tablePaginationData"
          :data-config="dataConfig"
          :table-border="true"
          @currentChange="handleCurrentChange"
          @sizeChange="handleSizeChange"
      >
        <template #topOperate="{pageParameter}">
          <slot name="topOperate" :pageParameter="pageParameter"></slot>
        </template>
        <template #rowOperate="{ index, row }">
          <slot name="rowOperate" :index="index" :row="row" />
        </template>
      </TablePagination>
    </div>
  </div>
</template>

<script lang="ts" setup>
import  FilterConditionForm from '../form/FilterConditionForm.vue'
import {onMounted, ref,defineExpose} from 'vue'
import TablePagination from './TablePagination.vue'
import request from "../../../request";
/**
 * Props：父组件传入的配置项（变化的部分）
 */
const props = defineProps({
  pageDataApi: {
    type: String,
    required: true
  },
  dataConfig: {
    type: Array,
    required: true,
    default: () => [],
  },
  filterFormConfig: {
    type: Array,
    required: true,
    default: () => [],
  }
})
// 表格数据
const tablePaginationData = ref({
  current: 1,
  pages: 0,
  records: [],
  size: 10,
  total: 0
});
const filterForm=ref({

})
/**
 * 核心逻辑：分页查询（通用）
 */
const initData = async (currentPage = 1, pageSize = 10) => {
  const res = await request.post(`${props.pageDataApi}`, {
    pageNum: currentPage,
    pageSize: pageSize,
    ...filterForm.value
  })
  if (res.status === 200) {
    tablePaginationData.value = {
      records: res.data.records,
      total: Number(res.data.total),
      current: Number(res.data.current),
      size: Number(res.data.size),
      pages: Number(res.data.pages)
    }
  }
}

/**
 * 分页事件（通用）
 */
const handleSizeChange = async (pageSize) => {
  await initData(tablePaginationData.value.current, pageSize)
}
const handleCurrentChange = async (currentPage) => {
  await initData(currentPage, tablePaginationData.value.size)
}

/**
 * 筛选事件（通用）
 */
const handleSearch =async () => {
  await initData() // 重置为第一页
}
const handleClear=async () => {
  await initData() // 重置为第一页
}
/**
 * 组件挂载时初始化数据
 */
const filterConditionFormRef=ref(null)
const getFilterForm=()=>{
  return filterForm.value
}
onMounted(async () => {
  await initData()
})
defineExpose(
    {
      initData,
      getFilterForm
    }
)
</script>