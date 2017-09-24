package com.example.mridul.vitcabshare;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Display;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

public class Login extends AppCompatActivity {

    private NestedScrollView nestedScrollView;
    private TextInputLayout textInputLayoutEmail;
    private TextInputLayout textInputLayoutPassword;
    private TextInputEditText textInputEditTextEmail;
    private TextInputEditText textInputEditTextPassword;
    private AppCompatButton appCompatButtonLogin;
    private AppCompatTextView textViewLinkRegister;
    private Toolbar toolbar;
    private ProgressDialog processdialog1;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatebase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        nestedScrollView = (NestedScrollView) findViewById(R.id.nestedScrollView);

        textInputLayoutEmail = (TextInputLayout) findViewById(R.id.textInputLayoutEmail);
        textInputLayoutPassword = (TextInputLayout) findViewById(R.id.textInputLayoutPassword);
        Display display = getWindowManager().getDefaultDisplay();
        int width = display.getWidth();
        int height = display.getHeight();
        LinearLayoutCompat myl=(LinearLayoutCompat) findViewById(R.id.linear_login);
        myl.setPadding(width/6,height/9,width/6,height/5);

        textInputEditTextEmail = (TextInputEditText) findViewById(R.id.textInputEditTextEmail);
        textInputEditTextPassword = (TextInputEditText) findViewById(R.id.textInputEditTextPassword);
        appCompatButtonLogin = (AppCompatButton) findViewById(R.id.button_sign_in);
        textViewLinkRegister = (AppCompatTextView) findViewById(R.id.button_sign_up2);
        processdialog1=new ProgressDialog(this);
        mDatebase= FirebaseDatabase.getInstance().getReference().child("travel");

        appCompatButtonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=textInputEditTextEmail.getText().toString();
                String password=textInputEditTextPassword.getText().toString();

                if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    textInputLayoutEmail.setError("Check your email");
                }
                else if(password.isEmpty()){
                    textInputLayoutPassword.setError("Cannot be blank");
                }
                else{
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(textInputEditTextPassword.getWindowToken(), 0);
                    textInputLayoutPassword.setErrorEnabled(false);
                    textInputLayoutEmail.setErrorEnabled(false);
                    processdialog1.setTitle("Logging In......");
                    processdialog1.setMessage("Please wait while we check your credentials");
                    processdialog1.setCanceledOnTouchOutside(false);
                    processdialog1.show();
                    LoginUser(email,password);
                }
            }
        });
        textViewLinkRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent main_intent = new Intent(Login.this, Register.class);
                startActivity(main_intent);
            }
        });
    }

    private void LoginUser(String email, String password) {
mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
    @Override
    public void onComplete(@NonNull Task<AuthResult> task) {
        if (task.isSuccessful()) {
            processdialog1.dismiss();
            String current_user_id=mAuth.getCurrentUser().getUid();
            String token_id= FirebaseInstanceId.getInstance().getToken();

            mDatebase.child(current_user_id).child("token").setValue(token_id).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Intent mainintent = new Intent(Login.this, MainActivity.class);
                    mainintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(mainintent);
                    finish();

                }
            });


        } else {
            processdialog1.hide();
            Toast.makeText(Login.this, "Check your email-id or password", Toast.LENGTH_SHORT).show();
        }
    }
});
    }
}
