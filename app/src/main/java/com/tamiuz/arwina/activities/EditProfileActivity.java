package com.tamiuz.arwina.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
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

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.fourhcode.forhutils.FUtilsValidation;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.gson.Gson;
import com.tamiuz.arwina.Models.RegisterResponseModel;
import com.tamiuz.arwina.Models.UserModel;
import com.tamiuz.arwina.R;
import com.tamiuz.arwina.networking.ApiClient;
import com.tamiuz.arwina.networking.ApiServiceInterface;
import com.tamiuz.arwina.networking.NetworkAvailable;
import com.tamiuz.arwina.utils.DialogUtil;
import com.tamiuz.arwina.utils.SharedPreferenceManager;
import com.tamiuz.arwina.utils.Urls;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Locale;

public class EditProfileActivity extends AppCompatActivity {

    @BindView(R.id.editCompany_name_ed_id)
    EditText fullName_ed;
    @BindView(R.id.editCompany_address_ed_id)
    EditText address_ed;
    @BindView(R.id.editCompany_phone_ed_id)
    EditText phone_ed;
    @BindView(R.id.editCompany_market_name_ed_id)
    EditText commerialName_ed;
    @BindView(R.id.editCompany_market_number_ed_id)
    EditText commerialNum_ed;
    @BindView(R.id.editCompany_image_txtV_id)
    TextView company_image_txtV;
    @BindView(R.id.editCompany_have_mandoop_rb_ib)
    RadioButton haveDelivry_rb;
    @BindView(R.id.editCompany_not_have_mandoop_rb_ib)
    RadioButton not_haveDelivry_rb;
    @BindView(R.id.have_mandoop_txtv_id)
    TextView have_mandoop_txtv;
    @BindView(R.id.editCompany_have_mandoop_rg_id)
    RadioGroup radioGroup;
    @BindView(R.id.editCompany_upload_imageV_id)
    LinearLayout upload_imageV_layout;
    @BindView(R.id.editCompany_car_imageV_id)
    LinearLayout editCompany_car_imageV;
    @BindView(R.id.editCompany_lisence_imageV_id)
    LinearLayout editCompany_lisence_imageV;
    @BindView(R.id.editCompany_identity_imageV_id)
    LinearLayout editCompany_identity_imageV;
    @BindView(R.id.edit_address_layout_id)
    LinearLayout edit_address_layout;
    @BindView(R.id.edit_car_image_txtV_id)
    TextView edit_car_image_txtV;
    @BindView(R.id.edit_lisenceImage_txtV_id)
    TextView edit_lisenceImage_txtV;
    @BindView(R.id.identity_image_txtV_id)
    TextView identity_image_txtV;
    @BindView(R.id.edit_address_txtV_id)
    TextView edit_address_txtV;
    @BindView(R.id.editProfile_userImageV_id)
    ImageView user_imageV;

    private UserModel userModel;
    private NetworkAvailable networkAvailable;
    private DialogUtil dialogUtil;
    private final String TAG = this.getClass().getSimpleName();
    private String storage_permission[];
    private MultipartBody.Part body, car_part_img, lisence_part_img, identity_part_img = null;
    private String commercialreg, commercialregno, havedelivery = "";
    private double latitude, longtitude = 0.0;
    private String address, cityName, streetName, locality;
    //Vars
    private String have_delivery = "1";
    private int PLACE_PICKER_REQUEST = 900;
    private final int car_image_code = 950;
    private final int lisence_image_code = 960;
    private final int identity_image_code = 970;
    private final int image_pick_gallery_code = 990;
    // Shared Pref
    SharedPreferenceManager preferenceManager;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        ButterKnife.bind(this);

