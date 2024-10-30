package fpl.md37.genz_fashion.ManagerScreen;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.genz_fashion.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

import fpl.md37.genz_fashion.adapter.AdapterTypeProduct;
import fpl.md37.genz_fashion.api.HttpRequest;
import fpl.md37.genz_fashion.models.Response;
import fpl.md37.genz_fashion.models.Size;
import fpl.md37.genz_fashion.models.TypeProduct;
import retrofit2.Call;
import retrofit2.Callback;

public class TypeProductFragment extends Fragment {
    private ArrayList<TypeProduct> typeProducts;
    private ArrayList<Size> sizes;
    private RecyclerView recyclerView;
    private AdapterTypeProduct adapter;
    private HttpRequest httpRequest;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_type_product, container, false);
        initializeViews(view);
        httpRequest = new HttpRequest();

        // Gọi API để lấy tất cả loại sản phẩm và kích thước
        fetchTypeProducts();
        fetchSizes();

        return view;
    }

    private void initializeViews(View view) {
        recyclerView = view.findViewById(R.id.recyclerTypeProduct);
        MaterialButton add = view.findViewById(R.id.btnadd);

        add.setOnClickListener(v -> showAddTypeProductDialog());
    }

    private void showAddTypeProductDialog() {
        // Khởi tạo BottomSheetDialog
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(requireContext());
        bottomSheetDialog.setContentView(R.layout.dialog_add_typeproduct);

        // Ánh xạ các view trong BottomSheetDialog
        EditText editItemName = bottomSheetDialog.findViewById(R.id.edtName);
        MaterialButton btnSubmit = bottomSheetDialog.findViewById(R.id.addsupplier);
        ImageButton btnBack = bottomSheetDialog.findViewById(R.id.buttonClose);

        btnBack.setOnClickListener(v -> bottomSheetDialog.dismiss());
        btnSubmit.setOnClickListener(v -> {
            String itemName = editItemName.getText().toString();
            // Xử lý thêm loại sản phẩm tại đây
            bottomSheetDialog.dismiss();
        });

        // Thiết lập chiều cao cho BottomSheetDialog
        bottomSheetDialog.setOnShowListener(dialog -> {
            BottomSheetDialog d = (BottomSheetDialog) dialog;
            FrameLayout bottomSheet = d.findViewById(com.google.android.material.R.id.design_bottom_sheet);
            if (bottomSheet != null) {
                CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) bottomSheet.getLayoutParams();
                layoutParams.height = getResources().getDisplayMetrics().heightPixels / 2;
                bottomSheet.setLayoutParams(layoutParams);
            }
        });

        // Hiển thị BottomSheetDialog
        bottomSheetDialog.show();
    }

    private void fetchTypeProducts() {
        httpRequest.callApi().getAlltypeproduct().enqueue(new Callback<Response<ArrayList<TypeProduct>>>() {
            @Override
            public void onResponse(Call<Response<ArrayList<TypeProduct>>> call, retrofit2.Response<Response<ArrayList<TypeProduct>>> response) {
                if (response.isSuccessful()){
                    if (response.body().getStatus()==200){
                        typeProducts = response.body().getData();
                        if (typeProducts != null) {
                            for (TypeProduct typeProduct : typeProducts) {
                                Log.d("TypeProduct", "ID: " + typeProduct.getId() + ", Name: " + typeProduct.getName() + ", Sizes: " + (typeProduct.getSizes() != null ? typeProduct.getSizes().size() : "null"));
                            }
                        }
                        setupRecyclerView(typeProducts);
//
                    }
                }
            }

            @Override
            public void onFailure(Call<Response<ArrayList<TypeProduct>>> call, Throwable t) {
                Log.d("Error", t.getMessage());
            }
        });
    }

    private void fetchSizes() {
        httpRequest.callApi().getAllSizes().enqueue(new Callback<Response<ArrayList<Size>>>() {
            @Override
            public void onResponse(Call<Response<ArrayList<Size>>> call, retrofit2.Response<Response<ArrayList<Size>>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getStatus() == 200) {
                    sizes = response.body().getData();
                    // Ghi log số lượng kích thước
                    Log.d("SizeList", "Number of sizes: " + sizes.size());

                    // Ghi log tên của từng kích thước
                    for (Size size : sizes) {
                        Log.d("SizeName", "Name: " + size.getId()+"Name: " + size.getName());
                    }
                } else {
                    Log.d("SizeList", "Response not successful or empty data");
                }
            }


            @Override
            public void onFailure(Call<Response<ArrayList<Size>>> call, Throwable t) {
                Log.d("Error", t.getMessage());
            }
        });
    }

    private void setupRecyclerView(ArrayList<TypeProduct> ds) {
        adapter = new AdapterTypeProduct(getContext(), ds);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);
    }

}
