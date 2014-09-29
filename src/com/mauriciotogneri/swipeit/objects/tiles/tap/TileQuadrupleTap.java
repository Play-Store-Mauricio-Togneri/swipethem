package com.mauriciotogneri.swipeit.objects.tiles.tap;

import android.graphics.Color;
import com.mauriciotogneri.swipeit.objects.tiles.Tile;
import com.mauriciotogneri.swipeit.shapes.Figure;

public class TileQuadrupleTap extends TileTap
{
	private static final int COLOR = Color.argb(255, 100, 200, 100);
	
	public TileQuadrupleTap(int i, int j)
	{
		super(i, j, TileQuadrupleTap.COLOR, Figure.getQuadrupleDot(i + Tile.BLOCK_SIDE, j + Tile.BLOCK_SIDE, 0.1f, Tile.TILE_SIDE));
	}
	
	@Override
	protected void verifyTaps()
	{
		if (this.taps == 4)
		{
			this.disabled = true;
		}
	}
}