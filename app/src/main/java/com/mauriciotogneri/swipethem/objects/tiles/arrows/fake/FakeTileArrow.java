package com.mauriciotogneri.swipethem.objects.tiles.arrows.fake;

import com.mauriciotogneri.swipethem.input.InputEvent.InputType;
import com.mauriciotogneri.swipethem.objects.tiles.Tile;
import com.mauriciotogneri.swipethem.shapes.Figure;

public abstract class FakeTileArrow extends Tile
{
    private static final InputType[] INPUTS = new InputType[] {InputType.SWIPE_UP, //
            InputType.SWIPE_DOWN, //
            InputType.SWIPE_LEFT, //
            InputType.SWIPE_RIGHT, //
            InputType.TAP_UP};

    public FakeTileArrow(int i, int j, int color, Figure figure)
    {
        super(i, j, color, figure, FakeTileArrow.INPUTS);
    }

    @Override
    public boolean isFake()
    {
        return true;
    }

    @Override
    public void process(InputType input)
    {
        setSwipeFail(true);
    }
}