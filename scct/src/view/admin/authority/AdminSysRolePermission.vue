<template>
  <FreedomDataManage
      ref="freedomDataManageRef"
      :data-config="tableDataConfig"
      :filter-form-config="filterFormConfig"
      :page-data-api="`${baseApi}/page`"
  >
    <template #topOperate="{pageParameter}">
      <el-button :icon="Plus" type="primary" @click="handleCreate" v-permission="'rolePerm:create'">创建</el-button>
      <el-button type="primary" @click="handleFenPei" v-permission="'perm:tree'">分配权限</el-button>
    </template>
    <template #rowOperate="{index,row}">
      <el-button :icon="Edit" type="primary" @click="handleEdit(row)" v-permission="'rolePerm:update'">编辑</el-button>
      <el-button :icon="Delete" type="danger" @click="handleDelete(row)" v-permission="'rolePerm:delete'">删除</el-button>
    </template>
  </FreedomDataManage>
  <AdminSysRolePermissionForm
      ref="formViewRef"
      :base-api="baseApi"
      @success-form="handleSuccessForm"
  />
  <FenPeiQuanXian
      ref="fenPeiQuanXianView"
  />
</template>

<script setup lang="ts">
import FenPeiQuanXian from '../../../components/common/form/FenPeiQuanXian.vue'
import  AdminSysRolePermissionForm from '../authority/form/AdminSysRolePermissionForm.vue'
import {Delete,Edit,TopLeft,TopRight,Plus,RefreshLeft} from '@element-plus/icons-vue'
import FreedomDataManage  from '../../../components/common/table/FreedomDataManage.vue'
import  {tableDataConfig,filterFormConfig,baseApi} from "../../../config/module/AdminSysRolePermissionConfig"
import {ref} from "vue";
import request from "../../../request";
const freedomDataManageRef=ref(null)
const formViewRef=ref(null)
const handleCreate=()=>{
  formViewRef.value.setVisible(true)
  formViewRef.value.resetForm()
}
const fenPeiQuanXianView=ref(null)
const handleFenPei=()=>{
  fenPeiQuanXianView.value.setVisible(true)
}
const handleEdit=(row)=>{
  formViewRef.value.setVisible(true)
  formViewRef.value.initFormData(row)
}
const handleSuccessForm=async ()=>{
  await freedomDataManageRef.value.initData()
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