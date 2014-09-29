package com.mauriciotogneri.swipeit.objects.tiles.arrow;

import android.graphics.Color;
import com.mauriciotogneri.swipeit.input.InputEvent.InputType;
import com.mauriciotogneri.swipeit.objects.tiles.Tile;
import com.mauriciotogneri.swipeit.shapes.Figure;

public class TileArrowLeft extends TileArrow
{
	private static final int COLOR = Color.argb(255, 255, 200, 40);
	
	public TileArrowLeft(int i, int j)
	{
		super(i, j, TileArrowLeft.COLOR, Figure.getArrowLeft(i + Tile.BLOCK_SIDE, j + Tile.BLOCK_SIDE, Tile.TILE_SIDE));
	}
	
	@Override
	public void process(InputType input)
	{
		if (input == InputType.SWIPE_LEFT)
		{
			this.disabled = true;
		}
		else
		{
			this.failed = true;
		}
	}
}