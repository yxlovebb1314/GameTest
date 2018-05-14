package game.goldtel.com.gametest.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import game.goldtel.com.gametest.R;
import game.goldtel.com.gametest.test.SoundPoolSurfaceView;
import game.goldtel.com.gametest.utils.ScreenUtils;

public class SoundPoolActivity extends AppCompatActivity {

    private Button bt_long,bt_short;
    private SoundPoolSurfaceView spsv;

    private void initView() {
        bt_long = (Button)findViewById(R.id.bt_act_sound_pool_bt_long);
        bt_short = (Button)findViewById(R.id.bt_act_sound_pool_bt_short);
        spsv = (SoundPoolSurfaceView)findViewById(R.id.spsv_act_sound_pool_view);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ScreenUtils.hideStateBar(this);
        setContentView(R.layout.activity_sound_pool);
        initView();
        setListener();
    }

    private void setListener() {
        bt_long.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spsv.startLongMusic();
            }
        });

        bt_short.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spsv.startShortMusic();
            }
        });


    }
}
