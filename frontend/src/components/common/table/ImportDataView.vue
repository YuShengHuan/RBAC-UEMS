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

      <!-- 导入配置区域 -->
      <el-card shadow="hover" class="config-card" >
        <div class="card-header">
          <span>配置显示</span>
        </div>
        <el-form :model="configVisible" label-width="120px" style="display: flex;">
          <el-form-item label="导入配置">
            <el-switch v-model="configVisible.importConfigVisible" active-text="启用" inactive-text="禁用"/>
          </el-form-item>
          <el-form-item label="字段配置">
            <el-switch v-model="configVisible.fieldConfigVisible" active-text="启用" inactive-text="禁用"/>
          </el-form-item>
        </el-form>
      </el-card>

      <!-- 导入配置区域（新增跨行填充选项） -->
      <el-card shadow="hover" class="config-card" v-show="configVisible.importConfigVisible">
        <div class="card-header">
          <span>导入配置</span>
        </div>
        <el-form :model="importConfig" label-width="120px">
          <el-form-item label="过滤空白行">
            <el-switch v-model="importConfig.filterEmptyRows" active-text="启用" inactive-text="禁用"/>
          </el-form-item>
          <el-form-item label="过滤重复行">
            <el-switch v-model="importConfig.filterDuplicateRows" active-text="启用" inactive-text="禁用"/>
          </el-form-item>
          <!-- 新增：跨行填充选项 -->
          <el-form-item label="跨行填充">
            <el-switch
                v-model="importConfig.crossRowFill"
                active-text="启用"
                inactive-text="禁用"
                tooltip="启用后，空值单元格将继承上一行对应列的非空数据（适用于共用行头/列数据场景）"
            />
          </el-form-item>
          <el-form-item label="构建模式">
            <el-switch style="margin-right: 10px;" v-model="importConfig.constructModel" active-text="启用" inactive-text="禁用"/>
            <el-card v-if="importConfig.constructModel">
              数组 [{a:'',b:''}]
            </el-card>
            <el-card v-else="importConfig.constructModel">
              集合 {a:[],b:[]}
            </el-card>
          </el-form-item>
          <el-form-item label="跳过行数">
            <el-input type="number" v-model="importConfig.skipRows" min="0"/>
          </el-form-item>
        </el-form>
      </el-card>

      <!-- 字段配置区域 -->
      <el-card shadow="hover" class="config-card" v-show="configVisible.fieldConfigVisible">
        <div class="card-header">
          <span>字段配置</span>
        </div>
        <div class="table-container-field">
          <table class="custom-table">
            <thead>
            <tr>
              <th>列头</th>
              <th>字段</th>
            </tr>
            </thead>
            <tbody style="max-height: 150px;">
            <tr v-for="tableColumn in tableColumns">
              <td style="width: 50%;"> <span style="display: inline;">{{tableColumn.label}}</span></td>
              <td style="width: 50%;">
                <el-select
                    v-model="tableColumn.field"
                    filterable
                    clearable
                    placeholder="请选择合适的字段">
                  <el-option v-for="et in importFormConfig"
                             :label="et.label"
                             :value="et.prop"
                  ></el-option>
                </el-select></td>
            </tr>
            <tr v-if="tableColumns.length===0">
              <td>No Data</td>
              <td>No Data</td>
            </tr>
            </tbody>
          </table>
        </div>
      </el-card>
      <!-- 解析结果表格 -->
      <div class="table-container" v-if="tableColumns.length > 0 && tableData.length > 0">
        <el-card shadow="hover">
          <div class="card-header">
            <span>数据预览</span>
            <div class="tag-group">
              <el-tag type="info">当前 {{ tableData.length }} 条数据</el-tag>
              <el-tag type="warning" v-if="stats.filteredEmptyRowCount > 0">已过滤 {{ stats.filteredEmptyRowCount }} 条空白行</el-tag>
              <el-tag type="success" v-if="stats.filteredDuplicateRowCount > 0">已过滤 {{ stats.filteredDuplicateRowCount }} 条重复行</el-tag>
              <!-- 新增：跨行填充统计标签 -->
              <el-tag type="primary" v-if="stats.filledCrossRowCount > 0">已跨行填充 {{ stats.filledCrossRowCount }} 个单元格</el-tag>
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
        <el-empty style="height: 50px;" description="请上传 Excel 文件解析数据" />
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
import {ref, reactive, onMounted,watch, defineExpose,defineProps,defineEmits} from 'vue';
import { ElMessage, ElEmpty, ElMessageBox, ElForm } from 'element-plus';
import * as XLSX from 'xlsx';
import request from "@/request/index.ts";

