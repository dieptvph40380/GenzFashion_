package fpl.md37.genz_fashion.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.genz_fashion.R;

import java.util.ArrayList;

import fpl.md37.genz_fashion.models.Order;
import fpl.md37.genz_fashion.models.ProducItem;

public class AdapterOrderPendingAdapter extends RecyclerView.Adapter<AdapterOrderPendingAdapter.ViewHolder>{
    Context context;
    private ArrayList<Order> orderList;

    public AdapterOrderPendingAdapter(Context context, ArrayList<Order> orderList) {
        this.context = context;
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public AdapterOrderPendingAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_pending_payment, parent, false);
        return new AdapterOrderPendingAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterOrderPendingAdapter.ViewHolder holder, int position) {
        Order order = orderList.get(position);
        holder.timecancle_pm.setText(order.getTimeCancleOrder());

        ArrayList<ProducItem> productList = new ArrayList<>(order.getProducts());
        int totalQuantity = 0;
        for (ProducItem productItem : productList) {
            totalQuantity += productItem.getQuantity();
        }

        holder.total_pm.setText(""+ totalQuantity+" items: "+ order.getTotalAmount());



        if (!productList.isEmpty()) {
            ProductPMAdapter productAdapter = new ProductPMAdapter(productList, context);
            holder.rvProductList_pm.setAdapter(productAdapter);
        } else {
            Log.d("AdapterOderActive", "Product list is empty.");
        }
    }

    @Override
    public int getItemCount() {
        return orderList == null ? 0 : orderList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView timecancle_pm,total_pm;
        RecyclerView rvProductList_pm;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            rvProductList_pm = itemView.findViewById(R.id.rvProductList_order_pm);
            total_pm = itemView.findViewById(R.id.total_order_pm);
            timecancle_pm = itemView.findViewById(R.id.timeorder_pm);
            LinearLayoutManager layoutManager = new LinearLayoutManager(itemView.getContext());
            layoutManager.setReverseLayout(true);
            rvProductList_pm.setLayoutManager(layoutManager);

        }
    }
}

