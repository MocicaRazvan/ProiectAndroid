package com.example.proiectandroid.Tasks;

import android.os.AsyncTask;

import com.example.proiectandroid.Listeners.DeleteArticleListener;
import com.example.proiectandroid.MyApplication;

public class DeleteArticle extends AsyncTask<String, Void, String> {
    private DeleteArticleListener listener;

    public DeleteArticle(DeleteArticleListener listener) {
        this.listener = listener;
    }

    @Override
    protected String doInBackground(String... strings) {
        try {

            MyApplication.getmAppDatabase().articleDao().delete(strings[0]);
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
        return "success";
    }

    @Override
    protected void onPostExecute(String s) {
        listener.deleteArticleFinished(s);
    }
}
