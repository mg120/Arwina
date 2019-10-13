package com.tamiuz.arwina.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.tamiuz.arwina.Models.NotifationsModel;
import com.tamiuz.arwina.R;
import com.tamiuz.arwina.activities.MainActivity;
import com.tamiuz.arwina.adapters.MyNotificationsAdapter;
import com.tamiuz.arwina.interfaces.RecyclerItemClickListner;
import com.tamiuz.arwina.networking.ApiClient;
import com.tamiuz.arwina.networking.ApiServiceInterface;
import com.tamiuz.arwina.networking.NetworkAvailable;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyNotifications extends AppCompatActivity {

    @BindView(R.id.notifications_recyclerV_id)
    RecyclerView recyclerView;
    @BindView(R.id.notifications_progressBar_id)
    ProgressBar progressBar;
    @BindView(R.id.notifications_noData_txtV_id)
    TextView noData_txtV;
    @BindView(R.id.notifications_title_txtV_id)
    TextView title_txtV;

    private NetworkAvailable networkAvailable;
    MyNotificationsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_notifications);
        ButterKnife.bind(this);

        if (MainActivity.userModel.getRole() == 0){
            title_txtV.setText(getString(R.string.user_notifications));
        } else if (MainActivity.userModel.getRole() == 2){
            title_txtV.setText(getString(R.string.mandoop_notification));
        }
        networkAvailable = new NetworkAvailable(this);
        if (networkAvailable.isNetworkAvailable()){
            getMyNotifications(MainActivity.userModel.getId());
        } else
            Toast.makeText(this, getString(R.string.error_connection), Toast.LENGTH_SHORT).show();
    }

    private void getMyNotifications(int id) {
        ApiServiceInterface serviceInterface = ApiClient.getClient().create(ApiServiceInterface.class);
        Call<NotifationsModel> call = serviceInterface.getNotifications(id);
        call.enqueue(new Callback<NotifationsModel>() {
            @Override
            public void onResponse(Call<NotifationsModel> call, Response<NotifationsModel> response) {
                if (response.body().getMessage()){
                    noData_txtV.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);

                    buildNotificationsRecyclerV(response.body().getData());
                } else {
                    noData_txtV.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<NotifationsModel> call, Throwable t) {
                t.printStackTrace();
                progressBar.setVisibility(View.GONE);
                noData_txtV.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            }
        });
    }

    private void buildNotificationsRecyclerV(List<NotifationsModel.messageData> data) {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        // setAdapter
        adapter = new MyNotificationsAdapter(MyNotifications.this, data);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new RecyclerItemClickListner() {
            @Override
            public void OnItemClick(int position) {
                if (data.get(position).getNotification().contains("طلب جديد")){
                    Intent intent = new Intent(MyNotifications.this, MyOrders.class);
                    startActivity(intent);
                }
            }
        });
    }

    @OnClick(R.id.notifications_back_txtV_id)
    void goBack(){
        finish();
    }
}
