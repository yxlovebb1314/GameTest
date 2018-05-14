package game.goldtel.com.gametest.test;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import game.goldtel.com.gametest.R;

/**
 * Created by BS on 2018-4-10.
 * 通过图片剪切让游戏角色动起来
 *
 * 1.先通过4个按键来使图片动起来
 * 2.自己实现一个类似遥感的自定义view，来控制角色移动
 */

public class PlayerSurfaceView extends SurfaceView implements SurfaceHolder.Callback,Runnable{

    private Canvas canvas;
    private SurfaceHolder sh;
    private Bitmap bitmap;
    private Paint mPaint;
    private Thread thread;
    private boolean flag;   //线程死循环标志位

    private int currentFrame = 0;  //当前应该显示的切图部分
    private int viewWid,viewHei;    //view宽高
    private int bitmapHei,bitmapWid;    //图片宽高
    private int frameHei,frameWid;  //每一帧宽高
    private int playerX,playerY;    //表示角色的当前位置
    private int direction = 0; //1,2,3,4表示左，上，右，下方向
    private boolean isMove; //标记player是否在移动,解决点击向左松手后又自动变成向右的方向的问题
    private boolean isLeft; //标记当前图片是向左还是向右,解决向上和向下松手后又变成向右的问题
    private int speed = 15;


    private void init() {
        sh = getHolder();
        sh.addCallback(this);
        mPaint = new Paint();
        mPaint.setColor(0xffffffff);
        mPaint.setAntiAlias(true);
        //设置焦点
        setFocusable(true);
        bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.robot);
        bitmapHei = bitmap.getHeight();
        bitmapWid = bitmap.getWidth();
        frameHei = bitmapHei/2;
        frameWid = bitmapWid/6;


    }

    public PlayerSurfaceView(Context context) {
        super(context);
        init();
    }

    public PlayerSurfaceView(Context context, AttributeSet attrs) {
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
                canvas.drawColor(0xff000000);
                drawFrame(currentFrame,canvas,mPaint);
            }
        }catch(Exception e) {
            e.printStackTrace();
        }finally {
            if(canvas != null)
            sh.unlockCanvasAndPost(canvas);
        }
    }


    private void drawFrame(int currentFrame,Canvas canvas,Paint mPaint) {
        canvas.save();
        canvas.clipRect(playerX,playerY,playerX+frameWid,playerY+frameHei);
        //当前帧的x位置
        int x = -currentFrame%6*frameWid;
        //当前帧的y位置
        int y = 0;
        if(currentFrame>=0 && currentFrame<6) {
           y = 0;
        }else if(currentFrame >=6 && currentFrame<=11) {
           y = -frameHei;
        }
        //如果是向左移动，则进行镜像操作
        if(isLeft) {
            canvas.scale(-1,1,playerX + x + bitmapWid/2,playerY + y+bitmapHei/2);
        }
        canvas.drawBitmap(bitmap,x+playerX,y+playerY,mPaint);
        canvas.restore();
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
        if(isMove) {
            currentFrame++;
            if(currentFrame >= 12) {
                currentFrame = 0;
            }
        }
        switch(direction) {
            case 1:
                if(isMove) {
                    playerX-=speed;
                    if(playerX<=0) {
                        playerX = 0;
                    }
                }
                break;
            case 2:
                if(isMove) {
                    playerY-=speed;
                    if(playerY<=0) {
                        playerY = 0;
                    }
                }
                break;
            case 3:
                if(isMove) {
                    playerX+=speed;
                    if(playerX>=(viewWid-frameWid)) {
                        playerX = viewWid - frameWid;
                    }
                }
                break;
            case 4:
                if(isMove) {
                    playerY+=speed;
                    if(playerY>=(viewHei-frameHei)) {
                        playerY = viewHei - frameHei;
                    }
                }
                break;
        }
    }


    /**
     * 对外提供的改变方向的方法
     * 1,2,3,4 对应 左,上,右,下 、
     * @param direction
     */
    public void setDirection(int direction,boolean isMove,boolean isLeft) {
        this.direction = direction;
        this.isMove = isMove;
        this.isLeft = isLeft;
    }

    /**
     * 重载方法,不含左右的改变方向方法,专门提供给向上和向下
     * @param direction
     * @param isMove
     */
    public void setDirection(int direction,boolean isMove) {
        this.direction = direction;
        this.isMove = isMove;
    }






    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        flag = false;
    }





}




