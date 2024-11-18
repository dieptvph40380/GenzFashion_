package fpl.md37.genz_fashion.UserScreen;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.genz_fashion.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fpl.md37.genz_fashion.api.HttpRequest;
import fpl.md37.genz_fashion.models.Product;
import fpl.md37.genz_fashion.models.Size;
import fpl.md37.genz_fashion.models.SizeQuantity;
import fpl.md37.genz_fashion.models.TypeProduct;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailUser extends AppCompatActivity {

    private ImageView backArrow, productImagePlaceholder;
    private TextView productName, productPrice, productDescription, tvSize;
    private RadioGroup sizeOptions;
    private Product product;

    private Map<String, String> sizeIdMap = new HashMap<>();
    private HttpRequest httpRequest = new HttpRequest();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_detail);

        backArrow = findViewById(R.id.backArrow);
        productImagePlaceholder = findViewById(R.id.productImagePlaceholder);
        productName = findViewById(R.id.productName);
        productPrice = findViewById(R.id.productPrice);
        productDescription = findViewById(R.id.productDescription);
        sizeOptions = findViewById(R.id.sizeOptions);
        tvSize = findViewById(R.id.tvSize);

        product = (Product) getIntent().getSerializableExtra("product");

        if (product != null) {
            Glide.with(this)
                    .load(product.getImage().get(0))
                    .into(productImagePlaceholder);

            productName.setText(product.getProduct_name());
            productPrice.setText(product.getPrice());
            productDescription.setText(product.getDescription());

            loadSizesFromApi();

            backArrow.setOnClickListener(v -> onBackPressed());
        } else {
            Toast.makeText(this, "Product details not available", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadSizesFromApi() {
        httpRequest.callApi().getTypeProductById(product.getTypeProductId()).enqueue(new Callback<fpl.md37.genz_fashion.models.Response<TypeProduct>>() {
            @Override
            public void onResponse(Call<fpl.md37.genz_fashion.models.Response<TypeProduct>> call, Response<fpl.md37.genz_fashion.models.Response<TypeProduct>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getStatus() == 200) {
                    TypeProduct typeProduct = response.body().getData();
                    if (typeProduct != null && typeProduct.getSizes() != null) {
                        updateSizeRadioButtons(typeProduct.getSizes());
                    } else {
                        Log.d("API Response", "No sizes available.");
                        tvSize.setText("Sizes and Quantities: Not available");
                    }
                } else {
                    Log.e("API Response", "Failed to load sizes");
                }
            }

            @Override
            public void onFailure(Call<fpl.md37.genz_fashion.models.Response<TypeProduct>> call, Throwable t) {
                Log.e("API Failure", "Error: " + t.getMessage());
                tvSize.setText("Failed to load sizes");
            }
        });
    }

    private void updateSizeRadioButtons(List<Size> sizes) {
        // Clear bảng ánh xạ cũ
        sizeIdMap.clear();

        // Duyệt qua danh sách size và cập nhật giao diện
        for (Size size : sizes) {
            sizeIdMap.put(size.getName(), size.getId());
        }

        // Cập nhật trạng thái các nút RadioButton
        for (int i = 0; i < sizeOptions.getChildCount(); i++) {
            RadioButton radioButton = (RadioButton) sizeOptions.getChildAt(i);
            String sizeName = radioButton.getText().toString();

            if (sizeIdMap.containsKey(sizeName)) {
                // Lấy số lượng cho size hiện tại
                int quantity = getAvailableQuantity(sizeName, product.getSizeQuantities());
                radioButton.setEnabled(quantity > 0);
                radioButton.setAlpha(quantity > 0 ? 1.0f : 0.5f);
            } else {
                // Nếu size không tồn tại, vô hiệu hóa nút
                radioButton.setEnabled(false);
                radioButton.setAlpha(0.5f);
            }
        }

        // Thêm sự kiện lắng nghe khi chọn RadioButton
        sizeOptions.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton selectedButton = findViewById(checkedId);

            if (selectedButton != null) {
                String selectedSize = selectedButton.getText().toString();

                // Lấy số lượng tương ứng với size được chọn
                int selectedQuantity = getAvailableQuantity(selectedSize, product.getSizeQuantities());

                // Hiển thị số lượng trong TextView
                tvSize.setText(  "Quantity: " + selectedQuantity);
            }
        });
    }

    /**
     * Lấy số lượng còn lại của kích thước được chọn.
     */
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