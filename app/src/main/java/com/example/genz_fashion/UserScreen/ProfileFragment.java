package com.example.genz_fashion.UserScreen;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.genz_fashion.ManagerScreen.ProfileCustomerFragment;
import com.example.genz_fashion.ManagerScreen.SignInActivity;
import com.example.genz_fashion.R;


public class ProfileFragment extends Fragment {
private LinearLayout layout_your_file, layout_payment, layout_order, layout_setting, layout_help, layout_privacy, layout_out;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
         layout_your_file=view.findViewById(R.id.profile_profile);
        layout_your_file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(new ProfileCustomerFragment(), R.id.frameLayout);

            }
        });
        layout_payment=view.findViewById(R.id.profile_payment);
        layout_payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(new PayMothodsFragment(), R.id.frameLayout);

            }
        });
        layout_order=view.findViewById(R.id.profile_cart);
        layout_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToActivity(MyOrderActivity.class);

            }
        });
        layout_setting=view.findViewById(R.id.profile_setting);
        layout_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragment(new SettingFragment(), R.id.frameLayout);
            }
        });
        layout_help=view.findViewById(R.id.profile_help);
        layout_help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragment(new HelpCenterFragment(), R.id.frameLayout);
            }
        });
        layout_privacy=view.findViewById(R.id.profile_policy);
        layout_privacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragment(new PrivacyPolicyFragment(), R.id.frameLayout);
            }
        });
        layout_out=view.findViewById(R.id.profile_out);
        layout_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToActivity(SignInActivity.class);

            }
        });
        return  view;
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
    private void navigateToActivity(Class<?> targetActivity) {
        if (getActivity() != null) {
            Intent intent = new Intent(getActivity(), targetActivity);
            startActivity(intent);
        }
    }
}