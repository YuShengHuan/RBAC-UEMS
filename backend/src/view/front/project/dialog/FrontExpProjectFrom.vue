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
      <!-- 实验编码 -->
      <el-form-item
          label="实验编码"
          prop="projectCode"
      >
        <el-input
            v-model="formData.projectCode"
            placeholder="请输入实验编码"
        />
      </el-form-item>

      <!-- 实验名称 -->
      <el-form-item
          label="实验名称"
          prop="projectName"
          :rules="[
          { required: true, message: '请输入实验名称', trigger: 'blur' },
          { min: 1, max: 100, message: '实验名称长度在 1-100 个字符之间', trigger: 'blur' }
        ]"
      >
        <el-input
            v-model="formData.projectName"
            placeholder="请输入实验名称"
        />
      </el-form-item>

      <!-- 教学课程 -->
      <el-form-item
          label="教学课程"
          prop="teachingCoreId"
          :rules="[
          { required: true, message: '请选择教学课程', trigger: 'change' }
        ]"
      >
        <el-select
            v-model="formData.teachingCoreId"
            placeholder="请选择教学课程"
            clearable
        >
          <el-option
              v-for="item in teachingCoreOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
          />
        </el-select>
      </el-form-item>

      <el-form-item label="授课周次" style="width: 100%;"
                    :rules="[
          { required: true, message: '请选择课程安排', trigger: 'change' }
        ]"
      >
        <el-form-item
            prop="projectWeekStart"
            :rules="[
                { required: true, message: '请输入开始周', trigger: 'blur' },
                { type: 'number', min: 1, max: 20, message: '周次范围1-20', trigger: 'blur' }
              ]"
            style="width: 48%"
        >
          <el-input
              v-model.number="formData.projectWeekStart"
              type="number"
              min="1"
              placeholder="开始周（如：5）"

          />
        </el-form-item>
        <el-form-item
            prop="projectWeekEnd"
            :rules="[
                { required: true, message: '请输入结束周', trigger: 'blur' },
                { type: 'number', min: formData.projectWeekStart || 1, max: 20, message: '结束周≥开始周', trigger: 'blur' }
              ]"
            style="width: 48%; margin-left: 4%;"
        >
          <el-input
              v-model.number="formData.projectWeekEnd"
              type="number"
              min="1"
              placeholder="结束周（如：6）"

          />
        </el-form-item>
      </el-form-item>

      <!-- 周学时数 -->
      <el-form-item
          label="周学时数"
          prop="weeklyHours"
          :rules="[
          { required: true, message: '请输入周学时数', trigger: 'blur' },
          { type: 'number', min: 1, message: '周学时数至少为1', trigger: 'blur' }
        ]"
      >
        <el-input
            v-model.number="formData.weeklyHours"
            type="number"
            placeholder="请输入周学时数"
        />
      </el-form-item>

      <!-- 计划学时数 -->
      <el-form-item
          label="计划学时数"
          prop="planHours"
          :rules="[
          { required: true, message: '请输入计划学时数', trigger: 'blur' },
          { type: 'number', min: 1, message: '计划学时数至少为1', trigger: 'blur' }
        ]"
      >
        <el-input
            v-model.number="formData.planHours"
            type="number"
            placeholder="请输入计划学时数"
        />
      </el-form-item>

      <!-- 实际学时数：创建时非必填，更新时必填 -->
      <el-form-item
          label="实际学时数"
          prop="actualHours"
          :rules="[
          { required: true, message: '请输入实际学时数', trigger: 'blur' },
          { type: 'number', min: 0, message: '实际学时数不能为负数', trigger: 'blur' }
        ]"
      >
        <el-input
            v-model.number="formData.actualHours"
            type="number"
            placeholder="请输入实际学时数"
        />
      </el-form-item>

      <!-- 实验类别 -->
      <el-form-item
          label="实验类别"
          prop="expCategory"
          :rules="[
          { required: true, message: '请选择实验类别', trigger: 'change' }
        ]"
      >
        <el-select
            v-model="formData.expCategory"
            placeholder="请选择实验类别"
            clearable
        >
          <el-option
              v-for="item in expCategoryOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
          />
        </el-select>
      </el-form-item>

      <!-- 实验类型 -->
      <el-form-item
          label="实验类型"
          prop="expType"
          :rules="[
          { required: true, message: '请选择实验类型', trigger: 'change' }
        ]"
      >
        <el-select
            v-model="formData.expType"
            placeholder="请选择实验类型"
            clearable
        >
          <el-option
              v-for="item in expTypeOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
          />
        </el-select>
      </el-form-item>

      <!-- 所属学科 -->
      <el-form-item
          label="所属学科"
          prop="subject"
          :rules="[
          { required: true, message: '请选择所属学科', trigger: 'change' }
        ]"
      >
        <el-select
            v-model="formData.subject"
            placeholder="请选择所属学科"
            clearable
        >
          <el-option
              v-for="item in subjectOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
          />
        </el-select>
      </el-form-item>

      <!-- 实验分组人数 -->
      <el-form-item
          label="分组人数"
          prop="groupNum"
      >
        <el-input
            v-model.number="formData.groupNum"
            type="number"
            placeholder="请输入实验分组人数"
        />
      </el-form-item>

      <!-- 实验者类别 -->
      <el-form-item
          label="实验者类别"
          prop="expPersonType"
          :rules="[
          { required: true, message: '请选择实验者类别', trigger: 'change' }
        ]"
      >
        <el-select
            v-model="formData.expPersonType"
            placeholder="请选择实验者类别"
            clearable
        >
          <el-option
              v-for="item in expPersonTypeOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
          />
        </el-select>
      </el-form-item>

      <!-- 实验要求 -->
      <el-form-item
          label="实验要求"
          prop="expRequirement"
          :rules="[
          { required: true, message: '请选择实验要求', trigger: 'change' }
        ]"
      >
        <el-select
            v-model="formData.expRequirement"
            placeholder="请选择实验要求"
            clearable
        >
          <el-option
              v-for="item in expRequirementOptions"
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
import {expCategoryOptions,expRequirementOptions,expTypeOptions,expPersonTypeOptions,subjectOptions} from "../../../../utils/globalOptionsUtil"
import {copyChangeKey, copySameKey} from "../../../../utils/commonUtil";
import request from "../../../../request";
import {ElMessage} from "element-plus";
import {computed, defineEmits, defineProps} from "vue";
import { baseApi } from "../../../../config/module/AdminExpProjectConfig";
const emit=defineEmits(
    ['success-form']
)
// 表单DOM引用
const formRef = ref<FormInstance>();

