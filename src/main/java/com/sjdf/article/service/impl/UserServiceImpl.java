package com.sjdf.article.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sjdf.article.dao.*;
import com.sjdf.article.entity.*;
import com.sjdf.article.param.ArticleParam;
import com.sjdf.article.service.ArticleService;
import com.sjdf.article.service.UserService;
import com.sjdf.article.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl extends ServiceImpl<UserDao,User> implements UserService{

    @Autowired
    RoundDao roundDao;

    @Autowired
    UserDao userDao;

    @Autowired
    ArticleDao articleDao;

    @Autowired
    ArticleService articleService;

    @Autowired
    ScoreResultDao scoreResultDao;

    @Autowired
    ScoreResultCenterDao scoreResultCenterDao;

    @Autowired
    RecordDao recordDao;

    public void calGroup(){
        try{
            long startTime = System.currentTimeMillis();
            String log = "";
            // 初始化每个用户的点坐标
            Round round = new Round();
            round.setBeginTime(String.valueOf(startTime));
            round.setGroupNum(3);
            roundDao.insert(round);
            //获取关键词
            //获取用户信息
            List<String> keys = articleDao.getDistinctKY();
            List<String> majors = userDao.getMajors();
            List<Integer> ages = userDao.getAges();
            List<String> hobbies = userDao.getHobbies();
            List<User> all = userDao.getAll();

            int typeSize = keys.size();
            int hobbySize = hobbies.size();
            int majorSize = majors.size();
            int ageSize = ages.size();

            List<ScoreResult> scoreResultList = new ArrayList<>();

            for(int i=0; i<all.size();i++){
                User tmpUser = all.get(i);
                Article article = articleDao.selectById(tmpUser.getLastArticleId());

                ScoreResult sr = new ScoreResult();
                int majorPos = this.findPos(majors, tmpUser.getMajor());
                int hobbyPos = this.findPos(hobbies, tmpUser.getHobby());
                int KYPos = findPos(keys, article.getKY());
                double age = (double) tmpUser.getAge()
                        / (double) ages.get(ageSize - 1);
                double major = majorPos == -1 ? 0 : ((double) majorPos + 1)
                        / (double) (majorSize);
                double hobby = hobbyPos == -1 ? 0 : ((double) hobbyPos + 1)
                        / (double) (hobbySize);
                double type = KYPos == -1 ? 0 : ((double) KYPos + 1)
                        / (double) (typeSize);

                sr.setAge(age);
                sr.setHobby(hobby);
                sr.setLastBookType(type);
                sr.setMajor(major);
                sr.setDoneCount(0);
                sr.setRoundId(round.getId());
                sr.setUserId(tmpUser.getUid());
                scoreResultDao.insert(sr);
                scoreResultList.add(sr);
            }
            // 用户点坐标初始化结束
            // 开始随机产生三个中心点
            // TODO 有风险无限循环永远产生不了3个不同的初始化点
            int scoreResultListSize = scoreResultList.size();
            List<Integer> ranPosList = new ArrayList<>();
            List<ScoreResultCenter> scoreResultCenterList = new ArrayList<>();
            for (int i = 0; i < round.getGroupNum(); i++) {
                boolean flag = true;
                while (true) {
                    int ran = (int) (Math.random() * scoreResultListSize);
                    if (ranPosList.contains(ran)) {
                        continue;
                    }
                    ScoreResultCenter center = new ScoreResultCenter();
                    ScoreResult result = scoreResultList.get(ran);
                    center.setAge(result.getAge());
                    center.setDoneCount(0);
                    center.setHobby(result.getHobby());
                    center.setLastBookType(result.getLastBookType());
                    center.setMajor(result.getMajor());
                    center.setRoundId(round.getId());
                    boolean tmpFlag = true;
                    for (int j = 0; j < scoreResultCenterList.size(); j++) {
                        ScoreResultCenter listCenter = scoreResultCenterList
                                .get(j);
                        if (listCenter.getAge() == center.getAge()
                                && listCenter.getHobby() == center.getHobby()
                                && listCenter.getMajor() == center.getMajor()
                                && listCenter.getLastBookType() == center
                                .getLastBookType()) {
                            tmpFlag = false;
                            ranPosList.add(ran);
                            break;
                        }
                    }
                    if (tmpFlag == false) {
                        continue;
                    }
                    scoreResultCenterDao.insert(center);
                    scoreResultCenterList.add(center);
                    ranPosList.add(ran);
                    if (flag == true) {
                        break;
                    }
                }
            }
            log += "初始化，随机抽取";
            for (int i = 0; i < scoreResultCenterList.size(); i++) {
                ScoreResultCenter center = scoreResultCenterList.get(i);
                log += "中心点" + (i + 1) + "=["+ center.getAge() + "," + center.getHobby() + ","
                        + center.getLastBookType() + "," + center.getMajor()
                        + "]，";
            }
            log += "\r\n<br/>";

            int turn = 0;
            List<List<ScoreResult>> oldList = new ArrayList<>();
            for (int i = 0; i < round.getGroupNum(); i++) {
                List<ScoreResult> list = new ArrayList<>();
                oldList.add(list);
            }
            List<List<ScoreResult>> newList = new ArrayList<>();
            for (int i = 0; i < round.getGroupNum(); i++) {
                List<ScoreResult> list = new ArrayList<>();
                newList.add(list);
            }
            while (true) {
                // 将新列表复制到旧列表，清除新列表
                for (int i = 0; i < newList.size(); i++) {
                    List<ScoreResult> oList = newList.get(i);
                    List<ScoreResult> nList = oldList.get(i);
                    nList.clear();
                    for (int j = 0; j < oList.size(); j++) {
                        nList.add(oList.get(j));
                    }
                }
                for (int i = 0; i < newList.size(); i++) {
                    List<ScoreResult> tmpList = newList.get(i);
                    tmpList.clear();
                }
                boolean flag = false;
                List<List<Double>> distList = new ArrayList<>();
                for (int i = 0; i < scoreResultListSize; i++) {
                    List<Double> tmpList = new ArrayList<>();
                    ScoreResult result = scoreResultList.get(i);
                    for (int j = 0; j < round.getGroupNum(); j++) {
                        ScoreResultCenter center = scoreResultCenterList.get(j);
                        double ag = result.getAge();
                        double ho = result.getHobby();
                        double lb = result.getLastBookType();
                        double ma = result.getMajor();
                        double cag = center.getAge();
                        double cho = center.getHobby();
                        double clb = center.getLastBookType();
                        double cma = center.getMajor();
                        double dist = Math.sqrt((ag - cag) * (ag - cag) + (ho - cho)
                                * (ho - cho) + (lb - clb) * (lb - clb)
                                + (ma - cma) * (ma - cma));
                        tmpList.add(dist);
                    }
                    distList.add(tmpList);
                }
                List<Integer> nearList = new ArrayList<>();
                for (int i = 0; i < distList.size(); i++) {
                    List<Double> tmpList = distList.get(i);
                    double min = tmpList.get(0);
                    int pos = 0;
                    for (int j = 1; j < tmpList.size(); j++) {
                        if (min > tmpList.get(j)) {
                            min = tmpList.get(j);
                            pos = j;
                        }
                    }
                    // log +="第"+(i+1)+"个点更靠近中心点"+(pos+1)+",";

                    newList.get(pos).add(scoreResultList.get(i));
                }
                //如果两次三个分组元素一致，聚类循环结束
                flag = this.isEquals(oldList, newList);
                if (flag == true) {
                    break;
                } else {
                    // TODO 中心点变更
                    turn++;
                    scoreResultCenterList.clear();
                    log += "第" + (turn ) + "次迭代，";
                    for (int i = 0; i < round.getGroupNum(); i++) {
                        List<ScoreResult> list = newList.get(i);
                        // TODO
                        int size = list.size();
                        double suma = 0;
                        double sumb = 0;
                        double sumc = 0;
                        double sumd = 0;
                        double sume = 0;
                        for (int j = 0; j < size; j++) {
                            ScoreResult sr = list.get(j);
                            sumb += sr.getAge();
                            sumc += sr.getHobby();
                            sumd += sr.getLastBookType();
                            sume += sr.getMajor();
                        }
                        double[] xyz = new double[5];
                        xyz[0] = suma / size;
                        xyz[1] = sumb / size;
                        xyz[2] = sumc / size;
                        xyz[3] = sumd / size;
                        xyz[4] = sume / size;
                        ScoreResultCenter newCenter = new ScoreResultCenter();
                        newCenter.setAge(xyz[1]);
                        newCenter.setHobby(xyz[2]);
                        newCenter.setLastBookType(xyz[3]);
                        newCenter.setMajor(xyz[4]);
                        newCenter.setRoundId(round.getId());
                        newCenter.setDoneCount(turn);
                        scoreResultCenterList.add(newCenter);
                        scoreResultCenterDao.insert(newCenter);
                        log += "中心点" + (i + 1) + "=["+ newCenter.getAge() + "," + newCenter.getHobby()
                                + "," + newCenter.getLastBookType() + "," + newCenter.getMajor()
                                + "]，";
                    }
                    log += "\r\n<br/>";
                }
            }
            log += "经过" + (turn ) + "次迭代，聚类算法收敛，得到最终聚类分组结果如下：\r\n<br/>";
//			List<List<Integer>> paramList = new ArrayList<>();
            for (int i = 0; i < newList.size(); i++) {
                List<ScoreResult> list = newList.get(i);
                log += "第" + (i + 1) + "组名单：";
//				List<Integer> pList = new ArrayList<>();
                for (int j = 0; j < list.size(); j++) {
                    ScoreResult sr = list.get(j);
                    log += "{userId = " + sr.getUserId() + "},";
//					pList.add(sr.getUserId());
                    sr.setDoneCount(turn);
                    scoreResultDao.updateById(sr);
                }
//				paramList.add(pList);
                log += "\r\n<br/>";
            }
            this.updateScoreResultGroup(newList);
            long endTime = System.currentTimeMillis();
            long time = endTime - startTime;
            round.setEndTime(String.valueOf(endTime));
            round.setTimeMillis(String.valueOf(time));
            log = "本次聚类计算运行时间为：" + time + "毫秒，" + "共涉及用户："
                    + (scoreResultListSize) + "个，" + "设置聚类中心："
                    + round.getGroupNum() + "个，计算过程如下：\r\n<br/>" + log;
            // request.setAttribute("log", log);
            round.setLog(log);
            roundDao.updateById(round);
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    @Override
    public void submitContent(Integer score, String content, Integer id, Integer uid) {
        Record r = new Record();
        r.setBookId(id);
        r.setOperdate(DateUtil.getDateStr());
        r.setUserId(uid);
        r.setComments(content);
        r.setScore(score);

        recordDao.insert(r);
        Article article = articleDao.selectById(id);
        article.setView(article.getView()+1); //累计查看次数
        articleDao.updateById(article);

        User user = userDao.selectById(uid);
        user.setLastArticleId(id);
        userDao.updateById(user);
    }

    @Override
    public List<Article> getTop20(Integer uid) {
        List<Integer> articleId = recordDao.getArticleId(uid);
        List<Article> articles = new ArrayList<>();
        if(articleId.size()>0){
            articles = articleService.listByIds(articleId);
            if(articles.size()<20){
                int limit = 20-articles.size();
                List<Integer> randIds = articleDao.getRandIds(limit, uid);
                List<Article> rands = articleService.listByIds(randIds);
                articles.addAll(rands);
            }
        }
        return articles;
    }

    private int findPos(List<String> list, String value) {
        if (list == null)
            return -1;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).equals(value)) {
                return i;
            }
        }
        return -1;
    }

    private boolean isEquals(List<List<ScoreResult>> oldList,
                             List<List<ScoreResult>> newList) {
        boolean ret = true;
        int size = oldList.size();
        for (int i = 0; i < size; i++) {
            List<ScoreResult> srList1 = oldList.get(i);
            List<ScoreResult> srList2 = newList.get(i);
            for (int j = 0; j < srList1.size(); j++) {
                ScoreResult result1 = srList1.get(j);
                boolean tmpFlag = false;
                for (int k = 0; k < srList2.size(); k++) {
                    ScoreResult result2 = srList2.get(k);
                    if (result1.getAge() == result2.getAge()
                            && result1.getHobby() == result2.getHobby()
                            && result1.getLastBookType() == result2.getLastBookType()
                            && result1.getMajor() == result2.getMajor()) {
                        tmpFlag = true;
                        break;
                    }
                }
                if (tmpFlag != true) {
                    return false;
                }
            }
            for (int j = 0; j < srList2.size(); j++) {
                ScoreResult result2 = srList2.get(j);
                boolean tmpFlag = false;
                for (int k = 0; k < srList1.size(); k++) {
                    ScoreResult result1 = srList1.get(k);
                    if (result1.getAge() == result2.getAge()
                            && result1.getHobby() == result2.getHobby()
                            && result1.getLastBookType() == result2.getLastBookType()
                            && result1.getMajor() == result2.getMajor()) {
                        tmpFlag = true;
                        break;
                    }
                }
                if (tmpFlag != true) {
                    return false;
                }
            }
        }
        return ret;
    }

    public void updateScoreResultGroup(List<List<ScoreResult>> list){
        for(int i = 0 ; i < list.size(); i++){
            List<ScoreResult> srList = list.get(i);
            for(int j = 0 ; j < srList.size(); j++){
                ScoreResult sr = srList.get(j);
                sr.setGroupId(i + 1);
                scoreResultDao.updateById(sr);
            }
        }
    }
}