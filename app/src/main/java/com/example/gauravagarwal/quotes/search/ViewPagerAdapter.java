package com.example.gauravagarwal.quotes.search;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by Gaurav Agarwal on 19-02-2017.
 */
public class ViewPagerAdapter extends FragmentPagerAdapter {
    private static String titleList[] = {"Category", "Author"};
    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if(position==0)    return new CatFragment();
        if(position==1)    return new AuthorFragment();
        return null;
    }
    @Override
    public CharSequence getPageTitle(int position) {
        return titleList[position];
    }

    @Override
    public int getCount() {
        return titleList.length;
    }
}
