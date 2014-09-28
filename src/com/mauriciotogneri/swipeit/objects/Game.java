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
	private static final int MAX_NUMBER_OF_TILES = Game.RESOLUTION_X * Game.RESOLUTION_Y;
	private static final int DIFFICULTY_LIMIT = 3;

	private final MainActivity activity;
	private final Renderer renderer;

	private int lives = 3;
	private int score = 0;
	private int difficultyCounter = 0;

	private final List<Tile> tiles = new ArrayList<Tile>();
	
	public Game(MainActivity activity, GLSurfaceView surfaceView)
	{
		this.activity = activity;
		this.renderer = new Renderer(activity, this, surfaceView);

		createNewTile();
		setScore();
		setLives();
	}

	public void setScore()
	{
		this.activity.setScore(this.score);
	}
	
	public void setLives()
	{
		this.activity.setLives(this.lives);
	}

	public Renderer getRenderer()
	{
		return this.renderer;
	}

	private void createNewTile()
	{
		// Random random = new Random();
		//
		// for (int i = 0; i < Game.RESOLUTION_X; i++)
		// {
		// for (int j = 0; j < Game.RESOLUTION_Y; j++)
		// {
		// Tile tile = createTile(Type.values()[random.nextInt(Type.values().length)], i, j);
		// this.tiles.add(tile);
		// }
		// }
		
		Tile initialTile = getNewTile();
		this.tiles.add(initialTile);
	}

	private Tile getNewTile()
	{
		Random random = new Random();

		Type type = Type.values()[random.nextInt(Type.values().length)];

		int i = random.nextInt(Game.RESOLUTION_X);
		int j = random.nextInt(Game.RESOLUTION_Y);

		while (tileOccupied(i, j))
		{
			i = random.nextInt(Game.RESOLUTION_X);
			j = random.nextInt(Game.RESOLUTION_Y);
		}

		return createTile(type, i, j);
	}

	private boolean tileOccupied(int i, int j)
	{
		boolean result = false;

		for (Tile tile : this.tiles)
		{
			if (tile.isIn(i, j))
			{
				result = true;
				break;
			}
		}

		return result;
	}

	private Tile createTile(Type type, int i, int j)
	{
		Tile result = null;

		switch (type)
		{
			case UP:
				result = new Tile(Type.UP, i, j);
				break;
			case DOWN:
				result = new Tile(Type.DOWN, i, j);
				break;
			case LEFT:
				result = new Tile(Type.LEFT, i, j);
				break;
			case RIGHT:
				result = new Tile(Type.RIGHT, i, j);
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

			if (tile != null)
			{
				if (tile.disables(input.type))
				{
					this.tiles.remove(tile);
					createNewTile();

					this.score++;
					setScore();
					
					this.difficultyCounter++;
					
					if (this.difficultyCounter == Game.DIFFICULTY_LIMIT)
					{
						this.difficultyCounter = 0;
						
						if (this.tiles.size() < Game.MAX_NUMBER_OF_TILES)
						{
							createNewTile();
						}
					}
				}
				else
				{
					this.lives--;
					setLives();
				}
			}
		}
	}
	
	private Tile getTile(int x, int y)
	{
		Tile result = null;

		for (Tile tile : this.tiles)
		{
			if (tile.isIn(x, y))
			{
				result = tile;
				break;
			}
		}

		return result;
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