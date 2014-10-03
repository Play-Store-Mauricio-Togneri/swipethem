package com.mauriciotogneri.swipeit.shapes.figures.stripes;

import android.opengl.GLES20;
import com.mauriciotogneri.swipeit.shapes.Shape;

public class Stripe extends Shape
{
	public Stripe(int color)
	{
		super(GLES20.GL_TRIANGLE_STRIP, color);
	}
}