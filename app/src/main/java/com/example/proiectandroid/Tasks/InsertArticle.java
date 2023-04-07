package com.example.proiectandroid.Tasks;

import android.os.AsyncTask;

import com.example.proiectandroid.Information.RoomArticle;
import com.example.proiectandroid.Listeners.InsertArticleListener;
import com.example.proiectandroid.MyApplication;

public class InsertArticle extends AsyncTask<RoomArticle, Void, String> {
    InsertArticleListener listener;

    public InsertArticle(InsertArticleListener listener) {
        this.listener = listener;
    }

    @Override
    protected String doInBackground(RoomArticle... roomArticles) {
        try {
            MyApplication.getmAppDatabase().articleDao().insertAll(roomArticles);

        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
        return "success";
    }

    @Override
    protected void onPostExecute(String s) {
        listener.insertArticlesFinished(s);
    }
}
