package fpl.md37.genz_fashion.ManagerScreen;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.genz_fashion.R;


public class ProfileCustomerFragment extends AppCompatActivity {
    ImageView image_viewprofile,img_Product;
    TextView tv_name,tv_address,tv_phone,tv_email;




    @Override
    public void  onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_profile_customer);

        image_viewprofile=findViewById(R.id.image_ViewProfile);
        tv_name=findViewById(R.id.tv_CusViewName);
        tv_address=findViewById(R.id.tv_CusViewAddress);
        tv_phone=findViewById(R.id.tv_CusViewPhone);
        tv_email=findViewById(R.id.tv_CusViewEmail);

        Intent intent = getIntent();
        String image=intent.getStringExtra("image");
        String name=intent.getStringExtra("name");
        String phone=intent.getStringExtra("phone");
        String email=intent.getStringExtra("email");
        String address=intent.getStringExtra("address");

        tv_name.setText(name != null ? name : "No available");
        tv_address.setText(address != null ? address : "No available");
        tv_email.setText(email != null ? email : "No available");
        tv_phone.setText(phone != null ? phone : "No available");

        // Hiển thị ảnh profile, hoặc ảnh mặc định nếu ảnh không có
        if (image != null && !image.isEmpty()) {
            setAvatarImage(image);
        } else {
            image_viewprofile.setImageResource(R.drawable.bg_chat); // Đảm bảo hình ảnh mặc định tồn tại trong drawable
        }


    }
    private void setAvatarImage(String avatar) {
        try {
            byte[] decodedString = Base64.decode(avatar, Base64.DEFAULT);
            Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            Glide.with(image_viewprofile.getContext())
                    .load(decodedBitmap)
                    .placeholder(R.drawable.default_avatar) // Đặt ảnh tạm trong khi tải
                    .into(image_viewprofile);
        } catch (IllegalArgumentException e) {
            Log.d("ProfileFragment", "Invalid Base64 string for avatar");

        }
    }
}