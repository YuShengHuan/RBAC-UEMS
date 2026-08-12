<template>
  <NormalDataManage
      ref="normalDataManageRef"
      :data-config="tableDataFrontTemplateConfig"
      :page-data-api="`${baseApi}/page/template`"
      :placeholder="searchPlaceholder"
  >
    <template #topOperate="{pageParameter}">
      <el-button :icon="Plus" type="primary" @click="handleCreate" v-permission="'report:create'">创建</el-button>
    </template>
    <template #rowOperate="{index,row}">
      <el-button :icon="Edit" type="primary" @click="handleEdit(row)" v-permission="'report:update'">编辑</el-button>
      <el-button :icon="Edit" type="success" @click="handleDetail(row)" v-permission="'report:detail'">查看详细</el-button>
      <el-button :icon="Delete" type="danger" @click="handleDelete(row)" v-permission="'report:delete'">删除</el-button>
    </template>
  </NormalDataManage>
  <FrontExpTemplateFrom
      ref="formViewRef"
      @success-form="handleSuccessForm"
  />
  <FrontExpTemplateDetail
      ref="detailViewRef"
  />
</template>

<script setup lang="ts">
import {Delete,Edit,Plus} from '@element-plus/icons-vue'
import NormalDataManage  from '../../../components/common/table/NormalDataManage.vue'
import  {tableDataFrontTemplateConfig,baseApi} from "../../../config/module/AdminExpReportConfig"
import {onMounted, ref} from "vue";
import request from "../../../request";
import FrontExpTemplateFrom from '../template/dialog/FrontExpTemplateFrom.vue'
import FrontExpTemplateDetail from '../template/dialog/FrontExpTemplateDetail.vue'
const normalDataManageRef=ref(null)
const searchPlaceholder=ref("输入课程名/班级/授课教师名")
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
const handleSuccessForm=async ()=>{
  await normalDataManageRef.value.initData()
}
const handleDetail=(row)=>{
  detailViewRef.value.setVisible(true,row.id)
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