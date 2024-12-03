package fpl.md37.genz_fashion.UserScreen;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.widget.ViewPager2;

import com.example.genz_fashion.R;
import fpl.md37.genz_fashion.adapter.ViewPagerOderAdapter;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class MyOrderFragment extends Fragment {

    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private ImageView btnBack;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_order, container, false);

        // Ánh xạ các thành phần giao diện
        tabLayout = view.findViewById(R.id.tabLayout);
        viewPager = view.findViewById(R.id.viewPager);
//        btnBack = view.findViewById(R.id.btnBack_order);

        // Thiết lập Adapter cho ViewPager
        ViewPagerOderAdapter adapter = new ViewPagerOderAdapter(requireActivity());
        viewPager.setAdapter(adapter);


//        // Nút Back
//        btnBack.setOnClickListener(v -> {
//            showBottomNav();
//            Fragment newFragment = new ProfileFragment();
//            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
//            transaction.setCustomAnimations(R.anim.bounce_in, R.anim.bounce_out);
//            transaction.replace(R.id.frameLayout_myorder, newFragment);
//            transaction.addToBackStack(null);
//            transaction.commit();
//        });

        // Thiết lập TabLayout và ViewPager
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            switch (position) {
                case 0:
                    tab.setText("Active");
                    break;
                case 1:
                    tab.setText("Completed");
                    break;
                case 2:
                    tab.setText("Cancelled");
                    break;
            }
        }).attach();

        return view;
    }
    private void showBottomNav() {
        BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.bottom_nav);
        if (bottomNavigationView != null) {
            bottomNavigationView.setVisibility(View.VISIBLE);
        }
    }
}
