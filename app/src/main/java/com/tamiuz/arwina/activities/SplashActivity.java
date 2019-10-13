package com.tamiuz.arwina.activities;

import android.content.Intent;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.google.gson.Gson;
import com.tamiuz.arwina.Models.UserModel;
import com.tamiuz.arwina.R;
import com.tamiuz.arwina.utils.SharedPreferenceManager;

public class SplashActivity extends AppCompatActivity {

    private Handler handler;
    private Intent intent ;
    private SharedPreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                preferenceManager = new SharedPreferenceManager(SplashActivity.this, SharedPreferenceManager.PREFERENCE_NAME);
                String user_data = preferenceManager.getValue(SharedPreferenceManager.USER_DATA, "");

                if (user_data != null && !user_data.equals("")) {
                    // Retrive Gson Object from Shared Prefernces ....
                    Gson gson = new Gson();
                    UserModel userModel = gson.fromJson(user_data, UserModel.class);
                    intent = new Intent(SplashActivity.this, MainActivity.class);
                    intent.putExtra("user_data", userModel);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(R.anim.enter_from_right, R.anim.exit_out_left);
                } else {
                    intent = new Intent(SplashActivity.this, LogInActivity.class);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(R.anim.enter_from_right, R.anim.exit_out_left);
                }
            }
        }, 4000);
    }
}
