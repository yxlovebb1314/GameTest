package game.goldtel.com.gametest.test.PlaneGame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.Random;
import java.util.Vector;

import game.goldtel.com.gametest.R;
import game.goldtel.com.gametest.test.PlaneGame.interFace.Boom;


/**
 * Created by BS on 2018-4-13.
 *
 */

public class PlaneGameSurfaceView extends SurfaceView implements SurfaceHolder.Callback,Runnable{

    private Canvas canvas;
    private SurfaceHolder sh;
    private Bitmap bitmap;
    private Paint mPaint;
    private Thread thread;
    private boolean flag;   //线程死循环标志位
    private int frameNum = 30;   //自定义view帧数

    /**
     * 数据部分
     */
    public static int screenW,screenH;  //view宽高
    //状态常量
    public static final int GAME_MENU = 0;  //菜单状态
    public static final int GAMEING = 1;    //游戏中
    public static final int GAME_WIN = 2;    //获胜
    public static final int GAME_LOST = 3;  //失败
    public static final int GAME_PAUSE = -1;    //暂停
    //当前游戏状态
    public static int gameState = GAME_MENU;
    //图片资源
    private Bitmap bmpBackGround;   //游戏背景
    private Bitmap bmpMenu,bmpMenuButton,bmpMenuButtonPress; //菜单背景,菜单按钮
    private Bitmap bmpPlayer,bmpPlayerHp;   //角色和血量
    private Bitmap bmpFly,bmpDuckl,bmpDuckr,bmpBoss;    //敌人图片
    public static Bitmap bmpEnemyBullet,bmpPlayerBullet,bmpBossBullet;  //子弹图片
    private Bitmap bmpBoom; //爆炸图片
    private Bitmap bmpGameWin,bmpGameLost;  //胜利与失败画面

    //爆炸效果
    private Vector<Boom> vcBoom = new Vector<>();


    /**
     * 敌人相关
     */
    private Vector<Enemy> vcEnemy;  //敌人容器
    private int createEnemyTime = 50;    //每次生成敌人的时间（毫秒）
    private int count;  //敌人数量计数器
    //敌人数组：1和2表示敌人种类，-1表示boss
    private int enemyArray[][] = {
//            {1,2},{1,1},{1,3,1,2},{1,2},
//            {2,3},{3,1,3},{2,2},{1,2},{2,2},
            {1,3,1,1},{2,1},{1,3},{2,1},{-1}
    };
    //当前去除一维数组的下表
    private int enemyArrayIndex;
    //是否出现Boss标识位
    private boolean isBoos;
    //随机库，为创建的敌人赋予随机坐标
    private Random random;
    //敌人子弹容器
    private Vector<Bullet> vcBullet = new Vector<>();
    //敌人子弹计数器
    private int countEnemyBullet;
    //boss
    private Boss boss;
    //boss子弹
    public static Vector<Bullet> vcBulletBoss;



    /**
     * 主角相关
     */
    //主角子弹容器
    private Vector<Bullet> vcBulletPlayer = new Vector<>();
    //主角子弹计数器
    private int countPlayerBullet;


    /**
     * 引用部分
     */
    private GameMenu gameMenu;
    private GameBg gameBg;
    private Player player;


    private void init() {
        sh = getHolder();
        sh.addCallback(this);
        mPaint = new Paint();
        mPaint.setColor(0xffffffff);
        mPaint.setAntiAlias(true);  //抗锯齿
        //设置焦点
        setFocusable(true);
        setFocusableInTouchMode(true);
        //设置常亮
        setKeepScreenOn(true);
    }

    public PlaneGameSurfaceView(Context context) {
        super(context);
        init();
    }

