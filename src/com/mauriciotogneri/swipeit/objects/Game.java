package com.mauriciotogneri.swipeit.objects;

import java.util.Random;
import android.content.Context;
import android.opengl.GLSurfaceView;
import com.mauriciotogneri.swipeit.objects.Square.Type;

public class Game
{
	public static final int RESOLUTION_X = 7;
	public static final int RESOLUTION_Y = 11;

	private final Renderer renderer;

	private Square squareUp;
	private Square squareDown;
	private Square squareLeft;
	private Square squareRight;
	
	public Game(Context context, GLSurfaceView surfaceView)
	{
		this.renderer = new Renderer(context, this, surfaceView);
		
		createSquare(Square.Type.UP);
		createSquare(Square.Type.DOWN);
		createSquare(Square.Type.LEFT);
		createSquare(Square.Type.RIGHT);
	}

	public Renderer getRenderer()
	{
		return this.renderer;
	}

	public void onTap(float x, float y)
	{
		// if (this.squareUp.isInside(x, y))
		// {
		// createSquare(Square.Type.UP);
		// }
	}
	
	public void onSwipeUp(float x, float y)
	{
		if (this.squareUp.isInside(x, y))
		{
			createSquare(Square.Type.UP);
		}
	}
	
	public void onSwipeDown(float x, float y)
	{
		if (this.squareDown.isInside(x, y))
		{
			createSquare(Square.Type.DOWN);
		}
	}
	
	public void onSwipeLeft(float x, float y)
	{
		if (this.squareLeft.isInside(x, y))
		{
			createSquare(Square.Type.LEFT);
		}
	}
	
	public void onSwipeRight(float x, float y)
	{
		if (this.squareRight.isInside(x, y))
		{
			createSquare(Square.Type.RIGHT);
		}
	}
	
	private void createSquare(Type type)
	{
		Random random = new Random();

		switch (type)
		{
			case UP:
				this.squareUp = new Square(Type.UP, random.nextInt(Game.RESOLUTION_X) + 0.5f, random.nextInt(Game.RESOLUTION_Y) + 0.5f);
				break;
			case DOWN:
				this.squareDown = new Square(Type.DOWN, random.nextInt(Game.RESOLUTION_X) + 0.5f, random.nextInt(Game.RESOLUTION_Y) + 0.5f);
				break;
			case LEFT:
				this.squareLeft = new Square(Type.LEFT, random.nextInt(Game.RESOLUTION_X) + 0.5f, random.nextInt(Game.RESOLUTION_Y) + 0.5f);
				break;
			case RIGHT:
				this.squareRight = new Square(Type.RIGHT, random.nextInt(Game.RESOLUTION_X) + 0.5f, random.nextInt(Game.RESOLUTION_Y) + 0.5f);
				break;
		}
	}
	
	public void draw(int positionLocation, int colorLocation)
	{
		this.squareUp.draw(positionLocation, colorLocation);
		this.squareDown.draw(positionLocation, colorLocation);
		this.squareLeft.draw(positionLocation, colorLocation);
		this.squareRight.draw(positionLocation, colorLocation);
	}
}