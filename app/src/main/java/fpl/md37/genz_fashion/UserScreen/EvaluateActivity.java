package fpl.md37.genz_fashion.UserScreen;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.example.genz_fashion.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import fpl.md37.genz_fashion.api.HttpRequest;
import fpl.md37.genz_fashion.models.Client;
import fpl.md37.genz_fashion.models.EvaluationRequest;
import fpl.md37.genz_fashion.models.ProducItem;
import fpl.md37.genz_fashion.utils.FirebaseUtil;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EvaluateActivity extends AppCompatActivity {
    private ProducItem productItem;
    private ImageView productImageView;
    private TextView productNameTextView, productDetailsTextView, productPriceTextView;
    private EditText et_re;
    private ImageView[] stars = new ImageView[5];
    private String review,currentTime;
    private int selectedStars = 0;
    private Button submit;
    private HttpRequest httpRequest;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evaluate);
        httpRequest = new HttpRequest();
        productImageView = findViewById(R.id.img_product_ev);
        productNameTextView = findViewById(R.id.tv_product_name_ev);
        productDetailsTextView = findViewById(R.id.tv_product_details_ev);
        productPriceTextView = findViewById(R.id.tv_product_price_ev);
        submit = findViewById(R.id.btn_submit);
        et_re = findViewById(R.id.et_review);

        currentTime = new SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault()).format(new Date());

        ImageButton imback = findViewById(R.id.btn_back_ev);

        stars[0] = findViewById(R.id.star_1);
        stars[1] = findViewById(R.id.star_2);
        stars[2] = findViewById(R.id.star_3);
        stars[3] = findViewById(R.id.star_4);
        stars[4] = findViewById(R.id.star_5);
        for (int i = 0; i < stars.length; i++) {
            final int index = i;
            stars[i].setOnClickListener(v -> updateStars(index + 1));
        }

        imback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        // Nhận đối tượng ProducItem từ Intent
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("product_item_data")) {
            productItem = (ProducItem) intent.getSerializableExtra("product_item_data");
        }

        if (productItem != null) {

            productNameTextView.setText(productItem.getProductId().getProduct_name());
            String details = "Size: " + productItem.getSizeId().getName() + " | Qty: " + productItem.getQuantity();
            productDetailsTextView.setText(details);
            productPriceTextView.setText("$" + String.valueOf(productItem.getProductId().getPrice()));
            if (productItem.getProductId().getImage() != null && !productItem.getProductId().getImage().isEmpty()) {
                Glide.with(this).load(productItem.getProductId().getImage().get(0)).into(productImageView);
            } else {
                productImageView.setImageResource(R.drawable.ic_launcher_background);
            }
        }

       submit.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               submitReview();
           }
       });

    }
    public void submitReview() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            Log.d("Details", "User not logged in");
            return;
        }
        String userId = currentUser.getUid();
        review = et_re.getText().toString();
        if (review.isEmpty()) {
            Toast.makeText(EvaluateActivity.this, "Please enter your review.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (selectedStars == 0) {
            Toast.makeText(EvaluateActivity.this, "Please select a rating.", Toast.LENGTH_SHORT).show();
            return;
        }
        // Tạo request
        EvaluationRequest reviewRequest = new EvaluationRequest(userId, productItem, review, selectedStars);
        httpRequest.callApi().submitEvaluation(reviewRequest).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    startActivity(new Intent(EvaluateActivity.this,EvaluateSuccessfull.class));
                } else {
                    try {
                        // Ghi log chi tiết
                        String errorBody = response.errorBody().string();
                        Log.e("EvaluateActivity", "Submission failed. Code: " + response.code() + ", Error: " + errorBody);
                    } catch (Exception e) {
                        Log.e("EvaluateActivity", "Error reading response body: " + e.getMessage());
                    }
                    Toast.makeText(EvaluateActivity.this, "Review submission failed: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("EvaluateActivity", "Error submitting review: " + t.getMessage());
                // Handle network or other errors
            }
        });

    }
    private void updateStars(int count) {
        selectedStars = count;
        for (int i = 0; i < stars.length; i++) {
            if (i < count) {
                stars[i].setImageResource(R.drawable.favorite2); // Ngôi sao được chọn
            } else {
                stars[i].setImageResource(R.drawable.favorite); // Ngôi sao không được chọn
            }
        }
    }
}