package com.mauriciotogneri.swipeit.input;

public class InputEvent
{
	private boolean valid = false;
	public Type type;
	public int x;
	public int y;
	
	public enum Type
	{
		TAP_DOWN, TAP_UP, SWIPE_UP, SWIPE_DOWN, SWIPE_LEFT, SWIPE_RIGHT
	}

	public InputEvent()
	{
		clear();
	}
	
	public void set(Type type, int x, int y)
	{
		this.type = type;
		this.x = x;
		this.y = y;
		this.valid = true;
	}
	
	public void clear()
	{
		this.type = null;
		this.x = 0;
		this.y = 0;
		this.valid = false;
	}

	public boolean isValid()
	{
		return this.valid;
	}
}