// 响应式状态
const fileList = ref([]);
const originalData = ref([]);
const tableColumns = ref([]);
const tableData = ref([]);
const visible=ref(false)
const setVisible=(v)=>{
  visible.value=v
}
const configVisible=reactive({
  importConfigVisible:false,
  fieldConfigVisible:false
})
// 导入配置（新增 crossRowFill 字段）
const importConfig = reactive({
  filterEmptyRows: true,
  filterDuplicateRows: true,
  constructModel:true,
  skipRows: 0, //跳过的行数
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
  if (file.size > 100 * 1024 * 1024) {
    ElMessage.error('文件大小不能超过 100MB');
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
      console.error('Excel 解析错误：', error);
    }
  };
  reader.readAsArrayBuffer(file);
};

/**
 * 新增：跨行填充处理逻辑
 * @param rows 待处理的数据行（不含表头）
 * @returns 处理后的行数据 + 填充统计数
 */
const handleCrossRowFill = (rows) => {
  if (!importConfig.crossRowFill || rows.length === 0) {
    return { filledRows: rows, fillCount: 0 };
  }

  let fillCount = 0;
  // 深拷贝避免修改原始数据
  const filledRows = JSON.parse(JSON.stringify(rows));

  // 遍历行（从第二行开始，继承上一行数据）
  for (let i = 1; i < filledRows.length; i++) {
    const currentRow = filledRows[i];
    const prevRow = filledRows[i - 1];

    // 遍历当前行的每一列
    for (let j = 0; j < currentRow.length; j++) {
      const cellValue = currentRow[j];
      // 空值判断（null/undefined/空字符串/纯空格）
      const isEmpty = cellValue === undefined || cellValue === null ||
          (typeof cellValue === 'string' && cellValue.trim() === '');

      if (isEmpty && prevRow[j] !== undefined && prevRow[j] !== null) {
        // 用上行非空数据填充当前空单元格
        currentRow[j] = prevRow[j];
        fillCount++;
      }
    }
  }

  return { filledRows, fillCount };
};

/**
 * 根据配置处理数据（过滤、去重、新增跨行填充）
 */
const processData = (jsonData) => {
  jsonData=jsonData.slice(Number(importConfig.skipRows),jsonData.length)

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

  // 步骤1：先处理跨行填充
  const { filledRows, fillCount } = handleCrossRowFill(dataRows);
  stats.value.filledCrossRowCount = fillCount;

  let processedRows = [...filledRows.map((row) => ({ data: row }))];
  const newStats = {
    filteredEmptyRowCount: 0,
    filteredDuplicateRowCount: 0,
    filledCrossRowCount: fillCount // 同步填充统计
  };

  // 步骤2：过滤空白行（基于填充后的数据）
  if (importConfig.filterEmptyRows) {
    const nonEmptyRows = processedRows.filter(row =>
        row.data.some(cell => cell !== undefined && cell !== null && cell.toString().trim() !== '')
    );
    newStats.filteredEmptyRowCount = processedRows.length - nonEmptyRows.length;
    processedRows = nonEmptyRows;
  }

  // 步骤3：过滤重复行
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

  //自动匹配列名构造对应的field
  handleAutoMatchColumnLabelAndField()
};
/**
 *
 * 自动匹配列名构造对应的field
 */
