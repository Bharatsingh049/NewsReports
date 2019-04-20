package com.news.newsreports;

import android.graphics.Bitmap;
import android.text.TextUtils;

import java.io.Serializable;

/**
 * Created by Bharat on 4/1/2017.
 */
public class NewsModel implements Serializable {
    private String author;
    private String Description;
    private String title;
    private String url;
    private String image2url;
    private String[] DateArray=new String[3];
    private String DateString;
    private Bitmap Imagebitmap;

    public NewsModel() {
    }

    public NewsModel(String author, String Description , String title, String url, String image2url) {
        super();
        this.author = author;
        this.title = title;
        this.url = url;
        this.Description = Description;
        this.image2url=image2url;
    }

    public void setDateArray(String[] date){
        this.DateArray[0]=date[0];
        this.DateArray[1]=date[1];
        this.DateArray[2]=date[2];
        this.DateString=date[0]+"-"+date[1]+"-"+date[2];
    }

    public void setDateString(String dateString) {
        this.DateString = dateString;
if (!TextUtils.isEmpty(dateString)){
        this.DateArray=dateString.split("-");}
    }

    public String[] getDateArray() {
        return DateArray;
    }

    public String getDateString() {
        return DateString;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getimage() {
        return image2url;
    }

    public void setimage(String image2url) {this.image2url = image2url;}

    public Bitmap getbitmapimage(){return Imagebitmap;}

    public void setbitmapimage(Bitmap Imagebitmap){this.Imagebitmap=Imagebitmap;}
}
