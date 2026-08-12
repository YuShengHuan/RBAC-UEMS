<template>
  <div class="h-full">
    <el-table
        :data="tableData"
        :span-method="arraySpanMethod"
        border
        :header-cell-style="headFirst"
        :cell-style="cellFirs"
        class="el-table schedule-table"
        :max-height="tableMaxHeight"
    >
      <template v-for="(c,index) in tableDataConfig">
        <el-table-column v-if="index===0" align="center">
          <template #header>
            <div>{{c.mouth}}月</div>
          </template>
          <!-- 节次列 -->
          <el-table-column label="节次/星期" align="center" width="115">
            <template #default="scope">
              {{ scope.row.period }}
            </template>
          </el-table-column>
        </el-table-column>
        <el-table-column v-else>
          <template #header>
            <div class="schedule-date">{{c.date}}日</div>
          </template>
          <!-- 星期列 -->
          <el-table-column :prop="c.prop" :label="c.label" align="center">
            <template #default="scope">
              <div class="cell-container"
                   @dragover.prevent
                   @drop="handleDrop($event, scope.row, c.prop)"
                   @dragenter="handleDragEnter($event)"
                   @dragleave="handleDragLeave($event)"
              >
                <!-- 主课程卡片（固定不可拖拽） -->
                <div
                    v-if="scope.row[c.prop].currentCourse && scope.row[c.prop].span.rowspan > 0"
                    draggable="true"
                    @dragstart="handleConflictDragStart($event, scope.row, c.prop, {...scope.row[c.prop].currentCourse},true)"
                    @dragend="handleDragEnd"
                    class="main-course-card"
                >
                  <div class="course-info">
                    <div class="course-name">{{ scope.row[c.prop].currentCourse.courseName }}</div>
                    <div class="course-detail">
                      <div>
                        {{ scope.row[c.prop].currentCourse.teacherName }}
                        /{{ scope.row[c.prop].currentCourse.className }}
                        /{{ scope.row[c.prop].currentCourse.classRoomName }}
                        /{{ scope.row[c.prop].currentCourse.weekRange }}
                      </div>
                    </div>
                  </div>
                  <el-popover
                      placement="bottom"
                      :show-arrow="false"
                      trigger="hover"
                      popper-style="display: flex;
  align-items: center;
  justify-content: center;;height:50px;min-width: 0px;padding: 0;width: auto;margin: 0;border-radius: 4px;box-shadow: 0 2px 12px rgba(0,0,0,0.1);overflow: hidden;"
                      :popper-options="{
                    modifiers: [
                      {
                        name: 'offset',
                        options: {
                          offset: [0, 2]  // 微调偏移，避免紧贴按钮
                        }
                      }
                    ]
                  }"
                  >
                    <template #default>
                      <div class="conflict-reason">
                        {{scope.row[c.prop].currentCourse?.conflictReason}}
                      </div>
                    </template>
                    <template #reference>
                      <span v-show="scope.row[c.prop].currentCourse?.isConflict" class="conflict-badge">冲突</span>
                    </template>
                  </el-popover>
                </div>
                <!-- 冲突课程列表（可拖拽） -->
                <div
                    v-for="(conflictItem, idx) in scope.row[c.prop].conflictCourseList"
                    :key="idx"
                    class="conflict-course-card"
                    draggable="true"
                    @dragstart="handleConflictDragStart($event, scope.row, c.prop, conflictItem,false)"
                    @dragend="handleDragEnd"
                >
                  <div class="conflict-info">
                    <div class="conflict-name">{{ conflictItem.courseName }}</div>
                    <div class="course-detail">
                      <div>
                        {{ conflictItem.teacherName }}
                        /{{ conflictItem.className }}
                        /{{ conflictItem.classRoomName }}
                        /{{ conflictItem.weekRange }}
                      </div>
                    </div>
                  </div>
                  <el-popover
                      placement="bottom"
                      :show-arrow="false"
                      trigger="hover"
                      popper-style="display: flex;
  align-items: center;
  justify-content: center;;height:50px;min-width: 0px;padding: 0;width: auto;margin: 0;border-radius: 4px;box-shadow: 0 2px 12px rgba(0,0,0,0.1);overflow: hidden;"
                      :popper-options="{
                    modifiers: [
                      {
                        name: 'offset',
                        options: {
                          offset: [0, 2]  // 微调偏移，避免紧贴按钮
                        }
                      }
                    ]
                  }"
                  >
                    <template #default>
                      <div class="conflict-reason">
                        时间段被占用
                      </div>
                    </template>
                    <template #reference>
                      <span class="conflict-badge">冲突</span>
                    </template>
                  </el-popover>
                </div>
              </div>
            </template>
          </el-table-column>
        </el-table-column>
      </template>
    </el-table>
  </div>
