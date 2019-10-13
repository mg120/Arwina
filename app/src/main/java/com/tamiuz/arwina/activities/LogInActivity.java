package com.tamiuz.arwina.activities;

import android.app.ProgressDialog;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.fourhcode.forhutils.FUtilsValidation;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.tamiuz.arwina.Models.UserLoginModel;
import com.tamiuz.arwina.Models.UserModel;
import com.tamiuz.arwina.R;
import com.tamiuz.arwina.ResetPassword.ForgetPasswordActivity;
import com.tamiuz.arwina.networking.ApiClient;
import com.tamiuz.arwina.networking.ApiServiceInterface;
import com.tamiuz.arwina.networking.NetworkAvailable;
import com.tamiuz.arwina.utils.DialogUtil;
import com.tamiuz.arwina.utils.SharedPreferenceManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LogInActivity extends AppCompatActivity {

    @BindView(R.id.login_phoneNum_ed_id)
    EditText phone_ed;
    @BindView(R.id.login_password_ed_id)
    EditText password_ed;

    private Intent intent;
    private NetworkAvailable networkAvailable;
    private DialogUtil dialogUtil;
    private UserModel userModel;
    private final String TAG = this.getClass().getSimpleName();
    private SharedPreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        ButterKnife.bind(this);

        networkAvailable = new NetworkAvailable(this);
        dialogUtil = new DialogUtil();
        //   get data from shared preferences ...
        preferenceManager = new SharedPreferenceManager(LogInActivity.this, SharedPreferenceManager.PREFERENCE_NAME);
        phone_ed.setText(preferenceManager.getValue(SharedPreferenceManager.USER_PHONE, ""));//"No name defined" is the default value.
        password_ed.setText(preferenceManager.getValue(SharedPreferenceManager.PASSWORD, "")); //0 is the default value.
    }

    @OnClick(R.id.newUser_txtV_id)
    void newSign() {
        intent = new Intent(LogInActivity.this, SignUpTypeActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.forgetPassword_txtV_id)
    void forgetPass() {
        intent = new Intent(LogInActivity.this, ForgetPasswordActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.lgoin_btn_id)
    void userLogin() {
        if (networkAvailable.isNetworkAvailable()) {
            if (!FUtilsValidation.isEmpty(phone_ed, getString(R.string.required))
                    && !FUtilsValidation.isEmpty(password_ed, getString(R.string.required))) {
                if (password_ed.getText().toString().length() < 6) {
                    password_ed.setError(getResources().getString(R.string.pass_is_weak));
                    password_ed.requestFocus();
                    return;
                }
                String token = FirebaseInstanceId.getInstance().getToken();
                Log.i("ttoken: ", token);
                // Show Progress Dialog
                final ProgressDialog dialog = dialogUtil.showProgressDialog(LogInActivity.this, getString(R.string.logining), false);
                ApiServiceInterface serviceInterface = ApiClient.getClient().create(ApiServiceInterface.class);
                Call<UserLoginModel> call = serviceInterface.userLogin(phone_ed.getText().toString(), password_ed.getText().toString(), token);
                call.enqueue(new Callback<UserLoginModel>() {
                    @Override
                    public void onResponse(Call<UserLoginModel> call, Response<UserLoginModel> response) {
                        Log.i(TAG, "onResponse " + response.toString());
                        userModel = new UserModel();
                        if (response.body().getMessage()) {
                            UserLoginModel.LoginDataObj loginDataObj = response.body().getData();
                            userModel.setId(loginDataObj.getId());
                            userModel.setRole(loginDataObj.getRole());
                            userModel.setName(loginDataObj.getName());
                            userModel.setPhone(loginDataObj.getPhone());
                            userModel.setAddress(loginDataObj.getAddress());
                            userModel.setCommercialreg(loginDataObj.getCommercialreg());
                            userModel.setCommercialregno(loginDataObj.getCommercialregno());
                            userModel.setPromotioncode(loginDataObj.getPromotioncode());
                            userModel.setMyorderscount(loginDataObj.getMyorderscount());
                            if (loginDataObj.getHavedelivery() != null)
                                userModel.setHavedelivery(loginDataObj.getHavedelivery());
                            userModel.setImage(loginDataObj.getImage());

                            dialog.dismiss();
                            // Convert User Data to Gon OBJECT ...
                            Gson gson = new Gson();
                            String user_data = gson.toJson(userModel);
                            preferenceManager.setValue(SharedPreferenceManager.USER_DATA, user_data);

                            Toast.makeText(LogInActivity.this, getString(R.string.login_success), Toast.LENGTH_SHORT).show();
                            // GoTo Main Activity...
                            Intent intent = new Intent(LogInActivity.this, MainActivity.class);
                            intent.putExtra("user_data", userModel);
                            startActivity(intent);
                            finish();
                        } else {
                            dialog.dismiss();
                            Toast.makeText(LogInActivity.this, getString(R.string.plz_check_data), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<UserLoginModel> call, Throwable t) {
                        Log.i(TAG, "onFailure " + t.getMessage());
                        t.printStackTrace();
                        dialog.dismiss();
                        Toast.makeText(LogInActivity.this, getString(R.string.plz_check_data), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        } else {
            Toast.makeText(this, getString(R.string.error_connection), Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.visitorLogin_btn_id)
    void login_asVisitor() {
        intent = new Intent(LogInActivity.this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        preferenceManager.setValue(SharedPreferenceManager.USER_PHONE, phone_ed.getText().toString());
        preferenceManager.setValue(SharedPreferenceManager.PASSWORD, password_ed.getText().toString());
    }
}
