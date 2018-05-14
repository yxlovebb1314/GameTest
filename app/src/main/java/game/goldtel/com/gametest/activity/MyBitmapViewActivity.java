package game.goldtel.com.gametest.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;

import game.goldtel.com.gametest.R;
import game.goldtel.com.gametest.utils.ScreenUtils;

public class MyBitmapViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //隐藏状态栏部分
        ScreenUtils.hideStateBar(this);
        setContentView(R.layout.activity_my_bitmap_view);
    }
}
