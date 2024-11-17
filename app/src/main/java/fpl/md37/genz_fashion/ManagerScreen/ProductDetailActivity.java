package fpl.md37.genz_fashion.ManagerScreen;

import static androidx.core.content.ContentProviderCompat.requireContext;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.example.genz_fashion.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import fpl.md37.genz_fashion.adapter.ImageSliderAdapter;
import fpl.md37.genz_fashion.api.ApiService;
import fpl.md37.genz_fashion.api.HttpRequest;
import fpl.md37.genz_fashion.models.Product;
import fpl.md37.genz_fashion.models.Response;
import fpl.md37.genz_fashion.models.Size;
import fpl.md37.genz_fashion.models.Suppliers;
import fpl.md37.genz_fashion.models.TypeProduct;
import retrofit2.Call;
import retrofit2.Callback;

public class ProductDetailActivity extends AppCompatActivity {
    private static final int PERMISSION_REQUEST_CODE = 100;
    private static final int PICK_IMAGE_REQUEST = 10;
    private ApiService apiService;
    private HttpRequest httpRequest;
    TextView supplier1,type1;
    private ViewPager2 viewPager;
    private Handler sliderHandler = new Handler(Looper.getMainLooper());
    private int currentPage = 0;
    Product product;
    BottomSheetDialog bottomSheetDialog;
    ArrayList<File> ds_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_manager_products_detail);
        httpRequest = new HttpRequest();
        apiService = httpRequest.callApi();
        ds_image = new ArrayList<>();
        product = (Product) getIntent().getSerializableExtra("product_data");

        if (product == null) {
            finish();
            return;
        }
        viewPager = findViewById(R.id.viewpager_images);
        ArrayList<String> images = product.getImage();
        ImageSliderAdapter adapter = new ImageSliderAdapter(this, images);
        viewPager.setAdapter(adapter);
        startAutoSlide();

        TextView name = findViewById(R.id.name_productdetail);
        TextView price = findViewById(R.id.price_productdetail);
        TextView quantity = findViewById(R.id.quantity_productdetail);
        supplier1 = findViewById(R.id.supplier_productdetail);
        type1 = findViewById(R.id.type_productdetail);
        TextView state = findViewById(R.id.status_productdetail);
        TextView description = findViewById(R.id.description_productdetail);
        Button btn_update = findViewById(R.id.update_product);
        ImageView btnout = findViewById(R.id.btnout2);

        btnout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        name.setText(product.getProduct_name());
        price.setText("$"+product.getPrice());
        quantity.setText("Quantity:"+product.getQuantity());
        state.setText("Status: "+(product.isState() ? "Còn Hàng" : "Hết Hàng"));
        description.setText("Description: "+product.getDescription());

        getSupplierDetails(product.getSuppliersId());
        getTypeDetails(product.getTypeProductId());
        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               showUpdateBottomSheet();
            }
        });
        
    }

    private void showUpdateBottomSheet() {
        // Khởi tạo BottomSheetDialog
        bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(R.layout.dialog_updateproduct);
        bottomSheetDialog.setCancelable(false);

        // Khai báo các view bên trong BottomSheetDialog
        ImageView imageView = bottomSheetDialog.findViewById(R.id.product_image2);
        ImageButton cancle = bottomSheetDialog.findViewById(R.id.img_cancel2);
        EditText nameEditText = bottomSheetDialog.findViewById(R.id.product_name_dg2);
        EditText priceEditText = bottomSheetDialog.findViewById(R.id.product_price_dg2);
        EditText quantityEditText = bottomSheetDialog.findViewById(R.id.product_quantity_dg2);
        EditText descriptionEditText = bottomSheetDialog.findViewById(R.id.product_description_dg2);
        Button updateButton = bottomSheetDialog.findViewById(R.id.update_button_prd2);
        MaterialButton btnSelectImage = bottomSheetDialog.findViewById(R.id.updateproductupload2);
         cancle.setOnClickListener(v -> bottomSheetDialog.dismiss());
         btnSelectImage.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 openImageChooser();
             }
         });

        if (product.getImage() != null && !product.getImage().isEmpty()) {
            assert imageView != null;
            Glide.with(this)
                    .load(product.getImage().get(0))
                    .into(imageView);
        }
        nameEditText.setText(product.getProduct_name());
        priceEditText.setText(product.getPrice());
        quantityEditText.setText(product.getQuantity());
        descriptionEditText.setText(product.getDescription());


        updateButton.setOnClickListener(v -> {

        });

        bottomSheetDialog.setOnShowListener(dialog -> {
            BottomSheetDialog d = (BottomSheetDialog) dialog;
            FrameLayout bottomSheet = d.findViewById(com.google.android.material.R.id.design_bottom_sheet);
            if (bottomSheet != null) {
                CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) bottomSheet.getLayoutParams();
                layoutParams.height = getResources().getDisplayMetrics().heightPixels;
                bottomSheet.setLayoutParams(layoutParams);
            }
        });
        // Hiển thị BottomSheetDialog
        bottomSheetDialog.show();
    }

    private void openImageChooser() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        } else {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            startActivityForResult(intent, PICK_IMAGE_REQUEST);
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            // Kiểm tra xem người dùng có chọn nhiều ảnh hay không
            if (data.getClipData() != null) {
                int count = data.getClipData().getItemCount(); // Số lượng ảnh được chọn
                for (int i = 0; i < count; i++) {
                    Uri imageUri = data.getClipData().getItemAt(i).getUri();
                    // Hiển thị ảnh trên ImageView (nếu bạn muốn hiển thị lần lượt)
                    ImageView imageView = bottomSheetDialog.findViewById(R.id.product_image2);
                    if (imageView != null && i == 0) { // Chỉ hiển thị ảnh đầu tiên để minh họa
                        imageView.setImageURI(imageUri);
                        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                    }

                    // Thêm ảnh vào danh sách File nếu cần thiết
                    File imageFile = createFileFromUri(imageUri);
                    ds_image.add(imageFile);
                }
            } else if (data.getData() != null) {
                // Trường hợp chỉ chọn một ảnh
                Uri imageUri = data.getData();
               ImageView imageView = bottomSheetDialog.findViewById(R.id.product_image);
                if (imageView != null) {
                    imageView.setImageURI(imageUri);
                    imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                }

                // Thêm ảnh vào danh sách File
                File imageFile = createFileFromUri(imageUri);
                ds_image.add(imageFile);
            }
        }
    }

    private File createFileFromUri(Uri uri) {
        String fileName = "image_" + System.currentTimeMillis() + ".jpg";
        File file = new File(this.getCacheDir(), fileName);
        try (InputStream inputStream = this.getContentResolver().openInputStream(uri);
             OutputStream outputStream = new FileOutputStream(file)) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }
     //lay thong tin supplier theo id
    private void getSupplierDetails(String id) {
        httpRequest.callApi().getSupplierById(id).enqueue(new Callback<Response<Suppliers>>() {
            @Override
            public void onResponse(Call<Response<Suppliers>> call, retrofit2.Response<Response<Suppliers>> response) {
                if (response.isSuccessful() && response.body().getStatus() == 200) {
                    Suppliers suppliers = response.body().getData();
                    supplier1.setText("Supplier: " + suppliers.getName());
                }
            }

            @Override
            public void onFailure(Call<Response<Suppliers>> call, Throwable t) {
                Log.e("ProductDetail", "Failed to fetch product type details", t);
            }
        });
    }
    //lay thong tin type theo id
    private void getTypeDetails(String id) {
        httpRequest.callApi().getTypeProductById(id).enqueue(new Callback<Response<TypeProduct>>() {
            @Override
            public void onResponse(Call<Response<TypeProduct>> call, retrofit2.Response<Response<TypeProduct>> response) {
                if (response.isSuccessful() && response.body().getStatus() == 200) {
                    TypeProduct typeProduct = response.body().getData();

                    // Lấy tên của từng size và nối chúng lại thành một chuỗi
                    StringBuilder sizesBuilder = new StringBuilder();
                    for (Size size : typeProduct.getSizes()) {
                        sizesBuilder.append(size.getName()).append(", ");
                    }

                    // Xóa dấu phẩy và khoảng trắng cuối cùng nếu có
                    String sizes = sizesBuilder.length() > 0 ? sizesBuilder.substring(0, sizesBuilder.length() - 2) : "Không có size";

                    // Hiển thị kết quả trong TextView
                    type1.setText("Type Product: " + typeProduct.getName() + " - Sizes: " + sizes);

                    Log.d("abc", "onResponse: " + typeProduct.getName() + ", Sizes: " + sizes);
                }
            }

            @Override
            public void onFailure(Call<Response<TypeProduct>> call, Throwable t) {
                Log.e("ProductDetail", "Failed to fetch product type details", t);
            }
        });
    }
    //chay slide anh
    private void startAutoSlide() {
        sliderHandler.postDelayed(slideRunnable, 3000); // Chuyển ảnh mỗi 3 giây
    }
    private final Runnable slideRunnable = new Runnable() {
        @Override
        public void run() {
            if (viewPager.getAdapter() != null) {
                currentPage = (currentPage + 1) % viewPager.getAdapter().getItemCount();
                viewPager.setCurrentItem(currentPage, true); // true để có hiệu ứng chuyển ảnh
            }
            sliderHandler.postDelayed(this, 3000); // Lặp lại sau 3 giây
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sliderHandler.removeCallbacks(slideRunnable); // Xóa runnable khi activity bị hủy
    }
}