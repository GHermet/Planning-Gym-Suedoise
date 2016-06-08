package com.hermax_lab.www.planninggymsuedoise.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.hermax_lab.www.planninggymsuedoise.Fragments.CoursFragment;
import com.hermax_lab.www.planninggymsuedoise.Fragments.DashboardFragment;
import com.hermax_lab.www.planninggymsuedoise.Fragments.SallesFragment;
import com.hermax_lab.www.planninggymsuedoise.Fragments.StaffFragment;
import com.hermax_lab.www.planninggymsuedoise.Fragments.StatFragment;

/**
 * Created by Guillaume on 5/11/16.
 */
public class ViewPagerAdapter extends FragmentPagerAdapter {

    public ViewPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    // Returns total number of pages
    @Override
    public int getCount() {
        int NUM_ITEMS = 5;
        return NUM_ITEMS;
    }


    // Returns the fragment to display for that page
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0: // Fragment # 0 - This will show FirstFragment
                return DashboardFragment.newInstance();

            case 1: // Fragment # 0 - This will show FirstFragment different title
                return StatFragment.newInstance();

            case 2: // Fragment # 1 - This will show SecondFragmentStaffFragment.newInstance();
                return SallesFragment.newInstance();

            case 3:
                return CoursFragment.newInstance();

            case 4:
                return StaffFragment.newInstance();

            default:
                return StaffFragment.newInstance();
        }
    }

    // Returns the page title for the top indicator
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position % 5) {
            case 0:
                return "Dashboard";
            case 1:
                return "Statistiques";
            case 2:
                return "Salles";
            case 3:
                return "Cours";
            case 4:
                return "Staff";
        }
        return "";
    }


    }
