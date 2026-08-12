<template>
  <el-dialog
      v-model="visible"
      title="自定义导出列"
      width="95%"
  >
    <div class="export-config-container">
      <p class="config-desc">拖拽调整列顺序，勾选需要导出的列。</p>
      <!-- 列配置列表 -->
      <el-table
          :data="tableColumns"
          border
          :max-height="tableMaxHeight"
          row-key="prop"
          class="column-table"
      >
        <el-table-column label="拖拽调整" width="100" align="center">
          <template #default>
            <el-icon class="drag-icon">
              <Menu />
            </el-icon>
          </template>
        </el-table-column>
        <el-table-column label="列名" min-width="150">
          <template #default="scope">
            <el-checkbox
                v-model="scope.row.checked"
                :label="scope.row.label"
            >{{ scope.row.label }}</el-checkbox>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="150" align="center">
          <template #default="scope">
            <el-button
                type="danger"
                @click="handleDeleteColumn(scope.row.prop)"
            >删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 未选择的列 -->
      <div class="unused-columns" v-if="unusedColumns.length > 0">
        <p class="unused-desc">未选择的列:</p>
        <el-checkbox-group v-model="selectedUnusedProps">
          <el-checkbox
              v-for="col in unusedColumns"
              :key="col.prop"
              :label="col.prop"
              class="unused-checkbox"
          >
            {{ col.label }}
          </el-checkbox>
        </el-checkbox-group>
        <el-button
            type="success"
            @click="handleAddColumns"
            class="add-btn"
        >
          添加选中列
        </el-button>
      </div>
      <div class="export-config-form">
          <div class="export-config-form-tile">导出配置</div>
          <el-form  :label-width="80">
             <el-form-item label="文件名">
                 <el-input v-model="exportConfig.fileName"/>
             </el-form-item>
            <el-form-item label="文件类型">
              <el-select v-model="exportConfig.fileType">
                 <el-option v-for="item in [
                     {label:'表格（xlsx）',value:'.xlsx'},
                     {label:'表格（xls）',value:'.xls'},
                     ]"
                            :key="item.value"
                            :label="item.label"
                            :value="item.value"/>
              </el-select>
            </el-form-item>
          </el-form>
      </div>
    </div>
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="visible=false">取消</el-button>
        <el-button type="primary" @click="handleExport">开始导出</el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script setup lang="ts">
import {ref, defineExpose, onMounted, nextTick, toRef, reactive, onBeforeUnmount} from 'vue';
import { ElMessage } from 'element-plus';
import Sortable from 'sortablejs';
import { Menu} from '@element-plus/icons-vue';
import * as XLSX from 'xlsx'; // 1. 引入 xlsx 库
// 1. 定义 props 和 emits
const props = defineProps<{
  // 原始列配置
  columns: any[];
}>();

// 2. 响应式数据
const visible = ref(false);
const exportConfig=reactive({
  fileName:'导出数据',
  fileType:'.xlsx'
})
const tableColumns = ref<any[]>([]);
const unusedColumns = ref<any[]>([]);
const tableData=ref<any[]>([]);
const selectedUnusedProps = ref<string[]>([]);

// 4. 初始化配置
const initConfig =() => {
  // 深拷贝原始列配置，并添加 checked 属性
  tableColumns.value = props.columns.map(col => ({
    ...col,
    checked: true, // 默认全部勾选
  }));
  // 初始化未选择的列为空
  unusedColumns.value = [];
  selectedUnusedProps.value = [];
  // 初始化拖拽
  nextTick(() => {
    initDrag();
  });
};
const setVisible=(val)=>{
    visible.value=val
    if(val){
        initConfig()
    }
}
const setTableData=(val)=>{
    tableData.value=val
}

// 5. 初始化拖拽功能
const initDrag = () => {
  const table = document.querySelector('.column-table .el-table__body-wrapper tbody');
  if (!table) return;

  new Sortable(table, {
    animation: 150,
    handle: '.drag-icon', // 指定拖拽触发元素
    onEnd: (evt) => {
      // 拖拽结束后更新列顺序
      const newColumns = [...tableColumns.value];
      const movedItem = newColumns.splice(evt.oldIndex, 1)[0];
      newColumns.splice(evt.newIndex, 0, movedItem);
      tableColumns.value = newColumns;
    },
  });
};

