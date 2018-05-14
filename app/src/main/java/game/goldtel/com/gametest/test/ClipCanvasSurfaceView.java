package game.goldtel.com.gametest.test;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Toast;

import game.goldtel.com.gametest.R;

/**
 * Created by BS on 2018-4-10.
 * 通过剪切实现画布的可视区域
 */

public class ClipCanvasSurfaceView extends SurfaceView implements SurfaceHolder.Callback,Runnable{

    private Canvas canvas;
    private SurfaceHolder sh;
    private Bitmap bitmap,bitmap2;
    private Paint mPaint;
    private Thread thread;
    private boolean flag;   //线程死循环标志位

    private Path path;
    private Rect rect;

    private void init() {
        sh = getHolder();
        sh.addCallback(this);
        bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.dva);
        mPaint = new Paint();
        mPaint.setColor(0xffffffff);
        mPaint.setAntiAlias(true);
//        mPaint.setStrokeWidth(2);
        //设置焦点
        setFocusable(true);
        path = new Path();
        rect = new Rect(20,200,300,500);
    }

    public ClipCanvasSurfaceView(Context context) {
        super(context);
        init();
    }

    public ClipCanvasSurfaceView(Context context, AttributeSet attrs) {
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
                /**
                 * 通过矩形设置可视区域
                 */
                canvas.save();
                canvas.clipRect(0,0,150,150);   //设置可视区域
                canvas.drawBitmap(bitmap,0,0,mPaint);
                canvas.restore();
                /**
                 * 通过Path设置可视区域
                 */
                canvas.save();
                path.addCircle(350,150,100, Path.Direction.CCW);
                canvas.clipPath(path);
                canvas.drawBitmap(bitmap,0,0,mPaint);
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

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        flag = false;
    }




}




