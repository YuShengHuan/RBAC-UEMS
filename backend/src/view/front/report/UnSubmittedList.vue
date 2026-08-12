<template>
  <NormalDataManage
      ref="normalDataManageRef"
      :data-config="tableDataSubmitConfig"
      :page-data-api="`${baseApi}/page/un-submitted`"
      :placeholder="searchPlaceholder"
  >
    <template #topOperate="{pageParameter}">
      <el-button type="success" @click="handleExport(pageParameter)" v-permission="'report:page:unSubmitted:export'">导出名单</el-button>
    </template>
    <template #rowOperate="{index,row}">
      <el-button :icon="Edit" type="primary" @click="handleUpload(row)" v-permission="'report:page:unSubmitted:upload'">上传</el-button>
    </template>
  </NormalDataManage>
  <FrontExpReportFrom
      ref="formViewRef"
      @success-form="handleSuccessForm"
  />
  <ExportDataView
      :columns="tableDataSubmitConfig"
      ref="exportViewRef"
  />
</template>

<script setup lang="ts">
import {Edit} from '@element-plus/icons-vue'
import NormalDataManage  from '../../../components/common/table/NormalDataManage.vue'
import  {tableDataSubmitConfig,baseApi} from "../../../config/module/AdminExpReportConfig"
import FrontExpReportFrom from '../report/dialog/FrontExpReportFrom.vue'
import {onMounted, ref} from "vue";
import request from "../../../request";
const normalDataManageRef=ref(null)
const searchPlaceholder=ref("输入课程名/班级/授课教师名")
const formViewRef=ref(null)
const handleUpload=(row)=>{
  formViewRef.value.setVisible(true)
  formViewRef.value.initFormData(row)
}
const exportViewRef=ref(null)
const handleExport=async (pageParameter)=>{
  const res = await request.post(`${baseApi}/page/un-submitted`, {
    pageNum: 1,
    pageSize:pageParameter.total,
    ...normalDataManageRef.value.getFilterForm()
  })
  if (res.status === 200) {
    exportViewRef.value.setVisible(true)
    exportViewRef.value.setTableData(res.data.records)
  }
}
const handleSuccessForm=async ()=>{
  await normalDataManageRef.value.initData()
}

</script>

<style scoped>

</style>