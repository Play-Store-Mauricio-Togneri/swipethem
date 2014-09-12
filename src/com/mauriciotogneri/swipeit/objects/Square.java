package com.mauriciotogneri.swipeit.objects;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import android.graphics.Color;
import android.opengl.GLES20;

public class Square
{
	private final float x;
	private final float y;
	private final int color;

	private final int positionLocation;
	private final int colorLocation;
	
	private final FloatBuffer vertexData;

	private static final int POSITION_COMPONENT_COUNT = 2;
	
	private static final int BYTES_PER_FLOAT = 4;
	
	private static final int STRIDE = Square.POSITION_COMPONENT_COUNT * Square.BYTES_PER_FLOAT;

	private static final float SQUARE_SIDE = 0.5f;
	
	public Square(int positionLocation, int colorLocation, int color, float x, float y)
	{
		this.positionLocation = positionLocation;
		this.colorLocation = colorLocation;
		this.color = color;
		this.x = x;
		this.y = y;

		float[] vertices = new float[]
			{
				x - Square.SQUARE_SIDE, y + Square.SQUARE_SIDE, //
				x - Square.SQUARE_SIDE, y - Square.SQUARE_SIDE, //
				x + Square.SQUARE_SIDE, y + Square.SQUARE_SIDE, //
				x + Square.SQUARE_SIDE, y - Square.SQUARE_SIDE
			};
		
		ByteBuffer byteBuffer = ByteBuffer.allocateDirect(vertices.length * Square.BYTES_PER_FLOAT);
		byteBuffer.order(ByteOrder.nativeOrder());
		this.vertexData = byteBuffer.asFloatBuffer();
		this.vertexData.put(vertices);
		
	}
	
	public boolean isInside(float x, float y)
	{
		return (x >= (this.x - Square.SQUARE_SIDE)) && (x <= (this.x + Square.SQUARE_SIDE)) && (y >= (this.y - Square.SQUARE_SIDE)) && (y <= (this.y + Square.SQUARE_SIDE));
	}
	
	public void draw()
	{
		this.vertexData.position(0);
		GLES20.glVertexAttribPointer(this.positionLocation, Square.POSITION_COMPONENT_COUNT, GLES20.GL_FLOAT, false, Square.STRIDE, this.vertexData);
		GLES20.glEnableVertexAttribArray(this.positionLocation);

		float red = Color.red(this.color) / 255f;
		float green = Color.green(this.color) / 255f;
		float blue = Color.blue(this.color) / 255f;
		float alpha = Color.alpha(this.color) / 255f;

		GLES20.glUniform4f(this.colorLocation, red, green, blue, alpha);

		GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);
	}
}