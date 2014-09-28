package com.mauriciotogneri.swipeit.objects;

import android.graphics.Color;
import com.mauriciotogneri.swipeit.input.InputEvent.InputType;
import com.mauriciotogneri.swipeit.shapes.Arrow;
import com.mauriciotogneri.swipeit.shapes.ArrowDown;
import com.mauriciotogneri.swipeit.shapes.ArrowLeft;
import com.mauriciotogneri.swipeit.shapes.ArrowRight;
import com.mauriciotogneri.swipeit.shapes.ArrowUp;
import com.mauriciotogneri.swipeit.shapes.Square;

public class Tile
{
	private final TileType type;
	
	private final int i;
	private final int j;
	
	private final Square square;
	private final Arrow arrow;
	
	public static final float BLOCK_SIDE = 0.5f;
	private static final float TILE_SIDE = Tile.BLOCK_SIDE * 0.75f;
	
	public enum TileType
	{
		UP(Color.argb(255, 60, 170, 230), InputType.SWIPE_UP, InputType.SWIPE_UP, InputType.SWIPE_DOWN, InputType.SWIPE_LEFT, InputType.SWIPE_RIGHT, InputType.TAP_UP), //
		DOWN(Color.argb(255, 255, 60, 60), InputType.SWIPE_DOWN, InputType.SWIPE_UP, InputType.SWIPE_DOWN, InputType.SWIPE_LEFT, InputType.SWIPE_RIGHT, InputType.TAP_UP), //
		LEFT(Color.argb(255, 255, 200, 40), InputType.SWIPE_LEFT, InputType.SWIPE_UP, InputType.SWIPE_DOWN, InputType.SWIPE_LEFT, InputType.SWIPE_RIGHT, InputType.TAP_UP), //
		RIGHT(Color.argb(255, 100, 200, 100), InputType.SWIPE_RIGHT, InputType.SWIPE_UP, InputType.SWIPE_DOWN, InputType.SWIPE_LEFT, InputType.SWIPE_RIGHT, InputType.TAP_UP); //
		
		private int color;
		private InputType inputValid;
		private InputType[] inputInvalid;
		
		private TileType(int color, InputType inputValid, InputType... inputInvalid)
		{
			this.color = color;
			this.inputValid = inputValid;
			this.inputInvalid = inputInvalid;
		}
		
		public int getColor()
		{
			return this.color;
		}
		
		public boolean disables(InputType input)
		{
			return (this.inputValid == input);
		}
		
		public boolean accepts(InputType input)
		{
			boolean result = false;
			
			for (InputType type : this.inputInvalid)
			{
				if (type == input)
				{
					result = true;
					break;
				}
			}

			return result;
		}
	}
	
	public Tile(TileType type, int i, int j)
	{
		this.type = type;

		this.i = i;
		this.j = j;
		
		float x = i + Tile.BLOCK_SIDE;
		float y = j + Tile.BLOCK_SIDE;
		
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
	
	public boolean isIn(int i, int j)
	{
		return (i == this.i) && (j == this.j);
	}
	
	public boolean disables(InputType input)
	{
		return this.type.disables(input);
	}
	
	public boolean accepts(InputType input)
	{
		return this.type.accepts(input);
	}
	
	public void draw(int positionLocation, int colorLocation)
	{
		this.square.draw(positionLocation, colorLocation);
		this.arrow.draw(positionLocation, colorLocation);
	}
}