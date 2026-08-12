import {courseTypeOptions} from '../../utils/globalOptionsUtil'
import {findLabelByValue, findValueByLabel} from "../../utils/commonUtil";
export const tableDataConfig=[
    {
        label:'课程编号',
        prop:'id'
    },
    {
        label:'课程编码',
        prop:'courseCode',
    },
    {
        label:'课程名称',
        prop:'courseName'
    },
    {
        label:'课程类型',
        prop:'courseType',
        formatter: (row) =>{
            return findLabelByValue(courseTypeOptions,row.courseType)
        },
    },
    {
        label:'分院名称',
        prop:'deptName',
    },
    {
        label:'创建时间',
        prop:'createAt'
    },
    {
        label:'更新时间',
        prop:'updateAt'
    }
]
export const filterFormConfig=[
    {
        label:'课程编码',
        prop:'courseCode',
    },
    {
        label:'课程名称',
        prop:'courseName'
    },
    {
        label:'课程类型',
        prop:'courseType',
        options:courseTypeOptions
    },
    {
        label:'分院名称',
        prop:'deptName',
    }
]
export const importFormConfig=[
    {
        label:'课程名称',
        prop:'courseName'
    },
    {
        label:'课程类型',
        prop:'courseType',
        formatter: (courseType) =>{
            return findValueByLabel(courseTypeOptions,courseType)
        },
    }
]
export const baseApi= "/api/admin/course"