package com.tamiuz.arwina.ResetPassword;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.fourhcode.forhutils.FUtilsValidation;
import com.tamiuz.arwina.activities.MyNotifications;
import com.tamiuz.arwina.activities.ProfileActivity;
import com.tamiuz.arwina.Models.ForgetPasswordModel;
import com.tamiuz.arwina.R;
import com.tamiuz.arwina.activities.LogInActivity;
import com.tamiuz.arwina.networking.ApiClient;
import com.tamiuz.arwina.networking.ApiServiceInterface;
import com.tamiuz.arwina.networking.NetworkAvailable;
import com.tamiuz.arwina.utils.DialogUtil;

public class NewPasswordActivity extends AppCompatActivity {

    @BindView(R.id.newPassword_ed_id)
    EditText newPassword_ed;

    private NetworkAvailable networkAvailable;
    private DialogUtil dialogUtil;
    private Intent intent ;
    private final String TAG = this.getClass().getSimpleName();
    private String phone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        ButterKnife.bind(this);

        networkAvailable = new NetworkAvailable(this);
        dialogUtil = new DialogUtil();

        if (getIntent().hasExtra("phone")){
            phone = getIntent().getStringExtra("phone");
        }
    }

    @OnClick(R.id.newPassword_send_btn_id)
    void sendNewPassword(){
        if (networkAvailable.isNetworkAvailable()){
            if (!FUtilsValidation.isEmpty(newPassword_ed, getString(R.string.required))) {
                // Show Progress Dialog
                final ProgressDialog dialog = dialogUtil.showProgressDialog(NewPasswordActivity.this, getString(R.string.sending), false);
                ApiServiceInterface serviceInterface = ApiClient.getClient().create(ApiServiceInterface.class);
                Call<ForgetPasswordModel> call = serviceInterface.new_password( phone, newPassword_ed.getText().toString());
                call.enqueue(new Callback<ForgetPasswordModel>() {
                    @Override
                    public void onResponse(Call<ForgetPasswordModel> call, Response<ForgetPasswordModel> response) {
                        Log.i(TAG, "onResponse " + response.toString());
                        dialog.dismiss();
                        if (response.body().getMessage()){
                            Toast.makeText(NewPasswordActivity.this, response.body().getData(), Toast.LENGTH_SHORT).show();
                            // GoTo Main Activity...
                            Intent intent = new Intent(NewPasswordActivity.this, LogInActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            dialog.dismiss();
                            Toast.makeText(NewPasswordActivity.this, response.body().getData(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ForgetPasswordModel> call, Throwable t) {
                        Log.i(TAG, "onFailure " + t.getMessage());
                        t.printStackTrace();
                        dialog.dismiss();
                        Toast.makeText(NewPasswordActivity.this, getString(R.string.plz_check_data), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }

    @OnClick(R.id.newPassword_back_txtV_id)
    void goBack(){
        finish();
    }

    @OnClick(R.id.newPassword_notifications_imageV_id)
    void goToNotifications(){
        intent = new Intent(NewPasswordActivity.this, MyNotifications.class);
        startActivity(intent);
    }

    @OnClick(R.id.newPassword_user_imageV_id)
    void goToProfile(){
        intent = new Intent(NewPasswordActivity.this, ProfileActivity.class);
        startActivity(intent);
    }
}
