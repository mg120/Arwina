package com.tamiuz.arwina.activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.fourhcode.forhutils.FUtilsValidation;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.gson.Gson;
import com.karan.churi.PermissionManager.PermissionManager;
import com.tamiuz.arwina.Models.RegisterResponseModel;
import com.tamiuz.arwina.Models.UserModel;
import com.tamiuz.arwina.R;
import com.tamiuz.arwina.networking.ApiClient;
import com.tamiuz.arwina.networking.ApiServiceInterface;
import com.tamiuz.arwina.networking.NetworkAvailable;
import com.tamiuz.arwina.utils.DialogUtil;
import com.tamiuz.arwina.utils.SharedPreferenceManager;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Locale;

import androidx.core.app.ActivityCompat;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MandoopSignUp extends AppCompatActivity {

    @BindView(R.id.mandoop_name_ed_id)
    EditText userNam_ed;
    @BindView(R.id.mandoop_phone_ed_id)
    EditText phone_ed;
    @BindView(R.id.mandoop_password_ed_id)
    EditText password_ed;
    @BindView(R.id.car_image_txtV_id)
    TextView car_image_txtV;
    @BindView(R.id.mandoop_image_txtV_id)
    TextView mandoop_image_txtV;
    @BindView(R.id.lisence_image_txtV_id)
    TextView lisence_image_txtV;
    @BindView(R.id.identity_image_txtV_id)
    TextView identity_image_txtV;
    @BindView(R.id.mandoopCar_upload_imageV_id)
    LinearLayout mandoopCar_upload_imageV;
    @BindView(R.id.mandoopLisence_upload_imageV_id)
    LinearLayout mandoopLisence_upload_imageV;
    @BindView(R.id.mandoop_national_upload_imageV_id)
    LinearLayout mandoop_national_upload_imageV;
    @BindView(R.id.mandoopImage_upload_imageV_id)
    LinearLayout mandoopImage_upload_imageV;
    @BindView(R.id.mandoop_address_layout_id)
    LinearLayout mandoop_address_layout;
    @BindView(R.id.address_txtV_id)
    TextView address_txtV;
    @BindView(R.id.signType_txtV_id)
    TextView signType_txtV;
    @BindView(R.id.mandoop_sign_progress_id)
    ProgressBar progressBar;

    private MultipartBody.Part body, car_part_img, lisence_part_img, identity_part_img = null;
    private UserModel userModel;
    private NetworkAvailable networkAvailable;
    private DialogUtil dialogUtil;
    private PermissionManager permission;
    private Intent intent;

    //Vars
    private final String TAG = this.getClass().getSimpleName();
    private String storage_permission[];
    private int Sign_PLACE_PICKER_REQUEST = 1300;
    private final int image_pick_gallery_code = 440;
    private final int car_image_code = 450;
    private final int lisence_image_code = 460;
    private final int identity_image_code = 470;
    private String commercialreg, commercialregno, havedelivery = "";
    private int sign_type;
    private double latitude, longtitude = 0.0;
    private String address, cityName, streetName, locality;
    // Shared Pref
    SharedPreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mandoop_sign_up);
        ButterKnife.bind(this);
        permission = new PermissionManager() {
        };
        permission.checkAndRequestPermissions(this);

        networkAvailable = new NetworkAvailable(this);
        dialogUtil = new DialogUtil();
        if (getIntent().hasExtra("sign_type")) {
            sign_type = getIntent().getExtras().getInt("sign_type");
            Log.i("sign_type", sign_type + "");
            if (sign_type == 0) { // User
                mandoopCar_upload_imageV.setVisibility(View.GONE);
                mandoop_national_upload_imageV.setVisibility(View.GONE);
                mandoopLisence_upload_imageV.setVisibility(View.GONE);
                mandoopImage_upload_imageV.setVisibility(View.GONE);
            } else {
                signType_txtV.setText(getString(R.string.mandoop_signUp));
            }
        }
    }

    @OnClick(R.id.company_sign_btn_id)
    void mandoopSignUp() {
        if (networkAvailable.isNetworkAvailable()) {
            if (!FUtilsValidation.isEmpty(userNam_ed, getString(R.string.required))
                    && !FUtilsValidation.isEmpty(phone_ed, getString(R.string.required))
                    && !FUtilsValidation.isEmpty(password_ed, getString(R.string.required))
            ) {
                // Show Progress Dialog
                final ProgressDialog dialog = dialogUtil.showProgressDialog(MandoopSignUp.this, getString(R.string.signing_up), false);
                userModel = new UserModel();
                String token = "123456";
                RequestBody NamePart = RequestBody.create(MultipartBody.FORM, userNam_ed.getText().toString().trim());
                ApiServiceInterface serviceInterface = ApiClient.getClient().create(ApiServiceInterface.class);
                Call<RegisterResponseModel> call = serviceInterface.register(sign_type, NamePart, phone_ed.getText().toString(), password_ed.getText().toString(),
                        commercialreg, commercialregno, address, havedelivery, token, latitude, longtitude, body, car_part_img, lisence_part_img, identity_part_img);
                call.enqueue(new Callback<RegisterResponseModel>() {
                    @Override
                    public void onResponse(Call<RegisterResponseModel> call, Response<RegisterResponseModel> response) {
                        if (response.body().getMessage()) {
                            dialog.dismiss();
                            RegisterResponseModel.LoginDataObj loginDataObj = response.body().getData();
                            userModel.setId(loginDataObj.getId());
                            userModel.setRole(loginDataObj.getRole());
                            userModel.setName(loginDataObj.getName());
                            userModel.setPhone(loginDataObj.getPhone());

                            // Save UserDataObj to Shared
                            Gson gson = new Gson();
                            String user_data = gson.toJson(userModel);
                            preferenceManager.setValue(SharedPreferenceManager.USER_DATA, user_data);
                            // GoTo Main Activity...
                            Toast.makeText(MandoopSignUp.this, getString(R.string.registered_successfully), Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(MandoopSignUp.this, MainActivity.class);
                            intent.putExtra("user_data", userModel);
                            startActivity(intent);
                        } else {
                            dialog.dismiss();
                            Toast.makeText(MandoopSignUp.this, getString(R.string.phone_already_taken), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<RegisterResponseModel> call, Throwable t) {
                        t.printStackTrace();
                        dialog.dismiss();
                        Toast.makeText(MandoopSignUp.this, getString(R.string.phone_already_taken), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        } else
            Toast.makeText(this, getString(R.string.error_connection), Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.mandoop_address_layout_id)
    void getAddress(){
        progressBar.setVisibility(View.VISIBLE);
        try {
            PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
            startActivityForResult(builder.build(MandoopSignUp.this), Sign_PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.mandoopImage_upload_imageV_id)
    void getmandoop_img() {
        intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, image_pick_gallery_code);
    }

    @OnClick(R.id.mandoopCar_upload_imageV_id)
    void getCar_img() {
        intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, car_image_code);
    }

    @OnClick(R.id.mandoopLisence_upload_imageV_id)
    void getLisence_img() {
        intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, lisence_image_code);
    }

    @OnClick(R.id.mandoop_national_upload_imageV_id)
    void getIdentity_img() {
        intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, identity_image_code);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        permission.checkResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == image_pick_gallery_code) {
                try {
                    Uri selectedImage = data.getData();
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
                    mandoop_image_txtV.setText(getString(R.string.get_image_success));
                    InputStream is = getContentResolver().openInputStream(data.getData());
                    createMandoopPartFile(getBytes(is));

                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (requestCode == lisence_image_code) {
                try {
                    Uri selectedImage = data.getData();
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
                    lisence_image_txtV.setText(getString(R.string.get_image_success));
                    InputStream is = getContentResolver().openInputStream(data.getData());
                    createLisencePartFile(getBytes(is));

                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (requestCode == car_image_code) {
                try {
                    Uri selectedImage = data.getData();
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
                    car_image_txtV.setText(getString(R.string.get_image_success));
                    InputStream is = getContentResolver().openInputStream(data.getData());
                    createCarPartFile(getBytes(is));

                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (requestCode == identity_image_code) {
                try {
                    Uri selectedImage = data.getData();
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
                    identity_image_txtV.setText(getString(R.string.get_image_success));
                    InputStream is = getContentResolver().openInputStream(data.getData());
                    createIdentityPartFile(getBytes(is));

                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (requestCode == Sign_PLACE_PICKER_REQUEST){
                Place place = PlacePicker.getPlace(data, this);
                address = String.format("%s", place.getAddress());
                latitude = place.getLatLng().latitude;
                longtitude = place.getLatLng().longitude;
                Log.e("lat", latitude + "");
                Log.e("lan", longtitude + "");
                Log.e("address", address);
                Geocoder gcd = new Geocoder(this, Locale.getDefault());
                List<Address> addresses = null;
                try {
                    addresses = gcd.getFromLocation(latitude, longtitude, 1);
                    if (addresses.size() > 0 && addresses != null) {
                        Address obj = addresses.get(0);
                        String add = obj.getAddressLine(0);
                        Log.i("addadd:: ", add);
                        cityName = addresses.get(0).getSubAdminArea();
                        streetName = addresses.get(0).getFeatureName();
                        Log.e("streetName: ", streetName);
                        Log.e("locality: ", locality);
                        address_txtV.setText(getString(R.string.get_address_successfully));
                        Toast.makeText(this, getString(R.string.get_address_successfully), Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                    } else {
                        // do your stuff
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(this, "لم يتم تحديد الموقع بعد!, حاول مرة اخرى", Toast.LENGTH_SHORT).show();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(this, "لم يتم تحديد الموقع بعد!, حاول مرة اخرى", Toast.LENGTH_SHORT).show();
                }
            }
        }
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

    private void createMandoopPartFile(byte[] imageBytes) {
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpeg"), imageBytes);
        body = MultipartBody.Part.createFormData("image", "image.jpg", requestFile);
    }

    private void createCarPartFile(byte[] imageBytes) {
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpeg"), imageBytes);
        car_part_img = MultipartBody.Part.createFormData("carimage", "image.jpg", requestFile);
    }

    private void createLisencePartFile(byte[] imageBytes) {
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpeg"), imageBytes);
        lisence_part_img = MultipartBody.Part.createFormData("licenseimage", "image.jpg", requestFile);
    }

    private void createIdentityPartFile(byte[] imageBytes) {
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpeg"), imageBytes);
        identity_part_img = MultipartBody.Part.createFormData("identityimage", "image.jpg", requestFile);
    }
}
