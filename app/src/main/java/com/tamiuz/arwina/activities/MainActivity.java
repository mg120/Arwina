package com.tamiuz.arwina.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;

import androidx.annotation.NonNull;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.google.android.material.navigation.NavigationView;

import androidx.appcompat.widget.SearchView;
import androidx.core.view.GravityCompat;
import androidx.core.view.MenuItemCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.tamiuz.arwina.Models.AllCompaniesModel;
import com.tamiuz.arwina.Models.SettingsInfoModel;
import com.tamiuz.arwina.Models.UnReadMesagesResponseModel;
import com.tamiuz.arwina.Models.UserModel;
import com.tamiuz.arwina.R;
import com.tamiuz.arwina.interfaces.RecyclerItemClickListner;
import com.tamiuz.arwina.adapters.MainRecyclerAdapter;
import com.tamiuz.arwina.networking.ApiClient;
import com.tamiuz.arwina.networking.ApiServiceInterface;
import com.tamiuz.arwina.networking.NetworkAvailable;
import com.tamiuz.arwina.utils.SharedPreferenceManager;
import com.tamiuz.arwina.utils.Urls;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener {

    @BindView(R.id.nv)
    NavigationView navigationView;
    @BindView(R.id.main_noData_txtV_id)
    TextView no_data_txtV;
    @BindView(R.id.main_progress_id)
    ProgressBar main_progressBar;
    @BindView(R.id.mainCompanies_recyclerV_id)
    RecyclerView main_recyclerV;
    @BindView(R.id.banner_slider)
    SliderLayout sliderLayout;

    private SharedPreferenceManager preferenceManager;
    private DrawerLayout drawer;
    private NetworkAvailable networkAvailable;
    private MainRecyclerAdapter adapter;
    public static UserModel userModel;
    List<SettingsInfoModel.InfoDataModel> infoDataModelList;
    List<AllCompaniesModel.CompanyData> companyDataList;
    List<AllCompaniesModel.CompanyData> sliderList;
    LayoutAnimationController controller;

    private int cart_count = 0;
    public static int user_Id = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        // Tool Bar
        Toolbar toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        // Tint progressBar...
        main_progressBar.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.MULTIPLY);
        networkAvailable = new NetworkAvailable(this);
        //   get data from shared preferences ...
        preferenceManager = new SharedPreferenceManager(MainActivity.this, SharedPreferenceManager.PREFERENCE_NAME);

        if (getIntent().hasExtra("user_data")) {
            userModel = getIntent().getParcelableExtra("user_data");
            user_Id = userModel.getId();
            Log.i("iid: ", user_Id + "");
            getUnReadNotifications(userModel.getId());
        }
        Log.i("iid: ", user_Id + "");
        companyDataList = new ArrayList<>();
        buildCompaniesRecycler(companyDataList);
        if (networkAvailable.isNetworkAvailable()) {
            getCompaniesData();
            getSettingInfoo();
        } else {
            Toast.makeText(this, getString(R.string.error_connection), Toast.LENGTH_LONG).show();
        }
        NavigationView navigationView = findViewById(R.id.nv);
        if (user_Id == 0) {
            Menu nav_Menu = navigationView.getMenu();
            nav_Menu.findItem(R.id.nav_logOut).setVisible(false);
        }
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void getUnReadNotifications(int id) {
        main_progressBar.setVisibility(View.VISIBLE);
        ApiServiceInterface serviceInterface = ApiClient.getClient().create(ApiServiceInterface.class);
        Call<UnReadMesagesResponseModel> call = serviceInterface.unReadNotifications(id);
        call.enqueue(new Callback<UnReadMesagesResponseModel>() {
            @Override
            public void onResponse(Call<UnReadMesagesResponseModel> call, Response<UnReadMesagesResponseModel> response) {
                main_progressBar.setVisibility(View.GONE);
                if (response.body().getMessage()) {
                    cart_count = response.body().getData();
                }
            }

            @Override
            public void onFailure(Call<UnReadMesagesResponseModel> call, Throwable t) {
                t.printStackTrace();
                main_progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void getSettingInfoo() {
        ApiServiceInterface serviceInterface = ApiClient.getClient().create(ApiServiceInterface.class);
        Call<SettingsInfoModel> call = serviceInterface.getSettingsInfo("ar");
        call.enqueue(new Callback<SettingsInfoModel>() {
            @Override
            public void onResponse(Call<SettingsInfoModel> call, Response<SettingsInfoModel> response) {
                if (response.body().getMessage()) {
                    infoDataModelList = response.body().getData().getInfo();
                }
            }

            @Override
            public void onFailure(Call<SettingsInfoModel> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void getCompaniesData() {
        main_progressBar.setVisibility(View.VISIBLE);
        ApiServiceInterface serviceInterface = ApiClient.getClient().create(ApiServiceInterface.class);
        Call<AllCompaniesModel> call = serviceInterface.getAllCompanies();
        call.enqueue(new Callback<AllCompaniesModel>() {
            @Override
            public void onResponse(Call<AllCompaniesModel> call, Response<AllCompaniesModel> response) {
                if (response.body().getMessage()) {
                    sliderList = response.body().getData().getSliders();
                    for (int x = 0; x < response.body().getData().getCompanies().size(); x++) {
                        if (user_Id != 0) {
                            if (userModel.getId() != response.body().getData().getCompanies().get(x).getId())
                                companyDataList.add(response.body().getData().getCompanies().get(x));
                        } else {
                            companyDataList.add(response.body().getData().getCompanies().get(x));
                        }
                    }

                    for (int i = 0; i < sliderList.size(); i++) {
                        TextSliderView textSliderView = new TextSliderView(MainActivity.this);
                        // initialize a SliderLayout
                        Log.i("imagee: ", Urls.imagesBase_Url + sliderList.get(i).getImage());
                        textSliderView.image(Urls.imagesBase_Url + sliderList.get(i).getImage())
                                .setScaleType(BaseSliderView.ScaleType.Fit)
                                .setOnSliderClickListener(MainActivity.this);
                        //add your extra information
                        textSliderView.bundle(new Bundle());
                        sliderLayout.addSlider(textSliderView);
                    }
                    sliderLayout.setPresetTransformer(SliderLayout.Transformer.Accordion);
                    sliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
                    sliderLayout.setCustomAnimation(new DescriptionAnimation());
                    sliderLayout.setDuration(4000);
                    sliderLayout.addOnPageChangeListener(MainActivity.this);

                    Log.i("cat_list", companyDataList.size() + "");
                    main_recyclerV.setVisibility(View.VISIBLE);
                    no_data_txtV.setVisibility(View.GONE);
                    adapter.notifyDataSetChanged();
                } else {
                    no_data_txtV.setVisibility(View.VISIBLE);
                    main_recyclerV.setVisibility(View.GONE);
                }
                main_progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<AllCompaniesModel> call, Throwable t) {
                t.printStackTrace();
                main_progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void buildCompaniesRecycler(List<AllCompaniesModel.CompanyData> companyDataList) {
        main_recyclerV.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        main_recyclerV.setHasFixedSize(true);
        // setAdapter
        controller = AnimationUtils.loadLayoutAnimation(MainActivity.this, R.anim.layout_slide_from_bottom);
        adapter = new MainRecyclerAdapter(MainActivity.this, companyDataList);
        main_recyclerV.setAdapter(adapter);

        // set Animation to recycler view ...
        main_recyclerV.setLayoutAnimation(controller);
        main_recyclerV.getAdapter().notifyDataSetChanged();
        main_recyclerV.scheduleLayoutAnimation();

        adapter.setOnItemClickListener(new RecyclerItemClickListner() {
            @Override
            public void OnItemClick(int position) {
                Intent intent = new Intent(MainActivity.this, CompanyProducts.class);
                intent.putExtra("item_Data", companyDataList.get(position));
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();
        Intent intent = null;
        if (id == R.id.nav_payment) {
            intent = new Intent(MainActivity.this, PaymentActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_aboutApp) {
            intent = new Intent(MainActivity.this, AboutUsActivity.class);
            intent.putExtra("about_data", infoDataModelList.get(0).getArabout());
            intent.putExtra("faceBook_page", infoDataModelList.get(0).getFacebook());
            intent.putExtra("youtube_page", infoDataModelList.get(0).getYoutube());
            intent.putExtra("instagram_page", infoDataModelList.get(0).getInstagram());
            intent.putExtra("twitter_page", infoDataModelList.get(0).getTwitter());
            startActivity(intent);
        } else if (id == R.id.nav_termsAndConditions) {
            intent = new Intent(MainActivity.this, ConditionsActivity.class);
            intent.putExtra("conditions", infoDataModelList.get(0).getArconditions());
            startActivity(intent);

        } else if (id == R.id.nav_privacyPolicy) {
            intent = new Intent(MainActivity.this, PrivacyPolicyActivity.class);
            intent.putExtra("privacy_data", infoDataModelList.get(0).getArprivacy());
            startActivity(intent);
        } else if (id == R.id.nav_contactUs) {
            if (user_Id != 0) {
                intent = new Intent(MainActivity.this, ContactUsActivity.class);
                startActivity(intent);
            } else {
                startActivity(new Intent(MainActivity.this, LogInActivity.class));
            }
        } else if (id == R.id.nav_aboutUs) {
            intent = new Intent(MainActivity.this, AboutCompanyActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_shareApp) {
            if (user_Id != 0) {
                intent = new Intent(MainActivity.this, ShareAppActivity.class);
                Log.i("codecode:: ", userModel.getPromotioncode());
                intent.putExtra("code", userModel.getPromotioncode());
                startActivity(intent);
            } else {
                startActivity(new Intent(MainActivity.this, LogInActivity.class));
            }
        } else if (id == R.id.nav_logOut) {
            logoutOfApp();
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_option_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.search_item);
        MenuItem notification_Item = menu.findItem(R.id.notification_item);
        notification_Item.setIcon(Converter.convertLayoutToImage(MainActivity.this, cart_count, R.drawable.ic_notifications_black_24dp));
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // put searchable items in ArrayList ...
                newText = newText.toLowerCase();

                List<AllCompaniesModel.CompanyData> newlist = new ArrayList<>();
                for (AllCompaniesModel.CompanyData item : companyDataList) {
                    if (item.getName().toLowerCase().contains(newText)) {
                        newlist.add(item);
                    }
                }
                adapter = new MainRecyclerAdapter(MainActivity.this, newlist);
                main_recyclerV.setAdapter(adapter);
                return true;
            }
        });
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        invalidateOptionsMenu();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        Intent intent = null;
        switch (item.getItemId()) {
            case R.id.notification_item:
                // do something
                cart_count = 0;
                if (user_Id != 0) {
                    intent = new Intent(MainActivity.this, MyNotifications.class);
                    startActivity(intent);
                } else {
                    startActivity(new Intent(MainActivity.this, LogInActivity.class));
                }
                return true;
            case R.id.user_item:
                // do something
                if (user_Id != 0) {
                    intent = new Intent(MainActivity.this, ProfileActivity.class);
                    intent.putExtra("role", userModel.getRole());
                    startActivity(intent);
                } else {
                    startActivity(new Intent(MainActivity.this, LogInActivity.class));
                }
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void logoutOfApp() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage(getString(R.string.outofApp))
                .setCancelable(false)
                .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //super.onBackPressed();
                        dialogInterface.dismiss();
                        //-------------------------------------------------------
                        preferenceManager.removeValue(SharedPreferenceManager.USER_DATA);
                        startActivity(new Intent(MainActivity.this, LogInActivity.class));
                        finish();
                        // -------------------------------------------------------
                    }
                })
                .setNegativeButton(getString(R.string.dismiss), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).create().show();
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
