package com.mauriciotogneri.swipeit.objects.tiles.arrow;

import android.graphics.Color;
import com.mauriciotogneri.swipeit.input.InputEvent.InputType;
import com.mauriciotogneri.swipeit.objects.tiles.Tile;
import com.mauriciotogneri.swipeit.shapes.Figure;

public class TileArrowUp extends TileArrow
{
	private static final int COLOR = Color.argb(255, 60, 170, 230);

	public TileArrowUp(int i, int j)
	{
		super(i, j, TileArrowUp.COLOR, Figure.getArrowUp(i + Tile.BLOCK_SIDE, j + Tile.BLOCK_SIDE, Tile.TILE_SIDE));
	}

	@Override
	public void process(InputType input)
	{
		if (input == InputType.SWIPE_UP)
		{
			this.disabled = true;
		}
		else
		{
			this.failed = true;
		}
	}
}