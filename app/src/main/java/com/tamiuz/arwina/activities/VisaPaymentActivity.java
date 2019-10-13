package com.tamiuz.arwina.activities;

import android.app.DatePickerDialog;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.fourhcode.forhutils.FUtilsValidation;
import com.tamiuz.arwina.R;
import com.tamiuz.arwina.networking.NetworkAvailable;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class VisaPaymentActivity extends AppCompatActivity {

    @BindView(R.id.cardNumber_ed_id)
    EditText cardNumber_ed;
    @BindView(R.id.expireDate_txtV_id)
    TextView expireDate_txtV;
    @BindView(R.id.cvv_ed_id)
    EditText cvv_ed;
    @BindView(R.id.holder_cardNum_ed_id)
    EditText holder_cardNum_ed;
    @BindView(R.id.cost_val_txtV_id)
    TextView cost_val_txtV;

    private NetworkAvailable networkAvailable;
    private Calendar myCalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visa_payment);
        ButterKnife.bind(this);

        networkAvailable = new NetworkAvailable(this);
        myCalendar = Calendar.getInstance();
    }

    @OnClick(R.id.payment_btn_id)
    void payNow() {
        if (networkAvailable.isNetworkAvailable()) {
            if (!FUtilsValidation.isEmpty(cardNumber_ed, getString(R.string.required))
                    && !FUtilsValidation.isEmpty(cvv_ed, getString(R.string.required))
                    && !FUtilsValidation.isEmpty(holder_cardNum_ed, getString(R.string.required))) {
                if (expireDate_txtV.getText().toString().equals(getString(R.string.expire_date))) {
                    Toast.makeText(this, getString(R.string.select_date_first), Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        } else {
            Toast.makeText(this, getString(R.string.error_connection), Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.visaPayment_back_txtV_id)
    void goBack() {
        finish();
    }

    @OnClick(R.id.visaPayment_notifications_imageV_id)
    void goToNotifications() {
        if (MainActivity.user_Id != 0) {
            Intent intent = new Intent(VisaPaymentActivity.this, MyNotifications.class);
            startActivity(intent);
        } else {
            startActivity(new Intent(VisaPaymentActivity.this, LogInActivity.class));
        }
    }

    @OnClick(R.id.visaPayment_user_imageV_id)
    void goToProfile() {
        if (MainActivity.user_Id != 0) {
            Intent intent = new Intent(VisaPaymentActivity.this, ProfileActivity.class);
            startActivity(intent);
        } else {
            startActivity(new Intent(VisaPaymentActivity.this, LogInActivity.class));
        }
    }

    @OnClick(R.id.expireDate_txtV_id)
    void getDate() {
        final DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                String myFormat = "yyyy/MM/dd";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                expireDate_txtV.setText(sdf.format(myCalendar.getTime()));
            }
        };
        DatePickerDialog mDate = new DatePickerDialog(VisaPaymentActivity.this, datePickerListener, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH));
        mDate.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        mDate.show();
    }
}
