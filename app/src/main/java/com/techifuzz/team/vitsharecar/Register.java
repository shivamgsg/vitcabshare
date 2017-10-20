package com.techifuzz.team.vitsharecar;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;

public class Register extends AppCompatActivity {

    private RelativeLayout nestedScrollView;
    private FirebaseAuth mAuth;
    private TextInputLayout textInputLayoutName;
    private TextInputLayout textInputLayoutEmail;
    private TextInputLayout textInputLayoutPassword;
    private TextInputLayout textInputLayoutNumber;

    private TextInputEditText textInputEditTextName;
    private TextInputEditText textInputEditTextEmail;
    private TextInputEditText textInputEditTextPassword;
    private TextInputEditText textInputEditTextNumber;

    private AppCompatButton appCompatButtonRegister;
    private AppCompatTextView appCompatTextViewLoginLink;
    private ProgressDialog progressDialog;
    private DatabaseReference mdatabase;
    private DatabaseReference databaseReference;
    private DatabaseReference mDatebase;
    private DatabaseReference getMdatabase;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        nestedScrollView = (RelativeLayout) findViewById(R.id.nestedScrollView);
        textInputLayoutName = (TextInputLayout) findViewById(R.id.textInputLayoutName);
        textInputLayoutEmail = (TextInputLayout) findViewById(R.id.textInputLayoutEmail);
        textInputLayoutPassword = (TextInputLayout) findViewById(R.id.textInputLayoutPassword);
        mAuth=FirebaseAuth.getInstance();

        textInputEditTextName = (TextInputEditText) findViewById(R.id.textInputEditTextName);
        textInputEditTextEmail = (TextInputEditText) findViewById(R.id.textInputEditTextEmail);
        textInputEditTextPassword = (TextInputEditText) findViewById(R.id.textInputEditTextPassword);


        appCompatButtonRegister = (AppCompatButton) findViewById(R.id.button_register);
        Display display = getWindowManager().getDefaultDisplay();
        int width = display.getWidth();
        int height = display.getHeight();
        LinearLayoutCompat myl = (LinearLayoutCompat) findViewById(R.id.linear_reg);
        myl.setPadding(width / 6, height / 22, width / 6, height / 25);

        progressDialog = new ProgressDialog(this);


        appCompatButtonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 String name = textInputEditTextName.getText().toString();
                 String email = textInputEditTextEmail.getText().toString();
                 String password = textInputEditTextPassword.getText().toString();

                if (name.isEmpty()) {
                    textInputLayoutName.setError("Name cannot be empty");
                } else if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    textInputLayoutEmail.setError("Check your email");
                } else if (password.isEmpty() || password.length() < 6) {
                    textInputLayoutPassword.setError("Minimum length should be 6");
                } else {
                    textInputLayoutName.setErrorEnabled(false);
                    textInputLayoutPassword.setErrorEnabled(false);
                    textInputLayoutEmail.setErrorEnabled(false);
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(textInputEditTextPassword.getWindowToken(), 0);
                    progressDialog.setTitle("Registering User......");
                    progressDialog.setMessage("Please wait while we Create your account");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();
                    register_user(name,email,password);
                }
            }
        });
    }



    private void register_user(final String name, final String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            FirebaseUser current_user= FirebaseAuth.getInstance().getCurrentUser();
                            String uid=current_user.getUid();

                            mdatabase=FirebaseDatabase.getInstance().getReference().child("user").child(uid);

                            String device_token= FirebaseInstanceId.getInstance().getToken();

                            HashMap<String,String> datamap=new HashMap<>();
                            datamap.put("name",name);
                            datamap.put("email",email);
                            datamap.put("image","default");
                            datamap.put("thumb_image","default");
                            datamap.put("token",device_token);
                            datamap.put("number","default");

                            mdatabase.setValue(datamap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if (task.isSuccessful())
                                    {
                                        progressDialog.dismiss();

                                        Intent mainintent=new Intent(Register.this,Google_phonenumber.class);
                                        mainintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(mainintent);

                                        finish();
                                    }
                                }
                            });
                        }
                        else
                        {
                            progressDialog.hide();
                            Toast.makeText(Register.this,"You are already registered",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
