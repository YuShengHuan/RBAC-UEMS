<template>
  <div class="table-pagination">
    <div class="top-table-operate-area">
      <slot name="topOperate" :pageParameter="{
        total: tableData.total,
        current: tableData.current,
        size: tableData.size,
        pages: tableData.pages
      }"></slot>
    </div>
    <el-table class="el-table" :data="tableData.records" :max-height="tableMaxHeight"
              row-key="id"
              :border="tableBorder"
    >
      <el-table-column v-if="tableFistColumnType==='selection'" type="selection"></el-table-column>
      <el-table-column v-else type="index" align="center" label="序号" width="80"></el-table-column>
      <template v-for="item in dataConfig">
        <el-table-column
            v-if="!item.hide"
            :label="item.label"
            align="center"
            show-overflow-tooltip
            min-width="100">
           <template #default="scope">
               <span :style="item.cellStyleFn?.(scope.row)">
                   {{
                   item.formatter?item.formatter(scope.row):scope.row[item.prop]
                 }}
               </span>
           </template>
        </el-table-column>
      </template>
      <el-table-column fixed="right" label="操作" align="center" width="70" >
          <template #default="scope">
              <el-popover
                  placement="left"
                  :show-arrow="false"
                  popper-style="min-width: 0px;padding: 0;width: auto;margin: 0;"
              >
                <template #default>
                   <el-button-group class="row-operate-button-area">
                     <slot name="rowOperate" :index="scope.$index" :row="scope.row" />
                   </el-button-group>
                </template>
                <template #reference>
                  <el-icon
                      class="el-icon-more-operate"
                  >
                    <MoreFilled />
                  </el-icon>
                </template>
              </el-popover>
          </template>
      </el-table-column>
    </el-table>
    <el-pagination
        style="width: 100%;background-color: #FFFFFF;"
        :current-page="tableData.current"
        :page-size="tableData.size"
        :page-sizes="[10, 20, 30, 40,50,60,70,80,90]"
        background
        layout="slot,sizes,prev, pager, next"
        :total="tableData.total"
        prev-text="上一页"
        next-text="下一页"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        class="el-pagination"
    >
      <template #default>
        <div style="margin-left: 10px;">
          总条数：{{tableData.total}}
        </div>
      </template>
    </el-pagination>
  </div>
</template>

<script lang="ts" setup>
import {MoreFilled} from '@element-plus/icons-vue'
import {ref,defineProps,defineEmits,watch,onMounted,onBeforeUnmount} from 'vue'
//分页变量配置
const props=defineProps({
  dataConfig:{
    type:Array,
    required:true,
    default:()=>[]
  },
  tableData:{
    type:Object,
    required:true,
    default:()=>[]
  },
  tableBorder:{
    type:Boolean,
    default:false
  },
  tableFistColumnType:{
     type:String,
     default:'selection'
  }
})
const emit=defineEmits(['sizeChange','currentChange'])
//分页变化事件
const handleSizeChange =(val) => {
  emit('sizeChange',val)
}
const handleCurrentChange = (val) => {
  emit('currentChange',val)
}
const tableMaxHeight = ref(0)
// 计算表格最大高度
const calcTableHeight = () => {
  const windowHeight = window.innerHeight
  const tableTop = document.querySelector('.el-table').getBoundingClientRect().top
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
</script>
<style scoped>
.top-table-operate-area{
  margin-bottom: 10px;
}
.table-pagination{
  width: 100%;
  height: 100%;
}
.el-table{
  width: 100%;
  height:calc(100% - 60px);
}
.row-operate-button-area{
  display: flex;
  align-items: center;
  justify-content: center;
}
.el-pagination{
  height: 60px;
  overflow-y: auto;
}
</style>
