package fpl.md37.genz_fashion.UserScreen;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.genz_fashion.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import fpl.md37.genz_fashion.ManagerScreen.Password_Manager;
import fpl.md37.genz_fashion.ManagerScreen.SignInActivity;
import fpl.md37.genz_fashion.UserScreen.ProfileFragment;

public class SettingFragment extends Fragment {
    private ImageView btnBack_setting;
    private LinearLayout lin_pass, deleteaccount; // Xử lý deleteaccount

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        btnBack_setting = view.findViewById(R.id.btnBack_setting);
        lin_pass = view.findViewById(R.id.PasswordManager);
        deleteaccount = view.findViewById(R.id.DeleteAccount); // Thêm xử lý deleteaccount

        btnBack_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBottomNav();
                Fragment newFragment = new ProfileFragment();
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out); // hiệu ứng mở dần
                transaction.replace(R.id.frameLayout_setting, newFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        lin_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragment(new Password_Manager(), R.id.frameLayout);
            }
        });

        deleteaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmDeleteAccount(); // Xử lý xóa tài khoản
            }
        });

        return view;
    }

    private void confirmDeleteAccount() {
        new AlertDialog.Builder(getContext())
                .setTitle("Delete Account")
                .setMessage("Are you sure you want to delete this account? This action cannot be undone.")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deleteAccount();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void deleteAccount() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            user.delete().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(getContext(), "The account has been successfully deleted.", Toast.LENGTH_SHORT).show();
                    // Điều hướng về màn hình đăng nhập
                    navigateToLoginScreen();
                } else {
                    Toast.makeText(getContext(), "Account deletion failed: " + task.getException().getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            });
        } else {
            Toast.makeText(getContext(), "Current user not found.", Toast.LENGTH_SHORT).show();
        }
    }

    private void navigateToLoginScreen() {
        // Điều hướng về màn hình đăng nhập
        if (getActivity() != null) {
            getActivity().finish();
            startActivity(new Intent(getContext(), SignInActivity.class)); // Cập nhật LoginActivity nếu cần
        }
    }

    private void showBottomNav() {
        BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.bottom_nav);
        if (bottomNavigationView != null) {
            bottomNavigationView.setVisibility(View.VISIBLE);
        }
    }

    private void replaceFragment(Fragment targetFragment, int frameId) {
        if (getActivity() != null) {
            View bottomNavigationView = getActivity().findViewById(R.id.bottom_nav);
            if (bottomNavigationView != null) {
                bottomNavigationView.setVisibility(View.GONE);
            }
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(frameId, targetFragment)
                    .addToBackStack(null)
                    .commit();
        }
    }
}
