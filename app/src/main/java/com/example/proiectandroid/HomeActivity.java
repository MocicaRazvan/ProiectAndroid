package com.example.proiectandroid;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.proiectandroid.Fragments.AllArticlesFragment;
import com.example.proiectandroid.Fragments.SavedArticlesFragment;
import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends AppCompatActivity {
    private AllArticlesFragment allArticlesFragment = new AllArticlesFragment();
    private SavedArticlesFragment savedArticlesFragment = new SavedArticlesFragment();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getSupportActionBar().setTitle("All Articles");

        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, allArticlesFragment).commit();

    }

    @Override
    public boolean onCreatePanelMenu(int featureId, @NonNull Menu menu) {
        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.menu_bruger, menu);

        return super.onCreatePanelMenu(featureId, menu);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menuAll:
                getSupportActionBar().setTitle("All Articles");
                getSupportFragmentManager().beginTransaction().
                        setCustomAnimations(R.anim.slide_in,
                                R.anim.fade_out,
                                R.anim.fade_in,
                                R.anim.slide_out).replace(R.id.fragmentContainer, allArticlesFragment).commit();
                return true;

            case R.id.menuSaved:
                getSupportActionBar().setTitle("Saved Articles");
                getSupportFragmentManager().beginTransaction().
                        setCustomAnimations(R.anim.slide_in,
                                R.anim.fade_out,
                                R.anim.fade_in,
                                R.anim.slide_out)
                        .replace(R.id.fragmentContainer, savedArticlesFragment).commit();
                return true;
            case R.id.menuLogout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(this, MainActivity.class));
                return true;
            case R.id.menuProfile:
                getSupportActionBar().setTitle("Your profile");
                startActivity(new Intent(this, ProfileActivity.class), ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
                return true;
            case R.id.menuAboutUs:
                getSupportActionBar().setTitle("About Us");
                startActivity(new Intent(this, AboutUsActivity.class), ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}

