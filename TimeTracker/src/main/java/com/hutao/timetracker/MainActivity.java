package com.hutao.timetracker;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ShareActionProvider;
import android.widget.TextView;


public class MainActivity extends Activity implements View.OnClickListener,SharedPreferences.OnSharedPreferenceChangeListener {

    private TextView counter;

    private TextView seconds;

    private String temp;

    private TimeListAdapter timeListAdapter;

    private Button startButton;

    private Button resetButton;

    private MHandler mHandler;

    private long mStart;

    private long mTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        SharedPreferences sharedPreferences;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
        counter = (TextView)findViewById(R.id.counter);
        counter.setText(DateUtils.formatElapsedTime(0));
        seconds = (TextView)findViewById(R.id.seconds);
        temp = DateUtils.formatElapsedTime(0);
        seconds.setText(temp.substring(temp.length()-2,temp.length()));
        if (sharedPreferences.getString("color","White").equals("White")) {
            counter.setTextColor(getResources().getColor(R.color.white));
        }
        if (sharedPreferences.getString("color","White").equals("Red")) {
            counter.setTextColor(getResources().getColor(R.color.red));
        }
        if (sharedPreferences.getString("color","White").equals("Green")) {
            counter.setTextColor(getResources().getColor(R.color.green));
        }
        if (sharedPreferences.getString("color","White").equals("Blue")) {
            counter.setTextColor(getResources().getColor(R.color.blue));
        }
        if (timeListAdapter == null) {
            timeListAdapter = new TimeListAdapter(this,0);
        }
        ListView listView = (ListView) findViewById(R.id.time_list);
        listView.setAdapter(timeListAdapter);
        mHandler = new MHandler();
        startButton = (Button)findViewById(R.id.start_stop);
        startButton.setOnClickListener(this);
        resetButton = (Button)findViewById(R.id.reset);
        resetButton.setOnClickListener(this);
    }

    @Override
    public  void onClick(View view) {

        switch (view.getId()) {
            case R.id.start_stop:
                if (isTimerRunning()) {
                    stopTimer();
                    startButton.setText(R.string.start);
                    resetButton.setText(R.string.reset);
                }
                else {
                    startTimer();
                    startButton.setText(R.string.stop);
                    resetButton.setText(R.string.save);
                }
                break;

            case R.id.reset:
                if (isTimerRunning()) {
                    saveTimer();
                }
                else {
                    resetTimer();
                    counter.setText(DateUtils.formatElapsedTime(0));
                    temp = DateUtils.formatElapsedTime(0);
                    seconds.setText(temp.substring(temp.length()-2,temp.length()));
                    startButton.setText(R.string.start);
                }
                break;
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences preferences,String key) {
        if (preferences.getString(key,"White").equals("White")) {
            counter.setTextColor(getResources().getColor(R.color.white));
        }
        if (preferences.getString(key,"White").equals("Red")) {
            counter.setTextColor(getResources().getColor(R.color.red));
        }
        if (preferences.getString(key,"White").equals("Green")) {
            counter.setTextColor(getResources().getColor(R.color.green));
        }
        if (preferences.getString(key,"White").equals("Blue")) {
            counter.setTextColor(getResources().getColor(R.color.blue));
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        ShareActionProvider shareActionProvider;
        MenuItem shareItem;

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        shareItem = menu.findItem(R.id.action_share);
        if (shareItem != null) {
            shareActionProvider = (ShareActionProvider)shareItem.getActionProvider();
            if (shareActionProvider != null) {
                shareActionProvider.setShareHistoryFileName(ShareActionProvider.DEFAULT_SHARE_HISTORY_FILE_NAME);
                shareActionProvider.setShareIntent(getDefaultIntent());
            }
        }
        return true;
    }

    private Intent getDefaultIntent() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_STREAM,"");
        return intent;
}

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent intent = new Intent(getApplicationContext(),SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void startTimer() {
        mStart = System.currentTimeMillis();
        mHandler.removeMessages(0);
        mHandler.sendEmptyMessage(0);
    }

    private void stopTimer() {
        mHandler.removeMessages(0);
    }

    private void resetTimer() {
        stopTimer();
        mTime = 0;
        if (timeListAdapter != null) {
            timeListAdapter.clear();
        }
    }

    private void saveTimer() {
        if (timeListAdapter != null) {
            timeListAdapter.add(mTime / 1000);
        }
    }

    private boolean isTimerRunning() {
        return mHandler.hasMessages(0);
    }


    class MHandler extends Handler {

        public MHandler() {
            super();
        }

        @Override
        public void handleMessage(Message message) {
            long current = System.currentTimeMillis();
            mTime += current - mStart;
            mStart = current;
            temp =DateUtils.formatElapsedTime(mTime/10);
            seconds.setText(temp.substring(temp.length()-2,temp.length()));
            counter.setText(DateUtils.formatElapsedTime(mTime/1000));
            mHandler.sendEmptyMessageDelayed(0,10);
        }
    }
}
