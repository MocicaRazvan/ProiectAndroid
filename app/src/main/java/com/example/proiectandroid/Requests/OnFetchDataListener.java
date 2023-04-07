package com.example.proiectandroid.Requests;

import com.example.proiectandroid.Information.NewsHeadlines;

import java.util.List;

public interface OnFetchDataListener<NewsApiResponse> {

    void onFetchData(List<NewsHeadlines> list, String message);

    void onError(String message);
}
