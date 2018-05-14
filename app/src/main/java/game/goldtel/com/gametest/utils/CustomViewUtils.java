package game.goldtel.com.gametest.utils;

import android.view.View;

/**
 * Created by BS on 2018-4-2.
 */

public class CustomViewUtils {

    /**
     *
     * @param measureSpec
     * @param normalSize 默认大小,如果不设置则为200
     * @return
     */
    public static int measureWidth(int measureSpec,int normalSize) {
        int result = 0;
        int specMode = View.MeasureSpec.getMode(measureSpec);
        int specSize = View.MeasureSpec.getSize(measureSpec);
        if(specMode == View.MeasureSpec.EXACTLY) {
            result = specSize;
        }else {
            if(normalSize == 0) {
                result = 200;
            }else if(normalSize > 0) {
                result = normalSize;
            }
            if(specMode == View.MeasureSpec.AT_MOST) {
                result = Math.min(result,specSize);
            }
        }
        return result;
    }

    /**
     *
     * @param measureSpec
     * @param normalSize 默认大小,如果不设置则为200
     * @return
     */
    public static int measureHeight(int measureSpec,int normalSize) {
        int result = 0;
        int specMode = View.MeasureSpec.getMode(measureSpec);
        int specSize = View.MeasureSpec.getSize(measureSpec);
        if(specMode == View.MeasureSpec.EXACTLY) {
            result = specSize;
        }else {
            if(normalSize == 0) {
                result = 200;
            }else if(normalSize > 0) {
                result = normalSize;
            }
            if(specMode == View.MeasureSpec.AT_MOST) {
                result = Math.min(result,specSize);
            }
        }
        return result;
    }



}
