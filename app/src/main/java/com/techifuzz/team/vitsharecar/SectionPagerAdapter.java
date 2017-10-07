package com.techifuzz.team.vitsharecar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by Mridul on 05-10-2017.
 */

public class SectionPagerAdapter extends FragmentPagerAdapter {

    public SectionPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        switch (position){
            case 0:
                Request request=new Request();
                return request;
            case 1:
                Sentrequest sentrequest=new Sentrequest();
                return sentrequest;

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    public CharSequence getPageTitle(int pos){
        switch (pos){
            case 0:
                return "Received Request";
            case 1:
                return "Sent Request";
            default:
                return null;
        }
    }
}
