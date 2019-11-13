package com.manuh.mudrabook;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;


public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences pref = getApplicationContext().getSharedPreferences("MUDRABOOK", 0);



                if(pref.getString("login","").equalsIgnoreCase("TRUE")) {
                    startActivity(new Intent(SplashActivity.this, ProfileActivity.class));
                    finish();
                }else{
                    startActivity(new Intent(SplashActivity.this,LoginActivity.class));
                    finish();
                }
            }
        },2000);

    }
}
