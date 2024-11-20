package fpl.md37.genz_fashion.UserScreen;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.genz_fashion.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;

import java.util.List;

import fpl.md37.genz_fashion.adapter.AdapterCart;
import fpl.md37.genz_fashion.adapter.CheckOutAdapter;
import fpl.md37.genz_fashion.api.HttpRequest;
import fpl.md37.genz_fashion.models.CartData;
import fpl.md37.genz_fashion.models.Client;
import fpl.md37.genz_fashion.models.ProducItem;
import fpl.md37.genz_fashion.models.ResponseCart;
import fpl.md37.genz_fashion.utils.AndroidUtil;
import fpl.md37.genz_fashion.utils.FirebaseUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CheckOutFragment extends Fragment {

    TextView tvName,tvPhone,tvAddress;
    ImageView profilePic;
    Button updateProfileBtn;
    Client currentUserModel;
    ActivityResultLauncher<Intent> imagePickLauncher;
    Uri selectedImageUri;
    Context safeContext; // Lưu trữ context an toàn
    private RecyclerView recyclerView;
    private CheckOutAdapter adapter;
    private TextView txtotal;
    private HttpRequest httpRequest;

    public CheckOutFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        safeContext = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_check_out, container, false);

        tvName=v.findViewById(R.id.tv_ClName);
        tvPhone=v.findViewById(R.id.tv_ClPhone);
        tvAddress=v.findViewById(R.id.tv_ClAddress);

        getUserData();

        recyclerView = v.findViewById(R.id.rcv_ClCheckOut);
        txtotal = v.findViewById(R.id.total_cart);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new CheckOutAdapter(getContext());
        recyclerView.setAdapter(adapter);
        httpRequest = new HttpRequest();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            httpRequest.callApi().getOrder(userId).enqueue(getCartID);
        }

        return v;
    }

    void getUserData() {
        FirebaseUtil.currentUserDetails().get().addOnCompleteListener(task -> {
            if (isAdded() && safeContext != null) {
                if (task.isSuccessful() && task.getResult() != null) {
                    currentUserModel = task.getResult().toObject(Client.class);

                    if (currentUserModel != null) {
                        tvName.setText(currentUserModel.getName());
                        tvPhone.setText(currentUserModel.getPhone());
                        tvAddress.setText(currentUserModel.getAddress());

                    } else {
                        AndroidUtil.showToast(safeContext, "User data not found.");
                    }
                } else {
                    AndroidUtil.showToast(safeContext, "Failed to fetch user data.");
                }
            }
        });
    }

    Callback<ResponseCart> getCartID = new Callback<ResponseCart>() {
        @Override
        public void onResponse(Call<ResponseCart> call, Response<ResponseCart> response) {
            Log.d("zzzz Call", "URL: " + call.request().url());
            if (response.isSuccessful()) {
                // Log toàn bộ phản hồi để kiểm tra
                String jsonResponse = new Gson().toJson(response.body());
                Log.d("zzzzz Response", "Response: " + jsonResponse);

                CartData cartData = response.body().getData();
//                double totalPrice = cartData.getTotalPrice();
                List<ProducItem> products = cartData.getProducts();

//                // Hiển thị tổng giá tiền
//                txtotal.setText("Total Price: $" + totalPrice);

                // Hiển thị danh sách sản phẩm trong giỏ hàng
                adapter.setProducts(products);
            } else {
                Log.e("zzzzz Error", "Failed to fetch cart: " + response.message());
                Toast.makeText(getContext(), "Failed to fetch cart: " + response.message(), Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onFailure(Call<ResponseCart> call, Throwable t) {
            Log.e("zzzzz Failure", "Network error: " + t.getMessage());
            Toast.makeText(getContext(), "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
        }
    };

}