package game.goldtel.com.gametest.test;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import game.goldtel.com.gametest.R;

/**
 * Created by BS on 2018-5-8.
 * Surface模板
 */

public class NormalSurfaceView extends SurfaceView implements SurfaceHolder.Callback,Runnable{

    private Canvas canvas;
    private SurfaceHolder sh;
    private Bitmap bitmap;
    private Paint mPaint;
    private Thread thread;
    private boolean flag;   //线程死循环标志位
    private int frameNum = 30;   //自定义view帧数



    private void init() {
        sh = getHolder();
        sh.addCallback(this);
        mPaint = new Paint();
        mPaint.setColor(0xffffffff);
        mPaint.setAntiAlias(true);
        //设置焦点
        setFocusable(true);
    }

    public NormalSurfaceView(Context context) {
        super(context);
        init();
    }

    public NormalSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
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
            if(end-start<1000/frameNum) {
                try {
                    Thread.sleep(1000/frameNum-(end-start));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 业务逻辑
     * 在这里实现耗时操作
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




