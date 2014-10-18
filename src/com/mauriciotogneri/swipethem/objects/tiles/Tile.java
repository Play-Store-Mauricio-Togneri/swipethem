package com.mauriciotogneri.swipethem.objects.tiles;

import android.graphics.Color;
import com.mauriciotogneri.swipethem.input.InputEvent.InputType;
import com.mauriciotogneri.swipethem.shapes.Figure;
import com.mauriciotogneri.swipethem.shapes.Square;

public abstract class Tile
{
	private final int i;
	private final int j;

	private final float x;
	private final float y;

	private final int color;

	private float alpha = 0;
	private float timerActive = 0;
	private float timerFinished = 0;

	private State state = State.ACTIVE;

	private boolean swipedOk = false;
	private boolean swipedFail = false;
	private boolean timeOut = false;
	private boolean disapear = false;
	private boolean finished = false;

	private final Square squareBackground;
	private final Figure figure;
	private final InputType[] validInputs;

	protected static final float BLOCK_SIDE = 0.5f;
	protected static final float TILE_SIDE = Tile.BLOCK_SIDE * 0.75f;

	private static final int TIME_LIMIT = 5;

	public enum TileType
	{
		NORMAL_ARROW_UP, //
		NORMAL_ARROW_DOWN, //
		NORMAL_ARROW_LEFT, //
		NORMAL_ARROW_RIGHT, //
		FAKE_ARROW_UP, //
		FAKE_ARROW_DOWN, //
		FAKE_ARROW_LEFT, //
		FAKE_ARROW_RIGHT;
	}

	private enum State
	{
		ACTIVE, FINISHED;
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

	public void processInput(InputType input)
	{
		if (this.state == State.ACTIVE)
		{
			process(input);
		}
	}
	
	protected abstract void process(InputType input);

	public void update(float delta)
	{
		switch (this.state)
		{
			case ACTIVE:
				updateActive(delta);
				break;
			case FINISHED:
				updateFinished(delta);
				break;
		}
	}
	
	private void updateActive(float delta)
	{
		this.timerActive += delta;
		this.alpha += (delta * 3);

		if (this.alpha > 1)
		{
			this.alpha = 1;
		}

		if (this.timerActive > Tile.TIME_LIMIT)
		{
			if (isFake())
			{
				this.disapear = true;
			}
			else
			{
				this.timeOut = true;
			}

			this.state = State.FINISHED;
		}
	}

	private void updateFinished(float delta)
	{
		this.timerFinished += delta;
		this.alpha -= (delta * 4);

		if (this.alpha < 0)
		{
			this.finished = true;
		}
	}

	public boolean isSwipedOk()
	{
		return this.swipedOk;
	}
	
	protected void setSwipeOk(boolean value)
	{
		this.swipedOk = value;

		if (value)
		{
			this.state = State.FINISHED;
		}
	}

	public boolean isSwipedFail()
	{
		return this.swipedFail;
	}

	protected void setSwipeFail(boolean value)
	{
		this.swipedFail = value;
		
		if (value)
		{
			this.state = State.FINISHED;
		}
	}
	
	public boolean isTimeOut()
	{
		return this.timeOut;
	}

	public boolean isDisappeared()
	{
		return this.disapear;
	}

	public boolean isFinished()
	{
		return this.finished;
	}

	public boolean isFake()
	{
		return false;
	}

	public void draw(int positionLocation, int colorLocation)
	{
		if (this.state == State.ACTIVE)
		{
			this.squareBackground.draw(positionLocation, colorLocation, this.alpha);

			Square squareForeground = new Square(this.x, this.y, Tile.TILE_SIDE * (this.timerActive / Tile.TIME_LIMIT), this.color);
			squareForeground.draw(positionLocation, colorLocation, this.alpha);

			if (this.figure != null)
			{
				this.figure.draw(positionLocation, colorLocation, this.alpha);
			}
		}
		else
		{
			if (this.swipedOk)
			{
				Square squareForeground = new Square(this.x, this.y, Tile.TILE_SIDE * (1 + this.timerFinished), this.color);
				squareForeground.draw(positionLocation, colorLocation, this.alpha);
			}
			else
			{
				Square squareForeground = new Square(this.x, this.y, Tile.TILE_SIDE / (1 + this.timerFinished), this.color);
				squareForeground.draw(positionLocation, colorLocation, this.alpha);
			}
			
			if (this.figure != null)
			{
				this.figure.draw(positionLocation, colorLocation, this.alpha);
			}
		}
	}
}