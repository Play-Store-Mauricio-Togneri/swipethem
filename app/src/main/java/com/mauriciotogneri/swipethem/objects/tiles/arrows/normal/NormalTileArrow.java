package com.mauriciotogneri.swipethem.objects.tiles.arrows.normal;

import com.mauriciotogneri.swipethem.input.InputEvent.InputType;
import com.mauriciotogneri.swipethem.objects.tiles.Tile;
import com.mauriciotogneri.swipethem.shapes.Figure;

public abstract class NormalTileArrow extends Tile
{
    private final InputType validInput;

    private static final InputType[] INPUTS = new InputType[] {InputType.SWIPE_UP, //
            InputType.SWIPE_DOWN, //
            InputType.SWIPE_LEFT, //
            InputType.SWIPE_RIGHT, //
            InputType.TAP_UP};

    public NormalTileArrow(int i, int j, int color, Figure figure, InputType validInput)
    {
        super(i, j, color, figure, NormalTileArrow.INPUTS);

        this.validInput = validInput;
    }

    @Override
    public void process(InputType input)
    {
        setSwipeOk(input == this.validInput);
        setSwipeFail(input != this.validInput);
    }
}