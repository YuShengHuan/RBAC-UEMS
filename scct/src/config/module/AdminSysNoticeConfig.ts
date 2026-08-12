// 表格列配置（对应AdminSysNoticeDTO字段）
import {findLabelByValue} from "../../utils/commonUtil";
import {noticeStatusOptions, noticeTypeOptions} from "../../utils/globalOptionsUtil";

export const tableDataConfig = [
  {
    label: '编号',
    prop: 'id'
  },
  {
    label: '通知标题',
    prop: 'noticeTitle'
  },
  {
    label: '通知内容',
    prop: 'noticeContent'
  },
  {
    label: '通知类型',
    prop: 'noticeType',
    // 可选：添加类型格式化（显示中文）
    formatter: (row) => {
      return findLabelByValue(noticeTypeOptions, row.noticeType);
    }
  },
  {
    label: '目标名称',
    prop: 'targetName'
  },
  {
    label: '发送者名字',
    prop: 'senderName'
  },
  {
    label: '通知状态',
    prop: 'noticeStatus',
    // 可选：状态格式化（显示中文）
    formatter: (row) => {
      return findLabelByValue(noticeStatusOptions,row.noticeStatus);
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
]

// 筛选表单配置（对应AdminSysNoticeQueryDTO查询字段）
export const filterFormConfig = [
  {
    label: '通知标题',
    prop: 'noticeTitle'
  },
  {
    label: '通知内容',
    prop: 'noticeContent'
  },
  {
    label: '通知类型',
    prop: 'noticeType',
    // 可选：配置下拉选项（对应类型枚举）
    options: noticeTypeOptions
  },
  {
    label: '目标名称',
    prop: 'targetName'
  },
  {
    label: '发送者名字',
    prop: 'senderName'
  }
]

// 基础接口地址（通知管理接口前缀，根据实际业务调整）
export const baseApi = "/api/admin/notice"