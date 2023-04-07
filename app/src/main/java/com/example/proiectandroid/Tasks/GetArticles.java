package com.example.proiectandroid.Tasks;

import android.os.AsyncTask;

import com.example.proiectandroid.Information.RoomArticle;
import com.example.proiectandroid.Listeners.GetArticlesListener;
import com.example.proiectandroid.MyApplication;

import java.util.List;

public class GetArticles extends AsyncTask<String, Void, List<RoomArticle>> {

    private GetArticlesListener listener;

    public GetArticles(GetArticlesListener listener) {
        this.listener = listener;
    }

    @Override
    protected List<RoomArticle> doInBackground(String... strings) {
        return MyApplication.getmAppDatabase().articleDao().getArticlesByUser(strings[0], strings[1]);
    }

    @Override
    protected void onPostExecute(List<RoomArticle> articles) {
        listener.getArticlesByUserFinished(articles);
    }
}
