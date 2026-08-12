package com.ru.app.module.base.mapper;

import com.ru.app.common.mapper.BaseMajorMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper  // 标记为 MyBatis Mapper 接口（或通过 @MapperScan 扫描包）
public interface AdminBaseMajorMapper extends BaseMajorMapper {

}
