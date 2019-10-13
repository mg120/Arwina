package com.tamiuz.arwina.activities;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.tamiuz.arwina.R;

public class AboutCompanyActivity extends AppCompatActivity {

    @BindView(R.id.aboutUs_content_txtV_id)
    TextView about_txtV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_company);
        ButterKnife.bind(this);

        about_txtV.setText("نبذة عن إروينا ؟ \n تطبيق أروينا هو أحد التطبيقات الرائدة في مجال توصيل المياة للمستهلك أيا كان موقعه \n\n");
        about_txtV.append("أين توجد خدمة أروينا ؟ \n");
        about_txtV.append("حاليا الخدمة متاحة في مدينة الرياض وقريبا في كل مناطق المملكة باْذن لله \n\n");
        about_txtV.append("كيفية استخدام التطبيق ؟ \n");
        about_txtV.append("بعد أن تمت عملية التحميل تستطيع الدخول مباشرة الى التطبيق وتحديد موقع التوصيل الذي ترغب وبناء عليه ستظهر شركات و موزعين المياة بجميع أنواعها وأحجامها المختلفة للتصفح \n");
        about_txtV.append("ولكن حتى نسعد بخدمتك ويتم توصيل طلبك يجب تسجيل الدخول في حال لديك حساب أو أنشى حساب جديد من خلاله الضغط على أيقونة تسجيل الدخول وأختيار إنشاء حساب جديد في أسفل الصفحة ومن ثم تعبئة البيانات المطلوبة الاسم و كلمة المرور وتاكيد كلمة المرور أخير تاكيد رقم الجوال من خلال إدخاله ويصل كود تفعيل على رقمك وبهذا يكتمل دخولك وحياك الله معنا");
        about_txtV.append("\n\n");
        about_txtV.append("أروينا تقدم لعملائها مميزات متعددة :\n");
        about_txtV.append("١/طلب توصيل المياة أصبح سهل جدا \n ٢/ إمكانية الدفع الامن من خلال بطاقة الائتمان \n ٣/جدولة الطلبات مسبقا \n ٤/ طلب توصيل المياة في الوقت و التاريخ المحدد اَي كان الموقع .");
    }

    @OnClick(R.id.aboutUs_back_txtV_id)
    void goBack() {
        finish();
    }

    @OnClick(R.id.aboutUs_user_imageV_id)
    void goToProfile() {
        if (MainActivity.user_Id != 0) {
            Intent intent = new Intent(AboutCompanyActivity.this, ProfileActivity.class);
            startActivity(intent);
        } else {
            startActivity(new Intent(AboutCompanyActivity.this, LogInActivity.class));
        }
    }

    @OnClick(R.id.aboutUs_notifications_imageV_id)
    void goToNotifications() {
        if (MainActivity.user_Id != 0) {
            Intent intent = new Intent(AboutCompanyActivity.this, MyNotifications.class);
            intent.putExtra("role", MainActivity.userModel.getRole());
            startActivity(intent);
        } else {
            startActivity(new Intent(AboutCompanyActivity.this, LogInActivity.class));
        }
    }
}