const handleAutoMatchColumnLabelAndField=()=>{
    tableColumns.value=tableColumns.value.map(
        item=>{
           props.importFormConfig.forEach(
               c=>{
                   if(c.label.trim()===item.label.trim()){
                         item.field=c.prop
                   }
               }
           )
          return item
        }
    )
}
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
  importConfig.skipRows=0;
  importConfig.filterEmptyRows = true;
  importConfig.filterDuplicateRows = true;
  importConfig.constructModel=true;
  importConfig.crossRowFill = false; // 重置跨行填充开关
};

/**
 * 提交导入
 */
const submitImport = async () => {
  // 构造请求数据
  let dataToSubmit
  if(importConfig.constructModel){
    dataToSubmit = tableData.value.map(row => {
      const { _displayIndex, ...cleanRow } = row;
      const targetForm={}
      tableColumns.value.forEach(
          item=>{
            if(item?.field){
              let formatter=props.importFormConfig.find(c=>c.prop===item.field)?.formatter
              if(formatter){
                 targetForm[item.field]=formatter(cleanRow[item.prop])
              }else{
                 targetForm[item.field]=cleanRow[item.prop]
              }
            }
          }
      )
      return targetForm;
    }).filter(item=>Object.keys(item).length !== 0);
  }else{
    dataToSubmit={}
    tableData.value.forEach(row => {
      const { _displayIndex, ...cleanRow } = row;
      tableColumns.value.forEach(
          item=>{
            if(item?.field){
               if(!dataToSubmit[item.field]){
                   dataToSubmit[item.field]=[]
               }
               dataToSubmit[item.field].push(cleanRow[item.prop])
            }
          }
      )
    })
  }
  if(dataToSubmit.length===0){
    ElMessage.error('导入失败,数据为[]');
     return
  }
  console.log(dataToSubmit)
  const res=await request.post(`${props.importApi}`,dataToSubmit)
  if(res.status===200){
    emit('success-form')
    ElMessage.success('导入成功！');
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
  overflow: auto;
}
.table-container-field {
  /* 可选：添加边框和阴影美化 */
  border: 1px solid #e0e0e0;
  border-radius: 4px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
}

/* 表格整体样式 */
.custom-table {
  width: 100%;
  border-spacing: 0;
  border-radius: 8px;
  border-collapse: collapse;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.05);
  position: relative;
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
.config-card { margin-bottom: 10px; overflow: auto;word-break: keep-all;}
.table-container { animation: fadeIn 0.3s ease-in-out; }
.card-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 15px; flex-wrap: wrap; gap: 10px; }
.tag-group { display: flex; gap: 10px; flex-wrap: wrap; }
.empty-state { margin: 50px auto; max-width: 400px; }
.action-bar { margin-top: 20px; display: flex; justify-content: flex-end; gap: 10px; }
@keyframes fadeIn { from { opacity: 0; transform: translateY(10px); } to { opacity: 1; transform: translateY(0); } }

/* 针对 radio-button 的样式优化（可选） */
/* 你可以根据 Element Plus 的主题色进行调整，这里是默认的 primary color (#409EFF) */
.area-radio-group {
  display: flex;
  gap: 10px; /* 选项之间的间距 */
}

.area-radio-button .el-radio-button__inner {
  /* 未选中时的样式 */
  border-color: #dcdfe6;
  background: #fff;
  color: #606266;
  transition: all 0.3s ease;
}

.area-radio-button.is-active .el-radio-button__inner {
  /* 选中时的样式（高亮） */
  border-color: #409EFF;
  background-color: #409EFF;
  color: #fff;
  box-shadow: -1px 0 0 0 #409EFF; /* 相邻边框颜色同步 */
}

.area-radio-button:hover .el-radio-button__inner {
  /* 鼠标悬停时的样式 */
  border-color: #c6e2ff;
  color: #409EFF;
}
</style>