package fpl.md37.genz_fashion.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.genz_fashion.R;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import fpl.md37.genz_fashion.models.ProducItem;
import fpl.md37.genz_fashion.models.Product;
import fpl.md37.genz_fashion.models.Size;

public class ProductPMAdapter extends RecyclerView.Adapter<ProductPMAdapter.ViewHolder>{
    private ArrayList<ProducItem> productItemList;
    private Context context;

    public ProductPMAdapter(ArrayList<ProducItem> productItemList, Context context) {
        this.productItemList = productItemList;
        this.context = context;
    }

    @NonNull
    @Override
    public ProductPMAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_product_order_payment, parent, false);
        return new ProductPMAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductPMAdapter.ViewHolder holder, int position) {
        ProducItem productItem = productItemList.get(position);
        Product product = productItem.getProductId();
        Size size = productItem.getSizeId();

        if (product != null) {
            holder.tvProductName_pm.setText(product.getProduct_name());
            double priceValue = Double.parseDouble(product.getPrice());
            NumberFormat numberFormat = NumberFormat.getInstance(new Locale("vi", "VN"));
            String formattedAmount = numberFormat.format(priceValue);
            holder.tvProductPrice_pm.setText(formattedAmount + " VND");


            if (size != null) {
                holder.tvProductSizeQty_pm.setText("Size: " + size.getName() + " | Qty: " + productItem.getQuantity());
            } else {
                holder.tvProductSizeQty_pm.setText("Size: N/A | Qty: " + productItem.getQuantity());
            }
            // Kiểm tra xem có ảnh không trước khi load
            if (product.getImage() != null && !product.getImage().isEmpty()) {
                Glide.with(context).load(product.getImage().get(0)).into(holder.ivProductImage_pm);
            } else {
                Glide.with(context).load(R.drawable.ic_launcher_background).into(holder.ivProductImage_pm); // placeholder nếu không có ảnh
            }
        }
    }

    @Override
    public int getItemCount() {
        return productItemList != null ? productItemList.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvProductName_pm, tvProductPrice_pm, tvProductSizeQty_pm;
        ImageView ivProductImage_pm;

        public ViewHolder(View itemView) {
            super(itemView);
            tvProductName_pm = itemView.findViewById(R.id.tvProductName_pm);
            tvProductPrice_pm = itemView.findViewById(R.id.tvProductPrice_pm);
            tvProductSizeQty_pm = itemView.findViewById(R.id.tvProductSizeQty_pm);
            ivProductImage_pm = itemView.findViewById(R.id.ivProductImage_pm);
        }
    }
}
