package com.mauriciotogneri.swipeit.objects.tiles.arrows.fake;

import com.mauriciotogneri.swipeit.media.Resources;
import com.mauriciotogneri.swipeit.objects.tiles.Tile;
import com.mauriciotogneri.swipeit.shapes.Figure;

public class FakeTileArrowDown extends FakeTileArrow
{
	public FakeTileArrowDown(int i, int j)
	{
		super(i, j, Resources.Colors.DOWN, Figure.getStripeHorizontal(i + Tile.BLOCK_SIDE, j + Tile.BLOCK_SIDE, Tile.TILE_SIDE));
	}
}