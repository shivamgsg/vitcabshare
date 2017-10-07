package com.techifuzz.team.vitsharecar;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
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

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
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

    private SignInButton signInButton;
    private GoogleApiClient mGoogleApiClient;
    private FirebaseAuth.AuthStateListener authStateListener;
    private static final int RC_SIGN_IN = 1;
    private static final String TAG = "LOGIN_ACTIVITY";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        nestedScrollView = (RelativeLayout) findViewById(R.id.nestedScrollView);
        mAuth=FirebaseAuth.getInstance();
        authStateListener= new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user=firebaseAuth.getCurrentUser();
                if(user!=null){
                    Intent intent=new Intent(Register.this,MainActivity.class);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(Register.this,"Hello",Toast.LENGTH_SHORT).show();
                }
            }

        };
        textInputLayoutName = (TextInputLayout) findViewById(R.id.textInputLayoutName);
        textInputLayoutEmail = (TextInputLayout) findViewById(R.id.textInputLayoutEmail);
        textInputLayoutPassword = (TextInputLayout) findViewById(R.id.textInputLayoutPassword);
        textInputLayoutNumber=(TextInputLayout) findViewById(R.id.textInputLayoutnumber);



        textInputEditTextName = (TextInputEditText) findViewById(R.id.textInputEditTextName);
        textInputEditTextEmail = (TextInputEditText) findViewById(R.id.textInputEditTextEmail);
        textInputEditTextPassword = (TextInputEditText) findViewById(R.id.textInputEditTextPassword);
        textInputEditTextNumber=(TextInputEditText) findViewById(R.id.textInputEditTextnumber);

        appCompatButtonRegister = (AppCompatButton) findViewById(R.id.button_register);
        Display display = getWindowManager().getDefaultDisplay();
        int width = display.getWidth();
        int height = display.getHeight();
        LinearLayoutCompat myl=(LinearLayoutCompat) findViewById(R.id.linear_reg);
        myl.setPadding(width/6,height/22,width/6,height/25);
        SignInButton ta=(SignInButton) findViewById(R.id.google_button);
        ta.setPadding(width/50,height/40,width/50,0);
        progressDialog=new ProgressDialog(this);


        appCompatButtonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name=textInputEditTextName.getText().toString();
                final String email=textInputEditTextEmail.getText().toString();
                final String password=textInputEditTextPassword.getText().toString();
                final String number=textInputEditTextNumber.getText().toString();
                if(name.isEmpty())
                {
                    textInputLayoutName.setError("Name cannot be empty");
                }
                else if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    textInputLayoutEmail.setError("Check your email");
                }
                else if(password.isEmpty() || password.length()<6){
                    textInputLayoutPassword.setError("Minimum length should be 6");
                }
                else if(number.isEmpty() || number.length()!=10 ){
                    textInputLayoutNumber.setError("Check your number");
                }
                else{
                    textInputLayoutName.setErrorEnabled(false);
                    textInputLayoutPassword.setErrorEnabled(false);
                    textInputLayoutEmail.setErrorEnabled(false);
                    textInputLayoutNumber.setErrorEnabled(false);
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(textInputEditTextPassword.getWindowToken(), 0);
                    final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Register.this);
                    alertDialogBuilder.setMessage("Do you want to continue with this number"+" "+number+" ?");
                            alertDialogBuilder.setPositiveButton("yes",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface arg0, int arg1) {
                                            progressDialog.setTitle("Registering User......");
                                            progressDialog.setMessage("Please wait while we Create your account");
                                            progressDialog.setCanceledOnTouchOutside(false);
                                            progressDialog.show();
                                            register_user(name,email,password,number);
                                        }
                                    });
                    alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
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
        });
//        appCompatTextViewLoginLink.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent1= new Intent(Register.this,Login.class);
//                startActivity(intent1);
//            }
//        });
        //-----google sign in

        signInButton=(SignInButton) findViewById(R.id.google_button);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleApiClient=new GoogleApiClient.Builder(Register.this)
                .enableAutoManage(Register.this,new  GoogleApiClient.OnConnectionFailedListener(){
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Toast.makeText(Register.this,"Error",Toast.LENGTH_SHORT).show();

                    }
                }).addApi(Auth.GOOGLE_SIGN_IN_API,gso).build();


        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mGoogleApiClient.clearDefaultAccountAndReconnect();
                signIn();
            }
        });
    }
    @Override
    public void onStop() {
        super.onStop();
        if (authStateListener != null) {
            mAuth.removeAuthStateListener(authStateListener);
        }
    }
    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    private void firebaseAuthWithGoogle(final GoogleSignInAccount account) {

        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            final String name=account.getDisplayName();
                            final String email=account.getEmail();
                            final String image=account.getPhotoUrl().toString();
                            final String device_token= FirebaseInstanceId.getInstance().getToken();
                            FirebaseUser current_user= FirebaseAuth.getInstance().getCurrentUser();
                            String uid=current_user.getUid();
                            getMdatabase=FirebaseDatabase.getInstance().getReference().child("user").child(uid);
                            getMdatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if(dataSnapshot.exists()){
                                        String numb=dataSnapshot.child("number").getValue().toString();
                                        if(numb.equals("default")){
                                            progressDialog.dismiss();
                                            startActivity(new Intent(Register.this,Google_phonenumber.class));
                                        }else
                                        {
                                            progressDialog.dismiss();
                                            startActivity(new Intent(Register.this,MainActivity.class));

                                        }

                                    }
                                    else{
                                        HashMap<String,String> hello=new HashMap<String, String>();
                                        hello.put("name",name);
                                        hello.put("email",email);
                                        hello.put("image",image);
                                        hello.put("thumb_image","default");
                                        hello.put("token",device_token);
                                        hello.put("number","default");
                                        getMdatabase.setValue(hello);
                                        progressDialog.dismiss();

                                        startActivity(new Intent(Register.this,Google_phonenumber.class));

                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(Register.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                progressDialog.setTitle("Registering User......");
                progressDialog.setMessage("Please wait while we Create your account");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();

                firebaseAuthWithGoogle(account);

            } else {
                // Google Sign In failed, update UI appropriately
                // ...
                Toast.makeText(Register.this,"There was a trouble signing in-Please try again",Toast.LENGTH_SHORT).show();;
            }
        }
    }


    private void register_user(final String name, final String email, String password, final String number) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            FirebaseUser current_user= FirebaseAuth.getInstance().getCurrentUser();
                            String uid=current_user.getUid();

                            mdatabase=FirebaseDatabase.getInstance().getReference().child("user").child(uid);

                            String device_token= FirebaseInstanceId.getInstance().getToken();

                            HashMap<String,String> datamap=new HashMap<String, String>();
                            datamap.put("name",name);
                            datamap.put("email",email);
                            datamap.put("image","default");
                            datamap.put("thumb_image","default");
                            datamap.put("token",device_token);
                            datamap.put("number",number);

                            mdatabase.setValue(datamap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if (task.isSuccessful())
                                    {
                                        progressDialog.dismiss();
                                        Intent mainintent=new Intent(Register.this,MainActivity.class);
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
