package game.goldtel.com.gametest.utils;

import android.util.Log;

import java.io.Serializable;


/**
 * Created by BS on 2018-4-12.
 * 数学帮助类
 */


public class MathUtils implements Serializable{


    /**
     * 通过角度和速度计算当前的x，y的值
     * sin(弧度) = y要移动的距离/speed
     * @param angle
     * @return
     */
    public static float getMoveLenY(int angle,int speed) {
        if(angle > 0 && angle < 90) {

        }else if(angle > 90 && angle < 180) {
            angle = 180 - angle;
        }else if(angle >180 && angle <270) {
            angle = angle - 180;
        }else if(angle >270 && angle <360) {
            angle = 360 - angle;
        }
        float sin = (float)Math.sin(Math.toRadians(angle)) * speed;
        return sin;
    }


    /**
     * X轴移动距离同上
     * @param angle
     * @param speed
     * @return
     */
    public static float getMoveLenX(int angle,int speed) {
        if(angle > 0 && angle < 90) {

        }else if(angle > 90 && angle < 180) {
            angle = 180 - angle;
        }else if(angle >180 && angle <270) {
            angle = angle - 180;
        }else if(angle >270 && angle <360) {
            angle = 360 - angle;
        }
        float cos = (float)Math.cos(Math.toRadians(angle)) * speed;
        return cos;
    }





    /**
     * 通过手指位置和大圆圆心位置来计算角色相应的角度
     * x = x1 - circielX;
     * y = circleY - y1;
     * 1.x>0 y>0 时，右上方  1°~89°
     * 2.x<0 y>0 时，左上方  91°~179°
     * 3.x<0 y<0 时，左下方  181°~269°
     * 4.x>0 y<0 时，右下方  271°~359°
     * 5.x>0 y=0 时，向右    0°
     * 6.x=0 y>0 时，向上    90°
     * 7.x<0 y=0 时，向左    180°
     * 8.x=0 y<0 时，向下    270°
     * @param circleX   大圆圆心
     * @param circleY
     * @param x1        手指位置
     * @param y1
     * @return
     */
    public static int getTwoPointAngle(int circleX,int circleY,int x1,int y1) {
        int x = x1 - circleX;
        int y = circleY - y1;
        if(x>0 && y==0) {
            //向右
            return 0;
        }else if(x==0 && y>0) {
            //向上
            return 90;
        }else if(x<0 && y==0) {
            //向左
            return 180;
        }else if(x==0 && y<0) {
            return 270;
        }else if(x>0 && y>0) {
            return getTwoPointAngle2(circleX,circleY,x1,y1,1);
        }else if(x<0 && y>0) {
            return getTwoPointAngle2(circleX,circleY,x1,y1,2);
        }else if(x<0 && y<0) {
            return getTwoPointAngle2(circleX,circleY,x1,y1,3);
        }else if(x>0 && y<0) {
            return getTwoPointAngle2(circleX,circleY,x1,y1,4);
        }
        return -1;
    }

    /**
     * 计算角度
     * 仅按照直角三角形来计算，钝角或超过180°的角通过象限数来加
     * 象限数，第一象限x>0,y>0
     * 第二象限 x<0,y>0
     * 第三象限 x<0,y<0
     * 第四象限 x>0,y<0
     */
    private static int getTwoPointAngle2(int circleX,int circleY,int x1, int y1,int xiangxian) {
        float x = x1 - circleX;
        float y = circleY - y1;
        if(x<0) {
            x = -x;
        }
        if(y<0) {
            y = -y;
        }
        int angle = (int)Math.toDegrees(Math.atan(y/x));
        switch (xiangxian) {
            case 1:
                break;
            case 2:
                angle = 180-angle;
                break;
            case 3:
                angle = 180+angle;
                break;
            case 4:
                angle = 360-angle;
                break;
        }
//Log.i("TAG","angle:"+angle);
        //会有角度=360°的情况，在这个地方屏蔽掉
        if(angle == 360) {
            return 0;
        }
        return angle;
    }


