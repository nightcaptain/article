package com.sjdf.article.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PageVo {
    private Object item;
    private Integer page;
}