package com.mauriciotogneri.swipeit.gesture;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public abstract class OnSwipeListener implements OnTouchListener
{
	private final GestureDetector gestureDetector;

	public OnSwipeListener(Context context)
	{
		this.gestureDetector = new GestureDetector(context, new GestureListener());
	}

	private final class GestureListener extends SimpleOnGestureListener
	{
		private static final int SWIPE_THRESHOLD = 50;
		private static final int SWIPE_VELOCITY_THRESHOLD = 50;

		@Override
		public boolean onDown(MotionEvent event)
		{
			return true;
		}

		@Override
		public boolean onFling(MotionEvent event1, MotionEvent event2, float velocityX, float velocityY)
		{
			boolean result = false;

			try
			{
				float diffY = event2.getY() - event1.getY();
				float diffX = event2.getX() - event1.getX();

				if (Math.abs(diffX) > Math.abs(diffY))
				{
					if ((Math.abs(diffX) > GestureListener.SWIPE_THRESHOLD) && (Math.abs(velocityX) > GestureListener.SWIPE_VELOCITY_THRESHOLD))
					{
						if (diffX > 0)
						{
							onSwipeRight(event1.getX(), event1.getY());
						}
						else
						{
							onSwipeLeft(event1.getX(), event1.getY());
						}
						result = true;
					}
				}
				else if (Math.abs(diffY) > Math.abs(diffX))
				{
					if ((Math.abs(diffY) > GestureListener.SWIPE_THRESHOLD) && (Math.abs(velocityY) > GestureListener.SWIPE_VELOCITY_THRESHOLD))
					{
						if (diffY > 0)
						{
							onSwipeDown(event1.getX(), event1.getY());
						}
						else
						{
							onSwipeUp(event1.getX(), event1.getY());
						}
						result = true;
					}
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}

			return result;
		}
	}

	public abstract void onSwipeRight(float x, float y);

	public abstract void onSwipeLeft(float x, float y);

	public abstract void onSwipeUp(float x, float y);

	public abstract void onSwipeDown(float x, float y);
	
	@Override
	@SuppressLint("ClickableViewAccessibility")
	public boolean onTouch(View view, MotionEvent event)
	{
		return this.gestureDetector.onTouchEvent(event);
	}
}