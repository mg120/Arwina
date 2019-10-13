package com.tamiuz.arwina.activities;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.tamiuz.arwina.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignUpTypeActivity extends AppCompatActivity {

    private int sign_type = 0;
    Intent intent = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_type);
        ButterKnife.bind(this);

    }

    @OnClick(R.id.back_imageV_id)
    void goBack() {
        finish();
    }

    @OnClick(R.id.user_sign_btn_id)
    void userSign() {
        sign_type = 0;
        intent = new Intent(SignUpTypeActivity.this, MandoopSignUp.class);
        intent.putExtra("sign_type", sign_type);
        startActivity(intent);
    }

    @OnClick(R.id.company_sign_btn_id)
    void companySign() {
        intent = new Intent(SignUpTypeActivity.this, CompanySignUp.class);
        startActivity(intent);
    }

    @OnClick(R.id.mandoop_sign_btn_id)
    void mandoopSign() {
        sign_type = 2;
        intent = new Intent(SignUpTypeActivity.this, MandoopSignUp.class);
        intent.putExtra("sign_type", sign_type);
        startActivity(intent);
    }
}
