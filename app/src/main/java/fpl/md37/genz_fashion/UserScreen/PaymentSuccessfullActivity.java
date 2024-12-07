package fpl.md37.genz_fashion.UserScreen;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.genz_fashion.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import fpl.md37.genz_fashion.api.HttpRequest;
import fpl.md37.genz_fashion.models.OrderRequest;
import fpl.md37.genz_fashion.models.ProducItem;
import fpl.md37.genz_fashion.models.RemoveProductsRequest;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentSuccessfullActivity extends Fragment {

    Button ViewOrder;
    TextView txtNotication, tv_XN;
    private HttpRequest httpRequest;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.activity_payment_successfull, container, false);

        ViewOrder = v.findViewById(R.id.viewOrderButton);

        ViewOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBottomNav();

                Fragment newFragment = new MyOrderFragment();
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.anim.bounce_in, R.anim.bounce_out);
                transaction.replace(R.id.layout_Payment, newFragment);
                transaction.addToBackStack(null);
                transaction.commit();



            }
        });

        return v;
    }

    private void showBottomNav() {
        BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.bottom_nav);
        if (bottomNavigationView != null) {
            bottomNavigationView.setVisibility(View.GONE);
        }
    }

}