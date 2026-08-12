<template>
  <el-dialog
      v-model="visible"
      :title="'导入数据'"
      :show-close="true"
      width="95%"
  >
    <div class="excel-parser">
      <!-- 文件上传区域 -->
      <el-upload
          class="upload-area"
          :auto-upload="false"
          :on-change="handleFileSelect"
          :file-list="fileList"
          accept=".xlsx, .xls"
          drag
      >
        <i class="el-icon-upload" />
        <div class="el-upload__text">将 Excel 文件拖到此处，或<em>点击上传</em></div>
        <div class="el-upload__tip" slot="tip">支持格式：.xlsx、.xls，最大文件大小：10MB</div>
      </el-upload>
      <!-- 解析结果表格 -->
      <div class="table-container" v-if="tableColumns.length > 0 && tableData.length > 0">
        <el-card shadow="hover">
          <div class="card-header">
            <span>数据预览</span>
            <div class="tag-group">
              <el-tag type="info">当前 {{ tableData.length }} 条数据</el-tag>
              <el-tag type="warning" v-if="stats.filteredEmptyRowCount > 0">已过滤 {{ stats.filteredEmptyRowCount }} 条空白行</el-tag>
              <el-tag type="success" v-if="stats.filteredDuplicateRowCount > 0">已过滤 {{ stats.filteredDuplicateRowCount }} 条重复行</el-tag>
            </div>
          </div>
          <el-table
              class="el-table"
              :data="tableData"
              :max-height="tableMaxHeight"
              :header-cell-style="{background:'#f5f7fa',fontWeight: 500 }"
          >
            <el-table-column
                v-for="(col, idx) in tableColumns"
                :key="idx"
                :prop="col.prop"
                :label="col.label"
                width="150"
                align="center"
            />
            <el-table-column label="操作" width="100" fixed="right" align="center">
              <template #default="scope">
                <el-button
                    type="danger"
                    @click="handleDeleteRow(scope.$index, scope.row)"

                >删除</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </div>

      <!-- 空状态提示 -->
      <div class="empty-state" v-if="fileList.length === 0 && tableColumns.length === 0">
        <el-empty style="height: 50px;"  description="请上传 Excel 文件解析数据" />
      </div>

      <!-- 无有效数据提示 -->
      <div class="empty-state" v-if="fileList.length > 0 && tableData.length === 0">
        <el-empty style="height: 50px;"  description="当前无有效数据可导入" />
      </div>

      <!-- 底部操作按钮 -->
      <div class="action-bar" v-if="fileList.length > 0">
        <el-button type="primary" @click="submitImport" :disabled="tableData.length === 0">
          确认导入
        </el-button>
        <el-button @click="resetAll">
          重新上传
        </el-button>
      </div>
    </div>
  </el-dialog>
</template>

<script setup>
import {defineEmits, defineExpose, defineProps, onMounted, reactive, ref, watch} from 'vue';
import {ElEmpty, ElForm, ElMessage, ElMessageBox} from 'element-plus';
import * as XLSX from 'xlsx';
import request from "@/request/index.ts";
import {findLabelByValue, findValueByLabel} from "@/utils/commonUtil.js";
import {weekDayOptions} from "@/utils/globalOptionsUtil.js";

// 响应式状态
const fileList = ref([]);
const originalData = ref([]);
const tableColumns = ref([]);
const tableData = ref([]);
const visible=ref(false)
const setVisible=(v)=>{
  visible.value=v
}
// 导入配置（新增 crossRowFill 字段）
const importConfig = reactive({
  filterEmptyRows: true,
  filterDuplicateRows: true,
  skipRows: -1, //跳过的行数
  crossRowFill: false // 新增：跨行填充开关，默认关闭
});
const props=defineProps(
    {
      importFormConfig:{
        type:Array,
        required:true,
        default:()=>([])
      },
      importApi:{
        type:String,
        required:true,
        default:''
      }
    }
)
const emit=defineEmits([
  'success-form'
])
// 统计信息（新增 filledCrossRowCount 字段）
const stats = ref({
  filteredEmptyRowCount: 0,
  filteredDuplicateRowCount: 0,
  filledCrossRowCount: 0 // 新增：跨行填充的单元格数量
});
const tableMaxHeight = ref(0)
// 计算表格最大高度
const calcTableHeight = () => {
  const windowHeight = window.innerHeight
  const tableTop = document.querySelector('.el-table')?.getBoundingClientRect().top
  // 留出 50px 边距，兼容表格未渲染的情况
  tableMaxHeight.value = tableTop ? (windowHeight - tableTop - 10) : 500
}

onMounted(() => {
  calcTableHeight()
  window.addEventListener('resize', calcTableHeight)
})
/**
 * 当配置项改变时，重新处理数据
 */
