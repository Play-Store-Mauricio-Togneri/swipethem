package com.mauriciotogneri.swipeit.input;

public class Movement
{
	public final Type type;
	public final float x;
	public final float y;

	public enum Type
	{
		TAP, SWIPE_UP, SWIPE_DOWN, SWIPE_LEFT, SWIPE_RIGHT
	}
	
	public Movement(Type type, float x, float y)
	{
		this.type = type;
		this.x = x;
		this.y = y;
	}
}