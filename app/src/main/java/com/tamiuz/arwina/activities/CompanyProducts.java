package com.tamiuz.arwina.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.tamiuz.arwina.Models.AllCompaniesModel;
import com.tamiuz.arwina.Models.ProductsModel;
import com.tamiuz.arwina.R;
import com.tamiuz.arwina.adapters.CompanyProductsAdapter;
import com.tamiuz.arwina.networking.ApiClient;
import com.tamiuz.arwina.networking.ApiServiceInterface;
import com.tamiuz.arwina.networking.NetworkAvailable;
import com.tamiuz.arwina.utils.DialogUtil;

import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CompanyProducts extends AppCompatActivity {

    @BindView(R.id.companyProducts_title_txtV_id)
    TextView companyTitle_txtV;
    @BindView(R.id.companyProducts_recyclerV_id)
    RecyclerView products_recyclerV;
    @BindView(R.id.companyProducts_noData_txtV_id)
    TextView noData_txtV;
    @BindView(R.id.companyProducts_progress_id)
    ProgressBar progressBar;

    private AllCompaniesModel.CompanyData companyData;
    private NetworkAvailable networkAvailable;
    private DialogUtil dialogUtil;
    private CompanyProductsAdapter adapter;
    LayoutAnimationController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_products);
        ButterKnife.bind(this);

        networkAvailable = new NetworkAvailable(this);
        dialogUtil = new DialogUtil();
        if (getIntent().hasExtra("item_Data")) {
            companyData = getIntent().getParcelableExtra("item_Data");
            companyTitle_txtV.setText(companyData.getName());
        }

        if (networkAvailable.isNetworkAvailable()) {
            getCompanyProducts(companyData.getId());
        } else
            Toast.makeText(this, getString(R.string.error_connection), Toast.LENGTH_SHORT).show();
    }

    private void getCompanyProducts(int id) {
        ApiServiceInterface serviceInterface = ApiClient.getClient().create(ApiServiceInterface.class);
        Call<ProductsModel> call = serviceInterface.company_Products(id);
        call.enqueue(new Callback<ProductsModel>() {
            @Override
            public void onResponse(Call<ProductsModel> call, Response<ProductsModel> response) {
                if (response.body().getMessage()) {
                    List<ProductsModel.ProductData> productDataList = response.body().getData();
                    products_recyclerV.setVisibility(View.VISIBLE);
                    noData_txtV.setVisibility(View.GONE);
                    buildCompaniesRecycler(productDataList);
                } else {
                    products_recyclerV.setVisibility(View.GONE);
                    noData_txtV.setVisibility(View.VISIBLE);
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<ProductsModel> call, Throwable t) {
                t.printStackTrace();
                progressBar.setVisibility(View.GONE);
                products_recyclerV.setVisibility(View.GONE);
                noData_txtV.setVisibility(View.VISIBLE);
            }
        });
    }

    private void buildCompaniesRecycler(List<ProductsModel.ProductData> productDataList) {
        products_recyclerV.setLayoutManager(new LinearLayoutManager(CompanyProducts.this));
        products_recyclerV.setHasFixedSize(true);
        // setAdapter
        controller = AnimationUtils.loadLayoutAnimation(CompanyProducts.this, R.anim.layout_slide_from_bottom);
        adapter = new CompanyProductsAdapter(CompanyProducts.this, productDataList);
        products_recyclerV.setAdapter(adapter);
        // set Animation to recycler view ...
        products_recyclerV.setLayoutAnimation(controller);
        products_recyclerV.getAdapter().notifyDataSetChanged();
        products_recyclerV.scheduleLayoutAnimation();
    }

    @OnClick(R.id.companyProducts_back_txtV_id)
    void goBack() {
        finish();
    }

    @OnClick(R.id.companyProducts_notifications_imageV_id)
    void goToNotifications() {
        if (MainActivity.user_Id != 0) {
            Intent intent = new Intent(CompanyProducts.this, MyNotifications.class);
            startActivity(intent);
        } else {
            startActivity(new Intent(CompanyProducts.this, LogInActivity.class));
        }
    }

    @OnClick(R.id.companyProducts_user_imageV_id)
    void goToProfie(){
        if (MainActivity.user_Id != 0) {
            Intent intent = new Intent(CompanyProducts.this, ProfileActivity.class);
            intent.putExtra("role", MainActivity.userModel.getRole());
            startActivity(intent);
        } else {
            startActivity(new Intent(CompanyProducts.this, LogInActivity.class));
        }
    }
}
