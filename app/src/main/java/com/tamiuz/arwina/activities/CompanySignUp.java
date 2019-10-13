package com.tamiuz.arwina.activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.fourhcode.forhutils.FUtilsValidation;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CompanySignUp extends AppCompatActivity {
    @BindView(R.id.company_name_ed_id)
    EditText fullName_ed;
    @BindView(R.id.company_address_ed_id)
    EditText address_ed;
    @BindView(R.id.company_phone_ed_id)
    EditText phone_ed;
    @BindView(R.id.company_market_name_ed_id)
    EditText commerialName_ed;
    @BindView(R.id.company_market_number_ed_id)
    EditText commerialNum_ed;
    @BindView(R.id.company_password_ed_id)
    EditText password_ed;
    @BindView(R.id.company_image_txtV_id)
    TextView company_image_txtV;

    private UserModel userModel;
    private NetworkAvailable networkAvailable;
    private DialogUtil dialogUtil;
    private final String TAG = this.getClass().getSimpleName();
    private String storage_permission[];
    private MultipartBody.Part body, car_part_img, lisence_part_img, identity_part_img = null;
    //Vars
    private String have_delivery = "1";
    private final int image_pick_gallery_code = 110;
    private double latitude, longtitude = 0.0;
    private String address, cityName, streetName, locality;
    // Shared Pref
    SharedPreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_sign_up);
        ButterKnife.bind(this);

        networkAvailable = new NetworkAvailable(this);
        dialogUtil = new DialogUtil();
        //   get data from shared preferences ...
        preferenceManager = new SharedPreferenceManager(CompanySignUp.this, SharedPreferenceManager.PREFERENCE_NAME);
        //Storage Permission
        storage_permission = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();
        // Check which radio button was clicked
        switch (view.getId()) {
            case R.id.have_mandoop_rb_ib:
                have_delivery = "1";
                break;
            case R.id.not_have_mandoop_rb_ib:
                have_delivery = "0";
                break;
        }
    }

    @OnClick(R.id.company_upload_imageV_id)
    void companyImage() {
        try {
            if (ActivityCompat.checkSelfPermission(CompanySignUp.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(CompanySignUp.this, storage_permission, image_pick_gallery_code);
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
        Intent gallery_intent = new Intent(Intent.ACTION_PICK);
        gallery_intent.setType("image/*");
        startActivityForResult(gallery_intent, image_pick_gallery_code);
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
                    createMultiPartFile(getBytes(is));

                } catch (IOException e) {
                    e.printStackTrace();
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

    private void createMultiPartFile(byte[] imageBytes) {
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpeg"), imageBytes);
        body = MultipartBody.Part.createFormData("image", "image.jpg", requestFile);
    }

    @OnClick(R.id.company_sign_btn_id)
    void companySign(){
        if (networkAvailable.isNetworkAvailable()) {
            if (!FUtilsValidation.isEmpty(fullName_ed, getString(R.string.required))
                    && !FUtilsValidation.isEmpty(address_ed, getString(R.string.required))
                    && !FUtilsValidation.isEmpty(phone_ed, getString(R.string.required))
                    && !FUtilsValidation.isEmpty(commerialName_ed, getString(R.string.required))
                    && !FUtilsValidation.isEmpty(commerialNum_ed, getString(R.string.required))
                    && !FUtilsValidation.isEmpty(password_ed, getString(R.string.required))) {
                if (password_ed.getText().toString().length() < 6) {
                    password_ed.setError(getResources().getString(R.string.pass_is_weak));
                    password_ed.requestFocus();
                    return;
                }
                String token = FirebaseInstanceId.getInstance().getToken();
                RequestBody NamePart = RequestBody.create(MultipartBody.FORM, fullName_ed.getText().toString().trim());
                // Show Progress Dialog
                final ProgressDialog dialog = dialogUtil.showProgressDialog(CompanySignUp.this, getString(R.string.signing_up), false);
                ApiServiceInterface serviceInterface = ApiClient.getClient().create(ApiServiceInterface.class);
                Call<RegisterResponseModel> call = serviceInterface.register(1, NamePart, phone_ed.getText().toString(), password_ed.getText().toString(), commerialName_ed.getText().toString(),
                        commerialNum_ed.getText().toString(), address_ed.getText().toString(), have_delivery, token, latitude, longtitude, body, car_part_img, lisence_part_img, identity_part_img);
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
                            userModel.setCommercialreg(dataObj.getCommercialreg());
                            userModel.setCommercialregno(dataObj.getCommercialregno());
                            userModel.setHavedelivery(dataObj.getHavedelivery());
                            userModel.setPromotioncode(dataObj.getPromotioncode());
                            userModel.setMyorderscount(dataObj.getMyorderscount());

                            dialog.dismiss();
                            // Save UserDataObj to Shared
                            Gson gson = new Gson();
                            String user_data = gson.toJson(userModel);
                            preferenceManager.setValue(SharedPreferenceManager.USER_DATA, user_data);

                            Toast.makeText(CompanySignUp.this, getString(R.string.sign_success), Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(CompanySignUp.this, MainActivity.class);
                            intent.putExtra("user_data" , userModel);
                            startActivity(intent);
                            finish();
                        } else {
                            dialog.dismiss();
                            Toast.makeText(CompanySignUp.this, getString(R.string.phone_already_exist), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<RegisterResponseModel> call, Throwable t) {
                        Log.i(TAG, "onFailure " + t.getMessage());
                        t.printStackTrace();
                        dialog.dismiss();
                        Toast.makeText(CompanySignUp.this, getString(R.string.phone_already_exist), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        } else {
            Toast.makeText(this, getString(R.string.error_connection), Toast.LENGTH_SHORT).show();
        }
    }
}
