package game.goldtel.com.gametest.test.PlaneGame;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import game.goldtel.com.gametest.R;
import game.goldtel.com.gametest.utils.ScreenUtils;

public class PlaneGameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ScreenUtils.hideStateBar(this);
        setContentView(R.layout.activity_plane_game);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        PlaneGameSurfaceView.gameState = PlaneGameSurfaceView.GAME_MENU;
    }
}
