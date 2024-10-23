package com.sjdf.article.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sjdf.article.dao.ArticleDao;
import com.sjdf.article.entity.Article;
import com.sjdf.article.param.ArticleParam;
import com.sjdf.article.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleDao,Article> implements ArticleService {

    @Autowired
    ArticleDao articleDao;

    @Override
    public List<Article> getArticleByInfo(ArticleParam articleParam) {
        return articleDao.getArticlesByInfo(articleParam);
    }

    @Override
    public List<Article> getArticlesByKeyword(String keyword, int type) {
        return articleDao.getArticlesByKeyword(keyword,type);
    }

    @Override
    public List<Article> getRandomsArt() {
        return articleDao.getRandomsArt();
    }
}