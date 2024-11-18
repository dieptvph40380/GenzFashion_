package fpl.md37.genz_fashion.UserScreen;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import fpl.md37.genz_fashion.ManagerScreen.ProfileCustomerFragment;
import fpl.md37.genz_fashion.ManagerScreen.SignInActivity;

import com.bumptech.glide.Glide;
import com.example.genz_fashion.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;


public class ProfileFragment extends Fragment {
    private LinearLayout layout_your_file, layout_payment, layout_order, layout_setting, layout_help, layout_privacy, layout_out;
    private ImageView btnbackProfile, imgProfile;
    private TextView tvProfileName;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        btnbackProfile = view.findViewById(R.id.btnBack_profile);
        imgProfile = view.findViewById(R.id.img_ProfileView);
        tvProfileName = view.findViewById(R.id.tvProfileName);

        // Lấy thông tin từ SharedPreferences
        SharedPreferences preferences = getActivity().getSharedPreferences("user_info", Activity.MODE_PRIVATE);
        String name = preferences.getString("name", "No Name");
        String avatar = preferences.getString("avatar", "");
        if (name.isEmpty()) {
            Log.d("ProfileFragment", "Name is empty!");
        }
        if (avatar.isEmpty()) {
            Log.d("ProfileFragment", "Avatar is empty!");
        }

        // Cập nhật tên trong ProfileFragment
        tvProfileName.setText(name);

        // Nếu có avatar mới, hiển thị
        if (!avatar.isEmpty()) {
            byte[] decodedString = Base64.decode(avatar, Base64.DEFAULT);
            Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            Glide.with(getContext())
                    .load(decodedBitmap)
                    .into(imgProfile);
        }

        btnbackProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Fragment newFragment = new HomeFragment();
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.anim.bounce_in, R.anim.bounce_out);
                transaction.replace(R.id.frameLayout_profile, newFragment);
                transaction.addToBackStack(null);
                transaction.commit();

                BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.bottom_nav);

                bottomNavigationView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        bottomNavigationView.setSelectedItemId(R.id.nav_home);
                    }
                }, 300);
            }
        });

        layout_your_file = view.findViewById(R.id.profile_profile);
        layout_your_file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(new PersonalInformationFragment_(), R.id.frameLayout);

            }
        });
        layout_payment = view.findViewById(R.id.profile_payment);
        layout_payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(new PayMothodsFragment(), R.id.frameLayout);

            }
        });
        layout_order = view.findViewById(R.id.profile_cart);
        layout_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToActivity(MyOrderActivity.class);

            }
        });
        layout_setting = view.findViewById(R.id.profile_setting);
        layout_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragment(new SettingFragment(), R.id.frameLayout);
            }
        });
        layout_help = view.findViewById(R.id.profile_help);
        layout_help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragment(new HelpCenterFragment(), R.id.frameLayout);
            }
        });
        layout_privacy = view.findViewById(R.id.profile_policy);
        layout_privacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragment(new PrivacyPolicyFragment(), R.id.frameLayout);
            }
        });

        layout_out = view.findViewById(R.id.profile_out);
        layout_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();

            }
        });
        return view;
    }

    private void logout() {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(getActivity(), SignInActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Xóa ngăn xếp
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.rotate_in, R.anim.zoom_out);

    }

    private void replaceFragment(Fragment targetFragment, int frameId) {
        if (getActivity() != null) {
            View bottomNavigationView = getActivity().findViewById(R.id.bottom_nav);
            if (bottomNavigationView != null) {
                bottomNavigationView.setVisibility(View.GONE);
            }
            getActivity().getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(R.anim.sile_right, R.anim.slide_left)
                    .replace(frameId, targetFragment)
                    .addToBackStack(null)
                    .commit();
        }
    }

    private void navigateToActivity(Class<?> targetActivity) {
        if (getActivity() != null) {
            Intent intent = new Intent(getActivity(), targetActivity);
            startActivity(intent);
        }
    }
}