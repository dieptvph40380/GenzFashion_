package com.example.genz_fashion.ManagerScreen;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.genz_fashion.R;
import com.example.genz_fashion.UserScreen.MainActivity;
import com.example.genz_fashion.UserScreen.OTPForgotPassActivity;
import com.example.genz_fashion.UserScreen.SendOtpFragment;
import com.example.genz_fashion.databinding.ActivitySignUpBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity {

    private ActivitySignUpBinding binding;

    private String username, email, password, passwordConfirm;

    public static final String KEY_USERNAME = "userName";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_PASSWORD = "password";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        listener(); // Handle click events
    }

    private void listener() {
        binding.btnBack.setOnClickListener(view -> {
            // Finish the activity to go back
            finish();
        });

        binding.edtPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String passwordInput = charSequence.toString();
                if (passwordInput.length() >= 6) {
                    Pattern pattern = Pattern.compile("[^a-zA-Z0-9]");
                    Matcher matcher = pattern.matcher(passwordInput);
                    boolean passwordsMatch = matcher.find();
                    if (passwordsMatch) {
                        binding.passwordTil.setHelperText("Your Password is Strong");
                        binding.passwordTil.setHelperTextColor(ColorStateList.valueOf(getResources().getColor(R.color.green)));
                        binding.passwordTil.setError("");
                    } else {
                        binding.passwordTil.setError("Please ensure your password contains at least one lowercase letter, one uppercase letter, and one special character.");
                    }
                } else {
                    binding.passwordTil.setHelperText("Password must be 6 characters long");
                    binding.passwordTil.setError("");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });

        binding.buttonDangKy.setOnClickListener(view -> {
            validateData();
        });

        binding.tvDangNhap.setOnClickListener(view -> {
            // Navigate to the login activity
            Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
            startActivity(intent);
        });
    }

    private void validateData() {
        username = binding.edtUsername.getText().toString().trim();
        email = binding.edtEmail.getText().toString().trim();
        password = binding.edtPassword.getText().toString().trim();
        passwordConfirm = binding.edtNhaplaiPassword.getText().toString().trim();

        if (TextUtils.isEmpty(username)) {
            binding.edtUsername.setError("Bạn cần nhập đủ thông tin");
            binding.edtUsername.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(email)) {
            binding.edtEmail.setError("Bạn cần nhập đủ thông tin");
            binding.edtEmail.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            binding.edtPassword.setError("Bạn cần nhập đủ thông tin");
            binding.edtPassword.requestFocus();
            return;
        } else if (!password.equals(passwordConfirm)) {
            binding.edtNhaplaiPassword.setError("Mật khẩu không khớp");
            binding.edtNhaplaiPassword.requestFocus();
            return;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.edtEmail.setError("Email không đúng định dạng");
            binding.edtEmail.requestFocus();
            return;
        } else {
            dangKy();
        }
    }

    private void dangKy() {
        // Pass data using Intent
        Intent intent = new Intent(SignUpActivity.this, SendOtpFragment.class);
        intent.putExtra(KEY_USERNAME, username);
        intent.putExtra(KEY_EMAIL, email);
        intent.putExtra(KEY_PASSWORD, password);
        startActivity(intent);
    }
}