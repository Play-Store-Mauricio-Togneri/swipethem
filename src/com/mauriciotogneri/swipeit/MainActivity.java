package com.mauriciotogneri.swipeit;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.WindowManager;
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
}