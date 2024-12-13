package fpl.md37.genz_fashion.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.genz_fashion.R;
import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import fpl.md37.genz_fashion.models.EvaluationRequest;
import fpl.md37.genz_fashion.models.EvaluationRequest2;
import fpl.md37.genz_fashion.models.ProducItem;
import fpl.md37.genz_fashion.models.Product;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {

    private List<EvaluationRequest2> reviews;
    private Context context;

    public ReviewAdapter(List<EvaluationRequest2> reviews, Context context) {
        this.reviews = reviews;
        this.context = context;
    }



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_review, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        EvaluationRequest2 review = reviews.get(position);

        String idUser = review.getIdClient();
        FirebaseFirestore fstore = FirebaseFirestore.getInstance();
        DocumentReference documentReference = fstore.collection("Client").document(idUser);
        documentReference.get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                String firestoreName = task.getResult().getString("name");
                String firestoreAvatar = task.getResult().getString("avatar");

                if (firestoreName != null && !firestoreName.isEmpty()) {
                    holder.userName.setText(firestoreName);
                }
                if (firestoreAvatar != null && !firestoreAvatar.isEmpty()) {
                    byte[] decodedString = Base64.decode(firestoreAvatar, Base64.DEFAULT);
                    Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                    Glide.with(holder.userImage.getContext())
                            .load(decodedBitmap)
                            .placeholder(R.drawable.default_avatar)
                            .into(holder.userImage);
                }
            } else {
                Log.d("ProfileFragment", "Failed to fetch user data from Firestore");
            }
        });

        StringBuilder productDetails = new StringBuilder();
        for (ProducItem productItem : review.getProduct()) {
            String productName = productItem.getProductId().getProduct_name().toUpperCase();
            String sizeName = productItem.getSizeId().getName().toUpperCase();
            productDetails.append(productName).append(" + ").append(sizeName).append(" , ");
        }

        if (productDetails.length() > 0) {
            productDetails.setLength(productDetails.length() - 2);
        }
        holder.productName.setText("Product: " + productDetails.toString());


        holder.rating.setRating(review.getStars());


        // Set review text
        holder.reviewText.setText(review.getReviewText());
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView userImage;
        TextView userName;
        TextView productName;
        RatingBar rating;
        TextView reviewText;

        public ViewHolder(View itemView) {
            super(itemView);
            userImage = itemView.findViewById(R.id.userImage);
            userName = itemView.findViewById(R.id.userName);
            productName = itemView.findViewById(R.id.productName);
            rating = itemView.findViewById(R.id.rating);
            reviewText = itemView.findViewById(R.id.reviewText);
        }
    }
}
