package game.goldtel.com.gametest.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import game.goldtel.com.gametest.R;
import game.goldtel.com.gametest.test.ControlBarSurfaceView;
import game.goldtel.com.gametest.test.PlayerSurfaceView2;
import game.goldtel.com.gametest.utils.ScreenUtils;

public class PlayerMoveWithControlBarActivity extends AppCompatActivity {

    private ControlBarSurfaceView cbsv;
    private PlayerSurfaceView2 psv;

    private void initView() {
        cbsv = (ControlBarSurfaceView)findViewById(R.id.cbsv2_act_player_move_with_cb_cbsv2);
        psv = (PlayerSurfaceView2)findViewById(R.id.psv2_act_player_move_with_cb_psv2);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ScreenUtils.hideStateBar(this);
        setContentView(R.layout.activity_player_move_with_control_bar);
        initView();
        setListener();
    }

    private void setListener() {
        cbsv.setOnAngleChanged(new ControlBarSurfaceView.ControlBarAngleChangeListener() {
            @Override
            public void getAngle(int angle) {
                psv.setPlayerAngle(angle);
            }
        });
    }
}
