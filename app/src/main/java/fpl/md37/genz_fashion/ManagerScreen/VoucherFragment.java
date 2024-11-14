package fpl.md37.genz_fashion.ManagerScreen;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.genz_fashion.R;

import java.util.ArrayList;

import fpl.md37.genz_fashion.adapter.AdapterTypeProduct;
import fpl.md37.genz_fashion.adapter.AdapterVoucher;
import fpl.md37.genz_fashion.api.HttpRequest;
import fpl.md37.genz_fashion.models.Response;
import fpl.md37.genz_fashion.models.TypeProduct;
import fpl.md37.genz_fashion.models.Voucher;
import retrofit2.Call;
import retrofit2.Callback;

public class VoucherFragment extends Fragment {
  private RecyclerView recyclerView;
  private ArrayList<Voucher> listvoucher;
  private AdapterVoucher adapter;
  private HttpRequest httpRequest;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_voucher, container, false);
        recyclerView=view.findViewById(R.id.rcv_view);
        ImageView btnback = view.findViewById(R.id.btnout);
        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MainActivityManager.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
            }
        });
        httpRequest=new HttpRequest();
        fetchVoucher();
        return  view;

    }
    private void fetchVoucher() {
        httpRequest.callApi().getAllVoucher().enqueue(new Callback<Response<ArrayList<Voucher>>>() {
            @Override
            public void onResponse(Call<Response<ArrayList<Voucher>>> call, retrofit2.Response<Response<ArrayList<Voucher>>> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus() == 200) {
                        listvoucher = response.body().getData();
                        if (listvoucher != null) {
                            for (Voucher voucher : listvoucher) {
                                Log.d("TypeProduct", "ID: " + voucher.getId() + ", Name: " + voucher.getImage());
                            }
                        }
                        setupRecyclerView(listvoucher);
                        Toast.makeText(getContext(), "List displayed successfully! ", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<Response<ArrayList<Voucher>>> call, Throwable t) {
                Log.d("Error", t.getMessage());
            }
        });
    }
    private void setupRecyclerView(ArrayList<Voucher> ds) {
        adapter = new AdapterVoucher(getContext(), ds);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);
    }

}