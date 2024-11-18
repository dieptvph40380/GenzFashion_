package fpl.md37.genz_fashion.UserScreen;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.genz_fashion.R;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.ByteArrayOutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import fpl.md37.genz_fashion.ManagerScreen.MainActivityManager;
import fpl.md37.genz_fashion.models.Client;
import fpl.md37.genz_fashion.utils.AndroidUtil;
import fpl.md37.genz_fashion.utils.FirebaseUtil;

public class PersonalInformationFragment_ extends Fragment {

    EditText edtName, edtEmail, edtPhone, edtAddress;
    ImageView profilePic;
    Button updateProfileBtn;
    Client currentUserModel;
    ActivityResultLauncher<Intent> imagePickLauncher;
    Uri selectedImageUri;

    public PersonalInformationFragment_() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imagePickLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if(result.getResultCode() == Activity.RESULT_OK){
                        Intent data = result.getData();
                        if(data != null && data.getData() != null){
                            selectedImageUri = data.getData();
                            AndroidUtil.setProfilePic(getContext(), selectedImageUri, profilePic);
                        }
                    }
                });
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_personal_information_, container, false);

        profilePic = view.findViewById(R.id.img_Profile);
        edtName = view.findViewById(R.id.edt_ProfileName);
        edtEmail = view.findViewById(R.id.edt_ProfileEmail);
        edtPhone = view.findViewById(R.id.edt_ProfilePhone);
        edtAddress = view.findViewById(R.id.edt_ProfileAddress);
        updateProfileBtn = view.findViewById(R.id.btn_update_profile);

        getUserData();

        updateProfileBtn.setOnClickListener(v -> {
            updateBtnClick();
        });

        profilePic.setOnClickListener(v -> {
            ImagePicker.with(this).cropSquare().compress(512).maxResultSize(512, 512)
                    .createIntent(intent -> {
                        imagePickLauncher.launch(intent);
                        return null;
                    });
        });

        ImageView btnback = view.findViewById(R.id.btnout);
        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBottomNav();
                // Thực hiện back lại màn hình trước
                getActivity().onBackPressed();  // Điều này sẽ thay đổi Fragment về Fragment trước đó
            }
        });


        return view;
    }
    private void showBottomNav() {
        BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.bottom_nav);
        if (bottomNavigationView != null) {
            bottomNavigationView.setVisibility(View.VISIBLE);
        }
    }

    void getUserData() {
        FirebaseUtil.currentUserDetails().get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                currentUserModel = task.getResult().toObject(Client.class);

                if (currentUserModel != null) {
                    edtName.setText(currentUserModel.getName());
                    edtEmail.setText(currentUserModel.getEmail());
                    edtPhone.setText(currentUserModel.getPhone());
                    edtAddress.setText(currentUserModel.getAddress());

                    // Lấy đường dẫn ảnh từ Firestore và hiển thị ảnh (dùng Glide để tải ảnh từ URL hoặc base64)
                    String profilePicPath = currentUserModel.getAvatar();
                    if (profilePicPath != null && !profilePicPath.isEmpty()) {
                        if (isValidBase64(profilePicPath)) {
                            byte[] decodedString = Base64.decode(profilePicPath, Base64.DEFAULT);
                            Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                            Glide.with(getContext())
                                    .load(decodedBitmap)
                                    .into(profilePic);
                        } else {
                            Glide.with(getContext())
                                    .load(profilePicPath)
                                    .into(profilePic);
                        }
                    }
                } else {
                    currentUserModel = new Client();
                    AndroidUtil.showToast(getContext(), "User data not found. Initialized default values.");
                }
            } else {
                AndroidUtil.showToast(getContext(), "Failed to fetch user data.");
            }
        });
    }

    void updateToFirestore() {
        FirebaseUtil.currentUserDetails().set(currentUserModel)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        AndroidUtil.showToast(getContext(),"Updated successfully");
                    }else{
                        AndroidUtil.showToast(getContext(),"Update failed");
                    }
                });
    }

    void updateBtnClick() {
        if (currentUserModel == null) {
            AndroidUtil.showToast(getContext(), "User data is not loaded yet. Please try again.");
            return;
        }

        String newUsername = edtName.getText().toString();
        if (newUsername.isEmpty() || newUsername.length() < 3) {
            edtName.setError("Username length should be at least 3 chars");
            return;
        }
        currentUserModel.setName(newUsername);

        String newUserEmail = edtEmail.getText().toString();
        if (newUserEmail.isEmpty() || !isValidEmail(newUserEmail)) {
            edtEmail.setError("Invalid email format");
            return;
        }
        currentUserModel.setEmail(newUserEmail);

        String newUserPhone = edtPhone.getText().toString();
        if (newUserPhone.isEmpty() || newUserPhone.length() < 10) {
            edtPhone.setError("Phone number must be at least 10 digits");
            return;
        }
        currentUserModel.setPhone(newUserPhone);

        String newUserAddress = edtAddress.getText().toString();
        if (newUserAddress.isEmpty() || newUserAddress.length() < 5) {
            edtAddress.setError("Address should be at least 5 chars");
            return;
        }
        currentUserModel.setAddress(newUserAddress);

        SharedPreferences preferences = getActivity().getSharedPreferences("user_info", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("name", newUsername); // Lưu tên mới
        editor.apply();

        // Lưu ảnh vào SharedPreferences (nếu có ảnh mới)
        if (selectedImageUri != null) {
            String base64Image = convertImageToBase64(selectedImageUri);
            currentUserModel.setAvatar(base64Image);

            // Lưu avatar vào SharedPreferences dưới dạng base64
            editor.putString("avatar", base64Image); // Lưu ảnh avatar mới
            editor.apply();
        }
        // Cập nhật thông tin người dùng vào Firestore
        updateToFirestore();
    }

    // Kiểm tra định dạng base64 hợp lệ
    private boolean isValidBase64(String base64String) {
        try {
            byte[] decodedBytes = Base64.decode(base64String, Base64.DEFAULT);
            return decodedBytes.length > 0;
        } catch (IllegalArgumentException e) {
            return false; // Chuỗi Base64 không hợp lệ
        }
    }

    // Chuyển ảnh thành chuỗi base64
    private String convertImageToBase64(Uri imageUri) {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            // Đọc ảnh và chuyển đổi thành byte[]
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream.toByteArray();
            // Chuyển byte[] thành chuỗi base64
            return Base64.encodeToString(byteArray, Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Kiểm tra định dạng email hợp lệ
    private boolean isValidEmail(String email) {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}";
        Pattern pattern = Pattern.compile(emailPattern);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
