package com.mauriciotogneri.swipeit.objects.tiles.arrows.normal;

import com.mauriciotogneri.swipeit.input.InputEvent.InputType;
import com.mauriciotogneri.swipeit.media.Resources;
import com.mauriciotogneri.swipeit.objects.tiles.Tile;
import com.mauriciotogneri.swipeit.shapes.Figure;

public class NormalTileArrowUp extends NormalTileArrow
{
	public NormalTileArrowUp(int i, int j)
	{
		super(i, j, Resources.Colors.UP, Figure.getArrowUp(i + Tile.BLOCK_SIDE, j + Tile.BLOCK_SIDE, Tile.TILE_SIDE), InputType.SWIPE_UP);
	}
}