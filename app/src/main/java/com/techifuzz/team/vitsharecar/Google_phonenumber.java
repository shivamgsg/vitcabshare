package com.techifuzz.team.vitsharecar;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.security.AuthProvider;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class Google_phonenumber extends AppCompatActivity {

    private Button button,getButton;
    private DatabaseReference databaseReference;
    private ProgressDialog progressDialog;
    private TextInputLayout inputLayout;
    private TextInputEditText textInputEditText;
    private EditText editText;
    private ProgressBar progressBar;
    private String verificationid;
    private PhoneAuthProvider.ForceResendingToken mforceResendingToken;
    private ImageView imageView;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener mA;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_phonenumber);
//        Display display = getWindowManager().getDefaultDisplay();
//        int width = display.getWidth();
//        int height = display.getHeight();
        firebaseAuth = FirebaseAuth.getInstance();
        mA = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null) {
                    Intent login = new Intent(Google_phonenumber.this, Main3Activity.class);
                    login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(login);
                    finish();
                }
            }
        };
        inputLayout=(TextInputLayout) findViewById(R.id.textInputLayoutnumber12);
        textInputEditText=(TextInputEditText) findViewById(R.id.textInputEditTextnumber12);
        textInputEditText.setCompoundDrawablesWithIntrinsicBounds(R.drawable.dial, 0, 0, 0);
        editText=(EditText) findViewById(R.id.otp);
        progressBar=(ProgressBar) findViewById(R.id.progress_bar_otp);
        imageView=(ImageView) findViewById(R.id.lock);
        button = (Button) findViewById(R.id.button_number);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
                String uid = current_user.getUid();
                final String numberw = editText.getText().toString();
                final String num=textInputEditText.getText().toString();
                PhoneAuthCredential credential=PhoneAuthProvider.getCredential(verificationid,numberw);
                if (credential.getSmsCode()==numberw) {
                    databaseReference = FirebaseDatabase.getInstance().getReference().child("user").child(uid);
                    Map data = new HashMap();
                    data.put("number",num);
                    databaseReference.updateChildren(data).addOnSuccessListener(new OnSuccessListener() {
                        @Override
                        public void onSuccess(Object o) {
                            Toast.makeText(Google_phonenumber.this, "Successfully Signed In", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(Google_phonenumber.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    });
                } else {
                    Toast.makeText(Google_phonenumber.this, "Enter the correct OTP", Toast.LENGTH_SHORT).show();
                }
            }
        });
        getButton=(Button) findViewById(R.id.button_number_otp);
        getButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
                final String uid = current_user.getUid();
                final String number = textInputEditText.getText().toString();
                textInputEditText.setEnabled(false);
                if (number.isEmpty() || number.length() != 10) {
                    inputLayout.setError("Check the number");
                } else {
                    PhoneAuthProvider.getInstance().verifyPhoneNumber(number, 60, TimeUnit.SECONDS, Google_phonenumber.this, new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                        @Override
                        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                            databaseReference = FirebaseDatabase.getInstance().getReference().child("user").child(uid);
                            Map data = new HashMap();
                            data.put("number",number);
                            databaseReference.updateChildren(data).addOnSuccessListener(new OnSuccessListener() {
                                @Override
                                public void onSuccess(Object o) {
                                    Toast.makeText(Google_phonenumber.this, "Successfully Signed In", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(Google_phonenumber.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            });

                        }

                        @Override
                        public void onVerificationFailed(FirebaseException e) {
                            Toast.makeText(Google_phonenumber.this, "Verification Failed", Toast.LENGTH_SHORT).show();
                            textInputEditText.setEnabled(true);

                        }

                        @Override
                        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {

                            verificationid=s;
                            mforceResendingToken=forceResendingToken;
                            progressBar.setVisibility(View.VISIBLE);
                            editText.setVisibility(View.VISIBLE);
                            getButton.setVisibility(View.INVISIBLE);
                            button.setVisibility(View.VISIBLE);
                            imageView.setVisibility(View.VISIBLE);
                        }
                    });
                }

            }
        });

    }
//    @Override
//    protected void onStop() {
//        firebaseAuth.removeAuthStateListener(mA);
//        firebaseAuth.signOut();
//        super.onStop();
//
//    }

    @Override
    public void onBackPressed() {
    }

//    @Override
//    protected void onDestroy() {
//        firebaseAuth.removeAuthStateListener(mA);
//        firebaseAuth.signOut();
//        super.onDestroy();
//
//    }
}
