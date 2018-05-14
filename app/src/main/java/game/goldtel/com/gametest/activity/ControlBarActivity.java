package game.goldtel.com.gametest.activity;

import android.graphics.PixelFormat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import game.goldtel.com.gametest.R;
import game.goldtel.com.gametest.test.ControlBarSurfaceView;
import game.goldtel.com.gametest.utils.ScreenUtils;

public class ControlBarActivity extends AppCompatActivity {

    private ControlBarSurfaceView cbsv;

    private void initView() {
        cbsv = (ControlBarSurfaceView)findViewById(R.id.cbsv_act_control_bar_view);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ScreenUtils.hideStateBar(this);
        setContentView(R.layout.activity_control_bar);
        initView();
        setListener();
    }

    private void setListener() {
        cbsv.setOnAngleChanged(new ControlBarSurfaceView.ControlBarAngleChangeListener() {
            @Override
            public void getAngle(int angle) {
Log.i("TAG","activity中得到的angle:"+angle);
            }
        });
    }

}
