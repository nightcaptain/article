package com.sjdf.article.entity;

import lombok.Data;

@Data
public class Record {
    private int id;
    private int userId;
    private int bookId;
    private String operdate;
    private int score;
    private String comments;
}