package fpl.md37.genz_fashion.ManagerScreen;

import android.app.Dialog;
import android.os.Bundle;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.example.genz_fashion.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

import fpl.md37.genz_fashion.adapter.AdapterTypeProduct;
import fpl.md37.genz_fashion.api.HttpRequest;
import fpl.md37.genz_fashion.models.TypeProduct;
import retrofit2.Call;
import retrofit2.Callback;
import fpl.md37.genz_fashion.models.Response;


public class TypeProductFragment extends Fragment {
    private ArrayList<TypeProduct> typeProducts;
    private HttpRequest httpRequest;
    private RecyclerView recyclerView;
    private AdapterTypeProduct adapter;
    private MaterialButton add;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_type_product, container, false);
        recyclerView=view.findViewById(R.id.recyclerTypeProduct);
        add=view.findViewById(R.id.btnadd);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Khởi tạo BottomSheetDialog
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(requireContext());
                bottomSheetDialog.setContentView(R.layout.dialog_add_typeproduct);

                // Ánh xạ các view trong BottomSheetDialog
                EditText editItemName = bottomSheetDialog.findViewById(R.id.edtName);
                MaterialButton btnSubmit = bottomSheetDialog.findViewById(R.id.addsupplier);

                // Xử lý sự kiện khi nhấn nút Submit
                btnSubmit.setOnClickListener(v1 -> {
                    String itemName = editItemName.getText().toString();
                    bottomSheetDialog.dismiss();
                });

                // Thiết lập chiều cao 1/3 màn hình cho BottomSheetDialog
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
        });
        httpRequest=new HttpRequest();
        httpRequest.callApi().getAlltypeproduct().enqueue(getAllTypeProduct);
        return view;
    }
    private void getData(ArrayList<TypeProduct> ds){
        adapter = new AdapterTypeProduct(getContext(), ds);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);
    }
    Callback<Response<ArrayList<TypeProduct>>> getAllTypeProduct=new Callback<Response<ArrayList<TypeProduct>>>() {
        @Override
        public void onResponse(Call<Response<ArrayList<TypeProduct>>> call, retrofit2.Response<Response<ArrayList<TypeProduct>>> response) {
            if (response.isSuccessful()){
                if (response.body().getStatus()==200){
                    typeProducts=response.body().getData();
                    getData(typeProducts);

                }
            }
        }

        @Override
        public void onFailure(Call<Response<ArrayList<TypeProduct>>> call, Throwable t) {
            Log.d("Diss","" + t.getMessage());
        }
    };
}