package com.example.genz_fashion.UserScreen;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.genz_fashion.R;
import com.example.genz_fashion.databinding.FragmentVerificationSuccessfulBinding;


public class VerificationSuccessfulFragment extends Fragment {

    private FragmentVerificationSuccessfulBinding binding;

    public static final String KEY_EMAIL = "email";

    private String email;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentVerificationSuccessfulBinding.inflate(inflater, container, false);

        nhanDuLieuTruyenSang();

        listener();

        return binding.getRoot();
    }

    private void nhanDuLieuTruyenSang() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            email = bundle.getString(KEY_EMAIL);
            binding.tvEmail.setText("Your account has been successfully created with the email address: " + email);
        }
    }

    private void listener() {
        binding.buttonTiepTuc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
    }
}