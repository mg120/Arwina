package com.tamiuz.arwina.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.tamiuz.arwina.adapters.MyProductsAdapter;
import com.tamiuz.arwina.Models.ForgetPasswordModel;
import com.tamiuz.arwina.Models.ProductsModel;
import com.tamiuz.arwina.R;
import com.tamiuz.arwina.interfaces.MyProductItemClickLisnter;
import com.tamiuz.arwina.networking.ApiClient;
import com.tamiuz.arwina.networking.ApiServiceInterface;
import com.tamiuz.arwina.networking.NetworkAvailable;

import java.util.List;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyProducts extends AppCompatActivity {

    @BindView(R.id.products_recyclerV_id)
    RecyclerView recyclerView;
    @BindView(R.id.products_progressBar_id)
    ProgressBar progressBar;
    @BindView(R.id.products_noData_txtV_id)
    TextView noData_txtV;
    @BindView(R.id.profile_swipeRefreshLayout)
    SwipeRefreshLayout swipe_layout;

    private NetworkAvailable networkAvailable;
    private Intent intent ;
    private MyProductsAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_products);
        ButterKnife.bind(this);

        networkAvailable = new NetworkAvailable(this);
        if (!networkAvailable.isNetworkAvailable())
            Toast.makeText(this, getString(R.string.error_connection), Toast.LENGTH_SHORT).show();
        else
            getMyProducts(MainActivity.userModel.getId());

        swipe_layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipe_layout.setRefreshing(true);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (MyProducts.this != null) {
                            getMyProducts(MainActivity.userModel.getId());
                            swipe_layout.setRefreshing(false);
                        }
                    }
                }, 1000);
            }
        });
        swipe_layout.setColorSchemeResources(
                R.color.refresh_progress_1,
                R.color.refresh_progress_2,
                R.color.refresh_progress_3);
    }

    private void getMyProducts(int id) {
        ApiServiceInterface serviceInterface = ApiClient.getClient().create(ApiServiceInterface.class);
        Call<ProductsModel> call = serviceInterface.getUser_Products(id);
        call.enqueue(new Callback<ProductsModel>() {
            @Override
            public void onResponse(Call<ProductsModel> call, Response<ProductsModel> response) {
                if (response.body().getMessage()){
                    noData_txtV.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);

                    buildProductsRecycler(response.body().getData());
                } else {
                    noData_txtV.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<ProductsModel> call, Throwable t) {
                t.printStackTrace();
                progressBar.setVisibility(View.GONE);
                noData_txtV.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            }
        });
    }

    private void buildProductsRecycler(List<ProductsModel.ProductData> data) {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        // setAdapter
        adapter = new MyProductsAdapter(MyProducts.this, data);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new MyProductItemClickLisnter() {
            @Override
            public void OnDeleteClick(int position) {
                progressBar.setVisibility(View.VISIBLE);
                ApiServiceInterface serviceInterface = ApiClient.getClient().create(ApiServiceInterface.class);
                Call<ForgetPasswordModel> call = serviceInterface.delete_product(data.get(position).getId() , MainActivity.userModel.getId());
                call.enqueue(new Callback<ForgetPasswordModel>() {
                    @Override
                    public void onResponse(Call<ForgetPasswordModel> call, Response<ForgetPasswordModel> response) {
                        progressBar.setVisibility(View.GONE);
                        if (response.body().getMessage()){
                            adapter.removeItem(position);
                            if (data.size() == 0) {
                                recyclerView.setVisibility(View.GONE);
                                noData_txtV.setVisibility(View.VISIBLE);
                            }
                            Toast.makeText(MyProducts.this, response.body().getData(), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MyProducts.this, response.body().getData(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ForgetPasswordModel> call, Throwable t) {
                        t.printStackTrace();
                        progressBar.setVisibility(View.GONE);
                    }
                });
            }

            @Override
            public void OnEditClick(int position) {
                Intent intent = new Intent(MyProducts.this, EditProductActivity.class);
                intent.putExtra("product_data" , data.get(position));
                startActivity(intent);
            }
        });
    }

    @OnClick(R.id.myPorducts_back_txtV_id)
    void goBack(){
        finish();
    }

    @OnClick(R.id.myPorducts_notifications_imageV_id)
    void goToNotifications(){
        intent = new Intent(MyProducts.this, MyNotifications.class);
        startActivity(intent);
    }

    @OnClick(R.id.add_product_button_id)
    void addProduct(){
        intent = new Intent(MyProducts.this, AddProductActivity.class);
        intent.putExtra("role" , MainActivity.userModel.getRole());
        startActivity(intent);
    }
}
