import {findLabelByValue, findValueByLabel} from "../../utils/commonUtil";
import {permStatusOptions, permTypOptions} from "../../utils/globalOptionsUtil";

export const tableDataConfig=[
  {
    label:'权限编号',
    prop:'id'
  },
  {
    label:'父权限编码',
    prop:'parentCode',
  },
  {
    label:'权限编码',
    prop:'permCode',
  },
  {
    label:'权限分组',
    prop:'permGroup'
  },
  {
    label:'权限名称',
    prop:'permName',
  },
  {
    label:'权限类型',
    prop:'permType',
    formatter: (row) =>{
      return findLabelByValue(permTypOptions,row.permType)
    },
  },
  {
    label:'请求路径',
    prop:'requestUri'
  },
  {
    label:'状态',
    prop:'permStatus',
    formatter: (row) =>{
      return findLabelByValue(permStatusOptions,row.permStatus)
    },
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
export const tableDataTreeConfig=[
  {
    label:'权限编码',
    prop:'permCode',
  },
  {
    label:'权限名称',
    prop:'permName',
  },
  {
    label:'权限类型',
    prop:'permType',
    formatter: (row) =>{
      return findLabelByValue(permTypOptions,row.permType)
    },
  },
  {
    label:'请求路径',
    prop:'requestUri'
  }
]
export const filterFormConfig=[
  {
    label:'权限编码',
    prop:'permCode'
  },
  {
    label:'权限名称',
    prop:'permName'
  },
  {
    label:'权限类型',
    prop:'permType',
    options:permTypOptions
  }
]
export const importFormConfig=[
  {
    label:'父权限编码',
    prop:'parentCode',
  },
  {
    label:'权限编码',
    prop:'permCode',
  },
  {
    label:'权限分组',
    prop:'permGroup'
  },
  {
    label:'权限名称',
    prop:'permName',
  },
  {
    label:'权限类型',
    prop:'permType',
    formatter: (permType) => {
      return findValueByLabel(permTypOptions, permType)
    }
  },
  {
    label:'请求路径',
    prop:'requestUri'
  },
  {
    label:'状态',
    prop:'permStatus',
    formatter: (permStatus) =>{
      return findValueByLabel(permStatusOptions,permStatus)
    },
  }
]
export const baseApi= "/api/admin/perm"