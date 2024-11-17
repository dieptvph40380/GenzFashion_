package fpl.md37.genz_fashion.UserScreen;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

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
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.genz_fashion.R;
import com.github.dhaval2404.imagepicker.ImagePicker;

import fpl.md37.genz_fashion.models.Client;
import fpl.md37.genz_fashion.utils.AndroidUtil;
import fpl.md37.genz_fashion.utils.FirebaseUtil;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null && data.getData() != null) {
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

        updateProfileBtn.setOnClickListener((v -> {
            updateBtnClick();
        }));

        profilePic.setOnClickListener((v) -> {
            ImagePicker.with(this).cropSquare().compress(512).maxResultSize(512, 512)
                    .createIntent(new Function1<Intent, Unit>() {
                        @Override
                        public Unit invoke(Intent intent) {
                            imagePickLauncher.launch(intent);
                            return null;
                        }
                    });
        });

        return view;
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
                } else {
                    // Khởi tạo đối tượng mặc định
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
                    if (task.isSuccessful()) {
                        AndroidUtil.showToast(getContext(), "Updated successfully");
                    } else {
                        AndroidUtil.showToast(getContext(), "Updated failed");
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

        // Cập nhật ảnh hồ sơ (nếu có)
        if (selectedImageUri != null) {
            FirebaseUtil.getCurrentProfilePicStorageRef().putFile(selectedImageUri)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            updateToFirestore();
                        } else {
                            AndroidUtil.showToast(getContext(), "Failed to upload profile picture");
                        }
                    });
        } else {
            updateToFirestore();
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
