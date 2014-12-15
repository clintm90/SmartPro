package com.trackcell.smartpro;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.support.v13.app.FragmentPagerAdapter;

import java.util.Locale;

public class SectionsPagerAdapter extends FragmentPagerAdapter
{
    Context mContext;
    public SectionsPagerAdapter(Context context, FragmentManager fm)
    {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position)
    {
        return PlaceholderFragment.newInstance(position + 1);
    }

    @Override
    public int getCount()
    {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position)
    {
        Locale l = Locale.getDefault();
        switch (position)
        {
            case 0:
                return mContext.getString(R.string.title_section1);
            case 1:
                return mContext.getString(R.string.title_section2);
            case 2:
                return mContext.getString(R.string.title_section3);
        }
        return null;
    }
}