watch(importConfig,()=>{
  if (originalData.value.length === 0) return;
  processData(originalData.value);
},{deep:true})
/**
 * 处理文件选择
 */
const handleFileSelect = (uploadFile) => {
  const file = uploadFile.raw;
  if (!file) return;

  // 文件校验
  const fileExt = file.name.split('.').pop().toLowerCase();
  if (!['xlsx', 'xls'].includes(fileExt)) {
    ElMessage.error('请上传 Excel 格式文件（.xlsx/.xls）');
    return;
  }
  if (file.size > 10 * 1024 * 1024) {
    ElMessage.error('文件大小不能超过 10MB');
    return;
  }
  fileList.value = [file];

  // 解析文件
  const reader = new FileReader();
  reader.onload = (e) => {
    try {
      const data = new Uint8Array(e.target.result);
      const workbook = XLSX.read(data, { type: 'array' });
      const sheetName = workbook.SheetNames[0];
      const worksheet = workbook.Sheets[sheetName];
      let jsonData = XLSX.utils.sheet_to_json(worksheet, { header: 1 });

      if (jsonData.length === 0) {
        ElMessage.warning('Excel 文件中无数据');
        return;
      }
      originalData.value = [...jsonData];
      processData(jsonData);

    } catch (error) {
      ElMessage.error('文件解析失败，请检查文件格式是否正确');
    }
  };
  reader.readAsArrayBuffer(file);
};

/**
 * 根据配置处理数据（过滤、去重、新增跨行填充）
 */
const processData = (jsonData) => {
  if(importConfig.skipRows<=-1){
      jsonData=[jsonData[0].map(item=>undefined),...jsonData]
  }else{
    jsonData=jsonData.slice(Number(importConfig.skipRows),jsonData.length)
  }
  const [headerRow, ...dataRows] = jsonData;
  tableColumns.value = Array.from({ length: headerRow.length }, (_, index) => {
    // 处理表头文本（空值兜底）
    const rawHeader = headerRow[index];
    let labelText = '';
    if (rawHeader === null || rawHeader === undefined) {
      labelText = `列${index + 1}`;
    } else {
      const trimmed = String(rawHeader).trim();
      labelText = trimmed || `列${index + 1}`;
    }
    return {
      prop: `col_${index}`, // 强制非空唯一 prop
      label: labelText,     // 友好的列名
      width: 120 // 可选：默认列宽，避免太挤
    };
  });

  let processedRows = [...dataRows.map((row) => ({ data: row }))];
  const newStats = {
    filteredEmptyRowCount: 0,
    filteredDuplicateRowCount: 0
  };

  // 过滤空白行
  if (importConfig.filterEmptyRows) {
    const nonEmptyRows = processedRows.filter(row =>
        row.data.some(cell => cell !== undefined && cell !== null && cell.toString().trim() !== '')
    );
    newStats.filteredEmptyRowCount = processedRows.length - nonEmptyRows.length;
    processedRows = nonEmptyRows;
  }

  //过滤重复行
  if (importConfig.filterDuplicateRows && processedRows.length > 0) {
    const seenRows = new Set();
    const uniqueRows = [];
    processedRows.forEach(row => {
      const rowStr = JSON.stringify(row.data);
      if (!seenRows.has(rowStr)) {
        seenRows.add(rowStr);
        uniqueRows.push(row);
      }
    });
    newStats.filteredDuplicateRowCount = processedRows.length - uniqueRows.length;
    processedRows = uniqueRows;
  }

  stats.value = newStats;
  tableData.value = processedRows.map((row, displayIndex) => {
    const rowObj = { _displayIndex: displayIndex + 1 };
    tableColumns.value.forEach((col, idx) => {
      rowObj[col?.prop] = row.data[idx] ?? '';
    });
    return rowObj;
  });

  ///处理课表的表格数据
  let dataTo=[{}]
  let weekDayColumns={}
  let addIndex=1
  tableData.value.forEach(
      item=>{
           Object.keys(item).forEach(
               (c,c_id)=>{
                  if(item[c]!==undefined || item[c]!=null || String(item[c]).length>0){
                    const res1 = String(item[c]).match(/^(\d{4})-(\d{4}).*?第(\d+)学期$/);
                    const res2 = String(item[c]).match(/([^\u0000-\u00FF]+)老师/)
                    const res4 = String(item[c]).match(/^(.*?)\/\((.*?)\)(.*?)\/\s*(实.*?)\/(.*?)\/(.*)$/);
                    const res5 = String(item[c]).match( /教工号\D*(\d+)/);
                    if(res1){
                      const [, yearStart, yearEnd, semester] =res1;
                      //学期获取
                      dataTo[dataTo.length-1]["semester"]=`${yearStart}-${yearEnd}-${semester}`
                    }
                    if(res5){
                      dataTo[dataTo.length-1]["userAccount"]=res5[1]
                    }
                    if(res2){
                       dataTo[dataTo.length-1]["realName"]=res2[1]
                    }
                    if(res4){
                      weekDayColumns[item]=findLabelByValue(weekDayOptions,c_id-2)

                      console.log(findLabelByValue(weekDayOptions,c_id-2))
                      const [, courseName, classPeriod, weekRange, labLocation, className, studentCount] = res4;
                      if(addIndex>1){
                        dataTo.push({...dataTo[dataTo.length-1]})
                      }
                      let index=dataTo.length-1
                      dataTo[index]["courseName"]=courseName
                      dataTo[index]["weekRange"]=weekRange
                      dataTo[index]["labLocation"]=labLocation
                      dataTo[index]["className"]=className
                      dataTo[index]["classPeriod"]=classPeriod
                      dataTo[index]["weekDay"]=weekDayColumns[item]
                      dataTo[index]["isReport"]='否'
                      if(courseName.includes("实验")){
                         dataTo[index]["isReport"]='是'
                      }
                      addIndex++
                    }
                  }
                }
           )
  })
  tableColumns.value=props.importFormConfig.map((item,index)=>{
       return {
          label:item.label||`${index+1}列`,
          prop: item.prop
       }
  })
  tableData.value=dataTo
};

