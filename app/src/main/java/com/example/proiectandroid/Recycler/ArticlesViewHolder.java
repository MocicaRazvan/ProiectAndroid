package com.example.proiectandroid.Recycler;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proiectandroid.R;

public class ArticlesViewHolder extends RecyclerView.ViewHolder {

    TextView textTitle, textSource;
    ImageView imgHeadline;
    CardView cardView;

    public ArticlesViewHolder(@NonNull View itemView) {
        super(itemView);

        textTitle = itemView.findViewById(R.id.textTitle);
        textSource = itemView.findViewById(R.id.textSource);
        imgHeadline = itemView.findViewById(R.id.imgHeadline);
        cardView = itemView.findViewById(R.id.mainContainer);
    }
}
