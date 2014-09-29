package com.mauriciotogneri.swipeit.shapes.figures.arrows;

public class ArrowLeft extends Arrow
{
	public ArrowLeft(float x, float y, float width, int color)
	{
		super(color);
		
		float[] vertices = new float[]
			{
				x + (width * 0.2f), y + (width * 0.7f), // 1
				x + (width * 0.4f), y + (width * 0.5f), // 2
				x - (width * 0.5f), y, // 3
				x - (width * 0.1f), y, // 4
				x + (width * 0.2f), y - (width * 0.7f), // 5
				x + (width * 0.4f), y - (width * 0.5f) // 6
			};

		setVertices(vertices);
	}
}