package game.goldtel.com.gametest.entity;

import java.io.Serializable;

/**
 * Created by BS on 2018-4-12.
 * 摇杆控制数据实体类
 * 保存xy坐标，角色移动角度
 */

public class ControlBarEntity implements Serializable {

    public static final long serialVersionUID = 1L;

    public ControlBarEntity(int angle, int x, int y) {
        this.angle = angle;
        this.x = x;
        this.y = y;
    }

    public ControlBarEntity() {
    }

    private int angle;
    private int x,y;

    public int getAngle() {
        return angle;
    }

    public void setAngle(int angle) {
        this.angle = angle;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return "ControlBarEntity{" +
                "angle=" + angle +
                ", x=" + x +
                ", y=" + y +
                '}';
    }
}
