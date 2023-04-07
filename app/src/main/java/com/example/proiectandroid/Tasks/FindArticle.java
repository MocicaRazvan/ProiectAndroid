package com.example.proiectandroid.Tasks;

import android.os.AsyncTask;

import com.example.proiectandroid.Listeners.CheckArticle;
import com.example.proiectandroid.Listeners.SelectArticleListener;
import com.example.proiectandroid.MyApplication;

public class FindArticle extends AsyncTask<String, Void, Integer> {

    CheckArticle listener;

    public FindArticle(CheckArticle listener) {
        this.listener = listener;
    }

    @Override
    protected Integer doInBackground(String... strings) {
        return MyApplication.getmAppDatabase().articleDao().getIsArticleSaved(strings[0]);
    }

    @Override
    protected void onPostExecute(Integer integer) {
        listener.getIsArticleInDbResult(integer);
    }
}
