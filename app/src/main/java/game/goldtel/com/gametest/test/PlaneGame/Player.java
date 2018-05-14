package game.goldtel.com.gametest.test.PlaneGame;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;

/**
 * Created by BS on 2018-4-20.
 * 角色
 */

public class Player {

    //血量
    private int playerHp = 3;
    //血量图片
    private Bitmap bmpPlayerHp;
    //游戏角色图片
    private Bitmap bmpPlayer;
    //游戏角色坐标
    public float playerX,playerY;
    //游戏角色宽高
    public float playerW,playerH;
    //角色移动速度
    public float speed;
    //角色移动标识
    private boolean isUp,isDown;
    /**
     * 碰撞后处于无敌时间
     */
    //计时器
    private int noCollisionCount = 0;
    //无敌时间
    private int noCollisionTime = 60;
    //是否碰撞
    private boolean isCollision;


    public Player (Bitmap bmpPlayer,Bitmap bmpPlayerHp) {
        this.bmpPlayer = bmpPlayer;
        this.bmpPlayerHp = bmpPlayerHp;
        playerX = PlaneGameSurfaceView.screenW/2 - bmpPlayer.getWidth()/2;
        playerY = PlaneGameSurfaceView.screenH - 200;
        playerW = bmpPlayer.getWidth();
        playerH = bmpPlayer.getHeight();
    }

    public void draw(Canvas canvas, Paint paint) {
        //绘制主角,当为无敌状态时，让主角闪烁
        if(isCollision) {
            if(noCollisionCount % 2 == 0) {
                canvas.drawBitmap(bmpPlayer,playerX,playerY,paint);
            }
        }else {
            canvas.drawBitmap(bmpPlayer,playerX,playerY,paint);
        }
        //绘制血量
        for(int i=0;i<playerHp;i++) {
            canvas.drawBitmap(bmpPlayerHp,i*bmpPlayerHp.getWidth()+10
                    ,PlaneGameSurfaceView.screenH - bmpPlayerHp.getHeight() - 10,paint);
        }

    }



    float tempX = 0,tempY = 0;    //手指按下位置与飞机位置的差值
    float downX,downY;    //按下的位置
    public void onTouchEvent(MotionEvent event) {
        /**
         * 记录手指按下的位置，通过计算移动的距离与按下的距离，来计算飞机应该移动的距离
         * 让飞机跟随手指轨迹移动
         */
        int action = event.getAction();
        switch(action) {
            case MotionEvent.ACTION_DOWN:
                downX = event.getX();
                downY = event.getY();
                tempX = downX - playerX;
                tempY = downY - playerY;
                break;
            case MotionEvent.ACTION_MOVE:
                float x = event.getX();
                float y = event.getY();
                playerX = x - tempX;
                playerY = y - tempY;
                /**
                 * 当飞机移动到屏幕边缘时，不能让其出界，此时手指继续移动
                 * 则temp值会缩短
                 */
                if(playerX <= 0) {
                    playerX = 0;
                    tempX = x - playerX;
                }
                if(playerX >= PlaneGameSurfaceView.screenW - bmpPlayer.getWidth()) {
                    playerX = PlaneGameSurfaceView.screenW - bmpPlayer.getWidth();
                    tempX = x - playerX;
                }
                if(playerY <= 0) {
                    playerY = 0;
                    tempY = y - playerY;
                }
                if(playerY >= PlaneGameSurfaceView.screenH - bmpPlayer.getHeight()) {
                    playerY = PlaneGameSurfaceView.screenH - bmpPlayer.getHeight();
                    tempY = y - playerY;
                }
                break;
            case MotionEvent.ACTION_UP:

                break;
        }

    }


    public void logic() {
        //处理无敌状态
        if(isCollision) {
            //计时器开始计时
            noCollisionCount++;
            if(noCollisionCount >= noCollisionTime) {
                //无敌状态消失后，初始化计时数据
                isCollision = false;
                noCollisionCount = 0;
            }
        }


    }




    public void setPlayerHp(int hp) {
        this.playerHp = hp;
    }

    public int getPlayerHp() {
        return playerHp;
    }

    /**
     * 判断主角与敌人的碰撞
     */
    public boolean isCollsionWith(Enemy en) {
        //是否处于碰撞状态
        if(!isCollision) {
            //没有碰撞则判断碰撞
            float x2 = en.x;
            float y2 = en.y;
            int w2 = en.frameW;
            int h2 = en.frameH;
            if(playerX>x2-playerW+15 && playerX<x2+w2-15
                    && playerY>y2-playerH+15 && playerY<y2+h2-15 ) {
                //碰撞即进入无敌状态
                isCollision = true;
                return true;
            }
            return false;
        }else {
            return false;
        }
    }


    /**
     * 判断主角与子弹的碰撞
     */
    public boolean isCoosionWith(Bullet bullet) {
        //是否处于无敌时间
        if(!isCollision) {
            float x2 = bullet.bulletX;
            float y2 = bullet.bulletY;
            float w2 = bullet.bmpBullet.getWidth();
            float h2 = bullet.bmpBullet.getHeight();
            if(playerX>x2-playerW+15 && playerX<x2+w2-15
                    && playerY>y2-playerH+15 && playerY<y2+h2-15 ) {
                //碰撞即进入无敌状态
                isCollision = true;
                return true;
            }
            return false;
        }else {
            return false;
        }
    }



}

























