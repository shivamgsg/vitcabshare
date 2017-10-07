package com.techifuzz.team.vitsharecar;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

public class Requestfinal extends AppCompatActivity {
    private Toolbar toolbar;
    private ViewPager viewPager;
    private SectionPagerAdapter sectionPagerAdapter;
    private TabLayout tabLayout;
    private Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requestfinal);

        toolbar=(Toolbar) findViewById(R.id.toolbar_request);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Request");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        viewPager=(ViewPager) findViewById(R.id.viewpager_request);
        sectionPagerAdapter=new SectionPagerAdapter(getSupportFragmentManager());

        viewPager.setAdapter(sectionPagerAdapter);
        tabLayout=(TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);

    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