</template>

<script setup lang="ts">
import {onBeforeUnmount, onMounted, ref,defineExpose} from "vue";
// 构建课表数据
const buildCourseScheduleArray = () => {
  const data = [];
  for (let x = 0; x < 12; x++) {
    const item: any = {};
    tableDataConfig.forEach((c, index) => {
      if (index === 0) {
        item["period"] = x + 1;
      } else {
        item[c.prop] = {
          isOccupy: false,
          span: { rowspan: 1, colspan: 1 },
          currentPeriod:x+1,
          currentWeekDay:index,
          currentCourse: null,
          conflictCourseList: [],
        };
      }
    });
    data.push(item);
  }
  return data;
};
const dateByCurrentDayToDoConfig = (configDay: number, currentDate: Date = new Date()): Date => {
  // 1. 校验配置的星期合法性
  if (![1, 2, 3, 4, 5, 6, 7].includes(configDay)) {
    throw new Error('configDay必须是1-7（1=周一，2=周二...7=周日）');
  }
  // 2. 获取本周一的日期（本周的起始点）
  const today = new Date(currentDate);
  const todayWeekDay = today.getDay(); // 0=周日，1=周一...6=周六

  // 计算本周一的日期：今天减去「今天到周一的偏移天数」
  const daysToMonday = todayWeekDay === 0 ? 6 : todayWeekDay - 1; // 周日到周一需减6天，其他天减(星期数-1)
  const mondayOfWeek = new Date(today);
  mondayOfWeek.setDate(today.getDate() - daysToMonday);

  // 3. 计算配置星期相对于本周一的偏移天数（周一偏移0，周二偏移1...周日偏移6）
  const offsetDays = configDay - 1;

  // 4. 本周一 + 偏移天数 = 本周内配置星期的日期
  const targetDate = new Date(mondayOfWeek);
  targetDate.setDate(mondayOfWeek.getDate() + offsetDays);

  return targetDate;
};


const formatDateByConfig = (configDay: number, currentDate?: Date): string => {
  const targetDate = dateByCurrentDayToDoConfig(configDay, currentDate);
  return `${String(targetDate.getDate()).padStart(2, '0')}`;
};

const getCurrentMonth=()=>{
  return new Date().getMonth()+1
}
// 表格配置
//下面这些函数是依据这个配置顺序来操作，乱了就废了
const tableDataConfig = [
  { label: "节次", prop: "period",mouth:`${getCurrentMonth()}` },
  { label: "星期一", prop: "weekDay_1",date:`${formatDateByConfig(1)}` },
  { label: "星期二", prop: "weekDay_2",date:`${formatDateByConfig(2)}` },
  { label: "星期三", prop: "weekDay_3",date:`${formatDateByConfig(3)}`},
  { label: "星期四", prop: "weekDay_4" ,date:`${formatDateByConfig(4)}`},
  { label: "星期五", prop: "weekDay_5" ,date:`${formatDateByConfig(5)}` },
  { label: "星期六", prop: "weekDay_6" ,date:`${formatDateByConfig(6)}`},
  { label: "星期日", prop: "weekDay_7" ,date:`${formatDateByConfig(7)}`},
];
const tableMaxHeight = ref(0)
// 计算表格最大高度
const calcTableHeight = () => {
  const windowHeight = window.innerHeight
  const tableTop = document.querySelector('.el-table')?.getBoundingClientRect().top
  // 留出 50px 边距
  tableMaxHeight.value = windowHeight - tableTop -25
}
onMounted(async () => {
  calcTableHeight()
  window.addEventListener('resize', calcTableHeight)
})

