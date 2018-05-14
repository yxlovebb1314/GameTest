package game.goldtel.com.gametest.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import game.goldtel.com.gametest.R;
import game.goldtel.com.gametest.test.PlayerSurfaceView;
import game.goldtel.com.gametest.utils.ScreenUtils;

public class PlayerActivity extends AppCompatActivity {

    private Button bt_left,bt_right,bt_top,bt_bottom;
    private PlayerSurfaceView player;

    private void initView() {
        bt_left = (Button)findViewById(R.id.bt_act_player_bt1);
        bt_right = (Button)findViewById(R.id.bt_act_player_bt2);
        bt_top = (Button)findViewById(R.id.bt_act_player_bt_top);
        bt_bottom = (Button)findViewById(R.id.bt_act_player_bottom);
        player = (PlayerSurfaceView)findViewById(R.id.psv_act_player_player);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ScreenUtils.hideStateBar(this);
        setContentView(R.layout.activity_player);
        initView();
        setListener();
    }

    private void setListener() {

        bt_left.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                //通过按下和抬起来表示用户是否一直处于按键状态
                if(action == MotionEvent.ACTION_DOWN) {
                    player.setDirection(1,true,true);
                }else if(action == MotionEvent.ACTION_UP) {
                    player.setDirection(1,false,true);
                }
                return false;
            }
        });

        bt_right.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                //通过按下和抬起来表示用户是否一直处于按键状态
                if(action == MotionEvent.ACTION_DOWN) {
                    player.setDirection(3,true,false);
                }else if(action == MotionEvent.ACTION_UP) {
                    player.setDirection(3,false,false);
                }
                return false;
            }
        });

        bt_top.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                //通过按下和抬起来表示用户是否一直处于按键状态
                if(action == MotionEvent.ACTION_DOWN) {
                    player.setDirection(2,true);
                }else if(action == MotionEvent.ACTION_UP) {
                    player.setDirection(2,false);
                }
                return false;
            }
        });

        bt_bottom.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                //通过按下和抬起来表示用户是否一直处于按键状态
                if(action == MotionEvent.ACTION_DOWN) {
                    player.setDirection(4,true);
                }else if(action == MotionEvent.ACTION_UP) {
                    player.setDirection(4,false);
                }
                return false;
            }
        });

    }






}
