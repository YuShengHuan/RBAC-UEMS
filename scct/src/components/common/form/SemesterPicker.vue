<template>
  <div class="semester-picker">
    <el-row :gutter="16" align="middle">
      <!-- 起始学年 -->
      <el-col :span="7">
        <el-select
            v-model="startYear"
            class="year-select"
            placeholder="选择起始学年"
            @change="handleYearChange"
        >
          <el-option
              v-for="year in yearOptions"
              :key="year"
              :label="year + '年'"
              :value="year"
          />
        </el-select>
      </el-col>

      <!-- 分隔符 -->
      <el-col :span="1" class="separator">
        -
      </el-col>

      <!-- 结束学年 -->
      <el-col :span="7">
        <el-select
            v-model="endYear"
            class="year-select"
            placeholder="选择结束学年"
            :disabled="!startYear"
        >
          <el-option
              v-for="year in endYearOptions"
              :key="year"
              :label="year + '年'"
              :value="year"
          />
        </el-select>
      </el-col>

      <!-- 第几学期 -->
      <el-col :span="7">
        <el-select
            v-model="semester"
            class="semester-select"
            placeholder="选择学期"
        >
          <el-option label="第一学期" value="1" />
          <el-option label="第二学期" value="2" />
        </el-select>
      </el-col>
    </el-row>
  </div>
</template>

<script setup lang="ts">
import { ref, watch, computed } from 'vue';
import { defineProps, defineEmits } from 'vue';

// 定义props，支持v-model
const props = defineProps({
  modelValue: {
    type: String,
    default: '',
    // 校验输入格式：如2025-2026-1
    validator: (val: string) => {
      if (!val) return true;
      const reg = /^\d{4}-\d{4}-[12]$/;
      return reg.test(val);
    }
  }
});

// 定义emits，触发v-model更新
const emit = defineEmits(['update:modelValue']);

// 核心数据：起始学年、结束学年、学期
const startYear = ref<string>('');
const endYear = ref<string>('');
const semester = ref<string>('');

// 生成学年选项（当前年前后5年）
const currentYear = new Date().getFullYear();
const yearOptions = computed(() => {
  const options: string[] = [];
  for (let i = currentYear +5; i >= currentYear - 20; i--) {
    options.push(i.toString());
  }
  return options;
});

// 结束学年选项（必须是起始学年+1）
const endYearOptions = computed(() => {
  if (!startYear.value) return [];
  const nextYear = (Number(startYear.value) + 1).toString();
  return [nextYear];
});

// 监听modelValue变化，自动解析格式（输入时拆分）
watch(
    () => props.modelValue,
    (val) => {
      if (val) {
        const [sYear, eYear, sem] = val.split('-');
        startYear.value = sYear;
        endYear.value = eYear;
        semester.value = sem;
      } else {
        // 清空输入时重置
        startYear.value = '';
        endYear.value = '';
        semester.value = '';
      }
    },
    { immediate: true}
);

// 起始学年变化时，自动填充结束学年（起始+1）
const handleYearChange = () => {
  if (startYear.value) {
    endYear.value = (Number(startYear.value) + 1).toString();
  }
};

// 监听核心数据变化，自动拼接格式（输出时组合）
watch(
    [() => startYear.value, () => endYear.value, () => semester.value],
    ([sYear, eYear, sem]) => {
      if (sYear && eYear && sem) {
        // 拼接为2025-2026-1格式
        const result = `${sYear}-${eYear}-${sem}`;
        emit('update:modelValue', result);
      } else {
        emit('update:modelValue', '');
      }
    },
    { immediate: true }
);
</script>

<style scoped>
.semester-picker {
  width: 100%;
  display: inline-block;
}

.year-select,
.semester-select {
  width: 100%;
}

.separator {
  text-align: center;
  font-size: 16px;
  color: #606266;
}
</style>