// 6. 处理列的删除
const handleDeleteColumn = (prop: string) => {
  const deletedCol = tableColumns.value.find(col => col.prop === prop);
  if (deletedCol) {
    // 将删除的列移动到“未选择的列”
    tableColumns.value = tableColumns.value.filter(col => col.prop !== prop);
    unusedColumns.value.push(deletedCol);
  }
};

// 7. 处理列的添加
const handleAddColumns = () => {
  if (selectedUnusedProps.value.length === 0) return;

  selectedUnusedProps.value.forEach(prop => {
    const addedCol = unusedColumns.value.find(col => col.prop === prop);
    if (addedCol) {
      // 将选中的列添加回表格，并从“未选择的列”中移除
      tableColumns.value.push({ ...addedCol, checked: true });
      unusedColumns.value = unusedColumns.value.filter(col => col.prop !== prop);
    }
  });
  selectedUnusedProps.value = [];
};
const tableMaxHeight = ref(0)
// 计算表格最大高度
const calcTableHeight = () => {
  const windowHeight = window.innerHeight
  const tableTop = document.querySelector('.el-table')?.getBoundingClientRect().top
  // 留出 50px 边距
  tableMaxHeight.value = windowHeight - tableTop - 65
}
onMounted(() => {
  calcTableHeight()
  window.addEventListener('resize', calcTableHeight)
})

onBeforeUnmount(() => {
  window.removeEventListener('resize', calcTableHeight)
})
// 8. 处理导出
const handleExport = () => {
  // 获取所有勾选的列
  const selectedColumns = tableColumns.value.filter(col => col.checked);
  if (selectedColumns.length === 0) {
    ElMessage.warning('请至少选择一列进行导出');
    return;
  }
  // 准备导出的数据
  const exportData = tableData.value.map(row => {
    const formattedRow: any = {};
    selectedColumns.forEach(col => {
      formattedRow[col.label] = col.formatter
          ? col.formatter(row)
          : row[col.prop] !== undefined ? row[col.prop] : '';
    });
    return formattedRow;
  });

  //应用导出函数
  handleExportData({
    columns: selectedColumns,
    data: exportData,
  });
};
const handleExportData = ({ data }: { columns: any[], data: any[] }) => {
  try {
    // 检查是否有数据可供导出
    if (!data || data.length === 0) {
      ElMessage.warning('没有数据可供导出');
      return;
    }
    const workbook = XLSX.utils.book_new();
    const worksheet = XLSX.utils.json_to_sheet(data);

    XLSX.utils.book_append_sheet(workbook, worksheet, '导出数据');
    XLSX.writeFile(workbook, `${exportConfig.fileName}${exportConfig.fileType}`);

    // . 导出成功提示
    ElMessage.success('Excel 文件导出成功！');

  } catch (error) {
    ElMessage.error('导出失败，请稍后重试');
  }
};
defineExpose({
   setVisible,
   setTableData
})
</script>

<style scoped>
.export-config-container {
  overflow-y: auto;
}

.config-desc, .unused-desc {
  margin: 10px 0;
  color: #666;
  font-size: 14px;
}

.column-table {
  margin-bottom: 20px;
  max-height: 200px;
  overflow: auto;
}

.drag-icon {
  cursor: move;
  color: #999;
  font-size: 18px;
}

.unused-columns {
  padding: 15px;
  border:1px solid #efefef;
  border-radius: 4px;
}
.export-config-form{
  border:1px solid #efefef;
   margin-top: 10px;
}
.export-config-form-tile{
   margin: 10px;
}
.unused-checkbox {
  margin-right: 15px;
  margin-bottom: 10px;
  display: inline-block;
}

.add-btn {
  margin-top: 10px;
}

.dialog-footer {
  text-align: right;
}
</style>