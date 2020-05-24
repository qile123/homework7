package com.bytedance.videoplayer;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.VideoView;

import java.util.Formatter;
import java.util.Locale;


public class MainActivity extends AppCompatActivity {

    private Button button;
    private SeekBar seekBar;
    private TextView textView;
    private VideoView videoView;
    private Handler handler;
    private Uri uri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("qile","video player is created");
        button = findViewById(R.id.button);
        seekBar = findViewById(R.id.media_progress);
        textView =findViewById(R.id.text);
        videoView = findViewById(R.id.video_view);
        //uri = Uri.parse("android.resource://" + this.getPackageName() + "/" +R.raw.bytedance);
       // videoView.setVideoURI(uri);
        //Log.d("qile",uri.toString());
        uri = null;
        uri = getIntent().getData();
        if(uri == null){
            videoView.setVideoPath("android.resource://" + getPackageName() + "/" + R.raw.bytedance);
        }
        else{
            videoView.setVideoURI(uri);
            videoView.start();
            handler.sendEmptyMessage(1);
        }

        if (savedInstanceState != null) {
            int ss = savedInstanceState.getInt("a");
            Log.d("qile","msg receied :"+ss);
            videoView.seekTo(ss);
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (videoView.isPlaying()) {
                    videoView.pause();
                    button.setText("播放");
                    handler.removeMessages(1);
                } else {
                    button.setText("暂停");
                    videoView.start();
                    handler.sendEmptyMessage(1);
                }
            }
        });

        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if (msg.what == 1) {
                    int currentPosition = videoView.getCurrentPosition();
                    int duration = videoView.getDuration();
                    updateTime(currentPosition,duration);
                    seekBar.setMax(duration);
                    seekBar.setProgress(currentPosition);
                    handler.sendEmptyMessageDelayed(1, 500);
                    return true;
                }
                return false;
            }
        });


        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
              //  updateTime(mMediaTime, progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                handler.removeMessages(1);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int progress = MainActivity.this.seekBar.getProgress();
                videoView.seekTo(progress);
                handler.sendEmptyMessage(1);
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        int a = videoView.getCurrentPosition();
        outState.putInt("a", videoView.getCurrentPosition());
        Log.d("qile","msg sended :"+a);
        super.onSaveInstanceState(outState);
    }

    private void updateTime(int nowTime,int totalTime){
        String progress = stringForTime(nowTime) + "/" + stringForTime(totalTime);
        textView.setText(progress);
    }

    private String stringForTime(int timeMs) {
        StringBuilder mFormatBuilder = new StringBuilder();
        Formatter mFormatter = new Formatter(mFormatBuilder, Locale.getDefault());
        int totalSeconds = timeMs / 1000;
        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = totalSeconds / 3600;
        mFormatBuilder.setLength(0);
        if (hours > 0) {
            return mFormatter.format("%d:%02d:%02d", hours, minutes, seconds).toString();
        } else {
            return mFormatter.format("%02d:%02d", minutes, seconds).toString();
        }
    }
    //
    @SuppressLint("SourceLockedOrientationActivity")
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        //当为横屏时候
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){

        }else if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            //竖屏时
        }
    }
}
