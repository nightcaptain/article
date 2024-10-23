package com.sjdf.article.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sjdf.article.entity.Article;
import com.sjdf.article.param.ArticleParam;
import com.sjdf.article.vo.TypeCountVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleDao extends BaseMapper<Article>{
    List<Article> getArticlesByInfo(@Param("param") ArticleParam articleParam);

    List<Article> getArticlesByKeyword(@Param("keyword")String keyword,@Param("type")int type);

    @Select("select distinct ky from article")
    List<String> getDistinctKY();

    List<Integer> getRandIds(@Param("limit") int limit,@Param("uid")Integer uid);

    List<Article> getRandomsArt();

    @Select("select count(id) as cou,type from article group by type")
    List<TypeCountVo> getTypeCount();

    @Select("select count(id) as cou,paper_type as type from article group by paper_type")
    List<TypeCountVo> getTypeCount2();

    @Select("SELECT count(id) as cou, date as type FROM `article` group by date")
    List<TypeCountVo> getTypeCount3();

    @Select("select * from article order by download_times desc,refer_times desc limit 7 ")
    List<Article> getTop7();
}