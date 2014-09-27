package com.mauriciotogneri.swipeit.objects;

import android.graphics.Color;
import com.mauriciotogneri.swipeit.input.Movement;
import com.mauriciotogneri.swipeit.shapes.Arrow;
import com.mauriciotogneri.swipeit.shapes.ArrowDown;
import com.mauriciotogneri.swipeit.shapes.ArrowLeft;
import com.mauriciotogneri.swipeit.shapes.ArrowRight;
import com.mauriciotogneri.swipeit.shapes.ArrowUp;
import com.mauriciotogneri.swipeit.shapes.Square;

public class Tile
{
	private final Type type;
	
	private final float x;
	private final float y;

	private final Square square;
	private final Arrow arrow;
	
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
		
		switch (type)
		{
			case UP:
				this.arrow = new ArrowUp(x, y, Tile.TILE_SIDE, Color.WHITE);
				break;
			case DOWN:
				this.arrow = new ArrowDown(x, y, Tile.TILE_SIDE, Color.WHITE);
				break;
			case LEFT:
				this.arrow = new ArrowLeft(x, y, Tile.TILE_SIDE, Color.WHITE);
				break;
			case RIGHT:
				this.arrow = new ArrowRight(x, y, Tile.TILE_SIDE, Color.WHITE);
				break;
			default:
				this.arrow = null;
				break;
		}
	}
	
	public boolean isInside(float x, float y)
	{
		return (x >= (this.x - Tile.BLOCK_SIDE)) && (x <= (this.x + Tile.BLOCK_SIDE)) && (y >= (this.y - Tile.BLOCK_SIDE)) && (y <= (this.y + Tile.BLOCK_SIDE));
	}

	public boolean isType(Movement.Type type)
	{
		return true;// (this.type == type);
	}
	
	public void draw(int positionLocation, int colorLocation)
	{
		this.square.draw(positionLocation, colorLocation);
		this.arrow.draw(positionLocation, colorLocation);
	}
}