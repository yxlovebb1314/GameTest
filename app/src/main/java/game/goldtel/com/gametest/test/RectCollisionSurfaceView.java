package game.goldtel.com.gametest.test;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by BS on 2018-4-11.
 * 矩形碰撞
 * 圆形碰撞类似，即两点之间距离小于等于两个半径之和即为碰撞
 * 两点间距离公式:根号下(x2-x1的平方+y2-y1的平方)
 */

public class RectCollisionSurfaceView extends SurfaceView implements SurfaceHolder.Callback,Runnable{

    private Canvas canvas;
    private SurfaceHolder sh;
    private Bitmap bitmap;
    private Paint mPaint;
    private Thread thread;
    private boolean flag;   //线程死循环标志位

    private int r1x,r1y,r1Wid,r1Hei;    //第一个矩形的xy和宽高
    private int r2x,r2y,r2Wid,r2Hei;    //第二个矩形的xy和宽高

    private boolean isCollision;    //是否碰撞

    private void init() {
        sh = getHolder();
        sh.addCallback(this);
        mPaint = new Paint();
        mPaint.setColor(0xffffffff);
        mPaint.setAntiAlias(true);
        //设置焦点
        setFocusable(true);
    }

    public RectCollisionSurfaceView(Context context) {
        super(context);
        init();
    }

    public RectCollisionSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        flag = true;
        thread = new Thread(this);
        thread.start();
        //设置两矩形的大小和位置
        r1x = 0;
        r1y = 0;
        r1Hei = 300;
        r1Wid = 300;
        r2Wid = 200;
        r2Hei = 200;
        r2x = getWidth()/2 -  r2Wid/2;
        r2y = getHeight()/2 - r2Hei/2;
    }


    /**
     * 绘画方法
     */
    public void myDraw() {
        try{
            canvas = sh.lockCanvas();
            if(canvas != null) {
                canvas.drawColor(0xff000000);
                mPaint.setColor(Color.RED);
                canvas.drawRect(r2x,r2y,r2x+r2Wid,r2y+r2Hei,mPaint);
                if(!isCollision) {
                    mPaint.setColor(0xffffffff);
                }
                canvas.drawRect(r1x,r1y,r1x+r1Wid,r1y+r1Hei,mPaint);
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
            if(end-start<16) {
                try {
                    Thread.sleep(16-(end-start));
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
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int)event.getX();
        int y = (int)event.getY();
        r1x = x - r1Wid/2;  //让矩形中心在手指的位置
        r1y = y - r1Hei/2;
        isCollision = isCollision(r1x,r1y,r1Wid,r1Hei,r2x,r2y,r2Wid,r2Hei);
        return true;
    }

    /**
     * 检测两矩形是否碰撞
     * 自己的算法：第一个矩形在第二个矩形的四周绕动所形成的轨迹，只要第一个矩形完全在轨迹内，则为碰撞
     * @return 是否碰撞
     */
    private boolean isCollision(int r1x,int r1y,int r1Wid, int r1Hei,int r2x,int r2y,int r2Wid, int r2Hei) {
        //算出轨迹的四周位置
        int left = r2x - r1Wid;
        int top = r2y - r1Hei;
        int right = r2x + r2Wid + r1Wid;
        int bottom = r2y + r2Hei + r1Hei;
        if(r1x>=left && r1y>=top && (r1x+r1Wid)<=right && (r1y+r1Hei)<=bottom) {
            return true;
        }else {
            return false;
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




