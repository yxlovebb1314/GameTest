package game.goldtel.com.gametest.test;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import game.goldtel.com.gametest.R;
import game.goldtel.com.gametest.entity.ControlBarEntity;
import game.goldtel.com.gametest.utils.MathUtils;

/**
 * Created by BS on 2018-4-12.
 * 自己实现一个摇杆SurfaceView
 */

public class ControlBarSurfaceView extends SurfaceView implements SurfaceHolder.Callback,Runnable{

    private Canvas canvas;
    private SurfaceHolder sh;
    private Bitmap bitmap;
    private Paint mPaint;
    private Paint paint;
    private Thread thread;
    private boolean flag;   //线程死循环标志位

    /**
     * 数据部分
     */
    private int viewHei,viewWid;    //控件宽高
    private int bCircleRadius,sCircleRadius;    //大小圆半径
    private int bCircleX,bCircleY,sCircleX,sCircleY;    //大小圆圆心位置
    private float lineSlope;    //x，y在坐标轴的斜率，用于表示方向
    private ControlBarEntity controlBarEntity;    //存放小圆的x，y值和斜率
    private boolean isBarMoving = false;

    /**
     * 对外部提供的回调接口
     */
    private ControlBarAngleChangeListener listener;

    private void init() {
        sh = getHolder();
        sh.addCallback(this);
        //使背景透明
        setZOrderOnTop(true);
        sh.setFormat(PixelFormat.TRANSLUCENT);
        mPaint = new Paint();
//        mPaint.setColor(0x22ffffff);
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        paint = new Paint();
        paint.setColor(0x00ffffff);
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
        //设置焦点
        setFocusable(true);
    }

    public ControlBarSurfaceView(Context context) {
        super(context);
        init();
    }

    public ControlBarSurfaceView(Context context, AttributeSet attrs) {
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
        //初始化圆形数据
        bCircleRadius = 150;
        sCircleRadius = 55;
        bCircleX = 150 + bCircleRadius;
        bCircleY = viewHei - 150 - bCircleRadius;
        sCircleX = bCircleX;
        sCircleY = bCircleY;
        //初始化图片数据
        bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.robot);
    }


    /**
     * 绘画方法
     */
    public void myDraw() {
        try{
            canvas = sh.lockCanvas();
            if(canvas != null) {
                //设置整张画布为透明色
                canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
                //设置大圆画笔颜色
                mPaint.setColor(0x2fffffff);
                //画大圆
                canvas.drawCircle(bCircleX,bCircleY,bCircleRadius,mPaint);
                //设置小圆画笔颜色
                mPaint.setColor(0x0fffffff);
                //画小圆
                canvas.drawCircle(sCircleX,sCircleY,sCircleRadius,mPaint);
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
        int action = event.getAction();
        if(controlBarEntity == null) {
            controlBarEntity = new ControlBarEntity();
        }
        float x = 0;
        float y = 0;
        switch(action) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                x = event.getX();
                y = event.getY();
                break;
            case MotionEvent.ACTION_UP:
                x = bCircleX;
                y = bCircleY;
                break;
        }
        //计算角度
        int angle = MathUtils.getTwoPointAngle(bCircleX,bCircleY,(int)x,(int)y);
        controlBarEntity.setAngle(angle);
        //获取两圆心间距离
        int nowLength = (int)Math.sqrt(Math.pow(x - bCircleX, 2) + Math.pow(y - bCircleY, 2));
        //当手指在大圆内时,即两圆心间距离<=大圆半径-小圆半径
        if(nowLength <= (bCircleRadius - sCircleRadius)) {
            sCircleX = (int)x;
            sCircleY = (int)y;
            controlBarEntity.setX(sCircleX);
            controlBarEntity.setY(sCircleY);
        }else {
            //当手指在大圆外时，通过角度和半径来计算小圆x和y的值
            sCircleX = MathUtils.getMarginX(angle,bCircleRadius,bCircleX);
            sCircleY = MathUtils.getMarginY(angle,bCircleRadius,bCircleY);
            controlBarEntity.setX(sCircleX);
            controlBarEntity.setY(sCircleY);
        }

        return true;
    }

    @Override
    public void run() {
        while(flag) {
            long start = System.currentTimeMillis();
            myDraw();
            logic();
            long end = System.currentTimeMillis();
            if(end-start<30) {
                try {
                    Thread.sleep(30-(end-start));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 业务逻辑
     */
    /**
     * 用-1来表示用户已抬起手指，但是只想返回一次-1，而不是每一帧都返回，所以用标记位来判断
     * 所以用临时变量来判断多个连续-1时，只返回一个来表示用户已抬起手指
     */
    int tempAngle = -2;
    private void logic() {
        if(listener != null) {
            if(controlBarEntity != null) {
                int an = controlBarEntity.getAngle();
                if(an == tempAngle && tempAngle == -1) {

                }else {
                    listener.getAngle(controlBarEntity.getAngle());
                }
                tempAngle = an;
            }
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        flag = false;
    }


    public void setOnAngleChanged(ControlBarAngleChangeListener listener) {
        this.listener = listener;
    }

    public interface ControlBarAngleChangeListener {
        public void getAngle(int angle);
    }


}




