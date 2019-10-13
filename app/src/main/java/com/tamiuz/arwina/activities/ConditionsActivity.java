package com.tamiuz.arwina.activities;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

import com.tamiuz.arwina.R;

public class ConditionsActivity extends AppCompatActivity {

    @BindView(R.id.conditions_Content_txtV_id)
    TextView conditions_txtV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conditions);
        ButterKnife.bind(this);

        if (getIntent().hasExtra("conditions")) {
            String conditions = getIntent().getStringExtra("conditions");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                conditions_txtV.setText(Html.fromHtml(conditions, Html.FROM_HTML_MODE_COMPACT));
            } else {
                conditions_txtV.setText(Html.fromHtml(conditions));
            }
        }
    }

    @OnClick(R.id.conditions_back_txtV_id)
    void goBack() {
        finish();
    }

    @OnClick(R.id.conditions_user_imageV_id)
    void gotoProfile() {
        if (MainActivity.user_Id != 0) {
            Intent intent = new Intent(ConditionsActivity.this, ProfileActivity.class);
            intent.putExtra("role", MainActivity.userModel.getRole());
            startActivity(intent);
        } else {
            startActivity(new Intent(ConditionsActivity.this, LogInActivity.class));
        }
    }

    @OnClick(R.id.conditions_notifications_imageV_id)
    void gotoNotifications() {
        if (MainActivity.user_Id != 0) {
            Intent intent = new Intent(ConditionsActivity.this, MyNotifications.class);
            startActivity(intent);
        } else {
            startActivity(new Intent(ConditionsActivity.this, LogInActivity.class));
        }
    }
}
