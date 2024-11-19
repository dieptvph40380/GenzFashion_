package fpl.md37.genz_fashion.UserScreen;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.genz_fashion.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import fpl.md37.genz_fashion.adapter.AdapterCart;
import fpl.md37.genz_fashion.api.HttpRequest;
import fpl.md37.genz_fashion.handel.Item_Handel_check;
import fpl.md37.genz_fashion.models.CartData;
import fpl.md37.genz_fashion.models.ProducItem;
import fpl.md37.genz_fashion.models.ResponseCart;
import fpl.md37.genz_fashion.models.SelectProductRequest;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CartFragment extends Fragment implements Item_Handel_check {
    private RecyclerView recyclerView;
    private AdapterCart adapter;
    private TextView txtotal;
    private HttpRequest httpRequest;
    private ImageView btn_back;
    private List<ProducItem> products;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cart, container, false);
        recyclerView = view.findViewById(R.id.recycler_view_cart);
        txtotal = view.findViewById(R.id.total_cart);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new AdapterCart(getContext(),this);
        recyclerView.setAdapter(adapter);
        httpRequest = new HttpRequest();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            httpRequest.callApi().getCart(userId).enqueue(getCartID);
        }

        btn_back = view.findViewById(R.id.back_button);


        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBottomNav();
                Fragment newFragment = new HomeFragment();
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.anim.bounce_in, R.anim.bounce_out);
                transaction.replace(R.id.frameLayout_cart, newFragment);
                transaction.addToBackStack(null);
                transaction.commit();

                BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.bottom_nav);

                bottomNavigationView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        bottomNavigationView.setSelectedItemId(R.id.nav_home);
                    }
                }, 300);
            }
        });

        return view;
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
                double totalPrice = cartData.getTotalPrice();
                List<ProducItem> products = cartData.getProducts();

                // Hiển thị tổng giá tiền
                txtotal.setText("Total Price: $" + totalPrice);

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

    private void showBottomNav() {
        BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.bottom_nav);
        if (bottomNavigationView != null) {
            bottomNavigationView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onProductChecked(ProducItem product, boolean isChecked) {
        // Lấy ID người dùng từ Firebase
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();

            // Gọi API để cập nhật sản phẩm được chọn
            List<String> selectedProductIds = new ArrayList<>();
            if (isChecked) {
                selectedProductIds.add(product.getProductId().getId());  // Thêm sản phẩm vào danh sách chọn
            }

            SelectProductRequest request = new SelectProductRequest(userId, selectedProductIds);
            httpRequest.callApi().selectProducts(request).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        // Nếu cập nhật thành công, có thể thực hiện cập nhật lại UI
                        Toast.makeText(getContext(), "Product selected successfully", Toast.LENGTH_SHORT).show();
                        // Sau khi chọn hoặc bỏ chọn, bạn có thể gọi lại API giỏ hàng để cập nhật UI
                        httpRequest.callApi().getCart(userId).enqueue(getCartID);
                    } else {
                        Toast.makeText(getContext(), "Failed to select product", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }


}