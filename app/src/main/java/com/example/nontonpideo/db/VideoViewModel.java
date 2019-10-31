package com.example.nontonpideo.db;

import android.annotation.SuppressLint;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.util.Log;

import com.example.nontonpideo.BuildConfig;
import com.example.nontonpideo.entity.Video;
import com.example.nontonpideo.util.NotificationUtil;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import cz.msebera.android.httpclient.Header;

public class VideoViewModel extends ViewModel {
    private static final String TAG = "VideoViewModel";
    private static final String MOVIE_URL
            = "https://api.themoviedb.org/3/movie/upcoming?language=en-US&api_key=";
    private static final String TV_SHOW_URL
            = "https://api.themoviedb.org/3/tv/popular?language=en-US&api_key=";
    private static final String API_KEY = BuildConfig.TMDB_API_KEY;
    private static final String MOVIE_SEARCH_URL = "https://api.themoviedb.org/3/search/movie?api_key="
            + API_KEY +"&language=en-US&query=";
    private static final String TV_SHOW_SEARCH_URL = "https://api.themoviedb.org/3/search/tv?api_key="
            + API_KEY +"&language=en-US&query=";
    private static final String MOVIES_RELEASED_URL = "https://api.themoviedb.org/3/discover/movie?api_key="
            + API_KEY + "&primary_release_date.gte={TODAY DATE}&primary_release_date.lte={TODAY DATE}";
    private static final String TV_SHOWS_RELEASED_URL = "https://api.themoviedb.org/3/discover/tv?api_key="
            + API_KEY + "&primary_release_date.gte={TODAY DATE}&primary_release_date.lte={TODAY DATE}";

    private final MutableLiveData<ArrayList<Video>> upcomingMoviesLiveData = new MutableLiveData<>();
    private final MutableLiveData<ArrayList<Video>> popularTVShowLiveData = new MutableLiveData<>();
    private VideoHelper videoHelper;

    public LiveData<ArrayList<Video>> getUpcomingMoviesLiveData() {
        return upcomingMoviesLiveData;
    }

    public LiveData<ArrayList<Video>> getPopularTVShowLiveData() {
        return popularTVShowLiveData;
    }

    public VideoViewModel() {
    }

    public void getAndInsertUpcomingMoviesToLiveData() {
        AsyncHttpClient client = new AsyncHttpClient();
        String url = MOVIE_URL + API_KEY;

        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    insertVideoLiveDataFromResponse(responseBody, true);
                } catch (Exception e) {
                    Log.e(TAG, "onSuccess triggered an exception:\n" + e.getMessage());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.e(TAG, "onFailure triggered");
            }
        });
    }

    public void getAndInsertPopularTvShowsToLiveData() {
        AsyncHttpClient client = new AsyncHttpClient();
        String url = TV_SHOW_URL + API_KEY;

        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    insertVideoLiveDataFromResponse(responseBody, false);
                } catch (Exception e) {
                    Log.e("TAG", "onSuccess triggered an exception:\n" + e.getMessage());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.e(TAG, "onFailure triggered");
            }
        });
    }

    public void searchAndInsertMovieToLiveData(String query){
        AsyncHttpClient client = new AsyncHttpClient();
        String url = MOVIE_SEARCH_URL + query;

        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    insertVideoLiveDataFromResponse(responseBody, true);
                } catch (Exception e) {
                    Log.e(TAG, "onSuccess triggered an exception:\n" + e.getMessage());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.e(TAG, "onFailure triggered");
            }
        });
    }

    public void searchAndInsertTVShowToLiveData(String query) {
        AsyncHttpClient client = new AsyncHttpClient();
        String url = TV_SHOW_SEARCH_URL + query;

        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    insertVideoLiveDataFromResponse(responseBody, false);
                } catch (Exception e) {
                    Log.e("TAG", "onSuccess triggered an exception:\n" + e.getMessage());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.e(TAG, "onFailure triggered");
            }
        });
    }

    private void insertVideoLiveDataFromResponse(byte[] responseBody, boolean isMovie) throws JSONException {
        JSONArray videoJSONArray = parseResponseToJSONArray(responseBody);
        ArrayList<Video> videos = addVideosFromJSON(videoJSONArray, isMovie);
        if (isMovie)
            upcomingMoviesLiveData.postValue(videos);
        else
            popularTVShowLiveData.postValue(videos);
    }

    private JSONArray parseResponseToJSONArray(byte[] responseBody) throws JSONException {
        String response = new String(responseBody);
        JSONObject responseObject = new JSONObject(response);
        return responseObject.getJSONArray("results");
    }

    private ArrayList<Video> addVideosFromJSON(JSONArray videosJSONArray, boolean isMovie) {
        ArrayList<Video> videos = new ArrayList<>();
        for (int i = 0; i < videosJSONArray.length(); i++) {
            JSONObject videoObject = null;
            try {
                videoObject = videosJSONArray.getJSONObject(i);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Video video = isMovie ? Video.fromMovieJSON(videoObject)
                    : Video.fromSeriesJSON(videoObject);
            if (video != null) {
                videos.add(video);
            }
        }
        return videos;
    }

    public void getAndInsertFavoriteMoviesToLiveData(Context context){
        ArrayList<Video> videos;

        videoHelper = VideoHelper.getInstance(context);
        videoHelper.open();

        videos = videoHelper.getFavoriteMoviesData();
        upcomingMoviesLiveData.postValue(videos);
    }

    public void getAndInsertFavoriteTVShowsToLiveData(Context context){
        ArrayList<Video> videos;

        videoHelper = VideoHelper.getInstance(context);
        videoHelper.open();

        videos = videoHelper.getFavoriteTVShowsData();
        popularTVShowLiveData.postValue(videos);
    }

    public void getAndNotifyReleasedMovies(final Context context){
        AsyncHttpClient client = new AsyncHttpClient();
        String url = MOVIES_RELEASED_URL;
        String formattedDate = getCurrentDateAsString();

        url = url.replace("{TODAY DATE}", formattedDate);

        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    JSONArray videoJSONArray = parseResponseToJSONArray(responseBody);
                    ArrayList<Video> videos = addVideosFromJSON(videoJSONArray, true);

                    NotificationUtil notificationUtil = new NotificationUtil(context);

                    for(Video video : videos){
                        notificationUtil.sendReleasedMovieNotification(video);
                    }

                } catch (Exception e) {
                    Log.e(TAG, "onSuccess triggered an exception:\n" + e.getMessage());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.e(TAG, "onFailure triggered");
            }
        });
    }

    public void getAndNotifyReleasedTVShows(final Context context){
        AsyncHttpClient client = new AsyncHttpClient();
        String url = TV_SHOWS_RELEASED_URL;
        String formattedDate = getCurrentDateAsString();

        url = url.replace("{TODAY DATE}", formattedDate);

        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    JSONArray videoJSONArray = parseResponseToJSONArray(responseBody);
                    ArrayList<Video> videos = addVideosFromJSON(videoJSONArray, false);

                    NotificationUtil notificationUtil = new NotificationUtil(context);

                    for(Video video : videos){
                        notificationUtil.sendReleasedMovieNotification(video);
                    }

                } catch (Exception e) {
                    Log.e(TAG, "onSuccess triggered an exception:\n" + e.getMessage());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.e(TAG, "onFailure triggered");
            }
        });
    }

    private String getCurrentDateAsString() {
        Date date = Calendar.getInstance().getTime();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(date);
    }
}
