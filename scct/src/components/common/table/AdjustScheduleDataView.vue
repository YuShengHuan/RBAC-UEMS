<template>
  <el-dialog
      v-model="visible"
      :title="'调整数据'"
      :show-close="true"
      width="95%"
      @close="handleClose"
  >
    <div class="dialog-content">
      <div class="class-selector">
        <span class="selector-label">选择班级：</span>
        <el-select size="large" v-model="currentClassCode" placeholder="请选择班级" filterable>
          <el-option
              v-for="op in classOptions"
              :key="op.value"
              :label="op.label"
              :value="op.value"
          />
        </el-select>
      </div>
      <TimeTable
          ref="timeTableRef"
          class="time-table"
      />
    </div>

    <template #footer>
      <span class="dialog-footer">
        <el-button @click="handleCancel">取消</el-button>
        <el-button type="primary" @click="handleSureAdjustData" :disabled="!currentClassCode">
          确认调整
        </el-button>
      </span>
    </template>
  </el-dialog>
</template>

<script setup lang="ts">
import TimeTable from '../card/TimeTable.vue'
import { ref, defineExpose, nextTick, watch, defineEmits, computed } from "vue";
import { ElMessage } from 'element-plus';

interface ClassOption {
  label: string;
  value: string;
}

interface TableCellData {
  isOccupy: boolean;
  span: { rowspan: number; colspan: number };
  currentPeriod: number;
  currentWeekDay: number;
  currentCourse: null;
  conflictCourseList:[];
}

interface TableRowData {
  period: number;
  [key: string]: TableCellData | number;
}

const visible = ref(false);
const currentClassCode = ref('');
const classOptions = ref<ClassOption[]>([]);
const timeTableRef = ref();
const adjustData = ref<CourseItem[]>([]);
const originalData = ref<CourseItem[]>([]); // 保存原始数据用于比较

const emit = defineEmits<{
  sure: [data: CourseItem[]];
  cancel: [];
  close: [];
}>();


// 设置对话框可见性
const setVisible = (v: boolean) => {
  visible.value = v;
  if (!v) {
    handleClose();
  }
};

// 设置数据
const setData = async (val: CourseItem[]) => {
  adjustData.value = JSON.parse(JSON.stringify(val)); // 深拷贝
  originalData.value = JSON.parse(JSON.stringify(val)); // 保存原始数据

  // 生成班级选项并去重
  const classMap = new Map();
  val.forEach(item => {
    if (!classMap.has(item.classCode)) {
      classMap.set(item.classCode, {
        label: item.className,
        value: item.classCode
      });
    }
  });

  classOptions.value = Array.from(classMap.values());

  await nextTick();

  // 默认选择第一个班级
  if (classOptions.value.length > 0 && !currentClassCode.value) {
    currentClassCode.value = classOptions.value[0].value;
  }
};

// 设置表格数据
const setTableData = (courseList: CourseItem[]) => {
  if (timeTableRef.value && timeTableRef.value.setTableData) {
    timeTableRef.value.setTableData(courseList);
  }
};

// 监听班级选择变化
watch(currentClassCode, (val) => {
  if (val) {
    const filterData = adjustData.value.filter(item => item.classCode === val);
    setTableData(filterData);
  } else {
    // 清空表格
    setTableData([]);
  }
}, { immediate: true });

// 监听表格数据变化
watch(
    () => timeTableRef.value?.getTableData?.(),
    (newTableData: TableRowData[]) => {
      if (!newTableData || !currentClassCode.value) return;

      // 从表格数据中提取课程信息
      const updatedCourses = extractCoursesFromTableData(newTableData);

      // 更新adjustData中对应班级的数据
      if (updatedCourses.length > 0) {
        // 移除该班级原有的课程数据
        adjustData.value = adjustData.value.filter(item => item.classCode !== currentClassCode.value);
        // 添加更新后的课程数据
        adjustData.value.push(...updatedCourses);
      }
    },
    { deep: true }
);

// 从表格数据中提取课程信息
const extractCoursesFromTableData = (tableData: TableRowData[]): CourseItem[] => {
  const courses: CourseItem[] = [];
  tableData.forEach(row => {
    Object.keys(row).forEach(key => {
      if (key !== 'period') {
        const cellData = row[key] as TableCellData;
        if (cellData.isOccupy && cellData.span.rowspan>0) {
            courses.push(cellData.currentCourse);
            cellData.conflictCourseList.forEach(
                 course=> courses.push(course)
            )
        }
      }
    });
  });

  return courses;
};

// 确认调整
const handleSureAdjustData = () => {
  if (!currentClassCode.value) {
    ElMessage.warning('请先选择班级');
    return;
  }
  emit('sure', adjustData.value);

  visible.value = false;
};

// 取消操作
const handleCancel = () => {
  // 恢复原始数据
  adjustData.value = JSON.parse(JSON.stringify(originalData.value));
  emit('cancel');
  visible.value = false;
};

// 对话框关闭
const handleClose = () => {
  // 重置状态
  currentClassCode.value = '';
  classOptions.value = [];
  adjustData.value = [];
  originalData.value = [];
  emit('close');
};

defineExpose({
  setVisible,
  setData
});
</script>

<style scoped>
.dialog-content {

}

.class-selector {
  margin: 0 10px;
  display: flex;
  align-items: center;
}

.selector-label {
  font-weight: 500;
  color: #606266;
  min-width: 80px;
}

.time-table {
  margin-top: 10px;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
}
</style>