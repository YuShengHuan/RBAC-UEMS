<template>
  <el-dialog
      v-model="visible"
      :title="currentFormStatus ? '更新通知' : '创建通知'"
      :show-close="true"
      width="95%"
  >
    <el-form
        :model="formData"
        label-width="100px"
        class="sys-notice-form"
        ref="formRef"
    >
      <!-- 通知标题 -->
      <el-form-item
          label="通知标题"
          prop="noticeTitle"
          :rules="[
          { required: true, message: '请输入通知标题', trigger: 'blur' },
          { min: 1, max: 50, message: '标题长度在1-50个字符之间', trigger: 'blur' }
        ]"
      >
        <el-input
            v-model="formData.noticeTitle"
            placeholder="请输入通知标题"
        />
      </el-form-item>

      <!-- 通知内容 -->
      <el-form-item
          label="通知内容"
          prop="noticeContent"
          :rules="[
          { required: true, message: '请输入通知内容', trigger: 'blur' },
          { min: 1, message: '内容长度至少1个字符', trigger: 'blur' }
        ]"
      >
        <el-input
            v-model="formData.noticeContent"
            placeholder="请输入通知内容"
            type="textarea"
            :rows="4"
        />
      </el-form-item>

      <!-- 通知类型（下拉选择） -->
      <el-form-item
          label="通知类型"
          prop="noticeType"
          :rules="[
          { required: true, message: '请选择通知类型', trigger: 'change' }
        ]"
      >
        <el-select
            v-model="formData.noticeType"
            placeholder="请选择通知类型"
            @change="handleNoticeTypeChange"
        >
          <el-option v-for="item in noticeTypeOptions"
                     :key="item.value"
                     :label="item.label"
                     :value="item.value"
          />
        </el-select>
      </el-form-item>

      <!-- 目标ID/名称（根据类型加载对应下拉选项） -->
      <el-form-item
          v-show="formData.noticeType"
          label="目标选择"
          prop="targetId"
      >
        <el-input
            v-if="formData.noticeType === 1"
            disabled
            model-value="全体用户"
        />
        <el-select
            v-else
            v-model="formData.targetId"
            placeholder="请选择目标"
        >
          <!-- 学院通知加载学院下拉 -->
          <el-option
              v-if="formData.noticeType === 2"
              v-for="item in deptOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
          />
          <!-- 班级通知加载班级下拉 -->
          <el-option
              v-else-if="formData.noticeType === 3"
              v-for="item in classOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
          />
          <!-- 个人通知加载用户下拉 -->
          <el-option
              v-else-if="formData.noticeType === 4"
              v-for="item in userOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
          />
        </el-select>
      </el-form-item>

      <!-- 发送者ID（当前登录用户，禁用） -->
      <el-form-item
          label="发送者"
          prop="senderId"
      >
        <el-input
            v-model="formData.senderId"
            placeholder="输入用户/不填"
        />
      </el-form-item>

      <!-- 通知状态（下拉选择） -->
      <el-form-item
          label="通知状态"
          prop="noticeStatus"
          :rules="[
          { required: true, message: '请选择通知状态', trigger: 'change' }
        ]"
      >
        <el-select
            v-model="formData.noticeStatus"
            placeholder="请选择通知状态"
        >
          <el-option
              v-for="item in noticeStatusOptions"
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
import type { FormInstance } from 'element-plus';
import request from "../../../../request";
import {copyChangeKey, copySameKey} from "../../../../utils/commonUtil";
import {noticeStatusOptions, noticeTypeOptions} from "../../../../utils/globalOptionsUtil";
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

// 下拉选项数据源（实际接口加载）
const deptOptions = ref([]); // 学院列表
const classOptions = ref([]); // 班级列表
const userOptions = ref([]); // 用户列表

// 页面挂载时加载下拉选项数据
onMounted(async () => {
  // 加载学院列表
  const deptRes = await request.get(`/api/admin/dept/select`);
  if (deptRes.status === 200) {
    deptOptions.value = deptRes.data
  }

  // 加载班级列表
  const classRes = await request.get(`/api/admin/class/select`);
  if (classRes.status === 200) {
    classOptions.value = classRes.data
  }

  // 加载用户列表
  const userRes = await request.get(`/api/admin/user/select`);
  if (userRes.status === 200) {
    userOptions.value = userRes.data
  }
});

// 通知表单数据（完全匹配AdminSysNoticeDTO）
const formData = reactive({
  id: undefined,                // 主键ID（编辑/删除/返回时用）
  noticeTitle: '',              // 通知标题
  noticeContent: '',            // 通知内容
  noticeType: undefined,        // 通知类型：1=系统 2=学院 3=班级 4=个人
  targetId: undefined,          // 目标ID（用户/学院/班级ID）
  senderId: undefined,          // 发送者ID（当前登录用户）
  noticeStatus: 1,              // 状态：0=无效 1=有效（默认有效）
  createAt: undefined,          // 创建时间（后端生成）
  updateAt: undefined           // 更新时间（后端生成）
});

// 初始表单数据（用于重置）
const initialFormData = reactive({...formData});

// 重置表单
const resetForm=()=>{
  formRef.value?.clearValidate()
  Object.assign(formData, initialFormData);
};

// 通知类型改变时的处理函数
const handleNoticeTypeChange = () => {
  // 切换类型清空目标选择
  formData.targetId = undefined;
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
.sys-notice-form {
  padding: 20px 0;
}

.el-button-footer {
  text-align: right;
}
</style>