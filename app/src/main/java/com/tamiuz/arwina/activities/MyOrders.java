package com.tamiuz.arwina.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.tamiuz.arwina.adapters.OrdersPagerAdapter;
import com.tamiuz.arwina.Models.OrdersModel;
import com.tamiuz.arwina.R;
import com.tamiuz.arwina.networking.ApiClient;
import com.tamiuz.arwina.networking.ApiServiceInterface;
import com.tamiuz.arwina.networking.NetworkAvailable;
import com.tamiuz.arwina.utils.DialogUtil;

import java.util.ArrayList;
import java.util.List;

import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyOrders extends AppCompatActivity {

    @BindView(R.id.no_orders_txtV_id)
    TextView no_orders_txtV;
    @BindView(R.id.orders_progressBar_id)
    ProgressBar progressBar;
    TabLayout tabLayout;
    TabItem process_tab;
    TabItem waiting_tab;
    TabItem completed_tab;
    @BindView(R.id.viewPager_id)
    ViewPager orders_ViewPager;
    @BindView(R.id.orders_layout_id)
    LinearLayout orders_layout;

    private NetworkAvailable networkAvailable;
    private DialogUtil dialogUtil;
    OrdersPagerAdapter pagerAdapter;

    private List<OrdersModel.OrderData> processing_list;
    private List<OrdersModel.OrderData> waiting_list;
    private List<OrdersModel.OrderData> completed_list;
    private List<OrdersModel.OrderData> orders_data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_orders);
        ButterKnife.bind(this);

        dialogUtil = new DialogUtil();
        networkAvailable = new NetworkAvailable(this);
        processing_list = new ArrayList<>();
        completed_list = new ArrayList<>();

        tabLayout = findViewById(R.id.orders_tabLayout_id);
        process_tab = findViewById(R.id.process_tab_id);
        completed_tab = findViewById(R.id.completed_tab_id);

        if (networkAvailable.isNetworkAvailable()){
            progressBar.setVisibility(View.VISIBLE);
            ApiServiceInterface serviceInterface = ApiClient.getClient().create(ApiServiceInterface.class);
            Call<OrdersModel> call = serviceInterface.getUser_Orders(MainActivity.userModel.getId());
            call.enqueue(new Callback<OrdersModel>() {
                @Override
                public void onResponse(Call<OrdersModel> call, Response<OrdersModel> response) {
                    OrdersModel orderModel = response.body();
                    if (orderModel.getMessage()) {
                        orders_layout.setVisibility(View.VISIBLE);
                        no_orders_txtV.setVisibility(View.GONE);

                        orders_data = response.body().getData();
                        for (int i= 0; i< orders_data.size(); i++) {
                            if (orders_data.get(i).getStatus() == 0 || orders_data.get(i).getStatus() == 1 || orders_data.get(i).getStatus() == 2){
                                processing_list.add(orders_data.get(i));
                            } else {
                                completed_list.add(orders_data.get(i));
                            }
                        }
                        Log.i("process_list: " , processing_list.size() +"");
                        Log.i("process_list: " , completed_list.size() +"");
                        progressBar.setVisibility(View.GONE);
                        pagerAdapter = new OrdersPagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount(), processing_list, completed_list);
                        orders_ViewPager.setAdapter(pagerAdapter);
                        orders_ViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
                        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                            @Override
                            public void onTabSelected(TabLayout.Tab tab) {
                                orders_ViewPager.setCurrentItem(tab.getPosition());
                            }

                            @Override
                            public void onTabUnselected(TabLayout.Tab tab) {

                            }

                            @Override
                            public void onTabReselected(TabLayout.Tab tab) {

                            }
                        });

                    } else {
                        orders_layout.setVisibility(View.GONE);
                        no_orders_txtV.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onFailure(Call<OrdersModel> call, Throwable t) {
                    t.printStackTrace();
                    progressBar.setVisibility(View.GONE);
                    orders_layout.setVisibility(View.GONE);
                    no_orders_txtV.setVisibility(View.VISIBLE);
                }
            });
        } else
            Toast.makeText(this, getString(R.string.error_connection), Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.orders_back_txtV_id)
    void goBack(){
        finish();
    }
}
