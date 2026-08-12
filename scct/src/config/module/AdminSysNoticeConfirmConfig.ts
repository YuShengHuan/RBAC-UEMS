// 表格列配置（对应AdminSysNoticeConfirmDTO字段）
import {findLabelByValue} from "../../utils/commonUtil";
import {noticeTypeOptions} from "../../utils/globalOptionsUtil";

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
    // 类型格式化（显示中文）
    formatter: (row) => {
      return findLabelByValue(noticeTypeOptions, row.noticeType);
    }
  },
  {
    label: '用户账号',
    prop: 'userAccount'
  },
  {
    label: '真实姓名',
    prop: 'realName'
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

// 筛选表单配置（对应AdminSysNoticeConfirmQueryDTO查询字段）
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
    // 配置下拉选项（对应类型枚举）
    options: noticeTypeOptions
  },
  {
    label: '用户账号',
    prop: 'userAccount'
  },
  {
    label: '真实姓名',
    prop: 'realName'
  }
]

// 基础接口地址（通知确认管理接口前缀，匹配确认场景）
export const baseApi = "/api/admin/notice-confirm"