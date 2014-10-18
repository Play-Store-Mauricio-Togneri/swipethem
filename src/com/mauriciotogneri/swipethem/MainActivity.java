package com.mauriciotogneri.swipethem;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.mauriciotogneri.swipethem.objects.Game;
import com.mauriciotogneri.swipethem.util.Statistics;

public class MainActivity extends Activity
{
	private Game game;
	private GLSurfaceView surfaceView;

	private static final String ATTRIBUTE_BEST = "BEST";

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		Window window = getWindow();
		window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.activity_main);

		this.surfaceView = (GLSurfaceView)findViewById(R.id.glSurface);
		this.game = new Game(this, this.surfaceView);
		this.surfaceView.setRenderer(this.game.getRenderer());
		
		Statistics.sendHitAppLaunched();
	}
	
	public void updateScore(final int score)
	{
		runOnUiThread(new Runnable()
		{
			@Override
			public void run()
			{
				TextView scoreView = (TextView)findViewById(R.id.score);
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
				TextView timeView = (TextView)findViewById(R.id.timer);
				timeView.setTextColor(color);
				timeView.setText(time);
			}
		});
	}

	public void vibrate()
	{
		Vibrator vibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
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
		SharedPreferences preferences = getPreferences(Context.MODE_PRIVATE);
		int bestScore = preferences.getInt(MainActivity.ATTRIBUTE_BEST, 0);

		if (score > bestScore)
		{
			bestScore = score;

			SharedPreferences.Editor editor = preferences.edit();
			editor.putInt(MainActivity.ATTRIBUTE_BEST, bestScore);
			editor.commit();
		}

		submitScore(bestScore);
		
		final LinearLayout blockScreen = (LinearLayout)findViewById(R.id.block_screen);
		blockScreen.setVisibility(View.VISIBLE);

		TextView labelScore = (TextView)blockScreen.findViewById(R.id.score_value);
		labelScore.setText(String.valueOf(score));
		
		TextView labelBest = (TextView)blockScreen.findViewById(R.id.best_value);
		labelBest.setText(String.valueOf(bestScore));

		ImageView play = (ImageView)blockScreen.findViewById(R.id.play);
		play.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				blockScreen.setVisibility(View.GONE);
				restartGame();
			}
		});
	}

	private void submitScore(int score)
	{
		// TODO
	}
	
	private void restartGame()
	{
		this.game.restart();
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