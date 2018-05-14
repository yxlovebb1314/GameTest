package game.goldtel.com.gametest.activity;

import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import game.goldtel.com.gametest.R;
import game.goldtel.com.gametest.test.BigBitmapSurfaceView;
import game.goldtel.com.gametest.test.PlaneGame.Utils;
import game.goldtel.com.gametest.utils.ScreenUtils;

public class BigBitmapActivity extends AppCompatActivity {

    private BigBitmapSurfaceView bbsv;

    private void initView() {
        bbsv = (BigBitmapSurfaceView)findViewById(R.id.bbsv_act_big_bitmap_bigBitmapView);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ScreenUtils.hideStateBar(this);
        setContentView(R.layout.activity_big_bitmap);
        initView();
        bbsv.setBitmap(BitmapFactory.decodeResource(getResources(),R.mipmap.big_bitmap1));
    }
}