// 课程安排下拉选项
const teachingCoreOptions = ref([]);
// 当前表单状态 (0-创建 1-更新)
const currentFormStatus = computed(() => {
  return formData.id ? 1 : 0;
});
// 弹窗显示状态
const visible = ref(false);
// 设置弹窗显示/隐藏
const setVisible =(val: boolean) => {
  visible.value = val;
};
onMounted(
    async ()=>{
      const res = await request.get(`/api/admin/teaching-core/select`);
      if (res.status === 200) {
        teachingCoreOptions.value = res.data;
      }
    }
)
// 实验项目表单数据（完全对齐ExpProject实体）
const formData = reactive({
  id: undefined,                // 项目ID
  projectCode: '',              // 实验编码
  projectName: '',              // 实验名称
  teachingCoreId: '',               // 课程安排ID（字符串接收，提交转数字）
  projectWeekStart: undefined,  // 授课周次开始（实体Integer）
  projectWeekEnd: undefined,    // 授课周次结束（实体Integer）
  weeklyHours: undefined,       // 周次学时数（实体Integer，移除默认0）
  planHours: undefined,         // 计划学时数（实体Integer，移除默认0）
  actualHours: undefined,       // 实际学时数（实体Integer，移除默认0）
  expCategory: '',              // 实验类别（1-基础/2-专业基础/3-专业/4-其他）
  expType: '',                  // 实验类型（1-演示性/2-验证性/3-综合性/4-设计研究）
  subject: '',                  // 实验所属学科（1-电子信息类/2-电子商务类/3-财政学类/4-其他）
  groupNum: 1,          // 实验分组人数（实体Integer，移除默认1）
  expPersonType: '',            // 实验者类别（1-本科生/2-专科生）
  expRequirement: '',           // 实验要求（1-选修/2-必修/3-其他）
  remark: ''                    // 备注
});
const initialFormData=reactive({...formData})
// 重置表单
const resetForm = () => {
  formRef.value?.clearValidate();
  Object.assign(formData, { ...initialFormData });
};

const initSourceData=reactive({})
// 初始化表单数据（从父组件传入，适配实体字段）
const initFormData=(source)=>{
  copySameKey(formData,source)
  Object.assign(initSourceData, { ...source });
}

// 保存表单数据（提交前转换字段类型对齐实体）
const handleSave = async () => {
  try {
    // 触发表单校验
    await formRef.value.validate();
    // 校验通过，提交数据
    console.log('表单数据提交:', formData);

    let res=null
    if(currentFormStatus.value==0){
      res=await request.post(`${baseApi}/create`,formData)
    }else if(currentFormStatus.value==1){
      let changeFormData=copyChangeKey(formData,initSourceData)
      if(changeFormData!=null){
        res=await request.put(`${baseApi}/update`,changeFormData)
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