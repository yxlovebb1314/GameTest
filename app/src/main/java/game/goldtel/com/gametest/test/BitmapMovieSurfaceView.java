package game.goldtel.com.gametest.test;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import game.goldtel.com.gametest.R;
import game.goldtel.com.gametest.utils.CustomViewUtils;

/**
 * Created by BS on 2018-4-10.
 * 动态位图
 */

public class BitmapMovieSurfaceView extends SurfaceView implements SurfaceHolder.Callback,Runnable{

    private Canvas canvas;
    private SurfaceHolder sh;
    private Bitmap bitmap;
    private Paint mPaint;
    private Thread thread;
    private boolean flag;   //线程死循环标志位

    private int bitmapHei,bitmapWid;
    private int bitmapX,bitmapY;


    private void init() {
        sh = getHolder();
        sh.addCallback(this);
        bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.dva);
        mPaint = new Paint();
        mPaint.setColor(0xffffffff);
        mPaint.setAntiAlias(true);
        //设置焦点
        setFocusable(true);
    }

    public BitmapMovieSurfaceView(Context context) {
        super(context);
        init();
    }

    public BitmapMovieSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(CustomViewUtils.measureWidth(widthMeasureSpec,0),
                CustomViewUtils.measureHeight(heightMeasureSpec,0));
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        bitmap = BitmapFactory.decodeResource(getResources(),R.mipmap.water);
        bitmapHei = bitmap.getHeight();
        bitmapWid = bitmap.getWidth();
        bitmapX = -bitmapWid+getWidth();
        bitmapY = getHeight()-bitmapHei;
Log.i("TAG","bitmapHei:"+bitmapHei+",bitmapWid:"+bitmapWid+",bitmapX:"+bitmapX+",bitmapY:"+bitmapY);
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
                if(bitmapX >= 0) {
                    bitmapX = -bitmapWid+getWidth();
                }
Log.i("TAG","bitmapX:"+bitmapX+",bitmapHei:"+bitmap.getHeight());
                canvas.drawBitmap(bitmap,bitmapX,bitmapY,mPaint);
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
        bitmapX+=5;
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        flag = false;
    }




}




