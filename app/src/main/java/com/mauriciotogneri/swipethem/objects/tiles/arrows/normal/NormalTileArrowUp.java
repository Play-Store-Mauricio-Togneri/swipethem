package com.mauriciotogneri.swipethem.objects.tiles.arrows.normal;

import com.mauriciotogneri.swipethem.input.InputEvent.InputType;
import com.mauriciotogneri.swipethem.media.Resources;
import com.mauriciotogneri.swipethem.objects.tiles.Tile;
import com.mauriciotogneri.swipethem.shapes.Figure;

public class NormalTileArrowUp extends NormalTileArrow
{
    public NormalTileArrowUp(int i, int j)
    {
        super(i, j, Resources.Colors.UP, Figure.getArrowUp(i + Tile.BLOCK_SIDE, j + Tile.BLOCK_SIDE, Tile.TILE_SIDE), InputType.SWIPE_UP);
    }
}