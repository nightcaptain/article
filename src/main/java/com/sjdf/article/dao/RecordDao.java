package com.sjdf.article.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sjdf.article.entity.Record;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecordDao extends BaseMapper<Record>{
    List<Integer> getArticleId(@Param("uid") Integer uid);
}