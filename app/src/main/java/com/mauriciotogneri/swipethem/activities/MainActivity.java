package com.mauriciotogneri.swipethem.activities;

import android.app.Activity;
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.mauriciotogneri.swipethem.R;
import com.mauriciotogneri.swipethem.objects.Game;
import com.mauriciotogneri.swipethem.util.Preferences;

public class MainActivity extends Activity
{
    private Game game;
    private GLSurfaceView surfaceView;

    private boolean helpOpened = false;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        this.surfaceView = (GLSurfaceView) findViewById(R.id.glSurface);
        this.game = new Game(this, this.surfaceView);
        this.surfaceView.setRenderer(this.game.getRenderer());

        Preferences.initialize(this);

        if (Preferences.isFirstLaunch())
        {
            Preferences.setFirstLaunch();
            displayHelp();
        }
    }

    public void updateScore(final int score)
    {
        runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                TextView scoreView = (TextView) findViewById(R.id.score);
                scoreView.setText(String.valueOf(score));
            }
        });
    }

    public void updateTimer(final String time, final int color)
    {
        runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                TextView timeView = (TextView) findViewById(R.id.timer);
                timeView.setTextColor(color);
                timeView.setText(time);
            }
        });
    }

    public void vibrate()
    {
        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(300);
    }

    public void showFinalScore(final int score)
    {
        runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                displayFinalScore(score);
            }
        });
    }

    private void displayFinalScore(int score)
    {
        int bestScore = Preferences.getBestScore();

        if (score > bestScore)
        {
            bestScore = score;
            Preferences.setBestScore(bestScore);
        }

        final LinearLayout blockScreen = (LinearLayout) findViewById(R.id.block_screen);
        blockScreen.setVisibility(View.VISIBLE);

        TextView labelScore = (TextView) blockScreen.findViewById(R.id.score_value);
        labelScore.setText(String.valueOf(score));

        TextView labelBest = (TextView) blockScreen.findViewById(R.id.best_value);
        labelBest.setText(String.valueOf(bestScore));

        ImageView play = (ImageView) blockScreen.findViewById(R.id.play);
        play.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                blockScreen.setVisibility(View.GONE);
                restartGame();
            }
        });

        ImageView howToPlay = (ImageView) blockScreen.findViewById(R.id.help);
        howToPlay.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                displayHelp();
            }
        });
    }

    private void displayHelp()
    {
        this.helpOpened = true;
        final ScrollView howToPlay = (ScrollView) findViewById(R.id.how_to_play);
        howToPlay.setVisibility(View.VISIBLE);

        Button start = (Button) howToPlay.findViewById(R.id.start);
        start.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                closeHelp();
            }
        });
    }

    private void closeHelp()
    {
        ScrollView howToPlay = (ScrollView) findViewById(R.id.how_to_play);
        howToPlay.setVisibility(View.GONE);
        this.helpOpened = false;
    }

    private void restartGame()
    {
        this.game.restart();
    }

    @Override
    public void onBackPressed()
    {
        if (this.helpOpened)
        {
            closeHelp();
        }
        else
        {
            super.onBackPressed();
        }
    }

    @Override
    protected void onResume()
    {
        super.onResume();

        if (this.game != null)
        {
            this.game.resume();
        }

        if (this.surfaceView != null)
        {
            this.surfaceView.onResume();
        }
    }

    @Override
    protected void onPause()
    {
        super.onPause();

        if (this.game != null)
        {
            this.game.pause(isFinishing());
        }

        if (this.surfaceView != null)
        {
            this.surfaceView.onPause();
        }
    }

    @Override
    protected void onDestroy()
    {
        if (this.game != null)
        {
            this.game.stop();
        }

        super.onDestroy();
    }
}