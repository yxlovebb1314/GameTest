package game.goldtel.com.gametest.test;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by BS on 2018-4-8.
 * 自定义View，一个跟随手指移动的小球
 */

public class MyBallWithFingerView extends View {

    private Paint mPaint;

    private int ballX = 200,ballY=200,radius=50;
    private int viewHei,viewWid;    //控件宽高


    private void init() {
        mPaint = new Paint();
        mPaint.setColor(0xff00dddd);    //画笔颜色
        mPaint.setAntiAlias(true);  //抗锯齿,即除去画面粗糙效果
    }

    public MyBallWithFingerView(Context context) {
        super(context);
        init();
    }

    public MyBallWithFingerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyBallWithFingerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        viewHei = getHeight();
        viewWid = getWidth();
Log.i("TAG","viewHei:"+viewHei+",viewWid:"+viewWid);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(0xff000000);   //设置画布背景色
        canvas.drawCircle(ballX,ballY,radius,mPaint);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        switch(action) {
            case MotionEvent.ACTION_MOVE:
                int x = (int) event.getX();
                int y = (int) event.getY();
                if(x<radius) x=radius;
                if(y<radius) y=radius;
                if(x>(viewWid-radius)) x=viewWid-radius;
                if(y>(viewHei-radius)) y=viewHei-radius;
                ballX = x;
                ballY = y;
                invalidate();
                break;
        }
        return true;
    }
}
















