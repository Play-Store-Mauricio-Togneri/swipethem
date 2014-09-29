package com.mauriciotogneri.swipeit.objects.tiles.arrow;

import android.graphics.Color;
import com.mauriciotogneri.swipeit.input.InputEvent.InputType;
import com.mauriciotogneri.swipeit.objects.tiles.Tile;
import com.mauriciotogneri.swipeit.shapes.Figure;

public class TileArrowDown extends TileArrow
{
	private static final int COLOR = Color.argb(255, 255, 60, 60);

	public TileArrowDown(int i, int j)
	{
		super(i, j, TileArrowDown.COLOR, Figure.getArrowDown(i + Tile.BLOCK_SIDE, j + Tile.BLOCK_SIDE, Tile.TILE_SIDE));
	}

	@Override
	public void process(InputType input)
	{
		if (input == InputType.SWIPE_DOWN)
		{
			this.disabled = true;
		}
		else
		{
			this.failed = true;
		}
	}
}