    /**
     *
     * 通过角度和大圆半径获取小圆在大圆内边缘上的x和y值
     * 通过角度来区分象限数并确定最终的x和y的值
     * @param angle 角度
     * @param radius 大圆半径
     * @param bcircleX 大圆圆心X
     */
    public static int getMarginX(int angle,int radius,int bcircleX) {
        int x = -1;
        if(angle == 0) {
            x = bcircleX + radius;
        }else if(angle == 90) {
            x = bcircleX;
        }else if(angle == 180) {
            x = bcircleX - radius;
        }else if(angle == 270) {
            x = bcircleX;
        }else if(angle > 0 && angle < 90) {
            //1
//            double cos = getCos(angle);
            x = (int)(getCos(angle)*radius) + bcircleX;
//Log.i("TAG","1象限,an："+angle+",cos:"+cos+",x:"+x);
        }else if(angle >90 && angle <180) {
            //2
            angle = 180 - angle;
//            double cos = getCos(angle);
            x = bcircleX - (int)(getCos(angle)*radius);
//Log.i("TAG","2象限,an："+angle+",cos:"+cos+",x:"+x);
        }else if(angle > 180 && angle < 270) {
            //3
            angle = angle - 180;
//            double cos = getCos(angle);
            x = bcircleX - (int)(getCos(angle)*radius);
//Log.i("TAG","3象限,an："+angle+",cos:"+cos+",x:"+x);
        }else if(angle > 270 && angle < 360) {
            //4
            angle = 360 - angle;
            double cos = getCos(angle);
            x = (int)(getCos(angle)*radius) + bcircleX;
//Log.i("TAG","4象限,an："+angle+",cos:"+cos+",x:"+x);
        }
//Log.i("TAG","bcircleX:"+bcircleX+",x:"+x);
        return x;
    }

    /**
     *
     * 通过角度和大圆半径获取小圆在大圆内边缘上的x和y值
     * 通过角度来区分象限数并确定最终的x和y的值
     * @param angle 角度
     * @param radius 大圆半径
     * @param bcircleY 大圆圆心Y
     */
    public static int getMarginY(int angle,int radius,int bcircleY) {
        int y = -1;
        if(angle == 0) {
            y = bcircleY;
        }else if(angle == 90) {
            y = bcircleY - radius;
        }else if(angle == 180) {
            y = bcircleY;
        }else if(angle == 270) {
            y = bcircleY + radius;
        }else if(angle > 0 && angle < 90) {
            //1
            double sin = getSin(angle);
            y = bcircleY - (int)(getSin(angle)*radius);
//Log.i("TAG","1象限,an："+angle+",sin:"+sin+",y:"+y);
        }else if(angle >90 && angle <180) {
            //2
            angle= 180 - angle;
            double sin = getSin(angle);
            y = bcircleY - (int)(getSin(angle)*radius);
//Log.i("TAG","2象限,an："+angle+",sin:"+sin+",y:"+y);
        }else if(angle > 180 && angle < 270) {
            //3
            angle = angle - 180;
            double sin = getSin(angle);
            y = bcircleY + (int)(getSin(angle)*radius);
//Log.i("TAG","3象限,an："+angle+",sin:"+sin+",y:"+y);
        }else if(angle > 270 && angle < 360) {
            //4
            angle = 360 - angle;
            double sin = getSin(angle);
            y = bcircleY + (int)(getSin(angle)*radius);
//Log.i("TAG","4象限,an："+angle+",sin:"+sin+",y:"+y);
        }
//Log.i("TAG","bcircleY:"+bcircleY+",y:"+y);
        return y;
    }


    /**
     * 通过角度返回sin值
     * @param angle 角度
     * @return
     */
    private static double getSin(int angle) {
        return Math.sin(Math.toRadians(angle));
    }

    private static double getCos(int angle) {
        return Math.cos(Math.toRadians(angle));
    }



    public static float getPlayerX(int direction,float playerX) {
        return 0;
    }

}






