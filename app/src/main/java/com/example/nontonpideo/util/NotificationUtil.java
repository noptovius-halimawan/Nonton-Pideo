package com.example.nontonpideo.util;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import com.example.nontonpideo.R;
import com.example.nontonpideo.activity.MainActivity;
import com.example.nontonpideo.activity.MovieDetailActivity;
import com.example.nontonpideo.adapter.VideoAdapter;
import com.example.nontonpideo.entity.Video;

import java.util.concurrent.atomic.AtomicInteger;

public class NotificationUtil {

    private final CharSequence CHANNEL_NAME = "nontonpideo-channel";
    private final String CHANNEL_ID = "channel_01";
    private final AtomicInteger notif_id = new AtomicInteger(0);
    private final Context context;

    public NotificationUtil(Context context) {
        this.context = context;
    }

    public void sendReleasedMovieNotification(Video video) {
        int id = notif_id.incrementAndGet();

        NotificationManager mNotificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        Bitmap largeIcon = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.ic_notifications_black_24dp);

        Intent intent = new Intent(context, MovieDetailActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(VideoAdapter.EXTRA_VIDEO_BYTE_ARRAY, ParcelableUtil.toByteArray(video));

        PendingIntent pendingIntent = PendingIntent.getActivity(context, id,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder mBuilder;
        mBuilder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setContentTitle("Released today: " + video.getTitle())
                .setContentText(video.getOverview())
                .setSmallIcon(R.drawable.ic_mail_black_24dp)
                .setLargeIcon(largeIcon)
                .setShowWhen(true)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            mBuilder.setChannelId(CHANNEL_ID);
            if (mNotificationManager != null) {
                mNotificationManager.createNotificationChannel(channel);
            }
        }
        Notification notification = mBuilder.build();
        if (mNotificationManager != null) {
            mNotificationManager.notify(id, notification);
        }
    }

    public void sendDailyNotification() {
        int id = notif_id.incrementAndGet();

        NotificationManager mNotificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        Bitmap largeIcon = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.ic_notifications_black_24dp);

        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, id,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder mBuilder;
        mBuilder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setContentTitle("Come back to us!")
                .setContentText("We missed you. Check out the latest movies and tv shows today with NontonPideo.")
                .setSmallIcon(R.drawable.ic_mail_black_24dp)
                .setLargeIcon(largeIcon)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            mBuilder.setChannelId(CHANNEL_ID);
            if (mNotificationManager != null) {
                mNotificationManager.createNotificationChannel(channel);
            }
        }
        Notification notification = mBuilder.build();
        if (mNotificationManager != null) {
            mNotificationManager.notify(id, notification);
        }
    }
}
