package com.sjdf.article;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@SpringBootApplication
@MapperScan("com.sjdf.article.dao")
public class ArticleApplication{

    public static void main(String[] args) {
        SpringApplication.run(ArticleApplication.class, args);
    }

}
