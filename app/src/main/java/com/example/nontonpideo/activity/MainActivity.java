package com.example.nontonpideo.activity;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.example.nontonpideo.R;
import com.example.nontonpideo.adapter.MyPagerAdapter;

import java.util.Objects;

public class MainActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener {

    public static final String MY_SHARED_PREFERENCES = "MySharedPrefs";
    public static final String VIEW_MODE_KEY = "VIEW_MODE_KEY";
    public static final String HOME_MODE = "HOME_MODE";
    public static final String FAVORITES_MODE = "FAVORITES_MODE";
    public static final String NO_MODE = "NO_MODE";
    public static final String SEARCH_KEY = "SEARCH_KEY";
    public static final String EMPTY_SEARCH = "";
    public static final int MOVIE_INDEX = 0;
    public static final int TV_SHOW_INDEX = 1;
    @SuppressLint("StaticFieldLeak")
    static ProgressBar PROGRESS_BAR;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private ViewPager viewPager;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navView = initializeBottomNavigationView();
        initializeSharedPreferences();
        initializeTabs();
        initializeViews();
        showLoading();

        if (savedInstanceState == null) {
            navView.setSelectedItemId(R.id.navigation_home);
        }
    }

    private final BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @SuppressLint("ApplySharedPref")
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            showLoading();

            editor = preferences.edit();
            editor.putString(SEARCH_KEY, EMPTY_SEARCH);

            switch (item.getItemId()) {
                case R.id.navigation_home:
                    showSearchBar();

                    editor.putString(VIEW_MODE_KEY, HOME_MODE);
                    editor.commit();

                    viewPager.setCurrentItem(MOVIE_INDEX);
                    Objects.requireNonNull(viewPager.getAdapter()).notifyDataSetChanged();
                    return true;

                case R.id.navigation_favorites:
                    hideSearchBar();

                    editor.putString(VIEW_MODE_KEY, FAVORITES_MODE);
                    editor.commit();

                    viewPager.setCurrentItem(MOVIE_INDEX);
                    Objects.requireNonNull(viewPager.getAdapter()).notifyDataSetChanged();
                    return true;
            }
            return false;
        }
    };

    private void hideSearchBar(){
        if(searchView != null){
            searchView.setIconified(true);
            searchView.setVisibility(View.GONE);
        }
    }

    private void showSearchBar(){
        if(searchView != null){
            searchView.setIconified(true);
            searchView.setVisibility(View.VISIBLE);
        }
    }

    private void initializeViews() {
        PROGRESS_BAR = findViewById(R.id.main_progress_bar);
    }

    @SuppressLint("ApplySharedPref")
    private void initializeSharedPreferences() {
        preferences = getSharedPreferences(MY_SHARED_PREFERENCES, Context.MODE_PRIVATE);
        editor = preferences.edit();
        editor.putString(VIEW_MODE_KEY, HOME_MODE);
        editor.commit();
    }

    private void initializeTabs() {
        viewPager = findViewById(R.id.main_view_pager);
        TabLayout tabLayout = findViewById(R.id.tab_layout);

        MyPagerAdapter pagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.addOnTabSelectedListener(this);
    }

    private BottomNavigationView initializeBottomNavigationView() {
        BottomNavigationView navView = findViewById(R.id.navigation);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        return navView;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);

        MenuItem myActionMenuItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) myActionMenuItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @SuppressLint("ApplySharedPref")
            @Override
            public boolean onQueryTextSubmit(String query) {
                editor = preferences.edit();
                query = query.trim();
                if (TextUtils.isEmpty(query)) {
                    editor.putString(SEARCH_KEY, EMPTY_SEARCH);
                } else {
                    editor.putString(SEARCH_KEY, query);
                }
                editor.commit();
                Objects.requireNonNull(viewPager.getAdapter()).notifyDataSetChanged();
                return true;
            }

            @SuppressLint("ApplySharedPref")
            @Override
            public boolean onQueryTextChange(String newText) {
                editor = preferences.edit();
                newText = newText.trim();

                if (TextUtils.isEmpty(newText)) {
                    editor.putString(SEARCH_KEY, EMPTY_SEARCH);
                    editor.commit();
                    Objects.requireNonNull(viewPager.getAdapter()).notifyDataSetChanged();
                }

                return true;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_change_language_settings) {
            Intent mIntent = new Intent(Settings.ACTION_LOCALE_SETTINGS);
            startActivity(mIntent);
        }else if(item.getItemId() == R.id.action_change_reminder_settings){
            Intent intent = new Intent(this, ReminderSettingsActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    private static synchronized void showLoading() {
        if (PROGRESS_BAR == null)
            return;
        PROGRESS_BAR.setVisibility(View.VISIBLE);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        int pos = tab.getPosition();
        viewPager.setCurrentItem(pos);
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
}
