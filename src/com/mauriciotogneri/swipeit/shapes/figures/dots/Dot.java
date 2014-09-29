package com.mauriciotogneri.swipeit.shapes.figures.dots;

import android.opengl.GLES20;
import com.mauriciotogneri.swipeit.shapes.Shape;

public class Dot extends Shape
{
	public Dot(float x, float y, float width, int color)
	{
		super(GLES20.GL_TRIANGLE_STRIP, color);
		
		float[] vertices = new float[]
			{
				x - (width * 0.5f), y + (width * 0.5f), // 1
				x - (width * 0.5f), y - (width * 0.5f), // 2
				x + (width * 0.5f), y + (width * 0.5f), // 3
				x + (width * 0.5f), y - (width * 0.5f) // 4
			};
		
		setVertices(vertices);
	}
}