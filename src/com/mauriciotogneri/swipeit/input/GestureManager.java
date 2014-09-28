package com.mauriciotogneri.swipeit.input;

import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public abstract class GestureManager implements OnTouchListener
{
	private float lastX1 = 0;
	private float lastY1 = 0;

	private float lastX2 = 0;
	private float lastY2 = 0;
	
	private static final int SWIPE_THRESHOLD = 20;

	@Override
	public boolean onTouch(View view, MotionEvent event)
	{
		int action = event.getAction();
		int pointerIndex = (action & MotionEvent.ACTION_POINTER_INDEX_MASK) >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
		int pointerId = event.getPointerId(pointerIndex);

		float x = event.getX(pointerIndex);
		float y = event.getY(pointerIndex);
		
		switch (action & MotionEvent.ACTION_MASK)
		{
			case MotionEvent.ACTION_DOWN: // 0
			case MotionEvent.ACTION_POINTER_DOWN: // 5
				if (pointerId == 0)
				{
					this.lastX1 = x;
					this.lastY1 = y;
				}
				else
				{
					this.lastX2 = x;
					this.lastY2 = y;
				}
				onTapDown(x, y);
				break;

			case MotionEvent.ACTION_UP: // 1
			case MotionEvent.ACTION_POINTER_UP: // 6
				if (pointerId == 0)
				{
					process(x, y, this.lastX1, this.lastY1);
				}
				else
				{
					process(x, y, this.lastX2, this.lastY2);
				}
				break;
		}
		
		return true;
	}

	private void process(float x, float y, float lastX, float lastY)
	{
		float diffX = x - lastX;
		float diffY = y - lastY;

		if ((Math.abs(diffX) > 1) && (Math.abs(diffY) > 1))
		{
			if (Math.abs(diffX) > Math.abs(diffY))
			{
				if (Math.abs(diffX) > GestureManager.SWIPE_THRESHOLD)
				{
					if (diffX > 0)
					{
						onSwipeRight(lastX, lastY);
					}
					else
					{
						onSwipeLeft(lastX, lastY);
					}
				}
			}
			else if (Math.abs(diffY) > Math.abs(diffX))
			{
				if (Math.abs(diffY) > GestureManager.SWIPE_THRESHOLD)
				{
					if (diffY > 0)
					{
						onSwipeDown(lastX, lastY);
					}
					else
					{
						onSwipeUp(lastX, lastY);
					}
				}
			}
		}
		else
		{
			onTapUp(lastX, lastY);
		}
	}
	
	public abstract void onTapDown(float x, float y);
	
	public abstract void onTapUp(float x, float y);
	
	public abstract void onSwipeRight(float x, float y);

	public abstract void onSwipeLeft(float x, float y);

	public abstract void onSwipeUp(float x, float y);

	public abstract void onSwipeDown(float x, float y);
}