package com.sjdf.article.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("scoreresultcenter")
public class ScoreResultCenter {
	@TableId(type = IdType.AUTO)
	private Integer id;
	private double age;
	private double major;
	private double hobby;
	private double lastBookType;
	private int roundId;
	private int doneCount;
}
