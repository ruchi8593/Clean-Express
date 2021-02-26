package com.example.cleaningconsultancy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CleanerDetail extends AppCompatActivity {

    private TextView txtPrice, txtName, txtDescription, txtTo, txtFrom, phoneNumber;
    Button btnBookNow;
    String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cleaner_detail);

        findId();
        getActivityData();

        btnBookNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityData();
            }
        });
    }

    private void findId() {
        txtPrice = findViewById(R.id.txtPrice);
        txtName = findViewById(R.id.txtName);
        txtDescription = findViewById(R.id.txtDescription);
        txtFrom = findViewById(R.id.txtFrom);
        txtTo = findViewById(R.id.txtTo);
        btnBookNow = findViewById(R.id.btnBookNow);
        phoneNumber = findViewById(R.id.phoneNumber);
    }

    private void getActivityData() {
        Intent intent = getIntent();
        txtName.setText(intent.getStringExtra("name"));
        txtPrice.setText("$ "+intent.getStringExtra("price"));
        txtDescription.setText(intent.getStringExtra("description"));
        phoneNumber.setText(phoneNumber.getText().toString() + " "+ intent.getStringExtra("phoneNumber"));
        txtFrom.setText(txtFrom.getText().toString() + " "+ intent.getStringExtra("availableFrom") + " AM");
        txtTo.setText(txtTo.getText().toString() + " "+ intent.getStringExtra("availableTo") + " PM");
        currentUserId = intent.getStringExtra("currentUserId");
    }

    private void startActivityData() {
        Intent intent = new Intent(CleanerDetail.this, BookCleaner.class);
        intent.putExtra("price", txtPrice.getText().toString());
        intent.putExtra("name", txtName.getText().toString());
        intent.putExtra("currentUserId", currentUserId);
        startActivity(intent);
    }
}