package game.goldtel.com.gametest.test;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import game.goldtel.com.gametest.R;

/**
 * Created by BS on 2018-4-10.
 * 帧动画和剪切画动画
 * 剪切画动画即动画靠1张图实现，通过每次设置显示区域来控制
 */

public class FrameMovieSurfaceView extends SurfaceView implements SurfaceHolder.Callback,Runnable{

    private Canvas canvas;
    private SurfaceHolder sh;
    private Bitmap bitmap;
    private Paint mPaint;
    private Thread thread;
    private boolean flag;   //线程死循环标志位

    private Bitmap[] fisharr = new Bitmap[10];
    int currentFrame;   //当前帧



    private void init() {
        sh = getHolder();
        sh.addCallback(this);
        mPaint = new Paint();
        mPaint.setColor(0xffffffff);
        mPaint.setAntiAlias(true);
        //设置焦点
        setFocusable(true);
        //初始化图片
        for(int i=0;i<fisharr.length;i++) {
            //资源文件命名按照一定规律的话，R资源文件对应ID也有一定规律
            fisharr[i] = BitmapFactory.decodeResource(getResources(),R.mipmap.fish0+i);
        }
        //初始化剪切图
        bitmap = BitmapFactory.decodeResource(getResources(),R.mipmap.fish);
    }

    public FrameMovieSurfaceView(Context context) {
        super(context);
        init();
    }

    public FrameMovieSurfaceView(Context context, AttributeSet attrs) {
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
                canvas.drawColor(0xff000000);
                canvas.drawBitmap(fisharr[currentFrame],300,300,mPaint);
                canvas.save();
                canvas.clipRect(0,0,bitmap.getWidth()/10,bitmap.getHeight());
                int bitmapX = -currentFrame*(bitmap.getWidth()/10);
                canvas.drawBitmap(bitmap,bitmapX,0,mPaint);
                canvas.restore();
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
     * 业务逻辑
     */
    private void logic() {
        currentFrame++;
        if(currentFrame>=fisharr.length) {
            currentFrame = 0;
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        flag = false;
    }




}




