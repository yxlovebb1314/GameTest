package game.goldtel.com.gametest.test;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by BS on 2018-4-9.
 * 使用SurfaceView来实现随手指移动的小球
 */

public class MyBallWithFingerSurfaceView extends SurfaceView implements SurfaceHolder.Callback{

    private Paint mPaint;
    private SurfaceHolder sh;
    private Canvas canvas;

    /**
     * 数据部分
     */
    private int ballX=100,ballY=100,radius=50;
    private int viewHei,viewWid;

    private void init() {
        mPaint = new Paint();
        //设置画笔参数
        mPaint.setColor(0xffffffff);
        mPaint.setAntiAlias(true);  //抗锯齿
        //获取SurfaceHolder
        sh = getHolder();
        //给surfaceview添加状态监听
        sh.addCallback(this);
    }

    public MyBallWithFingerSurfaceView(Context context) {
        super(context);
        init();
    }

    public MyBallWithFingerSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //获取整个控件宽高
        viewHei = getHeight();
        viewWid = getWidth();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        myDraw();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int)event.getX();
        int y = (int)event.getY();
        if(x<radius) x=radius;
        if(y<radius) y=radius;
        if(x>viewWid-radius) x=viewWid-radius;
        if(y>viewHei-radius) y=viewHei-radius;
        ballX = x;
        ballY = y;
        myDraw();
        return true;
    }

    /**
     * 绘画部分
     */
    public void myDraw() {
        canvas = sh.lockCanvas();
        canvas.drawColor(0xff000000);
        canvas.drawCircle(ballX,ballY,radius,mPaint);
        sh.unlockCanvasAndPost(canvas);
    }

















}








