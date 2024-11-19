package fpl.md37.genz_fashion.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.genz_fashion.R;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

import fpl.md37.genz_fashion.handel.Item_Handel_check;
import fpl.md37.genz_fashion.models.ProducItem;
import fpl.md37.genz_fashion.models.Product;
import fpl.md37.genz_fashion.models.Size;

public class AdapterCart extends RecyclerView.Adapter<AdapterCart.ViewHolder> {
    private Context context;
    private List<ProducItem> products;
    private Item_Handel_check listener;

    public AdapterCart(Context context,Item_Handel_check listener) {
        this.context = context;
        this.products = new ArrayList<>();
        this.listener = listener;
    }
    public void setProducts(List<ProducItem> products) {
        this.products = products;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AdapterCart.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cart_product, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterCart.ViewHolder holder, int position) {
        ProducItem product = products.get(position);
        holder.cart_name.setText(product.getProductId().getProduct_name());
        holder.cart_price.setText(product.getProductId().getPrice()+" VND");
        holder.cart_quantity.setText(String.valueOf(product.getQuantity()));

        // Set trạng thái checkbox (checked hoặc unchecked)
        holder.checkBox_cart.setChecked(product.isSelected());

        holder.checkBox_cart.setOnCheckedChangeListener(null);

        holder.checkBox_cart.setChecked(product.isSelected());

        holder.checkBox_cart.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // Cập nhật trạng thái chọn của sản phẩm và thông báo lại lên listener
            product.setSelected(isChecked);
            listener.onProductChecked(product, isChecked);
        });
       holder.btn_minus.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               int currentQuantity = product.getQuantity();
               if (currentQuantity > 1) {
                   product.setQuantity(currentQuantity - 1);
                   holder.cart_quantity.setText(String.valueOf(product.getQuantity()));
               }
           }
       });
        holder.btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                product.setQuantity(product.getQuantity() + 1);
                holder.cart_quantity.setText(String.valueOf(product.getQuantity()));
            }
        });
        String imageUrl = product.getProductId().getImage().get(0);
        if (imageUrl != null && !imageUrl.isEmpty()) {
            if (imageUrl.startsWith("http://localhost")) {

                imageUrl = imageUrl.replace("http://localhost", "http://10.0.2.2");
            }
            Log.d("ImageURL", "Image URL: " + imageUrl);
            Glide.with(holder.img_cart.getContext())
                    .load(imageUrl)
                    .into(holder.img_cart);
        }
        if (product.getSizeId() != null) {
            holder.cart_size.setText("Size: " + product.getSizeId().getName());
        } else {
            holder.cart_size.setText("No size available");
        }

    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img_cart;
        CheckBox checkBox_cart;
        MaterialButton btn_minus,btn_add;
        TextView cart_name,cart_size,cart_price,cart_quantity;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img_cart=itemView.findViewById(R.id.cart_IMG);
            cart_name=itemView.findViewById(R.id.cart_name);
            cart_size=itemView.findViewById(R.id.cart_size);
            cart_price=itemView.findViewById(R.id.cart_price);
            checkBox_cart=itemView.findViewById(R.id.checkbox);
            cart_quantity=itemView.findViewById(R.id.quantity_cart);
            btn_minus=itemView.findViewById(R.id.nexttypeproductupload);
            btn_add=itemView.findViewById(R.id.addtypeproductupload);
        }
    }
}
