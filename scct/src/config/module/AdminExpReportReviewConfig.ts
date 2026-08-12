import {findLabelByValue} from "../../utils/commonUtil";
import {userTypeOptions} from "../../utils/globalOptionsUtil";

/**
 * 实验报告批改表格配置
 */
export const tableDataConfig = [
  {
    label: '批改编号',
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
    prop: 'uploadUserAccount'
  },
  {
    label: '上传用户',
    prop: 'uploadRealName'
  },
  {
    label: '用户类型',
    prop: 'uploadUserType',
    formatter: (row) =>{
      return findLabelByValue(userTypeOptions,row.uploadUserType) || '未知'
    },
  },
  {
    label: '用户名',
    prop: 'reviewUserAccount'
  },
  {
    label: '批改用户',
    prop: 'reviewRealName'
  },
  {
    label: '用户类型',
    prop: 'reviewUserType',
    formatter: (row) =>{
      return findLabelByValue(userTypeOptions,row.reviewUserType) || '未知'
    },
  },
  {
    label: '成绩',
    prop: 'score'
  },
  {
    label: '批阅意见',
    prop: 'reviewComment'
  },
  {
    label: '查重率(%)',
    prop: 'plagiarismRate',
    formatter: (row) => {
      // 查重率保留2位小数，拼接百分号显示
      return row.plagiarismRate ? (row.plagiarismRate*100).toFixed(2) + '%' : '0.00%';
    }
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
 * 实验报告批改查询表单配置
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
    label: '批改用户',
    prop: 'reviewRealName'
  }
];
export const tableDataReviewConfig = [
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
 * 实验报告批改API基础路径
 */
export const baseApi = "/api/admin/report-review";