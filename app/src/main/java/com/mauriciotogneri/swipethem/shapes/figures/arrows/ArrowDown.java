package com.mauriciotogneri.swipethem.shapes.figures.arrows;

public class ArrowDown extends Arrow
{
    public ArrowDown(float x, float y, float width, int color)
    {
        super(color);

        float[] vertices = new float[] {x - (width * 0.7f), y + (width * 0.2f), // 1
                x - (width * 0.5f), y + (width * 0.4f), // 2
                x, y - (width * 0.5f), // 3
                x, y - (width * 0.1f), // 4
                x + (width * 0.7f), y + (width * 0.2f), // 5
                x + (width * 0.5f), y + (width * 0.4f) // 6
        };

        setVertices(vertices);
    }
}