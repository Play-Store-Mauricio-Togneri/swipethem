package com.mauriciotogneri.swipeit.objects.tiles.arrow;

import android.graphics.Color;
import com.mauriciotogneri.swipeit.input.InputEvent.InputType;
import com.mauriciotogneri.swipeit.objects.tiles.Tile;
import com.mauriciotogneri.swipeit.shapes.Figure;

public class TileArrowRight extends TileArrow
{
	private static final int COLOR = Color.argb(255, 100, 200, 100);

	public TileArrowRight(int i, int j)
	{
		super(i, j, TileArrowRight.COLOR, Figure.getArrowRight(i + Tile.BLOCK_SIDE, j + Tile.BLOCK_SIDE, Tile.TILE_SIDE));
	}
	
	@Override
	public void process(InputType input)
	{
		if (input == InputType.SWIPE_RIGHT)
		{
			this.disabled = true;
		}
		else
		{
			this.failed = true;
		}
	}
}