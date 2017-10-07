package com.techifuzz.team.vitsharecar;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class Google_phonenumber extends AppCompatActivity {

    private Button button;
    private DatabaseReference databaseReference;
    private ProgressDialog progressDialog;
    private TextInputLayout inputLayout;
    private TextInputEditText textInputEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_phonenumber);
//        Display display = getWindowManager().getDefaultDisplay();
//        int width = display.getWidth();
//        int height = display.getHeight();
        inputLayout=(TextInputLayout) findViewById(R.id.textInputLayoutnumber12);
        textInputEditText=(TextInputEditText) findViewById(R.id.textInputEditTextnumber12);
        Display display = getWindowManager().getDefaultDisplay();
        int width = display.getWidth();
        int height = display.getHeight();
        inputLayout.setPadding(width/6,height/40,width/6,height/22);

        button = (Button) findViewById(R.id.button_number);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
                String uid = current_user.getUid();
                final String number = textInputEditText.getText().toString();
                if (number.isEmpty() || number.length() != 10) {
                    inputLayout.setError("Check the number");
                } else {
                    inputLayout.setErrorEnabled(false);
                    databaseReference = FirebaseDatabase.getInstance().getReference().child("user").child(uid);
                    final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Google_phonenumber.this);
                    alertDialogBuilder.setMessage("Do you want to continue with this number" + " " + number+" ?");
                    alertDialogBuilder.setPositiveButton("yes",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {
                                    Map data = new HashMap();
                                    data.put("number", number);
                                    databaseReference.updateChildren(data).addOnSuccessListener(new OnSuccessListener() {
                                        @Override
                                        public void onSuccess(Object o) {
                                            Intent intent = new Intent(Google_phonenumber.this, MainActivity.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                    });
                                }
                            });
                    alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            AlertDialog alertDialog = alertDialogBuilder.create();
                            alertDialog.dismiss();
                        }
                    });
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }
            }

            ;
        });
    }
}
