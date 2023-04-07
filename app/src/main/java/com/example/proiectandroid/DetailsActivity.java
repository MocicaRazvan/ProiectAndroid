package com.example.proiectandroid;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.transition.Explode;
import android.transition.Slide;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.proiectandroid.Information.NewsHeadlines;
import com.example.proiectandroid.Information.RoomArticle;
import com.example.proiectandroid.Listeners.CheckArticle;
import com.example.proiectandroid.Listeners.DeleteArticleListener;
import com.example.proiectandroid.Listeners.InsertArticleListener;
import com.example.proiectandroid.Tasks.DeleteArticle;
import com.example.proiectandroid.Tasks.FindArticle;
import com.example.proiectandroid.Tasks.InsertArticle;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

public class DetailsActivity extends AppCompatActivity implements CheckArticle, InsertArticleListener, DeleteArticleListener {

    private NewsHeadlines headlines;
    private TextView txtTitle, txtAuthor, txtTime, txtDetail, txtContent;
    private ImageView imgNews;

    private Button btnSave, btnDelete;
    private boolean show;

    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        getWindow().setEnterTransition(new Slide());
        getWindow().setExitTransition(new Explode());

        txtTitle = findViewById(R.id.textDetailTitle);
        txtAuthor = findViewById(R.id.textDetailAuthor);
        txtTime = findViewById(R.id.textDetailTime);
        txtDetail = findViewById(R.id.textDetailDetail);
        txtContent = findViewById(R.id.textDetailContent);
        imgNews = findViewById(R.id.imgDetailNews);
        btnSave = findViewById(R.id.btnSave);
        btnDelete = findViewById(R.id.btnDelete);


        headlines = (NewsHeadlines) getIntent().getSerializableExtra("data");

        txtTitle.setText(headlines.getTitle());
        txtAuthor.setText(headlines.getAuthor());
        txtTime.setText(headlines.getPublishedAt());
        txtDetail.setText(headlines.getDescription());
        txtContent.setText(headlines.getContent());

        if (headlines.getUrlToImage() != null) {
            Picasso.get().load(headlines.getUrlToImage()).into(imgNews);
        }

        new FindArticle(this).execute(headlines.getTitle());

    }


    @Override
    public void getIsArticleInDbResult(Integer result) {
        if (result > 0) {
            show = false;
        } else {
            show = true;
        }


        if (show) {
            btnSave.setVisibility(View.VISIBLE);
            btnSave.setOnClickListener(v -> {
                insertArticle();
            });
        } else {
            btnDelete.setVisibility(View.VISIBLE);
            btnDelete.setOnClickListener(v -> {
                deleteArticle();

            });
        }

    }

    private void deleteArticle() {
        new DeleteArticle(this).execute(headlines.getTitle());
    }

    private void insertArticle() {
        FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
        RoomArticle article = new RoomArticle(headlines.getTitle(), headlines.getAuthor(),
                headlines.getPublishedAt(), headlines.getDescription(), headlines.getContent()
                , headlines.getUrlToImage(), mUser.getUid(), headlines.getSource().getName());

        new InsertArticle(this).execute(article);
    }

    @Override
    public void insertArticlesFinished(String result) {
        if (result.equals("success")) {
            Toast.makeText(this, "Article added to saved list", Toast.LENGTH_SHORT).show();
            this.recreate();
        } else {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void deleteArticleFinished(String result) {
        if (result.equals("success")) {

            Toast.makeText(this, "Article added to saved list", Toast.LENGTH_SHORT).show();
            this.recreate();
        } else {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
        }

    }
}