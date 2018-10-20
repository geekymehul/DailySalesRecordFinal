package com.example.gargc.dailysalesrecord.Reports;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class ViewPagerAdapter extends FragmentPagerAdapter
{
    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        Fragment fragment = null;
        if (position == 0)
        {
            fragment = new LineChartFragment();
        }
        else if (position == 1)
        {
            fragment = new BarGraphFragment();
        }
        else if (position == 2)
        {
            fragment = new PieChartFragment();
        }
        return fragment;

    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {

        String title = null;
        if (position == 0)
        {
            title = "Line Chart";
        }
        else if (position == 1)
        {
            title = "Bar Graph";
        }
        else if (position == 2)
        {
            title = "Pie Chart";
        }
        return title;

    }

}
