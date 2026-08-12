<template>
  <el-dialog
      v-model="visible"
      :title="currentFormStatus ? '更新批改' : '创建批改'"
      :show-close="true"
      width="95%"
  >
    <el-form
        :model="formData"
        label-width="120px"
        class="sys-user-form"
        ref="formRef"
    >
      <!-- 实验报告（关联） -->
      <el-form-item
          label="实验报告"
          prop="reportId"
          :rules="[
          { required: true, message: '请选择实验报告', trigger: 'change' }
        ]"
      >
        <el-select
            v-model="formData.reportId"
            placeholder="请选择实验报告"
        >
          <el-option
              v-for="item in reportOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
          />
        </el-select>
      </el-form-item>

      <!-- 批改用户 -->
      <el-form-item
          label="批改用户"
          prop="reviewUserId"
          :rules="[
          { required: true, message: '请选择批改用户', trigger: 'change' }
        ]"
      >
        <el-select
            v-model="formData.reviewUserId"
            placeholder="请选择批改用户"
        >
          <el-option
              v-for="item in reviewUserOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
          />
        </el-select>
      </el-form-item>


      <!-- 成绩 -->
      <el-form-item
          label="成绩"
          prop="score"
          :rules="[
          { required: true, message: '请输入成绩', trigger: 'blur' }
        ]"
      >
        <el-input
            v-model="formData.score"
            type="number"
            placeholder="请输入成绩（0-100）"
        />
      </el-form-item>

      <!-- 查重率 -->
      <el-form-item
          label="查重率(%)"
          prop="plagiarismRate"
      >
        <el-input
            v-model="formData.plagiarismRate"
            type="number"
            placeholder="请输入查重率（可选）"
        />
      </el-form-item>

      <!-- 批阅意见 -->
      <el-form-item
          label="批阅意见"
          prop="reviewComment"
      >
        <el-input
            v-model="formData.reviewComment"
            type="textarea"
            :rows="3"
            placeholder="请输入批阅意见"
        />
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
  ref,
  defineProps,
  defineEmits,
    computed,
  onMounted
} from 'vue';
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

// 下拉选项（接口请求类选项）
const reportOptions = ref([]); // 实验报告列表（关联批改）
const reviewUserOptions = ref([]); // 批改用户列表（教师等）

// 设置弹窗显示/隐藏
const setVisible = (val: boolean) => {
  visible.value = val;
};

// 加载下拉选项数据
onMounted(async () => {
  // 加载实验报告列表（用于选择关联报告）
  const reportRes = await request.get(`/api/admin/report/select`);
  if (reportRes.status === 200) {
    reportOptions.value = reportRes.data;
  }

  // 加载批改用户列表（如教师列表）
  const userRes = await request.get(`/api/admin/user/select`);
  if (userRes.status === 200) {
    reviewUserOptions.value = userRes.data;
  }
});

// 实验报告批改表单数据（对应AdminExpReportReviewDTO）
const formData = reactive({
  id: undefined,                // 批改ID
  reportId: '',                 // 关联实验报告ID
  reviewUserId: '',             // 批改用户ID
  score: 0,                     // 成绩
  reviewComment: '',            // 批阅意见
  plagiarismRate: 0             // 查重率
});

// 初始表单数据（用于重置）
const initialFormData = reactive({ ...formData });

// 重置表单
const resetForm = () => {
  formRef.value?.clearValidate();
  Object.assign(formData, initialFormData);
};
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