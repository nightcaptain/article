package com.sjdf.article.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sjdf.article.entity.Article;
import com.sjdf.article.entity.User;
import com.sjdf.article.param.ArticleParam;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserDao extends BaseMapper<User>{
    @Select("select hobby from User order by hobby")
    List<String> getHobbies();
    @Select("select major from User order by major")
    List<String> getMajors();
    @Select("select age from User order by age")
    List<Integer> getAges();
    @Select("select * from User order by uid")
    List<User> getAll();
}