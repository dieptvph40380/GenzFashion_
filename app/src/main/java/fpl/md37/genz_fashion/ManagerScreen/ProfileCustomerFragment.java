package fpl.md37.genz_fashion.ManagerScreen;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.genz_fashion.R;


public class ProfileCustomerFragment extends Fragment {
    ImageView image_viewprofile,img_Product;
    TextView tv_name,tv_address,tv_phone,tv_email;

    public ProfileCustomerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_profile_customer, container, false);

        image_viewprofile=view.findViewById(R.id.image_ViewProfile);
        tv_name=view.findViewById(R.id.tv_CusViewName);
        tv_address=view.findViewById(R.id.tv_CusViewAddress);
        tv_phone=view.findViewById(R.id.tv_CusViewPhone);
        tv_email=view.findViewById(R.id.tv_CusViewEmail);

        Intent intent = requireActivity().getIntent();
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
            Glide.with(this)
                    .load(image)
                    .into(image_viewprofile);
        } else {
            image_viewprofile.setImageResource(R.drawable.bg_chat); // Đảm bảo hình ảnh mặc định tồn tại trong drawable
        }



        return  view;
    }
}