    public PlaneGameSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        //获取surfaceView宽高
        screenW = getWidth();
        screenH = getHeight();
        initGame();
        flag = true;
        thread = new Thread(this);
        thread.start();
    }


    private void initGame() {
        /**
         * 当程序切入后台重新进入时，会调用surfaceCreated方法
         * 那么就会重新init，只有在界面为菜单界面时，才允许init
         */
        if(gameState == GAME_MENU) {
            //加载游戏资源
            bmpBackGround = BitmapFactory.decodeResource(getResources(),R.mipmap.background);
            bmpMenu = BitmapFactory.decodeResource(getResources(), R.mipmap.menu);
            bmpMenuButton = BitmapFactory.decodeResource(getResources(),R.mipmap.button);
            bmpMenuButtonPress = BitmapFactory.decodeResource(getResources(), R.mipmap.button_press);
            bmpPlayer = BitmapFactory.decodeResource(getResources(),R.mipmap.player);
            bmpPlayerHp = BitmapFactory.decodeResource(getResources(),R.mipmap.hp);
            bmpFly = BitmapFactory.decodeResource(getResources(),R.mipmap.enemy_fly);
            bmpDuckl = BitmapFactory.decodeResource(getResources(),R.mipmap.enemy_duck);
            bmpDuckr = BitmapFactory.decodeResource(getResources(),R.mipmap.enemy_pig);
            bmpPlayerBullet = BitmapFactory.decodeResource(getResources(),R.mipmap.bullet);
            bmpEnemyBullet = BitmapFactory.decodeResource(getResources(),R.mipmap.bullet_enemy);
            bmpBossBullet = BitmapFactory.decodeResource(getResources(),R.mipmap.boosbullet);
            bmpBoom = BitmapFactory.decodeResource(getResources(),R.mipmap.boom);
            bmpBoss = BitmapFactory.decodeResource(getResources(),R.mipmap.robot);
            bmpGameWin = BitmapFactory.decodeResource(getResources(),R.mipmap.gamewin);
            bmpGameLost = BitmapFactory.decodeResource(getResources(),R.mipmap.gamelost);
            //菜单栏
            gameMenu = new GameMenu(bmpMenu,bmpMenuButton,bmpMenuButtonPress);
            //游戏背景
            gameBg = new GameBg(bmpBackGround);
            //游戏角色
            player = new Player(bmpPlayer,bmpPlayerHp);
            //实例敌人容易
            vcEnemy = new Vector();
            //实例随机库
            random = new Random();
            //实例boss子弹
            vcBulletBoss = new Vector<>();
            //实例化boss
            boss = new Boss(bmpBoss);
        }
    }


    /**
     * 绘画方法
     */
    public void myDraw() {
        try{
            canvas = sh.lockCanvas();
            if(canvas != null) {
                canvas.drawColor(Color.WHITE);
                switch(gameState) {
                    case GAME_MENU:
                        if(gameMenu != null) {
                            gameMenu.draw(canvas,mPaint);
                        }
                        break;
                    case GAMEING:
                        if(gameBg != null) {
                            gameBg.draw(canvas,mPaint);
                        }
                        if(player != null) {
                            player.draw(canvas,mPaint);
                        }
                        //绘制敌人相关信息
                        if(!isBoos) {
                            //绘制普通敌人
                            for(int i=0;i<vcEnemy.size();i++) {
                                vcEnemy.get(i).draw(canvas,mPaint);
                            }
                            //敌人子弹绘制
                            for(int i=0;i<vcBullet.size();i++) {
                                vcBullet.elementAt(i).draw(canvas,mPaint);
                            }
                        }else {
                            //绘制boss
                            boss.draw(canvas,mPaint);
                            //绘制boss子弹
                            for(int i=0;i<vcBulletBoss.size();i++) {
                                vcBulletBoss.get(i).draw(canvas,mPaint);
                            }
                        }
                        //绘制主角子弹
                        for(int i=0;i<vcBulletPlayer.size();i++) {
                            vcBulletPlayer.elementAt(i).draw(canvas,mPaint);
                        }
                        //绘制爆炸效果
                        for(int i=0;i<vcBoom.size();i++) {
                            vcBoom.get(i).draw(canvas,mPaint);
                        }
                        break;
                    case GAME_PAUSE:

                        break;
                    case GAME_WIN:
                        canvas.save();
                        canvas.scale(((float)screenW/bmpGameWin.getWidth())
                                ,((float)screenH/bmpGameWin.getHeight()),0,0);
                        canvas.drawBitmap(bmpGameWin,0,0,mPaint);
                        canvas.restore();
                        break;
                    case GAME_LOST:
                        canvas.save();
                        canvas.scale(((float)screenW/bmpGameWin.getWidth())
                                ,((float)screenH/bmpGameWin.getHeight()),0,0);
                        canvas.drawBitmap(bmpGameLost,0,0,mPaint);
                        canvas.restore();
                        break;
                }
            }
        }catch(Exception e) {
            e.printStackTrace();
        }finally {
            if(canvas != null)
            sh.unlockCanvasAndPost(canvas);
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch(gameState) {
            case GAME_MENU:
                if(gameMenu != null) {
                    gameMenu.onTouchEvent(event);
                }
                break;
            case GAMEING:
                if(player != null) {
                    player.onTouchEvent(event);
                }
                break;
            case GAME_PAUSE:

                break;
            case GAME_WIN:
                break;
            case GAME_LOST:
                break;
        }
        return true;
    }

    @Override
    public void run() {
        while(flag) {
            long start = System.currentTimeMillis();
            myDraw();
            logic();
            long end = System.currentTimeMillis();
            if(end-start<1000/frameNum) {
                try {
                    Thread.sleep(1000/frameNum-(end-start));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 业务逻辑
     * 根据游戏状态执行相应的逻辑
     */
    private void logic() {
        switch (gameState) {
            case GAME_MENU:

                break;
            case GAMEING:
                gameBg.logic();
                player.logic();
                //敌人逻辑
                if(!isBoos) {
                    /*for(int i=0;i<vcEnemy.size();i++) {
                        Enemy enemy = vcEnemy.get(i);
                        if(enemy.isDead) {
                            vcEnemy.removeElementAt(i);
                        }else {
                            enemy.logic();
                        }
                    }*/
                    Utils.getRemovedVectorEnemy(vcEnemy,0);
                    for(int i=0;i<vcEnemy.size();i++) {
                        Enemy enemy = vcEnemy.get(i);
                        enemy.logic();
                    }
                    //生成敌人
                    count++;
                    if(count % createEnemyTime == 0) {
                        for(int i=0;i<enemyArray[enemyArrayIndex].length;i++) {
                            //苍蝇
                            if(enemyArray[enemyArrayIndex][i] == 1) {
                                //给敌人一个随机的x坐标
                                int x = random.nextInt(screenW - 100) + 50;
                                vcEnemy.addElement(new Enemy(bmpFly,1,x,-50));
                            }
                            //左鸭子
                            else if(enemyArray[enemyArrayIndex][i] == 2) {
                                int y = random.nextInt(20);
                                vcEnemy.addElement(new Enemy(bmpDuckl,2,-50,y));
                            }
                            //右鸭子
                            else if(enemyArray[enemyArrayIndex][i] == 3) {
                                int y = random.nextInt(20);
                                vcEnemy.addElement(new Enemy(bmpDuckr,3,screenW+50,y));
                            }
                        }
//Log.i("TAG","vcEnemy.size:"+vcEnemy.size());
                        //当此时enemyArrayIndex为最后一组即boss时
                        if(enemyArrayIndex == enemyArray.length - 1) {
                            isBoos = true;
                        }else {
                            enemyArrayIndex++;
                        }
                        /*enemyArrayIndex++;
                        //---------temp---------
                        if(enemyArrayIndex == enemyArray.length-1) {
                            enemyArrayIndex = 0;
                            count = 0;
                        }*/
                    }
                    //处理敌人与主角的碰撞
                    for(int i=0;i<vcEnemy.size();i++) {
                        boolean collsionWith = player.isCollsionWith(vcEnemy.get(i));
//Log.i("TAG","coosionWith:"+collsionWith);
                        if(collsionWith) {
                            player.setPlayerHp(player.getPlayerHp() - 1);
                            if(player.getPlayerHp() < 0) {
                                player.setPlayerHp(3);
                            }
                        }
                    }

                    //每隔几秒添加一个敌人子弹
                    countEnemyBullet++;
                    if(countEnemyBullet % 40 == 0) {
                        for(int i=0;i<vcEnemy.size();i++) {
                            Enemy enemy = vcEnemy.elementAt(i);
                            int bulletType = 0;
                            switch(enemy.type) {
                                case Enemy.TYPE_FLY:
                                    bulletType = Bullet.BULLET_FLY;
                                    break;
                                case Enemy.TYPE_DUCKL:
                                case Enemy.TYPE_DUCKR:
                                    bulletType = Bullet.BULLET_DUCK;
                                    break;
                            }
                            vcBullet.add(new Bullet(bmpEnemyBullet, enemy.x,enemy.y,bulletType));
                        }
                    }

                    //敌人子弹逻辑
                    Utils.getRemovedVectorBullet(vcBullet,0);
                    for(int i=0;i<vcBullet.size();i++) {
                        Bullet b = vcBullet.get(i);
                        b.logic();
                    }

                    //敌人子弹与主角碰撞
                    for(int i=0;i<vcBullet.size();i++) {
                        if(player.isCoosionWith(vcBullet.get(i))) {
                            player.setPlayerHp(player.getPlayerHp()-1);
                            //当主角血量小于0，游戏失败
                            if(player.getPlayerHp()<0) {
//                                gameState = GAME_LOST;
                                player.setPlayerHp(3);
                            }
                        }
                    }

                    //主角子弹与敌人碰撞
                    for(int i=0;i<vcBulletPlayer.size();i++) {
                        Bullet buletPlayer = vcBulletPlayer.get(i);
                        //将每一颗子弹与所有敌机进行碰撞测试
                        for(int j=0;j<vcEnemy.size();j++) {
                            if(vcEnemy.get(j).isCollsionWith(buletPlayer)) {
                                //添加爆炸效果
                                vcBoom.add(new Boom
                                        (bmpBoom,vcEnemy.get(j).x,vcEnemy.get(j).y,7));
                            }
                        }
                    }

                }else {
                    //boss逻辑
                    boss.logic();
                    //添加boss子弹
                    if(countPlayerBullet % 25 == 0) {
                        //添加非疯狂状态下的普通子弹
                        vcBulletBoss.add(
                                new Bullet(bmpBossBullet,boss.x+boss.frameW/2-bmpBossBullet.getWidth()/2,
                                        boss.y+boss.frameH,Bullet.BULLET_BOSS,Bullet.DIR_DOWN));
                    }

                    //删除已出界的boss子弹
                    Utils.getRemovedVectorBullet(vcBulletBoss,0);
                    for(int i=0;i<vcBulletBoss.size();i++) {
                        Bullet bullet = vcBulletBoss.get(i);
                        bullet.logic();
                    }

                    //boss子弹与主角碰撞
                    for(int i=0;i<vcBulletBoss.size();i++) {
                        if(player.isCoosionWith(vcBulletBoss.get(i))) {
                            player.setPlayerHp(player.getPlayerHp());
                            if(player.getPlayerHp() < 0) {
                                player.setPlayerHp(3);
                            }
                        }
                    }

                    //boss被主角击中
                    for(int i=0;i<vcBulletPlayer.size();i++) {
                        Bullet b = vcBulletPlayer.get(i);
                        if(boss.isCollsionWith(b)) {
                            if(boss.hp <= 0) {
                                gameState = GAME_WIN;
                            }else {
                                //及时删除此子弹，以免重复碰撞计算
                                b.isDead = true;
                                boss.setHp(boss.hp - 1);
                            }
                        }
                    }


                }

                //添加主角子弹
                countPlayerBullet++;
                if(countPlayerBullet % 2 == 0) {
                    vcBulletPlayer.add(
                            new Bullet(bmpPlayerBullet,player.playerX+player.playerW/2-bmpPlayerBullet.getWidth()/2
                                    ,player.playerY - 5,Bullet.BULLET_PLAYER));
                }
                //将出界的子弹移除
                Utils.getRemovedVectorBullet(vcBulletPlayer,0);
                //执行主角子弹逻辑
                for(int i=0;i<vcBulletPlayer.size();i++) {
                    Bullet bullet = vcBulletPlayer.get(i);
                    bullet.logic();
                }
                //爆炸效果逻辑
                //删除已播放完毕的爆炸
                Utils.getRemovedVectorBoom(vcBoom,0);
                for(int i=0;i<vcBoom.size();i++) {
                    Boom boom = vcBoom.get(i);
                    boom.logic();
                }

                break;
            case GAME_PAUSE:

                break;
            case GAME_WIN:

                break;
            case GAME_LOST:

                break;
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        flag = false;
    }




}




