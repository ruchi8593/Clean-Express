package com.example.cleaningconsultancy.ui.search;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.cleaningconsultancy.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import static java.lang.Boolean.parseBoolean;

public class SearchFragment extends Fragment {

    private SearchViewModel mViewModel;
    FirebaseAuth fAuth;
    private EditText zipcode;
    private FirebaseFirestore db;
    String currentUserId;
    ArrayList<SearchViewModel> searchViewModels = new ArrayList<SearchViewModel>(0);

    public static SearchFragment newInstance() {
        return new SearchFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        db = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();
        currentUserId = fAuth.getCurrentUser().getUid();

        final View view = inflater.inflate(R.layout.search_fragment, container, false);
        zipcode = view.findViewById(R.id.zipcode);
        zipcode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String txtZipcode = zipcode.getText().toString();
                //Toast.makeText(getContext(), ": "+txtZipcode, Toast.LENGTH_SHORT).show();
                if(txtZipcode.length() == 6) {
                    //to get all users
                    db.collection("users")
                            .whereEqualTo("zipCode", txtZipcode.toUpperCase())
                            .whereEqualTo("isCleaner", true)
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            Log.i("USER_LIST_SUCCESS", document.getId() + " => " + document.getData());
                                            String cleanerId = document.getId();
                                            String name = document.getData().get("name").toString();
                                            String email = document.getData().get("email").toString();
                                            String zipcode = document.getData().get("zipCode").toString();
                                            String phoneNumber = document.getData().get("phoneNumber").toString();
                                            String availableFrom = document.getData().get("availableFrom").toString();
                                            String availableTo = document.getData().get("availableTo").toString();
                                            Integer price = Integer.parseInt(document.getData().get("price").toString());
                                            String description = document.getData().get("description").toString();
                                            Boolean isCleaner = parseBoolean(document.getData().get("isCleaner").toString());

                                            SearchViewModel model = new SearchViewModel(
                                                    currentUserId,
                                                    cleanerId,
                                                    name,
                                                    email,
                                                    zipcode,
                                                    phoneNumber,
                                                    availableFrom,
                                                    availableTo,
                                                    price,
                                                    description,
                                                    isCleaner
                                            );
                                            searchViewModels.add(model);
                                        }
//                                        if(searchViewModels.size() == 0){
//                                            searchViewModels.add(new SearchViewModel("No Cleaner Found", ""));
//                                        }
                                        RecyclerView myNewList = (RecyclerView) view.findViewById(R.id.cleanerList);
                                        myNewList.setLayoutManager(new GridLayoutManager(getContext(), 1));
                                        myNewList.setAdapter(new CleanerAdapter(searchViewModels, getContext()));
                                    } else {
                                        Log.i("USER_LIST_FAIL", "Error getting documents.", task.getException());
                                    }
                                }
                            });
                }
                else {
                  //Toast.makeText(getContext(), "Enter Valid Zipcode", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return  view;
    }



    /*@Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(SearchViewModel.class);
    }*/

}