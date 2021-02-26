package com.example.cleaningconsultancy.ui.register;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.cleaningconsultancy.BookCleaner;
import com.example.cleaningconsultancy.MainActivity;
import com.example.cleaningconsultancy.R;
import com.example.cleaningconsultancy.ui.login.LoginActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private EditText availableFrom, availableTo;
    EditText mFullName, mEmail, mPassword, mPhone,mPostalCode,confirmPassword;
    Button mRegister, mLogin,mCancel;
    FirebaseAuth fAuth;
    FirebaseFirestore fstore;
    ProgressBar progressBar;
    String userID;
    CheckBox chkCleaner;
    LinearLayout idPostalCode,idAvailable,idFromTo;
    private TimePickerDialog pickTimeDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mFullName = findViewById(R.id.fullName);
        mEmail = findViewById(R.id.email);
        mPassword = findViewById(R.id.password);
        confirmPassword = findViewById(R.id.confirmPassword);
        mPhone = findViewById(R.id.phnumber);
        mFullName = findViewById(R.id.fullName);
        mPostalCode = findViewById(R.id.location);
        chkCleaner = findViewById(R.id.chkCleaner);
        idPostalCode = findViewById(R.id.idPostalCode);
        idAvailable = findViewById(R.id.idAvailable);
        idFromTo = findViewById(R.id.idFromTo);
        mRegister = findViewById(R.id.register);
        mLogin = findViewById(R.id.btnLogIn);
        mCancel = findViewById(R.id.cancelReg);

        availableFrom = findViewById(R.id.availableFrom);
        availableFrom.setInputType(InputType.TYPE_NULL);

        availableTo = findViewById(R.id.availableTo);
        availableTo.setInputType(InputType.TYPE_NULL);

        fAuth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();

        progressBar = findViewById(R.id.loading);

        if(fAuth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }


        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();
                String confirmPwd = confirmPassword.getText().toString().trim();

                final String fullname = mFullName.getText().toString().trim();
                final String phNumber = mPhone.getText().toString().trim();

                //If Cleaner
                final String fromAvailable = availableFrom.getText().toString().trim();
                final String toAvailable = availableTo.getText().toString().trim();
                final String postalCode = mPostalCode.getText().toString().trim();


                if(TextUtils.isEmpty(email)){
                    mEmail.setError("Email is required");
                    return;
                }

                else if(TextUtils.isEmpty(password)){
                    mPassword.setError("Password is required");
                    return;
                }

                else if(!TextUtils.isEmpty(password) && password.length() < 6){
                    mPassword.setError("Password Must be >= 6 Characters");
                    return;
                }
                else if(TextUtils.isEmpty(confirmPwd)){
                    mPassword.setError("Confirm Password is required");
                    return;
                }

                else if(!password.equals(confirmPwd)){
                    confirmPassword.setError("Your password do not match with your confirm password");
                    return;
                }

                else if(TextUtils.isEmpty(fullname)){
                    mFullName.setError("Full Name is required");
                    return;
                }

                else if(TextUtils.isEmpty(phNumber)){
                    mPhone.setError("Phone number is required");
                    return;
                }
                else if(chkCleaner.isChecked() && TextUtils.isEmpty(fromAvailable)){
                    availableFrom.setError("Availability is required");
                    return;
                }
                else if(chkCleaner.isChecked() && TextUtils.isEmpty(toAvailable)){
                    availableTo.setError("Availability is required");
                    return;
                }
                else if(chkCleaner.isChecked() && TextUtils.isEmpty(postalCode)){
                    mPostalCode.setError("Postal code is required");
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);

                fAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    final FirebaseUser user = fAuth.getCurrentUser();
                                    Toast.makeText(getApplicationContext(), "Welcome to Clean Express", Toast.LENGTH_SHORT).show();
                                    userID = user.getUid();

                                    Map<String, Object> mapUser = new HashMap<>();
                                    mapUser.put("name", fullname);
                                    mapUser.put("email", email);
                                    mapUser.put("phoneNumber", phNumber);
                                    mapUser.put("isCleaner", chkCleaner.isChecked());
                                    mapUser.put("zipCode", postalCode.toUpperCase());
                                    mapUser.put("availableFrom", fromAvailable);
                                    mapUser.put("availableTo", toAvailable);
                                    mapUser.put("price", 0);
                                    mapUser.put("description", "");
                                    DocumentReference documentReference = fstore.collection("users")
                                            .document(userID);
                                    documentReference.set(mapUser)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Log.i("OnSuccess","user profile is created for "+ userID);
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Log.i("OnFailure","user profile is not created.Error "+ e);
                                                }
                                            });
                                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                } else {
                                    Toast.makeText(getApplicationContext(), "Authentication failed. Error ! "+task.getException(), Toast.LENGTH_SHORT).show();
                                    Log.i("OnFailure","user profile is not created.Exception "+task.getException());
                                    progressBar.setVisibility(View.GONE);
                                }
                            }
                        });
            }
        });

        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }
        });

        chkCleaner.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    idPostalCode.setVisibility(View.VISIBLE);
                    idAvailable.setVisibility(View.VISIBLE);
                    idFromTo.setVisibility(View.VISIBLE);
                }
                else {
                    idPostalCode.setVisibility(View.GONE);
                    idAvailable.setVisibility(View.GONE);
                    idFromTo.setVisibility(View.GONE);
                }
            }
        });

        setTime();
    }
    private void setTime() {

        availableFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar calendar = Calendar.getInstance();
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minutes = calendar.get(Calendar.MINUTE);

                pickTimeDialog = new TimePickerDialog(RegisterActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int h, int m) {
                        availableFrom.setText(h + ":" + m);
                    }
                }, hour, minutes, true);
                pickTimeDialog.show();
            }
        });
        availableTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar calendar = Calendar.getInstance();
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minutes = calendar.get(Calendar.MINUTE);

                pickTimeDialog = new TimePickerDialog(RegisterActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int h, int m) {
                        availableTo.setText(h + ":" + m);
                    }
                }, hour, minutes, true);
                pickTimeDialog.show();
            }
        });
    }

}