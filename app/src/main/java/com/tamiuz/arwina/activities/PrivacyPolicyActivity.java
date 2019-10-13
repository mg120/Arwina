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

public class PrivacyPolicyActivity extends AppCompatActivity {

    @BindView(R.id.privacyPolicy_content_txtV_id)
    TextView privacyPolicy_txtV;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_policy);
        ButterKnife.bind(this);

        if (getIntent().hasExtra("privacy_data")){
            String privacyPolicy = getIntent().getStringExtra("privacy_data");

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                privacyPolicy_txtV.setText(Html.fromHtml(privacyPolicy, Html.FROM_HTML_MODE_COMPACT));
            } else {
                privacyPolicy_txtV.setText(Html.fromHtml(privacyPolicy));
            }
        }
    }

    @OnClick(R.id.privacyPolicy_back_txtV_id)
    void goBack(){
        finish();
    }

    @OnClick(R.id.privacyPolicy_user_imageV_id)
    void goToProfile(){
        if (MainActivity.user_Id != 0) {
            Intent intent = new Intent(PrivacyPolicyActivity.this, ProfileActivity.class);
            startActivity(intent);
        } else {
            startActivity(new Intent(PrivacyPolicyActivity.this, LogInActivity.class));
        }
    }

    @OnClick(R.id.privacyPolicy_notifications_imageV_id)
    void goToNotifications(){
        if (MainActivity.user_Id != 0) {
            Intent intent = new Intent(PrivacyPolicyActivity.this, MyNotifications.class);
            intent.putExtra("role", MainActivity.userModel.getRole());
            startActivity(intent);
        } else {
                startActivity(new Intent(PrivacyPolicyActivity.this, LogInActivity.class));
        }
    }
}
