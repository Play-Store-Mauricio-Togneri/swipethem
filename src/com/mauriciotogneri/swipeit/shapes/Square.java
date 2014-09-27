package com.mauriciotogneri.swipeit.shapes;

public class Square extends Arrow
{
	public Square(float x, float y, float width, int color)
	{
		super(color);

		float[] vertices = new float[]
			{
				x - width, y + width, //
				x - width, y - width, //
				x + width, y + width, //
				x + width, y - width
			};
		
		setVertices(vertices);
	}
}