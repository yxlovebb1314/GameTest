package game.goldtel.com.gametest.test;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by BS on 2018-4-26.
 * 一个可以通过手指缩放图片的surfaceview
 *
 */

public class BigBitmap2SurfaceView extends SurfaceView implements SurfaceHolder.Callback,Runnable {

    private Canvas canvas;
    private SurfaceHolder sh;
    private Bitmap bitmap;
    private Paint mPaint;
    private Thread thread;
    private boolean flag;   //线程死循环标志位
    private int frameNum = 40;   //自定义view帧数
    private Matrix matrix = new Matrix();  //矩阵

    /**
     * 数据部分
     */
    private float bitmapW,bitmapH,screenW,screenH; //bitmap和view宽高
    private float bitmapX,bitmapY;  //当前bitmap的位置
    private float lengthX,lengthY;  //记录手指按下的位置与图片原来位置的宽度高度差
    private float down1X,down1Y,down2X,down2Y;  //记录双指按下时的位置
    private float kPointX,kPointY;   //记录缩放时所参照的点，即按下点的中点
    private float k = 1;    //记录当前图片应缩放的系数
    private float oldK = 1; //表示历史图片被缩放的倍数
    private List<Integer> pointerIdList = new ArrayList<>();

    private void init() {
        sh = getHolder();
        sh.addCallback(this);
        mPaint = new Paint();
        mPaint.setColor(0xffffffff);
        mPaint.setAntiAlias(true);
        //设置焦点
        setFocusable(true);
        setFocusableInTouchMode(true);
    }

    public BigBitmap2SurfaceView(Context context) {
        super(context);
        init();
    }

    public BigBitmap2SurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        flag = true;
        thread = new Thread(this);
        thread.start();
        screenW = getWidth();
        screenH = getHeight();
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
//                    if(fingerNum <= 1) {
//                        canvas.drawBitmap(bitmap,bitmapX,bitmapY,mPaint);
//                    }else if(fingerNum == 2) {
//                        matrix.postScale(k,k,kPointX,kPointY);
////                        matrix.postTranslate(bitmapX,bitmapY);
//                        canvas.drawBitmap(bitmap,matrix,mPaint);
//                    }
                    canvas.scale(k,k,kPointX,kPointY);
                    canvas.drawBitmap(bitmap,bitmapX,bitmapY,mPaint);
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




