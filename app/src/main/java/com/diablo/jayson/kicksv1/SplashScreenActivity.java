package com.diablo.jayson.kicksv1;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        View decorView = this.getWindow().getDecorView();
//        int uiOptions = View.SYSTEM_UI_FLAG_LOW_PROFILE;
//        decorView.setSystemUiVisibility(uiOptions);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splash_screen);
        int SPLASH_SCREEN_TIME_OUT = 500;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent mainActivity = new Intent(SplashScreenActivity.this, MainActivity.class);
                startActivity(mainActivity);
                finish();
            }
        }, SPLASH_SCREEN_TIME_OUT);

    }
}
