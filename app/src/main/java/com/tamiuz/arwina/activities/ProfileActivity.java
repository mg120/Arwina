package com.tamiuz.arwina.activities;

import android.app.ProgressDialog;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.tamiuz.arwina.Models.UserLoginModel;
import com.tamiuz.arwina.R;
import com.tamiuz.arwina.networking.ApiClient;
import com.tamiuz.arwina.networking.ApiServiceInterface;
import com.tamiuz.arwina.networking.NetworkAvailable;
import com.tamiuz.arwina.utils.DialogUtil;
import com.tamiuz.arwina.utils.Urls;

import androidx.constraintlayout.widget.ConstraintLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {

    @BindView(R.id.profile_name_txtV_id)
    TextView title_txtV;
    @BindView(R.id.profile_user_imageV_id)
    ImageView user_imageV;
    @BindView(R.id.profile_update_val_txtV_id)
    TextView profile_update_val_txtV;
    @BindView(R.id.profile_products_cardLayout_id)
    ConstraintLayout products_cardLayout;
    @BindView(R.id.profile_ordersNum_val_txtV_id)
    TextView ordersNum_val_txtV;

    private Intent intent;
    private NetworkAvailable networkAvailable;
    private DialogUtil dialogUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);

        if (getIntent().hasExtra("role")) {
            int role = getIntent().getExtras().getInt("role");
            if (role == 0) {
                products_cardLayout.setVisibility(View.GONE);
            } else if (role == 2) {
                products_cardLayout.setVisibility(View.GONE);
            }
        }

        networkAvailable = new NetworkAvailable(this);
        dialogUtil = new DialogUtil();
        if (networkAvailable.isNetworkAvailable()) {
            getProfileData(MainActivity.userModel.getId());
        } else
            Toast.makeText(this, getString(R.string.error_connection), Toast.LENGTH_SHORT).show();
    }

    private void getProfileData(int id) {
        // Show Progress Dialog
        final ProgressDialog dialog = dialogUtil.showProgressDialog(ProfileActivity.this, getString(R.string.loading), false);
        ApiServiceInterface serviceInterface = ApiClient.getClient().create(ApiServiceInterface.class);
        Call<UserLoginModel> call = serviceInterface.profile_Data(id);
        call.enqueue(new Callback<UserLoginModel>() {
            @Override
            public void onResponse(Call<UserLoginModel> call, Response<UserLoginModel> response) {
                dialog.dismiss();
                if (response.body().getMessage()) {
                    MainActivity.userModel.setMyorderscount(response.body().getData().getMyorderscount());
                    title_txtV.setText(response.body().getData().getName());
                    ordersNum_val_txtV.setText(response.body().getData().getMyorderscount() + " " + getString(R.string.order));
                    profile_update_val_txtV.setText(response.body().getData().getName());
                    if (MainActivity.userModel.getImage() != null)
                        Glide.with(ProfileActivity.this)
                                .load(Urls.imagesBase_Url + response.body().getData().getImage())
                                .error(R.drawable.add_user)
                                .fitCenter()
                                .into(user_imageV);
                }
            }

            @Override
            public void onFailure(Call<UserLoginModel> call, Throwable t) {
                t.printStackTrace();
                dialog.dismiss();
            }
        });
    }

    @OnClick(R.id.profile_notifications_imageV_id)
    void goToNotifications() {
        intent = new Intent(ProfileActivity.this, MyNotifications.class);
        startActivity(intent);
    }

    @OnClick(R.id.profile_back_txtV_id)
    void goBack() {
        finish();
    }

    @OnClick(R.id.profile_orders_cardLayout_id)
    void goToOrders() {
        intent = new Intent(ProfileActivity.this, MyOrders.class);
        startActivity(intent);
    }

    @OnClick(R.id.profile_products_cardLayout_id)
    void goToProducts() {
        intent = new Intent(ProfileActivity.this, MyProducts.class);
        startActivity(intent);
    }

    @OnClick(R.id.profile_products_cardLayout_id)
    void goToWallet() {
//        intent = new Intent(ProfileActivity.this, MyProducts.class);
//        startActivity(intent);
    }

    @OnClick(R.id.editProfile_cardLayout_id)
    void updateProfileData() {
        intent = new Intent(ProfileActivity.this, EditProfileActivity.class);
        startActivity(intent);
    }
}
