package com.techifuzz.team.vitsharecar;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        mA= new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser()==null){
                    Intent lofin=new Intent(MainActivity.this,StartActivity.class);
                    lofin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(lofin);
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
                transaction.replace(R.id.frame_layout, account); // give your fragment container id in first parameter
                transaction.commit();
            }
        });
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        textView=(TextView) navigationView.getHeaderView(0).findViewById(R.id.username);
        circleImageView1 = (CircleImageView) navigationView.getHeaderView(0).findViewById(R.id.circleImageView4);
        if(firebaseAuth.getCurrentUser()!=null) {
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
        drawerLayout.addDrawerListener(drawerToggle);
        setupDrawerToggle();
        setupDrawerContent(navigationView);
    }


    @Override
    public void onStart() {
        super.onStart();
//        firebaseAuth.addAuthStateListener(mA);
        if (firebaseAuth.getCurrentUser() == null) {
            sendToStart();
        }
    }


    @Override
    public void onBackPressed() {
        DrawerLayout layout = (DrawerLayout)findViewById(R.id.drawer_layout);
        if (layout.isDrawerOpen(GravityCompat.START)) {
            layout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

//        @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        super.onCreateOptionsMenu(menu);
//        getMenuInflater().inflate(R.menu.layout_menu,menu);
//        return true;
//    }

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
                        selectDrawerItem(item);
                        return true;
                    }
                }
        );
    }

    private void selectDrawerItem(MenuItem item) {
        Fragment fragment = null;
        Class fragmentclass;
        switch (item.getItemId()) {
            case R.id.edit_travel:
                fragmentclass = Edittravelrequest.class;
                break;
            case R.id.request:
                fragmentclass = Request.class;
                break;
            case R.id.show_all:
                fragmentclass = Showall.class;
                break;
            case R.id.review:
                fragmentclass = Feedback.class;
                break;
            case R.id.account:
                fragmentclass = Account.class;
                break;
            case R.id.aboutus:
                fragmentclass=Aboutus.class;
                break;
            case R.id.privacy:
                fragmentclass=Privacy.class;
                break;
            case R.id.opensou:
                fragmentclass=Opensource.class;
                break;
            case R.id.Logout:
                logout();



            default:
                fragmentclass = Edittravelrequest.class;
                break;

        }
        try {
            fragment = (Fragment) fragmentclass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
//        FragmentManager fragmentmanager = getSupportFragmentManager();
        FragmentTransaction tx=getSupportFragmentManager().beginTransaction();
        tx.replace(R.id.frame_layout, fragment).commit();

        item.setChecked(true);
        setTitle(item.getTitle());
        drawerLayout.closeDrawer(GravityCompat.START);
    }

    private void logout() {
        if (mA != null) {
            firebaseAuth.removeAuthStateListener(mA);
            firebaseAuth.signOut();

            Intent startIntent1 = new Intent(MainActivity.this, StartActivity.class);
            startIntent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
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