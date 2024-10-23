package com.sjdf.article.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.sjdf.article.dao.ArticleDao;
import com.sjdf.article.entity.Article;
import com.sjdf.article.entity.User;
import com.sjdf.article.param.ArticleParam;
import com.sjdf.article.service.ArticleService;
import com.sjdf.article.service.UserService;
import com.sjdf.article.vo.PageVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.function.Consumer;

@Controller
public class IndexController {

    @Autowired
    ArticleService articleService;

    @Autowired
    ArticleDao articleDao;

    @Autowired
    UserService userService;

    @RequestMapping("/")
    public Object index(HttpServletRequest req,Model model){
        User user = (User)req.getSession().getAttribute("user");
        List<Article> top20;
        if(user==null||(top20=userService.getTop20(user.getUid())).size()==0){
            top20 = articleService.getRandomsArt();
        }
        model.addAttribute("top20",top20);
        return "index";
    }

    @RequestMapping("/daping")
    public String daping(Model model,HttpServletRequest req){
        User user = (User)req.getSession().getAttribute("user");
        if(user!=null){
            List<Article> top7 = articleDao.getTop7();
            model.addAttribute("top7",top7);
            return "daping/index";
        }else{
            return "login";
        }
    }

    @RequestMapping("/logout")
    public String logout(HttpServletRequest request){
        request.getSession().removeAttribute("user");
        return "redirect:/";
    }

    @RequestMapping("/advance_search")
    public String advance_search(){
        return "advance_search";
    }

    @RequestMapping("/login")
    public String login(){
        return "login";
    }

    @RequestMapping("/register")
    public String register(){
        return "register";
    }

    @RequestMapping("/calGroup")
    @ResponseBody
    public String calGroup(){
        userService.calGroup();
        return "success";
    }

    @RequestMapping("/articleDetail/{id}")
    public String articleDetail(@PathVariable("id") Integer id,Model model,HttpServletRequest request){
        Object user = request.getSession().getAttribute("user");
        if(user!=null){
            Article byId = articleService.getById(id);
            model.addAttribute("article",byId);
            return "detail";
        }else{
            return "login";
        }
    }

    @RequestMapping("/registerUser")
    @ResponseBody
    public String registerUser(User user, HttpServletRequest request){
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername,user.getUsername());
        long count = userService.count(wrapper);
        if(count==0){
            userService.save(user);
            request.getSession().setAttribute("user",user);
            return "{\"code\":0}";
        }else{
            return "{\"code\":1}";
        }
    }

    @RequestMapping("/loginUser")
    @ResponseBody
    public String loginUser(String username,String password,HttpServletRequest request){
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername,username);
        User user = userService.getOne(wrapper);
        if(user!=null && user.getPassword().equals(password)){
            request.getSession().setAttribute("user",user);
            return "{\"code\":0}";
        }else{
            return "{\"code\":1}";
        }
    }

    @RequestMapping("/search")
    public String search(@RequestParam(defaultValue = "") String keyword, @RequestParam(defaultValue = "2") int type, Model model){
        List<Article> articlesByKeyword = articleService.getArticlesByKeyword(keyword, type);
        model.addAttribute("list",articlesByKeyword);
        model.addAttribute("keyword",keyword);
        return "search";
    }

    @RequestMapping("/findArticle")
    @ResponseBody
    public Object findArticle(@RequestParam("keywords")String keywords,
                              @RequestParam(defaultValue = "1")Integer type,
                              @RequestParam(defaultValue = "1987")Integer startDate,
                              @RequestParam(defaultValue = "2022")Integer endDate,
                              @RequestParam(defaultValue = "1")Integer page){
        int limit = 0;
        if(page>1){
            limit = (page-1)*10;
        }
        ArticleParam articleParam = new ArticleParam(keywords,type,startDate,endDate,limit);
        return new PageVo(articleService.getArticleByInfo(articleParam),page);
    }

    @PostMapping("/postContent")
    @ResponseBody
    public Object postContent(Integer score,String content,Integer id,HttpServletRequest req){
        User user = (User)req.getSession().getAttribute("user");
        if(user!=null){
            userService.submitContent(score,content,id,user.getUid());
            return "{\"code\":0}";
        }else{
            return "{\"code\":1}";
        }
    }
}