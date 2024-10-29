package fpl.md37.genz_fashion.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.genz_fashion.R;

import java.util.ArrayList;

import fpl.md37.genz_fashion.models.TypeProduct;

public class AdapterTypeProduct extends RecyclerView.Adapter<AdapterTypeProduct.ViewHolder>{
    private Context context;
private ArrayList<TypeProduct> typeProducts;

    public AdapterTypeProduct(Context context, ArrayList<TypeProduct> typeProducts) {
        this.context = context;
        this.typeProducts = typeProducts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = ((Activity)context).getLayoutInflater();
        View view = inflater.inflate(R.layout.itemtypeproduct,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
    TypeProduct typeProduct=typeProducts.get(position);
        String imageUrl = typeProduct.getImage();
        Log.d("ImageURL", "Image URL: " + imageUrl);
        if (imageUrl != null && !imageUrl.isEmpty()) {
            Glide.with(holder.imageView.getContext())
                    .load(imageUrl)

                    .into(holder.imageView);
        }
        holder.type.setText(typeProduct.getName());

        holder.btnedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        holder.btndelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
    }

    @Override
    public int getItemCount() {
        return typeProducts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
     ImageView imageView;
     TextView type, size;
     ImageButton btnedit, btndelete;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.ivTypeProductImage);
            type=itemView.findViewById(R.id.tvTypeProductName);
            size=itemView.findViewById(R.id.tvTypeProductSize);;
            btnedit=itemView.findViewById(R.id.btnEdit);
            btndelete=itemView.findViewById(R.id.btnDelete);
        }
    }
}
