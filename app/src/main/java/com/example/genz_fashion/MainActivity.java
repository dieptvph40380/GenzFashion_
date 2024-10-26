package com.example.genz_fashion;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.genz_fashion.fragmentUser.Cart_Fragment;
import com.example.genz_fashion.fragmentUser.Chat_Fragment;
import com.example.genz_fashion.fragmentUser.FragmentSetting;
import com.example.genz_fashion.fragmentUser.HomeFragment;
import com.example.genz_fashion.fragmentUser.MyWishlistFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {


    private BottomNavigationView bottomNavigationView;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize bottom navigation view
        bottomNavigationView = findViewById(R.id.bottom_nav);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.nav_home) {
                    replaceFragment(new HomeFragment());
                    return true;
                } else if (menuItem.getItemId() == R.id.nav_bag) {
                    replaceFragment(new Cart_Fragment());
                    return true;
                }else if (menuItem.getItemId() == R.id.nav_favorite) {
                    replaceFragment(new MyWishlistFragment());
                    return true;
                }else if (menuItem.getItemId() == R.id.nav_chat) {
                    replaceFragment(new Chat_Fragment());
                    return true;
                }else if (menuItem.getItemId() == R.id.nav_profile) {
                    replaceFragment(new FragmentSetting());
                    return true;
                }
                return false;
            }
        });

        // Initially replace fragment with Home fragment
        replaceFragment(new HomeFragment());
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
    }

}