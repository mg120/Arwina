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
import com.tamiuz.arwina.networking.ApiClient;
import com.tamiuz.arwina.networking.ApiServiceInterface;
import com.tamiuz.arwina.networking.NetworkAvailable;
import com.tamiuz.arwina.utils.DialogUtil;

public class ForgetPasswordActivity extends AppCompatActivity {

    @BindView(R.id.forgetPass_phoneNum_ed_id)
    EditText phone_ed;

    private NetworkAvailable networkAvailable;
    private DialogUtil dialogUtil;
    private Intent intent ;
    private final String TAG = this.getClass().getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        ButterKnife.bind(this);

        networkAvailable = new NetworkAvailable(this);
        dialogUtil = new DialogUtil();
    }

    @OnClick(R.id.forgetPass_send_btn_id)
    void sendPhone(){
        if (networkAvailable.isNetworkAvailable()){
            if (!FUtilsValidation.isEmpty(phone_ed, getString(R.string.required))) {
                // Show Progress Dialog
                final ProgressDialog dialog = dialogUtil.showProgressDialog(ForgetPasswordActivity.this, getString(R.string.sending), false);
                ApiServiceInterface serviceInterface = ApiClient.getClient().create(ApiServiceInterface.class);
                Call<ForgetPasswordModel> call = serviceInterface.forget_password(phone_ed.getText().toString());
                call.enqueue(new Callback<ForgetPasswordModel>() {
                    @Override
                    public void onResponse(Call<ForgetPasswordModel> call, Response<ForgetPasswordModel> response) {
                        Log.i(TAG, "onResponse " + response.toString());
                        dialog.dismiss();
                        if (response.body().getMessage()){

                            Toast.makeText(ForgetPasswordActivity.this, response.body().getData(), Toast.LENGTH_SHORT).show();
                            // GoTo Main Activity...
                            Intent intent = new Intent(ForgetPasswordActivity.this, SendCodeActivity.class);
                            intent.putExtra("phone" , phone_ed.getText().toString());
                            startActivity(intent);
                            finish();
                        } else {
                            dialog.dismiss();
                            Toast.makeText(ForgetPasswordActivity.this, response.body().getData(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ForgetPasswordModel> call, Throwable t) {
                        Log.i(TAG, "onFailure " + t.getMessage());
                        t.printStackTrace();
                        dialog.dismiss();
                        Toast.makeText(ForgetPasswordActivity.this, getString(R.string.plz_check_data), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }

    @OnClick(R.id.forgetPass_back_txtV_id)
    void goBack(){
        finish();
    }

    @OnClick(R.id.forgetPass_notifications_imageV_id)
    void goToNotifications(){
        intent = new Intent(ForgetPasswordActivity.this, MyNotifications.class);
        startActivity(intent);
    }

    @OnClick(R.id.forgetPass_user_imageV_id)
    void goToProfile(){
        intent = new Intent(ForgetPasswordActivity.this, ProfileActivity.class);
        startActivity(intent);
    }
}
