<template>
  <NormalDataManage
      ref="normalDataManageRef"
      :data-config="tableDataReviewConfig"
      :page-data-api="`${baseApi}/page/un-reviewed`"
      :placeholder="searchPlaceholder"
  >
    <template #rowOperate="{index,row}">
      <el-button :icon="Edit" type="primary" @click="handleReview(row)" v-permission="'reportReview:unReview:review'">批阅</el-button>
    </template>
  </NormalDataManage>
  <FrontExpReviewForm
      ref="frontExpReviewForm"
  />
</template>

<script setup lang="ts">
import {Edit} from '@element-plus/icons-vue'
import FrontExpReviewForm  from '../review/dialog/FrontExpReviewForm.vue'
import NormalDataManage  from '../../../components/common/table/NormalDataManage.vue'
import  {tableDataReviewConfig,baseApi} from "../../../config/module/AdminExpReportReviewConfig"
import {onMounted, ref} from "vue";
const normalDataManageRef=ref(null)
const searchPlaceholder=ref("输入课程名/班级/授课教师名")
const frontExpReviewForm=ref(null)
const handleReview=(row)=>{
  frontExpReviewForm.value.setVisible(true)
  frontExpReviewForm.value.initFormData(row)
}
</script>

<style scoped>

</style>