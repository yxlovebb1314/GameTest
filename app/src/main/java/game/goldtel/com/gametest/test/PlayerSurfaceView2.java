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
import game.goldtel.com.gametest.utils.MathUtils;

/**
 * Created by BS on 2018-4-13.
 * 通过图片剪切让游戏角色动起来
 *
 * 1.先通过4个按键来使图片动起来
 * 2.自己实现一个类似遥感的自定义view，来控制角色移动
 */

public class PlayerSurfaceView2 extends SurfaceView implements SurfaceHolder.Callback,Runnable{

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
    private float playerX,playerY;    //表示角色的当前位置
    private int direction = 0; //1,2,3,4表示右，上，左，下方向，5,6,7,8表示一二三四象限
    private boolean isMove = false; //标记player是否在移动,解决点击向左松手后又自动变成向右的方向的问题
    private boolean isLeft; //标记当前图片是向左还是向右,解决向上和向下松手后又变成向右的问题
    private int speed = 15;
    private int angle;  //当前角色角度


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

    public PlayerSurfaceView2(Context context) {
        super(context);
        init();
    }

    public PlayerSurfaceView2(Context context, AttributeSet attrs) {
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
            movePlayer(direction);
        }
    }

    /**
     * 移动角色
     * @param direction 移动的方向 1,2,3,4 右，上，左，下  5,6,7,8 一二三四象限
     */
    private void movePlayer(int direction) {
        float moveLenX;
        float moveLenY;
        switch(direction) {
            //向右
            case 1:
                playerX += speed;
                if(playerX>=(viewWid-frameWid)) {
                    playerX = viewWid - frameWid;
                }
                break;
            //向上
            case 2:
                playerY -= speed;
                if(playerY<=0) {
                    playerY = 0;
                }
                break;
            //向左
            case 3:
                playerX -= speed;
                if(playerX<=0) {
                    playerX = 0;
                }
                break;
            //向下
            case 4:
                playerY += speed;
                if(playerY>=(viewHei-frameHei)) {
                    playerY = viewHei - frameHei;
                }
                break;
            //向右上 x增大，y减小
            case 5:
                //得到x增大的距离
                moveLenX = MathUtils.getMoveLenX(angle,speed);
                moveLenY = MathUtils.getMoveLenY(angle,speed);
                playerX += moveLenX;
                playerY -= moveLenY;
                if(playerX>=(viewWid-frameWid)) {
                    playerX = viewWid - frameWid;
                }
                if(playerY<=0) {
                    playerY = 0;
                }
                break;
            //向左上 x减小，y减小
            case 6:
                moveLenX = MathUtils.getMoveLenX(angle,speed);
                moveLenY = MathUtils.getMoveLenY(angle,speed);
                playerX -= moveLenX;
                playerY -= moveLenY;
                if(playerX<=0) {
                    playerX = 0;
                }
                if(playerY<=0) {
                    playerY = 0;
                }
                break;
            //向左下 x减小，y增大
            case 7:
                moveLenX = MathUtils.getMoveLenX(angle,speed);
                moveLenY = MathUtils.getMoveLenY(angle,speed);
                playerX -= moveLenX;
                playerY += moveLenY;
                if(playerX<=0) {
                    playerX = 0;
                }
                if(playerY>=(viewHei-frameHei)) {
                    playerY = viewHei - frameHei;
                }
                break;
            //向右下 x增大，y增大
            case 8:
                moveLenX = MathUtils.getMoveLenX(angle,speed);
                moveLenY = MathUtils.getMoveLenY(angle,speed);
                playerX += moveLenX;
                playerY += moveLenY;
                if(playerX>=(viewWid-frameWid)) {
                    playerX = viewWid - frameWid;
                }
                if(playerY>=(viewHei-frameHei)) {
                    playerY = viewHei - frameHei;
                }
                break;
        }
    }




    /**
     * 当用户移动摇杆时产生角度变化，然后进行回调，
     * 主界面再调用setPlayerAngle来设置角度
     * 此时通过角度计算出x，y之间的关系，并标记方向
     * @param angle
     */
    private void setDirection(int angle) {
        if(angle == 0) {
            direction = 1;
            isLeft = false;
        }else if(angle == 90) {
            direction = 2;
        }else if(angle == 180) {
            direction = 3;
            isLeft = true;
        }else if(angle == 270) {
            direction = 4;
        }else if(angle > 0  && angle <90) {
            direction = 5;
            isLeft = false;
        }else if(angle > 90 && angle <180) {
            direction = 6;
            isLeft = true;
        }else if(angle > 180 && angle <270) {
            direction = 7;
            isLeft = true;
        }else if(angle > 270 && angle <360) {
            direction = 8;
            isLeft = false;
        }
    }







    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        flag = false;
    }


    /**
     * 对外部提供的方法
     */

    public void setPlayerAngle(int angle) {
        this.angle = angle;
        if(angle>=0 && angle <360) {
            isMove = true;
            setDirection(angle);
        }else {
            isMove = false;
        }
    }



}




