package fpl.md37.genz_fashion.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.genz_fashion.R;

import java.util.ArrayList;

import fpl.md37.genz_fashion.handel.Item_Handel_click;
import fpl.md37.genz_fashion.models.TypeProduct;

public class AdapterTypeProductUser extends RecyclerView.Adapter<AdapterTypeProductUser.ViewHolder>{
    private Context context;
    private Item_Handel_click listener;

    public AdapterTypeProductUser(Context context, ArrayList<TypeProduct> typeProducts,Item_Handel_click listener) {
        this.context = context;
        this.typeProducts = typeProducts;
        this.listener = listener;
    }

    private ArrayList<TypeProduct> typeProducts;
    @NonNull
    @Override
    public AdapterTypeProductUser.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = ((Activity)context).getLayoutInflater();
        View view = inflater.inflate(R.layout.item_typeproduct_btn,parent,false);
        return new AdapterTypeProductUser.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterTypeProductUser.ViewHolder holder, int position) {
        TypeProduct typeProduct=typeProducts.get(position);
        holder.name.setText(typeProduct.getName());
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onTypeProductClick(typeProduct.getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return typeProducts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name ;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tvFilter);
        }
    }
}
