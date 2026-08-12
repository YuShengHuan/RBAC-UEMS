/**
 * 实验报告表格配置
 */
import {findLabelByValue} from "../../utils/commonUtil";
import {
  attachmentTypeOptions,
  userTypeOptions
} from "../../utils/globalOptionsUtil";

export const tableDataConfig = [
  {
    label: '报告编号',
    prop: 'id'
  },
  {
    label: '学期',
    prop: 'semester'
  },
  {
    label: '课程名称',
    prop: 'courseName'
  },
  {
    label: '项目名称',
    prop: 'projectName'
  },
  {
    label: '班级名称',
    prop: 'className'
  },
  {
    label: '用户名',
    prop: "uploadUserAccount"
  },
  {
    label: '上传用户',
    prop: 'uploadRealName'
  },
  {
    label:'用户类型',
    prop:'uploadUserType',
    formatter: (row) =>{
      return findLabelByValue(userTypeOptions,row.uploadUserType) || '未知'
    }
  },
  {
    label: '文件路径',
    prop: 'filePath'
  },
  {
    label: '附件类型',
    prop: 'attachmentType',
    formatter: (row) => {
      return findLabelByValue(attachmentTypeOptions,row.attachmentType)
    }
  },
  {
    label: '备注',
    prop: 'remark'
  },
  {
    label: '创建时间',
    prop: 'createAt'
  },
  {
    label: '更新时间',
    prop: 'updateAt'
  }
];
/**
 * 实验报告查询表单配置
 */
export const filterFormConfig = [
  {
    label: '学期',
    prop: 'semester'
  },
  {
    label: '课程名称',
    prop: 'courseName'
  },
  {
    label: '项目名称',
    prop: 'projectName'
  },
  {
    label: '班级名称',
    prop: 'className'
  },
  {
    label: '上传用户',
    prop: 'uploadRealName'
  },
  {
    label: '附件类型',
    prop: 'attachmentType',
    options: attachmentTypeOptions
  },
  {
    label:'用户类型',
    prop:'uploadUserType',
    options:userTypeOptions
  }
];

export const tableDataFrontTemplateConfig = [
  {
    label: '课程名称',
    prop: 'courseName'
  },
  {
    label: '项目名称',
    prop: 'projectName'
  },
  {
    label: '班级名称',
    prop: 'className'
  },
  {
    label: '用户名',
    prop: "uploadUserAccount"
  },
  {
    label: '上传用户',
    prop: 'uploadRealName'
  },
  {
    label:'用户类型',
    prop:'uploadUserType',
    formatter: (row) =>{
      return findLabelByValue(userTypeOptions,row.uploadUserType) || '未知'
    }
  },
  {
    label: '文件路径',
    prop: 'filePath'
  },
  {
    label: '附件类型',
    prop: 'attachmentType',
    formatter: (row) => {
      return findLabelByValue(attachmentTypeOptions,row.attachmentType)
    }
  },
  {
    label: '备注',
    prop: 'remark'
  },
  {
    label: '创建时间',
    prop: 'createAt'
  },
  {
    label: '更新时间',
    prop: 'updateAt'
  }
];
export const tableDataSubmitConfig = [
  {
    label: '课程名称',
    prop: 'courseName'
  },
  {
    label: '项目名称',
    prop: 'projectName'
  },
  {
    label: '工号',
    prop: 'reviewUserAccount'
  },
  {
    label: '教师名字',
    prop: 'reviewRealName'
  },
  {
    label: '班级名称',
    prop: 'className'
  },
  {
    label: '学号',
    prop: 'uploadUserAccount'
  },
  {
    label: '学生名字',
    prop: 'uploadRealName'
  }
];
/**
 * 实验报告API基础路径
 */
export const baseApi = "/api/admin/report";