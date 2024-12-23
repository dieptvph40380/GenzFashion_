package fpl.md37.genz_fashion.UserScreen;

import static android.app.PendingIntent.getActivity;

import static java.security.AccessController.getContext;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import vn.zalopay.sdk.Environment;
import vn.zalopay.sdk.ZaloPayError;
import vn.zalopay.sdk.ZaloPaySDK;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.genz_fashion.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import fpl.md37.genz_fashion.adapter.CheckOutAdapter;
import fpl.md37.genz_fashion.api.CreateOrder;
import fpl.md37.genz_fashion.api.HttpRequest;
import fpl.md37.genz_fashion.models.CartData;
import fpl.md37.genz_fashion.models.Client;
import fpl.md37.genz_fashion.models.OrderRequest;
import fpl.md37.genz_fashion.models.ProducItem;
import fpl.md37.genz_fashion.models.RemoveProductsRequest;
import fpl.md37.genz_fashion.models.ResponseCart;
import fpl.md37.genz_fashion.utils.AndroidUtil;
import fpl.md37.genz_fashion.utils.FirebaseUtil;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.zalopay.sdk.listeners.PayOrderListener;

public class CheckOutActivity extends AppCompatActivity {
    TextView tvName, tvPhone, tvAddress, totalcheckout, tvPayment, tvPC_ToTal, tvPC_Shipping, tvPC_Voucher, tvPC_Payment, Voucher, tvOrder, tv_Methods;
    CheckBox cbChekOut;
    Client currentUserModel;
    ImageView btnBack;
    ActivityResultLauncher<Intent> imagePickLauncher;
    Double PriceTotal = 0.0, PriceShip = 0.0, PriceVoucher = 0.0, PricePayment = 0.0;
    Context safeContext;    // Lưu trữ context an toàn
    private RecyclerView recyclerView;
    private CheckOutAdapter adapter;
    private HttpRequest httpRequest;
    String selectedPaymentMethod;
    private String userId;
    CartData cartData;
    String totalString,name,phone,address;
    List<ProducItem> products;
    private ActivityResultLauncher<Intent> selectPaymentMethodLauncher;
    private static final String CHANNEL_ID = "payment_success_channel";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_check_out);

        tvName = findViewById(R.id.tv_ClName);
        tvPhone = findViewById(R.id.tv_ClPhone);
        tvAddress = findViewById(R.id.tv_ClAddress);
        totalcheckout = findViewById(R.id.total_CheckOut);
        tvPayment = findViewById(R.id.tv_Payment);
        cbChekOut = findViewById(R.id.cb_CheckOut);
        tvPC_ToTal = findViewById(R.id.tv_TotalPrice);
        tvPC_Shipping = findViewById(R.id.tv_DiscountShipping);
        tvPC_Voucher = findViewById(R.id.tv_DiscountVouchers);
        tvPC_Payment = findViewById(R.id.tv_TotalPayment);
        tvOrder = findViewById(R.id.tv_Order);
        tv_Methods = findViewById(R.id.tv_Methods);
        btnBack = findViewById(R.id.btnBack);

        createNotificationChannel();
        StrictMode.ThreadPolicy policy = new
                StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        // ZaloPay SDK Init
        ZaloPaySDK.init(2553, Environment.SANDBOX);

        getUserData();

        recyclerView = findViewById(R.id.rcv_ClCheckOut);
        recyclerView.setLayoutManager(new LinearLayoutManager(CheckOutActivity.this));
        adapter = new CheckOutAdapter(CheckOutActivity.this);
        recyclerView.setAdapter(adapter);
        httpRequest = new HttpRequest();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            userId = currentUser.getUid();
            Log.d("CheckOutFragment", "User ID: " + userId);
            httpRequest.callApi().getOrder(userId).enqueue(getCartID);
        }

        selectPaymentMethodLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            String selectedMethod = data.getStringExtra("selected_method");
                            if (selectedMethod != null) {
                                tv_Methods.setText(selectedMethod);
                                if ("ZaloPay".equals(tv_Methods.getText().toString())) {
                                    cbChekOut.setChecked(false);  // Bỏ chọn checkbox
                                }// Cập nhật UI với phương thức thanh toán
                                Toast.makeText(this, "Phương thức thanh toán: " + selectedMethod, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }
        );

        tv_Methods.setOnClickListener(view -> {
            Intent intent = new Intent(CheckOutActivity.this, PayMothodsFragment.class);
            selectPaymentMethodLauncher.launch(intent);
            // Mở PayMothodsFragment và chờ kết quả trả về
        });

        String voucherPrice = getIntent().getStringExtra("voucher_price");
        PriceVoucher = Double.parseDouble(voucherPrice);

        NumberFormat numberFormat = NumberFormat.getInstance(new Locale("vi", "VN"));
        String formattedVoucherPrice = numberFormat.format(PriceVoucher);
        tvPC_Voucher.setText(formattedVoucherPrice+ " VND");

        cbChekOut.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                // Nếu checkbox được chọn, thiết lập tvMethods là "add payment"
                tv_Methods.setText("Add Card");
                selectedPaymentMethod = tvPayment.getText().toString();
                Log.d("CheckOutActivity", "Selected Payment Method: " + tv_Methods.getText().toString());
                Toast.makeText(getApplicationContext(), "Selected Payment: " + tv_Methods.getText().toString(), Toast.LENGTH_SHORT).show();
            } else {
                selectedPaymentMethod = tv_Methods.getText().toString();
                Toast.makeText(getApplicationContext(), "ZaloPay is selected, unchecking the box.", Toast.LENGTH_SHORT).show();
            }

        });


        tvOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cbChekOut.isChecked()) {
                    // Kiểm tra điều kiện để đảm bảo các dữ liệu cần thiết đã có
                    if (currentUserModel == null || selectedPaymentMethod == null || cartData == null || cartData.getProducts().isEmpty()) {
                        Toast.makeText(getApplicationContext(), "Missing required data to place order.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    // Tạo đối tượng OrderRequest với id_client, payment_method và danh sách sản phẩm đã chọn
                    OrderRequest orderRequest = new OrderRequest(userId,name,phone,address, selectedPaymentMethod, products,PricePayment);
                    Gson gson = new Gson();
                    String orderRequestJson = gson.toJson(orderRequest);
                    Log.d("OrderRequest", "Data sent to API: " + orderRequestJson);
                    // Gửi API addOrder
                    httpRequest.callApi().addOrder(orderRequest).enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            if (response.isSuccessful()) {
                                Toast.makeText(getApplicationContext(), "Order placed successfully!", Toast.LENGTH_SHORT).show();
                                removeProductsFromCart(products);
                                showNotification();
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

                } else {
                    // Hiển thị thông báo nếu checkbox chưa được chọn
                    // Kiểm tra nếu tvPayment là "ZaloPay" thì gọi phương thức Payment
                    if ("ZaloPay".equals(tv_Methods.getText().toString())) {
                        String methods = tv_Methods.getText().toString();
                        Payment(userId, methods, products);
                        // Gọi phương thức Payment khi thanh toán bằng ZaloPay
                    }
                    Toast.makeText(getApplicationContext(), "Please select the payment method before proceeding.", Toast.LENGTH_SHORT).show();
                }

            }
        });


        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment newFragment = new CartFragment();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.anim.bounce_in, R.anim.bounce_out);
                transaction.replace(R.id.layout_checkout, newFragment);
                transaction.addToBackStack(null);
                transaction.commit();
                finish();
            }
        });


    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.d("ZaloPay", "onNewIntent triggered");
        ZaloPaySDK.getInstance().onResult(intent);
    }

    public void switchToFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.layout_checkout, fragment) // Thay thế Fragment hiện tại
                .addToBackStack(null) // Lưu trạng thái vào back stack nếu cần
                .commit();
    }

    void getUserData() {
        FirebaseUtil.currentUserDetails().get().addOnCompleteListener(task -> {
            if (!isFinishing() && !isDestroyed()) { // Thay thế isAdded() cho Activity
                if (task.isSuccessful() && task.getResult() != null) {
                    currentUserModel = task.getResult().toObject(Client.class);

                    if (currentUserModel != null) {
                        tvName.setText(currentUserModel.getName());
                        tvPhone.setText(currentUserModel.getPhone());
                        tvAddress.setText(currentUserModel.getAddress());
                        name = currentUserModel.getName().toString();
                        phone = currentUserModel.getPhone().toString();
                        address = currentUserModel.getAddress().toString();
                        Log.d("data", "getUserData: "+ name +"," +phone+","+address);
                    } else {
                        AndroidUtil.showToast(getApplicationContext(), "User data not found."); // Dùng this thay cho safeContext
                    }
                } else {
                    AndroidUtil.showToast(getApplicationContext(), "Failed to fetch user data."); // Dùng this thay cho safeContext
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
                cartData = response.body().getData();
                double totalPrice = cartData.getTotalPrice();
                String idCart = cartData.getId();
                Log.d("CheckOutFragment", "Cart ID: " + idCart);
                products = cartData.getProducts();

                try {
                    String ship = tvPC_Shipping.getText().toString();
                    String shipFormatted = ship.replace(".", "");
                    PriceShip = Double.parseDouble(shipFormatted);

                    // Tính tổng giá trị
                    PriceTotal = totalPrice + PriceVoucher;
                    PricePayment = totalPrice + PriceShip;

                    // Định dạng số theo chuẩn Việt Nam
                    NumberFormat numberFormat = NumberFormat.getInstance(new Locale("vi", "VN"));
                    String formattedPriceTotal = numberFormat.format(PriceTotal);
                    String formattedPricePayment = numberFormat.format(PricePayment);
                    String formattedTotalCheckout = numberFormat.format(PricePayment);

                    // Hiển thị lên giao diện
                    tvPC_ToTal.setText(formattedPriceTotal+" VND");
                    tvPC_Payment.setText(formattedPricePayment+" VND");
                    totalcheckout.setText(formattedTotalCheckout+" VND");

                    // Chuyển thành chuỗi không có phần thập phân (nếu cần)
                    totalString = String.format("%.0f", PricePayment);
                } catch (NumberFormatException e) {
                    // Xử lý lỗi khi dữ liệu không hợp lệ
                    e.printStackTrace();
                    tvPC_ToTal.setText("N/A");
                    tvPC_Payment.setText("N/A");
                    totalcheckout.setText("N/A");
                }


                // Hiển thị danh sách sản phẩm trong giỏ hàng
                adapter.setProducts(products);
            } else {
                Log.e("zzzzz Error", "Failed to fetch cart: " + response.message());
                Toast.makeText(getApplicationContext(), "Failed to fetch cart: " + response.message(), Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onFailure(Call<ResponseCart> call, Throwable t) {
            Log.e("zzzzz Failure", "Network error: " + t.getMessage());
            Toast.makeText(getApplicationContext(), "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
        }
    };

    private void removeProductsFromCart(List<ProducItem> products) {
        List<String> cartItemIds = new ArrayList<>();
        for (ProducItem item : products) {
            cartItemIds.add(item.getId()); // Lấy _id của từng sản phẩm
        }

        RemoveProductsRequest request = new RemoveProductsRequest(userId, cartItemIds);

        httpRequest.callApi().removeProducts(request).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Fragment newFragment = new PaymentSuccessfullActivity();
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.setCustomAnimations(R.anim.bounce_in, R.anim.bounce_out);
                    transaction.replace(R.id.layout_checkout, newFragment);
                    transaction.addToBackStack(null);;
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


    void Payment(String userId, String methods, List<ProducItem> products) {

        CreateOrder orderApi = new CreateOrder();
        Log.d("CreateOrder", "Response data: " + totalString);
        try {
            JSONObject data = orderApi.createOrder(totalString);
            String code = data.getString("return_code");
            Log.d("CreateOrder", "Response data: " + data.toString());

            if (code.equals("1")) {
                String token = data.getString("zp_trans_token");
                ZaloPaySDK.getInstance().payOrder(CheckOutActivity.this, token, "demozpdk://app", new PayOrderListener() {
                    @Override
                    public void onPaymentSucceeded(String s, String s1, String s2) {
                        order(userId, methods, products);
                        showNotification();
//                        Intent intent1=new Intent(CheckOutActivity.this, PaymentNotication.class);
//                        Gson gson = new Gson();
//                        String productsJson = gson.toJson(products);
//                        intent1.putExtra("result","Thanh toán thành công");
//                        intent1.putExtra("productsJson", productsJson);
//                        intent1.putExtra("userId", userId);
//                        intent1.putExtra("paymentMethod", methods
//                        );
//                        Log.d("PaymentNotification", "Products JSON: " + productsJson);
//                        Log.d("PaymentNotification", "UserID: " + userId);
//                        Log.d("PaymentNotification", "PaymentMethod: " + methods);
//                        startActivity(intent1);
                    }

                    @Override
                    public void onPaymentCanceled(String s, String s1) {
                        Fragment newFragment = new Payment_Faield();
                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                        transaction.setCustomAnimations(R.anim.bounce_in, R.anim.bounce_out);
                        transaction.replace(R.id.layout_checkout, newFragment);
                        transaction.addToBackStack(null);;
                        transaction.commit();
                        showNotificationFailed();
                    }

                    @Override
                    public void onPaymentError(ZaloPayError zaloPayError, String s, String s1) {

                        Fragment newFragment = new Payment_Faield();
                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                        transaction.setCustomAnimations(R.anim.bounce_in, R.anim.bounce_out);
                        transaction.replace(R.id.layout_checkout, newFragment);
                        transaction.addToBackStack(null);;
                        transaction.commit();
                        showNotificationFailed();
                    }
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void order(String userId, String selectedPaymentMethod, List<ProducItem> products) {
        // Kiểm tra điều kiện để đảm bảo các dữ liệu cần thiết đã có
        if (currentUserModel == null || selectedPaymentMethod == null || cartData == null || cartData.getProducts().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Missing required data to place order.", Toast.LENGTH_SHORT).show();
            return;
        }
        // Tạo đối tượng OrderRequest với id_client, payment_method và danh sách sản phẩm đã chọn
        OrderRequest orderRequest = new OrderRequest(userId,name,phone,address, selectedPaymentMethod, products,PricePayment);
        Gson gson = new Gson();
        String orderRequestJson = gson.toJson(orderRequest);
        Log.d("OrderRequest", "Data sent to API: " + orderRequestJson);
        // Gửi API addOrder
        httpRequest.callApi().addOrder(orderRequest).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Order placed successfully!", Toast.LENGTH_SHORT).show();
                    removeProductsFromCart(products);
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
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Payment Success";
            String description = "Notifications for payment success actions";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;

            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = (NotificationManager) getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
    }
    private void showNotification() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.putExtra("navigate_to_fragment", "MyOrderFragment");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);


        PendingIntent pendingIntent = PendingIntent.getActivity(
                getApplicationContext(),
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );


        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                .setSmallIcon(R.drawable.logo_app)
                .setContentTitle("Order Successful")
                .setContentText("You have successfully placed your order! Click here to view your order.")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        // Hiển thị thông báo
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            notificationManager.notify(1, builder.build());
        }
    }
    private void showNotificationFailed() {

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.putExtra("navigate_to_fragment", "HomeFragment");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);


        PendingIntent pendingIntent = PendingIntent.getActivity(
                getApplicationContext(),
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );


        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                .setSmallIcon(R.drawable.logo_app)
                .setContentTitle("Order Failed")
                .setContentText("Your order could not be processed. Please try again or contact customer support for assistance.")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        // Hiển thị thông báo
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            notificationManager.notify(1, builder.build());
        }

    }


}
