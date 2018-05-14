package game.goldtel.com.gametest.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import game.goldtel.com.gametest.R;
import game.goldtel.com.gametest.test.AnimationView;
import game.goldtel.com.gametest.utils.ScreenUtils;

public class AnimationViewActivity extends AppCompatActivity {

    private Button bt1,bt2,bt3,bt4;
    private AnimationView av;

    private void initView() {
        av = (AnimationView)findViewById(R.id.av_act_ani_view_av);
        bt1 = (Button)findViewById(R.id.bt_act_ani_view_bt1);
        bt2 = (Button)findViewById(R.id.bt_act_ani_view_bt2);
        bt3 = (Button)findViewById(R.id.bt_act_ani_view_bt3);
        bt4 = (Button)findViewById(R.id.bt_act_ani_view_bt4);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ScreenUtils.hideStateBar(this);
        setContentView(R.layout.activity_animation_view);
        initView();
        setListener();
    }

    private void setListener() {
        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                av.alphaAnimation();
            }
        });
        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                av.scaleAnimation();
            }
        });
        bt3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                av.translateAnimation();
            }
        });
        bt4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                av.rotateAnimation();
            }
        });
    }
}
