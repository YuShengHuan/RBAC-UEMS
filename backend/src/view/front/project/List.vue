<template>
  <NormalDataManage
      ref="normalDataManageRef"
      :data-config="tableDataFrontConfig"
      :page-data-api="`${baseApi}/page`"
      :placeholder="searchPlaceholder"
  >
    <template #topOperate="{pageParameter}">
      <el-button :icon="Plus" type="primary" @click="handleCreate" v-permission="'project:create'">创建</el-button>
      <el-button :icon="TopRight" type="success" @click="handleImport" v-permission="'project:batchInsert'">导入</el-button>
      <el-button :icon="TopLeft" type="success" @click="handleExport(pageParameter)" v-permission="'project:export'">导出</el-button>
    </template>
    <template #rowOperate="{index,row}">
      <el-button :icon="Edit" type="primary" @click="handleEdit(row)" v-permission="'project:update'">编辑</el-button>
      <el-button :icon="Edit" type="success" @click="handleDetail(row)" v-permission="'project:detail'">查看详细</el-button>
      <el-button :icon="Delete" type="danger" @click="handleDelete(row)" v-permission="'project:delete'">删除</el-button>
    </template>
  </NormalDataManage>
  <ImportDataView
      ref="importDataRef"
      :import-form-config="importFormConfig"
      :import-api="`${baseApi}/batch-insert`"
      @success-form="handleSuccessForm"
  />
  <FrontExpProjectFrom
      ref="formViewRef"
      @success-form="handleSuccessForm"
  />
  <FrontExpProjectDetail
      ref="detailViewRef"
  />
  <ExportDataView
      :columns="tableDataFrontConfig"
      ref="exportViewRef"
  />
</template>

<script setup lang="ts">
import {Delete,Edit,TopLeft,TopRight,Plus,DocumentAdd} from '@element-plus/icons-vue'
import ImportDataView from '../../../components/common/table/ImportDataView.vue'
import FrontExpProjectFrom from '../project/dialog/FrontExpProjectFrom.vue'
import FrontExpProjectDetail from '../project/dialog/FrontExpProjectDetail.vue'
import NormalDataManage  from '../../../components/common/table/NormalDataManage.vue'
import  {tableDataFrontConfig,importFormConfig,baseApi} from "../../../config/module/AdminExpProjectConfig"
import {onMounted, ref} from "vue";
import request from "../../../request";
const normalDataManageRef=ref(null)
const searchPlaceholder=ref("输入课程名/班级/授课教师名")
const importDataRef=ref(null)
const handleImport=()=>{
  importDataRef.value.setVisible(true)
}
const formViewRef=ref(null)
const detailViewRef=ref(null)
const handleCreate=()=>{
  formViewRef.value.setVisible(true)
  formViewRef.value.resetForm()
}
const handleEdit=(row)=>{
  formViewRef.value.setVisible(true)
  formViewRef.value.initFormData(row)
}
const handleDetail=(row)=>{
  detailViewRef.value.setVisible(true,row.id)
}
const handleSuccessForm=async ()=>{
  await normalDataManageRef.value.initData()
}

const exportViewRef=ref(null)
const handleExport=async (pageParameter)=>{
  const res = await request.post(`${baseApi}/page`, {
    pageNum: 1,
    pageSize:pageParameter.total,
    ...normalDataManageRef.value.getFilterForm()
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
      await normalDataManageRef.value.initData()
    }
  }catch (e) {
    console.log(e)
  }
}
</script>

<style scoped>

</style>