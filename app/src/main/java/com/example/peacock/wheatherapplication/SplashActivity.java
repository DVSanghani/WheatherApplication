package com.example.peacock.wheatherapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;

public class SplashActivity extends AppCompatActivity {

    Animation animFadeIn;

    private Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);

        activity = SplashActivity.this;

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {

                //Open Dashboard Page - if already logged-in.
                startActivity(new Intent(activity, MainActivity.class));

                finish();
            }
        }, 3000);   // 3000ml = 3 Sec.

    }
}
