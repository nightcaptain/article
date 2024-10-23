package com.sjdf.article.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class Round {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private int groupNum;
    private String beginTime;
    private String endTime;
    private String timeMillis;
    private String log;
}