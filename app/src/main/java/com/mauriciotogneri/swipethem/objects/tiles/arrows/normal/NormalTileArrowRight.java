package com.mauriciotogneri.swipethem.objects.tiles.arrows.normal;

import com.mauriciotogneri.swipethem.input.InputEvent.InputType;
import com.mauriciotogneri.swipethem.media.Resources;
import com.mauriciotogneri.swipethem.objects.tiles.Tile;
import com.mauriciotogneri.swipethem.shapes.Figure;

public class NormalTileArrowRight extends NormalTileArrow
{
    public NormalTileArrowRight(int i, int j)
    {
        super(i, j, Resources.Colors.RIGHT, Figure.getArrowRight(i + Tile.BLOCK_SIDE, j + Tile.BLOCK_SIDE, Tile.TILE_SIDE), InputType.SWIPE_RIGHT);
    }
}