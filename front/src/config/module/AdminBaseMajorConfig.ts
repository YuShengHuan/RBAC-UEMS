export const tableDataConfig=[
    {
        label:'专业编号',
        prop:'id'
    },
    {
        label:'专业编码',
        prop:'majorCode',
    },
    {
        label:'分院名称',
        prop:'deptName',
    },
    {
        label:'专业名称',
        prop:'majorName',
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
        label:'专业编码',
        prop:'majorCode'
    },
    {
        label:'专业名称',
        prop:'majorName'
    }
]
export const importFormConfig=[
    {
        label:'专业名称',
        prop:'majorName'
    },
    {
        label:'分院名称',
        prop:'deptName'
    }
]
export const baseApi= "/api/admin/major"