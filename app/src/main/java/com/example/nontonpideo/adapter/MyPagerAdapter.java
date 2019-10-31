package com.example.nontonpideo.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.nontonpideo.activity.MovieFragment;
import com.example.nontonpideo.activity.TVShowFragment;

import static com.example.nontonpideo.activity.MainActivity.MOVIE_INDEX;
import static com.example.nontonpideo.activity.MainActivity.TV_SHOW_INDEX;

public class MyPagerAdapter extends FragmentPagerAdapter {

    private static final int NUMBER_OF_TABS = 2;

    public MyPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        if (i == MOVIE_INDEX) {
            return new MovieFragment();
        } else if (i == TV_SHOW_INDEX) {
            return new TVShowFragment();
        }
        return null;
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }

    @Override
    public int getCount() {
        return NUMBER_OF_TABS;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0) {
            return "Movies";
        } else if (position == 1) {
            return "TV Shows";
        }
        return "Error";
    }
}
