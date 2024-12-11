package fpl.md37.genz_fashion.ManagerScreen;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.genz_fashion.R;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivityManager extends AppCompatActivity {
    LinearLayout products,typeproduct,support,voucher,orders,supplierss,statis,infor,detail;
    ImageView logout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main_admin);

        products = findViewById(R.id.products_admin);
        typeproduct = findViewById(R.id.producttype_admin);
        support = findViewById(R.id.support_admin);
        voucher = findViewById(R.id.voucher_admin);
        orders = findViewById(R.id.orders_admin);
        supplierss = findViewById(R.id.suppliers_admin);
        statis = findViewById(R.id.statis_admin);
        infor = findViewById(R.id.information_admin);
        detail = findViewById(R.id.ordersdetails_admin);
        logout=findViewById(R.id.AD_logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(MainActivityManager.this, SignInActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                MainActivityManager.this.overridePendingTransition(R.anim.rotate_in, R.anim.zoom_out);
            }
        });
        products.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(new ProductsFragment());
            }
        });

        typeproduct.setOnClickListener(v -> {
            replaceFragment(new TypeProductFragment());
        });

        supplierss.setOnClickListener(v -> {
            replaceFragment(new SupplierFragment());
        });
        voucher.setOnClickListener(v -> {
            replaceFragment(new VoucherFragment());
        });

//        supplierss.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(MainActivityManager.this, SuppliersFragment.class));
//            }
//        });

        statis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(new StatisticalFragment());
            }
        });
         infor.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 replaceFragment(new InformationFragment());
             }
         });

    }
    private void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.sile_right,R.anim.slide_left);
        transaction.replace(R.id.frameLayout1, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

}