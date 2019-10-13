package com.tamiuz.arwina.activities;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.ButterKnife;
import butterknife.OnClick;

import android.os.Bundle;

import com.tamiuz.arwina.R;

public class ThanksPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thanks_page);
        ButterKnife.bind(this);

    }

    @OnClick(R.id.okk_btn_btn_id)
    void okk_clicked(){
        finish();
    }
}
