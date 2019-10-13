package com.tamiuz.arwina.activities;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.tamiuz.arwina.R;

public class ShareAppActivity extends AppCompatActivity {

    @BindView(R.id.code_val_txtV_id)
    TextView code_val_txtV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_app);
        ButterKnife.bind(this);

        if (getIntent().hasExtra("code")){
            String code = getIntent().getStringExtra("code");
            code_val_txtV.setText(code);
        }
    }

    @OnClick(R.id.share_code_btn_id)
    void shareCode() {
        try {
            int applicationNameId = this.getApplicationInfo().labelRes;
            final String appPackageName = this.getPackageName();
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(Intent.EXTRA_SUBJECT, getString(applicationNameId));
            String text = "Install this cool application: ";
            String link = "https://play.google.com/store/apps/details?id=" + appPackageName;
            i.putExtra(Intent.EXTRA_TEXT, text + " " + link + "\n\n" +  "كود خصم للتطبيق: " + code_val_txtV.getText().toString());
            startActivity(Intent.createChooser(i, getString(R.string.share_app)));
        } catch (Exception e){
            e.getMessage();
        }
    }

    @OnClick(R.id.shareApp_back_txtV_id)
    void goBack() {
        finish();
    }

    @OnClick(R.id.shareApp_user_imageV_id)
    void goToProfile() {
        Intent intent = new Intent(ShareAppActivity.this, ProfileActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.shareApp_notifications_imageV_id)
    void goToNotifications() {
        Intent intent = new Intent(ShareAppActivity.this, MyNotifications.class);
        intent.putExtra("role", MainActivity.userModel.getRole());
        startActivity(intent);
    }
}
