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
      <el-form-item
          label="角色"
          prop="roleId"
          :rules="[
          { required: true, message: '请输入角色', trigger: 'blur' },
          { min: 1, message: '角色长度在 >=1 个字符之间', trigger: 'blur' }
        ]"
      >
        <el-select
            v-model="formData.roleId"
            placeholder="请选择角色"
        >
          <el-option
              v-for="item in roleOptions"
              :label="item.label"
              :value="item.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item
          label="权限"
          prop="permId"
          :rules="[
          { required: true, message: '请输入权限', trigger: 'blur' },
          { min: 1, message: '权限长度在 >=1 个字符之间', trigger: 'blur' }
        ]"
      >
        <el-select
            v-model="formData.permId"
            placeholder="请选择权限"
        >
          <el-option
              v-for="item in permOptions"
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
const permOptions=ref([])
const roleOptions=ref([])
onMounted(async ()=>{
    let res=await request.get("/api/admin/role/select")
    if(res.status===200){
        roleOptions.value=res.data
    }
    res=await request.get("/api/admin/perm/select")
    if(res.status===200){
       permOptions.value=res.data
    }
})
// 用户表单数据
const formData = reactive({
  id: undefined,
  permId: '',
  roleId: '',
});
const initialFormData=reactive(
    {...formData}
)
const resetForm=()=>{
  formRef.value?.clearValidate()
  Object.assign(formData, initialFormData)
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
  // 可以暴露表单数据供父组件访问
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