package com.ru.app.common.dto;

import lombok.Data;

import java.time.LocalDateTime;


@Data
public class CommonSysDictDTO {
    private Long id;

    private String dictGroup;

    private String dictKey;

    private String dictValue;

    private String remark;

    private LocalDateTime createAt;

    private LocalDateTime updateAt;
}