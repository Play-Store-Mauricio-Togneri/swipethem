package com.mauriciotogneri.swipeit;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.WindowManager;
import com.mauriciotogneri.swipeit.gesture.OnSwipeListener;
import com.mauriciotogneri.swipeit.ui.Renderer;

public class MainActivity extends Activity
{
	private GLSurfaceView surfaceView;
	private Renderer renderer;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		
		this.renderer = new Renderer(this);

		this.surfaceView = new GLSurfaceView(this);
		this.surfaceView.setEGLContextClientVersion(2);
		this.surfaceView.setRenderer(this.renderer);
		setContentView(this.surfaceView);

		this.surfaceView.setOnTouchListener(new OnSwipeListener(this)
		{
			@Override
			public void onSwipeUp(float x, float y)
			{
				MainActivity.this.renderer.onSwipeUp(x, y);
			}

			@Override
			public void onSwipeDown(float x, float y)
			{
				MainActivity.this.renderer.onSwipeDown(x, y);
			}

			@Override
			public void onSwipeLeft(float x, float y)
			{
				MainActivity.this.renderer.onSwipeLeft(x, y);
			}

			@Override
			public void onSwipeRight(float x, float y)
			{
				MainActivity.this.renderer.onSwipeRight(x, y);
			}
		});
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