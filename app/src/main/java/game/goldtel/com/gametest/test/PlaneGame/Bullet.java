package game.goldtel.com.gametest.test.PlaneGame;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

import game.goldtel.com.gametest.test.PlaneGame.interFace.CanDeadObj;

/**
 * Created by BS on 2018-4-23.
 */

public class Bullet implements CanDeadObj{

    //子弹图片
    public Bitmap bmpBullet;
    //子弹坐标
    public float bulletX,bulletY;
    //子弹速度
    public float speed;
    //子弹种类以及常量
    public int bulletType;
    //主角子弹
    public static final int BULLET_PLAYER = -1;
    //鸭子子弹
    public static final int BULLET_DUCK = 1;
    //苍蝇子弹
    public static final int BULLET_FLY = 2;
    //Boss子弹
    public static final int BULLET_BOSS = 3;
    //子弹是否超出屏幕
    public boolean isDead;

    //Boss疯狂状态下子弹相关成员变量
    private int dir;    //当前boss子弹方向
    //8方向常量
    public static final int DIR_UP = -1;
    public static final int DIR_DOWN = 2;
    public static final int DIR_LEFT = 3;
    public static final int DIR_RIGHT = 4;
    public static final int DIR_UP_LEFT = 5;
    public static final int DIR_UP_RIGHT = 6;
    public static final int DIR_DOWN_LEFT = 7;
    public static final int DIR_DOWN_RIGHT = 8;


    public Bullet(Bitmap bmpBullet,float bulletX,float bulletY,int bulletType) {
        this.bmpBullet = bmpBullet;
        this.bulletX = bulletX;
        this.bulletY = bulletY;
        this.bulletType = bulletType;
        int tempSpeed = 8;
        //不同的子弹类型速度不一样
        switch (bulletType) {
            case BULLET_PLAYER:
                speed = 44;
                break;
            case BULLET_DUCK:
                speed = 3+tempSpeed;
                break;
            case BULLET_FLY:
                speed = 4+tempSpeed;
                break;
            case BULLET_BOSS:
                speed = 5+tempSpeed;
                break;
        }
    }


    /**
     * 专用于处理Boss疯狂状态下创建的子弹
     */
    public Bullet(Bitmap bmpBullet, float bulletX, float bulletY, int bulletType, int dir) {
        this.bmpBullet = bmpBullet;
        this.bulletX = bulletX;
        this.bulletY = bulletY;
        this.bulletType = bulletType;
        speed = 5;
        this.dir = dir;
    }


    //子弹绘制
    public void draw(Canvas canvas, Paint paint) {
        canvas.drawBitmap(bmpBullet,bulletX,bulletY,paint);
    }


    public void logic() {
        //不同类型子弹逻辑不一样
        //主角子弹垂直向上
        switch(bulletType) {
            case BULLET_PLAYER:
                bulletY -= speed;
                if(bulletY <= 50) {
                    isDead = true;
                }
                break;
            //鸭子和苍蝇的垂直向下
            case BULLET_FLY:
            case BULLET_DUCK:
                bulletY += speed;
                if(bulletY > PlaneGameSurfaceView.screenH) {
                    isDead = true;
                }
                break;
            //Boss疯狂状态下的8方向子弹逻辑
            case BULLET_BOSS:
//Boss疯狂状态下的子弹逻辑待实现
                switch (dir) {
                    //方向上的子弹
                    case DIR_UP:
                        bulletY -= speed;
                        break;
                    //方向下的子弹
                    case DIR_DOWN:
                        bulletY += speed;
                        break;
                    //方向左的子弹
                    case DIR_LEFT:
                        bulletX -= speed;
                        break;
                    //方向右的子弹
                    case DIR_RIGHT:
                        bulletX += speed;
                        break;
                    //方向左上的子弹
                    case DIR_UP_LEFT:
                        bulletY -= speed;
                        bulletX -= speed;
                        break;
                    //方向右上的子弹
                    case DIR_UP_RIGHT:
                        bulletX += speed;
                        bulletY -= speed;
                        break;
                    //方向左下的子弹
                    case DIR_DOWN_LEFT:
                        bulletX -= speed;
                        bulletY += speed;
                        break;
                    //方向右下的子弹
                    case DIR_DOWN_RIGHT:
                        bulletY += speed;
                        bulletX += speed;
                        break;
                }
                //边界处理
                if (bulletY > PlaneGameSurfaceView.screenH || bulletY <= -40
                        || bulletX > PlaneGameSurfaceView.screenW || bulletX <= -40) {
                    isDead = true;
                }
                break;
        }
    }


    @Override
    public boolean isDead() {
        return isDead;
    }
}


























