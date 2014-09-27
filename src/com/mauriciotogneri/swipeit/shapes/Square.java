package com.mauriciotogneri.swipeit.shapes;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import android.graphics.Color;
import android.opengl.GLES20;

public class Square
{
	private final int color;
	private final FloatBuffer vertexData;
	
	private static final int POSITION_COMPONENT_COUNT = 2;
	
	private static final int BYTES_PER_FLOAT = 4;
	
	private static final int STRIDE = Square.POSITION_COMPONENT_COUNT * Square.BYTES_PER_FLOAT;
	
	public Square(float x, float y, float width, int color)
	{
		this.color = color;

		float[] vertices = new float[]
			{
				x - width, y + width, //
				x - width, y - width, //
				x + width, y + width, //
				x + width, y - width
			};
		
		ByteBuffer byteBuffer = ByteBuffer.allocateDirect(vertices.length * Square.BYTES_PER_FLOAT);
		byteBuffer.order(ByteOrder.nativeOrder());
		this.vertexData = byteBuffer.asFloatBuffer();
		this.vertexData.put(vertices);
	}

	public void draw(int positionLocation, int colorLocation)
	{
		this.vertexData.position(0);
		GLES20.glVertexAttribPointer(positionLocation, Square.POSITION_COMPONENT_COUNT, GLES20.GL_FLOAT, false, Square.STRIDE, this.vertexData);
		GLES20.glEnableVertexAttribArray(positionLocation);
		
		float red = Color.red(this.color) / 255f;
		float green = Color.green(this.color) / 255f;
		float blue = Color.blue(this.color) / 255f;
		
		GLES20.glUniform4f(colorLocation, red, green, blue, 1);
		
		GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);
	}
}