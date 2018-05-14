package game.goldtel.com.gametest.biz;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.util.Log;
import android.view.MotionEvent;

/**
 * Created by BS on 2018-4-25.
 */

public class BigBitmapBiz {

    public static final int MODE_CANVAS = 1;  //缩放画布实现缩放图片
    public static final int MODE_MATRIX = 2;  //直接缩放图片
    public static final int MODE_MATRIX2 = 3;   //获取到图片时，获取一个被放大的bitmap

    public static int mode = 3; //当前模式

    public static Matrix mx = new Matrix();

    /**
     * 数据部分
     */
    public static float bitmapW,bitmapH;  //图片宽高
    public static float screenW,screenH;  //view宽高
    //view宽高比与位图宽高比
    public static float proportionScreen;
    public static float proportionBitmap;
    public static float x,y;  //drawBitmap时的坐标
    public static float oldX,oldY;  //记录手指上一次记录的位置
    public static Bitmap bitmap;


    public static void draw(Canvas canvas, Paint paint) {
        switch(mode) {
            case MODE_CANVAS:
                //缩放画布
                proportionScreen = screenW / screenH;
                proportionBitmap = bitmapW / bitmapH;
                if(proportionBitmap < proportionScreen) {
                    canvas.scale(screenW/bitmapW,screenW/bitmapW,0,0);
                    //画布抗锯齿
                    canvas.setDrawFilter(new PaintFlagsDrawFilter
                            (0, Paint.ANTI_ALIAS_FLAG|Paint.FILTER_BITMAP_FLAG));
                    canvas.drawBitmap(bitmap,x,y,paint);
                }else {
                    canvas.scale(screenW/bitmapW,screenW/bitmapW,bitmapW/2,bitmapH/2);
                    canvas.drawBitmap(bitmap,0,screenH/2 - bitmapH/2,paint);
                }
                break;
            case MODE_MATRIX:
                //通过矩阵缩放bitmap
                proportionScreen = screenW / screenH;
                proportionBitmap = bitmapW / bitmapH;
                if(proportionBitmap < proportionScreen) {
//                    canvas.scale(screenW/bitmapW,screenW/bitmapW,0,0);
//                    //画布抗锯齿
//                    canvas.setDrawFilter(new PaintFlagsDrawFilter
//                            (0, Paint.ANTI_ALIAS_FLAG|Paint.FILTER_BITMAP_FLAG));
//                    canvas.drawBitmap(bitmap,x,y,paint);
                    mx.reset();
                    mx.postScale(screenW/bitmapW,screenW/bitmapW);
                    mx.postTranslate(x,y);
                    canvas.setDrawFilter(new PaintFlagsDrawFilter
                            (0, Paint.ANTI_ALIAS_FLAG|Paint.FILTER_BITMAP_FLAG));
                    canvas.drawBitmap(bitmap,mx,paint);
                    Log.i("TAG","bitmapH:"+bitmapH+",bitmap.getHeight():"+bitmap.getHeight());
                }else {
//                    canvas.scale(screenW/bitmapW,screenW/bitmapW,bitmapW/2,bitmapH/2);
//                    canvas.drawBitmap(bitmap,0,screenH/2 - bitmapH/2,paint);
                }
                break;
            case MODE_MATRIX2:
                //通过矩阵缩放bitmap
                    canvas.setDrawFilter(new PaintFlagsDrawFilter
                            (0, Paint.ANTI_ALIAS_FLAG|Paint.FILTER_BITMAP_FLAG));
                    canvas.drawBitmap(bitmap,x,y,paint);
                break;
        }
    }



    public static boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        switch(mode) {
            case MODE_CANVAS:
                switch(action) {
                    case MotionEvent.ACTION_DOWN:
                        oldX = event.getX();
                        oldY = event.getY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        float fingerX = event.getX();
                        float fingerY = event.getY();
                        y += (fingerY - oldY)/(screenW/bitmapW);
                        if(y > 0) {
                            y = 0;
                        }
                        float temp = -(bitmapH - screenH/(screenW/bitmapW));
                        if(y < temp) {
                            y = temp;
                        }
                        //将此次位置保留为lod位置
                        oldX = fingerX;
                        oldY = fingerY;
                        break;
                }
                break;
            case MODE_MATRIX:
                switch(action) {
                    case MotionEvent.ACTION_DOWN:
                        oldX = event.getX();
                        oldY = event.getY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        float fingerX = event.getX();
                        float fingerY = event.getY();
//                        y += (fingerY - oldY)*(screenW/bitmapW);
                        y += fingerY - oldY;
                        if(y > 0) {
                            y = 0;
                        }
//                        float temp = -(bitmapH - screenH/(screenW/bitmapW));
                        float temp = -(bitmapH*(screenW/bitmapW) - screenH);
                        if(y < temp) {
                            y = temp;
                        }
                        //将此次位置保留为lod位置
                        oldX = fingerX;
                        oldY = fingerY;
                        break;
                }
                break;
            case MODE_MATRIX2:
                switch(action) {
                    case MotionEvent.ACTION_DOWN:
                        oldX = event.getX();
                        oldY = event.getY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        float fingerX = event.getX();
                        float fingerY = event.getY();
//                        y += (fingerY - oldY)*(screenW/bitmapW);
                        y += fingerY - oldY;
                        if(y > 0) {
                            y = 0;
                        }
//                        float temp = -(bitmapH - screenH/(screenW/bitmapW));
                        float temp = -(bitmapH - screenH);
                        if(y < temp) {
                            y = temp;
                        }
                        //将此次位置保留为lod位置
                        oldX = fingerX;
                        oldY = fingerY;
                        break;
                }
                break;
        }

        return true;
    }


    public static void logic() {

    }


}
