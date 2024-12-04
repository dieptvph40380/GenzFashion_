package fpl.md37.genz_fashion.UserScreen;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.genz_fashion.R;

import fpl.md37.genz_fashion.models.ProducItem;

public class EvaluateFragment extends Fragment {
    private ProducItem productItem;
    private ImageView productImageView;
    private TextView productNameTextView, productDetailsTextView, productPriceTextView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_evaluate, container, false);
        productImageView = view.findViewById(R.id.img_product_ev);
        productNameTextView = view.findViewById(R.id.tv_product_name_ev);
        productDetailsTextView = view.findViewById(R.id.tv_product_details_ev);
        productPriceTextView = view.findViewById(R.id.tv_product_price_ev);
        // Nhận đối tượng ProducItem từ Bundle
        if (getArguments() != null) {
            productItem = (ProducItem) getArguments().getSerializable("product_item_data");
        }
        if (productItem != null) {
            productNameTextView.setText(productItem.getProductId().getProduct_name());
            String details = "Size: " + productItem.getSizeId().getName() + " | Qty: " + productItem.getQuantity();
            productDetailsTextView.setText(details);
            productPriceTextView.setText("$" + String.valueOf(productItem.getProductId().getPrice()));
            if (productItem.getProductId().getImage() != null && !productItem.getProductId().getImage().isEmpty()) {
                Glide.with(getContext()).load(productItem.getProductId().getImage().get(0)).into(productImageView);
            } else {
                productImageView.setImageResource(R.drawable.ic_launcher_background);
            }
        }

        return view;
    }
}
