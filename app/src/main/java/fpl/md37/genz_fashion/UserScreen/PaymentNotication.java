package fpl.md37.genz_fashion.UserScreen;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.genz_fashion.R;


public class PaymentNotication extends AppCompatActivity {
    TextView txtNotication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_notication);

        txtNotication = findViewById(R.id.tv_Notification);
        Intent intent = getIntent();
        String result = intent.getStringExtra("result");

        txtNotication.setText(result);

        // Sử dụng Handler để chuyển màn hình sau 2 giây
        new Handler().postDelayed(() -> {
            if ("Thanh toán thành công".equals(result)) {
                // Chuyển tới MyOrderFragment
                navigateToFragment(new MyOrderFragment());
            } else if ("Hủy thanh toán".equals(result)) {
                // Chuyển tới CheckOutActivity
                Intent nextIntent = new Intent(PaymentNotication.this, CheckOutActivity.class);
                startActivity(nextIntent);
            } else {
                // Chuyển tới MainActivity
                Intent nextIntent = new Intent(PaymentNotication.this, MainActivity.class);
                startActivity(nextIntent);
            }
            finish(); // Đóng PaymentNotication sau khi chuyển màn hình
        }, 2000); // 2 giây
    }

    // Hàm chuyển tới một Fragment
    private void navigateToFragment(MyOrderFragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, fragment) // `R.id.container` là vùng chứa Fragment trong MainActivity
                .addToBackStack(null) // Lưu trạng thái để quay lại nếu cần
                .commit();
    }
}
