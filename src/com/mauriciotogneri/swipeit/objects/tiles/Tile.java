package com.mauriciotogneri.swipeit.objects.tiles;

import android.graphics.Color;
import com.mauriciotogneri.swipeit.input.InputEvent.InputType;
import com.mauriciotogneri.swipeit.shapes.Figure;
import com.mauriciotogneri.swipeit.shapes.Square;

public abstract class Tile
{
	private final int i;
	private final int j;

	private final float x;
	private final float y;
	
	private final int color;

	private float alpha = 0;
	private float timer = 0;

	protected boolean swipedOk = false;
	protected boolean swipedFail = false;
	protected boolean timeOut = false;
	
	private final Square squareBackground;
	private final Figure figure;
	private final InputType[] validInputs;
	
	protected static final float BLOCK_SIDE = 0.5f;
	protected static final float TILE_SIDE = Tile.BLOCK_SIDE * 0.75f;
	
	private static final int TIME_LIMIT = 5;

	public enum TileType
	{
		UP(Color.argb(255, 60, 170, 230)), //
		DOWN(Color.argb(255, 255, 60, 60)), //
		LEFT(Color.argb(255, 255, 200, 40)), //
		RIGHT(Color.argb(255, 100, 200, 100));
		
		private int color;
		
		private TileType(int color)
		{
			this.color = color;
		}
		
		public int getColor()
		{
			return this.color;
		}
	}
	
	public Tile(int i, int j, int color, Figure figure, InputType[] validInputs)
	{
		this.i = i;
		this.j = j;
		this.x = i + Tile.BLOCK_SIDE;
		this.y = j + Tile.BLOCK_SIDE;
		
		this.squareBackground = new Square(this.x, this.y, Tile.TILE_SIDE, color);

		int red = Color.red(color) - 50;
		int green = Color.green(color) - 50;
		int blue = Color.blue(color) - 50;
		this.color = Color.argb(255, (red < 0) ? 0 : red, (green < 0) ? 0 : green, (blue < 0) ? 0 : blue);
		
		this.figure = figure;
		this.validInputs = validInputs;
	}
	
	public boolean isIn(int i, int j)
	{
		return (i == this.i) && (j == this.j);
	}
	
	public boolean acceptsInput(InputType input)
	{
		boolean result = false;
		
		for (InputType type : this.validInputs)
		{
			if (type == input)
			{
				result = true;
				break;
			}
		}
		
		return result;
	}
	
	public abstract void process(InputType input);

	public void update(float delta)
	{
		this.timer += delta;
		this.alpha += (delta * 3);

		if (this.alpha > 1)
		{
			this.alpha = 1;
		}

		if (this.timer > Tile.TIME_LIMIT)
		{
			this.timeOut = true;
		}
	}
	
	public boolean isSwipedOk()
	{
		return this.swipedOk;
	}
	
	public boolean isSwipedFail()
	{
		return this.swipedFail;
	}

	public boolean isTimeOut()
	{
		return this.timeOut;
	}
	
	public void draw(int positionLocation, int colorLocation)
	{
		this.squareBackground.draw(positionLocation, colorLocation, this.alpha);
		
		Square squareForeground = new Square(this.x, this.y, Tile.TILE_SIDE * (this.timer / Tile.TIME_LIMIT), this.color);
		squareForeground.draw(positionLocation, colorLocation, this.alpha);

		if (this.figure != null)
		{
			this.figure.draw(positionLocation, colorLocation, this.alpha);
		}
	}
}