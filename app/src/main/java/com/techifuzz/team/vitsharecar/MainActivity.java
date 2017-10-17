package com.techifuzz.team.vitsharecar;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private Toolbar toolbar;
    public NavigationView navigationView;
    private ActionBarDrawerToggle drawerToggle;
    private DrawerLayout drawerLayout;
    private DatabaseReference databaseReference;
    private TextView textView;
    private CircleImageView circleImageView1;
    private FirebaseAuth.AuthStateListener mA;
    private ImageButton imageButton;
    private DatabaseReference mUserRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new Edittravelrequest()).commit();
        }
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        mA= new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser()==null){
                    Intent login=new Intent(MainActivity.this,StartActivity.class);
                    login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(login);
                    finish();
                }
            }
        };

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        imageButton=(ImageButton) findViewById(R.id.pinButton);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment account = new Account();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_layout, account);
                toolbar.setTitle("Account");
                transaction.commit();
            }
        });
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        textView=(TextView) navigationView.getHeaderView(0).findViewById(R.id.username);
        circleImageView1 = (CircleImageView) navigationView.getHeaderView(0).findViewById(R.id.circleImageView4);
        if(firebaseAuth.getCurrentUser()!=null) {
            mUserRef=FirebaseDatabase.getInstance().getReference().child("user").child(firebaseAuth.getCurrentUser().getUid());
            databaseReference = FirebaseDatabase.getInstance().getReference().child("user").child(firebaseUser.getUid());
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    textView.setText(dataSnapshot.child("name").getValue().toString());
                    String link = dataSnapshot.child("image").getValue().toString();
                    Picasso.with(getBaseContext()).load(link).placeholder(R.drawable.cool).into(circleImageView1);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }


       checknetconnection();
        drawerLayout.addDrawerListener(drawerToggle);
        setupDrawerToggle();
        setupDrawerContent(navigationView);
    }

    private void checknetconnection() {
        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            connected = true;
        }
        else {
            connected = false;
            Toast.makeText(MainActivity.this,"Internet not working",Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onStart() {
        super.onStart();
//        firebaseAuth.addAuthStateListener(mA);
        if (firebaseAuth.getCurrentUser() == null) {
            sendToStart();
        }
        else
        {
            mUserRef.child("online").setValue("true");
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        if(currentUser != null) {

            mUserRef.child("online").setValue(ServerValue.TIMESTAMP);

        }

    }

    @Override
    public void onBackPressed() {
        DrawerLayout layout = (DrawerLayout)findViewById(R.id.drawer_layout);
        if (layout.isDrawerOpen(GravityCompat.START)) {
            layout.closeDrawer(GravityCompat.START);
        } else {
            final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
            alertDialogBuilder.setMessage("Are you sure you want to exit the app?");
            alertDialogBuilder.setPositiveButton("yes",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            finishAffinity();
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
    public void setupDrawerToggle() {
        drawerToggle = new android.support.v7.app.ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.app_name, R.string.app_name);
        drawerToggle.syncState();

    }
    private void sendToStart() {
        Intent startIntent = new Intent(MainActivity.this, StartActivity.class);
        startActivity(startIntent);
        finish();

    }

    public void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem item) {
                        selectDrawerItems(item);
                        return true;
                    }
                }
        );
    }
public boolean selectDrawerItems(MenuItem item){
    int id = item.getItemId();

    if (id == R.id.edit_travel) {
        Edittravelrequest edittravelrequest=new Edittravelrequest();
        FragmentManager manager=getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.frame_layout,edittravelrequest,edittravelrequest.getTag()).commit();

    } else if (id == R.id.request) {
        Intent intent = new Intent(MainActivity.this, Requestfinal.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);

    } else if (id == R.id.chat) {
        Chatting chatting=new Chatting();
        FragmentManager manager=getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.frame_layout,chatting,chatting.getTag()).commit();

    } else if (id == R.id.show_all) {
        Showall showall=new Showall();
        FragmentManager manager=getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.frame_layout,showall,showall.getTag()).commit();

    }else if (id == R.id.review) {
        Feedback feedback=new Feedback();
        FragmentManager manager=getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.frame_layout,feedback,feedback.getTag()).commit();

    }else if (id == R.id.account) {
        Account account=new Account();
        FragmentManager manager=getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.frame_layout,account,account.getTag()).commit();

    }else if (id == R.id.aboutus) {
        Aboutus aboutus=new Aboutus();
        FragmentManager manager=getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.frame_layout,aboutus,aboutus.getTag()).commit();

    }else if (id == R.id.privacy) {
        Privacy privacy=new Privacy();
        FragmentManager manager=getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.frame_layout,privacy,privacy.getTag()).commit();

    }else if (id == R.id.opensou) {
        Opensource opensource=new Opensource();
        FragmentManager manager=getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.frame_layout,opensource,opensource.getTag()).commit();

    }else if (id == R.id.Logout) {
        logout();

    }
//    item.setChecked(true);
    setTitle(item.getTitle());
    drawerLayout.closeDrawer(GravityCompat.START);
    return true;
}
    private void logout() {
        if (mA != null) {
            firebaseAuth.removeAuthStateListener(mA);
            firebaseAuth.signOut();
            mUserRef.child("online").setValue(ServerValue.TIMESTAMP);
            Intent startIntent1 = new Intent(MainActivity.this, StartActivity.class);
            startIntent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(startIntent1);
            finish();
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                break;

        }
        return super.onOptionsItemSelected(item);
    }

}