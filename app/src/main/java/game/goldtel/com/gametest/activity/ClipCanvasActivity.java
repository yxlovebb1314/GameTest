package game.goldtel.com.gametest.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import game.goldtel.com.gametest.R;
import game.goldtel.com.gametest.utils.ScreenUtils;

public class ClipCanvasActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ScreenUtils.hideStateBar(this);
        setContentView(R.layout.activity_clip_canvas);
    }
}
