package com.mauriciotogneri.swipeit.objects.tiles.tap;

import android.graphics.Color;
import com.mauriciotogneri.swipeit.objects.tiles.Tile;
import com.mauriciotogneri.swipeit.shapes.Figure;

public class TileTripleTap extends TileTap
{
	private static final int COLOR = Color.argb(255, 255, 200, 40);
	
	public TileTripleTap(int i, int j)
	{
		super(i, j, TileTripleTap.COLOR, Figure.getTripleDot(i + Tile.BLOCK_SIDE, j + Tile.BLOCK_SIDE, 0.1f, Tile.TILE_SIDE));
	}

	@Override
	protected void verifyTaps()
	{
		if (this.taps == 3)
		{
			this.disabled = true;
		}
	}
}