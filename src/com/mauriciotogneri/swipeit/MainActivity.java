package com.mauriciotogneri.swipeit;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TextView;
import com.mauriciotogneri.swipeit.objects.Game;

public class MainActivity extends Activity
{
	private Game game;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		setContentView(R.layout.activity_main);

		GLSurfaceView surfaceView = (GLSurfaceView)findViewById(R.id.glSurface);
		this.game = new Game(this, surfaceView);
		surfaceView.setRenderer(this.game.getRenderer());

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

	public void updateTimer(final int time, final int color)
	{
		runOnUiThread(new Runnable()
		{
			@Override
			public void run()
			{
				TextView timeView = (TextView)findViewById(R.id.timer);
				timeView.setTextColor(color);

				if (time > 9)
				{
					timeView.setText(String.valueOf(time));
				}
				else if (time >= 0)
				{
					timeView.setText("0" + time);
				}
			}
		});
	}
	
	public void showFinalScore(final int score)
	{
		runOnUiThread(new Runnable()
		{
			@Override
			public void run()
			{
				AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
				builder.setTitle("Times's up!");
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
	}

	@Override
	protected void onPause()
	{
		super.onPause();

		if (this.game != null)
		{
			this.game.pause(isFinishing());
		}
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();

		if (this.game != null)
		{
			this.game.stop();
		}
	}
}