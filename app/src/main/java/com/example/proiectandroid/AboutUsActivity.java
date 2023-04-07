package com.example.proiectandroid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.transition.Explode;
import android.transition.Slide;

import com.example.proiectandroid.Fragments.MapsFragment;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

public class AboutUsActivity extends AppCompatActivity {
    private YouTubePlayerView youTubePlayerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        getWindow().setEnterTransition(new Slide());
        getWindow().setExitTransition(new Explode());

        youTubePlayerView = findViewById(R.id.youtubePlayer);
        getLifecycle().addObserver(youTubePlayerView);

//        youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
//            @Override
//            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
//                String videoId = "q-Y-z6HmRgI";
//                youTubePlayer.pause();
//                youTubePlayer.loadVideo(videoId, 0);
//            }
//        });

        Fragment fragment = new MapsFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.detailsFragment, fragment).commit();
    }
}