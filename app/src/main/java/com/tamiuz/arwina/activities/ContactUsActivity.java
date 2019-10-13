package com.tamiuz.arwina.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import com.fourhcode.forhutils.FUtilsValidation;
import com.tamiuz.arwina.Models.ContactUsResponseModel;
import com.tamiuz.arwina.R;
import com.tamiuz.arwina.networking.ApiClient;
import com.tamiuz.arwina.networking.ApiServiceInterface;
import com.tamiuz.arwina.networking.NetworkAvailable;
import com.tamiuz.arwina.utils.DialogUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ContactUsActivity extends AppCompatActivity {

    @BindView(R.id.contactUs_name_ed_id)
    EditText name_ed;
    @BindView(R.id.contactUs_phone_ed_id)
    EditText phone_ed;
    @BindView(R.id.contactUs_message_ed_id)
    EditText message_ed;

    private NetworkAvailable networkAvailable;
    private DialogUtil dialogUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
        ButterKnife.bind(this);

        networkAvailable = new NetworkAvailable(this);
        dialogUtil = new DialogUtil();
    }

    @OnClick(R.id.send_message_btn_id)
    void sendMessage() {
        if (networkAvailable.isNetworkAvailable()) {
            if (!FUtilsValidation.isEmpty(name_ed, getString(R.string.required))
                    && !FUtilsValidation.isEmpty(phone_ed, getString(R.string.required))
                    && !FUtilsValidation.isEmpty(message_ed, getString(R.string.required))) {
                String token = "123456";
                // Show Progress Dialog
                final ProgressDialog dialog = dialogUtil.showProgressDialog(ContactUsActivity.this, getString(R.string.sending), false);
                ApiServiceInterface serviceInterface = ApiClient.getClient().create(ApiServiceInterface.class);
                Call<ContactUsResponseModel> call = serviceInterface.contact_Message(name_ed.getText().toString(), phone_ed.getText().toString(), message_ed.getText().toString());
                call.enqueue(new Callback<ContactUsResponseModel>() {
                    @Override
                    public void onResponse(Call<ContactUsResponseModel> call, Response<ContactUsResponseModel> response) {
                        dialog.dismiss();
                        if (response.body().getMessage()) {
                            Toast.makeText(ContactUsActivity.this, response.body().getData(), Toast.LENGTH_SHORT).show();
                            name_ed.setText("");
                            phone_ed.setText("");
                            message_ed.setText("");
                        } else {
                            Toast.makeText(ContactUsActivity.this, response.body().getData(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ContactUsResponseModel> call, Throwable t) {
                        dialog.dismiss();
                        t.printStackTrace();
                    }
                });
            }
        } else {
            Toast.makeText(this, getString(R.string.error_connection), Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.contactUs_back_txtV_id)
    void goBack() {
        finish();
    }

    @OnClick(R.id.contactUs_notifications_imageV_id)
    void goToNotifications(){
        Intent intent = new Intent(ContactUsActivity.this, MyNotifications.class);
        startActivity(intent);
    }

    @OnClick(R.id.contactUs_user_imageV_id)
    void gotoProfile(){
        Intent intent = new Intent(ContactUsActivity.this, ProfileActivity.class);
        intent.putExtra("role" , MainActivity.userModel.getRole());
        startActivity(intent);
    }
}
