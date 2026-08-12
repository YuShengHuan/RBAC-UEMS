<template>
  <NormalDataManage
      ref="normalDataManageRef"
      :data-config="tableDataSubmitConfig"
      :page-data-api="`${baseApi}/page/submitted`"
      :placeholder="searchPlaceholder"
  >
    <template #topOperate="{pageParameter}">
      <el-button type="success" @click="handleExport(pageParameter)" v-permission="'report:page:submitted:export'">导出名单</el-button>
      <el-button type="primary" @click="handleExportDownloadToZip(pageParameter)" v-permission="'report:page:submitted:download:zip'">导出文件为ZIP</el-button>
    </template>
    <template #rowOperate="{index,row}">
       <el-button :icon="Edit" type="primary" @click="handleDetail(row)" v-permission="'report:detail'">查看详细</el-button>
    </template>
  </NormalDataManage>
  <FrontExpReportDetail
      ref="detailViewRef"
  />
  <ExportDataView
      :columns="tableDataSubmitConfig"
      ref="exportViewRef"
  />
</template>

<script setup lang="ts">
import {Edit} from '@element-plus/icons-vue'
import NormalDataManage  from '../../../components/common/table/NormalDataManage.vue'
import FrontExpReportDetail from '../report/dialog/FrontExpReportDetail.vue'
import  {tableDataSubmitConfig,baseApi} from "../../../config/module/AdminExpReportConfig"
import {onMounted, ref} from "vue";
import request from "../../../request";
import {mimeToExt} from "../../../utils/mimeUtil";
const normalDataManageRef=ref(null)
const searchPlaceholder=ref("输入课程名/班级/授课教师名")
const exportViewRef=ref(null)
const handleExport=async (pageParameter)=>{
  const res = await request.post(`${baseApi}/page/submitted`, {
    pageNum: 1,
    pageSize:pageParameter.total,
    ...normalDataManageRef.value.getFilterForm()
  })
  if (res.status === 200) {
    exportViewRef.value.setVisible(true)
    exportViewRef.value.setTableData(res.data.records)
  }
}
const handleExportDownloadToZip=async (pageParameter)=>{
  try {
      const response = await request.post(`${baseApi}/page/submitted/download/zip`, {
            pageNum: 1,
            pageSize:pageParameter.total,
            ...normalDataManageRef.value.getFilterForm()
          },{
            responseType: 'blob', // 必须设置，否则无法正确解析为文件
            headers: {
              'Accept': 'application/octet-stream' // 仅需这个头
            }
          }
      )
    // 3. 获取后端传的文件名
    const fileName = response.headers['content-disposition']?.split(';')[1]?.split("=")[1];
    const ext="."+fileName.split(".")[1]

    // 5. 处理文件名（清理特殊字符 + 拼接后缀）
    const safeBaseName = "压缩文件".replace(/[\\/:*?"<>|]/g, '_'); // 清理特殊字符
    const fullFileName = safeBaseName + ext; // 完整文件名（含后缀）

    const blob = new Blob([response.data], {
      type: response.headers['content-type'] || 'application/octet-stream'
    });
    const tempUrl = URL.createObjectURL(blob);

    const a = document.createElement('a');
    a.href = tempUrl;
    //设置文件名
    a.download = fullFileName;
    document.body.appendChild(a);
    a.click();

    // 5. 清理资源
    window.URL.revokeObjectURL(tempUrl);
    document.body.removeChild(a);
  }catch (e) {
      console.log("下载异常"+e)
  }

}
const detailViewRef=ref(null)
const handleDetail=(row)=>{
  detailViewRef.value.setVisible(true,row.reportId)
}
</script>

<style scoped>

</style>