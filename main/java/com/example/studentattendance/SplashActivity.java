package com.example.studentattendance;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class SplashActivity extends AppCompatActivity {
    //time in milliseconds
    private static int SPLASH_SCREEN = 4000;
    Animation topAnim, bottomAnim;
    ImageView splash;
    TextView logo, slogan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        //Animations
        topAnim = AnimationUtils.loadAnimation(this, R.anim.top_animation);
        bottomAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);

        splash = findViewById(R.id.splashimageid);
        logo = findViewById(R.id.logoid);
        slogan = findViewById(R.id.sloganid);

        splash.setAnimation(topAnim);
        logo.setAnimation(bottomAnim);
        slogan.setAnimation(bottomAnim);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);

                Pair[] pairs = new Pair[3];
                pairs[0] = new Pair<View, String>(splash, "logo_image");
                pairs[1] = new Pair<View, String>(logo, "logo_text");
                pairs[2] = new Pair<View, String>(slogan, "slogan_text");

                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(SplashActivity.this, pairs);
                SplashActivity.this.startActivity(intent, options.toBundle());

            }
        },SPLASH_SCREEN);
    }
}