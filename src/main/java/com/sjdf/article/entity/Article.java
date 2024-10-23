package com.sjdf.article.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;

@Data
public class Article implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    /**
     * id
     */
    private Integer id;

    /**
     * title
     */
    @TableField("TI")
    private String TI;

    /**
     * author
     */
    @TableField("AU")
    private String AU;

    /**
     * mentor
     */
    private String mentor;

    /**
     * paper_source
     */
    @TableField("LY")
    private String LY;

    /**
     * paper_type
     */
    private String paperType;

    /**
     * date
     */
    private String date;

    /**
     * keywords
     */
    @TableField("KY")
    private String KY;

    /**
     * abstract
     */
    @TableField("AB")
    private String AB;

    /**
     * download_times
     */
    private Integer downloadTimes;

    /**
     * refer_times
     */
    private Integer referTimes;

    /**
     * link
     */
    private String link;

    private Integer view;

    private String type;

    public Article() {}

    public String getAB(){
        if(this.AB.length()<=200){
            return this.AB;
        }else{
            return this.AB.substring(0,201)+"...";
        }
    }

    public String getTI(){
        if(this.TI.length()<=20){
            return this.TI;
        }else{
            return this.TI.substring(0,21)+"...";
        }
    }
}