package com.mauriciotogneri.swipethem.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.content.SharedPreferences;
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
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;
import com.mauriciotogneri.swipethem.R;
import com.mauriciotogneri.swipethem.objects.Game;
import com.mauriciotogneri.swipethem.util.Statistics;

public class MainActivity extends Activity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener
{
	private Game game;
	private GLSurfaceView surfaceView;
	
	private GoogleApiClient apiClient;
	private boolean intentInProgress = false;
	private boolean openingLeaderboard = false;
	private boolean helpOpened = false;
	
	private static final String ATTRIBUTE_BEST = "BEST";
	private static final String ATTRIBUTE_FIRST_LAUNCH = "FIRST_LAUNCH";
	
	private static final int REQUEST_RESOLVE_ERROR = 1001;

	private static final int ACHIVEMENT_50 = 50;
	private static final int ACHIVEMENT_75 = 75;
	private static final int ACHIVEMENT_100 = 100;
	private static final int ACHIVEMENT_150 = 150;
	private static final int ACHIVEMENT_200 = 200;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		Window window = getWindow();
		window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		setContentView(R.layout.activity_main);
		
		this.surfaceView = (GLSurfaceView)findViewById(R.id.glSurface);
		this.game = new Game(this, this.surfaceView);
		this.surfaceView.setRenderer(this.game.getRenderer());

		Statistics.sendHitAppLaunched();

		GoogleApiClient.Builder builder = new GoogleApiClient.Builder(this, this, this);
		builder.addApi(Games.API).addScope(Games.SCOPE_GAMES);
		this.apiClient = builder.build();

		if (firstLaunch())
		{
			displayHelp();
		}
	}
	
	private boolean firstLaunch()
	{
		SharedPreferences preferences = getSharedPreferences(MainActivity.class.toString(), Context.MODE_PRIVATE);
		boolean result = preferences.getBoolean(MainActivity.ATTRIBUTE_FIRST_LAUNCH, true);
		
		if (result)
		{
			SharedPreferences.Editor editor = getSharedPreferences(MainActivity.class.toString(), Context.MODE_PRIVATE).edit();
			editor.putBoolean(MainActivity.ATTRIBUTE_FIRST_LAUNCH, false);
			editor.commit();
		}
		
		return result;
	}

	@Override
	public void onConnected(Bundle connectionHint)
	{
		if (this.openingLeaderboard)
		{
			showRanking();
		}
	}
	
	@Override
	public void onConnectionSuspended(int cause)
	{
		this.apiClient.reconnect();
	}
	
	@Override
	public void onConnectionFailed(ConnectionResult result)
	{
		if ((!this.intentInProgress) && result.hasResolution())
		{
			try
			{
				this.intentInProgress = true;
				result.startResolutionForResult(this, MainActivity.REQUEST_RESOLVE_ERROR);
			}
			catch (SendIntentException e)
			{
				this.intentInProgress = false;
				this.apiClient.connect();
			}
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if (requestCode == MainActivity.REQUEST_RESOLVE_ERROR)
		{
			this.intentInProgress = false;
			
			if (resultCode == Activity.RESULT_OK)
			{
				if ((!this.apiClient.isConnecting()) && (!this.apiClient.isConnected()))
				{
					this.apiClient.connect();
				}
			}
		}
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
		
		ImageView leaderboard = (ImageView)blockScreen.findViewById(R.id.ranking);
		leaderboard.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				showRanking();
			}
		});

		ImageView howToPlay = (ImageView)blockScreen.findViewById(R.id.help);
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
		final ScrollView howToPlay = (ScrollView)findViewById(R.id.how_to_play);
		howToPlay.setVisibility(View.VISIBLE);
		
		Button start = (Button)howToPlay.findViewById(R.id.start);
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
		ScrollView howToPlay = (ScrollView)findViewById(R.id.how_to_play);
		howToPlay.setVisibility(View.GONE);
		this.helpOpened = false;
	}
	
	private void submitScore(int score)
	{
		if (this.apiClient.isConnected())
		{
			Games.Leaderboards.submitScore(this.apiClient, getString(R.string.leaderboard_high_scores), score);
			
			if (score >= MainActivity.ACHIVEMENT_200)
			{
				Games.Achievements.unlock(this.apiClient, getString(R.string.achievement_200));
			}
			else if (score >= MainActivity.ACHIVEMENT_150)
			{
				Games.Achievements.unlock(this.apiClient, getString(R.string.achievement_150));
			}
			else if (score >= MainActivity.ACHIVEMENT_100)
			{
				Games.Achievements.unlock(this.apiClient, getString(R.string.achievement_100));
			}
			else if (score >= MainActivity.ACHIVEMENT_75)
			{
				Games.Achievements.unlock(this.apiClient, getString(R.string.achievement_75));
			}
			else if (score >= MainActivity.ACHIVEMENT_50)
			{
				Games.Achievements.unlock(this.apiClient, getString(R.string.achievement_50));
			}
		}
	}
	
	private void showRanking()
	{
		if (this.apiClient.isConnected())
		{
			this.openingLeaderboard = false;
			startActivityForResult(Games.Leaderboards.getLeaderboardIntent(this.apiClient, getString(R.string.leaderboard_high_scores)), 456);
		}
		else
		{
			this.openingLeaderboard = true;
			this.apiClient.connect();
		}
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