package com.sjdf.article.controller;

import com.sjdf.article.dao.ArticleDao;
import com.sjdf.article.vo.TypeCountVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@RestController
public class RestApiController {

    @Autowired
    ArticleDao articleDao;

    @RequestMapping("/typeCount")
    public Object typeCount(){
        List<TypeCountVo> typeCount = articleDao.getTypeCount();
        List<String> types = new ArrayList<>();
        List<Integer> cous = new ArrayList<>();
        String str = "{\"values\":[";
        for(TypeCountVo item: typeCount){
            types.add(item.getType());
            cous.add(item.getCou());
            str+="{\"name\":\""+item.getType()+"\",\"value\":"+item.getCou()+"},";
        }

        Object[] objects = types.toArray();
        String[] strs = new String[objects.length];
        for(int i=0;i<objects.length;i++){
            strs[i] = "\""+String.valueOf(objects[i])+"\"";
        }
        str = str.substring(0, str.length() - 1);
        return str+"],\"name\":"+Arrays.toString(strs)+",\"data\":"+Arrays.toString(cous.toArray())+"}";
    }

    @RequestMapping("/typeCount2")
    public Object typeCount2(){
        List<TypeCountVo> typeCount = articleDao.getTypeCount2();
        List<String> types = new ArrayList<>();
        List<Integer> cous = new ArrayList<>();
        String str = "{\"values\":[";
        for(TypeCountVo item: typeCount){
            types.add(item.getType());
            cous.add(item.getCou());
            str+="{\"name\":\""+item.getType()+"\",\"value\":"+item.getCou()+"},";
        }

        Object[] objects = types.toArray();
        String[] strs = new String[objects.length];
        for(int i=0;i<objects.length;i++){
            strs[i] = "\""+String.valueOf(objects[i])+"\"";
        }
        str = str.substring(0, str.length() - 1);
        return str+"],\"name\":"+Arrays.toString(strs)+",\"data\":"+Arrays.toString(cous.toArray())+"}";
    }

    @RequestMapping("/typeCount3")
    public Object typeCount3(){
        List<TypeCountVo> typeCount = articleDao.getTypeCount3();
        List<String> types = new ArrayList<>();
        List<Integer> cous = new ArrayList<>();
        for(TypeCountVo item: typeCount){
            types.add(item.getType());
            cous.add(item.getCou());
        }

        Object[] objects = types.toArray();
        String[] strs = new String[objects.length];
        for(int i=0;i<objects.length;i++){
            strs[i] = "\""+String.valueOf(objects[i])+"\"";
        }
        return "{\"name\":"+Arrays.toString(strs)+",\"data\":"+Arrays.toString(cous.toArray())+"}";
    }

}