package fpl.md37.genz_fashion.UserScreen;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.fragment.app.Fragment;

import com.example.genz_fashion.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import fpl.md37.genz_fashion.api.HttpRequest;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import androidx.core.app.NotificationCompat;

public class PaymentSuccessfullActivity extends Fragment {

    Button ViewOrder;
    TextView txtNotication, tv_XN;
    private HttpRequest httpRequest;
    private static final String CHANNEL_ID = "payment_success_channel";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.activity_payment_successfull, container, false);

        ViewOrder = v.findViewById(R.id.viewOrderButton);

//        createNotificationChannel();

        ViewOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                showNotification();

                Intent intent = new Intent(getContext(), MainActivity.class);

                startActivity(intent);

                getActivity().overridePendingTransition(R.anim.bounce_in, R.anim.bounce_out);

                getActivity().finish();
            }
        });

        return v;
    }

//    private void createNotificationChannel() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            CharSequence name = "Payment Success";
//            String description = "Notifications for payment success actions";
//            int importance = NotificationManager.IMPORTANCE_DEFAULT;
//
//            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
//            channel.setDescription(description);
//
//            NotificationManager notificationManager = getActivity().getSystemService(NotificationManager.class);
//            if (notificationManager != null) {
//                notificationManager.createNotificationChannel(channel);
//            }
//        }
//    }

//    private void showNotification() {
//
//        Intent intent = new Intent(getContext(), MainActivity.class);
//        intent.putExtra("navigate_to_fragment", "MyOrderFragment");
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//
//
//        PendingIntent pendingIntent = PendingIntent.getActivity(
//                getContext(),
//                0,
//                intent,
//                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
//        );
//
//
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(getContext(), CHANNEL_ID)
//                .setSmallIcon(R.drawable.logo_app)
//                .setContentTitle("Payment Successful")
//                .setContentText("Your payment was successful! Click here to view your order.")
//                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
//                .setContentIntent(pendingIntent)
//                .setAutoCancel(true);
//
//        // Hiển thị thông báo
//        NotificationManager notificationManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
//        if (notificationManager != null) {
//            notificationManager.notify(1, builder.build());
//        }
//    }


    private void showBottomNav() {
        BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.bottom_nav);
        if (bottomNavigationView != null) {
            bottomNavigationView.setVisibility(View.VISIBLE);
        }
    }
}
