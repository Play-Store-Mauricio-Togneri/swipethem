package com.mauriciotogneri.swipeit.objects.tiles.tap;

import android.graphics.Color;
import com.mauriciotogneri.swipeit.objects.tiles.Tile;
import com.mauriciotogneri.swipeit.shapes.Figure;

public class TileSingleTap extends TileTap
{
	private static final int COLOR = Color.argb(255, 60, 170, 230);
	
	public TileSingleTap(int i, int j)
	{
		super(i, j, TileSingleTap.COLOR, Figure.getSingleDot(i + Tile.BLOCK_SIDE, j + Tile.BLOCK_SIDE, 0.1f));
	}

	@Override
	protected boolean verifyTaps()
	{
		if (this.taps == 1)
		{
			this.disabled = true;
		}

		return false;
	}
}