<template>
  <FreedomDataManage
      ref="freedomDataManageRef"
      :data-config="tableDataConfig"
      :filter-form-config="filterFormConfig"
      :page-data-api="`${baseApi}/page`"
  >
    <template #topOperate="{pageParameter}">
      <el-button :icon="Plus" type="primary" @click="handleCreate" v-permission="'lab:create'">创建</el-button>
      <el-button :icon="TopRight" type="success" @click="handleImport" v-permission="'lab:batchInsert'">导入</el-button>
      <el-button :icon="TopLeft" type="success" @click="handleExport(pageParameter)" v-permission="'lab:export'">导出</el-button>
      <el-button :icon="Delete" type="danger" @click="handleBatchDelete(pageParameter)" v-permission="'lab:batchDelete'">批量删除</el-button>
    </template>
    <template #rowOperate="{index,row}">
      <el-button :icon="Edit" type="primary" @click="handleEdit(row)" v-permission="'lab:update'">编辑</el-button>
      <el-button :icon="Delete" type="danger" @click="handleDelete(row)" v-permission="'lab:delete'">删除</el-button>
    </template>
  </FreedomDataManage>
  <AdminExpLabForm
      ref="formViewRef"
      :base-api="baseApi"
      @success-form="handleSuccessForm"
  />
  <ImportDataView
      ref="importDataRef"
      :import-form-config="importFormConfig"
      :import-api="`${baseApi}/batch-insert`"
      @success-form="handleSuccessForm"
  />
  <ExportDataView
      :columns="tableDataConfig"
      ref="exportViewRef"
  />
</template>

<script setup lang="ts">
import  AdminExpLabForm from '../exp/form/AdminExpLabForm.vue'
import  ImportDataView from '../../../components/common/table/ImportDataView.vue'
import {ElMessage, ElMessageBox} from "element-plus";
import {Delete,Edit,TopLeft,TopRight,Plus,RefreshLeft} from '@element-plus/icons-vue'
import FreedomDataManage  from '../../../components/common/table/FreedomDataManage.vue'
import  {tableDataConfig,filterFormConfig,baseApi,importFormConfig} from "../../../config/module/AdminExpLabConfig"
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
const importDataRef=ref(null)
const handleImport=()=>{
  importDataRef.value.setVisible(true)
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
const handleBatchDelete=async (pageParameter)=>{
  let res = await request.post(`${baseApi}/page`, {
    pageNum: 1,
    pageSize:pageParameter.total,
    ...freedomDataManageRef.value.getFilterForm()
  })
  if (res.status === 200) {
    res=await request.delete(`${baseApi}/batch-delete`,
        {
          data: res.data.records.map(item=>item.id),
          headers: {
            'Content-Type': 'application/json'
          }
        }
    )
    if(res.status===200){
      await freedomDataManageRef.value.initData()
    }
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