package com.example.nontonpideo.entity;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Video implements Parcelable {

    public static final String MOVIE = "Movie";
    public static final String TV_SHOW = "TV Show";
    private final int id;
    private final String title;
    private final String picture;
    private final String year;
    private final String overview;
    private final String score;
    private final String type;
    private static final String TAG = "Video";

    public int getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public Video(String type, int id, String title, String picture, String year, String overview, String score) {
        this.type = type;
        this.id = id;
        this.title = title;
        this.picture = picture;
        this.year = year;
        this.overview = overview;
        this.score = score;
    }

    /**
     * Creates an instance of video from movie response JSON
     *
     * @param object a movie object from the response JSON
     * @return returns a movie video
     */
    public static Video fromMovieJSON(JSONObject object) {
        try {
            int id = object.getInt("id");
            String title = object.getString("title");
            String picture = "https://image.tmdb.org/t/p/w185"
                    + object.getString("poster_path");
            String overview = object.getString("overview");
            String score = object.getString("vote_average");
            String year;
            try {
                year = getYearFromDate(object.getString("release_date"));
            } catch (ParseException e) {
                Log.e(TAG, "Date parse exception triggered.");
                return null;
            }
            return new Video(MOVIE, id, title, picture, year, overview, score);
        } catch (Exception e) {
            Log.e(TAG, "JSONObject traversal exception triggered.");
        }
        return null;
    }

    /**
     * Parse year from date given in the API.
     *
     * @param release_date the release date or the initial airing date form the API
     * @return returns the parsed year.
     * @throws ParseException throws exception if year is not in appropriate format
     */
    private static String getYearFromDate(String release_date) throws ParseException {
        String year;
        Date date;
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        date = dateFormat.parse(release_date);
        Calendar releaseDateCalendar = Calendar.getInstance();
        releaseDateCalendar.setTime(date);
        year = String.valueOf(releaseDateCalendar.get(Calendar.YEAR));
        return year;
    }

    /**
     * Creates an instance of video from tv show response JSON
     *
     * @param object a tv show object from the response JSON
     * @return returns a tv show video
     */
    public static Video fromSeriesJSON(JSONObject object) {
        try {
            int id = object.getInt("id");
            String title = object.getString("original_name");
            String picture = "https://image.tmdb.org/t/p/w185"
                    + object.getString("poster_path");
            String overview = object.getString("overview");
            String score = object.getString("vote_average");
            String year;
            try {
                year = getYearFromDate(object.getString("first_air_date"));
            } catch (ParseException e) {
                Log.e(TAG, "Date parse exception triggered.");
                return null;
            }
            return new Video(TV_SHOW, id, title, picture, year, overview, score);
        } catch (Exception e) {
            Log.e(TAG, "JSONObject traversal exception triggered.");
        }
        return null;
    }

    public String getTitle() {
        return title;
    }

    public String getPicture() {
        return picture;
    }

    public String getYear() {
        return year;
    }

    public String getOverview() {
        return overview;
    }

    public String getScore() {
        return score;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.title);
        dest.writeString(this.picture);
        dest.writeString(this.year);
        dest.writeString(this.overview);
        dest.writeString(this.score);
        dest.writeString(this.type);
    }

    public Video(Parcel in) {
        this.id = in.readInt();
        this.title = in.readString();
        this.picture = in.readString();
        this.year = in.readString();
        this.overview = in.readString();
        this.score = in.readString();
        this.type = in.readString();
    }

    public static final Parcelable.Creator<Video> CREATOR = new Parcelable.Creator<Video>() {
        @Override
        public Video createFromParcel(Parcel source) {
            return new Video(source);
        }

        @Override
        public Video[] newArray(int size) {
            return new Video[size];
        }
    };
}
