package fpl.md37.genz_fashion.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.genz_fashion.R;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import fpl.md37.genz_fashion.models.EvaluationRequest2;
import fpl.md37.genz_fashion.models.ProducItem;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {

    private final List<EvaluationRequest2> reviews;
    private final Context context;

    public ReviewAdapter(List<EvaluationRequest2> reviews, Context context) {
        this.reviews = reviews;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_review, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        EvaluationRequest2 review = reviews.get(position);

        // Load dữ liệu từ Firestore
        String idUser = review.getIdClient();
        FirebaseFirestore fstore = FirebaseFirestore.getInstance();
        DocumentReference documentReference = fstore.collection("Client").document(idUser);

        documentReference.get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                String firestoreName = task.getResult().getString("name");
                String firestoreAvatar = task.getResult().getString("avatar");

                // Hiển thị tên người dùng
                if (firestoreName != null && !firestoreName.isEmpty()) {
                    holder.userName.setText(firestoreName);
                }

                // Hiển thị ảnh đại diện (với kiểm tra context an toàn)
                if (firestoreAvatar != null && !firestoreAvatar.isEmpty()) {
                    try {
                        byte[] decodedString = Base64.decode(firestoreAvatar, Base64.DEFAULT);
                        Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

                        Context imageContext = holder.userImage.getContext();
                        if (imageContext instanceof android.app.Activity && ((android.app.Activity) imageContext).isDestroyed()) {
                            return; // Không tiếp tục nếu activity bị hủy
                        }

                        Glide.with(imageContext)
                                .load(decodedBitmap)
                                .placeholder(R.drawable.default_avatar)
                                .into(holder.userImage);
                    } catch (Exception e) {
                        Log.e("ReviewAdapter", "Error loading avatar", e);
                    }
                }
            } else {
                Log.d("ReviewAdapter", "Failed to fetch user data from Firestore");
            }
        });

        // Hiển thị chi tiết sản phẩm
        StringBuilder productDetails = new StringBuilder();
        for (ProducItem productItem : review.getProduct()) {
            if (productItem.getProductId() != null && productItem.getSizeId() != null) {
                String productName = productItem.getProductId().getProduct_name().toUpperCase();
                String sizeName = productItem.getSizeId().getName().toUpperCase();
                productDetails.append(productName).append(" + ").append(sizeName).append(", ");
            }
        }

        // Loại bỏ dấu phẩy cuối cùng
        if (productDetails.length() > 0) {
            productDetails.setLength(productDetails.length() - 2);
        }
        holder.productName.setText("Product: " + productDetails.toString());

        // Hiển thị đánh giá sao
        holder.rating.setRating(review.getStars());

        // Hiển thị nội dung đánh giá
        holder.reviewText.setText(review.getReviewText());
    }

    @Override
    public int getItemCount() {
        return reviews != null ? reviews.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView userImage;
        TextView userName;
        TextView productName;
        RatingBar rating;
        TextView reviewText;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            userImage = itemView.findViewById(R.id.userImage);
            userName = itemView.findViewById(R.id.userName);
            productName = itemView.findViewById(R.id.productName);
            rating = itemView.findViewById(R.id.rating);
            reviewText = itemView.findViewById(R.id.reviewText);
        }
    }
}
