package com.sjdf.article.param;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.List;

@Data
@AllArgsConstructor
public class ArticleParam {
    /**
     * 题名
     * 关键词
     * 摘要
     * 作者
     * 来源
     */
    private String keywords;

    /**
     * 综合
     * 期刊
     * 硕博
     * 会议
     * 报纸
     */
    private Integer types;

    private Integer startYear;

    private Integer endYear;

    private Integer page;
}