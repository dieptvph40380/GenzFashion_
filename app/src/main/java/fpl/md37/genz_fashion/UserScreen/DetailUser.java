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

import fpl.md37.genz_fashion.models.Product;
import fpl.md37.genz_fashion.models.SizeQuantity;

public class DetailUser extends AppCompatActivity {

    private ImageView backArrow, productImagePlaceholder;
    private TextView productName, productPrice, productDescription, tvSize;
    private RadioGroup sizeOptions;
    private Product product;

    // Bảng ánh xạ size với SizeId
    private Map<String, String> sizeIdMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_detail);

        // Ánh xạ các view từ XML
        backArrow = findViewById(R.id.backArrow);
        productImagePlaceholder = findViewById(R.id.productImagePlaceholder);
        productName = findViewById(R.id.productName);
        productPrice = findViewById(R.id.productPrice);
        productDescription = findViewById(R.id.productDescription);
        sizeOptions = findViewById(R.id.sizeOptions);
        tvSize = findViewById(R.id.tvSize);

        // Ánh xạ các RadioButton
        RadioButton sizeS = findViewById(R.id.sizeS);
        RadioButton sizeM = findViewById(R.id.sizeM);
        RadioButton sizeL = findViewById(R.id.sizeL);
        RadioButton sizeXL = findViewById(R.id.sizeXL);
        RadioButton sizeXXL = findViewById(R.id.sizeXXL);

        // Nhận dữ liệu sản phẩm từ Intent
        product = (Product) getIntent().getSerializableExtra("product");

        // Kiểm tra sản phẩm có tồn tại
        if (product != null) {
            Log.d("Product details", "Product: " + product);

            // Load thông tin sản phẩm
            Glide.with(this)
                    .load(product.getImage().get(0))
                    .into(productImagePlaceholder);

            productName.setText(product.getProduct_name());
            productPrice.setText(product.getPrice());
            productDescription.setText(product.getDescription());

            // Khởi tạo bảng ánh xạ size và sizeId
            initializeSizeIdMap();

            // Vô hiệu hóa các RadioButton có số lượng bằng 0
            disableUnavailableSizes(sizeS, sizeM, sizeL, sizeXL, sizeXXL);

            // Xử lý sự kiện lựa chọn kích thước
            sizeOptions.setOnCheckedChangeListener((group, checkedId) -> {
                String selectedSize = getSelectedSize(checkedId);

                // Log size đã chọn
                Log.d("Size Selection", "Selected size: " + selectedSize);

                // Tìm số lượng còn lại
                int quantity = getAvailableQuantity(selectedSize, product.getSizeQuantities());

                // Cập nhật giao diện
                tvSize.setText("Số lượng còn: " + quantity);
            });

            // Xử lý sự kiện Back button
            backArrow.setOnClickListener(v -> onBackPressed());

        } else {
            Toast.makeText(this, "Product details not available", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Vô hiệu hóa các RadioButton có số lượng bằng 0 và các kích thước không tồn tại trong dữ liệu.
     */
    private void disableUnavailableSizes(RadioButton sizeS, RadioButton sizeM, RadioButton sizeL, RadioButton sizeXL, RadioButton sizeXXL) {
        // Đặt mặc định tất cả các nút đều bị vô hiệu hóa
        sizeS.setEnabled(false);
        sizeS.setAlpha(0.5f);
        sizeM.setEnabled(false);
        sizeM.setAlpha(0.5f);
        sizeL.setEnabled(false);
        sizeL.setAlpha(0.5f);
        sizeXL.setEnabled(false);
        sizeXL.setAlpha(0.5f);
        sizeXXL.setEnabled(false);
        sizeXXL.setAlpha(0.5f);

        // Lấy danh sách SizeQuantity từ sản phẩm
        List<SizeQuantity> sizeQuantities = product.getSizeQuantities();
        if (sizeQuantities != null) {
            for (SizeQuantity sq : sizeQuantities) {
                String sizeId = sq.getSizeId();
                int quantity = 0;

                try {
                    quantity = Integer.parseInt(sq.getQuantity());
                } catch (NumberFormatException e) {
                    Log.e("SizeQuantity", "Invalid quantity format: " + sq.getQuantity());
                }

                // Kích hoạt RadioButton nếu có số lượng
                if (sizeIdMap.get("S").equals(sizeId)) {
                    sizeS.setEnabled(quantity > 0);
                    sizeS.setAlpha(quantity > 0 ? 1.0f : 0.5f); // Làm mờ nếu không có số lượng
                } else if (sizeIdMap.get("M").equals(sizeId)) {
                    sizeM.setEnabled(quantity > 0);
                    sizeM.setAlpha(quantity > 0 ? 1.0f : 0.5f);
                } else if (sizeIdMap.get("L").equals(sizeId)) {
                    sizeL.setEnabled(quantity > 0);
                    sizeL.setAlpha(quantity > 0 ? 1.0f : 0.5f);
                } else if (sizeIdMap.get("XL").equals(sizeId)) {
                    sizeXL.setEnabled(quantity > 0);
                    sizeXL.setAlpha(quantity > 0 ? 1.0f : 0.5f);
                } else if (sizeIdMap.get("XXL").equals(sizeId)) {
                    sizeXXL.setEnabled(quantity > 0);
                    sizeXXL.setAlpha(quantity > 0 ? 1.0f : 0.5f);
                }
            }
        } else {
            Log.e("disableUnavailableSizes", "SizeQuantities is null or empty");
        }
    }



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

    /**
     * Lấy kích thước được chọn từ RadioGroup.
     */
    private String getSelectedSize(int checkedId) {
        if (checkedId == R.id.sizeS) return "S";
        if (checkedId == R.id.sizeM) return "M";
        if (checkedId == R.id.sizeL) return "L";
        if (checkedId == R.id.sizeXL) return "XL";
        if (checkedId == R.id.sizeXXL) return "XXL";
        return "";
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
            Log.d("SizeQuantity", "Checking Size: " + sq.getSizeId() + ", Quantity: " + sq.getQuantity());
            if (sizeId.equals(sq.getSizeId())) {
                try {
                    return Integer.parseInt(sq.getQuantity());
                } catch (NumberFormatException e) {
                    Log.e("SizeQuantity", "Invalid quantity format: " + sq.getQuantity());
                }
            }
        }

        Log.d("SizeQuantity", "No match for selected size: " + selectedSize);
        return 0;
    }

}
