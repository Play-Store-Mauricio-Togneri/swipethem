package com.mauriciotogneri.swipeit.gesture;

import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public abstract class GestureManager implements OnTouchListener
{
	private float lastX = 0;
	private float lastY = 0;
	
	private static final int SWIPE_THRESHOLD = 20;
	
	@Override
	public boolean onTouch(View view, MotionEvent event)
	{
		int action = event.getAction();
		float x = event.getX();
		float y = event.getY();

		switch (action)
		{
			case MotionEvent.ACTION_DOWN:
				this.lastX = x;
				this.lastY = y;
				break;
			
			case MotionEvent.ACTION_UP:
				float diffX = x - this.lastX;
				float diffY = y - this.lastY;
				
				if ((Math.abs(diffX) > 1) && (Math.abs(diffY) > 1))
				{
					if (Math.abs(diffX) > Math.abs(diffY))
					{
						if (Math.abs(diffX) > GestureManager.SWIPE_THRESHOLD)
						{
							if (diffX > 0)
							{
								onSwipeRight(this.lastX, this.lastY);
							}
							else
							{
								onSwipeLeft(this.lastX, this.lastY);
							}
						}
					}
					else if (Math.abs(diffY) > Math.abs(diffX))
					{
						if (Math.abs(diffY) > GestureManager.SWIPE_THRESHOLD)
						{
							if (diffY > 0)
							{
								onSwipeDown(this.lastX, this.lastY);
							}
							else
							{
								onSwipeUp(this.lastX, this.lastY);
							}
						}
					}
				}
				else
				{
					onTap(x, y);
				}
				break;
		}

		return true;
	}

	public abstract void onTap(float x, float y);

	public abstract void onSwipeRight(float x, float y);
	
	public abstract void onSwipeLeft(float x, float y);
	
	public abstract void onSwipeUp(float x, float y);
	
	public abstract void onSwipeDown(float x, float y);
}