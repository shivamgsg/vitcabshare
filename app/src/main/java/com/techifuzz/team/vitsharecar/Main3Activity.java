package com.techifuzz.team.vitsharecar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
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

public class Main3Activity extends AppCompatActivity {
    private SignInButton signInButton;
    private GoogleApiClient mGoogleApiClient;
    private FirebaseAuth.AuthStateListener authStateListener;
    private static final int RC_SIGN_IN = 2;
    private static final String TAG = "LOGIN_ACTIVITY";
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;
    private DatabaseReference getMdatabase;
    private Button button,button1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        mAuth=FirebaseAuth.getInstance();
        authStateListener= new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user=firebaseAuth.getCurrentUser();
                if(user!=null){
                    Intent intent=new Intent(Main3Activity.this,MainActivity.class);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(Main3Activity.this,"Hello",Toast.LENGTH_SHORT).show();
                }
            }

        };
        signInButton=(SignInButton) findViewById(R.id.googlebutton);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleApiClient=new GoogleApiClient.Builder(Main3Activity.this)
                .enableAutoManage(Main3Activity.this,new  GoogleApiClient.OnConnectionFailedListener(){
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Toast.makeText(Main3Activity.this,"Error",Toast.LENGTH_SHORT).show();

                    }
                }).addApi(Auth.GOOGLE_SIGN_IN_API,gso).build();


        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mGoogleApiClient.clearDefaultAccountAndReconnect();
                signIn();
            }
        });
        button=(Button) findViewById(R.id.signup_byn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent reg_intent=new Intent(Main3Activity.this,Register.class);
                startActivity(reg_intent);
            }
        });
        button1=(Button) findViewById(R.id.signin_byn);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent reg_intent=new Intent(Main3Activity.this,Login.class);
                startActivity(reg_intent);

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
                            getMdatabase= FirebaseDatabase.getInstance().getReference().child("user").child(uid);
                            getMdatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if(dataSnapshot.exists()){
                                        String numb=dataSnapshot.child("number").getValue().toString();
                                        if(numb.equals("default")){
                                            progressDialog.dismiss();
                                            startActivity(new Intent(Main3Activity.this,Google_phonenumber.class));
                                        }else
                                        {
                                            progressDialog.dismiss();
                                            startActivity(new Intent(Main3Activity.this,MainActivity.class));

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

                                        startActivity(new Intent(Main3Activity.this,Google_phonenumber.class));

                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(Main3Activity.this, "Authentication failed.",
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
                progressDialog=new ProgressDialog(Main3Activity.this);
                progressDialog.setTitle("Registering User......");
                progressDialog.setMessage("Please wait while we Create your account");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();

                firebaseAuthWithGoogle(account);

            } else {
                // Google Sign In failed, update UI appropriately
                // ...
                Toast.makeText(Main3Activity.this,"There was a trouble signing in-Please try again",Toast.LENGTH_SHORT).show();;
            }
        }
    }
}
