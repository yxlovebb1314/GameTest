package game.goldtel.com.gametest.test.PlaneGame.interFace;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Created by BS on 2018-4-24.
 */

public class Boom {

    //爆炸效果资源图
    private Bitmap bmpBoom;
    //爆炸效果坐标位置
    private float boomX,boomY;
    //当前帧下标
    private int currentFrameIndex;
    //爆炸效果总帧数
    private int totleFrame;
    //每帧宽高
    private int frameW,frameH;
    //是否播放完毕，优化处理
    public boolean playEnd;

    public Boom(Bitmap bmpBoom,float x,float y,int totleFrame) {
        this.bmpBoom = bmpBoom;
        this.boomX = x;
        this.boomY = y;
        this.totleFrame = totleFrame;
        frameW = bmpBoom.getWidth() / totleFrame;
        frameH = bmpBoom.getHeight();
    }


    public void draw(Canvas canvas, Paint paint) {
        canvas.save();
        canvas.clipRect(boomX,boomY,boomX + frameW,boomY + frameH);
        canvas.drawBitmap(bmpBoom,boomX - currentFrameIndex*frameW,boomY,paint);
        canvas.restore();
    }

    public void logic() {
        if(currentFrameIndex < totleFrame) {
            currentFrameIndex++;
        }else {
            playEnd = true;
        }
    }






}































