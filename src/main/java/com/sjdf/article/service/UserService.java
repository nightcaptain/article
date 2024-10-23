package com.sjdf.article.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sjdf.article.entity.Article;
import com.sjdf.article.entity.User;
import com.sjdf.article.param.ArticleParam;

import java.util.List;

public interface UserService extends IService<User>{
    void calGroup();
    void submitContent(Integer score,String content,Integer id,Integer uid);
    List<Article> getTop20(Integer uid);
}