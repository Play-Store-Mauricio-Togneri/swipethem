package com.mauriciotogneri.swipethem.shapes.figures.arrows;

import android.opengl.GLES20;

import com.mauriciotogneri.swipethem.shapes.Shape;

public class Arrow extends Shape
{
    public Arrow(int color)
    {
        super(GLES20.GL_TRIANGLE_STRIP, color);
    }
}