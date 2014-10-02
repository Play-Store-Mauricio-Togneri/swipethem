package com.mauriciotogneri.swipeit.objects.tiles;

import android.graphics.Color;
import com.mauriciotogneri.swipeit.input.InputEvent.InputType;
import com.mauriciotogneri.swipeit.shapes.Figure;
import com.mauriciotogneri.swipeit.shapes.Square;

public abstract class Tile
{
	private final int i;
	private final int j;

	private float alpha = 0;

	private final Square square;
	private final Figure figure;
	private final InputType[] validInputs;

	protected static final float BLOCK_SIDE = 0.5f;
	protected static final float TILE_SIDE = Tile.BLOCK_SIDE * 0.75f;
	
	public enum TileType
	{
		UP(Color.argb(255, 60, 170, 230)), //
		DOWN(Color.argb(255, 255, 60, 60)), //
		LEFT(Color.argb(255, 255, 200, 40)), //
		RIGHT(Color.argb(255, 100, 200, 100)), //
		SINGLE_TAP(Color.argb(255, 60, 170, 230));

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

		float x = i + Tile.BLOCK_SIDE;
		float y = j + Tile.BLOCK_SIDE;

		this.square = new Square(x, y, Tile.TILE_SIDE, color);
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
	
	public abstract boolean isTapped();

	public abstract boolean isDisabled();

	public abstract boolean isFailed();

	public void update(float delta)
	{
		this.alpha += (delta * 3);

		if (this.alpha > 1)
		{
			this.alpha = 1;
		}
	}

	public void draw(int positionLocation, int colorLocation)
	{
		this.square.draw(positionLocation, colorLocation, this.alpha);

		if (this.figure != null)
		{
			this.figure.draw(positionLocation, colorLocation, this.alpha);
		}
	}
}