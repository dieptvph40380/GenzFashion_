package fpl.md37.genz_fashion.ManagerScreen;

import android.content.Context;
import android.os.Bundle;
import android.os.ConditionVariable;
import android.util.Log;
import android.widget.SearchView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.genz_fashion.R;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import fpl.md37.genz_fashion.adapter.AdapterViewCustomer;
import fpl.md37.genz_fashion.models.Client;

public class InformationFragment extends AppCompatActivity {
    FirebaseFirestore db;
    RecyclerView rcv_client, rcv_grocery;
    SearchView searchCustomer;
    Context context;
    ArrayList<Client> clients = new ArrayList<>();
    AdapterViewCustomer adapter_clients;
    ArrayList<Client> filteredClients = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.fragment_customer_information);

        rcv_client = findViewById(R.id.rcv_client);
        searchCustomer = findViewById(R.id.search_Customer);
        db = FirebaseFirestore.getInstance();

        adapter_clients = new AdapterViewCustomer(this, clients, db);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
//        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false);
        rcv_client.setLayoutManager(linearLayoutManager);
        rcv_client.setAdapter(adapter_clients);

        ListenFirebaseFirestore_Cilent();

        // Thiết lập listener cho SearchView
        searchCustomer.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterList(newText); // Gọi hàm lọc
                return true;
            }
        });
    }

    private void ListenFirebaseFirestore_Cilent() {
        db.collection("Client")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            Log.e("TAG", "fail", error);
                            return;
                        }
                        if (value != null) {
                            for (DocumentChange dc : value.getDocumentChanges()) {
                                switch (dc.getType()) {
                                    case ADDED: {
                                        Client newU = dc.getDocument().toObject(Client.class);
                                        clients.add(newU);
                                        adapter_clients.notifyItemInserted(clients.size() - 1);
                                        break;
                                    }
                                    case MODIFIED: {
                                        Client update = dc.getDocument().toObject(Client.class);
                                        if (dc.getOldIndex() == dc.getNewIndex()) {
                                            clients.set(dc.getOldIndex(), update);
                                            adapter_clients.notifyItemChanged(dc.getOldIndex());

                                        } else {
                                            clients.remove(dc.getOldIndex());
                                            clients.add(update);
                                            adapter_clients.notifyItemMoved(dc.getOldIndex(), dc.getNewIndex());

                                        }
                                        break;
                                    }
                                    case REMOVED: {
                                        dc.getDocument().toObject(Client.class);
                                        clients.remove(dc.getOldIndex());
                                        adapter_clients.notifyItemRemoved(dc.getOldIndex());
                                        break;
                                    }
                                }
                            }
                        }
                    }
                });
    }

    private void filterList(String text) {
        filteredClients.clear(); // Xóa danh sách lọc trước đó
        if (text.isEmpty()) {
            filteredClients.addAll(clients); // Nếu ô tìm kiếm trống, thêm lại tất cả khách hàng
        } else {
            for (Client client : clients) {
                if (client.getName().toLowerCase().contains(text.toLowerCase())) { // Kiểm tra nếu tên chứa chuỗi tìm kiếm
                    filteredClients.add(client);
                }
            }
        }
        adapter_clients.updateList(filteredClients); // Cập nhật danh sách trong adapter
    }

}