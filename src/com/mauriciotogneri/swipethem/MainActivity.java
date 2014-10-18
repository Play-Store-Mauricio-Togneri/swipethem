package com.mauriciotogneri.swipethem;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.WindowManager;
import android.widget.TextView;
import com.mauriciotogneri.swipethem.objects.Game;
import com.mauriciotogneri.swipethem.util.Statistics;

public class MainActivity extends Activity
{
	private Game game;
	private GLSurfaceView surfaceView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		
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
				AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
				builder.setTitle("Time's up!");
				builder.setCancelable(false);
				builder.setMessage("\r\nScore: " + score + "\r\n");

				builder.setPositiveButton("Restart", new OnClickListener()
				{
					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						restartGame();
					}
				});

				AlertDialog alert = builder.create();
				alert.show();
			}
		});
	}

	private void restartGame()
	{
		this.game.restart();
	}
	
	// public void setLives(final int lives)
	// {
	// runOnUiThread(new Runnable()
	// {
	// @Override
	// public void run()
	// {
	// ImageView life1 = (ImageView)findViewById(R.id.life1);
	// life1.setVisibility((lives > 0) ? View.VISIBLE : View.GONE);
	//
	// ImageView life2 = (ImageView)findViewById(R.id.life2);
	// life2.setVisibility((lives > 1) ? View.VISIBLE : View.GONE);
	//
	// ImageView life3 = (ImageView)findViewById(R.id.life3);
	// life3.setVisibility((lives > 2) ? View.VISIBLE : View.GONE);
	// }
	// });
	// }
	
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