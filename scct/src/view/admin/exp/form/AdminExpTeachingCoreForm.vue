<template>
  <el-dialog
      v-model="visible"
      :title="currentFormStatus ? '更新数据' : '创建数据'"
      :show-close="true"
      width="95%"
  >
    <el-form
        :model="formData"
        label-width="100px"
        class="sys-user-form"
        ref="formRef"
    >
      <!-- 原有基础字段（保持不变） -->
      <el-form-item
          label="学期"
          prop="semester"
          :rules="[
          { required: true, message: '请输入学期', trigger: 'blur' },
          { pattern: /^\d{4}-\d{4}-\d$/, message: '请输入正确格式（如：2023-2024-1）', trigger: 'blur' }
        ]"
      >
        <SemesterPicker
            v-model="formData.semester"
        />
      </el-form-item>

      <el-form-item
          label="课程"
          prop="courseId"
          :rules="[
          { required: true, message: '请选择课程', trigger: 'change' }
        ]"
      >
        <el-select
            v-model="formData.courseId"
            placeholder="请选择课程"
            clearable
        >
          <el-option
              v-for="item in courseOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
          />
        </el-select>
      </el-form-item>

      <el-form-item
          label="授课教师"
          prop="userId"
          :rules="[
          { required: true, message: '请选择授课教师', trigger: 'change' }
        ]"
      >
        <el-select
            v-model="formData.userId"
            placeholder="请选择授课教师"
            clearable
        >
          <el-option
              v-for="item in userOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
          />
        </el-select>
      </el-form-item>

      <el-form-item
          label="班级"
          prop="classId"
          :rules="[
          { required: true, message: '请选择班级', trigger: 'change' }
        ]"
      >
        <el-select
            v-model="formData.classId"
            placeholder="请选择班级"
            clearable
        >
          <el-option
              v-for="item in classOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
          />
        </el-select>
      </el-form-item>
    </el-form>

    <template #footer>
      <div class="el-button-footer">
        <el-button @click="setVisible(false)">取消</el-button>
        <el-button type="primary" @click="handleSave">保存</el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script setup lang="ts">
import {
  defineExpose,
  reactive,
  computed,
  ref,
  defineProps,
  defineEmits, onMounted
} from 'vue';
import SemesterPicker from '../../../../components/common/form/SemesterPicker.vue'
import type { FormInstance } from 'element-plus';
import request from "../../../../request";
import {copyChangeKey, copySameKey} from "../../../../utils/commonUtil";
import {ElMessage} from "element-plus";
const props = defineProps({
  baseApi: {
    type: String,
    required: true
  }
})

const emit = defineEmits(['success-form'])

// 表单DOM引用
const formRef = ref<FormInstance>();

// 弹窗显示状态
const visible = ref(false);

// 设置弹窗显示/隐藏
const setVisible = (val: boolean) => {
  visible.value = val;
};

// 下拉选项（接口请求类选项）
const courseOptions = ref([]) //课程选项
const classOptions = ref([])  // 班级选项
const userOptions = ref([])   // 授课教师选项

// 初始化下拉选项
onMounted(async () => {
  let courseRes = await request.get(`/api/admin/course/select`)
  if (courseRes.status === 200) {
    courseOptions.value = courseRes.data
  }
  let classRes = await request.get(`/api/admin/class/select`)
  if (classRes.status === 200) {
    classOptions.value = classRes.data
  }
  let userRes = await request.get(`/api/admin/user/select?userType=2`)
  if (userRes.status === 200) {
    userOptions.value = userRes.data
  }
})

// 表单数据（完全对齐ExpCourseSchedule实体）
const formData = reactive({
  id: undefined,                // 课程安排ID
  semester: '' ,                 // 学期
  courseId: '',                 // 课程ID（字符串接收，提交时自动转数字）
  classId: '',                  // 班级ID
  userId: '',                   // 授课用户ID
});

// 初始表单数据（用于重置）
const initialFormData = reactive({ ...formData });

// 重置表单
const resetForm = () => {
  formRef.value?.clearValidate()
  Object.assign(formData, { ...initialFormData })
}

const initSourceData=reactive({})
const initFormData=(source)=>{
  copySameKey(formData,source)
  Object.assign(initSourceData, source)
}
// 当前表单状态 (0-创建 1-更新)
const currentFormStatus = computed(() => {
  return formData.id ? 1 : 0;
});
// 保存表单数据
const handleSave = async () => {
  if (!formRef.value) return;
  try {
    // 触发表单校验
    await formRef.value.validate();
    // 校验通过，提交数据
    console.log('表单数据提交:', formData);

    let res=null
    if(currentFormStatus.value==0){
      res=await request.post(`${props.baseApi}/create`,formData)
    }else if(currentFormStatus.value==1){
      let changeFormData=copyChangeKey(formData,initSourceData)
      if(changeFormData!=null){
        res=await request.put(`${props.baseApi}/update`,changeFormData)
      }
    }
    if(res?.status===200 || res?.status===201){
      setVisible(false);
      emit('success-form')
    }
  } catch (error) {
    // 校验失败，不提交
    ElMessage.error('表单校验失败:'+error);
  }
};
// 暴露给父组件的方法
defineExpose({
  setVisible,
  resetForm,
  initFormData
});
</script>

<style scoped>
.sys-user-form {
  padding: 20px 0;
}

.el-button-footer {
  text-align: right;
}

</style>