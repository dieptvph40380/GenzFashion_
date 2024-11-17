package fpl.md37.genz_fashion.UserScreen;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.example.genz_fashion.R;
import com.example.genz_fashion.databinding.FragmentHomeBinding;
import java.util.ArrayList;
import java.util.List;
import fpl.md37.genz_fashion.adapter.AdapterProduct;
import fpl.md37.genz_fashion.adapter.AdapterProductUser;
import fpl.md37.genz_fashion.adapter.CategoryAdapter;
import fpl.md37.genz_fashion.api.HttpRequest;
import fpl.md37.genz_fashion.handel.Item_Handle_Product;
import fpl.md37.genz_fashion.models.Product;
import fpl.md37.genz_fashion.models.Response;
import retrofit2.Call;
import retrofit2.Callback;

public class HomeFragment extends Fragment {
    FragmentHomeBinding binding;
    private CountDownTimer countDownTimer;
    private HttpRequest httpRequest;
    private ArrayList<Product> productList = new ArrayList<>();  // Khởi tạo danh sách rỗng
    private RecyclerView rcv;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rcv = binding.rvItems;  // Liên kết RecyclerView từ layout
        setupCategoryRecyclerView();

        httpRequest = new HttpRequest();  // Khởi tạo HttpRequest
        startCountdownTimer(2 * 60 * 60 * 1000);  // Countdown 2 giờ
        fetchProducts();  // Lấy danh sách sản phẩm
    }

    private void setupRecyclerView(ArrayList<Product> products) {
        if (products == null) {
            products = new ArrayList<>();  // Khởi tạo danh sách rỗng nếu products là null
        }

//        AdapterProduct adapter = new AdapterProduct(getContext(), products, this);
        AdapterProductUser adapter=new AdapterProductUser(getContext(),products,this);
        rcv.setLayoutManager(new GridLayoutManager(getContext(), 2));
        rcv.setAdapter(adapter);
    }

    private void fetchProducts() {
        httpRequest.callApi().getAllProducts().enqueue(new Callback<Response<ArrayList<Product>>>() {
            @Override
            public void onResponse(Call<Response<ArrayList<Product>>> call, retrofit2.Response<Response<ArrayList<Product>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    productList = response.body().getData();
                    if (productList == null) {
                        productList = new ArrayList<>();  // Khởi tạo danh sách rỗng nếu không có sản phẩm
                    }
                    setupRecyclerView(productList);
                } else {
                    Toast.makeText(getContext(), "Lỗi khi lấy sản phẩm", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Response<ArrayList<Product>>> call, Throwable t) {
                Toast.makeText(getContext(), "Có lỗi xảy ra: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupCategoryRecyclerView() {
        List<CategoryAdapter.Category> categoryList = new ArrayList<>();
        categoryList.add(new CategoryAdapter.Category("T-Shirt", R.drawable.ic_shirt));
        categoryList.add(new CategoryAdapter.Category("Pant", R.drawable.ic_pant));
        categoryList.add(new CategoryAdapter.Category("Dress", R.drawable.ic_dress));
        categoryList.add(new CategoryAdapter.Category("Jacket", R.drawable.ic_jacket));

        Log.d("CategoryList", "Size: " + categoryList.size());  // Kiểm tra kích thước danh sách

        CategoryAdapter adapter = new CategoryAdapter(categoryList);
        binding.rvCategory.setAdapter(adapter);
        binding.rvCategory.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
    }

    private void startCountdownTimer(long duration) {
        countDownTimer = new CountDownTimer(duration, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                int hours = (int) (millisUntilFinished / (1000 * 60 * 60));
                int minutes = (int) (millisUntilFinished % (1000 * 60 * 60)) / (1000 * 60);
                int seconds = (int) ((millisUntilFinished % (1000 * 60)) / 1000);

                binding.tvHours.setText(String.format("%02d", hours));
                binding.tvMinutes.setText(String.format("%02d", minutes));
                binding.tvSeconds.setText(String.format("%02d", seconds));
            }

            @Override
            public void onFinish() {
                binding.tvHours.setText("00");
                binding.tvMinutes.setText("00");
                binding.tvSeconds.setText("00");
            }
        };
        countDownTimer.start();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }
}
