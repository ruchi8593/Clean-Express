package com.example.cleaningconsultancy.repositories;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Map;

public class UserRepository {

    private static FirebaseFirestore db;
    private static final String DOCUMENT_NAME = "users";

    public UserRepository(){
        db = FirebaseFirestore.getInstance();
    }
//
//    public Map<String, Object> getAllUsers(){
//
//    }
}
