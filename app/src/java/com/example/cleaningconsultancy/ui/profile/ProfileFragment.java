package com.example.cleaningconsultancy.ui.profile;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.cleaningconsultancy.MainActivity;
import com.example.cleaningconsultancy.R;
import com.example.cleaningconsultancy.ui.login.LoginActivity;
import com.example.cleaningconsultancy.ui.profile.ProfileViewModel;
import com.example.cleaningconsultancy.ui.register.RegisterActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class ProfileFragment extends Fragment {

    private ProfileViewModel profileViewModel;
    private FirebaseFirestore fStore;
    private FirebaseAuth fAuth;
    private String userId;

    ProgressBar progressBar;
    private Button btnSave, btnCancel;
    private EditText fullName, email, phoneNumber, postalCode, availableFrom, availableTo, price, description;
    private Boolean isCleaner;
    LinearLayout idPostalCode, idAvailable, idPrice, idDescription;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        fStore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();

        FirebaseUser user = fAuth.getCurrentUser();
        userId = user.getUid();

        profileViewModel = ViewModelProviders.of(this).get(ProfileViewModel.class);
        View root = inflater.inflate(R.layout.fragment_profile, container, false);

        idPostalCode = root.findViewById(R.id.idPostalCode);
        idAvailable = root.findViewById(R.id.idAvailable);
        idPrice = root.findViewById(R.id.idPrice);
        idDescription = root.findViewById(R.id.idDescription);

        fullName = root.findViewById(R.id.fullName);
        email = (EditText)root.findViewById(R.id.email);
        phoneNumber = (EditText)root.findViewById(R.id.phoneNumber1);
        postalCode = (EditText)root.findViewById(R.id.postalCode1);
        availableFrom = (EditText)root.findViewById(R.id.availableFrom);
        availableTo = (EditText)root.findViewById(R.id.availableTo);
        price = (EditText)root.findViewById(R.id.price1);
        description = (EditText)root.findViewById(R.id.description1);

        btnSave = root.findViewById(R.id.updateProfile);
        btnCancel = root.findViewById(R.id.cancelUpdate);

        progressBar = root.findViewById(R.id.loading);
        progressBar.setVisibility(View.VISIBLE);

        getUserDataAndUpdateUI();

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateAndSaveData();

                    }
                });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
                alertDialog.setTitle("Cancel Changes");
                alertDialog.setMessage("Are you sure you want to cancel changes?");
                alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getUserDataAndUpdateUI();
                    }
                });
                alertDialog.setNegativeButton("No", null);
                alertDialog.show();
            }
        });
        return root;
    }

    private void getUserDataAndUpdateUI (){
        //get current user
        fStore.collection("users").document(userId).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Log.i("User", document.getData().toString());

                                fullName.setText(document.get("name").toString());
                                email.setText(document.get("email").toString());
                                phoneNumber.setText(document.get("phoneNumber").toString());
                                postalCode.setText(document.get("zipCode").toString());
                                availableFrom.setText(document.get("availableFrom").toString());
                                availableTo.setText(document.get("availableTo").toString());
                                price.setText(document.get("price").toString());
                                description.setText(document.get("description").toString());
                                isCleaner = Boolean.parseBoolean(document.get("isCleaner").toString());

                                if(isCleaner){
                                    idPostalCode.setVisibility(View.VISIBLE);
                                    idAvailable.setVisibility(View.VISIBLE);
                                    idPrice.setVisibility(View.VISIBLE);
                                    idDescription.setVisibility(View.VISIBLE);
                                }
                                else {
                                    idPostalCode.setVisibility(View.GONE);
                                    idAvailable.setVisibility(View.GONE);
                                    idPrice.setVisibility(View.GONE);
                                    idDescription.setVisibility(View.GONE);
                                }

                                progressBar.setVisibility(View.GONE);
                            }
                        } else {
                            progressBar.setVisibility(View.GONE);
                            Log.i("USER_LIST_FAIL", "Error getting documents.", task.getException());
                        }
                    }
                });

    }

    private void validateAndSaveData(){
        String strName = fullName.getText().toString();
        String strEmail = email.getText().toString();
        String strPhoneNumber = phoneNumber.getText().toString();
        String strPostalCode = postalCode.getText().toString();
        String strAvailabileFrom = availableFrom.getText().toString();
        String strAvailabileTo = availableTo.getText().toString();
        String strPrice = price.getText().toString();
        String strDescription = description.getText().toString();
        if(TextUtils.isEmpty(strName)){
            fullName.setError("Full Name is required");
            return;
        }

        if(TextUtils.isEmpty(strEmail)){
            email.setError("Email is required");
            return;
        }

        if(TextUtils.isEmpty(strPhoneNumber)){
            phoneNumber.setError("Phone Number is required");
            return;
        }

        if(isCleaner && TextUtils.isEmpty(strPostalCode)){
            postalCode.setError("Postal Code is required");
            return;
        }

        if(isCleaner && TextUtils.isEmpty(strAvailabileFrom)){
            availableFrom.setError("Availability is required");
            return;
        }
        if(isCleaner && TextUtils.isEmpty(strAvailabileTo)){
            availableTo.setError("Availability is required");
            return;
        }

        if(isCleaner && TextUtils.isEmpty(strPrice)){
            price.setError("Price is required");
            return;
        }

        if(isCleaner && TextUtils.isEmpty(strDescription)){
            description.setError("Description is required");
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        updateUserData(strName, strEmail, strPhoneNumber, strPostalCode, strAvailabileFrom, strAvailabileTo, strPrice, strDescription, isCleaner);
    }
    private void updateUserData(String strName, String strEmail, String strPhoneNumber, String strPostalCode, String strAvailabileFrom, String strAvailabileTo, String strPrice, String strDescription, Boolean isCleaner) {
        DocumentReference documentReference = fStore.collection("users")
                .document(userId);
        Map<String, Object> mapUser = new HashMap<>();
        mapUser.put("name", strName);
        mapUser.put("email", strEmail);
        mapUser.put("phoneNumber", strPhoneNumber);
        mapUser.put("zipCode", strPostalCode);
        mapUser.put("availableFrom", strAvailabileFrom);
        mapUser.put("availableTo", strAvailabileTo);
        Integer price = 0;
        if(strPrice != "" && strPrice != null)
            price = Integer.parseInt(strPrice);
        mapUser.put("price", price);
        mapUser.put("description", strDescription);
        mapUser.put("isCleaner", isCleaner);

        documentReference.set(mapUser)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        progressBar.setVisibility(View.GONE);
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
                        alertDialog.setTitle("Update Profile");
                        alertDialog.setMessage("Profile Updated Successfully");
                        alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                startActivity(new Intent(getContext(), MainActivity.class));
                            }
                        });
                        alertDialog.show();
                        Log.i("OnSuccess","User profile is updated successfully");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressBar.setVisibility(View.GONE);
                        Log.i("OnFailure","Opps!User profile is not updated. "+ e);
                    }
                });
    }

}