        if (MainActivity.userModel.getRole() == 0) {
            address_ed.setVisibility(View.GONE);
            commerialNum_ed.setVisibility(View.GONE);
            commerialName_ed.setVisibility(View.GONE);
            upload_imageV_layout.setVisibility(View.GONE);
            have_mandoop_txtv.setVisibility(View.GONE);
            editCompany_car_imageV.setVisibility(View.GONE);
            editCompany_lisence_imageV.setVisibility(View.GONE);
            editCompany_identity_imageV.setVisibility(View.GONE);
            edit_address_layout.setVisibility(View.GONE);
            radioGroup.setVisibility(View.GONE);
        } else if (MainActivity.userModel.getRole() == 1) {
            editCompany_car_imageV.setVisibility(View.GONE);
            editCompany_lisence_imageV.setVisibility(View.GONE);
            editCompany_identity_imageV.setVisibility(View.GONE);
            edit_address_layout.setVisibility(View.GONE);
        } else if (MainActivity.userModel.getRole() == 2) {
            commerialNum_ed.setVisibility(View.GONE);
            commerialName_ed.setVisibility(View.GONE);
            have_mandoop_txtv.setVisibility(View.GONE);
            radioGroup.setVisibility(View.GONE);
        }
        if (MainActivity.userModel.getImage() != null)
            Glide.with(this)
                    .load(Urls.imagesBase_Url + MainActivity.userModel.getImage())
                    .error(R.drawable.add_user)
                    .fitCenter()
                    .into(user_imageV);
        fullName_ed.setText(MainActivity.userModel.getName());
        address_ed.setText(MainActivity.userModel.getAddress());
        phone_ed.setText(MainActivity.userModel.getPhone());
        commerialName_ed.setText(MainActivity.userModel.getCommercialreg());
        commerialNum_ed.setText(MainActivity.userModel.getCommercialregno());
        if (MainActivity.userModel.getHavedelivery() == 1) {
            haveDelivry_rb.setChecked(true);
        } else {
            not_haveDelivry_rb.setChecked(true);
        }
        networkAvailable = new NetworkAvailable(this);
        dialogUtil = new DialogUtil();
        // get data from shared preferences ...
        preferenceManager = new SharedPreferenceManager(EditProfileActivity.this, SharedPreferenceManager.PREFERENCE_NAME);
        //Storage Permission
        storage_permission = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();
        // Check which radio button was clicked
        switch (view.getId()) {
            case R.id.editCompany_have_mandoop_rb_ib:
                have_delivery = "1";
                break;
            case R.id.editCompany_not_have_mandoop_rb_ib:
                have_delivery = "0";
                break;
        }
    }

    @OnClick(R.id.editCompany_upload_imageV_id)
    void companyImage() {
        try {
            if (ActivityCompat.checkSelfPermission(EditProfileActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(EditProfileActivity.this, storage_permission, image_pick_gallery_code);
            } else {
                pickGallery();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Handle permission result...
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case image_pick_gallery_code:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    pickGallery();
                else
                    Toast.makeText(this, "permission denied", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void pickGallery() {
        intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, image_pick_gallery_code);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == image_pick_gallery_code) {
                try {
                    Uri selectedImage = data.getData();
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
                    company_image_txtV.setText(getString(R.string.get_image_success));
                    InputStream is = getContentResolver().openInputStream(data.getData());
                    createMandoopPartFile(getBytes(is));

                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (requestCode == lisence_image_code) {
                try {
                    Uri selectedImage = data.getData();
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
                    edit_lisenceImage_txtV.setText(getString(R.string.get_image_success));
                    InputStream is = getContentResolver().openInputStream(data.getData());
                    createLisencePartFile(getBytes(is));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (requestCode == car_image_code) {
                try {
                    Uri selectedImage = data.getData();
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
                    edit_car_image_txtV.setText(getString(R.string.get_image_success));
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
            } else if (requestCode == PLACE_PICKER_REQUEST) {
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
                        add = add + "\n" + obj.getCountryName();
                        cityName = addresses.get(0).getSubAdminArea();
                        edit_address_txtV.setText(getString(R.string.get_address_successfully));
                        address_ed.setText(address);
                        Toast.makeText(this, getString(R.string.get_address_successfully), Toast.LENGTH_SHORT).show();
                    } else {
                        // do your stuff
                        Toast.makeText(this, "لم يتم تحديد الموقع بعد!, حاول مرة اخرى", Toast.LENGTH_SHORT).show();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "لم يتم تحديد الموقع بعد!, حاول مرة اخرى", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @OnClick(R.id.editCompany_btn_id)
    void companySign() {
        if (networkAvailable.isNetworkAvailable()) {
            if (!FUtilsValidation.isEmpty(fullName_ed, getString(R.string.required))
                    && !FUtilsValidation.isEmpty(phone_ed, getString(R.string.required))) {

                String token = "123456";
                RequestBody NamePart = RequestBody.create(MultipartBody.FORM, fullName_ed.getText().toString().trim());
                address = address_ed.getText().toString();
                commercialreg = commerialName_ed.getText().toString();
                commercialregno = commerialNum_ed.getText().toString();
                // Show Progress Dialog
                final ProgressDialog dialog = dialogUtil.showProgressDialog(EditProfileActivity.this, getString(R.string.updatting), false);
                ApiServiceInterface serviceInterface = ApiClient.getClient().create(ApiServiceInterface.class);
                Call<RegisterResponseModel> call = serviceInterface.update_profile(MainActivity.userModel.getId(), NamePart, phone_ed.getText().toString(),
                        commercialreg, commercialregno, address, have_delivery, latitude, longtitude, body, car_part_img, lisence_part_img, identity_part_img);
                call.enqueue(new Callback<RegisterResponseModel>() {
                    @Override
                    public void onResponse(Call<RegisterResponseModel> call, Response<RegisterResponseModel> response) {
                        Log.i(TAG, "onResponse " + response.toString());
                        userModel = new UserModel();
                        if (response.body().getMessage()) {
                            RegisterResponseModel.LoginDataObj dataObj = response.body().getData();
                            userModel.setId(dataObj.getId());
                            userModel.setRole(dataObj.getRole());
                            userModel.setName(dataObj.getName());
                            userModel.setPhone(dataObj.getPhone());
                            userModel.setAddress(dataObj.getAddress());
                            userModel.setImage(dataObj.getImage());
                            if (dataObj.getCommercialreg() != null)
                                userModel.setCommercialreg(dataObj.getCommercialreg());
                            if (dataObj.getCommercialregno() != null)
                                userModel.setCommercialregno(dataObj.getCommercialregno());
                            if (dataObj.getHavedelivery() != null)
                                userModel.setHavedelivery(dataObj.getHavedelivery());

                            dialog.dismiss();
                            // Save UserDataObj to Shared
                            Gson gson = new Gson();
                            String user_data = gson.toJson(userModel);
                            preferenceManager.setValue(SharedPreferenceManager.USER_DATA, user_data);

                            Toast.makeText(EditProfileActivity.this, getString(R.string.date_updatted_success), Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            dialog.dismiss();
                            Toast.makeText(EditProfileActivity.this, getString(R.string.phone_already_exist), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<RegisterResponseModel> call, Throwable t) {
                        Log.i(TAG, "onFailure " + t.getMessage());
                        t.printStackTrace();
                        dialog.dismiss();
                        Toast.makeText(EditProfileActivity.this, getString(R.string.phone_already_exist), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        } else {
            Toast.makeText(this, getString(R.string.error_connection), Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.editCompany_car_imageV_id)
    void getCarImage() {
        intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, car_image_code);
    }

    @OnClick(R.id.editCompany_lisence_imageV_id)
    void getLisenceImage() {
        intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, lisence_image_code);
    }

    @OnClick(R.id.editCompany_identity_imageV_id)
    void getIdentityImage() {
        intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, identity_image_code);
    }

    @OnClick(R.id.edit_address_layout_id)
    void getAddress() {
        try {
            PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
            startActivityForResult(builder.build(EditProfileActivity.this), PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
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