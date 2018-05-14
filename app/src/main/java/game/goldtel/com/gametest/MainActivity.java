package game.goldtel.com.gametest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.Vector;

import game.goldtel.com.gametest.activity.AnimationViewActivity;
import game.goldtel.com.gametest.activity.BallWithFingerActivity;
import game.goldtel.com.gametest.activity.BallWithFingerSurfaceActivity;
import game.goldtel.com.gametest.activity.BigBitmap2Activity;
import game.goldtel.com.gametest.activity.BigBitmapActivity;
import game.goldtel.com.gametest.activity.BitmapMovieActivity;
import game.goldtel.com.gametest.activity.ClipCanvasActivity;
import game.goldtel.com.gametest.activity.ControlBarActivity;
import game.goldtel.com.gametest.activity.FrameMovieActivity;
import game.goldtel.com.gametest.activity.MyBitmapViewActivity;
import game.goldtel.com.gametest.activity.PlayerActivity;
import game.goldtel.com.gametest.activity.PlayerMoveWithControlBarActivity;
import game.goldtel.com.gametest.activity.RectCollisionActivity;
import game.goldtel.com.gametest.activity.RegionCollisionActivity;
import game.goldtel.com.gametest.activity.SoundPoolActivity;
import game.goldtel.com.gametest.test.PlaneGame.PlaneGameActivity;
import game.goldtel.com.gametest.test.PlaneGame.Utils;

public class MainActivity extends AppCompatActivity {

    private Button bt_ball,bt_ball2,bt3,bt4,bt5,bt6,bt7,bt8,bt9,bt10,bt11,bt12,bt13,bt14
            ,bt15,bt16;

    private void initView() {
        bt_ball = (Button)findViewById(R.id.bt_act_main_ballWithFinger);
        bt_ball2 = (Button)findViewById(R.id.bt_act_main_ballWithFingerSurface);
        bt3 = (Button)findViewById(R.id.bt_act_main_bt3);
        bt4 = (Button)findViewById(R.id.bt_act_main_bt4);
        bt5 = (Button)findViewById(R.id.bt_act_main_bt5);
        bt6 = (Button)findViewById(R.id.bt_act_main_bt6);
        bt7 = (Button)findViewById(R.id.bt_act_main_bt7);
        bt8 = (Button)findViewById(R.id.bt_act_main_bt8);
        bt9 = (Button)findViewById(R.id.bt_act_main_bt9);
        bt10 = (Button)findViewById(R.id.bt_act_main_bt10);
        bt11 = (Button)findViewById(R.id.bt_act_main_bt11);
        bt12 = (Button)findViewById(R.id.bt_act_main_bt12);
        bt13 = (Button)findViewById(R.id.bt_act_main_bt13);
        bt14 = (Button)findViewById(R.id.bt_act_main_bt14);
        bt15 = (Button)findViewById(R.id.bt_act_main_bt15);
        bt16 = (Button)findViewById(R.id.bt_act_main_bt16);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        setListener();
    }


    private void setListener() {
        bt_ball.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, BallWithFingerActivity.class);
                startActivity(intent);
            }
        });

        bt_ball2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, BallWithFingerSurfaceActivity.class);
                startActivity(intent);
            }
        });

        bt3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MyBitmapViewActivity.class);
                startActivity(intent);
            }
        });
        bt4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ClipCanvasActivity.class);
                startActivity(intent);
            }
        });

        bt5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AnimationViewActivity.class);
                startActivity(intent);
            }
        });

        bt6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, BitmapMovieActivity.class);
                startActivity(intent);
            }
        });
        bt7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, FrameMovieActivity.class);
                startActivity(intent);
            }
        });
        bt8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, PlayerActivity.class);
                startActivity(intent);

            }
        });
        bt9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RectCollisionActivity.class);
                startActivity(intent);
            }
        });

        bt10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RegionCollisionActivity.class);
                startActivity(intent);
            }
        });
        bt11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SoundPoolActivity.class);
                startActivity(intent);
            }
        });
        bt12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ControlBarActivity.class);
                startActivity(intent);
            }
        });
        bt13.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, PlayerMoveWithControlBarActivity.class);
                startActivity(intent);
            }
        });
        bt14.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, PlaneGameActivity.class);
                startActivity(intent);
            }
        });
        bt15.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, BigBitmapActivity.class);
                startActivity(intent);
            }
        });
        bt16.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, BigBitmap2Activity.class);
                startActivity(intent);
            }
        });
    }
}
