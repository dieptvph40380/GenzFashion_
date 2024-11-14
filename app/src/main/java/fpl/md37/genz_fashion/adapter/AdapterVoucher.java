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

import fpl.md37.genz_fashion.models.Product;
import fpl.md37.genz_fashion.models.Voucher;

public class AdapterVoucher extends RecyclerView.Adapter<AdapterVoucher.ViewHolder> {
    private Context context;
    private ArrayList<Voucher> listVoucher;

    public AdapterVoucher(Context context, ArrayList<Voucher> listVoucher) {
        this.context = context;
        this.listVoucher = listVoucher;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = ((Activity)context).getLayoutInflater();
        View view = inflater.inflate(R.layout.item_voucher,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Voucher voucher=listVoucher.get(position);
        String imageUrl = voucher.getImage();
        if (imageUrl != null && !imageUrl.isEmpty()) {
            if (imageUrl.startsWith("http://localhost")) {

                imageUrl = imageUrl.replace("http://localhost", "http://10.0.2.2");
            }
            Log.d("ImageURL", "Image URL: " + imageUrl);
            Glide.with(holder.imageView.getContext())
                    .load(imageUrl)
                    .into(holder.imageView);
        }
        holder.name.setText(voucher.getName());
        holder.dis.setText(voucher.getDescription());
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
        return listVoucher.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView name, dis, date;
        ImageButton btnedit, btndelete;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.ImageVoucher);
            name=itemView.findViewById(R.id.Namevoucher);
            dis=itemView.findViewById(R.id.discriptionvoucher);;
            btnedit=itemView.findViewById(R.id.btnEdit);
            btndelete=itemView.findViewById(R.id.btnDelete);
        }
    }
}