    /**
     * =============================================
     * 猜想，由于x，y坐标变动很频繁且精确，可否根据/运算来计算
     * 大概手指位置，来表示手指是否有移动等判定
     * =============================================
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int actionMasked = event.getActionMasked();
        /**
         * index通过event.getIndex()方法获得
         * 此数据是表示当前有变化的手指的下标
         * 比如此时有3根手指在屏幕上，抬起一根手指，得到的是
         * 抬起这根手指的下标
         *
         * 接：第一根手指和第二根手指按下，抬起第一根手指
         * 要改变与bitmap的坐标差，index是关键
         * https://www.jianshu.com/p/cafedd319512
         */
        int actionIndex;
        switch(actionMasked) {
            case MotionEvent.ACTION_DOWN:
                actionIndex = event.getActionIndex();
                //第一个手指按下,单指模式
                //记录第一个手指的id
                actionIndex = event.getActionIndex();
                pointerIdList.add(actionIndex);
                //计算图片与手指的坐标差
//                lengthX = (event.getX(actionIndex) - bitmapX)/oldK;
//                lengthY = (event.getY(actionIndex) - bitmapY)/oldK;
                lengthX = (event.getX() - bitmapX*oldK);
                lengthY = (event.getY() - bitmapY*oldK);
//Log.i("TAG","ACTION_DOWN  index:"+actionIndex+",list:"+pointerIdList.toString());
Log.i("TAG","---------------------\n第一根手指按下时计算的差值 length:("+lengthX+","+lengthY+")    \n-------------------");
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                //非主要手指按下，即第二根或以上的手指按下
                actionIndex = event.getActionIndex();
                if(pointerIdList != null && pointerIdList.size() == 1) {
                    //当此时按下的为第二根手指时，才进行保存
                    pointerIdList.add(actionIndex);
                    //保存两根手指按下时的位置
                    down1X = event.getX(event.getPointerId(pointerIdList.get(0)));
                    down1Y = event.getY(event.getPointerId(pointerIdList.get(0)));
                    down2X = event.getX(event.getPointerId(pointerIdList.get(1)));
                    down2Y = event.getY(event.getPointerId(pointerIdList.get(1)));
Log.i("TAG","第一根手指:"+"("+down1X+","+down1Y+")");
Log.i("TAG","第二根手指:"+"("+down2X+","+down2Y+")");
                }
//Log.i("TAG","ACTION_POINTER_DOWN  index:"+actionIndex+",list:"+pointerIdList.toString());
                break;
            case MotionEvent.ACTION_POINTER_UP:
                //两指及以上时，某个手指抬起
                actionIndex = event.getActionIndex();
                //获取抬起手指的id
                removeUpFingerPointerId(actionIndex);
                if(pointerIdList.size() == 1) {
                    try{
                        //当只剩一根手指时，计算当前的图片移动差值
                        lengthX = (event.getX(pointerIdList.get(0)) - bitmapX*oldK);
                        lengthY = (event.getY(pointerIdList.get(0)) - bitmapY*oldK);
Log.i("TAG","---------------------\n第二根手指抬起时计算的差值 length:("+lengthX+","+lengthY+")    \n-------------------");
                    }catch(Exception e) {
                        e.printStackTrace();
                        Log.i("TAG","抛异常");
                    }
                }
                /**
                 * 保存此次缩放操作造成的倍率，下一次缩放操作倍率基于此次倍率进行改变
                 * (因为缩放图片时是以原图大小进行缩放,所以需要保存一个历史倍率,在此基础上来计算新倍率)
                 */
                oldK = k;
//Log.i("TAG","ACTION_POINTER_UP  index:"+actionIndex+",list:"+pointerIdList.toString());
                break;
            case MotionEvent.ACTION_UP:
//Log.i("TAG","单指抬起,bitmapX:"+bitmapX+",bitmapY:"+bitmapY);
                //最后一个手指抬起
                pointerIdList.clear();

//Log.i("TAG","ACTION_UP  list:"+pointerIdList.toString());
                break;
            case MotionEvent.ACTION_MOVE:
                //移动手指
                switch(pointerIdList.size()) {
                    case 1:
                        //单指移动
                        oneFingerMove(event);
                        break;
                    case 2:
                        //双指移动
                        twoFingersMove(event);
                        break;
                }
                break;
        }
        return true;
    }



    /**
     * 抬起手指时，删除list中抬起手指的actionIndex
     */
    private void removeUpFingerPointerId(int actionIndex) {
        if(pointerIdList != null && pointerIdList.size()>0) {
            for(int i=0;i<pointerIdList.size();i++) {
                int p = pointerIdList.get(i);
                if(p == actionIndex) {
                    pointerIdList.remove(i);
                    break;
                }
            }
        }
    }




    /**
     * 当图片在非居中位置时，每次调整一点，最终调整至
     * 居中位置，每次调整一点是为了实现一个平移动画效果
     */
    private void changeBitmapPosition() {

    }

    /**
     * 计算两点中心点位置
     */
    private void getkPointPosition(float x1,float y1,float x2,float y2) {
        kPointX = (x1+x2)/2;
        kPointY = (y1+y2)/2;
    }


    /**
     * 获取缩放倍率，用过按下的距离与当前距离来计算
     * 缩放倍率=当前距离/按下的距离
     */
    private void getK(float x1,float y1,float x2,float y2) {
        //计算按下时的距离
        float downLength = (float)Math.sqrt
                ((down2X - down1X)*(down2X - down1X) + (down2Y - down1Y)*(down2Y - down1Y));
Log.i("TAG","按下时的距离:"+(int)downLength);
        //计算当前距离
        float nowLength = (float)Math.sqrt
                ((x2 - x1)*(x2 - x1) + (y2 - y1)*(y2 - y1));
Log.i("TAG","当前的距离:"+(int)nowLength);
        //得到当前应缩放的倍率
        k = nowLength / downLength;
        //计算实际倍率
        k = oldK * k;
Log.i("TAG","得到的k:"+k);
    }

    /**
     * 单指移动
     */
    private void oneFingerMove(MotionEvent event) {
        //获取到当前手指的坐标
        float x = event.getX(0);
        float y = event.getY(0);
        bitmapX = (x - lengthX)/oldK;
        bitmapY = (y - lengthY)/oldK;
Log.i("TAG","=====================================");
Log.i("TAG","单指移动: bitmap:("+(int)bitmapX+","+(int)bitmapY+")");
Log.i("TAG","手指坐标: finger:("+(int)x+","+(int)y+")");
Log.i("TAG","坐标差值: length:("+(int)lengthX+","+(int)lengthY+")");
    }


    /**
     * 双指移动
     */
    private void twoFingersMove(MotionEvent event) {
        float x1 = event.getX(pointerIdList.get(0));
        float y1 = event.getY(pointerIdList.get(0));
        float x2 = event.getX(pointerIdList.get(1));
        float y2 = event.getY(pointerIdList.get(1));
Log.i("TAG","按下时，第一根手指:"+"("+down1X+","+down1Y+",第二根手指:"+"("+down2X+","+down2Y+")");
Log.i("TAG","当前的，第一根手指:"+"("+x1+","+y1+",第二根手指:"+"("+x2+","+y2+")");
        if(pointerIdList != null && pointerIdList.size()==2) {
            //获取到中心点位置
            getkPointPosition(x1,y1,x2,y2);
        }
        getK(x1,y1,x2,y2);
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

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
        bitmapW = bitmap.getWidth();
        bitmapH = bitmap.getHeight();
    }

}