/**
 * 处理行删除
 */
const handleDeleteRow = (index, row) => {
  ElMessageBox.confirm(
      '此操作将永久删除该行数据, 是否继续?',
      '提示',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
  ).then(() => {
    tableData.value.splice(index, 1);
    ElMessage.success('行删除成功');
  }).catch(() => { /* 用户取消 */ });
};

/**
 * 重置所有状态（包含跨行填充开关）
 */
const resetAll = () => {
  fileList.value = [];
  originalData.value = [];
  tableColumns.value = [];
  tableData.value = [];
  stats.value = { filteredEmptyRowCount: 0, filteredDuplicateRowCount: 0, filledCrossRowCount: 0 };
  importConfig.filterEmptyRows = true;
  importConfig.filterDuplicateRows = true;
};

/**
 * 提交导入
 */
const submitImport = async () => {
  // 构造请求数据
  let dataToSubmit=tableData.value.map(
      item=>{
        const targetForm={}
        Object.keys(item).forEach(
              k=>{
                let formatter=props.importFormConfig.find(c=>c.prop===k)?.formatter
                if(formatter){
                  targetForm[k]=formatter(item[k])
                }else{
                  targetForm[k]=item[k]
                }
              }
        )
        return targetForm
      }
  )
  if(dataToSubmit.length===0){
    ElMessage.error('导入失败,数据为[]');
     return
  }
  try {
    const res=await request.post(`${props.importApi}`,dataToSubmit)
    if(res.status===200){
      emit('success-form')
      ElMessage.success('导入成功！');
    }
  }catch (e) {

  }

};
defineExpose(
    {
      setVisible
    }
)
</script>

<style scoped>
.el-table{
  width: 100%;
}
/* 表头样式 */
.custom-table th {
  position: sticky;
  top: 0;
  background: linear-gradient(135deg, #f5f7fa 5%, #e4e9f2 100%);
  color: #333;
  font-weight: 600;
  text-align: left;
  padding: 12px 16px;
  border-bottom: 1px solid #e0e6ed;
  border-right: 1px solid #e0e6ed;
  z-index: 10;
}

/* 表头最后一列无边框 */
.custom-table th:last-child {
  border-right: none;
}

/* 表体单元格样式 */
.custom-table td {
  padding: 12px 16px;
  border-bottom: 1px solid #f0f3f9;
  border-right: 1px solid #f0f3f9;
  color: #666;
  transition: background-color 0.3s ease;
}

/* 表体最后一列无边框 */
.custom-table td:last-child {
  border-right: none;
}

/* 奇数行背景色 */
.custom-table tbody tr:nth-child(odd) {
  background-color: #fafbfd;
}

/* 最后一行无边框 */
.custom-table tbody tr:last-child td {
  border-bottom: none;
}
/* ... 其他样式 ... */
.excel-parser { padding:10px; width: 100%; margin: 0 auto; }
.upload-area { margin-bottom: 10px; }
.table-container { animation: fadeIn 0.3s ease-in-out; }
.card-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 15px; flex-wrap: wrap; gap: 10px; }
.tag-group { display: flex; gap: 10px; flex-wrap: wrap; }
.empty-state { margin: 50px auto; max-width: 400px; }
.action-bar { margin-top: 20px; display: flex; justify-content: flex-end; gap: 10px; }
@keyframes fadeIn { from { opacity: 0; transform: translateY(10px); } to { opacity: 1; transform: translateY(0); } }
</style>