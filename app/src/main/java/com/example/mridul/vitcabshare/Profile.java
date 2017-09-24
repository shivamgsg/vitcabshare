package com.example.mridul.vitcabshare;

import android.icu.text.DateFormat;
import android.icu.text.StringPrepParseException;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.gcm.TaskParams;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Profile extends AppCompatActivity {

    private TextView textView;
    private ImageView imageView;
    private Button button;
    private DatabaseReference muserdatabase;
    private String mcurrent_state;
    private DatabaseReference msendreq;
    private DatabaseReference mreceivedreq;
    private DatabaseReference notification;
    private DatabaseReference mRootRef;
    private FirebaseUser mcurrent_user;
    private DatabaseReference mshow;
    private DatabaseReference nshow;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        final String user_id = getIntent().getStringExtra("user_id");


        mRootRef = FirebaseDatabase.getInstance().getReference();
        muserdatabase = FirebaseDatabase.getInstance().getReference().child("travel").child(user_id);
        msendreq = FirebaseDatabase.getInstance().getReference().child("Request");
        mreceivedreq = FirebaseDatabase.getInstance().getReference().child("Accept");
        notification = FirebaseDatabase.getInstance().getReference().child("Notification");
        mshow=FirebaseDatabase.getInstance().getReference().child("show");
        nshow=FirebaseDatabase.getInstance().getReference().child("travel");


        mcurrent_user = FirebaseAuth.getInstance().getCurrentUser();


        textView = (TextView) findViewById(R.id.name_profile);
        imageView = (ImageView) findViewById(R.id.pic);
        button = (Button) findViewById(R.id.button_send_req);


        mcurrent_state = "not_send";


        muserdatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String display = dataSnapshot.child("name").getValue().toString();
                String image = dataSnapshot.child("image").getValue().toString();

                textView.setText(display);
                Picasso.with(Profile.this).load(image).placeholder(R.drawable.cool).into(imageView);

                msendreq.child(mcurrent_user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.hasChild(user_id)) {

                            String req_type = dataSnapshot.child(user_id).child("request_type").getValue().toString();

                            if (req_type.equals("received")) {

                                mcurrent_state = "req_received";
                                button.setText("Accept Request");

                            } else if (req_type.equals("sent")) {

                                mcurrent_state = "req_sent";
                                button.setText("Cancel Request");

                            }
                        } else {
                            mreceivedreq.child(mcurrent_user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.hasChild(user_id)) {
//                                        String hell=dataSnapshot.child(user_id).child("h").getValue().toString();
//                                        if(hell.equals("hello")){
                                        mcurrent_state = "accept";
                                        button.setText("Request Accepted");
                                        button.setEnabled(false);
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button.setEnabled(false);
                if (mcurrent_state.equals("not_send")) {

                    DatabaseReference mnotification = mRootRef.child("Notification").child(user_id).push();
                    String notification = mnotification.getKey();

                    nshow.child(mcurrent_user.getUid()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String display = dataSnapshot.child("name").getValue().toString();
                            String image = dataSnapshot.child("image").getValue().toString();

                            HashMap<String, String> show = new HashMap<String, String>();
                            show.put("name", display);
                            show.put("image", image);
                            mshow.child(user_id).child(mcurrent_user.getUid()).setValue(show);
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                    HashMap<String, String> notificationdata = new HashMap<String, String>();
                    notificationdata.put("from", mcurrent_user.getUid());
                    notificationdata.put("type", "default");

                    Map requestMap = new HashMap();
                    requestMap.put("Request/" + mcurrent_user.getUid() + "/" + user_id + "/request_type", "sent");
                    requestMap.put("Request/" + user_id + "/" + mcurrent_user.getUid() + "/request_type", "received");
                    requestMap.put("Notification/" + user_id + "/" + notification, notificationdata);

                    mRootRef.updateChildren(requestMap, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                            if (databaseError != null) {
                                Toast.makeText(Profile.this, "Error", Toast.LENGTH_LONG).show();
                            } else {
                                mcurrent_state = "req_sent";
                                button.setText("Cancel Request");
                            }
                            button.setEnabled(true);
                        }

                    });
                }
                if (mcurrent_state.equals("req_sent")) {
                    msendreq.child(mcurrent_user.getUid()).child(user_id).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            msendreq.child(user_id).child(mcurrent_user.getUid()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    mshow.child(user_id).child(mcurrent_user.getUid()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            button.setEnabled(true);
                                            mcurrent_state = "not_send";
                                            button.setText("Send Request");
                                        }
                                    });


                                }
                            });

                        }
                    });

                }


                if (mcurrent_state.equals("req_received")) {


                    Map friendsMap = new HashMap();
                    friendsMap.put("Accept/" + mcurrent_user.getUid() + "/" + user_id + "/h", "hello");
                    friendsMap.put("Accept/" + user_id + "/" + mcurrent_user.getUid() + "/h", "hello");


                    friendsMap.put("Request/" + mcurrent_user.getUid() + "/" + user_id, null);
                    friendsMap.put("Request/" + user_id + "/" + mcurrent_user.getUid(), null);

                    mRootRef.updateChildren(friendsMap, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                            if (databaseError == null) {
                                button.setEnabled(false);
                                mcurrent_state = "accept";
                                button.setText("Request Accepted");
                            } else {
                                String error = databaseError.getMessage();
                                Toast.makeText(Profile.this, error, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }
}
