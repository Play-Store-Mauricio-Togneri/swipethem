package com.mauriciotogneri.swipeit.objects;

import android.graphics.Color;
import com.mauriciotogneri.swipeit.input.InputEvent.InputType;
import com.mauriciotogneri.swipeit.shapes.Figure;
import com.mauriciotogneri.swipeit.shapes.Square;

public class Tile
{
	private final TileType type;

	private final int i;
	private final int j;

	private float alpha = 0;

	private final Square square;
	private final Figure figure;

	public static final float BLOCK_SIDE = 0.5f;
	private static final float TILE_SIDE = Tile.BLOCK_SIDE * 0.75f;
	
	private int taps = 0;
	private boolean disabledOk = false;
	private boolean disabledFail = false;

	public enum TileType
	{
		UP(Color.argb(255, 60, 170, 230), InputType.SWIPE_UP, InputType.SWIPE_UP, InputType.SWIPE_DOWN, InputType.SWIPE_LEFT, InputType.SWIPE_RIGHT, InputType.TAP_UP), //
		DOWN(Color.argb(255, 255, 60, 60), InputType.SWIPE_DOWN, InputType.SWIPE_UP, InputType.SWIPE_DOWN, InputType.SWIPE_LEFT, InputType.SWIPE_RIGHT, InputType.TAP_UP), //
		LEFT(Color.argb(255, 255, 200, 40), InputType.SWIPE_LEFT, InputType.SWIPE_UP, InputType.SWIPE_DOWN, InputType.SWIPE_LEFT, InputType.SWIPE_RIGHT, InputType.TAP_UP), //
		RIGHT(Color.argb(255, 100, 200, 100), InputType.SWIPE_RIGHT, InputType.SWIPE_UP, InputType.SWIPE_DOWN, InputType.SWIPE_LEFT, InputType.SWIPE_RIGHT, InputType.TAP_UP), //
		TAP_1(Color.argb(255, 60, 170, 230), InputType.TAP_UP, InputType.SWIPE_UP, InputType.SWIPE_DOWN, InputType.SWIPE_LEFT, InputType.SWIPE_RIGHT, InputType.TAP_UP), //
		TAP_2(Color.argb(255, 255, 60, 60), InputType.TAP_UP, InputType.SWIPE_UP, InputType.SWIPE_DOWN, InputType.SWIPE_LEFT, InputType.SWIPE_RIGHT, InputType.TAP_UP), //
		TAP_3(Color.argb(255, 255, 200, 40), InputType.TAP_UP, InputType.SWIPE_UP, InputType.SWIPE_DOWN, InputType.SWIPE_LEFT, InputType.SWIPE_RIGHT, InputType.TAP_UP), //
		TAP_4(Color.argb(255, 100, 200, 100), InputType.TAP_UP, InputType.SWIPE_UP, InputType.SWIPE_DOWN, InputType.SWIPE_LEFT, InputType.SWIPE_RIGHT, InputType.TAP_UP); //

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
				this.figure = Figure.getArrowUp(x, y, Tile.TILE_SIDE);
				break;
			case DOWN:
				this.figure = Figure.getArrowDown(x, y, Tile.TILE_SIDE);
				break;
			case LEFT:
				this.figure = Figure.getArrowLeft(x, y, Tile.TILE_SIDE);
				break;
			case RIGHT:
				this.figure = Figure.getArrowRight(x, y, Tile.TILE_SIDE);
				break;
			case TAP_1:
				this.figure = Figure.getSingleDot(x, y, 0.1f);
				break;
			case TAP_2:
				this.figure = Figure.getDoubleDot(x, y, 0.1f, Tile.TILE_SIDE);
				break;
			case TAP_3:
				this.figure = Figure.getTripleDot(x, y, 0.1f, Tile.TILE_SIDE);
				break;
			case TAP_4:
				this.figure = Figure.getQuadrupleDot(x, y, 0.1f, Tile.TILE_SIDE);
				break;
			default:
				this.figure = null;
				break;
		}
	}

	public boolean isIn(int i, int j)
	{
		return (i == this.i) && (j == this.j);
	}
	
	public boolean accepts(InputType input)
	{
		return this.type.accepts(input);
	}
	
	public void process(InputType input)
	{
		if (input == InputType.TAP_UP)
		{
			this.taps++;
		}

		switch (this.type)
		{
			case UP:
				if (input == InputType.SWIPE_UP)
				{
					this.disabledOk = true;
				}
				else
				{
					this.disabledFail = true;
				}
				break;
			case DOWN:
				if (input == InputType.SWIPE_DOWN)
				{
					this.disabledOk = true;
				}
				else
				{
					this.disabledFail = true;
				}
				break;
			case LEFT:
				if (input == InputType.SWIPE_LEFT)
				{
					this.disabledOk = true;
				}
				else
				{
					this.disabledFail = true;
				}
				break;
			case RIGHT:
				if (input == InputType.SWIPE_RIGHT)
				{
					this.disabledOk = true;
				}
				else
				{
					this.disabledFail = true;
				}
				break;
			case TAP_1:
				if (input == InputType.TAP_UP)
				{
					if (this.taps == 1)
					{
						this.disabledOk = true;
					}
				}
				else
				{
					this.disabledFail = true;
				}
				break;
			case TAP_2:
				if (input == InputType.TAP_UP)
				{
					if (this.taps == 2)
					{
						this.disabledOk = true;
					}
				}
				else
				{
					this.disabledFail = true;
				}
				break;
			case TAP_3:
				if (input == InputType.TAP_UP)
				{
					if (this.taps == 3)
					{
						this.disabledOk = true;
					}
				}
				else
				{
					this.disabledFail = true;
				}
				break;
			case TAP_4:
				if (input == InputType.TAP_UP)
				{
					if (this.taps == 4)
					{
						this.disabledOk = true;
					}
				}
				else
				{
					this.disabledFail = true;
				}
				break;
		}
	}
	
	public boolean isDisabledOk()
	{
		return this.disabledOk;
	}

	public boolean isDisabledFail()
	{
		return this.disabledFail;
	}
	
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