<template>
  <div style="width: 300px; margin: 20px;">
    <!-- 输入框 + 弹出面板 -->
    <el-popover
        v-model="popoverVisible"
        placement="bottom-start"
        trigger="click"
        :close-on-click-outside="true"
        popper-class="custom-popover"
    >
      <!-- 弹出面板内容：表格 + 分页 -->
      <div class="popover-content">
        <!-- 表格（承载大量数据，支持勾选） -->
        <el-table
            :data="tableData"
            :show-header="false"
        border
        size="small"
        @row-click="handleRowClick"
        style="width: 100%;"
        >
        <el-table-column
            prop="name"
        align="center"
        ></el-table-column>
        </el-table>

        <!-- 分页（处理大量数据，避免卡顿） -->
        <el-pagination
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
            :current-page="currentPage"
            :page-sizes="[10, 20, 50, 100]"
            :page-size="pageSize"
            layout="total, sizes, prev, pager, next, jumper"
            :total="total"
            style="margin-top: 10px; text-align: right;"
        ></el-pagination>
      </div>

      <!-- 输入框（外观和普通输入框一致） -->
      <el-input
          slot="reference"
          v-model="selectedValue"
          placeholder="点击选择内容"
          readonly
      style="cursor: pointer;"
      ></el-input>
    </el-popover>
  </div>
</template>

<script>
export default {
  data() {
    return {
      popoverVisible: false,  // 控制弹出面板显示/隐藏
      selectedValue: '',      // 输入框绑定的值
      tableData: [],          // 表格数据（海量数据存储）
      currentPage: 1,         // 当前页码
      pageSize: 10,           // 每页条数
      total: 1000             // 总数据量（模拟海量数据）
    };
  },
  created() {
    // 初始化加载第一页数据（实际项目中替换为接口请求）
    this.loadTableData();
  },
  methods: {
    // 加载表格数据（模拟接口请求，支持分页）
    loadTableData() {
      // 实际项目中：调用后端接口，传入currentPage和pageSize获取对应页数据
      // 这里用模拟数据演示
      const start = (this.currentPage - 1) * this.pageSize;
      this.tableData = Array.from({ length: this.pageSize }, (_, i) => ({
        name: `选项${start + i + 1}`  // 模拟海量选项（如1000条）
      }));
    },

    // 表格行点击事件：选中行并回显到输入框
    handleRowClick(row) {
      this.selectedValue = row.name;  // 输入框显示选中的内容
      this.popoverVisible = false;    // 选中后关闭面板
    },

    // 每页条数改变
    handleSizeChange(val) {
      this.pageSize = val;
      this.currentPage = 1;  // 重置为第一页
      this.loadTableData();
    },

    // 页码改变
    handleCurrentChange(val) {
      this.currentPage = val;
      this.loadTableData();
    }
  }
};
</script>

<style scoped>
/* 自定义弹出面板样式，避免过宽/过窄 */
.custom-popover {
  width: 300px !important;  /* 和输入框宽度一致 */
}
.popover-content {
  max-height: 400px;  /* 限制面板高度，超出滚动 */
  overflow: auto;
}
</style>