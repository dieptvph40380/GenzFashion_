package com.example.genz_fashion.UserScreen;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.genz_fashion.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class PayMothodsFragment extends Fragment {

   private ImageView btnBack_payment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pay_mothods, container, false);
        btnBack_payment = view.findViewById(R.id.btnBack_payment);


        btnBack_payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showBottomNav();
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
        return view;
    }

    private void showBottomNav() {
        BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.bottom_nav);
        if (bottomNavigationView != null) {
            bottomNavigationView.setVisibility(View.VISIBLE);
        }
    }

}