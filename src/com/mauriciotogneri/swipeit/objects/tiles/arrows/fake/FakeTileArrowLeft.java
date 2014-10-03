package com.mauriciotogneri.swipeit.objects.tiles.arrows.fake;

import com.mauriciotogneri.swipeit.media.Resources;
import com.mauriciotogneri.swipeit.objects.tiles.Tile;
import com.mauriciotogneri.swipeit.shapes.Figure;

public class FakeTileArrowLeft extends FakeTileArrow
{
	public FakeTileArrowLeft(int i, int j)
	{
		super(i, j, Resources.Colors.LEFT, Figure.getStripeVertical(i + Tile.BLOCK_SIDE, j + Tile.BLOCK_SIDE, Tile.TILE_SIDE));
	}
}