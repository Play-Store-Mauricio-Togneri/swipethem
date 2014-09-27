package com.mauriciotogneri.swipeit.ui;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.View;
import com.mauriciotogneri.swipeit.objects.Game;

public class CustomSurfaceView extends GLSurfaceView
{
	public CustomSurfaceView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		
		setEGLContextClientVersion(2);
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{
		int width = View.MeasureSpec.getSize(widthMeasureSpec);
		int height = View.MeasureSpec.getSize(heightMeasureSpec);
		
		float screenRatio = (float)height / (float)width;
		float resolutionRatio = (float)Game.RESOLUTION_Y / (float)Game.RESOLUTION_X;
		
		if (screenRatio > resolutionRatio)
		{
			// the screen height is bigger than it should be => reduce height
			height = (int)(resolutionRatio * width);
		}
		else if (screenRatio < resolutionRatio)
		{
			// the screen height is smaller than it should be => reduce width
			width = (int)(height / resolutionRatio);
		}

		setMeasuredDimension(width, height);
	}
}