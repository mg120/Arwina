package com.tamiuz.arwina.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.tamiuz.arwina.Models.DeliversResponseModel;
import com.tamiuz.arwina.R;
import com.tamiuz.arwina.networking.ApiClient;
import com.tamiuz.arwina.networking.ApiServiceInterface;
import com.tamiuz.arwina.networking.NetworkAvailable;
import com.tamiuz.arwina.utils.Urls;

import java.util.List;

public class SelectMandoopActivity extends AppCompatActivity implements OnMapReadyCallback, LocationListener {

    @BindView(R.id.selectMandoop_mapView_id)
    MapView mapView;

    private GoogleMap googleMap;

    private final String TAG = this.getClass().getSimpleName();
    public static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    public static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int location_permission_request_code = 550;
    private static final int Dialog_Error_Request = 120;
    private static final float default_Zoom = 13f;
    private static final float MIN_DISTANCE = 1000;
    private static final long MIN_TIME = 400;
    String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
    private LocationManager locationManager;
    private boolean mLocationPermissionGrated = false;
    Marker marker;
    private int marker_tag;
    private Intent intent;
    private NetworkAvailable networkAvailable;
    private Dialog myDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_mandoop);
        ButterKnife.bind(this);

        networkAvailable = new NetworkAvailable(this);
        myDialog = new Dialog(this);
        if (mapView != null) {
            mapView.onCreate(null);
            mapView.onResume();
            mapView.getMapAsync(this);
        }

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, permissions, location_permission_request_code);
        }
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME, MIN_DISTANCE, this);
        if (isSericeOk()) {
            getLocationPermission();
        }
    }

    private void getAllMandoops() {
        int height = 36;
        int width = 36;
        marker_tag = 0;
        BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.map2);
        Bitmap b = bitmapdraw.getBitmap();
        final Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);


        ApiServiceInterface serviceInterface = ApiClient.getClient().create(ApiServiceInterface.class);
        Call<DeliversResponseModel> call = serviceInterface.get_Delivers();
        call.enqueue(new Callback<DeliversResponseModel>() {
            @Override
            public void onResponse(Call<DeliversResponseModel> call, Response<DeliversResponseModel> response) {
                if (response.body().getMessage()){
                    List<DeliversResponseModel.DeliverData> list = response.body().getData();

                    for (int i = 0; i < list.size(); i++) {
                        Log.i("nname:: ", list.get(i).getName());
                        LatLng latLng1 = new LatLng(Double.parseDouble(list.get(i).getLat()), Double.parseDouble(list.get(i).getLng()));
                        marker = googleMap.addMarker(new MarkerOptions().position(latLng1).icon(BitmapDescriptorFactory.fromBitmap(smallMarker)).title(list.get(i).getName()));
                        marker.setTag(marker_tag++);
                    }
                    googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                        @Override
                        public void onInfoWindowClick(Marker marker) {
                            Integer position = (Integer) marker.getTag();
                           show_popup(position, list.get(position));
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<DeliversResponseModel> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    @OnClick(R.id.selectMandoop_back_txtV_id)
    void goBack(){
        finish();
    }

    @OnClick(R.id.selectMandoop_notifications_imageV_id)
    void goToNotification(){
        intent = new Intent(SelectMandoopActivity.this, MyNotifications.class);
        startActivity(intent);
    }

    @OnClick(R.id.selectMandoop_user_imageV_id)
    void goToProfile(){
        intent = new Intent(SelectMandoopActivity.this, ProfileActivity.class);
        startActivity(intent);
    }

    private boolean isSericeOk() {
        Log.i(TAG, "is Service Ok Started ...");
        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this);
        if (available == ConnectionResult.SUCCESS) {
            Log.i(TAG, "Service Ok is OK");
            return true;
        } else if (GoogleApiAvailability.getInstance().isUserResolvableError(available)) {
            Log.i(TAG, "Service Error occured but we can fix it");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(this, available, Dialog_Error_Request);
            dialog.show();
            return false;
        } else {
            Toast.makeText(this, "You canot make map request", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    private void getLocationPermission() {
        Log.i(TAG, "onLocation Permissions Started ...");
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(), FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this.getApplicationContext(), COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mLocationPermissionGrated = true;
            } else {
                ActivityCompat.requestPermissions(this, permissions, location_permission_request_code);
            }
        } else {
            ActivityCompat.requestPermissions(this, permissions, location_permission_request_code);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap1) {
        MapsInitializer.initialize(this);
        googleMap = googleMap1;
        googleMap1.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        if (mLocationPermissionGrated) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            googleMap.setMyLocationEnabled(true);
        }
        if (networkAvailable.isNetworkAvailable())
            getAllMandoops();
    }

    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            try {
                moveCamera(new LatLng(location.getLatitude(), location.getLongitude()), default_Zoom, "My Location");
            } catch (NullPointerException ex) {
                ex.printStackTrace();
            }
            locationManager.removeUpdates(this);
        }
    }

    private void moveCamera(LatLng latLng, float zoom, String title) {
        Log.i(TAG, "moveCamera called");
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));

        if (!title.equals("My Location")) {
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng)
                    .title(title);
            googleMap.addMarker(markerOptions);
        }
    }

    private void show_popup(int position, final DeliversResponseModel.DeliverData deliverData) {
        ImageView family_imageV, close_imageV;
        TextView family_name_txtV, deliver_phone_txtV;
        RatingBar ratingBar;
        ProgressBar progressBar;
        Button select_deliver_btn;

        myDialog.setCancelable(true);
        myDialog.setContentView(R.layout.deliver_pop_up);
        ratingBar = myDialog.findViewById(R.id.deliver_ratingBar_id);
        family_imageV = myDialog.findViewById(R.id.deliver_imageV_id);
        family_name_txtV = myDialog.findViewById(R.id.deliver_name_txtV_id);
        deliver_phone_txtV = myDialog.findViewById(R.id.deliver_phone_txtV_id);
        close_imageV = myDialog.findViewById(R.id.close_imageV_id);
        progressBar = myDialog.findViewById(R.id.deliver_image_progress_id);
        select_deliver_btn = myDialog.findViewById(R.id.select_deliver_btn_id);

        close_imageV.setImageDrawable(null);
        // Set Data to Views...
        family_name_txtV.setText(String.valueOf(deliverData.getName()));
        deliver_phone_txtV.setText(String.valueOf(deliverData.getPhone()));
//        Glide.with(this)
//                .load(Urls.imagesBase_Url + deliverData.getImage())
//                .centerCrop()
//                .into(family_imageV);

        Glide.with(this)
                .load(Urls.imagesBase_Url + deliverData.getImage())
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        // image ready, hide progress now
                        progressBar.setVisibility(View.GONE);
                        return false;   // return false if you want Glide to handle everything else.
                    }
                })
                .centerCrop()   // cache both original & resized image
                .into(family_imageV);
        select_deliver_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                close_imageV.setImageResource(R.drawable.check);
                Intent intent = new Intent();
                intent.putExtra("driver_id", deliverData.getId());
                intent.putExtra("driver_name", deliverData.getName());
                intent.putExtra("driver_lat", marker.getPosition().latitude);
                intent.putExtra("driver_lng", marker.getPosition().longitude);
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });

        myDialog.getWindow().setGravity(Gravity.BOTTOM);
        myDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
