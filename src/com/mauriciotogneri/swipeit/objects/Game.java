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
	private static final int TOTAL_TIME = 10;

	private final MainActivity activity;
	private final Renderer renderer;
	private final AudioManager audioManager;

	private float time = Game.TOTAL_TIME;
	private int score = 0;
	private int difficultyCounter = 0;
	private boolean finished = false;

	private final List<Tile> tiles = new ArrayList<Tile>();
	
	public Game(MainActivity activity, GLSurfaceView surfaceView)
	{
		this.activity = activity;
		this.renderer = new Renderer(activity, this, surfaceView);
		this.audioManager = new AudioManager(activity);
		this.audioManager.playAudio("audio/music/music.ogg");

		restart();
	}

	public void updateScore()
	{
		this.activity.updateScore(this.score);
	}
	
	public void updateTimer()
	{
		this.activity.updateTimer((int)this.time);
		
		if ((this.time < 1) && (!this.finished))
		{
			this.finished = true;
			this.activity.showFinalScore(this.score);
		}
	}

	public Renderer getRenderer()
	{
		return this.renderer;
	}

	public void restart()
	{
		this.time = Game.TOTAL_TIME;
		this.score = 0;
		this.difficultyCounter = 0;
		this.finished = false;
		this.tiles.clear();
		
		createNewTile();
		updateScore();
		updateTimer();
	}

	private void createNewTile()
	{
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
		if (!this.finished)
		{
			this.time -= delta;
			updateTimer();

			processInput(input);
		}

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

						this.audioManager.playSound("audio/sound/good.ogg");
					}
					else
					{
						this.audioManager.playSound("audio/sound/bad.ogg");
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
		if (this.audioManager != null)
		{
			this.audioManager.resumeAudio();
		}
		
		if (this.renderer != null)
		{
			this.renderer.resume();
		}
	}
	
	public void pause(boolean finishing)
	{
		if (this.audioManager != null)
		{
			this.audioManager.pauseAudio();
		}
		
		if (this.renderer != null)
		{
			this.renderer.pause(finishing);
		}
	}

	public void stop()
	{
		if (this.audioManager != null)
		{
			this.audioManager.stopAudio();
		}
	}
}