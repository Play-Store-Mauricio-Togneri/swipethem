package com.mauriciotogneri.swipeit.objects;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import android.graphics.Color;
import android.opengl.GLES20;

public class Square
{
	private final Type type;
	
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

	private static final float FADE_RATE = 0.02f;
	
	public enum Type
	{
		UP(Color.argb(255, 60, 170, 230)), DOWN(Color.argb(255, 255, 60, 60)), LEFT(Color.argb(255, 255, 200, 40)), RIGHT(Color.argb(255, 100, 200, 100));
		
		private int color;
		
		private Type(int color)
		{
			this.color = color;
		}
		
		public int getColor()
		{
			return this.color;
		}
	}

	public Square(int positionLocation, int colorLocation, Type type, float x, float y)
	{
		this.positionLocation = positionLocation;
		this.colorLocation = colorLocation;
		this.type = type;
		this.color = type.getColor();
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

		GLES20.glUniform4f(this.colorLocation, red, green, blue, 1);
		
		GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);
	}
}