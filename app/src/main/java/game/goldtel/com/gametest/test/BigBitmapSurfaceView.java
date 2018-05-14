package game.goldtel.com.gametest.test;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import game.goldtel.com.gametest.biz.BigBitmapBiz;

/**
 * Created by BS on 2018-4-24.
 * 通过手指的缩放操作来实现图片的缩放
 * 总结：
 * 1.缩放时，bitmap大小从始至终都未改变，不管使用矩阵缩放还是画布缩放
 * 2.在缩放后，计算画布上坐标时，如果按照矩阵缩放，则图片实际高度应该按照缩放后的来计算，画布上坐标不改变
 *   如果按照画布缩放，那么画布上每一个坐标点都是被缩放过的，而bitmap宽高未改变
 */

public class BigBitmapSurfaceView extends SurfaceView implements SurfaceHolder.Callback,Runnable{

    private Canvas canvas;
    private SurfaceHolder sh;
    private Bitmap bitmap;
    private Paint mPaint;
    private Thread thread;
    private boolean flag;   //线程死循环标志位
    private int frameNum = 30;   //自定义view帧数

    /**
     * 数据部分
     */
//    private float bitmapW,bitmapH;  //图片宽高
//    private float screenW,screenH;  //view宽高
//    //view宽高比与位图宽高比
//    float proportionScreen;
//    float proportionBitmap;
//    private float x,y;  //drawBitmap时的坐标
//    private float oldX,oldY;  //记录手指上一次记录的位置

    private void init() {
        sh = getHolder();
        sh.addCallback(this);
        mPaint = new Paint();
        mPaint.setColor(0xffffffff);
        mPaint.setAntiAlias(true);
        //设置焦点
        setFocusable(true);
    }

    public BigBitmapSurfaceView(Context context) {
        super(context);
        init();
    }

    public BigBitmapSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        flag = true;
        thread = new Thread(this);
        thread.start();
        //获取屏幕宽高
        /*screenW = getWidth();
        screenH = getHeight();
Log.i("TAG","屏幕宽:"+screenW+",屏幕高:"+screenH);*/
        BigBitmapBiz.screenW = getWidth();
        BigBitmapBiz.screenH = getHeight();
        if(BigBitmapBiz.mode == BigBitmapBiz.MODE_MATRIX2) {
            Matrix matrix = new Matrix();
            matrix.setScale((float)getWidth()/(float)bitmap.getWidth()
                    ,(float)getWidth()/(float)bitmap.getWidth());
            BigBitmapBiz.bitmap = Bitmap.createBitmap
                    (bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),matrix,true);
Log.i("TAG","screenW:"+getWidth()+",bitmapW:"+BigBitmapBiz.bitmap.getWidth());
            BigBitmapBiz.bitmapH = BigBitmapBiz.bitmap.getHeight();
            BigBitmapBiz.bitmapW = BigBitmapBiz.bitmap.getWidth();
        }
    }


    /**
     * 绘画方法
     */
    public void myDraw() {
        try{
            canvas = sh.lockCanvas();
            if(canvas != null) {
                if(bitmap != null) {
                    canvas.drawColor(Color.BLACK);
                    canvas.save();
                    /**
                     * 比较view与位图的  宽度/高度
                     * 如果位图比例更小，则是长图，横向填满view，按照横向填满比率来伸缩Y轴,缩放点为左上角
                     * 如果view比例更小，则是宽图，横向填满view，按照横向填满比率来伸缩Y轴，缩放点为位图中心点
                     * 区分是否是长宽图的目的在于，如果是长图，则可以拖动
                     * 在绘画时无区别
                     */
                    /*//先获取到图片宽高与屏幕宽高比  始终以 屏幕数据/图片数据 来比较
                    proportionScreen = screenW / screenH;
                    proportionBitmap = bitmapW / bitmapH;
                    if(proportionBitmap < proportionScreen) {
                        canvas.scale(screenW/bitmapW,screenW/bitmapW,0,0);
                        canvas.setDrawFilter(new PaintFlagsDrawFilter
                                (0, Paint.ANTI_ALIAS_FLAG|Paint.FILTER_BITMAP_FLAG));
                        canvas.drawBitmap(bitmap,x,y,mPaint);
                    }else {
                        canvas.scale(screenW/bitmapW,screenW/bitmapW,bitmapW/2,bitmapH/2);
                        canvas.drawBitmap(bitmap,0,screenH/2 - bitmapH/2,mPaint);
                    }*/
                    BigBitmapBiz.draw(canvas,mPaint);
                    canvas.restore();
                }
            }
        }catch(Exception e) {
            e.printStackTrace();
        }finally {
            if(canvas != null)
            sh.unlockCanvasAndPost(canvas);
        }
    }




    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return BigBitmapBiz.onTouchEvent(event);
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


    /**
     * 对外提供的方法
     */
    public void setBitmap(Bitmap bitmap) {
       /* this.bitmap = bitmap;
        //获取图片宽高
        bitmapW = bitmap.getWidth();
        bitmapH = bitmap.getHeight();
Log.i("TAG","初始图片宽："+bitmapW+",初始图片高:"+bitmapH);*/
        this.bitmap = bitmap;
        BigBitmapBiz.bitmap = bitmap;
        BigBitmapBiz.bitmapW = bitmap.getWidth();
        BigBitmapBiz.bitmapH = bitmap.getHeight();

    }


}




