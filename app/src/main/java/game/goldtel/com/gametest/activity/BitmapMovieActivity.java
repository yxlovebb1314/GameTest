package game.goldtel.com.gametest.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import game.goldtel.com.gametest.R;
import game.goldtel.com.gametest.utils.ScreenUtils;

public class BitmapMovieActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ScreenUtils.hideStateBar(this);
        setContentView(R.layout.activity_bitmap_movie);
    }
}
