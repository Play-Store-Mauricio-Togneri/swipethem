package com.mauriciotogneri.swipethem.objects.tiles.arrows.fake;

import com.mauriciotogneri.swipethem.media.Resources;
import com.mauriciotogneri.swipethem.objects.tiles.Tile;
import com.mauriciotogneri.swipethem.shapes.Figure;

public class FakeTileArrowDown extends FakeTileArrow
{
    public FakeTileArrowDown(int i, int j)
    {
        super(i, j, Resources.Colors.DOWN, Figure.getStripeHorizontal(i + Tile.BLOCK_SIDE, j + Tile.BLOCK_SIDE, Tile.TILE_SIDE));
    }
}