package com.mauriciotogneri.swipeit.objects;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import android.opengl.GLSurfaceView;
import com.mauriciotogneri.swipeit.MainActivity;
import com.mauriciotogneri.swipeit.input.InputEvent;
import com.mauriciotogneri.swipeit.objects.Tile.Type;

public class Game
{
	public static final int RESOLUTION_X = 5;
	public static final int RESOLUTION_Y = 8;

	private final MainActivity activity;
	private final Renderer renderer;

	private int score = 0;

	private final List<Tile> tiles = new ArrayList<Tile>();
	
	public Game(MainActivity activity, GLSurfaceView surfaceView)
	{
		this.activity = activity;
		this.renderer = new Renderer(activity, this, surfaceView);

		initializeTiles();
		setScore();
	}

	private void initializeTiles()
	{
		Random random = new Random();

		for (int i = 0; i < Game.RESOLUTION_X; i++)
		{
			for (int j = 0; j < Game.RESOLUTION_Y; j++)
			{
				Tile tile = createTile(Type.values()[random.nextInt(Type.values().length)], i, j);
				this.tiles.add(tile);
			}
		}
	}

	public void setScore()
	{
		this.activity.setScore(this.score);
	}

	public Renderer getRenderer()
	{
		return this.renderer;
	}

	private Tile getTile(float x, float y)
	{
		Tile result = null;

		for (Tile tile : this.tiles)
		{
			if (tile.isInside(x, y))
			{
				result = tile;
				break;
			}
		}

		return result;
	}
	
	private Tile createTile(Type type, int x, int y)
	{
		Tile result = null;

		switch (type)
		{
			case UP:
				result = new Tile(Type.UP, x + Tile.BLOCK_SIDE, y + Tile.BLOCK_SIDE);
				break;
			case DOWN:
				result = new Tile(Type.DOWN, x + Tile.BLOCK_SIDE, y + Tile.BLOCK_SIDE);
				break;
			case LEFT:
				result = new Tile(Type.LEFT, x + Tile.BLOCK_SIDE, y + Tile.BLOCK_SIDE);
				break;
			case RIGHT:
				result = new Tile(Type.RIGHT, x + Tile.BLOCK_SIDE, y + Tile.BLOCK_SIDE);
				break;
		}
		
		return result;
	}
	
	public void update(float delta, int positionLocation, int colorLocation, InputEvent input)
	{
		processInput(input);
		
		for (Tile tile : this.tiles)
		{
			tile.draw(positionLocation, colorLocation);
		}
	}
	
	private void processInput(InputEvent input)
	{
		if (input.isValid())
		{
			Tile tile = getTile(input.x, input.y);

			if ((tile != null) && tile.disables(input.type))
			{
				this.tiles.remove(tile);
				this.score++;
				setScore();
			}
		}
	}
	
	public void resume()
	{
		if (this.renderer != null)
		{
			this.renderer.resume();
		}
	}
	
	public void pause(boolean finishing)
	{
		if (this.renderer != null)
		{
			this.renderer.pause(finishing);
		}
	}
}