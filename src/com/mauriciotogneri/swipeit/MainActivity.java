package com.mauriciotogneri.swipeit;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.WindowManager;
import com.mauriciotogneri.swipeit.objects.Game;

public class MainActivity extends Activity
{
	private GLSurfaceView surfaceView;
	private Game game;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		
		setContentView(R.layout.activity_main);
		
		this.surfaceView = (GLSurfaceView)findViewById(R.id.glSurface);
		this.game = new Game(this, this.surfaceView);
		this.surfaceView.setRenderer(this.game.getRenderer());
	}

	@Override
	protected void onPause()
	{
		super.onPause();
		this.surfaceView.onPause();
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		this.surfaceView.onResume();
	}
}