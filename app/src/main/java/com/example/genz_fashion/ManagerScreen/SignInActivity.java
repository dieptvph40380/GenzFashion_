package com.example.genz_fashion.ManagerScreen;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.genz_fashion.UserScreen.ForgotPassActivity;
import com.example.genz_fashion.UserScreen.MainActivity;
import com.example.genz_fashion.databinding.ActivitySigninBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

public class SignInActivity extends AppCompatActivity {

    private ActivitySigninBinding binding;

    private FirebaseAuth firebaseAuth;

    private String email = "", password = "";
    private String role = "";
    public static final String TAG = "DangNhapActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseAuth = FirebaseAuth.getInstance();

        binding = ActivitySigninBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        listener();
    }

    private void listener() {


        binding.btnsign.setOnClickListener(view -> {
            validateData();
        });

        binding.forgotpass.setOnClickListener(view -> {
            Intent intent = new Intent(SignInActivity.this, ForgotPassActivity.class);
            startActivity(intent);
        });

        binding.toRegister.setOnClickListener(view -> {
            Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
            startActivity(intent);
        });
    }

    private void validateData() {
        email = binding.edtemail.getText().toString().trim();
        password = binding.edtpassword.getText().toString().trim();

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.edtemail.setError("Địa chỉ email không hợp lệ");
            binding.edtemail.requestFocus();
        } else if (TextUtils.isEmpty(password)) {
            binding.edtpassword.setError("Mật khẩu không được để trống");
            binding.edtpassword.requestFocus();
        } else {
            dangNhap();
        }
    }

    private void dangNhap() {
        binding.progressbar.setVisibility(View.VISIBLE);
        binding.btnsign.setVisibility(View.GONE);

        firebaseAuth.signInWithEmailAndPassword(email, md5(password))
                .addOnSuccessListener(authResult -> {
                    checkVaiTro();
                })
                .addOnFailureListener(e -> {
                    binding.progressbar.setVisibility(View.GONE);
                    binding.btnsign.setVisibility(View.VISIBLE);
                    Toast.makeText(SignInActivity.this, "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "onFailure: Lỗi không thể đăng nhập " + e.getMessage());
                });
    }

    private void checkVaiTro() {
        binding.progressbar.setVisibility(View.VISIBLE);
        binding.btnsign.setVisibility(View.GONE);

        String uid = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Accounts");

        ref.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                role = "" + snapshot.child("vaiTro").getValue();

                if ("khachHang".equals(role)) {
                    startActivity(new Intent(SignInActivity.this, MainActivity.class));
                    finish();
                } else if ("admin".equals(role)) {
                    startActivity(new Intent(SignInActivity.this, MainActivity.class));
                    finish();
                }
                else {
                    Toast.makeText(SignInActivity.this, "Tài khoản không tồn tại", Toast.LENGTH_SHORT).show();
                    binding.progressbar.setVisibility(View.GONE);
                    binding.btnsign.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "Database error: " + error.getMessage());
            }
        });
    }

    public static String md5(final String s) {
        final String MD5 = "MD5";
        try {
            MessageDigest digest = java.security.MessageDigest.getInstance(MD5);
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            StringBuilder hexString = new StringBuilder();
            for (byte b : messageDigest) {
                String h = Integer.toHexString(0xFF & b);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
}