import {mimeToExt} from "./MimeUtil";
import request from "../request";

export const wait=(ms)=>{
    return new Promise(resolve => setTimeout(resolve, ms));
}
//下载文件
export const downloadFile=async (URI,downloadFileName)=>{
    try {
        if(URI){
            const response=await request.get(URI,{
                responseType: 'blob', // 必须设置，否则无法正确解析为文件
                headers: {
                    'Accept': 'application/octet-stream' // 仅需这个头
                }
            })
            console.log(URI)
            // 3. 获取MIME类型（处理可能的空格或参数，如"application/pdf; charset=utf-8"）
            const contentType = response.headers['content-type']?.split(';')[0]?.trim();
            if (!contentType) {
                throw new Error('无法获取文件类型');
            }

            // 4. 根据MIME类型映射文件后缀（默认用.bin）
            const ext = mimeToExt[contentType] || '.bin';

            // 5. 处理文件名（清理特殊字符 + 拼接后缀）
            const safeBaseName = downloadFileName.replace(/[\\/:*?"<>|]/g, '_'); // 清理特殊字符
            const fullFileName = safeBaseName + ext; // 完整文件名（含后缀）

            const blob = new Blob([response.data], {
                type: response.headers['content-type'] || 'application/octet-stream'
            });
            const tempUrl = URL.createObjectURL(blob);

            const a = document.createElement('a');
            a.href = tempUrl;
            //设置文件名
            a.download = fullFileName;
            document.body.appendChild(a);
            a.click();

            // 5. 清理资源
            window.URL.revokeObjectURL(tempUrl);
            document.body.removeChild(a);
        }
    }catch (e) {
        console.log("下载异常"+e)
    }

}
//复制相同字段
export const copySameKey=(target,source)=>{
    Object.keys(target).forEach(
        item=>{
            let sameKey=Object.keys(source).find(s=>s===item)
            if(sameKey){
                target[sameKey]=source[sameKey]
            }
        }
    )
}
// 检查是否有更新,默认是排除id
export const copyChangeKey = (checkTarget, source,exclude='id') => {
    let copyTarget = {};
    // 遍历checkTarget的所有键
    Object.keys(checkTarget).forEach(item => {
        if (String(checkTarget[item])?.length>0&& String(checkTarget[item]) !== String(source[item])) {
            copyTarget[item] = checkTarget[item];
        }
    });
    if(Object.keys(copyTarget).length === 0){
        return null
    }else{
        if(Object.keys(source).includes(exclude)){
             copyTarget[exclude]=source[exclude]
             return copyTarget
        }
        return copyTarget;
    }
};
export const findLabelByValue=(option,value)=>{
    // 空值/空数组直接返回空字符串
    if (!option || option.length === 0 || value === undefined || value === null) {
        return '';
    }
    //  查找匹配项（考虑value可能是数字/字符串，统一转字符串比较）
    const matchItem = option.find(item => String(item.value).trim() === String(value).trim());
    // 返回label
    return matchItem ? matchItem.label : '';
}

export const findValueByLabel=(option,label)=>{
    // 空值/空数组直接返回空字符串
    if (!option || option.length === 0 || label === undefined || label === null) {
        return '';
    }
    //  查找匹配项（考虑value可能是数字/字符串，统一转字符串比较）
    const matchItem = option.find(item => String(item.label).trim() === String(label).trim());
    // 返回label
    return matchItem ? matchItem.value : '';
}

// 格式化日期时间（处理后端可能返回的LocalDateTime字符串）
export const formatDateTime = (dateTime?: string | LocalDateTime) => {
    if (!dateTime) return '-'
    // 假设后端返回的是ISO格式字符串（如：2023-10-23T16:52:11）
    const date = new Date(dateTime.toString())
    return date.toLocaleString('zh-CN', {
        year: 'numeric',
        month: '2-digit',
        day: '2-digit',
        hour: '2-digit',
        minute: '2-digit',
        second: '2-digit'
    }).replace(',', ' ')
}