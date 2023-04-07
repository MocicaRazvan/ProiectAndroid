package com.example.proiectandroid.Fragments;

import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proiectandroid.DetailsActivity;
import com.example.proiectandroid.Listeners.SelectArticleListener;
import com.example.proiectandroid.Information.NewsApiResponse;
import com.example.proiectandroid.Information.NewsHeadlines;
import com.example.proiectandroid.R;
import com.example.proiectandroid.Recycler.ArticlesAdapter;
import com.example.proiectandroid.Requests.OnFetchDataListener;
import com.example.proiectandroid.Requests.RequestManager;

import java.util.List;
import java.util.Objects;


public class AllArticlesFragment extends Fragment implements SelectArticleListener, View.OnClickListener {

    private RecyclerView recyclerView;

    private ArticlesAdapter adapter;

    private ProgressDialog progressDialog;

    private Button b1, b2, b3, b4, b5, b6, b7;
    private SearchView searchView;


    public AllArticlesFragment() {
        super(R.layout.fragment_all_articles);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("Getting the news");
        progressDialog.show();

        recyclerView = view.findViewById(R.id.recyclerMain);

        b1 = view.findViewById(R.id.btn1);
        b1.setOnClickListener(this);
        b2 = view.findViewById(R.id.btn2);
        b2.setOnClickListener(this);
        b3 = view.findViewById(R.id.btn3);
        b3.setOnClickListener(this);
        b4 = view.findViewById(R.id.btn4);
        b4.setOnClickListener(this);
        b5 = view.findViewById(R.id.btn5);
        b5.setOnClickListener(this);
        b6 = view.findViewById(R.id.btn6);
        b6.setOnClickListener(this);
        b7 = view.findViewById(R.id.btn7);
        b7.setOnClickListener(this);

        searchView = view.findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                progressDialog.setTitle("Getting the news articles of " + query);
                progressDialog.show();

                RequestManager manager = new RequestManager(getActivity());
                manager.getNewsHeadlines(listener, "general", query);

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });


        RequestManager manager = new RequestManager(getActivity());
        manager.getNewsHeadlines(listener, "general", null);
    }


    private final OnFetchDataListener<NewsApiResponse> listener = new OnFetchDataListener<NewsApiResponse>() {
        @Override
        public void onFetchData(List<NewsHeadlines> list, String message) {
            if (list.isEmpty()) {
                Toast.makeText(getActivity(), "No news found", Toast.LENGTH_SHORT).show();
            } else {
                showNews(list);
            }
            progressDialog.dismiss();
        }

        @Override
        public void onError(String message) {
            Toast.makeText(getActivity(), "Something went wrong...", Toast.LENGTH_SHORT).show();
        }
    };

    private void showNews(List<NewsHeadlines> list) {

        recyclerView.setHasFixedSize(true);
//        int span = requireContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT ? 1 : 2;
        int span = 1;
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), span));
        adapter = new ArticlesAdapter(getActivity(), list, this);
        recyclerView.setAdapter(adapter);
    }


    @Override
    public void OnNewsClicked(NewsHeadlines headlines) {
        startActivity(new Intent(getActivity(), DetailsActivity.class)
                        .putExtra("data", headlines),
                ActivityOptions.makeSceneTransitionAnimation(getActivity()).toBundle()
        );

    }


    @Override
    public void onClick(View v) {
        Button button = (Button) v;
        String category = button.getText().toString();
        progressDialog.setTitle("Getting news of " + category);
        progressDialog.show();

        RequestManager manager = new RequestManager(getActivity());
        manager.getNewsHeadlines(listener, category, null);

    }


}