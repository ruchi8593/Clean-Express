package com.example.cleaningconsultancy.ui.order;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.cleaningconsultancy.R;
import com.example.cleaningconsultancy.ui.search.CleanerAdapter;
import com.example.cleaningconsultancy.ui.search.SearchViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import static java.lang.Boolean.parseBoolean;

public class OrderHistory extends Fragment {

    private FirebaseFirestore db;
    FirebaseAuth fAuth;
    String currentUserId;
    LinearLayout noHistorylayout;

    ArrayList<OrderHistoryViewModel> orderHistoryViewModels = new ArrayList<OrderHistoryViewModel>(0);

    public static OrderHistory newInstance() {
        return new OrderHistory();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        fAuth = FirebaseAuth.getInstance();
        currentUserId = fAuth.getCurrentUser().getUid();
        db = FirebaseFirestore.getInstance();

        final View view = inflater.inflate(R.layout.order_history_fragment, container, false);
        db.collection("orders")
                .whereEqualTo("currentUserId", currentUserId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.i("USER_LIST_SUCCESS", document.getId() + " => " + document.getData());
                                String name = document.getData().get("cleanerName").toString();
                                String price = document.getData().get("orderPrice").toString();
                                String date = document.getData().get("orderDate").toString();
                                String time = document.getData().get("orderTime").toString();

                                OrderHistoryViewModel model = new OrderHistoryViewModel(
                                        name,
                                        price,
                                        date,
                                        time
                                );
                                orderHistoryViewModels.add(model);
                            }
//                                        if(searchViewModels.size() == 0){
//                                            searchViewModels.add(new SearchViewModel("No Cleaner Found", ""));
//                                        }
                            RecyclerView myNewList = (RecyclerView) view.findViewById(R.id.orderList);
                            myNewList.setLayoutManager(new GridLayoutManager(getContext(), 1));
                            myNewList.setAdapter(new OrderAdapter(orderHistoryViewModels, getContext()));
                        } else {
                            Log.i("USER_LIST_FAIL", "Error getting documents.", task.getException());
                        }

                        if(orderHistoryViewModels.size() == 0){
                            noHistorylayout = view.findViewById(R.id.noHistorylayout);
                            noHistorylayout.setVisibility(View.VISIBLE);
                        }
                    }
                });
        return view;
    }

}