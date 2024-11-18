package fpl.md37.genz_fashion.UserScreen;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.genz_fashion.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fpl.md37.genz_fashion.api.HttpRequest;
import fpl.md37.genz_fashion.models.Product;
import fpl.md37.genz_fashion.models.Response;
import fpl.md37.genz_fashion.models.Size;
import fpl.md37.genz_fashion.models.SizeQuantity;
import fpl.md37.genz_fashion.models.TypeProduct;
import retrofit2.Call;
import retrofit2.Callback;

public class DetailUser extends AppCompatActivity {

    private ImageView backArrow, productImagePlaceholder;
    private TextView productName, productPrice, productDescription;
    private Product product;
    private HttpRequest httpRequest = new HttpRequest();
    private Map<String, String> sizeIdMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_detail);

        // Ánh xạ các view trong layout
        backArrow = findViewById(R.id.backArrow);
        productImagePlaceholder = findViewById(R.id.productImagePlaceholder);
        productName = findViewById(R.id.productName);
        productPrice = findViewById(R.id.productPrice);
        productDescription = findViewById(R.id.productDescription);

        // Nút thêm vào giỏ hàng và mua ngay
        LinearLayout addToCartButton = findViewById(R.id.addToCart);
        LinearLayout buyNowButton = findViewById(R.id.addBuyNow);

        addToCartButton.setOnClickListener(v -> showBottomSheet("Add to Cart"));
        buyNowButton.setOnClickListener(v -> showBottomSheet("Buy Now"));

        // Nhận dữ liệu sản phẩm từ Intent
        product = (Product) getIntent().getSerializableExtra("product");

        if (product != null) {
            updateProductDetails(product);
            backArrow.setOnClickListener(v -> onBackPressed());
        } else {
            Toast.makeText(this, "Product details not available", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateProductDetails(Product product) {
        if (product != null) {
            String imageUrl = product.getImage() != null && !product.getImage().isEmpty() ? product.getImage().get(0) : "";
            if (!TextUtils.isEmpty(imageUrl)) {
                Glide.with(this)
                        .load(imageUrl)
                        .into(productImagePlaceholder);
            } else {
                productImagePlaceholder.setImageResource(R.drawable.shark); // Placeholder image
            }

            productName.setText(TextUtils.isEmpty(product.getProduct_name()) ? "Unknown Product" : product.getProduct_name());
            productPrice.setText(TextUtils.isEmpty(product.getPrice()) ? "Price Not Available" : product.getPrice());
            productDescription.setText(TextUtils.isEmpty(product.getDescription()) ? "No description available." : product.getDescription());

            // Lấy và cập nhật danh sách kích thước từ API
            loadSizesFromApi();
        }
    }

    private void loadSizesFromApi() {
        httpRequest.callApi().getTypeProductById(product.getTypeProductId()).enqueue(new Callback<Response<TypeProduct>>() {


            @Override
            public void onResponse(Call<Response<TypeProduct>> call, retrofit2.Response<Response<TypeProduct>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getStatus() == 200) {
                    TypeProduct typeProduct = response.body().getData();
                    if (typeProduct != null && typeProduct.getSizes() != null) {
                        // Cập nhật sizeIdMap từ danh sách sizes
                        for (Size size : typeProduct.getSizes()) {
                            sizeIdMap.put(size.getName(), size.getId());
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<fpl.md37.genz_fashion.models.Response<TypeProduct>> call, Throwable t) {
                Log.e("API Failure", "Error: " + t.getMessage());
            }
        });
    }

    private void showBottomSheet(String action) {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);

        // Inflate layout cho BottomSheet
        LinearLayout sheetLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.dialog_selected, null);

        // Gắn dữ liệu vào các thành phần trong BottomSheet
        ImageView imgProduct = sheetLayout.findViewById(R.id.imgProduct);
        TextView tvProductPrice = sheetLayout.findViewById(R.id.tvProductPrice);
        TextView tvProductStock = sheetLayout.findViewById(R.id.tvProductStock);
        RadioGroup sizeOptions = sheetLayout.findViewById(R.id.sizeOptions);

        if (product != null) {
            String imageUrl = product.getImage() != null && !product.getImage().isEmpty() ? product.getImage().get(0) : "";
            if (!TextUtils.isEmpty(imageUrl)) {
                Glide.with(this)
                        .load(imageUrl)
                        .into(imgProduct);
            } else {
                imgProduct.setImageResource(R.drawable.shark); // Placeholder image
            }

            tvProductPrice.setText(TextUtils.isEmpty(product.getPrice()) ? "Price Not Available" : product.getPrice());

            // Cập nhật các size vào RadioGroup
            updateSizeRadioButtons(sizeOptions);

            // Thêm sự kiện lắng nghe khi chọn RadioButton
            sizeOptions.setOnCheckedChangeListener((group, checkedId) -> {
                RadioButton selectedButton = sheetLayout.findViewById(checkedId);

                if (selectedButton != null) {
                    String selectedSize = selectedButton.getText().toString();

                    // Lấy số lượng tương ứng với size được chọn
                    int selectedQuantity = getAvailableQuantity(selectedSize, product.getSizeQuantities());

                    // Hiển thị số lượng trong TextView
                    tvProductStock.setText("Quantity: " + selectedQuantity);
                }
            });
        }


        // Gắn layout vào BottomSheetDialog và hiển thị
        bottomSheetDialog.setContentView(sheetLayout);
        bottomSheetDialog.show();
        /**
     * Khởi tạo bảng ánh xạ size và sizeId.
     */
    private void initializeSizeIdMap() {
        sizeIdMap.put("S", "6703f36ea2c21ac50fc110a8");
        sizeIdMap.put("M", "6703f374a2c21ac50fc110aa");
        sizeIdMap.put("L", "6703f379a2c21ac50fc110ac");
        sizeIdMap.put("XL", "67360d3dd0baa60674d5662d");
        sizeIdMap.put("XXL", "67360dxyz0baa60674d566xy"); // Thêm size khác nếu cần
    }

    private void updateSizeRadioButtons(RadioGroup sizeOptions) {
        sizeOptions.removeAllViews(); // Clear existing views

        for (String sizeName : sizeIdMap.keySet()) {
            RadioButton radioButton = new RadioButton(this);
            radioButton.setText(sizeName);
            sizeOptions.addView(radioButton);
        }
    }

    private int getAvailableQuantity(String selectedSize, List<SizeQuantity> sizeQuantities) {
        if (sizeQuantities == null || sizeIdMap.get(selectedSize) == null) {
            Log.d("SizeQuantity", "SizeQuantities hoặc SizeIdMap không hợp lệ.");
            return 0;
        }

        String sizeId = sizeIdMap.get(selectedSize);
        for (SizeQuantity sq : sizeQuantities) {
            if (sizeId.equals(sq.getSizeId())) {
                try {
                    return Integer.parseInt(sq.getQuantity());
                } catch (NumberFormatException e) {
                    Log.e("SizeQuantity", "Invalid quantity format: " + sq.getQuantity());
                }
            }
        }
        return 0;
    }
}

