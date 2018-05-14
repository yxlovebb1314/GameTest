package game.goldtel.com.gametest.test;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import game.goldtel.com.gametest.R;

/**
 * Created by BS on 2018-4-9.

 * 由于画布的旋转看着不明显，所以绘制了两根线
 *
 * 重点：在画完一个图形后，旋转画布，再画的时候，之前画好的图形不会随着画布的旋转而旋转
 * 画布的旋转只影响后面的绘画!!
 */

public class MyBitmapSurfaceView extends SurfaceView implements SurfaceHolder.Callback,Runnable{

    private Canvas canvas;
    private SurfaceHolder sh;
    private Bitmap bitmap,bitmap2;
    private Paint mPaint;
    private Thread thread;
    private boolean flag;   //线程死循环标志位
    private Matrix mx = new Matrix();   //矩阵
    private int viewHei,viewWid;

    private void init() {
        sh = getHolder();
        sh.addCallback(this);
        bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        bitmap2 = BitmapFactory.decodeResource(getResources(),R.mipmap.publish_in_pond);
        mPaint = new Paint();
        mPaint.setColor(0xffffffff);
        mPaint.setAntiAlias(true);
//        mPaint.setStrokeWidth(2);
        //设置焦点
        setFocusable(true);
    }

    public MyBitmapSurfaceView(Context context) {
        super(context);
        init();
    }

    public MyBitmapSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        flag = true;
        thread = new Thread(this);
        thread.start();
        viewHei = getHeight();
        viewWid = getWidth();
    }


    /**
     * 绘画方法
     */
    public void myDraw() {
        try{
            canvas = sh.lockCanvas();
            if(canvas != null) {
                /**
                 * 绘制x轴和y轴线
                 */
                canvas.drawColor(Color.BLACK);
                canvas.drawLine(0,0,viewWid,0,mPaint);
                canvas.drawLine(0,0,0,viewHei,mPaint);
                /**
                 * 绘制简单bitmap
                 */
//                canvas.drawBitmap(bitmap,50,50,mPaint);
                /**
                 * 旋转画布绘制bitmap达到旋转bitmap的效果
                 */
                /*canvas.rotate(30,bitmap.getWidth()/2,bitmap.getHeight()/2); //画布旋转30°，围绕图片的中心点旋转
                canvas.drawBitmap(bitmap,50,50,mPaint);
                canvas.drawBitmap(bitmap,200,200,mPaint);*/
                /**
                 * 通过save和restore来操控旋转
                 */
                /*canvas.drawBitmap(bitmap,50,0,mPaint);
                canvas.save();
                canvas.rotate(30);
                canvas.drawBitmap(bitmap,250,0,mPaint);
                canvas.restore();
                canvas.drawBitmap(bitmap,400,0,mPaint);*/
                /**
                 * 通过矩阵Matrix实现旋转效果
                 * postTranslate方法实现平移效果
                 */
                /*mx.reset();
                mx.postRotate(30,bitmap.getWidth()/2,bitmap.getHeight()/2);
                mx.postTranslate(100,100);  //矩阵平移
                canvas.drawBitmap(bitmap,mx,mPaint);*/
                /**
                 * 通过平移画布达到平移位图的效果
                 */
                /*canvas.save();
                canvas.translate(100,100);  //X,Y轴平移的距离
                canvas.drawBitmap(bitmap,0,0,mPaint);
                canvas.restore();*/
                /**
                 * 通过缩放画布来实现缩放位图的效果
                 */
                /*canvas.save();
                canvas.scale(2f,2f,0,0);    //前两个参数是X,Y轴缩放的比例，后两个是一组，即画布相对于那个点进行缩放
                canvas.drawBitmap(bitmap,50,50,mPaint);
                canvas.restore();
                canvas.drawBitmap(bitmap,50,50,mPaint);*/
                /**
                 * 通过缩放画布实现镜像效果
                 */
                /*canvas.drawBitmap(bitmap2,50,50,mPaint);
                canvas.save();
                //X轴镜像,
                canvas.scale(-1,1,0,0);
                canvas.drawBitmap(bitmap2,-(50+bitmap2.getWidth()*2),50,mPaint);
                canvas.restore();
                //Y轴镜像
                canvas.save();
                canvas.scale(1,-1);
                canvas.drawBitmap(bitmap2,50,-(50+bitmap2.getHeight()*2),mPaint);
                canvas.restore();
                //X,Y轴镜像
                canvas.save();
                canvas.scale(-1,-1);
                canvas.drawBitmap(bitmap2,-(50+bitmap2.getWidth()*2),-(50+bitmap2.getHeight()*2),mPaint);
                canvas.restore();*/
                /**
                 * 通过矩阵实现镜像效果
                 */
                //X轴镜像
                canvas.drawBitmap(bitmap2,50,50,mPaint);
                canvas.save();
                mx.reset();
                mx.postScale(-1,1);
                mx.postTranslate(50+bitmap2.getWidth()*2,50);
                canvas.drawBitmap(bitmap2,mx,mPaint);
                canvas.restore();
                //Y轴镜像
                canvas.save();
                mx.reset();
                mx.postScale(1,-1);
                mx.postTranslate(50,60+bitmap2.getHeight()*2);
                canvas.drawBitmap(bitmap2,mx,mPaint);
                canvas.restore();
                //X,Y轴镜像
                canvas.save();
                mx.reset();
                mx.postScale(-1,-1);
                mx.postTranslate(50+bitmap2.getWidth()*2,60+bitmap2.getHeight()*2);
                canvas.drawBitmap(bitmap2,mx,mPaint);
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




