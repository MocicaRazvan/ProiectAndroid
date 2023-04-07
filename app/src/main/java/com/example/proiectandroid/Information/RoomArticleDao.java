package com.example.proiectandroid.Information;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface RoomArticleDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(RoomArticle... articles);

    @Query("delete from RoomArticle where title =:txt")
    void delete(String txt);


    @Query("select * from RoomArticle where ownerId= :user and title like '%' || :query || '%'")
    List<RoomArticle> getArticlesByUser(String user, String query);

    @Query("select count() from RoomArticle where title =:txt")
    int getIsArticleSaved(String txt);
}
