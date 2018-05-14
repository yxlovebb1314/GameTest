package game.goldtel.com.gametest.test;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import game.goldtel.com.gametest.R;
import game.goldtel.com.gametest.utils.CustomViewUtils;

/**
 * Created by BS on 2018-4-11.
 * SoundPool播放音乐
 * 注：SoundPool只能存放1M大小的音乐数据!
 * 如果大于1M，则只会播放一小段，而且会抛异常，但程序不报错
 * 并且音频格式最好用OGG格式
 */

public class SoundPoolSurfaceView extends SurfaceView implements SurfaceHolder.Callback,Runnable{

    private Canvas canvas;
    private SurfaceHolder sh;
    private Bitmap bitmap;
    private Paint mPaint;
    private Thread thread;
    private boolean flag;   //线程死循环标志位

    private SoundPool.Builder spb;
    private SoundPool sp;
    private int soundId_long;   //长音乐文件id
    private int soundId_short;  //短音乐文件id


    private void init(Context context) {
        sh = getHolder();
        sh.addCallback(this);
        mPaint = new Paint();
        mPaint.setColor(0xffffffff);
        mPaint.setAntiAlias(true);
        mPaint.setTextSize(55);
        //设置焦点
        setFocusable(true);
        /**
         *  实例化SoundPool方法
         *  原来通过new 的方式已过时
         *  现在使用
         *  SoundPool.Builder spb= new SoundPool.Builder();
            spb.setMaxStreams(10);
            SoundPool sp = spb.build();
         */
        if(Build.VERSION.SDK_INT >= 21)  {
            spb = new SoundPool.Builder();
            spb.setMaxStreams(4);   //设置最大可播放的音频数量
            sp = spb.build();
        }else {
            sp = new SoundPool(4,AudioManager.STREAM_MUSIC,100);
        }
        /**
         * 加载音乐文件获取id
         * 注意：加载完成后不能马上进行播放，可能会导致无声音
         * 如果需要实现这样的代码逻辑，则需要实现一个监听
         * sp.setOnLoadCompleteListener 在监听中完成播放操作
         */
        soundId_long = sp.load(context, R.raw.himi_long,1);
        soundId_short = sp.load(context,R.raw.himi_short,1);
    }

    public SoundPoolSurfaceView(Context context) {
        super(context);
        init(context);
    }

    public SoundPoolSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        flag = true;
        thread = new Thread(this);
        thread.start();
    }


    /**
     * 绘画方法
     */
    public void myDraw() {
        try{
            canvas = sh.lockCanvas();
            if(canvas != null) {
                canvas.drawColor(0xff000000);
                canvas.drawText("点击按钮进行播放",50,50,mPaint);
            }
        }catch(Exception e) {
            e.printStackTrace();
        }finally {
            if(canvas != null)
            sh.unlockCanvasAndPost(canvas);
        }
    }




    @Override
    public void run() {
        while(flag) {
            long start = System.currentTimeMillis();
            myDraw();
            logic();
            long end = System.currentTimeMillis();
            if(end-start<50) {
                try {
                    Thread.sleep(50-(end-start));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    /**
     * 播放长音效
     */
    public void startLongMusic() {
        int play = sp.play(soundId_long,1f,1f,0,0,1);
Log.i("TAG","长音效:"+play);
    }

    /**
     * 播放短音效
     */
    public void startShortMusic() {
        int play = sp.play(soundId_short, 1f, 1f, 0, 0, 1);
Log.i("TAG","短音效:"+play);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(CustomViewUtils.measureWidth(widthMeasureSpec,0),
                CustomViewUtils.measureHeight(heightMeasureSpec,0));
    }

    /**
     * 业务逻辑
     */
    private void logic() {
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        flag = false;
    }




}




