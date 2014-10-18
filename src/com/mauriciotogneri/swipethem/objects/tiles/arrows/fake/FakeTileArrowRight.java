package com.mauriciotogneri.swipethem.objects.tiles.arrows.fake;

import com.mauriciotogneri.swipethem.media.Resources;
import com.mauriciotogneri.swipethem.objects.tiles.Tile;
import com.mauriciotogneri.swipethem.shapes.Figure;

public class FakeTileArrowRight extends FakeTileArrow
{
	public FakeTileArrowRight(int i, int j)
	{
		super(i, j, Resources.Colors.RIGHT, Figure.getStripeVertical(i + Tile.BLOCK_SIDE, j + Tile.BLOCK_SIDE, Tile.TILE_SIDE));
	}
}