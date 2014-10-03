package com.mauriciotogneri.swipeit.objects.tiles.arrow;

import com.mauriciotogneri.swipeit.input.InputEvent.InputType;
import com.mauriciotogneri.swipeit.objects.tiles.Tile;
import com.mauriciotogneri.swipeit.shapes.Figure;

public abstract class TileArrow extends Tile
{
	private final InputType validInput;

	private static final InputType[] INPUTS = new InputType[]
		{
			InputType.SWIPE_UP, //
			InputType.SWIPE_DOWN, //
			InputType.SWIPE_LEFT, //
			InputType.SWIPE_RIGHT, //
			InputType.TAP_UP
		};
	
	public TileArrow(int i, int j, int color, Figure figure, InputType validInput)
	{
		super(i, j, color, figure, TileArrow.INPUTS);

		this.validInput = validInput;
	}

	@Override
	public void process(InputType input)
	{
		this.swipedOk = (input == this.validInput);
		this.swipedFail = (input != this.validInput);
	}
}