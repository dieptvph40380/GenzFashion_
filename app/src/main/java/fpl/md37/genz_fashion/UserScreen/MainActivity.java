package fpl.md37.genz_fashion.UserScreen;

import static android.app.PendingIntent.getActivity;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.genz_fashion.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private static final String CHANNEL_ID = "order_notifications";

    private BottomNavigationView bottomNavigationView;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Tạo Notification Channel (chỉ thực hiện trên Android 8.0 trở lên)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Order Notifications";
            String description = "Notifications for order updates";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            // Đăng ký kênh với hệ thống
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }

        // Initialize bottom navigation view
        bottomNavigationView = findViewById(R.id.bottom_nav);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.nav_home) {
                    replaceFragment(new HomeFragment());
                    return true;
                } else if (menuItem.getItemId() == R.id.nav_favorite) {
                    replaceFragment(new MyWishlistFragment());
                    return true;
                } else if (menuItem.getItemId() == R.id.nav_chat) {
                    replaceFragment(new MyOrderFragment());
                    return true;
                } else if (menuItem.getItemId() == R.id.nav_profile) {
                    replaceFragment(new ProfileFragment());
                    return true;
                }
                return false;
            }
        });

        // Kiểm tra Intent và hiển thị Fragment tương ứng
        handleIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (intent != null) {
            String fragmentName = intent.getStringExtra("navigate_to_fragment");
            if (fragmentName != null) {
                switch (fragmentName) {
                    case "MyOrderFragment":
                        bottomNavigationView.setSelectedItemId(R.id.nav_chat); // Cập nhật trạng thái
                        replaceFragment(new MyOrderFragment());
                        break;
                    case "MyWishlistFragment":
                        bottomNavigationView.setSelectedItemId(R.id.nav_favorite);
                        replaceFragment(new MyWishlistFragment());
                        break;
                    case "ProfileFragment":
                        bottomNavigationView.setSelectedItemId(R.id.nav_profile);
                        replaceFragment(new ProfileFragment());
                        break;
                        case "HomeFragment":
                            bottomNavigationView.setSelectedItemId(R.id.nav_home);
                            replaceFragment(new HomeFragment());
                            break;
                    default:
                        bottomNavigationView.setSelectedItemId(R.id.nav_home);
                        replaceFragment(new HomeFragment());
                        break;
                }
            } else {
                // Mặc định hiển thị HomeFragment
                bottomNavigationView.setSelectedItemId(R.id.nav_home);
                replaceFragment(new HomeFragment());
            }
        }
    }


    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
    }
}