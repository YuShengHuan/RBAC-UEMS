<template>
  <FreedomDataManage
      ref="freedomDataManageRef"
      :data-config="tableDataConfig"
      :filter-form-config="filterFormConfig"
      :page-data-api="`${baseApi}/page`"
  >
    <template #topOperate="{pageParameter}">
      <el-button :icon="Plus" type="primary" @click="handleCreate" v-permission="'reportReview:create'">创建</el-button>
      <el-button :icon="TopLeft" type="success" @click="handleExport(pageParameter)" v-permission="'reportReview:export'">导出</el-button>
    </template>
    <template #rowOperate="{index,row}">
      <el-button :icon="Edit" type="primary" @click="handleEdit(row)" v-permission="'reportReview:update'">编辑</el-button>
      <el-button :icon="Delete" type="danger" @click="handleDelete(row)" v-permission="'reportReview:delete'">删除</el-button>
    </template>
  </FreedomDataManage>
  <AdminExpReportReviewForm
      ref="formViewRef"
      :base-api="baseApi"
      @success-form="handleSuccessForm"
  />
  <ExportDataView
      :columns="tableDataConfig"
      ref="exportViewRef"
  />
</template>

<script setup lang="ts">
import  AdminExpReportReviewForm from '../exp/form/AdminExpReportReviewForm.vue'
import {Delete,Edit,TopLeft,TopRight,Plus,Download} from '@element-plus/icons-vue'
import FreedomDataManage  from '../../../components/common/table/FreedomDataManage.vue'
import  {tableDataConfig,filterFormConfig,baseApi} from "../../../config/module/AdminExpReportReviewConfig"
import {ref} from "vue";
import request from "../../../request";
const freedomDataManageRef=ref(null)
const formViewRef=ref(null)
const handleCreate=()=>{
  formViewRef.value.setVisible(true)
  formViewRef.value.resetForm()
}
const handleEdit=(row)=>{
  formViewRef.value.setVisible(true)
  formViewRef.value.initFormData(row)
}
const handleSuccessForm=async ()=>{
  await freedomDataManageRef.value.initData()
}
const exportViewRef=ref(null)
const handleExport=async (pageParameter)=>{
  const res = await request.post(`${baseApi}/page`, {
    pageNum: 1,
    pageSize:pageParameter.total,
    ...freedomDataManageRef.value.getFilterForm()
  })
  if (res.status === 200) {
    exportViewRef.value.setVisible(true)
    exportViewRef.value.setTableData(res.data.records)
  }
}
const handleDelete=async (row)=>{
  try {
    const res=await request.delete(`${baseApi}/delete/${row.id}`)
    if(res.status===200){
      await freedomDataManageRef.value.initData()
    }
  }catch (e) {
    console.log(e)
  }

}
</script>

<style scoped>

</style>