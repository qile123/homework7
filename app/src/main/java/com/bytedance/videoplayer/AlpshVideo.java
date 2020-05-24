package com.bytedance.videoplayer;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.VideoView;

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