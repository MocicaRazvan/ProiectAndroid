package com.example.proiectandroid.Information;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "RoomArticle")
public class RoomArticle {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String title;
    public String author;
    public String time;
    public String detail;
    public String content;
    public String imgUrl;
    public String ownerId;

    public String source;


    public RoomArticle(String title, String author, String time, String detail, String content, String imgUrl, String ownerId, String source) {
        this.title = title;
        this.author = author;
        this.time = time;
        this.detail = detail;
        this.content = content;
        this.imgUrl = imgUrl;
        this.ownerId = ownerId;
        this.source = source;
    }
}
