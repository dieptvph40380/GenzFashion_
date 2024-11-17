package fpl.md37.genz_fashion.utils;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.List;

public class FirebaseUtil {

    // Lấy ID người dùng hiện tại
    public static String currentUserId() {
        return FirebaseAuth.getInstance().getUid();
    }

    // Kiểm tra người dùng đã đăng nhập chưa
    public static boolean isLoggedIn() {
        return currentUserId() != null;
    }

    // Truy xuất thông tin người dùng hiện tại từ Firestore
    public static DocumentReference currentUserDetails() {
        if (!isLoggedIn()) {
            throw new IllegalStateException("User is not logged in.");
        }
        return FirebaseFirestore.getInstance()
                .collection("users")
                .document(currentUserId());
    }

    // Lấy StorageReference của ảnh đại diện người dùng hiện tại
    public static StorageReference getCurrentProfilePicStorageRef() {
        String userId = currentUserId();
        if (userId == null) {
            throw new IllegalStateException("User is not logged in.");
        }
        return FirebaseStorage.getInstance()
                .getReference()
                .child("profile_pic")
                .child(userId);
    }

    // Lấy StorageReference của ảnh đại diện người dùng khác
    public static StorageReference getOtherProfilePicStorageRef(String otherUserId) {
        if (otherUserId == null || otherUserId.isEmpty()) {
            throw new IllegalArgumentException("Invalid user ID.");
        }
        return FirebaseStorage.getInstance()
                .getReference()
                .child("profile_pic")
                .child(otherUserId);
    }
}