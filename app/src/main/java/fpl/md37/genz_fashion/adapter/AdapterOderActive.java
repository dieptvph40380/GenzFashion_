package fpl.md37.genz_fashion.adapter;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.genz_fashion.R;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import fpl.md37.genz_fashion.UserScreen.MainActivity;
import fpl.md37.genz_fashion.handel.Item_Handel_checkOrder;
import fpl.md37.genz_fashion.models.Order;
import fpl.md37.genz_fashion.models.ProducItem;

public class AdapterOderActive extends RecyclerView.Adapter<AdapterOderActive.OrderActiveViewHolder> {

    private ArrayList<Order> orderList;
    private Context context;
    private Item_Handel_checkOrder listener;

    // Constructor
    public AdapterOderActive(ArrayList<Order> orderList, Context context,Item_Handel_checkOrder listener) {
        this.orderList = orderList;
        this.context = context;
        this.listener = listener;
    }


    @NonNull
    @Override
    public OrderActiveViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_active, parent, false);

        return new OrderActiveViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderActiveViewHolder holder, int position) {
        Order order = orderList.get(position);
        holder.timeOrder.setText(order.getTimeOrder());

        ArrayList<ProducItem> productList = new ArrayList<>(order.getProducts());
        int totalQuantity = 0;
        for (ProducItem productItem : productList) {
            totalQuantity += productItem.getQuantity();
        }

        NumberFormat numberFormat = NumberFormat.getInstance(new Locale("vi", "VN"));
        String formattedAmount = numberFormat.format(order.getTotalAmount());
        holder.total.setText(totalQuantity + " items: " + formattedAmount + " VND");

        if (!productList.isEmpty()) {
            ProductACAdapter productAdapter = new ProductACAdapter(productList, context);
            holder.rvProductList.setAdapter(productAdapter);
        } else {
            Log.d("AdapterOderActive", "Product list is empty.");
        }

        // Xử lý click btnTrackOrder
        holder.btnTrackOrder.setOnClickListener(v -> {
            if (listener != null) {
                listener.onTrackOrderClick(order);
            }

            // Gửi thông báo
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            // Intent mở Activity khi nhấn thông báo
            Intent intent = new Intent(context, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

            // Tạo Notification
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "order_notifications")
                    .setSmallIcon(R.drawable.logo_app) // Thay logo_app bằng icon của bạn
                    .setContentTitle("Order Canceled")
                    .setContentText("Your order has been successfully canceled. If you have any questions, please contact customer support.")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true); // Tự động ẩn thông báo khi nhấn vào

            // Hiển thị thông báo
            if (notificationManager != null) {
                notificationManager.notify(position, builder.build()); // ID phải duy nhất cho mỗi thông báo
            }
        });
    }


    @Override
    public int getItemCount() {
        return orderList == null ? 0 : orderList.size();
    }

    // ViewHolder class
    public static class OrderActiveViewHolder extends RecyclerView.ViewHolder {
        TextView timeOrder,total;
        RecyclerView rvProductList;
        Button btnTrackOrder;

        public OrderActiveViewHolder(@NonNull View itemView) {
            super(itemView);
            rvProductList = itemView.findViewById(R.id.rvProductList_order);
            btnTrackOrder = itemView.findViewById(R.id.btnTrackOrder);
            total = itemView.findViewById(R.id.total_order);
            timeOrder = itemView.findViewById(R.id.timeorder);
            rvProductList.setLayoutManager(new LinearLayoutManager(itemView.getContext()));
        }
    }
}