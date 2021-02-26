package com.example.cleaningconsultancy.ui.support;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.cleaningconsultancy.MainActivity;
import com.example.cleaningconsultancy.R;
import com.example.cleaningconsultancy.ThankYouActivity;
import com.example.cleaningconsultancy.ui.login.LoginActivity;
import com.example.cleaningconsultancy.ui.search.SearchFragment;
import com.example.cleaningconsultancy.ui.support.SupportViewModel;
import com.google.firebase.auth.FirebaseAuth;

public class SupportFragment extends Fragment {

    EditText fullName, email, message;
    Button contactUs;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_support, container, false);
        fullName = root.findViewById(R.id.fullName);
        email = root.findViewById(R.id.email);
        message = root.findViewById(R.id.message);
        contactUs = root.findViewById(R.id.contactUs);

        contactUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isError = false;
                if(fullName.getText().toString().length() ==0)
                {
                    fullName.requestFocus();
                    fullName.setError("Name can not be empty.");
                    isError = true;
                }
                if(email.getText().toString().length() ==0)
                {
                    email.requestFocus();
                    email.setError("Name can not be empty.");
                    isError = true;
                }
                if(message.getText().toString().length() ==0)
                {
                    message.requestFocus();
                    message.setError("Name can not be empty.");
                    isError = true;
                }
                if(!isError) {
                    new AlertDialog.Builder(getContext())
                            .setTitle("Acknowledgement")
                            .setMessage("We will contact you soon.")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(getActivity(), MainActivity.class);
                                    startActivity(intent);
                                }
                            })
                            .setNegativeButton(android.R.string.no, null)
                            .show();
                }
            }
        });

        return root;
    }
}