onBeforeUnmount(() => {
  window.removeEventListener('resize', calcTableHeight)
})
// 响应式数据
const tableData = ref(buildCourseScheduleArray());
const setTableData=(courseList:[])=>{
  tableData.value=buildCourseScheduleArray()
  courseList.forEach(
       course=>{
         handleCourseLayUp(course)
       }
  )
}
const getTableData=()=>{
    return [...tableData.value]
}
defineExpose(
    {
      setTableData,
      getTableData
    }
)
const dragConflictInfo = ref({
  sourceRow: null,
  sourceProp: null,
  isDragMain:false,
  conflictCourse: null, // 被拖拽的冲突课程
  sourceIsMain: false, // 是否是主课程位置的冲突
});

// 表头样式
const headFirst = ({ columnIndex ,rowIndex}: any) => {
  if (columnIndex === 0&&rowIndex==0) {
    return { background: "rgb(4,133,246)", color: "#fff", fontWeight: "bold" };
  }
  let day=new Date().getDay()
  if(columnIndex===(day===0?7:day)&&rowIndex==0){
    return { background: "rgb(6,230,248)", color: "#2d2d2d", fontWeight: "bold" };
  }
  if (columnIndex > 0&&rowIndex==0) {
    return { background: "rgb(255,255,255)", color: "#2d2d2d", fontWeight: "bold" };
  }
  if (columnIndex === 0&&rowIndex==1) {
    return { background: "#7e7f80", color: "#fff", fontWeight: "bold" };
  }
  return { background: "#1e6eb4", color: "#fff", border: "none", height: "50px" };
};
// 单元格样式
const cellFirs = ({ columnIndex }: any) => {
  if (columnIndex === 0) {
    return { background: "#d6e5ef", color: "#0388fc", fontWeight: "bold",fontSize:'18px' };
  }
  return { color: "#2d2d2d",padding: "2px"}; // 加高单元格容纳冲突课程
};
// 合并单元格方法
const arraySpanMethod = ({ rowIndex, columnIndex }: any) => {
  if (columnIndex === 0 || rowIndex < 0) return [1, 1];
  return mergeCellSpan(rowIndex + 1, columnIndex);
};
const mergeCellSpan = (rowIndex: number, columnIndex: number) => {
  const targetRow = tableData.value.find(item => String(item["period"]) === String(rowIndex));
  if (!targetRow) return [1, 1];

  const weekDayKey = `weekDay_${columnIndex}`;
  const cellData = targetRow[weekDayKey];
  return cellData?.span ? [cellData.span.rowspan, cellData.span.colspan] : [1, 1];
};
//处理课程在表格中的放置
const handleCourseLayUp=(course)=>{

  const targetRow = tableData.value.find(tf => String(tf["period"]) === String(course.periodStart));
  if (!targetRow) return;

  const weekDayKey = `weekDay_${Number(course.weekDay)}`;
  let weekDayData = targetRow[weekDayKey];
  if (!weekDayData) return;
  //处理超过节次限制的课程
  if(Number(course.periodEnd)>12){
    weekDayData.conflictCourseList.push(course)
    return;
  }

  if (weekDayData.isOccupy) {
    // 添加为冲突课程
    if (weekDayData.span.rowspan > 0) {
      weekDayData.conflictCourseList.push(course);
    } else {
      let i = course.periodStart - 1;
      while (i >= 1) {
        const prevRow = tableData.value.find(tf => String(tf["period"]) === String(i));
        const prevCell = prevRow?.[weekDayKey];
        if (prevCell?.span.rowspan > 0) {
          prevCell.conflictCourseList.push(course);
          break;
        }
        i--;
      }
    }
  } else {
    const periodSpan =
        isNaN(course.periodEnd) ? 1 : (Number(course.periodEnd) - Number(course.periodStart))+1;
    if (periodSpan === 1) {
      weekDayData.isOccupy = true;
      weekDayData.currentCourse = course;
    } else {
      let i = 1;
      let isConflict = false;
      const nextDataArray: any[] = [];
      while (i < periodSpan) {
        const currentPeriod = Number(course.periodStart) + i;
        const nextRow = tableData.value.find(tf => String(tf["period"]) === String(currentPeriod));
        if (!nextRow) { isConflict = true; break; }

        const nextCell = nextRow[weekDayKey];
        if (nextCell.isOccupy) {
          nextCell.conflictCourseList.push(course);
          isConflict = true;
          break;
        } else {
          nextDataArray.push(nextCell);
        }
        i++;
      }
      if (!isConflict) {
        weekDayData.span = { rowspan: periodSpan, colspan: 1 };
        weekDayData.isOccupy = true;
        weekDayData.currentCourse = course;

        nextDataArray.forEach(nd => {
          nd.isOccupy = true;
          nd.currentCourse = course;
          nd.span = { rowspan: 0, colspan: 1 };
        });
      }
    }
  }
}
// ========== 冲突课程拖拽逻辑 ==========
// 冲突课程拖拽开始
const handleConflictDragStart = (e: DragEvent, row: any, prop: [string,null], conflictItem: any,isDragMain:boolean) => {
  dragConflictInfo.value = {
    sourceRow: row,
    sourceProp: prop,
    conflictCourse: { ...conflictItem }, // 深拷贝冲突课程数据
    isDragMain:isDragMain,
    sourceIsMain: !!row[prop].currentCourse, // 源位置是否有主课程
  };
  e.dataTransfer?.setData('text/plain', JSON.stringify(dragConflictInfo.value));
  (e.target as HTMLElement).classList.add('conflict-dragging');
};

