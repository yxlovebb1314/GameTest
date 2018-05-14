package game.goldtel.com.gametest.test.PlaneGame;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

import game.goldtel.com.gametest.test.PlaneGame.interFace.CanDeadObj;

/**
 * Created by BS on 2018-4-20.
 * 敌人类
 */

public class Enemy implements CanDeadObj{
    
    //敌人种类
    public int type;
    //苍蝇
    public static final int TYPE_FLY = 1;
    //鸭子(从左向右移动)
    public static final int TYPE_DUCKL = 2;
    //鸭子(从右向左移动)
    public static final int TYPE_DUCKR = 3;
    //敌人图片
    public Bitmap bmpEnemy;
    //敌人坐标
    public float x,y;
    //敌人每帧的宽高
    public int frameW,frameH;
    //敌人当前帧
    private int frameIndex;
    //敌人移动速度
    private float speed;
    //判断敌人是否出屏幕
    public boolean isDead;


    public Enemy(Bitmap bitmap,int enemyType,int x,int y) {
        this.bmpEnemy = bitmap;
        frameW = bmpEnemy.getWidth()/10;
        frameH = bmpEnemy.getHeight();
        this.type = enemyType;
        this.x = x;
        this.y = y;
        //图同种类的敌人速度不同
        switch(type) {
            case TYPE_FLY:
                speed = 25;
                break;
            case TYPE_DUCKL:
                speed = 3;
                break;
            case TYPE_DUCKR:
                speed = 3;
                break;
        }
    }

    /**
     * 敌人绘制函数
     */
    public void draw(Canvas canvas,Paint paint) {
        canvas.save();
        canvas.clipRect(x,y,x+frameW,y+frameH);
        canvas.drawBitmap(bmpEnemy,x - frameIndex*frameW,y,paint);
        canvas.restore();
    }


    /**
     * 敌人逻辑
     */
    public void logic() {
        //不断循环敌人帧数
        frameIndex ++;
        if(frameIndex >= 10) {
            frameIndex = 0;
        }
        //不同种类的敌人有用不同的逻辑
        switch(type) {
            case TYPE_FLY:
                if(isDead == false) {
                    //减速出现，加速返回
                    speed -= 1;
                    y += speed;
                }
                if(y <= -200) {
                    isDead = true;
                }
                break;
            case TYPE_DUCKL:
                if(!isDead) {
                    //斜右下角运动
                    x += speed / 2;
                    y += speed;
                    if(x > PlaneGameSurfaceView.screenW) {
                        isDead = true;
                    }
                }
                break;
            case TYPE_DUCKR:
                //斜左下角运动
                if(!isDead) {
                    x -= speed / 2;
                    y += speed;
                    if(x<-50) {
                        isDead = true;
                    }
                }
                break;
        }
    }


    /**
     * 敌机与子弹碰撞
     * @param bullet
     * @return
     */
    public boolean isCollsionWith(Bullet bullet) {
        float x2 = bullet.bulletX;
        float y2 = bullet.bulletY;
        float w2 = bullet.bmpBullet.getWidth();
        float h2 = bullet.bmpBullet.getHeight();
        if(x>x2-frameW && x<x2+w2
                && y>y2-frameH && y<y2+h2 ) {
            isDead = true;
            return true;
        }
        return false;
    }



    @Override
    public boolean isDead() {
        return isDead;
    }
}


























