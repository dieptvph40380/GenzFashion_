package fpl.md37.genz_fashion.UserScreen;

import static android.app.PendingIntent.getActivity;
import static java.security.AccessController.getContext;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.genz_fashion.R;

public class EvaluateSuccessfull extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_evaluate_successfull);
        Button button = findViewById(R.id.backhome_ev);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EvaluateSuccessfull.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}