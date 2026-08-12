/**
 * 实验项目表格配置
 */
import {findLabelByValue, findValueByLabel} from "../../utils/commonUtil";
import {
    expCategoryOptions,
    expPersonTypeOptions,
    expRequirementOptions,
    expTypeOptions,
    subjectOptions
} from "../../utils/globalOptionsUtil";

export const tableDataConfig = [

    {
        label: '项目编号',
        prop: 'id'
    },
    {
        label: '学期',
        prop: 'semester'
    },
    {
        label: '项目编码',
        prop: 'projectCode'
    },
    {
        label: '项目名称',
        prop: 'projectName'
    },
    {
        label:'工号',
        prop: 'userAccount'
    },
    {
        label: '授课教师',
        prop: 'realName'
    },
    {
        label: '课程名称',
        prop: 'courseName'
    },
    {
        label: '班级名称',
        prop: 'className'
    },

    {
        label: '授课周次',
        prop: 'projectWeekRange'
    },
    {
        label: '周学时数',
        prop: 'weeklyHours'
    },
    {
        label: '计划学时数',
        prop: 'planHours'
    },
    {
        label: '实际学时数',
        prop: 'actualHours'
    },
    {
        label: '实验类别',
        prop: 'expCategory',
        formatter: (row) => {
            return findLabelByValue(expCategoryOptions,row.expCategory);
        },
    },
    {
        label: '实验类型',
        prop: 'expType',
        formatter: (row) => {
            return findLabelByValue(expTypeOptions,row.expType)
        },
    },
    {
        label: '所属学科',
        prop: 'subject',
        formatter: (row) => {
            return findLabelByValue(subjectOptions,row.subject)
        },
    },
    {
        label: '分组人数',
        prop: 'groupNum'
    },
    {
        label: '实验者类别',
        prop: 'expPersonType',
        formatter: (row) => {
            return findLabelByValue(expTypeOptions,row.expPersonType)
        },
    },
    {
        label: '实验要求',
        prop: 'expRequirement',
        formatter: (row) => {
            return findLabelByValue(expRequirementOptions,row.expRequirement)
        },
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
 * 实验项目查询表单配置
 */
export const filterFormConfig = [
    {
        label: '项目编码',
        prop: 'projectCode',
    },
    {
        label: '项目名称',
        prop: 'projectName'
    },
    {
        label: '课程名称',
        prop: 'courseName'
    },
    {
        label: '学期',
        prop: 'semester',
    }
];
export const importFormConfig=[
    {
        label: '学期',
        prop: 'semester'
    },
    {
        label: '项目编码',
        prop: 'projectCode'
    },
    {
        label: '项目名称',
        prop: 'projectName'
    },
    {
        label:'工号',
        prop: 'userAccount'
    },
    {
        label: '授课教师',
        prop: 'realName'
    },
    {
        label: '课程名称',
        prop: 'courseName'
    },
    {
        label: '班级名称',
        prop: 'className'
    },
    {
        label: '实验地点',
        prop: 'labLocation'
    },

    {
        label: '授课周次',
        prop: 'projectWeekRange'
    },
    {
        label: '周学时数',
        prop: 'weeklyHours'
    },
    {
        label: '计划学时数',
        prop: 'planHours'
    },
    {
        label: '实际学时数',
        prop: 'actualHours'
    },
    {
        label: '实验类别',
        prop: 'expCategory',
        formatter: (expCategory) => {
            return findValueByLabel(expCategoryOptions,expCategory);
        },
    },
    {
        label: '实验类型',
        prop: 'expType',
        formatter: (expType) => {
            return findValueByLabel(expTypeOptions,expType)
        },
    },
    {
        label: '所属学科',
        prop: 'subject',
        formatter: (subject) => {
            return findValueByLabel(subjectOptions,subject)
        },
    },
    {
        label: '分组人数',
        prop: 'groupNum'
    },
    {
        label: '实验者类别',
        prop: 'expPersonType',
        formatter: (expPersonType) => {
            return findValueByLabel(expPersonTypeOptions,expPersonType)
        },
    },
    {
        label: '实验要求',
        prop: 'expRequirement',
        formatter: (expRequirement) => {
            return findValueByLabel(expRequirementOptions,expRequirement)
        },
    },
    {
        label: '备注',
        prop: 'remark'
    }
]
export const tableDataFrontConfig = [
    {
        label: '学期',
        prop: 'semester'
    },
    {
        label: '项目编码',
        prop: 'projectCode'
    },
    {
        label: '项目名称',
        prop: 'projectName'
    },
    {
        label:'工号',
        prop: 'userAccount'
    },
    {
        label: '授课教师',
        prop: 'realName'
    },
    {
        label: '课程名称',
        prop: 'courseName'
    },
    {
        label: '班级名称',
        prop: 'className'
    },

    {
        label: '授课周次',
        prop: 'projectWeekRange'
    },
    {
        label: '周学时数',
        prop: 'weeklyHours'
    },
    {
        label: '计划学时数',
        prop: 'planHours'
    },
    {
        label: '实际学时数',
        prop: 'actualHours'
    },
    {
        label: '实验类别',
        prop: 'expCategory',
        formatter: (row) => {
            return findLabelByValue(expCategoryOptions,row.expCategory);
        },
    },
    {
        label: '实验类型',
        prop: 'expType',
        formatter: (row) => {
            return findLabelByValue(expTypeOptions,row.expType)
        },
    },
    {
        label: '所属学科',
        prop: 'subject',
        formatter: (row) => {
            return findLabelByValue(subjectOptions,row.subject)
        },
    },
    {
        label: '分组人数',
        prop: 'groupNum'
    },
    {
        label: '实验者类别',
        prop: 'expPersonType',
        formatter: (row) => {
            return findLabelByValue(expTypeOptions,row.expPersonType)
        },
    },
    {
        label: '实验要求',
        prop: 'expRequirement',
        formatter: (row) => {
            return findLabelByValue(expRequirementOptions,row.expRequirement)
        },
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
 * 实验项目API基础路径
 */
export const baseApi = "/api/admin/project";