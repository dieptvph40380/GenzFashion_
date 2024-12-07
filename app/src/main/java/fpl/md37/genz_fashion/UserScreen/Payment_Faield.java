package fpl.md37.genz_fashion.UserScreen;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.genz_fashion.R;

public class Payment_Faield extends Fragment {

    Button HomeButton;

    public Payment_Faield() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_payment__faield, container, false);

        HomeButton.findViewById(R.id.HomeButton);

        HomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), MainActivity.class);
                // Khởi chạy Activity
                startActivity(intent);
                // Áp dụng hiệu ứng chuyển động khi chuyển Activity
                getActivity().overridePendingTransition(R.anim.bounce_in, R.anim.bounce_out);

                getActivity().finish();

            }
        });

        return v;
    }
}