package com.techifuzz.team.vitsharecar;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class Viewrequest extends AppCompatActivity {
    private DatabaseReference databaseReference,reference;
    private RecyclerView recyclerView;
    private TextView textView,getTextView,getGetTextView,view,getGetGetTextView,hello;
    private TextView getView;
    private CircleImageView circleImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewrequest);

        final FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = current_user.getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("travel").child(uid);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(!dataSnapshot.exists()){
                    RelativeLayout t=(RelativeLayout) findViewById(R.id.hello);
                    t.setVisibility(View.INVISIBLE);
                    Toast.makeText(Viewrequest.this,"No request",Toast.LENGTH_SHORT).show();

                }
                else{
                    RelativeLayout t1=(RelativeLayout) findViewById(R.id.hello2);
                    t1.setVisibility(View.INVISIBLE);
                    String name = dataSnapshot.child("name").getValue().toString();
                    String image = dataSnapshot.child("image").getValue().toString();
                    String to=dataSnapshot.child("to").getValue().toString();
                    String from=dataSnapshot.child("from").getValue().toString();
                    String date=dataSnapshot.child("date").getValue().toString();
                    String time=dataSnapshot.child("time").getValue().toString();

                    textView=(TextView) findViewById(R.id.naamereq);
                    textView.setText(name);
                    getTextView=(TextView) findViewById(R.id.from_display_req);
                    getTextView.setText(to);
                    getGetTextView=(TextView) findViewById(R.id.to_display_req);
                    getGetTextView.setText(from);
                    hello=(TextView) findViewById(R.id.date_display_req);
                    hello.setText(date);
                    view=(TextView) findViewById(R.id.time_display_req);
                    view.setText(time);
                    circleImageView=(CircleImageView) findViewById(R.id.circleImageViewreq);
                    Picasso.with(Viewrequest.this).load(image).placeholder(R.drawable.cool).into(circleImageView);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}