// 拖拽结束
const handleDragEnd = (e: DragEvent) => {
  (e.target as HTMLElement).classList.remove('conflict-dragging');
};

// 拖拽进入目标
const handleDragEnter = (e: DragEvent) => {
  (e.currentTarget as HTMLElement).classList.add('drag-over');
};

// 拖拽离开目标
const handleDragLeave = (e: DragEvent) => {
  if ((e.currentTarget as HTMLElement).contains(e.relatedTarget as Node)) return;
  (e.currentTarget as HTMLElement).classList.remove('drag-over');
};

// 放置冲突课程
const handleDrop = (e: DragEvent, targetRow: any, targetProp: string) => {
  const targetCell = targetRow[targetProp];
  (e.currentTarget as HTMLElement).classList.remove('drag-over');

  // 无拖拽的冲突课程则返回
  if (!dragConflictInfo.value.conflictCourse) return;


  // 1. 从源位置移除冲突课程
  const sourceCell = dragConflictInfo.value.sourceRow[dragConflictInfo.value.sourceProp];
  if(!dragConflictInfo.value.isDragMain) {
    sourceCell.conflictCourseList = sourceCell.conflictCourseList.filter(
        (item: any) => item.code !== dragConflictInfo.value.conflictCourse.code
    );
  }else{
    //移除主课程
    const mainCourse = sourceCell.currentCourse; // 先保存主课程数据
    sourceCell.isOccupy = false;
    sourceCell.currentCourse = null;

// 还原合并的单元格（基于保存的mainCourse）
    if (mainCourse && sourceCell.span.rowspan > 1) {
      const periodSpan = Number(mainCourse.periodEnd) - Number(mainCourse.periodStart) + 1;
      let i = 1;
      while (i < periodSpan) {
        const currentPeriod = Number(mainCourse.periodStart) + i;
        const nextRow = tableData.value.find(tf => String(tf["period"]) === String(currentPeriod));
        if (nextRow) { // 防止找不到行
          const nextCell = nextRow[dragConflictInfo.value.sourceProp]; // 正确获取对应星期的单元格
          nextCell.isOccupy = false;
          nextCell.currentCourse = null; // 修正：赋值给nextCell而非sourceCell
          nextCell.span = { rowspan: 1, colspan: 1 };
        }
        i++;
      }
    }
// 还原当前单元格的span
    sourceCell.span = { rowspan: 1, colspan: 1 };

// 处理冲突课程（重新放置）
    if (sourceCell.conflictCourseList.length > 0) {
      const conflictCourseList = [...sourceCell.conflictCourseList];
      sourceCell.conflictCourseList = []; // 清空原冲突列表
      conflictCourseList.forEach(course => {
        handleCourseLayUp(course); // 重新调用放置逻辑
      });
    }
  }
  const conflictCourse = dragConflictInfo.value.conflictCourse;
  conflictCourse.weekDay=targetCell.currentWeekDay
  let rowSpan=(Number(conflictCourse.periodEnd)-Number(conflictCourse.periodStart))
  conflictCourse.periodStart=targetCell.currentPeriod
  conflictCourse.periodEnd =targetCell.currentPeriod+rowSpan
  // 2. 处理目标位置
  if (targetCell.isOccupy) {
    // 目标位置有主课程，添加为冲突
    targetCell.conflictCourseList.push(conflictCourse);
  } else {
    // 目标位置为空，设为主课程/加入冲突
    handleCourseLayUp(conflictCourse)
  }


  // 重置拖拽信息
  dragConflictInfo.value = {
    sourceRow: null,
    sourceProp: null,
    isDragMain: false,
    conflictCourse: null,
    sourceIsMain: false,
  };
};
</script>

