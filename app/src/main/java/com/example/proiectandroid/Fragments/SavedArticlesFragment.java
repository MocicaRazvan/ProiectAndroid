package com.example.proiectandroid.Fragments;

import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.PrimaryKey;

import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.proiectandroid.DetailsActivity;
import com.example.proiectandroid.Information.NewsHeadlines;
import com.example.proiectandroid.Information.RoomArticle;
import com.example.proiectandroid.Listeners.GetArticlesListener;
import com.example.proiectandroid.Listeners.SelectArticleListener;
import com.example.proiectandroid.R;
import com.example.proiectandroid.Recycler.ArticlesAdapter;
import com.example.proiectandroid.Tasks.GetArticles;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;


public class SavedArticlesFragment extends Fragment implements GetArticlesListener, SelectArticleListener {

    private RecyclerView recyclerView;
    private ArticlesAdapter adapter;
    private List<NewsHeadlines> list;
    private ProgressDialog progressDialog;

    private SearchView searchView;
    private FirebaseUser mUser;

    public SavedArticlesFragment() {
        super(R.layout.fragment_saved_articles);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        list = new ArrayList<>();

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("Getting the saved news");
        progressDialog.show();

        recyclerView = view.findViewById(R.id.savedRecyclerMain);
        searchView = view.findViewById(R.id.savedSearchView);
        mUser = FirebaseAuth.getInstance().getCurrentUser();

        new GetArticles(this).execute(mUser.getUid(), "");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                progressDialog.setTitle("Getting the saved news");
                progressDialog.show();
                new GetArticles(SavedArticlesFragment.this).execute(mUser.getUid(), query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Toast.makeText(getActivity(), "Something went wrong...", Toast.LENGTH_SHORT).show();
                return false;
            }
        });

    }

    private NewsHeadlines convert(RoomArticle article) {
        return new NewsHeadlines(article.author, article.title, article.detail, article.imgUrl, article.time, article.content, article.source);
    }

    @Override
    public void getArticlesByUserFinished(List<RoomArticle> articles) {

        list.clear();
        for (RoomArticle article :
                articles) {
            list.add(convert(article));
        }
        recyclerView.setHasFixedSize(true);
        int span = 1;
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), span));
        adapter = new ArticlesAdapter(getActivity(), list, this);
        recyclerView.setAdapter(adapter);
        progressDialog.dismiss();

    }


    @Override
    public void OnNewsClicked(NewsHeadlines headlines) {
        startActivity(new Intent(getActivity(), DetailsActivity.class)
                .putExtra("data", headlines), ActivityOptions.makeSceneTransitionAnimation(getActivity()).toBundle());
    }

    @Override
    public void onResume() {
        super.onResume();
        progressDialog.setTitle("Getting the saved news");
        progressDialog.show();
        new GetArticles(this).execute(mUser.getUid(), "");
        progressDialog.dismiss();
    }
}