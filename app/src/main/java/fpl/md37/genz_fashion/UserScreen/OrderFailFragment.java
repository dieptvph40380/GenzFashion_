package fpl.md37.genz_fashion.UserScreen;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.genz_fashion.R;
import com.example.genz_fashion.databinding.FragmentOrderFailBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class OrderFailFragment extends Fragment {

    private FragmentOrderFailBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout using ViewBinding
        binding = FragmentOrderFailBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Set onClickListener for the home button
        binding.homebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFragment(new HomeFragment());
            }
        });
    }

    private void openFragment(Fragment fragment) {
        showBottomNav();
        FragmentTransaction transaction = requireActivity()
                .getSupportFragmentManager()
                .beginTransaction();
        transaction.replace(R.id.frameLayout_paymentfail, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void showBottomNav() {
        BottomNavigationView bottomNavigationView = requireActivity().findViewById(R.id.bottom_nav);
        if (bottomNavigationView != null) {
            bottomNavigationView.setVisibility(View.VISIBLE);
        }
    }

}
