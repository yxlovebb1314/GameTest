package game.goldtel.com.gametest.test.PlaneGame;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;

/**
 * Created by BS on 2018-4-13.
 * 菜单界面
 */

public class GameMenu {

    //背景图
    private Bitmap bgBitmap;
    //按钮图片资源,常态按钮和被点击时的按钮
    private Bitmap bitmapButton,bitmapButtonPress;
    //按钮的坐标位置
    private int buttonX,buttonY;
    //按钮图片的宽高
    private int buttonW,buttonH;
    //按钮是否被按下标识位
    private boolean isButtonPress = false;
    //标记用户按下时的位置是否在按钮中间
    private boolean isDownInButton = false;

    //菜单初始化
    public GameMenu(Bitmap bgBitmap, Bitmap bitmapButton, Bitmap bitmapButtonPress) {
        this.bgBitmap = bgBitmap;
        this.bitmapButton = bitmapButton;
        this.bitmapButtonPress = bitmapButtonPress;
        //设置按钮位置
        buttonW = bitmapButton.getWidth();
        buttonH = bitmapButton.getHeight();
        buttonX = PlaneGameSurfaceView.screenW/2 - buttonW/2;
        buttonY = PlaneGameSurfaceView.screenH - buttonH - 100;
        isButtonPress = false;
    }

    //菜单绘图函数
    public void draw(Canvas canvas, Paint paint) {
        /**
         * 绘制背景图时，要使图片填满屏幕
         * 所以要根据bitmap尺寸进行缩放
         */
        canvas.save();
        canvas.scale(((float)PlaneGameSurfaceView.screenW)/bgBitmap.getWidth(),
                ((float)PlaneGameSurfaceView.screenH)/bgBitmap.getHeight());
        canvas.drawBitmap(bgBitmap,0,0,paint);
        canvas.restore();
        if(isButtonPress) {
            canvas.drawBitmap(bitmapButtonPress,buttonX,buttonY,paint);
        }else {
            canvas.drawBitmap(bitmapButton,buttonX,buttonY,paint);
        }
    }

    /**
     * 菜单触屏函数,用于判断用户是否点击了开始按钮
     * 对于中途移除或者移入到按钮的判定：
     * 1.按下时选中了按钮，中途移出了按钮，抬起时
     * @param event
     */
    public void onTouchEvent(MotionEvent event) {
        if(event != null) {
            int action = event.getAction();
            float downX,downY;    //记录按下的x和y
            float moveX,moveY;    //中途移动时的x和y
            float upX,upY;    //抬起手指时的x和y
            /**
             * 当用户按下按钮，中途如果移出按钮，则不算点击
             * 只有当按下和抬起都在按钮中时，才算一次点击
             */
            switch(action) {
                case MotionEvent.ACTION_DOWN:
                    downX = event.getX();
                    downY = event.getY();
                    isDownInButton = isPointInButton(downX,downY);
                    isButtonPress = isDownInButton;
                    break;
                case MotionEvent.ACTION_MOVE:
                    moveX = event.getX();
                    moveY = event.getY();
                    /**
                     * 如果用户按下的位置不在按钮中，那么后续移动到按钮中时，
                     * 也不会是选中状态
                     * 必须当用户按下时在按钮中时，移动手指进出按钮才会有图片的切换
                     * 否则一直显示常态按钮图片
                     */
                    if(isPointInButton(moveX,moveY)) {
                        if(isDownInButton) {
                            //按下时在按钮中
                            isButtonPress = true;
                        }else {
                            isButtonPress = false;
                        }
                    }else {
                        isButtonPress = false;
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    upX = event.getX();
                    upY = event.getY();
                    /**
                     * 用户抬起手指时，如果按下时在按钮中间，才算点击了按钮
                     * 此时才能执行下一步操作
                     */
                    if(isPointInButton(upX,upY)) {
                        if(isDownInButton) {
                            //按下时在按钮中
                            isButtonPress = true;
                            /**
                             * 执行下一步操作
                             */
                            PlaneGameSurfaceView.gameState = PlaneGameSurfaceView.GAMEING;

                        }else {
                            isButtonPress = false;
                        }
                    }else {
                        isButtonPress = false;
                    }
                    break;
            }



        }
    }


    /**
     * 判断某一点的位置是否在按钮中间
     */
    private boolean isPointInButton(float x,float y) {
        if(x>=buttonX && x<=(buttonX+buttonW) && y>=buttonY && y<=(buttonY+buttonH)) {
            return true;
        }
        return false;
    }





}








