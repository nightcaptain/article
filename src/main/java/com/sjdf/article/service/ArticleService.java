package com.sjdf.article.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sjdf.article.entity.Article;
import com.sjdf.article.param.ArticleParam;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ArticleService extends IService<Article>{
    List<Article> getArticleByInfo(ArticleParam articleParam);
    List<Article> getArticlesByKeyword(String keyword,int type);
    List<Article> getRandomsArt();
}