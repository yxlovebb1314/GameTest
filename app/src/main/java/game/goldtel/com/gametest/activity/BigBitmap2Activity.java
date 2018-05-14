package game.goldtel.com.gametest.activity;

import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import game.goldtel.com.gametest.R;
import game.goldtel.com.gametest.test.BigBitmap2SurfaceView;
import game.goldtel.com.gametest.utils.ScreenUtils;

public class BigBitmap2Activity extends AppCompatActivity {

    private BigBitmap2SurfaceView bbsv;

    private void initView() {
        bbsv = (BigBitmap2SurfaceView)findViewById(R.id.bbsv_act_big_bitmap2_sv_bbsv);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ScreenUtils.hideStateBar(this);
        setContentView(R.layout.activity_big_bitmap2_surface_view);
        initView();
        bbsv.setBitmap(BitmapFactory.decodeResource(getResources(),R.mipmap.big_bitmap1));
    }
}
