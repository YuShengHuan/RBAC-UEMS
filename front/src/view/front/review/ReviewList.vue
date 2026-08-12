<template>
  <NormalDataManage
      ref="normalDataManageRef"
      :data-config="tableDataReviewConfig"
      :page-data-api="`${baseApi}/page/reviewed`"
      :placeholder="searchPlaceholder"
  >
    <template #rowOperate="{index,row}">
      <el-button :icon="Edit" type="primary" @click="handleDetail(row)" v-permission="'reportReview:detail'">查看详细</el-button>
    </template>
  </NormalDataManage>
  <FrontExpReviewDetail
      ref="detailViewRef"
  />
</template>

<script setup lang="ts">
import {Download,Edit} from '@element-plus/icons-vue'
import NormalDataManage  from '../../../components/common/table/NormalDataManage.vue'
import  {tableDataReviewConfig,baseApi} from "../../../config/module/AdminExpReportReviewConfig"
import {onMounted, ref} from "vue";
import FrontExpReviewDetail from '../review/dialog/FrontExpReviewDetail.vue'
const normalDataManageRef=ref(null)
const searchPlaceholder=ref("输入课程名/班级/授课教师名")
const detailViewRef=ref(null)
const handleDetail=(row)=>{
  detailViewRef.value.setVisible(true,row.reviewId)
}
</script>

<style scoped>

</style>