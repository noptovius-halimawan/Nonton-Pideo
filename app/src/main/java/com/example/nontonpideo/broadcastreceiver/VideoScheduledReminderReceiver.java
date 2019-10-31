package com.example.nontonpideo.broadcastreceiver;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.nontonpideo.db.VideoViewModel;
import com.example.nontonpideo.util.NotificationUtil;

import java.util.Calendar;

public class VideoScheduledReminderReceiver extends BroadcastReceiver {

    private final int RELEASE_REMINDER_ID = 1;
    private final int DAILY_REMINDER_ID = 2;
    private final String REQUEST_CODE = "request-code";

    @Override
    public void onReceive(Context context, Intent intent) {
        int MISSING_ID = 404;
        int requestCode = intent.getIntExtra(REQUEST_CODE, MISSING_ID);

        if(requestCode == RELEASE_REMINDER_ID){
            VideoViewModel videoViewModel = new VideoViewModel();
            videoViewModel.getAndNotifyReleasedMovies(context);
            videoViewModel.getAndNotifyReleasedTVShows(context);
        }else if(requestCode == DAILY_REMINDER_ID){
            NotificationUtil notifUtil = new NotificationUtil(context);
            notifUtil.sendDailyNotification();
        }
    }

    public void enableReleaseReminder(Context context){
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, VideoScheduledReminderReceiver.class);
        int RELEASE_REMINDER_HOUR_OF_DAY = 8;
        Calendar calendar = getCalendarWithHourOfDay(RELEASE_REMINDER_HOUR_OF_DAY);

        intent.putExtra(REQUEST_CODE, RELEASE_REMINDER_ID);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, RELEASE_REMINDER_ID, intent,
                0);

        if (alarmManager != null) {
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY, pendingIntent);
        }
    }

    public void enableDailyReminder(Context context){
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, VideoScheduledReminderReceiver.class);
        int DAILY_REMINDER_HOUR_OF_DAY = 7;
        Calendar calendar = getCalendarWithHourOfDay(DAILY_REMINDER_HOUR_OF_DAY);

        intent.putExtra(REQUEST_CODE, DAILY_REMINDER_ID);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, DAILY_REMINDER_ID, intent,
                0);

        if (alarmManager != null) {
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY, pendingIntent);
        }
    }

    public void disableReleaseReminder(Context context){
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, VideoScheduledReminderReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, RELEASE_REMINDER_ID, intent,
                0);
        pendingIntent.cancel();

        if (alarmManager != null) {
            alarmManager.cancel(pendingIntent);
        }
    }

    public void disableDailyReminder(Context context){
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, VideoScheduledReminderReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, DAILY_REMINDER_ID, intent,
                0);
        pendingIntent.cancel();

        if (alarmManager != null) {
            alarmManager.cancel(pendingIntent);
        }
    }

    private Calendar getCalendarWithHourOfDay(int hourOfDay){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar;
    }

}
