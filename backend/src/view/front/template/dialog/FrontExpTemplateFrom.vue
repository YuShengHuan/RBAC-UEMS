<template>
  <el-dialog class="create-page"
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
          { required: true, message: '请输入实验项目', trigger: 'blur' }
        ]"
      >
        <el-select
            v-model="formData.projectId"
            filterable
            clearable
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
          { required: true, message: '请输入上传用户', trigger: 'blur' }
        ]"
      >
        <el-select
            v-model="formData.uploadUserId"
            disabled
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
            disabled
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
      <el-form-item>
        <el-button-group>
          <el-button class="cancel-button" @click="setVisible(false)">取消</el-button>
          <el-button class="save-button" style="margin-left: 10px;" type="primary" @click="handleSave">保存</el-button>
        </el-button-group>
      </el-form-item>
    </el-form>
  </el-dialog>
</template>

<script setup lang="ts">
import {
  reactive,
  ref,
  onMounted, defineExpose
} from 'vue';
import type { FormInstance } from 'element-plus';
import {attachmentTypeOptions} from "../../../../utils/globalOptionsUtil"
import CustomFileUpload from '../../../../components/common/form/CustomFileUpload.vue'
import {copyChangeKey, copySameKey} from "../../../../utils/commonUtil";
import request from "../../../../request";
import {ElMessage} from "element-plus";
import {computed, defineEmits, defineProps} from "vue";
import { baseApi } from "../../../../config/module/AdminExpReportConfig";
import {useUserStore} from "../../../../stores/user";
const emit=defineEmits(
    ['success-form']
)
const currentFormStatus = computed(() => {
  return formData.id ? 1 : 0;
});
// 弹窗显示状态
const visible = ref(false);

// 表单DOM引用
const formRef = ref<FormInstance>();
// 下拉选项（接口请求类选项）
// 下拉选项（接口请求类选项）
const projectOptions = ref([]); // 实验项目选项
const userOptions = ref([]);    // 上传用户选项
const isRequest=ref(false)
// 设置弹窗显示/隐藏
const setVisible = async (val: boolean) => {
  visible.value = val;
};
onMounted(
    async ()=>{
      const projectRes = await request.get(`/api/admin/project/select`);
      if (projectRes.status === 200) {
        projectOptions.value = projectRes.data;
      }
      const userRes= await request.get(`/api/admin/user/select?userType=2`);
      if(userRes.status==200){
        userOptions.value=userRes.data
      }
    }
)
const userStore=useUserStore()
// 实验报告表单数据（对应AdminExpReportDTO）
const formData = reactive({
  id: undefined,                // 报告ID
  projectId: '',                // 实验项目ID
  uploadUserId: userStore.userInfo.id,             // 上传用户ID
  file: '',                 // 文件
  filePath: '',                 // 文件路径
  attachmentType: 1,           // 附件类型（1-实验模板/2-实验报告）
  remark: ''                    // 备注
});
const initialFormData=reactive({...formData})
// 重置表单
const resetForm = () => {
  formRef.value?.clearValidate();
  Object.assign(formData, { ...initialFormData });
};

// 初始化表单数据（从父组件传入，适配实体字段）
const initFormData=(source)=>{
  copySameKey(formData,source)
}

// 保存表单数据（提交前转换字段类型对齐实体）
const handleSave = async () => {
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
      res=await request.post(`${baseApi}/create`,formData,config)
    }else if(currentFormStatus.value==1){
      let changeFormData=copyChangeKey(formData,initSourceData)
      if(changeFormData!=null){
        res=await request.put(`${baseApi}/update`,changeFormData,config)
      }
    }
    if(res?.status===200 || res?.status===201){
      await setVisible(false);
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
  // 可以暴露表单数据供父组件访问
  resetForm,
  initFormData
});
</script>

<style scoped>
.create-page{
  height: calc(100% - 40px);
  width: calc(100% - 40px);
  padding: 20px;
  overflow: auto;
}
.save-button{
  width: 150px;
  margin-right: 10px;
}
.cancel-button{
  width: 100px;
}
</style>