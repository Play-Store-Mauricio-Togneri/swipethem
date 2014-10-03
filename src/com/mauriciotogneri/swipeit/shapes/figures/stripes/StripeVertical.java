package com.mauriciotogneri.swipeit.shapes.figures.stripes;

public class StripeVertical extends Stripe
{
	public StripeVertical(float x, float y, float width, int color)
	{
		super(color);
		
		float[] vertices = new float[]
			{
				x - (width * 0.2f), y + (width * 0.7f), // 1
				x - (width * 0.2f), y - (width * 0.7f), // 2
				x + (width * 0.2f), y + (width * 0.7f), // 3
				x + (width * 0.2f), y - (width * 0.7f) // 4
			};

		setVertices(vertices);
	}
}