package com.example.nontonpideo.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.example.nontonpideo.R;
import com.example.nontonpideo.broadcastreceiver.VideoScheduledReminderReceiver;

public class ReminderSettingsActivity extends AppCompatActivity implements
        CompoundButton.OnCheckedChangeListener {

    private final String RELEASE_REMINDER_STATUS_KEY = "release-reminder-status-key";
    private final String DAILY_REMINDER_STATUS_KEY = "daily-reminder-status-key";
    private SharedPreferences sharedPreferences;
    private boolean isToBeRemindedDaily = false;
    private boolean isToBeRemindedForReleases = false;
    private Switch releaseSwitch;
    private Switch dailySwitch;
    private final VideoScheduledReminderReceiver receiver = new VideoScheduledReminderReceiver();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder_settings);

        initializeViews();

        sharedPreferences = getSharedPreferences(MainActivity.MY_SHARED_PREFERENCES,
                        Context.MODE_PRIVATE);

        isToBeRemindedDaily = sharedPreferences.getBoolean(DAILY_REMINDER_STATUS_KEY, false);
        isToBeRemindedForReleases = sharedPreferences.getBoolean(RELEASE_REMINDER_STATUS_KEY, false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        isToBeRemindedDaily = sharedPreferences.getBoolean(DAILY_REMINDER_STATUS_KEY, false);
        isToBeRemindedForReleases = sharedPreferences.getBoolean(RELEASE_REMINDER_STATUS_KEY, false);
        setInitialSwitchState();

    }

    private void initializeViews() {
        releaseSwitch = findViewById(R.id.switch_release_reminder);
        dailySwitch = findViewById(R.id.switch_daily_reminder);

        setInitialSwitchState();

        releaseSwitch.setOnCheckedChangeListener(this);
        dailySwitch.setOnCheckedChangeListener(this);
    }

    private void setInitialSwitchState() {
        if(isToBeRemindedForReleases){
            releaseSwitch.setChecked(true);
        }

        if(isToBeRemindedDaily){
            dailySwitch.setChecked(true);
        }
    }

    @SuppressLint("ApplySharedPref")
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        switch(buttonView.getId()){
            case R.id.switch_daily_reminder:
                if (isChecked) {
                    receiver.enableDailyReminder(this);
                    editor.putBoolean(DAILY_REMINDER_STATUS_KEY, true);
                } else {
                    receiver.disableDailyReminder(this);
                    editor.putBoolean(DAILY_REMINDER_STATUS_KEY, false);
                }
                break;
            case R.id.switch_release_reminder:
                if (isChecked) {
                    receiver.enableReleaseReminder(this);
                    editor.putBoolean(RELEASE_REMINDER_STATUS_KEY, true);
                } else {
                    receiver.disableReleaseReminder(this);
                    editor.putBoolean(RELEASE_REMINDER_STATUS_KEY, false);
                }
                break;
        }
        editor.commit();
    }
}
