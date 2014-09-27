package com.mauriciotogneri.swipeit.objects;

import android.graphics.Color;
import com.mauriciotogneri.swipeit.shapes.Square;

public class Tile
{
	private final Type type;
	
	private final float x;
	private final float y;

	private final Square square;
	
	public static final float BLOCK_SIDE = 0.5f;
	private static final float TILE_SIDE = Tile.BLOCK_SIDE * 0.75f;
	
	public enum Type
	{
		UP(Color.argb(255, 60, 170, 230)), //
		DOWN(Color.argb(255, 255, 60, 60)), //
		LEFT(Color.argb(255, 255, 200, 40)), //
		RIGHT(Color.argb(255, 100, 200, 100));
		
		private int color;
		
		private Type(int color)
		{
			this.color = color;
		}
		
		public int getColor()
		{
			return this.color;
		}
	}
	
	public Tile(Type type, float x, float y)
	{
		this.type = type;
		this.x = x;
		this.y = y;

		this.square = new Square(x, y, Tile.TILE_SIDE, type.getColor());
	}
	
	public boolean isInside(float x, float y)
	{
		return (x >= (this.x - Tile.TILE_SIDE)) && (x <= (this.x + Tile.TILE_SIDE)) && (y >= (this.y - Tile.TILE_SIDE)) && (y <= (this.y + Tile.TILE_SIDE));
	}

	public boolean isType(Type type)
	{
		return (this.type == type);
	}
	
	public void draw(int positionLocation, int colorLocation)
	{
		this.square.draw(positionLocation, colorLocation);
	}
}