package game.goldtel.com.gametest.test.PlaneGame;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Created by BS on 2018-4-20.
 * 操作游戏背景
 */

public class GameBg {

    //为了循环不放，定义两个位图对象,但是资源是同一张图片
    private Bitmap gameBg1,gameBg2;
    //游戏背景坐标
    private float bg1X,bg1Y,bg2X,bg2Y;
    //背景滚动速度
    private float speed = 0.5f;
    //图片贴合部分
    private float k = 220;

    public GameBg(Bitmap bitmap) {
        gameBg1 = bitmap;
        gameBg2 = bitmap;
        //先让第一张背景底部正好填满整个屏幕
        //得到图片和屏幕的高度差来计算Y值,如果图片高度小于屏幕，则为正，否则为负数
        bg1Y = PlaneGameSurfaceView.screenH - gameBg1.getHeight();
        //第二张图片位于第一张图片上方
        bg2Y = bg1Y - gameBg2.getHeight() + k;
    }

    /**
     * 背景绘图函数
     */
    public void draw(Canvas canvas, Paint paint) {
        //将两张背景都绘制出来
        //伸缩图片至屏幕宽度
        canvas.save();
        //缩放画布，以屏幕左下角为基准点
        canvas.scale((float)PlaneGameSurfaceView.screenW/gameBg1.getWidth()
                ,(float)PlaneGameSurfaceView.screenW/gameBg1.getWidth(),0,PlaneGameSurfaceView.screenH);
        canvas.drawBitmap(gameBg1,bg1X,bg1Y,paint);
        canvas.drawBitmap(gameBg2,bg2X,bg2Y,paint);
        canvas.restore();
    }


    /**
     * 逻辑函数
     */
    public void logic() {
        bg1Y += speed;
        bg2Y += speed;
        //当第一张图片的Y坐标超出屏幕,将其移到第二张图片的上方
        if(bg1Y>PlaneGameSurfaceView.screenH) {
            bg1Y = bg2Y - gameBg1.getHeight() + k;
        }
        //第二张图片同上原理
        if(bg2Y>PlaneGameSurfaceView.screenH) {
            bg2Y = bg1Y - gameBg2.getHeight() + k;
        }
    }


}
