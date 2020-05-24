## homework7

- 下载app-debug.apk

- 对应目录下终端运行

  **adb install -t app-debug.apk**

  

### 自定义VideoView

```java
public class AlpshVideo extends VideoView {  //为防止横竖屏幕的宽高不适配,需要自定义VideoView重写测量方法
    public AlpshVideo(Context context) {
        super(context);
    }

    public AlpshVideo(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AlpshVideo(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //重新计算高度
        int width = getDefaultSize(0, widthMeasureSpec);
        int height = getDefaultSize(0, heightMeasureSpec);
        setMeasuredDimension(width, height);
    }
}
```

### 布局

```java
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:keepScreenOn="true"
    android:orientation="vertical">
    
    <com.bytedance.videoplayer.AlpshVideo
        android:id="@+id/video_view"
        android:layout_width="match_parent"
        android:layout_height="240dp"
        android:layout_gravity="center" />

    <Button
        android:id="@+id/button"
        android:layout_width="match_parent"
        android:layout_height="40dp"
        android:text="播放" />

    <SeekBar
        android:id="@+id/media_progress"
        android:layout_width="match_parent"
        android:layout_height="5dp"
        android:layout_marginTop="10dp"
        android:max="200"
        android:progress="0" />

    <TextView
        android:id="@+id/text"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content" />
</LinearLayout>
```

### MainActivity

```java
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
```

