package com.tamiuz.arwina.activities;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.webkit.URLUtil;
import android.widget.TextView;
import android.widget.Toast;

import com.tamiuz.arwina.Models.SettingsInfoModel;
import com.tamiuz.arwina.R;
import com.tamiuz.arwina.networking.ApiClient;
import com.tamiuz.arwina.networking.ApiServiceInterface;
import com.tamiuz.arwina.networking.NetworkAvailable;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AboutUsActivity extends AppCompatActivity {

    @BindView(R.id.aboutContent_txtV_id)
    TextView aboutApp_txtV;

    private NetworkAvailable networkAvailable;
    String faceBook_url, youtube_url, twitter_url, instagram_url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        ButterKnife.bind(this);

        networkAvailable = new NetworkAvailable(this);
        if (getIntent().hasExtra("about_data")) {
            String about_txt = getIntent().getStringExtra("about_data");
            faceBook_url = getIntent().getStringExtra("faceBook_page");
            youtube_url = getIntent().getStringExtra("youtube_page");
            instagram_url = getIntent().getStringExtra("instagram_page");
            twitter_url = getIntent().getStringExtra("twitter_page");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                aboutApp_txtV.setText(Html.fromHtml(about_txt, Html.FROM_HTML_MODE_COMPACT));
            } else {
                aboutApp_txtV.setText(Html.fromHtml(about_txt));
            }
        }

//        if (networkAvailable.isNetworkAvailable()){
//            getAboutData();
//        } else
//            Toast.makeText(this, getString(R.string.error_connection), Toast.LENGTH_SHORT).show();
    }

    private void getAboutData() {
        ApiServiceInterface serviceInterface = ApiClient.getClient().create(ApiServiceInterface.class);
        Call<SettingsInfoModel> call = serviceInterface.getSettingsInfo("ar");
        call.enqueue(new Callback<SettingsInfoModel>() {
            @Override
            public void onResponse(Call<SettingsInfoModel> call, Response<SettingsInfoModel> response) {
                SettingsInfoModel settingsInfoModel = response.body();
            }

            @Override
            public void onFailure(Call<SettingsInfoModel> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    @OnClick(R.id.aboutApp_back_txtV_id)
    void goBack() {
        finish();
    }

    @OnClick(R.id.aboutApp_notifications_imageV_id)
    void goToNotifications() {
        if (MainActivity.user_Id != 0) {
            Intent intent = new Intent(AboutUsActivity.this, MyNotifications.class);
            startActivity(intent);
        } else {
            startActivity(new Intent(AboutUsActivity.this, LogInActivity.class));
        }
    }

    @OnClick(R.id.aboutApp_user_imageV_id)
    void goToProfile() {
        if (MainActivity.user_Id != 0) {
            Intent intent = new Intent(AboutUsActivity.this, ProfileActivity.class);
            intent.putExtra("role", MainActivity.userModel.getRole());
            startActivity(intent);
        } else {
            startActivity(new Intent(AboutUsActivity.this, LogInActivity.class));
        }
    }

    @OnClick(R.id.about_youtube_imageV_id)
    void youtubeClicked() {
        openWebPage(AboutUsActivity.this, youtube_url);
    }

    @OnClick(R.id.about_instgram_imageV_id)
    void instgramClicked() {
        openWebPage(AboutUsActivity.this, instagram_url);
    }

    @OnClick(R.id.about_twetter_imageV_id)
    void twitterClicked() {
        openWebPage(AboutUsActivity.this, twitter_url);
    }

    @OnClick(R.id.about_facebook_imageV_id)
    void faceBookClicked() {
        openWebPage(AboutUsActivity.this, faceBook_url);
    }

    public static void openWebPage(Context context, String url) {
        try {
            if (!URLUtil.isValidUrl(url)) {
                Toast.makeText(context, " This is not a valid link", Toast.LENGTH_LONG).show();
            } else {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                context.startActivity(intent);
            }
        } catch (ActivityNotFoundException e) {
            Toast.makeText(context, " You don't have any browser to open web page", Toast.LENGTH_LONG).show();
        }
    }
}