<style scoped>
.h-full {
  padding: 10px;
  user-select: none;
  height: calc(100vh - 20px);
}

.schedule-table {
  --el-table-row-hover-bg-color: #f0f8ff;
}
/* 单元格容器 */
.cell-container {
  width: 100%;
  height: 100%;
  border-radius: 4px;
  padding: 4px;
}
.schedule-date{
  display: flex;
  align-items: center;
  justify-content: center;
  height: 100%;
  width: 100%;
}
.schedule-date-active{
  background-color: #12d2e1;
}
/* 主课程卡片（不可拖拽） */
.main-course-card {
  width: 100%;
  height: 100%;
  background: linear-gradient(135deg, #e8f4f8 0%, #f0f8fb 100%);
  border: 1px solid #1e6eb4;
  border-radius: 6px;
  padding: 6px;
  margin-bottom: 4px;
}

/* 冲突课程卡片（可拖拽） */
.conflict-course-card {
  width: 100%;
  background: linear-gradient(135deg, #fdf2f8 0%, #fef7fb 100%);
  border: 1px solid #ff4d4f;
  border-radius: 6px;
  padding: 6px;
  margin-bottom: 4px;
  cursor: grab;
  transition: all 0.2s;
}

.conflict-course-card.conflict-dragging {
  opacity: 0.5;
  cursor: grabbing;
}

/* 课程信息 */
.course-name {
  font-weight: bold;
  color: #1e6eb4;
  margin-bottom: 2px;
}

.conflict-name {
  font-weight: bold;
  color: #ff4d4f;
  margin-bottom: 2px;
}

.course-detail, .conflict-detail {
  font-size: 12px;
  color: #666;
  margin-bottom: 2px;
}

.course-week {
  font-size: 11px;
  color: #999;
}

/* 冲突徽章 */
.conflict-badge {
  font-size: 10px;
  background: #ff4d4f;
  color: #fff;
  padding: 1px 4px;
  border-radius: 3px;
  z-index: 100;
}
.conflict-reason{
  padding: 10px 15px;
}
/* 拖拽覆盖样式 */
.drag-over {
  background: #e6f7ff;
  border: 2px dashed #1890ff;
  min-height: 45px;
}
</style>