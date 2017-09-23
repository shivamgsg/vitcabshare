package com.example.mridul.vitcabshare;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private Toolbar toolbar;
    public NavigationView navigationView;
    private ActionBarDrawerToggle drawerToggle;
    private DrawerLayout drawerLayout;
    private DatabaseReference databaseReference;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        textView=(TextView) navigationView.getHeaderView(0).findViewById(R.id.username);
//        FirebaseUser current_user= FirebaseAuth.getInstance().getCurrentUser();
//        String uid=current_user.getUid();
//        databaseReference= FirebaseDatabase.getInstance().getReference().child("user").child(uid);
//        databaseReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                String display=dataSnapshot.child("name").getValue().toString();
//                textView.setText(display);
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });

        drawerLayout.addDrawerListener(drawerToggle);
        setupDrawerToggle();
        setupDrawerContent(navigationView);
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentuser = mAuth.getCurrentUser();
        if (currentuser == null) {
            Intent start = new Intent(MainActivity.this, StartActivity.class);
            startActivity(start);
            finish();
        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        super.onCreateOptionsMenu(menu);
//        getMenuInflater().inflate(R.menu.main_menu,menu);
//        return true;
//    }
    public void setupDrawerToggle() {
        drawerToggle = new android.support.v7.app.ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.app_name, R.string.app_name);
        drawerToggle.syncState();

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
                Intent i=new Intent(MainActivity.this,Login.class);
                finish();
                startActivity(i);

            default:
                fragmentclass = Showall.class;

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