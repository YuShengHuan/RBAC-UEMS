import {findLabelByValue, findValueByLabel} from "../../utils/commonUtil";
import {genderOptions, userStatusOptions, userTypeOptions} from "../../utils/globalOptionsUtil";

export const tableDataConfig=[
    {
        label:'用户编号',
        prop:'id'
    },
    {
        label:'用户名',
        prop:'userAccount',
    },
    {
        label:'密码',
        prop:'userPassword',
    },
    {
        label:'真实名',
        prop:'realName',
    },
    {
        label:'性别',
        prop:'gender',
        formatter: (row) =>{
            return findLabelByValue(genderOptions,row.gender)
        },
        cellStyleFn:(row) => {
            return {
                backgroundColor:row.gender==1?'rgb(1,135,252)':row.gender==0?'rgba(4,246,174,0.71)':'',
                borderRadius:'5px',
                padding:'5px',
                color:'#ffffff'
            }
        }
    },
    {
        label:'手机号码',
        prop:'phone'
    },
    {
        label:'邮箱',
        prop:'email'
    },
    {
        label:'用户类型',
        prop:'userType',
        formatter: (row) =>{
            return findLabelByValue(userTypeOptions,row.userType) || '未知'
        },
        cellStyleFn:(row) => {
            return {
                backgroundColor:
                    row.userType==2?'rgb(1,135,253)':row.userType==3?'rgb(5,237,255)':'rgb(246,125,4)',
                borderRadius:'5px',
                padding:'5px',
                color:'#ffffff'
            }
        }
    },
    {
        label:'分院名字',
        prop:'deptName'
    },
    {
        label:'班级名字',
        prop:'className'
    },
    {
        label:'用户状态',
        prop:'userStatus',
        formatter: (row) =>{
            return findLabelByValue(userStatusOptions,row.userStatus)
        },
        cellStyleFn:(row) => {
            return {
                backgroundColor:row.userStatus==1?'rgb(12,255,4)':'rgb(211,19,19)',
                borderRadius:'5px',
                padding:'5px',
                color:'#ffffff'
            }
        }
    },
    {
        label:'备注',
        prop:'remark'
    },
    {
        label:'创建时间',
        prop:'createAt'
    },
    {
        label:'更新时间',
        prop:'updateAt'
    },
]
export const filterFormConfig=[
    {
        label:'账户',
        prop:'userAccount'
    },
    {
        label:'真实名',
        prop:'realName'
    },
    {
        label:'学院',
        prop:'deptName'
    },
    {
        label:'班级',
        prop:'className'
    },
    {
        label:'用户类型',
        prop:'userType',
        options:userTypeOptions
    }
]

export const importFormConfig=[
    {
        label:'用户名',
        prop:'userAccount',
    },
    {
        label:'密码',
        prop:'userPassword',
    },
    {
        label:'真实名',
        prop:'realName',
    },
    {
        label:'用户类型',
        prop:'userType',
        formatter: (userType) =>{
            return findValueByLabel(userTypeOptions,userType)
        }
    },
    {
        label:'分院名称',
        prop:'deptName'
    },
    {
        label:'班级名称',
        prop:'className'
    }
]
export const baseApi= "/api/admin/user"