package game.goldtel.com.gametest.test.PlaneGame;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.media.Image;

/**
 * Created by BS on 2018-4-24.
 */

public class Boss {

    //Boss血量
    public int hp = 50;
    //boss图片
    public Bitmap bmpBoss;
    //boss坐标
    public float x,y;
    //boss每帧宽高
    public float frameW,frameH;
    //boss当前帧
    public int frameIndex;
    //boss运行速度
    public float speed = 10;
    //boss运动轨迹，一定时间会想着屏幕下方运动，并且发射大范围子弹（是否狂态）
    //正常状态下，子弹垂直向下运动
    private boolean isCrazy;
    //进入疯狂状态的状态时间间隔
    private int crazyTime = 200;
    //计数器
    private int count;


    public Boss(Bitmap bitmap) {
        this.bmpBoss = bitmap;
        frameW = bmpBoss.getWidth()/6;
        frameH = bmpBoss.getHeight()/2;
        //Boss的X坐标居中
        x = PlaneGameSurfaceView.screenW/2 - frameW/2;
        y = 50;
    }



    public void draw(Canvas canvas, Paint paint) {
        canvas.save();
        canvas.clipRect(x,y,x+frameW,y+frameH);
        canvas.drawBitmap(bmpBoss,x-frameIndex*frameW,y,paint);
        canvas.restore();
    }


    public void logic() {
        //帧数++
        frameIndex ++;
        if(frameIndex >= 6) {
            frameIndex = 0;
        }

        //没有疯狂的状态
        if(!isCrazy) {
            x += speed;
            //boss撞墙后反弹
            if(x + frameW >= PlaneGameSurfaceView.screenW || x<=0) {
                speed = -speed;
            }
            count ++;
            //变为疯狂状态
            if(count % crazyTime == 0) {
                isCrazy = true;
                speed = 24;
            }
        }else {
            //疯狂状态
            speed -= 1;
            //当boss返回时创建大量子弹
            if(speed == 0) {
                //添加8方向子弹
                PlaneGameSurfaceView.vcBulletBoss.add(
                        new Bullet(PlaneGameSurfaceView.bmpBossBullet,x+30,y,Bullet.BULLET_BOSS,Bullet.DIR_UP));
                PlaneGameSurfaceView.vcBulletBoss.add(new Bullet(PlaneGameSurfaceView.bmpBossBullet, x+40, y+10, Bullet.BULLET_BOSS, Bullet.DIR_DOWN));
                PlaneGameSurfaceView.vcBulletBoss.add(new Bullet(PlaneGameSurfaceView.bmpBossBullet, x+40, y+10, Bullet.BULLET_BOSS, Bullet.DIR_LEFT));
                PlaneGameSurfaceView.vcBulletBoss.add(new Bullet(PlaneGameSurfaceView.bmpBossBullet, x+40, y+10, Bullet.BULLET_BOSS, Bullet.DIR_RIGHT));
                PlaneGameSurfaceView.vcBulletBoss.add(new Bullet(PlaneGameSurfaceView.bmpBossBullet, x+40, y+10, Bullet.BULLET_BOSS, Bullet.DIR_UP_LEFT));
                PlaneGameSurfaceView.vcBulletBoss.add(new Bullet(PlaneGameSurfaceView.bmpBossBullet, x+40, y+10, Bullet.BULLET_BOSS, Bullet.DIR_UP_RIGHT));
                PlaneGameSurfaceView.vcBulletBoss.add(new Bullet(PlaneGameSurfaceView.bmpBossBullet, x+40, y+10, Bullet.BULLET_BOSS, Bullet.DIR_DOWN_LEFT));
                PlaneGameSurfaceView.vcBulletBoss.add(new Bullet(PlaneGameSurfaceView.bmpBossBullet, x+40, y+10, Bullet.BULLET_BOSS, Bullet.DIR_DOWN_RIGHT));
            }
            y += speed;
            if(y <= 0) {
                //回复正常状态
                isCrazy = false;
                speed = 10;
            }
        }
    }

    //判断碰撞（Boss被主角击中）
    public boolean isCollsionWith(Bullet bullet) {
        float x2 = bullet.bulletX;
        float y2 = bullet.bulletY;
        float w2 = bullet.bmpBullet.getWidth();
        float h2 = bullet.bmpBullet.getHeight();
        if(x>x2-frameW && x<x2+w2
                && y>y2-frameH && y<y2+h2 ) {
            return true;
        }
        return false;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public int getHp() {
        return this.hp;
    }

}



















