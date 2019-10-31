package com.example.nontonpideo.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.nontonpideo.activity.MovieDetailActivity;
import com.example.nontonpideo.R;
import com.example.nontonpideo.entity.Video;

import java.util.ArrayList;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoViewHolder> {

    public static final String EXTRA_VIDEO = "extra-video";
    public static final String EXTRA_VIDEO_BYTE_ARRAY = "extra-video-byte-array";
    private static final int REQUEST_ID_ACTION = 100;
    private final Context context;
    private ArrayList<Video> videos = new ArrayList<>();

    public VideoAdapter(Context context) {
        this.context = context;
    }

    private ArrayList<Video> getVideos() {
        return videos;
    }

    public void setVideos(ArrayList<Video> videos) {
        this.videos = videos;
    }

    @NonNull
    @Override
    public VideoViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemRow = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_video,
                viewGroup, false);
        return new VideoViewHolder(itemRow);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoViewHolder cvh, int i) {
        cvh.bindVideoData(getVideos().get(i));
    }

    @Override
    public int getItemCount() {
        return getVideos().size();
    }

    class VideoViewHolder extends RecyclerView.ViewHolder {

        final ImageView poster;
        final TextView title;
        final TextView year;
        final TextView overview;
        final Button btnViewDetails;

        VideoViewHolder(@NonNull View v) {
            super(v);
            poster = v.findViewById(R.id.img_poster);
            title = v.findViewById(R.id.tv_title);
            year = v.findViewById(R.id.tv_year);
            overview = v.findViewById(R.id.tv_overview);
            btnViewDetails = v.findViewById(R.id.btn_view_details);
        }

        /**
         * Bind a video data to its respective views.
         * @param video the video data to be bound.
         */
        void bindVideoData(Video video){
            this.title.setText(getShortTitle(video.getTitle()));
            this.year.setText(video.getYear());
            this.overview.setText(getShortOverview(video.getOverview()));
            Glide.with(context).load(video.getPicture()).into(this.poster);
            bindViewDetailsButton(this.btnViewDetails, video);
        }

        /**
         * This method is used to bind onclick functions into the view details button, which will
         * redirect the user to the view details activity.
         *
         * @param btn   This is the button we want to bind the function onto.
         * @param video This is the movie data we want to bind the view details activity to.
         */
        private void bindViewDetailsButton(Button btn, final Video video) {
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent viewDetailsIntent = new Intent(context, MovieDetailActivity.class);
                    viewDetailsIntent.putExtra(EXTRA_VIDEO, video);
                    ((Activity)context).startActivityForResult(viewDetailsIntent, REQUEST_ID_ACTION);
                }
            });
        }
    }

    /**
     * This method is used to return overviews that are 150 + 3 characters in size to be fit
     * into the ListView.
     *
     * @param overview This is the raw overview from the movie data which might be longer or
     *                 shorter than 150 characters.
     * @return Returns the result shortened version of overview, added with "..." for cosmetics.
     */
    private String getShortOverview(String overview) {
        if (overview.length() > 100) {
            overview = overview.substring(0, 99);
            overview = String.format("%s...", overview);
        } else if (overview.length() < 100) {
            overview = String.format("%-100s", overview);
        }
        return overview;
    }

    /**
     * This method is used to return titles that are 27 + 3 characters in size to be fit into
     * the ListView.
     *
     * @param title This is the raw title from the movie data which might be longer or shorter
     *              than 27 characters.
     * @return Returns the result shortened version of title, added with "..." for cosmetics.
     */
    private String getShortTitle(String title){
        if (title.length() > 20) {
            title = title.substring(0, 19);
            title = String.format("%s...", title);
        } else if (title.length() < 20) {
            title = String.format("%-20s", title);
        }
        return title;
    }
}
