package com.mauriciotogneri.swipeit.objects.tiles.arrow;

import com.mauriciotogneri.swipeit.input.InputEvent.InputType;
import com.mauriciotogneri.swipeit.objects.tiles.Tile;
import com.mauriciotogneri.swipeit.shapes.Figure;

public abstract class TileArrow extends Tile
{
	private static final InputType[] INPUTS = new InputType[]
		{
			InputType.SWIPE_UP, //
			InputType.SWIPE_DOWN, //
			InputType.SWIPE_LEFT, //
			InputType.SWIPE_RIGHT, //
			InputType.TAP_UP
		};

	public TileArrow(int i, int j, int color, Figure figure)
	{
		super(i, j, color, figure, TileArrow.INPUTS);
	}
}