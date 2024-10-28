package fpl.md37.genz_fashion.ManagerScreen;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.genz_fashion.R;

public class MainActivity_Manager extends AppCompatActivity {
    LinearLayout products,typeproduct,support,voucher,orders,supplierss,statis,infor,detail;
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

        products.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity_Manager.this, ProductsFragment.class));
            }
        });

        typeproduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(new TypeProductFragment());
            }
        });

        supplierss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(new SuppliersFragment());
            }
        });

        statis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity_Manager.this, StatisticalFragment.class));
            }
        });

    }
    private void replaceFragment(Fragment targetFragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout2, targetFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

}