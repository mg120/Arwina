package com.tamiuz.arwina.activities;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.tamiuz.arwina.Models.MakeOrderResponseModel;
import com.tamiuz.arwina.R;
import com.tamiuz.arwina.networking.ApiClient;
import com.tamiuz.arwina.networking.ApiServiceInterface;

public class MandoopNewOrderActivity extends AppCompatActivity {

    @BindView(R.id.order_state_progress_bar_id)
    ProgressBar progressBar;

    private int order_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mandoop_new_order);
        ButterKnife.bind(this);

        if (getIntent().hasExtra("order_id")){
            order_id = getIntent().getExtras().getInt("order_id");
        }
    }


    @OnClick(R.id.okk_btn_btn_id)
    void accept_Order(){
        updateOrder(1, order_id);
    }

    @OnClick(R.id.no_btn_btn_id)
    void reject_Order(){
//        updateOrder(0, order_id);
        finish();
    }

    private void updateOrder(int orderStatus, int order_id) {
        progressBar.setVisibility(View.VISIBLE);
        ApiServiceInterface serviceInterface = ApiClient.getClient().create(ApiServiceInterface.class);
        Call<MakeOrderResponseModel> call = serviceInterface.update_order(order_id, MainActivity.userModel.getId());
        call.enqueue(new Callback<MakeOrderResponseModel>() {
            @Override
            public void onResponse(Call<MakeOrderResponseModel> call, Response<MakeOrderResponseModel> response) {
                progressBar.setVisibility(View.GONE);
                if (response.body().getMessage()) {
                    Toast.makeText(MandoopNewOrderActivity.this, response.body().getData(), Toast.LENGTH_SHORT).show();
                    finish();
                } else
                    Toast.makeText(MandoopNewOrderActivity.this, response.body().getData(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<MakeOrderResponseModel> call, Throwable t) {
                t.printStackTrace();
                progressBar.setVisibility(View.GONE);
            }
        });
    }
}
