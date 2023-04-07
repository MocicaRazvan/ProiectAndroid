package com.example.proiectandroid.Recycler;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proiectandroid.Listeners.SelectArticleListener;
import com.example.proiectandroid.Information.NewsHeadlines;
import com.example.proiectandroid.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ArticlesAdapter extends RecyclerView.Adapter<ArticlesViewHolder> {

    private Context context;
    private List<NewsHeadlines> headlines;

    private SelectArticleListener listener;


    public ArticlesAdapter(Context context, List<NewsHeadlines> headlines, SelectArticleListener listener
    ) {
        this.context = context;
        this.headlines = headlines;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ArticlesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ArticlesViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.headline_list_items, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ArticlesViewHolder holder, int position) {

        holder.textTitle.setText(headlines.get(position).getTitle());
        holder.textSource.setText(headlines.get(position).getSource().getName());

        if (headlines.get(position).getUrlToImage() != null) {
            Picasso.get().load(headlines.get(position).getUrlToImage())
                    .into(holder.imgHeadline);
        }

        holder.cardView.setOnClickListener(v -> {
            listener.OnNewsClicked(headlines.get(position));
        });

    }

    @Override
    public int getItemCount() {
        return headlines.size();
    }
}
