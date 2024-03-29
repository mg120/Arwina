package com.tamiuz.arwina.activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fourhcode.forhutils.FUtilsValidation;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.tamiuz.arwina.Models.MakeOrderResponseModel;
import com.tamiuz.arwina.R;
import com.tamiuz.arwina.networking.ApiClient;
import com.tamiuz.arwina.networking.ApiServiceInterface;
import com.tamiuz.arwina.networking.NetworkAvailable;
import com.tamiuz.arwina.utils.DialogUtil;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddProductActivity extends AppCompatActivity {
    @BindView(R.id.product_name_ed_id)
    EditText prodName_ed;
    @BindView(R.id.product_price_ed_id)
    EditText prodPrice_ed;
    @BindView(R.id.product_quantity_ed_id)
    EditText product_quantity_ed;
    @BindView(R.id.product_maxQuantity_ed_id)
    EditText prodMaxQuantity_ed;
    @BindView(R.id.product_completeDesc_ed_id)
    EditText product_Desc_ed;
    @BindView(R.id.product_address_layout_id)
    LinearLayout product_address_layout;
    @BindView(R.id.product_address_txtV_id)
    TextView address_txtV;
    @BindView(R.id.product_iamge_txtV_id)
    TextView product_iamge_txtV;

    private static final int PLACE_PICKER_REQUEST = 110;
    private static final int image_pick_gallery_code = 120;
    double latitude, longtitude = 0.0;
    String address, cityName, streetName, locality;
    private boolean mGallery_permission;
    private final String TAG = this.getClass().getSimpleName();

    private NetworkAvailable networkAvailable;
    private DialogUtil dialogUtil;
    String storage_permission[];
    private MultipartBody.Part body = null;
    private static String READ_Storage = Manifest.permission.READ_EXTERNAL_STORAGE;
    private static String Write_Storage = Manifest.permission.WRITE_EXTERNAL_STORAGE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        ButterKnife.bind(this);

        networkAvailable = new NetworkAvailable(this);
        dialogUtil = new DialogUtil();
    }

    @OnClick(R.id.addProduct_back_txtV_id)
    void goBack() {
        finish();
    }

    @OnClick(R.id.add_product_btn_id)
    void addProduct() {
        if (networkAvailable.isNetworkAvailable()) {
            if (!FUtilsValidation.isEmpty(prodName_ed, getString(R.string.required))
                    && !FUtilsValidation.isEmpty(prodPrice_ed, getString(R.string.required))
                    && !FUtilsValidation.isEmpty(product_quantity_ed, getString(R.string.required))
                    && !FUtilsValidation.isEmpty(prodMaxQuantity_ed, getString(R.string.required))
                    && !FUtilsValidation.isEmpty(product_Desc_ed, getString(R.string.required))) {

                if (address_txtV.getText().toString().equals(getString(R.string.address))){
                    Toast.makeText(this, getString(R.string.should_select_address_first), Toast.LENGTH_SHORT).show();
                    return;
                }
                if (product_iamge_txtV.getText().toString().equals(getString(R.string.product_image))){
                    Toast.makeText(this, getString(R.string.should_select_product_image), Toast.LENGTH_SHORT).show();
                    return;
                }
                RequestBody NamePart = RequestBody.create(MultipartBody.FORM, prodName_ed.getText().toString().trim());
                // Show Progress Dialog
                final ProgressDialog dialog = dialogUtil.showProgressDialog(AddProductActivity.this, getString(R.string.adding), false);
                ApiServiceInterface serviceInterface = ApiClient.getClient().create(ApiServiceInterface.class);
                Call<MakeOrderResponseModel> call = serviceInterface.add_product(MainActivity.userModel.getId(), NamePart, product_quantity_ed.getText().toString(),
                        prodMaxQuantity_ed.getText().toString(), address, product_Desc_ed.getText().toString(), prodPrice_ed.getText().toString(), body);
                call.enqueue(new Callback<MakeOrderResponseModel>() {
                    @Override
                    public void onResponse(Call<MakeOrderResponseModel> call, Response<MakeOrderResponseModel> response) {
                        dialog.dismiss();
                        if (response.body().getMessage()){
                            Toast.makeText(AddProductActivity.this, response.body().getData(), Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(AddProductActivity.this, ThanksPage.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(AddProductActivity.this, response.body().getData(), Toast.LENGTH_SHORT).show();
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
    }

    @OnClick(R.id.product_upload_imageV_id)
    void getProductImage() {
        if (mGallery_permission)
            pickGallery();
        else
            getGalleryPermission();
    }

    private void getGalleryPermission() {
        Log.i(TAG, "onLocation Permissions Started ...");

        if (ContextCompat.checkSelfPermission(this.getApplicationContext(), READ_Storage) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this.getApplicationContext(), Write_Storage) == PackageManager.PERMISSION_GRANTED) {
                mGallery_permission = true;
                pickGallery();
            } else {
                ActivityCompat.requestPermissions(this, storage_permission, image_pick_gallery_code);
            }
        } else {
            ActivityCompat.requestPermissions(this, storage_permission, image_pick_gallery_code);
        }
    }

    private void pickGallery() {
        Intent gallery_intent = new Intent(Intent.ACTION_PICK);
        gallery_intent.setType("image/*");
        startActivityForResult(gallery_intent, image_pick_gallery_code);
    }

    private byte[] getBytes(InputStream is) throws IOException {
        ByteArrayOutputStream byteBuff = new ByteArrayOutputStream();
        int buffSize = 1024;
        byte[] buff = new byte[buffSize];

        int len = 0;
        while ((len = is.read(buff)) != -1) {
            byteBuff.write(buff, 0, len);
        }
        return byteBuff.toByteArray();
    }

    private void createMultiPartFile(byte[] imageBytes) {
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpeg"), imageBytes);
        body = MultipartBody.Part.createFormData("image", "image.jpg", requestFile);
    }

    @OnClick(R.id.product_address_layout_id)
    void getAddress() {
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        try {
            startActivityForResult(builder.build(AddProductActivity.this), PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
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
                address_txtV.setText("تم اختيار العنوان");
                Geocoder gcd = new Geocoder(this, Locale.getDefault());
                List<Address> addresses = null;
                try {
                    addresses = gcd.getFromLocation(latitude, longtitude, 1);
                    if (addresses.size() > 0 && addresses != null) {
                        Address obj = addresses.get(0);
                        String add = obj.getAddressLine(0);
                        add = add + "\n" + obj.getCountryName();
                        add = add + "\n" + obj.getCountryCode();
                        add = add + "\n" + obj.getAdminArea();
                        add = add + "\n" + obj.getPostalCode();
                        add = add + "\n" + obj.getSubAdminArea();
                        add = add + "\n" + obj.getLocality();
                        add = add + "\n" + obj.getSubThoroughfare();
                        Log.i("addadd:: ", add);
                        cityName = addresses.get(0).getSubAdminArea();
                        streetName = addresses.get(0).getFeatureName();
                        locality = addresses.get(0).getLocality();
                        Log.e("cityName: ", addresses.get(0).getAddressLine(0));
                    } else {
                        // do your stuff
                        Toast.makeText(this, "لم يتم تحديد الموقع بعد!, حاول مرة اخرى", Toast.LENGTH_SHORT).show();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "لم يتم تحديد الموقع بعد!, حاول مرة اخرى", Toast.LENGTH_SHORT).show();
                }
            }
        } else if (requestCode == image_pick_gallery_code){
            if (resultCode == RESULT_OK){
                try {
                    android.net.Uri selectedImage = data.getData();
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
                    product_iamge_txtV.setText(getString(R.string.get_image_success));
                    InputStream is = getContentResolver().openInputStream(data.getData());
                    createMultiPartFile(getBytes(is));

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
