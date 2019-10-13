package com.tamiuz.arwina.activities;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.isapanah.awesomespinner.AwesomeSpinner;
import com.tamiuz.arwina.Models.MakeOrderResponseModel;
import com.tamiuz.arwina.Models.OrderProductDataModel;
import com.tamiuz.arwina.Models.ProductsModel;
import com.tamiuz.arwina.R;
import com.tamiuz.arwina.networking.ApiClient;
import com.tamiuz.arwina.networking.ApiServiceInterface;
import com.tamiuz.arwina.networking.NetworkAvailable;
import com.tamiuz.arwina.utils.DialogUtil;
import com.tamiuz.arwina.utils.Urls;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class OrderActivity extends AppCompatActivity {

    @BindView(R.id.order_title_txtV_id)
    TextView title_txtV;
    @BindView(R.id.orderProduct_name_txtV_id)
    TextView orderProduct_name_txtV;
    @BindView(R.id.orderProduct_price_txtV_id)
    TextView orderProduct_price_txtV;
    @BindView(R.id.orderProduct_quantity_txtV_id)
    TextView orderProduct_quantity_txtV;
    @BindView(R.id.orderProduct_address_txtV_id)
    TextView orderProduct_address_txtV;
    @BindView(R.id.orderProduct_desc_txtV_id)
    TextView orderProduct_desc_txtV;
    @BindView(R.id.orderProduct_imageV_id)
    ImageView orderProduct_imageV;
    @BindView(R.id.discount_coupon_ed_id)
    EditText discount_coupon_ed;
    @BindView(R.id.product_quantity_val_txtV_id)
    TextView count_txtV;
    @BindView(R.id.totalPrice_val_txtV_id)
    TextView totalPrice_val_txtV;
    @BindView(R.id.address_val_txtV_id)
    TextView address_val_txtV;
    @BindView(R.id.date_val_txtV_id)
    TextView date_val_txtV;
    @BindView(R.id.time_val_txtV_id)
    TextView time_val_txtV;
    @BindView(R.id.notes_ed_id)
    EditText notes_ed;
    @BindView(R.id.order_image_progress_id)
    ProgressBar progressBar;
    @BindView(R.id.selected_mandoop_txtV_id)
    TextView selected_Mandoop_txtV;

    private Intent intent;
    private int PLACE_PICKER_REQUEST = 110;
    private int mandoop_request_code = 10;
    double latitude, longtitude = 0.0;
    String address, city, state, country, knownName, mohafza, mntqa, other_notes, discount_coupon = "";
    Geocoder geocoder;
    List<Address> addresses;
    private ProductsModel.ProductData productData;
    private NetworkAvailable networkAvailable;
    private DialogUtil dialogUtil;
    private Calendar myCalendar;
    private int deliver_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        ButterKnife.bind(this);

        networkAvailable = new NetworkAvailable(this);
        dialogUtil = new DialogUtil();
        myCalendar = Calendar.getInstance();
        if (getIntent().hasExtra("product_data")) {
            productData = getIntent().getParcelableExtra("product_data");
            title_txtV.setText(productData.getTitle());
            orderProduct_name_txtV.setText(productData.getTitle());
            orderProduct_address_txtV.setText(productData.getAddress());
            orderProduct_price_txtV.setText(String.valueOf(productData.getPrice()) + getString(R.string.ryal_saudi));
            totalPrice_val_txtV.setText(String.valueOf(productData.getPrice()) + getString(R.string.ryal_saudi));
            orderProduct_quantity_txtV.setText(String.valueOf(productData.getQty()) + getString(R.string.packag));
            orderProduct_desc_txtV.setText(productData.getDesc());
            Log.i("imagee: ", productData.getImage());
            Glide.with(this)
                    .load(Urls.imagesBase_Url + productData.getImage())
                    .placeholder(R.drawable.product)
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
                    .diskCacheStrategy(DiskCacheStrategy.ALL)   // cache both original & resized image
                    .into(orderProduct_imageV);
        }
    }

    @OnClick(R.id.plus_imageV_id)
    void setPlus_imageV() {
        int item_count = Integer.parseInt(count_txtV.getText().toString());
        item_count++;
        count_txtV.setText(String.valueOf(item_count));
        totalPrice_val_txtV.setText(String.valueOf(item_count * productData.getPrice()) + getString(R.string.ryal_saudi));
    }

    @OnClick(R.id.minus_imageV_id)
    void setMinus_imageV() {
        int item_count = Integer.parseInt(count_txtV.getText().toString());
        if (item_count > 1) {
            Log.i(" count", item_count + "");
            item_count--;
            count_txtV.setText(String.valueOf(item_count));
            totalPrice_val_txtV.setText(String.valueOf(item_count * productData.getPrice()) + getString(R.string.ryal_saudi));
        }
    }

    @OnClick(R.id.address_val_txtV_id)
    void getAddress() {
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        try {
            startActivityForResult(builder.build(OrderActivity.this), PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.order_deliverCompany_layout_id)
    void selectMandoop() {
        intent = new Intent(OrderActivity.this, SelectMandoopActivity.class);
        startActivityForResult(intent, mandoop_request_code);
    }

    @OnClick(R.id.contine_order_btn_id)
    void orderProduct() {
        if (networkAvailable.isNetworkAvailable()) {
            other_notes = notes_ed.getText().toString();
            discount_coupon = discount_coupon_ed.getText().toString();

            if (date_val_txtV.getText().toString().equals(getString(R.string.date))) {
                Toast.makeText(this, getString(R.string.select_date_first), Toast.LENGTH_SHORT).show();
                return;
            }
            if (time_val_txtV.getText().toString().equals(getString(R.string.time))) {
                Toast.makeText(this, getString(R.string.select_time_first), Toast.LENGTH_SHORT).show();
                return;
            }
            if (address_val_txtV.getText().toString().equals(getString(R.string.click_here_to_detect_address))) {
                Toast.makeText(this, getString(R.string.should_select_address_first), Toast.LENGTH_SHORT).show();
                return;
            }
            if (selected_Mandoop_txtV.getText().toString().equals(getString(R.string.select_mandoop))){
                Toast.makeText(this, getString(R.string.should_select_mandoop_first), Toast.LENGTH_SHORT).show();
                return;
            }
            OrderProductDataModel orderProductDataModel = new OrderProductDataModel();
            orderProductDataModel.setProduct_id(productData.getId());
            orderProductDataModel.setTrader_id(productData.getTrader_id());
            orderProductDataModel.setDeliver_address(address);
            orderProductDataModel.setDeliver_lat(latitude);
            orderProductDataModel.setDeliver_lng(longtitude);
            orderProductDataModel.setProduct_count(count_txtV.getText().toString());
            orderProductDataModel.setOrder_price(Integer.parseInt(count_txtV.getText().toString()) * productData.getPrice());
            orderProductDataModel.setDeliver_company(deliver_id);
            orderProductDataModel.setOrder_date(date_val_txtV.getText().toString());
            orderProductDataModel.setOrder_time(time_val_txtV.getText().toString());
            orderProductDataModel.setOther_notes(other_notes);
            Intent intent = new Intent(OrderActivity.this, PaymentActivity.class);
            intent.putExtra("order_data", orderProductDataModel);
            startActivity(intent);
            finish();
        } else
            Toast.makeText(this, getString(R.string.error_connection), Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.date_val_txtV_id)
    void getDate() {
        final DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                String myFormat = "yyyy/MM/dd";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                date_val_txtV.setText(sdf.format(myCalendar.getTime()));
            }
        };
        DatePickerDialog mDate = new DatePickerDialog(OrderActivity.this, datePickerListener, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH));
        mDate.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        mDate.show();
    }

    @OnClick(R.id.time_val_txtV_id)
    void getTime() {
        // Inflate Time Picker ....
        int hour = myCalendar.get(Calendar.HOUR_OF_DAY);
        int minute = myCalendar.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(OrderActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

                String needed_order_time = selectedHour + ":" + selectedMinute;
                time_val_txtV.setText(needed_order_time);
            }
        }, hour, minute, false);
        mTimePicker.setTitle(getString(R.string.detect_delivery_time));
        mTimePicker.show();
    }

    @OnClick(R.id.order_back_txtV_id)
    void goBack() {
        finish();
    }

    @OnClick(R.id.order_notifications_imageV_id)
    void goToNotifications() {
        intent = new Intent(OrderActivity.this, MyNotifications.class);
        startActivity(intent);
    }

    @OnClick(R.id.order_user_imageV_id)
    void goToProfile() {
        intent = new Intent(OrderActivity.this, ProfileActivity.class);
        startActivity(intent);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, this);
                address = String.format("%s", place.getAddress());
                latitude = place.getLatLng().latitude;
                longtitude = place.getLatLng().longitude;
                Log.e("lat", latitude + "");
                Log.e("lan", longtitude + "");
                Log.e("address", address);
                address_val_txtV.setText("تم اختيار العنوان");
                Geocoder gcd = new Geocoder(this, Locale.getDefault());
                List<Address> addresses = null;
                try {
                    addresses = gcd.getFromLocation(latitude, longtitude, 1);
                    if (addresses.size() > 0 && addresses != null) {
                        Address obj = addresses.get(0);
                        country = obj.getCountryName();
                        mohafza = obj.getAdminArea();
                        city = obj.getSubAdminArea();
                        mntqa = obj.getLocality();
                    } else {
                        // do your stuff
                        Toast.makeText(this, "لم يتم تحديد الموقع بعد!, حاول مرة اخرى", Toast.LENGTH_SHORT).show();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "لم يتم تحديد الموقع بعد!, حاول مرة اخرى", Toast.LENGTH_SHORT).show();
                }
            }
        } else if (requestCode == mandoop_request_code) {
            if (resultCode == RESULT_OK) {
                deliver_id = data.getExtras().getInt("driver_id");
                String driver_name = data.getStringExtra("driver_name");
                double driver_lat = data.getExtras().getDouble("driver_lat");
                double driver_lng = data.getExtras().getDouble("driver_lng");
                selected_Mandoop_txtV.setText(driver_name);
            }
        }
    }
}
