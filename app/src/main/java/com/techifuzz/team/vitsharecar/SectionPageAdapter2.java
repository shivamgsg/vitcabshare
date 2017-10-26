package com.techifuzz.team.vitsharecar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by Mridul on 23-10-2017.
 */

public class SectionPageAdapter2 extends FragmentPagerAdapter {

    public SectionPageAdapter2(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        switch (position){
            case 0:
                Edittravelrequest request=new Edittravelrequest();
                return request;
            case 1:
                Showall sentrequest=new Showall();
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
                return "Offer Ride";
            case 1:
                return "Find Ride";
            default:
                return null;
        }
    }
}

