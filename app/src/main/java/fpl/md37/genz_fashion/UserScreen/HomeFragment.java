package fpl.md37.genz_fashion.UserScreen;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.genz_fashion.R;
import com.example.genz_fashion.databinding.FragmentHomeBinding;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private CountDownTimer countDownTimer;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize image slider
        ArrayList<SlideModel> slideModels = new ArrayList<>();
        slideModels.add(new SlideModel(R.drawable.banner1, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.banner2, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.banner3, ScaleTypes.FIT));
        binding.slide.setImageList(slideModels, ScaleTypes.FIT);

        // Start countdown timer
        startCountdownTimer(2 * 60 * 60 * 1000); // 2 hours
    }

    private void startCountdownTimer(long duration) {
        countDownTimer = new CountDownTimer(duration, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                int hours = (int) (millisUntilFinished / (1000 * 60 * 60));
                int minutes = (int) (millisUntilFinished % (1000 * 60 * 60)) / (1000 * 60);
                int seconds = (int) ((millisUntilFinished % (1000 * 60)) / 1000);

                binding.tvHours.setText(String.format("%02d", hours));
                binding.tvMinutes.setText(String.format("%02d", minutes));
                binding.tvSeconds.setText(String.format("%02d", seconds));
            }

            @Override
            public void onFinish() {
                binding.tvHours.setText("00");
                binding.tvMinutes.setText("00");
                binding.tvSeconds.setText("00");
            }
        };
        countDownTimer.start();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        binding = null; // Avoid memory leaks
    }
}
