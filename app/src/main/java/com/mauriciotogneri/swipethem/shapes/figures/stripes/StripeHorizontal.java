package com.mauriciotogneri.swipethem.shapes.figures.stripes;

public class StripeHorizontal extends Stripe
{
    public StripeHorizontal(float x, float y, float width, int color)
    {
        super(color);

        float[] vertices = new float[] {x - (width * 0.7f), y + (width * 0.2f), // 1
                x - (width * 0.7f), y - (width * 0.2f), // 2
                x + (width * 0.7f), y + (width * 0.2f), // 3
                x + (width * 0.7f), y - (width * 0.2f) // 4
        };

        setVertices(vertices);
    }
}