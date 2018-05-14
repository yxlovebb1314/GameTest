package game.goldtel.com.gametest.test;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;

import game.goldtel.com.gametest.R;
import game.goldtel.com.gametest.utils.CustomViewUtils;
import game.goldtel.com.gametest.utils.ScreenUtils;

/**
 * Created by BS on 2018-4-10.
 * 在自定义View中使用Animation
 *
 * 注意：Animation的每种动画都是对整个画布进行操作！！
 */

public class AnimationView extends View implements Animation.AnimationListener{

    private Paint paint;
    private Matrix matrix;
    private Bitmap bitmap;

    private Animation alphaAnimation;   //渐变动画
    private Animation scaleAnimation;   //缩放动画
    private Animation translateAnimation;   //平移动画
    private Animation rotateAnimation;  //旋转动画

    private void init() {
        paint = new Paint();
        paint.setAntiAlias(true);
        matrix = new Matrix();
        bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.dva);
    }

    public AnimationView(Context context) {
        super(context);
        init();
    }

    public AnimationView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(CustomViewUtils.measureWidth(widthMeasureSpec,0)
        ,CustomViewUtils.measureHeight(heightMeasureSpec,0));
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(0xff000000);   //画布黑色
        /*canvas.save();
        matrix.reset();
        matrix.postScale(0.5f,0.5f,bitmap.getWidth()/2,bitmap.getHeight()/2);
        canvas.drawBitmap(bitmap,matrix,paint);
        canvas.restore();*/
        canvas.drawBitmap(bitmap,0,0,paint);
    }


    @Override
    public void onAnimationStart(Animation animation) {
Log.i("TAG","动画开始");
    }

    @Override
    public void onAnimationEnd(Animation animation) {
Log.i("TAG","动画结束");
    }

    @Override
    public void onAnimationRepeat(Animation animation) {
Log.i("TAG","动画重复");
    }

    /**
     * 对外提供的渐变方法
     */
    public void alphaAnimation() {
        //从什么透明度变到什么透明度
        if(alphaAnimation == null) {
            alphaAnimation = new AlphaAnimation(0.1f,1.0f);
        }
        alphaAnimation.setAnimationListener(this);
        alphaAnimation.setDuration(1000);
        startAnimation(alphaAnimation);

    }

    public void scaleAnimation() {
        /**
         * 第1,2个参数：x轴的起始和结束的伸缩比例
         * 第3,4个参数：y轴的起始和结束的伸缩比例
         * 第5个参数表示动画在X轴伸缩的参照物，比如相对于自己，相对于parent等
         * 第6个参数表示动画在参照物的X轴坐标位置
         * 7,8参数为Y轴同上
         */
        scaleAnimation = new ScaleAnimation(0.0f,1.0f,0.0f,1.0f,Animation.RELATIVE_TO_SELF
                ,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        scaleAnimation.setAnimationListener(this);
        scaleAnimation.setDuration(1000);
        startAnimation(scaleAnimation);
    }

    public void translateAnimation() {
        /**
         * 1,2参数：X轴从哪到哪
         * 3,4参数：Y轴从哪到哪
         */
        translateAnimation = new TranslateAnimation(-getWidth(),0,0,0);
        translateAnimation.setAnimationListener(this);
        translateAnimation.setDuration(500);
        startAnimation(translateAnimation);
    }

    public void rotateAnimation() {
        /**
         * 1,2参数：从多少度到多少度
         * 3,4参数：X轴旋转参照物，相对于参照物的位置
         * 5,6参数：Y轴同上
         */
        rotateAnimation = new RotateAnimation(0.0f,360.0f,Animation.RELATIVE_TO_SELF
                ,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        rotateAnimation.setAnimationListener(this);
        rotateAnimation.setDuration(1000);
        startAnimation(rotateAnimation);
    }

}
