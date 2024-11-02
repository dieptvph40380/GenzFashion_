package fpl.md37.genz_fashion.ManagerScreen;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.loader.content.CursorLoader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.genz_fashion.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;

import java.io.File;
import java.util.ArrayList;

import fpl.md37.genz_fashion.adapter.AdapterSuppliers;
import fpl.md37.genz_fashion.api.ApiService;
import fpl.md37.genz_fashion.api.HttpRequest;
import fpl.md37.genz_fashion.handel.Item_Handel_Suppliers;
import fpl.md37.genz_fashion.models.Response;
import fpl.md37.genz_fashion.models.Suppliers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;


public class SupplierFragment extends Fragment implements Item_Handel_Suppliers {
    private static final int PERMISSION_REQUEST_CODE = 100;
    private ArrayList<Suppliers> listSuppliers;
    private RecyclerView recyclerView;
    private AdapterSuppliers adapter;
    private HttpRequest httpRequest;
    private static final int PICK_IMAGE_REQUEST = 10;
    private Uri imageUri;
    private boolean isUpdating = false;
    private BottomSheetDialog bottomSheetDialog;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_suppliers, container, false);
        checkPermissions();
        initializeViews(view);
        ImageView btnback=view.findViewById(R.id.btnout);
        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
        httpRequest =new HttpRequest();

        fetchSuppliers();

        return view;
    }

    private void initializeViews(View view) {
        recyclerView = view.findViewById(R.id.recyclerViewSuppliers);
        MaterialButton add = view.findViewById(R.id.btnadd);
        add.setOnClickListener(v -> showAddSupplierDialog());
    }

    private void showAddSupplierDialog(){
        // Khởi tạo BottomSheetDialog
        bottomSheetDialog = new BottomSheetDialog(requireContext());
        bottomSheetDialog.setContentView(R.layout.dialog_addsuppliers);

        // Ánh xạ các view trong BottomSheetDialog
        EditText editItemName = bottomSheetDialog.findViewById(R.id.edtName);
        EditText editItemPhone = bottomSheetDialog.findViewById(R.id.edtPhone);
        EditText editItemEmail = bottomSheetDialog.findViewById(R.id.edtEmail);
        EditText editItemDes = bottomSheetDialog.findViewById(R.id.edtInfor);

        MaterialButton btnSubmit = bottomSheetDialog.findViewById(R.id.addsupplier);
        ImageButton btnBack = bottomSheetDialog.findViewById(R.id.buttonClose);
        MaterialButton btnSelectImage = bottomSheetDialog.findViewById(R.id.addsuppliersUpload); // Nút để chọn ảnh
        ImageView imageView = bottomSheetDialog.findViewById(R.id.imageDialog); // ImageView để hiển thị ảnh
        btnBack.setOnClickListener(view -> bottomSheetDialog.dismiss());

        // Mở ứng dụng chọn ảnh khi người dùng nhấn vào nút
        btnSelectImage.setOnClickListener(v -> openImageChooser());

        btnSubmit.setOnClickListener(view -> {
            String itemName = editItemName.getText().toString();
            String itemPhone = editItemPhone.getText().toString();
            String itemEmail = editItemEmail.getText().toString();
            String itemDes = editItemDes.getText().toString();
            addSuppliers(itemName, itemPhone,itemEmail,itemDes, bottomSheetDialog);
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

        bottomSheetDialog.show();

    }

    private void addSuppliers(String itemName, String itemPhone, String itemEmail, String itemDes, BottomSheetDialog bottomSheetDialog) {
        // Chuyển đổi các giá trị thành RequestBody
        RequestBody namePart = RequestBody.create(MediaType.parse("text/plain"), itemName);
        RequestBody phonePart = RequestBody.create(MediaType.parse("text/plain"), itemPhone);
        RequestBody emailPart = RequestBody.create(MediaType.parse("text/plain"), itemEmail);
        RequestBody desPart = RequestBody.create(MediaType.parse("text/plain"), itemDes);

        // Chuyển đổi URI thành MultipartBody.Part
        MultipartBody.Part imagePart = null;
        if (imageUri != null) {
            File file = new File(getRealPathFromURI(imageUri));
            RequestBody requestFile = RequestBody.create(MediaType.parse("image/png"), file);
            imagePart = MultipartBody.Part.createFormData("image", file.getName(), requestFile);
        }

        ApiService apiService = httpRequest.callApi();
        Call<ResponseBody> call=apiService.addSuppliers(namePart,phonePart,emailPart,desPart,imagePart);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    // Xử lý phản hồi thành công
                    bottomSheetDialog.dismiss();
                    fetchSuppliers();
                } else {
                    // Xử lý lỗi
                    Log.d("AddSuppliers", "Error: " + response.message());
                    Log.d("AddSuppliers", "Error Code: " + response.code());
                    Log.d("AddSuppliers", "Error Body: " + response.errorBody().toString());
                    Toast.makeText(getContext(), "Không thể thêm nhà cung cấp: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }






    private void fetchSuppliers(){
        httpRequest.callApi().getAllsuppliers().enqueue(new Callback<Response<ArrayList<Suppliers>>>() {
            @Override
            public void onResponse(Call<Response<ArrayList<Suppliers>>> call, retrofit2.Response<Response<ArrayList<Suppliers>>> response) {
                if (response.isSuccessful()){
                    if (response.body().getStatus() ==200){
                        listSuppliers=response.body().getData();
                        if (listSuppliers != null){
                            for (Suppliers suppliers: listSuppliers){
                                Log.d("Suppliers","ID"+ suppliers.getId()+"Name: " + suppliers.getName()+ "Phone: "+suppliers.getPhone()+ "Email: "+suppliers.getEmail() +"Description: "+suppliers.getDescription() + suppliers.getImage());
                            }
                        }
                        setupRecyclerView(listSuppliers);
                    }
                }
            }



            @Override
            public void onFailure(Call<Response<ArrayList<Suppliers>>> call, Throwable t) {

            }
        });
    }

    private void setupRecyclerView(ArrayList<Suppliers> ds){
        adapter=new AdapterSuppliers(getContext(),ds,this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        recyclerView.setAdapter(adapter);
    }

    // Phương thức để mở ứng dụng chọn ảnh
    private void openImageChooser() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            ImageView imageViewAdd = bottomSheetDialog.findViewById(R.id.imageDialog);
            if (imageViewAdd != null) {
                imageViewAdd.setImageURI(imageUri);
            }
            if (isUpdating) {
                // Nếu đang ở chế độ cập nhật
                if (bottomSheetDialog != null && bottomSheetDialog.isShowing()) {
                    ImageView imageViewUpdate = bottomSheetDialog.findViewById(R.id.imageDialog);
                    if (imageViewUpdate != null) {
                        imageViewUpdate.setImageURI(imageUri); // Hiển thị ảnh trong dialog cập nhật
                    }
                }
            } else {
                if (bottomSheetDialog != null && bottomSheetDialog.isShowing()) {
                    ImageView imageViewAdd1 = bottomSheetDialog.findViewById(R.id.imageDialog1);
                    if (imageViewAdd1 != null) {
                        imageViewAdd1.setImageURI(imageUri);
                    }
                }
            }
        }
    }


    // Phương thức để lấy đường dẫn thực tế từ URI
    private String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(getContext(), contentUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }
    private void checkPermissions() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        }
    }

    Callback<Response<Suppliers>> responseCallback=new Callback<Response<Suppliers>>() {
        @Override
        public void onResponse(Call<Response<Suppliers>> call, retrofit2.Response<Response<Suppliers>> response) {
            if (response.isSuccessful()){
                if (response.body().getStatus()==200){
                    fetchSuppliers();
                }
            }
        }

        @Override
        public void onFailure(Call<Response<Suppliers>> call, Throwable t) {

        }
    };

    @Override
    public void Delete(Suppliers suppliers) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Confirm delete");
        builder.setMessage("Are you sure you want to delete?");
        builder.setPositiveButton("yes", (dialog, which) -> {
            httpRequest.callApi()
                    .deleteSuppliers(suppliers.getId())
                    .enqueue(responseCallback);
            fetchSuppliers();
            Toast.makeText(getContext(), "Xóa thành công", Toast.LENGTH_SHORT).show();
        });
        builder.setNegativeButton("no", (dialog, which) -> {
            dialog.dismiss();
        });
        builder.show();
    }

    @Override
    public void Update(Suppliers suppliers) {

    }
}