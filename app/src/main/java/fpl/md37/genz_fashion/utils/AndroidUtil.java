package fpl.md37.genz_fashion.utils;

import android.content.Context;
import android.net.Uri;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

public class AndroidUtil {

    // Hiển thị thông báo Toast
    public static void showToast(Context context, String message) {
        if (context != null && message != null) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        }
    }

    // Cập nhật ảnh hồ sơ
    public static void setProfilePic(Context context, Uri imageUri, ImageView imageView) {
        if (context != null && imageUri != null && imageView != null) {
            Glide.with(context)
                    .load(imageUri)
                    .apply(RequestOptions.circleCropTransform()) // Cắt hình tròn
                    .into(imageView);
        }
    }
}
