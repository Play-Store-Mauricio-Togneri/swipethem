package com.mauriciotogneri.swipeit.objects.tiles.tap;

import com.mauriciotogneri.swipeit.input.InputEvent.InputType;
import com.mauriciotogneri.swipeit.objects.tiles.Tile;
import com.mauriciotogneri.swipeit.shapes.Figure;

public abstract class TileTap extends Tile
{
	protected int taps = 0;
	protected boolean tapped = false;
	protected boolean disabled = false;
	protected boolean failed = false;
	
	private static final InputType[] INPUTS = new InputType[]
		{
			InputType.SWIPE_UP, //
			InputType.SWIPE_DOWN, //
			InputType.SWIPE_LEFT, //
			InputType.SWIPE_RIGHT, //
			InputType.TAP_UP
		};
	
	public TileTap(int i, int j, int color, Figure figure)
	{
		super(i, j, color, figure, TileTap.INPUTS);
	}
	
	@Override
	public void process(InputType input)
	{
		if (input == InputType.TAP_UP)
		{
			this.taps++;
			this.tapped = verifyTaps();
		}
		else
		{
			this.failed = true;
		}
	}
	
	protected abstract boolean verifyTaps();

	@Override
	public boolean isTapped()
	{
		return this.tapped;
	}
	
	@Override
	public boolean isDisabled()
	{
		return this.disabled;
	}
	
	@Override
	public boolean isFailed()
	{
		return this.failed;
	}
}