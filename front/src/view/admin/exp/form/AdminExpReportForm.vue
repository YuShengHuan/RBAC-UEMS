<template>
  <el-dialog
      v-model="visible"
      :title="currentFormStatus ? '更新数据' : '创建数据'"
      :show-close="true"
      width="95%"
  >
    <el-form
        :model="formData"
        label-width="120px"
        class="sys-user-form"
        ref="formRef"
    >

      <!-- 实验项目 -->
      <el-form-item
          label="实验项目"
          prop="projectId"
          :rules="[
          { required: true, message: '请输入实验项目', trigger: 'blur' },
          { min: 1, message: '实验项目在 >=1 个字符之间', trigger: 'blur' }
        ]"
      >
        <el-select
            v-model="formData.projectId"
            placeholder="请选择实验项目"
        >
          <el-option
              v-for="item in projectOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
          />
        </el-select>
      </el-form-item>

      <el-form-item
          label="上传文件"
          prop="file"
          :rules="[
          { required: true, message: '请输入上传文件', trigger: 'blur' }
        ]"
      >
        <CustomFileUpload
            v-model:model-value="formData.file"
            v-model:file-tip="formData.filePath"
            accept="application/msword,application/vnd.openxmlformats-officedocument.wordprocessingml.document"
        />
      </el-form-item>

      <!-- 上传用户 -->
      <el-form-item
          label="上传用户"
          prop="uploadUserId"
          :rules="[
          { required: true, message: '请输入上传用户', trigger: 'blur' },
          { min: 1, message: '上传用户在 >=1 个字符之间', trigger: 'blur' }
        ]"
      >
        <el-select
            v-model="formData.uploadUserId"
            placeholder="请选择上传用户"
        >
          <el-option
              v-for="item in userOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
          />
        </el-select>
      </el-form-item>

      <!-- 附件类型 -->
      <el-form-item
          label="附件类型"
          prop="attachmentType"
          :rules="[
          { required: true, message: '请输入附件类型', trigger: 'blur' }
        ]"
      >
        <el-select
            v-model="formData.attachmentType"
            placeholder="请选择附件类型"
        >
          <el-option
              v-for="item in attachmentTypeOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
          />
        </el-select>
      </el-form-item>

      <!-- 备注 -->
      <el-form-item
          label="备注"
          prop="remark"
      >
        <el-input
            v-model="formData.remark"
            type="textarea"
            :rows="3"
            placeholder="请输入备注（可选）"
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
  computed,
  ref,
  defineProps,
  defineEmits,
  onMounted
} from 'vue';
import {attachmentTypeOptions} from "../../../../utils/globalOptionsUtil"
import CustomFileUpload from '../../../../components/common/form/CustomFileUpload.vue'
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
const projectOptions = ref([]); // 实验项目选项
const userOptions = ref([]);    // 上传用户选项


// 设置弹窗显示/隐藏
const setVisible = (val: boolean) => {
  visible.value = val;
};

// 加载下拉选项数据（实际接口根据项目调整）
onMounted(async () => {
  // 加载实验项目列表
  const projectRes = await request.get(`/api/admin/project/select`);
  if (projectRes.status === 200) {
    projectOptions.value = projectRes.data;
  }

  // 加载用户列表（如学生/教师列表）
  const userRes = await request.get(`/api/admin/user/select`);
  if (userRes.status === 200) {
    userOptions.value = userRes.data;
  }
});

// 实验报告表单数据（对应AdminExpReportDTO）
const formData = reactive({
  id: undefined,                // 报告ID
  projectId: '',                // 实验项目ID
  uploadUserId: '',             // 上传用户ID
  file: '',                 // 文件
  filePath: '',                 // 文件路径
  attachmentType: '',           // 附件类型（1-实验模板/2-实验报告）
  remark: ''                    // 备注
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
    const config={
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    }
    if(currentFormStatus.value==0){
      res=await request.post(`${props.baseApi}/create`,formData,config)
    }else if(currentFormStatus.value==1){
      let changeFormData=copyChangeKey(formData,initSourceData)
      if(changeFormData!=null){
        res=await request.put(`${props.baseApi}/update`,changeFormData,config)
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