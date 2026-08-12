<template>
  <el-dialog
      v-model="visible"
      :title="currentFormStatus ? '更新通知确认' : '创建通知确认'"
      :show-close="true"
      width="95%"
  >
    <el-form
        :model="formData"
        label-width="100px"
        class="sys-notice-confirm-form"
        ref="formRef"
    >
      <!-- 通知ID（下拉选择，加载所有通知列表） -->
      <el-form-item
          label="通知"
          prop="noticeId"
          :rules="[
          { required: true, message: '请选择通知', trigger: 'change' }
        ]"
      >
        <el-select
            v-model="formData.noticeId"
            placeholder="请选择通知"
        >
          <el-option
              v-for="notice in noticeOptions"
              :key="notice.value"
              :label="notice.label"
              :value="notice.value"
          />
        </el-select>
      </el-form-item>


      <!-- 用户ID（下拉选择，加载所有用户列表） -->
      <el-form-item
          label="用户"
          prop="userId"
          :rules="[
          { required: true, message: '请选择用户', trigger: 'change' }
        ]"
      >
        <el-select
            v-model="formData.userId"
            placeholder="请选择用户"
        >
          <el-option
              v-for="user in userOptions"
              :key="user.value"
              :label="user.label"
              :value="user.value"
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
import type { FormInstance } from 'element-plus';
import request from "../../../../request";
import {copyChangeKey, copySameKey} from "../../../../utils/commonUtil";
import {ElMessage} from "element-plus";

const props=defineProps({
  baseApi:{
    type:String,
    required:true
  }
})
const emit=defineEmits(
    ['success-form']
)
// 表单DOM引用
const formRef = ref<FormInstance>();

// 弹窗显示状态
const visible = ref(false);

// 设置弹窗显示/隐藏
const setVisible = (val: boolean) => {
  visible.value = val;
};

// 下拉选项数据源（接口加载）
const noticeOptions = ref([]); // 所有通知列表
const userOptions = ref([]);   // 所有用户列表

// 页面挂载时加载下拉数据
onMounted(async () => {
  // 加载通知列表（用于通知ID下拉）
  const noticeRes = await request.get(`/api/admin/notice/select`);
  if (noticeRes.status === 200) {
    noticeOptions.value = noticeRes.data;
  }

  // 加载用户列表（用于用户ID下拉）
  const userRes = await request.get(`/api/admin/user/select`);
  if (userRes.status === 200) {
    userOptions.value = userRes.data;
  }
});

// 通知确认表单数据（完全匹配AdminSysNoticeConfirmDTO）
const formData = reactive({
  id: undefined,                // 主键ID（编辑/删除/返回时用）
  noticeId: undefined,          // 通知ID（下拉选择）
  userId: undefined,            // 用户ID（下拉选择）
});

// 初始表单数据（用于重置）
const initialFormData = reactive({...formData});

// 重置表单
const resetForm=()=>{
  formRef.value?.clearValidate()
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
.sys-notice-confirm-form {
  padding: 20px 0;
}

.el-button-footer {
  text-align: right;
}
</style>