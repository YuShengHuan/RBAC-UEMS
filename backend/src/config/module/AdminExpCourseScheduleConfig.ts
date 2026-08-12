import {isReportOptions, weekDayOptions} from '../../utils/globalOptionsUtil'
import {findLabelByValue, findValueByLabel} from "../../utils/commonUtil";

export const tableDataConfig = [
    {
        label: '编号',
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
        label:'工号',
        prop: 'userAccount'
    },
    {
        label: '授课教师',
        prop: 'realName'
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
        label: '周次范围',
        prop: 'weekRange'
    },
    {
        label: '星期',
        prop: 'weekDay',
        formatter: (row) => {
            return findLabelByValue(weekDayOptions,row.weekDay)
        },
    },
    {
        label: '节次',
        prop: 'classPeriod'
    },
    {
        label: '是否有报告',
        prop: 'isReport',
        formatter: (row) => {
            return findLabelByValue(isReportOptions,row.isReport)
        },
    },
    {
        label: '总学时',
        prop: 'classHours'
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
 * 实验课程安排查询表单配置
 */
export const filterFormConfig = [
    {
        label: '课程名称',
        prop: 'courseName',
    },
    {
        label: '班级名称',
        prop: 'className',
    },
    {
        label: '授课教师姓名',
        prop: 'realName',
    },
    {
        label: '实验地点',
        prop: 'labLocation',
    },
    {
        label: '是否提交报告',
        prop: 'isReport',
        options: isReportOptions
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
        label: '课程名称',
        prop: 'courseName'
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
        label: '班级名称',
        prop: 'className'
    },
    {
        label: '实验地点',
        prop: 'labLocation'
    },
    {
        label: '周次范围',
        prop: 'weekRange'
    },
    {
        label: '星期',
        prop: 'weekDay',
        formatter: (weekDay) => {
            return findValueByLabel(weekDayOptions,weekDay)
        },
    },
    {
        label: '节次',
        prop: 'classPeriod'
    },
    {
        label: '是否有报告',
        prop: 'isReport',
        formatter: (isReport) => {
            return findValueByLabel(isReportOptions,isReport)
        },
    },
    {
        label: '总学时',
        prop: 'classHours'
    }
]
export const tableDataFrontConfig = [
    {
        label: '学期',
        prop: 'semester'
    },
    {
        label: '课程名称',
        prop: 'courseName'
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
        label: '班级名称',
        prop: 'className'
    },
    {
        label: '实验地点',
        prop: 'labLocation'
    },
    {
        label: '周次范围',
        prop: 'weekRange'
    },
    {
        label: '星期',
        prop: 'weekDay',
        formatter: (row) => {
            return findLabelByValue(weekDayOptions,row.weekDay)
        },
    },
    {
        label: '节次',
        prop: 'classPeriod'
    },
    {
        label: '是否有报告',
        prop: 'isReport',
        formatter: (row) => {
            return findLabelByValue(isReportOptions,row.isReport)
        },
    },
    {
        label: '总学时',
        prop: 'classHours'
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
 * 实验课程安排API基础路径
 */
export const baseApi = "/api/admin/course-schedule";