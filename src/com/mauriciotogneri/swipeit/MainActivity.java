package com.mauriciotogneri.swipeit;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.WindowManager;
import com.mauriciotogneri.swipeit.gesture.GestureManager;
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
		
		setContentView(R.layout.activity_main);
		
		this.renderer = new Renderer(this);

		this.surfaceView = (GLSurfaceView)findViewById(R.id.glSurface);
		this.surfaceView.setRenderer(this.renderer);
		
		this.surfaceView.setOnTouchListener(new GestureManager()
		{
			@Override
			public void onTap(float x, float y)
			{
				MainActivity.this.renderer.onTap(x, y);
			}
			
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

		// this.surfaceView.setOnTouchListener(new OnSwipeListener(this)
		// {
		// @Override
		// public void onSwipeUp(float x, float y)
		// {
		// MainActivity.this.renderer.onSwipeUp(x, y);
		// }
		//
		// @Override
		// public void onSwipeDown(float x, float y)
		// {
		// MainActivity.this.renderer.onSwipeDown(x, y);
		// }
		//
		// @Override
		// public void onSwipeLeft(float x, float y)
		// {
		// MainActivity.this.renderer.onSwipeLeft(x, y);
		// }
		//
		// @Override
		// public void onSwipeRight(float x, float y)
		// {
		// MainActivity.this.renderer.onSwipeRight(x, y);
		// }
		// });
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