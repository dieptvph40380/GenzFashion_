package fpl.md37.genz_fashion.UserScreen;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.example.genz_fashion.R;

import fpl.md37.genz_fashion.api.HttpRequest;
import fpl.md37.genz_fashion.models.OrderRequest;
import fpl.md37.genz_fashion.models.ProducItem;
import fpl.md37.genz_fashion.models.RemoveProductsRequest;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class PaymentNotication extends AppCompatActivity {
    TextView txtNotication, tv_XN;
    private HttpRequest httpRequest;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_notication);

        txtNotication = findViewById(R.id.tv_Notification);
        tv_XN = findViewById(R.id.tv_XN);

        Intent intent = getIntent();
        String result = intent.getStringExtra("result");
        String productsJson = intent.getStringExtra("productsJson");

        // Giải mã JSON thành danh sách ProductItem
        Gson gson = new Gson();
        Type productListType = new TypeToken<List<ProducItem>>() {
        }.getType();
        List<ProducItem> products = gson.fromJson(productsJson, productListType);
        httpRequest = new HttpRequest();
        txtNotication.setText(result);

        tv_XN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "222222", Toast.LENGTH_SHORT).show();
            }
        });
        // Sử dụng Handler để chuyển màn hình sau 2 giây
        new Handler().postDelayed(() -> {
            if ("Thanh toán thành công".equals(result)) {
                // Chuyển tới MyOrderFragment
                navigateToFragment(new MyOrderFragment());
            } else if ("Hủy thanh toán".equals(result)) {
                // Chuyển tới CheckOutActivity
                Intent nextIntent = new Intent(PaymentNotication.this, CheckOutActivity.class);
                startActivity(nextIntent);
            } else {
                // Chuyển tới MainActivity
                Intent nextIntent = new Intent(PaymentNotication.this, MainActivity.class);
                startActivity(nextIntent);
            }
            finish(); // Đóng PaymentNotication sau khi chuyển màn hình
        }, 2000); // 2 giây
    }

    // Hàm chuyển tới một Fragment
    private void navigateToFragment(MyOrderFragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, fragment) // `R.id.container` là vùng chứa Fragment trong MainActivity
                .addToBackStack(null) // Lưu trạng thái để quay lại nếu cần
                .commit();
    }

    void order(String userId, String selectedPaymentMethod, List<ProducItem> products) {
        // Kiểm tra điều kiện để đảm bảo các dữ liệu cần thiết đã có
        // Tạo đối tượng OrderRequest với id_client, payment_method và danh sách sản phẩm đã chọn
        OrderRequest orderRequest = new OrderRequest(userId, selectedPaymentMethod, products);
        Gson gson = new Gson();
        String orderRequestJson = gson.toJson(orderRequest);
        Log.d("OrderRequest", "Data sent to API: " + orderRequestJson);
        // Gửi API addOrder
        httpRequest.callApi().addOrder(orderRequest).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Order placed successfully!", Toast.LENGTH_SHORT).show();
                    removeProductsFromCart(userId, products);
                } else {
                    try {
                        if (response.errorBody() != null) {
                            String errorResponse = response.errorBody().string();
                            Log.e("OrderError", "Server error: " + errorResponse);
                            Toast.makeText(getApplicationContext(), "Failed to place order: " + errorResponse, Toast.LENGTH_SHORT).show();
                        } else {
                            Log.e("OrderError", "Unknown server error.");
                            Toast.makeText(getApplicationContext(), "Failed to place order: Unknown error.", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        Log.e("OrderError", "Error parsing errorBody: " + e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                // Xử lý lỗi kết nối hoặc hệ thống
                Toast.makeText(getApplicationContext(), "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("OrderError", "Network error: " + t.getMessage());
            }
        });

    }

    private void removeProductsFromCart(String userId, List<ProducItem> products) {
        List<String> cartItemIds = new ArrayList<>();
        for (ProducItem item : products) {
            cartItemIds.add(item.getId()); // Lấy _id của từng sản phẩm
        }

        RemoveProductsRequest request = new RemoveProductsRequest(userId, cartItemIds);

        httpRequest.callApi().removeProducts(request).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Products removed successfully", Toast.LENGTH_SHORT).show();

                    Fragment newFragment = new MyOrderFragment();
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.setCustomAnimations(R.anim.bounce_in, R.anim.bounce_out);
                    transaction.replace(R.id.layout_checkout, newFragment);
                    transaction.addToBackStack(null);
                    transaction.commit();


                } else {
                    Log.e("RemoveProductsError", "Failed to remove products: " + response.message());
                    Toast.makeText(getApplicationContext(), "Failed to remove products.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("RemoveProductsError", "Network error: " + t.getMessage());
                Toast.makeText(getApplicationContext(), "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
