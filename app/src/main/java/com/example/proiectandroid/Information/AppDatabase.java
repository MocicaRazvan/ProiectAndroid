package com.example.proiectandroid.Information;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {RoomArticle.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract RoomArticleDao articleDao();
}
