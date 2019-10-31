package com.example.nontonpideo.activity;

import android.os.Bundle;
import android.os.Parcel;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.nontonpideo.R;
import com.example.nontonpideo.adapter.VideoAdapter;
import com.example.nontonpideo.db.VideoHelper;
import com.example.nontonpideo.entity.Video;
import com.example.nontonpideo.util.ParcelableUtil;

public class MovieDetailActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView title;
    private TextView year;
    private TextView overview;
    private TextView score;
    private ImageView poster;
    private Video video;
    private boolean isFavorited;
    private FloatingActionButton btnFavorites;
    private VideoHelper videoHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        initializeViews();
        prepareVideoHelper();

        byte[] videoByteArray = getIntent().getByteArrayExtra(VideoAdapter.EXTRA_VIDEO_BYTE_ARRAY);
        if(videoByteArray != null){
            Parcel videoParcel = ParcelableUtil.parseByteArray(videoByteArray);
            video = new Video(videoParcel);
        }else{
            video = getIntent().getParcelableExtra(VideoAdapter.EXTRA_VIDEO);
        }

        if (video != null) {
            displayVideoDetails(video);
            String type = video.getType();

            if (type.equals(Video.MOVIE)) {
                isFavorited = videoHelper
                        .getMovieFavoritedStatus(video.getId());
            } else if (type.equals(Video.TV_SHOW)) {
                isFavorited = videoHelper
                        .getTVShowFavoritedStatus(video.getId());
            }
            if (isFavorited) {
                btnFavorites.setImageResource(R.drawable.ic_star_black);
            }
        }
    }

    private void prepareVideoHelper() {
        videoHelper = VideoHelper.getInstance(getApplicationContext());
        videoHelper.open();
    }

    private void displayVideoDetails(Video video) {
        title.setText(video.getTitle());
        year.setText(video.getYear());
        overview.setText(video.getOverview());
        score.setText(video.getScore());
        Glide.with(this).load(video.getPicture()).into(poster);
    }

    private void initializeViews() {
        title = findViewById(R.id.tv_title);
        year = findViewById(R.id.tv_year);
        overview = findViewById(R.id.tv_overview);
        score = findViewById(R.id.tv_score);
        poster = findViewById(R.id.img_poster);
        btnFavorites = findViewById(R.id.btn_favorites);
        btnFavorites.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_favorites) {
            if (isFavorited) {
                if (video.getType().equals(Video.MOVIE)) {
                    videoHelper.deleteMovie(video.getId());
                } else if (video.getType().equals(Video.TV_SHOW)) {
                    videoHelper.deleteTVShow(video.getId());
                }
                isFavorited = false;
                btnFavorites.setImageResource(R.drawable.ic_star_border_black);
            } else {
                if (video.getType().equals(Video.MOVIE)) {
                    videoHelper.insertMovie(video);
                } else if (video.getType().equals(Video.TV_SHOW)) {
                    videoHelper.insertTVShow(video);
                }
                isFavorited = true;
                btnFavorites.setImageResource(R.drawable.ic_star_black);
            }
        }
    }
}
