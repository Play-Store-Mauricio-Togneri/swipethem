package com.mauriciotogneri.swipeit.objects;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import android.opengl.GLSurfaceView;
import com.mauriciotogneri.swipeit.MainActivity;
import com.mauriciotogneri.swipeit.audio.AudioManager;
import com.mauriciotogneri.swipeit.input.InputEvent;
import com.mauriciotogneri.swipeit.objects.Tile.TileType;

public class Game
{
	public static final int RESOLUTION_X = 4;
	public static final int RESOLUTION_Y = 6;
	private static final int MAX_NUMBER_OF_TILES = Game.RESOLUTION_X * Game.RESOLUTION_Y;
	private static final int DIFFICULTY_LIMIT = 10;

	private final MainActivity activity;
	private final Renderer renderer;
	private final AudioManager audioManager;

	private float time = 60;
	private int score = 0;
	private int difficultyCounter = 0;

	private final List<Tile> tiles = new ArrayList<Tile>();
	
	public Game(MainActivity activity, GLSurfaceView surfaceView)
	{
		this.activity = activity;
		this.renderer = new Renderer(activity, this, surfaceView);
		this.audioManager = new AudioManager(activity);

		createNewTile();
		updateScore();
		updateTimer();
	}

	public void updateScore()
	{
		this.activity.updateScore(this.score);
	}
	
	public void updateTimer()
	{
		this.activity.updateTimer((int)this.time);
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

		TileType type = TileType.values()[random.nextInt(TileType.values().length)];

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

	private Tile createTile(TileType type, int i, int j)
	{
		Tile result = null;

		switch (type)
		{
			case UP:
				result = new Tile(TileType.UP, i, j);
				break;
			case DOWN:
				result = new Tile(TileType.DOWN, i, j);
				break;
			case LEFT:
				result = new Tile(TileType.LEFT, i, j);
				break;
			case RIGHT:
				result = new Tile(TileType.RIGHT, i, j);
				break;
		}
		
		return result;
	}
	
	public void update(float delta, int positionLocation, int colorLocation, InputEvent input)
	{
		this.time -= delta;
		updateTimer();

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
				if (tile.accepts(input.type))
				{
					this.tiles.remove(tile);
					createNewTile();
					
					if (tile.disables(input.type))
					{
						this.score++;
						updateScore();

						this.difficultyCounter++;

						if (this.difficultyCounter == Game.DIFFICULTY_LIMIT)
						{
							this.difficultyCounter = 0;

							if (this.tiles.size() < Game.MAX_NUMBER_OF_TILES)
							{
								createNewTile();
							}
						}

						this.audioManager.playSound("audio/good.ogg");
					}
					else
					{
						this.audioManager.playSound("audio/bad.ogg");
					}
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