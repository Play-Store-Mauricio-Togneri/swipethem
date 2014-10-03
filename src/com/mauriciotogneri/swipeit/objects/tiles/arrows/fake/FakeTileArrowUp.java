package com.mauriciotogneri.swipeit.objects.tiles.arrows.fake;

import com.mauriciotogneri.swipeit.media.Resources;
import com.mauriciotogneri.swipeit.objects.tiles.Tile;
import com.mauriciotogneri.swipeit.shapes.Figure;

public class FakeTileArrowUp extends FakeTileArrow
{
	public FakeTileArrowUp(int i, int j)
	{
		super(i, j, Resources.Colors.UP, Figure.getStripeHorizontal(i + Tile.BLOCK_SIDE, j + Tile.BLOCK_SIDE, Tile.TILE_SIDE));
	}
}