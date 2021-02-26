package com.example.cleaningconsultancy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class BookCleaner extends AppCompatActivity {

    private EditText pickDate, pickTime;
    private TextView txtTitle, txtPrice, txtCustName, txtAddress, txtNumber,
            txtEmail, txtCardname, txtCardnumber, txtExpiryMonth, txtExpiryYear, txtCvv;
    RadioGroup payment_methods;
    Button btnPay;

    private DatePickerDialog pickDateDialog;
    private TimePickerDialog pickTimeDialog;

    private SimpleDateFormat dateFormatter;

    FirebaseFirestore fstore;
    String selectedText = "";
    String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_cleaner);

        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.CANADA);
        fstore = FirebaseFirestore.getInstance();

        findId();
        setDate();
        setTime();
        getActivityData();

        btnPay.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                pay();
            }
        });
    }

    private void findId() {
        pickDate = findViewById(R.id.pickDate);
        pickDate.setInputType(InputType.TYPE_NULL);

        pickTime = findViewById(R.id.pickTime);
        pickTime.setInputType(InputType.TYPE_NULL);

        txtTitle = (TextView)findViewById(R.id.txtCleanerName);
        txtPrice = (TextView)findViewById(R.id.txtPrice);

        txtCustName = (TextView)findViewById(R.id.txtCustName);
        txtAddress = (TextView)findViewById(R.id.txtAddress);
        txtNumber = (TextView)findViewById(R.id.txtNumber);
        txtEmail = (TextView)findViewById(R.id.txtEmail);

        txtCardname = (TextView)findViewById(R.id.txtCardname);
        txtCardnumber = (TextView)findViewById(R.id.txtCardnumber);
        txtExpiryMonth = (TextView)findViewById(R.id.txtExpiryMonth);
        txtExpiryYear = (TextView)findViewById(R.id.txtExpiryYear);
        txtCvv = (TextView)findViewById(R.id.txtCvv);
        payment_methods = (RadioGroup) findViewById(R.id.payment_methods);

        btnPay = (Button) findViewById(R.id.btnPay);
    }

    private void setDate() {
        pickDate.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                pickDateDialog = new DatePickerDialog(BookCleaner.this, new OnDateSetListener() {
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar newDate = Calendar.getInstance();
                        newDate.set(year, monthOfYear, dayOfMonth);
                        pickDate.setText(dateFormatter.format(newDate.getTime()));
                    }
                },year, month, day);

                pickDateDialog.show();

            }
        });
    }

    private void setTime() {

        pickTime.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar calendar = Calendar.getInstance();
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minutes = calendar.get(Calendar.MINUTE);

                    pickTimeDialog = new TimePickerDialog(BookCleaner.this, new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker timePicker, int h, int m) {
                            pickTime.setText(h + ":" + m);
                        }
                    }, hour, minutes, true);
                    pickTimeDialog.show();
            }
        });
    }

    private void getActivityData() {
        Intent intent = getIntent();

        txtTitle.setText(txtTitle.getText().toString() + " "+ intent.getStringExtra("name"));
        txtPrice.setText(txtPrice.getText().toString() + " "+ intent.getStringExtra("price"));
        currentUserId = intent.getStringExtra("currentUserId");
    }

    private void pay() {
        boolean isError = validate();

        if(!isError) {

            Map<String, Object> orderMap = new HashMap<>(0);
            orderMap.put("currentUserId", currentUserId);
            orderMap.put("cleanerName", txtTitle.getText().toString().split(": ")[1]);
            orderMap.put("orderPrice", txtPrice.getText().toString().split(": ")[1]);
            orderMap.put("orderDate", pickDate.getText().toString());
            orderMap.put("orderTime", pickTime.getText().toString());
            orderMap.put("customerName", txtCustName.getText().toString());
            orderMap.put("address", txtAddress.getText().toString());
            orderMap.put("phone", txtNumber.getText().toString());
            orderMap.put("email", txtEmail.getText().toString());
            orderMap.put("paymentType", selectedText);
            orderMap.put("cardName", txtCardname.getText().toString());
            orderMap.put("cardnumber", txtCardnumber.getText().toString());
            orderMap.put("expiryMonth", txtExpiryMonth.getText().toString());
            orderMap.put("expiryYear", txtExpiryYear.getText().toString());
            orderMap.put("CVV", txtCvv.getText().toString());

            fstore.collection("orders")
                    .add(orderMap)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Log.i("Order Detail", "Success in Order");
                            Intent intent = new Intent(getApplicationContext(), ThankYouActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            getApplicationContext().startActivity(intent);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.i("Order Detail", "Failure in Order");
                        }
                    });
        }
    }

    private boolean validate() {
        boolean isError = false;
        if(txtCustName.getText().toString().length() ==0)
        {
            txtCustName.requestFocus();
            txtCustName.setError("Name can not be empty.");
            isError = true;
        }
        if(txtAddress.getText().toString().length() ==0)
        {
            txtAddress.requestFocus();
            txtAddress.setError("Address can not be empty.");
            isError = true;
        }
        if(txtNumber.getText().toString().length() ==0)
        {
            txtNumber.requestFocus();
            txtNumber.setError("Contact Number can not be empty.");
            isError = true;
        }
        if(txtEmail.getText().toString().length() ==0)
        {
            txtEmail.requestFocus();
            txtEmail.setError("Email can not be empty.");
            isError = true;
        }
        if(txtCardname.getText().toString().length() ==0)
        {
            txtCardname.requestFocus();
            txtCardname.setError("Card Name can not be empty.");
            isError = true;
        }
        if(txtCardnumber.getText().toString().length() ==0)
        {
            txtCardnumber.requestFocus();
            txtCardnumber.setError("Card Number can not be empty.");
            isError = true;
        }
        if(txtExpiryMonth.getText().toString().length() ==0)
        {
            txtExpiryMonth.requestFocus();
            txtExpiryMonth.setError("Expiry Month can not be empty.");
            isError = true;
        }
        if(txtExpiryYear.getText().toString().length() ==0)
        {
            txtExpiryYear.requestFocus();
            txtExpiryYear.setError("Expiry Year can not be empty.");
            isError = true;
        }
        if(txtCvv.getText().toString().length() ==0)
        {
            txtCvv.requestFocus();
            txtCvv.setError("CVV can not be empty.");
            isError = true;
        }

        if (payment_methods.getCheckedRadioButtonId() == -1)
        {
            isError = true;
        }
        else
        {
            RadioButton radioButton = (RadioButton) payment_methods.findViewById(payment_methods.getCheckedRadioButtonId());
            selectedText = (String) radioButton.getText();
        }
        return isError;
    }
}