package com.example.genz_fashion.adapter;

import android.support.annotation.NonNull;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.genz_fashion.fragmentUser.ActiveFragment;
import com.example.genz_fashion.fragmentUser.CancelledFragment;
import com.example.genz_fashion.fragmentUser.CheckOutFragment;
import com.example.genz_fashion.fragmentUser.CompletedFragment;

public class ViewPagerOderAdapter extends FragmentStateAdapter {
    public ViewPagerOderAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new ActiveFragment();
            case 1:
                return new CompletedFragment();
            case 2:
                return new CancelledFragment();
            default:
                return new ActiveFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