/*@Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        int fingerCount = event.getPointerCount();

        if(fingerCount == 1) {
            //单指操作
            fingerNum = 1;
            //单指，进行移动操作
            switch(action) {
                case MotionEvent.ACTION_DOWN:
                    lengthX = event.getX() - bitmapX;
                    lengthY = event.getY() - bitmapY;
                    break;
                case MotionEvent.ACTION_MOVE:
                    bitmapX = event.getX() - lengthX;
                    bitmapY = event.getY() - lengthY;
                    break;
                case MotionEvent.ACTION_UP:
                    fingerNum = 0;
                    break;
            }
        }else if(fingerCount == 2) {
            fingerNum = 2;
            //双指，进行缩放操作
            switch(event.getActionMasked()) {
                case MotionEvent.ACTION_POINTER_DOWN:
Log.i("TAG","ACTION_POINTER_DOWN  第一个手指按住的情况下，按下第二个手指");
                    //记录两指按下的位置
                    down1X = event.getX(event.getPointerId(0));
                    down1Y = event.getY(event.getPointerId(0));
                    down2X = event.getX(event.getPointerId(1));
                    down2Y = event.getY(event.getPointerId(1));
//Log.i("TAG","down1X:"+down1X+",down1Y:"+down1Y+",down2X:"+down2X+",down2Y:"+down2Y);
                    break;
                case MotionEvent.ACTION_MOVE:
                    finger1X = event.getX(event.getPointerId(0));
                    finger1Y = event.getY(event.getPointerId(0));
                    finger2X = event.getX(event.getPointerId(1));
                    finger2Y = event.getY(event.getPointerId(1));
//Log.i("TAG","点1("+x1+","+y1+")  点2("+x2+","+y2+")");
                    //获取中心点位置
                    getkPointPosition(finger1X,finger1Y,finger2X,finger2Y);
//Log.i("TAG","中心点:("+kPointX+","+kPointY+")");
                    //获取缩放倍率
                    getK(finger1X,finger1Y,finger2X,finger2Y);
                    break;
                case MotionEvent.ACTION_POINTER_UP:
Log.i("TAG","ACTION_POINTER_UP  两指按住时，抬起一个手指");
                    int actionIndex = event.getActionIndex();
                    fingerNum = 1;
                    break;
            }
        }


        return true;
    }*/
