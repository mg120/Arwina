package com.tamiuz.arwina.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.tamiuz.arwina.Models.MakeOrderResponseModel;
import com.tamiuz.arwina.Models.OrderProductDataModel;
import com.tamiuz.arwina.R;
import com.tamiuz.arwina.networking.ApiClient;
import com.tamiuz.arwina.networking.ApiServiceInterface;
import com.tamiuz.arwina.networking.NetworkAvailable;
import com.tamiuz.arwina.utils.DialogUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentActivity extends AppCompatActivity {

    @BindView(R.id.receivingPayment__imageV_icon)
    ImageView receivingPayment_imageV;
    @BindView(R.id.visaPayment_imageV_icon)
    ImageView visaPayment_imageV;

    private int payment_type = 0;
    private Intent intent;
    private NetworkAvailable networkAvailable;
    private DialogUtil dialogUtil;
    private OrderProductDataModel orderProductDataModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        ButterKnife.bind(this);

        networkAvailable = new NetworkAvailable(this);
        dialogUtil = new DialogUtil();
        if (getIntent().hasExtra("order_data")) {
            orderProductDataModel = getIntent().getParcelableExtra("order_data");
        }
    }

    @OnClick(R.id.payment_back_txtV_id)
    void goBack() {
        finish();
    }

    @OnClick(R.id.payment_notifications_imageV_id)
    void goToNotifications() {
        if (MainActivity.user_Id != 0) {
            intent = new Intent(PaymentActivity.this, MyNotifications.class);
            startActivity(intent);
        } else {
            startActivity(new Intent(PaymentActivity.this, LogInActivity.class));
        }
    }

    @OnClick(R.id.payment_user_imageV_id)
    void goToProfile() {
        if (MainActivity.user_Id != 0) {
            intent = new Intent(PaymentActivity.this, ProfileActivity.class);
            startActivity(intent);
        } else {
            startActivity(new Intent(PaymentActivity.this, LogInActivity.class));
        }
    }

    @OnClick(R.id.visaPayment_cardlayout_language)
    void visaClicked() {
        payment_type = 1;
        receivingPayment_imageV.setVisibility(View.GONE);
        visaPayment_imageV.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.receivingPayment_cardlayout_language)
    void deliveredClicked() {
        payment_type = 0;
        receivingPayment_imageV.setVisibility(View.VISIBLE);
        visaPayment_imageV.setVisibility(View.GONE);
    }

    @OnClick(R.id.continue_payment_btn_id)
    void conrinuePayment() {
        if (payment_type == 0) {
            if (networkAvailable.isNetworkAvailable()) {
                makeOrder(orderProductDataModel, 10);
            } else {
                Toast.makeText(this, getString(R.string.error_connection), Toast.LENGTH_SHORT).show();
            }
        } else if (payment_type == 1) {
            makeOrder(orderProductDataModel, 0);
        }
    }

    private void makeOrder(OrderProductDataModel order_Data, int add_price){
        // Show Progress Dialog
        final ProgressDialog dialog = dialogUtil.showProgressDialog(PaymentActivity.this, getString(R.string.ordering), false);
        ApiServiceInterface serviceInterface = ApiClient.getClient().create(ApiServiceInterface.class);
        Call<MakeOrderResponseModel> call = serviceInterface.order_product(MainActivity.userModel.getId(), order_Data.getTrader_id(), order_Data.getProduct_id(), order_Data.getDeliver_lat(), order_Data.getDeliver_lng(),
                order_Data.getDeliver_address(), order_Data.getProduct_count(), order_Data.getOrder_price() + add_price, order_Data.getDeliver_company(), order_Data.getOrder_date(), order_Data.getOrder_time(), order_Data.getOther_notes());
        call.enqueue(new Callback<MakeOrderResponseModel>() {
            @Override
            public void onResponse(Call<MakeOrderResponseModel> call, Response<MakeOrderResponseModel> response) {
                dialog.dismiss();
                if (response.body().getMessage()) {
                    if (payment_type == 1){
                        Toast.makeText(PaymentActivity.this, response.body().getData(), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(PaymentActivity.this, VisaPaymentActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(PaymentActivity.this, response.body().getData(), Toast.LENGTH_SHORT).show();
                        finish();
                    }
                } else {
                    Toast.makeText(PaymentActivity.this, response.body().getData(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MakeOrderResponseModel> call, Throwable t) {
                t.printStackTrace();
                dialog.dismiss();
            }
        });
    }
}
