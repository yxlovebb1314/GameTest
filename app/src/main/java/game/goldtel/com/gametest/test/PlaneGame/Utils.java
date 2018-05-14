package game.goldtel.com.gametest.test.PlaneGame;

import java.util.Vector;

import game.goldtel.com.gametest.test.PlaneGame.interFace.Boom;
import game.goldtel.com.gametest.test.PlaneGame.interFace.CanDeadObj;

/**
 * Created by BS on 2018-4-23.
 */

public class Utils {


    public static Vector getRemovedVectorBullet(Vector<Bullet> vector, int position) {
        if(vector == null) {
            return null;
        }
        if(vector != null && vector.size()>0) {
            Bullet bullet = vector.get(position);
            if(bullet.isDead()) {
                vector.remove(position);
                if(position < vector.size()) {
                    //递归
                    getRemovedVectorBullet(vector,position);
                }
            }else {
                if(position < vector.size() - 1) {
                    //递归
                    getRemovedVectorBullet(vector,position+1);
                }else {
                    return vector;
                }
            }
        }
        return null;
    }

    public static Vector getRemovedVectorEnemy(Vector<Enemy> vector, int position) {
        if(vector == null) {
            return null;
        }
        if(vector != null && vector.size()>0) {
            Enemy enemy = vector.get(position);
            if(enemy.isDead()) {
                vector.remove(position);
                if(position < vector.size()) {
                    //递归
                    getRemovedVectorEnemy(vector,position);
                }
            }else {
                if(position < vector.size() - 1) {
                    //递归
                    getRemovedVectorEnemy(vector,position+1);
                }else {
                    return vector;
                }
            }
        }
        return null;
    }


    public static Vector getRemovedVectorBoom(Vector<Boom> vector, int position) {
        if(vector == null) {
            return null;
        }
        if(vector != null && vector.size()>0) {
            Boom boom = vector.get(position);
            if(boom.playEnd) {
                vector.remove(position);
                if(position < vector.size()) {
                    //递归
                    getRemovedVectorBoom(vector,position);
                }
            }else {
                if(position < vector.size() - 1) {
                    //递归
                    getRemovedVectorBoom(vector,position+1);
                }else {
                    return vector;
                }
            }
        }
        return null;
    }



}
















