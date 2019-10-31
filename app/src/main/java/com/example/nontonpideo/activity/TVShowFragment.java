package com.example.nontonpideo.activity;


import android.appwidget.AppWidgetManager;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RemoteViews;

import com.example.nontonpideo.R;
import com.example.nontonpideo.adapter.VideoAdapter;
import com.example.nontonpideo.db.VideoHelper;
import com.example.nontonpideo.db.VideoViewModel;
import com.example.nontonpideo.entity.Video;
import com.example.nontonpideo.widgets.FavoritesWidgetProvider;

import java.util.ArrayList;
import java.util.Objects;

public class TVShowFragment extends Fragment {

    private RecyclerView rv;
    private VideoAdapter videoAdapter;
    private SharedPreferences preferences;
    private boolean isLoading = false;

    public TVShowFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tvshow, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        preferences = Objects.requireNonNull(getActivity())
                .getSharedPreferences(MainActivity.MY_SHARED_PREFERENCES,
                        Context.MODE_PRIVATE);

        String mode = preferences.getString(MainActivity.VIEW_MODE_KEY, MainActivity.NO_MODE);
        String query = preferences.getString(MainActivity.SEARCH_KEY, MainActivity.EMPTY_SEARCH);

        showViewBasedOnMode(mode, query);
    }

    private void showViewBasedOnMode(String mode, String query) {
        VideoViewModel videoViewModel;
        initializeViews();
        initializeAdapterAndRecyclerView();
        showLoading();

        if (Objects.requireNonNull(mode).equals(MainActivity.HOME_MODE)) {
            videoViewModel = initializeViewModel();
            if(query.equals(MainActivity.EMPTY_SEARCH)){
                videoViewModel.getAndInsertPopularTvShowsToLiveData();
            }else{
                videoViewModel.searchAndInsertTVShowToLiveData(query);
            }
        } else {
            videoViewModel = initializeFavoriteTVShowsViewModel();
            videoViewModel.getAndInsertFavoriteTVShowsToLiveData(Objects.requireNonNull(getActivity()).getApplicationContext());
        }
    }

    private void initializeViews() {
        rv = Objects.requireNonNull(getView()).findViewById(R.id.rv_series);
        rv.setHasFixedSize(true);
        MainActivity.PROGRESS_BAR = Objects.requireNonNull(getActivity()).findViewById(R.id.main_progress_bar);
    }

    private VideoViewModel initializeViewModel() {
        VideoViewModel videoViewModel = ViewModelProviders.of(this)
                .get("TVShowViewModel", VideoViewModel.class);
        videoViewModel.getPopularTVShowLiveData().observe(this, getVideos);
        return videoViewModel;
    }

    private void initializeAdapterAndRecyclerView() {
        videoAdapter = new VideoAdapter(getContext());
        videoAdapter.notifyDataSetChanged();
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        rv.setAdapter(videoAdapter);
    }

    private VideoViewModel initializeFavoriteTVShowsViewModel() {
        VideoViewModel videoViewModel = ViewModelProviders.of(this)
                .get("TVShowViewModel", VideoViewModel.class);
        videoViewModel.getPopularTVShowLiveData().observe(this, getVideos);
        return videoViewModel;
    }

    private final Observer<ArrayList<Video>> getVideos = new Observer<ArrayList<Video>>() {
        @Override
        public void onChanged(@Nullable ArrayList<Video> videos) {
            synchronized (this){
                if (videos != null && isLoading) {
                    videoAdapter.setVideos(videos);
                    stopLoading();
                    videoAdapter.notifyDataSetChanged();

                    RemoteViews view = new RemoteViews(Objects.requireNonNull(getActivity())
                            .getPackageName(), R.layout.favorites_widget);
                    ComponentName theWidget = new ComponentName(getActivity(), FavoritesWidgetProvider.class);
                    AppWidgetManager manager = AppWidgetManager.getInstance(getActivity());
                    manager.updateAppWidget(theWidget, view);
                }
            }
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        VideoHelper videoHelper = VideoHelper.getInstance(Objects.requireNonNull(getActivity()).getApplicationContext());
        videoHelper.close();
    }

    @Override
    public void onResume() {
        super.onResume();
        String mode = preferences.getString(MainActivity.VIEW_MODE_KEY, MainActivity.NO_MODE);
        if (Objects.requireNonNull(mode).equals(MainActivity.FAVORITES_MODE)) {
            showLoading();

            VideoViewModel videoViewModel;
            videoViewModel = initializeFavoriteTVShowsViewModel();
            videoViewModel.getAndInsertFavoriteTVShowsToLiveData(Objects.requireNonNull(getActivity()).getApplicationContext());

            videoAdapter.notifyDataSetChanged();
        }
    }

    private void stopLoading() {
        isLoading = false;
        if (MainActivity.PROGRESS_BAR == null)
            return;
        MainActivity.PROGRESS_BAR.setVisibility(View.GONE);
    }

    private void showLoading() {
        isLoading = true;
        if (MainActivity.PROGRESS_BAR == null)
            return;
        MainActivity.PROGRESS_BAR.setVisibility(View.VISIBLE);
    }
}
