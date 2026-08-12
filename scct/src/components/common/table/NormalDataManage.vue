<style scoped>
.card-page{
  margin: 0% 2% 0% 2%;
}
</style>
<template>
  <div class="root">
    <div class="card-page">
      <NormalFilterConditionForm
          ref="normalFilterConditionFormRef"
          v-model="filterForm"
          @search="initData"
          @clear="initData"
          :placeholder="placeholder"
      />
      <TablePagination
          :table-data="tablePaginationData"
          :data-config="dataConfig"
          :table-border="true"
          table-fist-column-type="index"
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
import {onMounted, ref,defineExpose} from 'vue'
import TablePagination from './TablePagination.vue'
import request from "../../../request";
import NormalFilterConditionForm from "../form/NormalFilterConditionForm.vue"
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
  placeholder:{
    type:String
  }
})
const normalFilterConditionFormRef=ref(null)
// 表格数据
const tablePaginationData = ref({
  current: 1,
  pages: 0,
  records: [],
  size: 10,
  total: 0
});
const currentYear=new Date().getFullYear()
const currentMouth=new Date().getMonth()
const currentSemester=currentMouth>8||currentMouth<2?`${currentYear}-${currentYear+1}-1`:`${currentYear-1}-${currentYear}-2`
const filterForm=ref({
  semester:currentSemester
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
 * 组件挂载时初始化数据
 */
const getFilterForm=()=>{
  return filterForm.value
}
const handleSizeChange = async (pageSize) => {
  await initData(tablePaginationData.value.current, pageSize)
}
const handleCurrentChange = async (currentPage) => {
  await initData(currentPage, tablePaginationData.value.size)
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