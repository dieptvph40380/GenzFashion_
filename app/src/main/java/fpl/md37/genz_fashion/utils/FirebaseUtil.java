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
            throw new IllegalStateException("Client is not logged in.");
        }
        return FirebaseFirestore.getInstance()
                .collection("Client")
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
    public static String currentUserId(){
        return FirebaseAuth.getInstance().getUid();
    }

    public static boolean isLoggedIn(){
        if(currentUserId()!=null){
            return true;
        }
        return false;
    }

    public static DocumentReference currentUserDetails(){
        return FirebaseFirestore.getInstance().collection("users").document(currentUserId());
    }

    public static CollectionReference allUserCollectionReference(){
        return FirebaseFirestore.getInstance().collection("users");
    }

    public static DocumentReference getChatroomReference(String chatroomId){
        return FirebaseFirestore.getInstance().collection("chatrooms").document(chatroomId);
    }

    public static CollectionReference getChatroomMessageReference(String chatroomId){
        return getChatroomReference(chatroomId).collection("chats");
    }

    public static String getChatroomId(String userId1,String userId2){
        if(userId1.hashCode()<userId2.hashCode()){
            return userId1+"_"+userId2;
        }else{
            return userId2+"_"+userId1;
        }
    }

    public static CollectionReference allChatroomCollectionReference(){
        return FirebaseFirestore.getInstance().collection("chatrooms");
    }

    public static DocumentReference getOtherUserFromChatroom(List<String> userIds){
        if(userIds.get(0).equals(FirebaseUtil.currentUserId())){
            return allUserCollectionReference().document(userIds.get(1));
        }else{
            return allUserCollectionReference().document(userIds.get(0));
        }
    }

    public static String timestampToString(Timestamp timestamp){
        return new SimpleDateFormat("HH:MM").format(timestamp.toDate());
    }

    public static void logout(){
        FirebaseAuth.getInstance().signOut();
    }

    public static StorageReference getCurrentProfilePicStorageRef(){
        return FirebaseStorage.getInstance().getReference().child("profile_pic")
                .child(FirebaseUtil.currentUserId());
    }

    public static StorageReference  getOtherProfilePicStorageRef(String otherUserId){
        return FirebaseStorage.getInstance().getReference().child("profile_pic")
                .child(otherUserId);
    }
}
