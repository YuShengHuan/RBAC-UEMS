<template>
  <el-dialog
      v-model="visible"
      :title="'预览数据'"
      :show-close="true"
      width="95%"
  >
    <el-table
        class="el-table"
        :data="tableData"
        :max-height="tableMaxHeight"
        :header-cell-style="{background:'#f5f7fa',fontWeight: 500 }"
    >
      <el-table-column
          v-for="(item, idx) in tableColumns"
          :key="idx"
          :prop="item.prop"
          :label="item.label"
          align="center"
      >
        <template #default="scope">
               <span :style="item.cellStyleFn?.(scope.row)">
                   {{
                   item.formatter?item.formatter(scope.row):scope.row[item.prop]
                 }}
               </span>
        </template>
      </el-table-column>
    </el-table>
  </el-dialog>
</template>

<script setup>
import {defineEmits, defineExpose, defineProps, onMounted, reactive, ref, watch} from 'vue';
const visible=ref(false)
const setVisible=(v)=>{
  visible.value=v
}
const props=defineProps(
    {
      tableData:{
        type:Array,
        required:true,
        default:()=>([])
      },
      tableColumns:{
        type:Array,
        required:true,
        default:()=>([])
      }
    }
)
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
</style>