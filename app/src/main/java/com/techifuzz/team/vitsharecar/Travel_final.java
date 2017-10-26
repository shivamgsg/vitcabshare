package com.techifuzz.team.vitsharecar;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class Travel_final extends Fragment {
    private Toolbar toolbar;
    private ViewPager viewPager;
    private SectionPageAdapter2 sectionPagerAdapter;
    private TabLayout tabLayout;
    private Fragment fragment;



    public Travel_final() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_travel_final, container, false);
//        toolbar=(Toolbar) view.findViewById(R.id.toolbar_travel);
//        view.setSupportActionBar(toolbar);
//        getSupportActionBar().setTitle("Travel");
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
        viewPager=(ViewPager) view.findViewById(R.id.viewpager_request1);
        sectionPagerAdapter=new SectionPageAdapter2(getChildFragmentManager());
        viewPager.setOffscreenPageLimit(1);
        viewPager.setAdapter(sectionPagerAdapter);
        tabLayout=(TabLayout) view.findViewById(R.id.tab_layout3);
        tabLayout.setupWithViewPager(viewPager);
        return view;
    }

}
