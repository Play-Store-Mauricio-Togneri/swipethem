package com.mauriciotogneri.swipethem.objects;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import android.graphics.Color;
import android.opengl.GLSurfaceView;
import com.mauriciotogneri.swipethem.MainActivity;
import com.mauriciotogneri.swipethem.audio.AudioManager;
import com.mauriciotogneri.swipethem.input.InputEvent;
import com.mauriciotogneri.swipethem.media.Resources;
import com.mauriciotogneri.swipethem.objects.tiles.Tile;
import com.mauriciotogneri.swipethem.objects.tiles.Tile.TileType;
import com.mauriciotogneri.swipethem.objects.tiles.arrows.fake.FakeTileArrowDown;
import com.mauriciotogneri.swipethem.objects.tiles.arrows.fake.FakeTileArrowLeft;
import com.mauriciotogneri.swipethem.objects.tiles.arrows.fake.FakeTileArrowRight;
import com.mauriciotogneri.swipethem.objects.tiles.arrows.fake.FakeTileArrowUp;
import com.mauriciotogneri.swipethem.objects.tiles.arrows.normal.NormalTileArrowDown;
import com.mauriciotogneri.swipethem.objects.tiles.arrows.normal.NormalTileArrowLeft;
import com.mauriciotogneri.swipethem.objects.tiles.arrows.normal.NormalTileArrowRight;
import com.mauriciotogneri.swipethem.objects.tiles.arrows.normal.NormalTileArrowUp;
import com.mauriciotogneri.swipethem.util.Statistics;

public class Game
{
	private final MainActivity activity;
	private final Renderer renderer;
	private final AudioManager audioManager;

	private float time = Game.TOTAL_TIME;

	private int score = 0;
	private int level = 1;
	private int difficultyCounter = 0;
	private int difficultyLimit = Game.INITIAL_DIFFICULTY_LIMIT;

	private boolean finished = false;

	private final Object listLock = new Object();
	private final List<Tile> activeTiles = new ArrayList<Tile>();
	private final List<Tile> finishedTiles = new ArrayList<Tile>();
	
	public static final int RESOLUTION_X = 4;
	public static final int RESOLUTION_Y = 6;
	private static final int MAX_NUMBER_OF_TILES = Game.RESOLUTION_X * Game.RESOLUTION_Y;
	private static final int INITIAL_DIFFICULTY_LIMIT = 4;
	private static final int TOTAL_TIME = 60;
	
	private static final int COLOR_NORMAL = Color.argb(255, 85, 85, 85);
	private static final int COLOR_WARNING = Color.argb(255, 255, 60, 60);
	
	public Game(MainActivity activity, GLSurfaceView surfaceView)
	{
		this.activity = activity;
		this.renderer = new Renderer(activity, this, surfaceView);
		this.audioManager = new AudioManager(activity);
		this.audioManager.playAudio(Resources.Music.MUSIC);

		restart();
	}

	private void updateScore(int value)
	{
		this.score += value;

		if (this.score < 0)
		{
			this.score = 0;
		}
		
		this.activity.updateScore(this.score);
	}
	
	private void updateTimer()
	{
		int finalTime = (int)this.time;
		
		this.activity.updateTimer((finalTime > 9) ? String.valueOf(finalTime) : ("0" + finalTime), (finalTime > 9) ? Game.COLOR_NORMAL : Game.COLOR_WARNING);
		
		if ((finalTime == 0) && (!this.finished))
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
		synchronized (this.listLock)
		{
			this.time = Game.TOTAL_TIME;
			this.score = 0;
			this.difficultyCounter = 0;
			this.difficultyLimit = Game.INITIAL_DIFFICULTY_LIMIT;
			this.finished = false;
			this.activeTiles.clear();
			this.finishedTiles.clear();

			createNormalTile();
			updateScore(0);
			updateTimer();

			Statistics.sendHitNewGame();
		}
	}

	private void createNormalTile()
	{
		Tile initialTile = getTile(TileType.NORMAL_ARROW_UP, TileType.NORMAL_ARROW_DOWN, TileType.NORMAL_ARROW_LEFT, TileType.NORMAL_ARROW_RIGHT);
		this.activeTiles.add(initialTile);
	}
	
	private void createFakeTile()
	{
		Tile initialTile = getTile(TileType.FAKE_ARROW_UP, TileType.FAKE_ARROW_DOWN, TileType.FAKE_ARROW_LEFT, TileType.FAKE_ARROW_RIGHT);
		this.activeTiles.add(initialTile);
	}

	private Tile getTile(TileType... types)
	{
		Random random = new Random();

		TileType type = getTileType(types);

		int i = random.nextInt(Game.RESOLUTION_X);
		int j = random.nextInt(Game.RESOLUTION_Y);

		while (tileOccupied(i, j))
		{
			i = random.nextInt(Game.RESOLUTION_X);
			j = random.nextInt(Game.RESOLUTION_Y);
		}

		return createTile(type, i, j);
	}

	private TileType getTileType(TileType... values)
	{
		Random random = new Random();

		return values[random.nextInt(values.length)];
	}

	private boolean tileOccupied(int i, int j)
	{
		boolean result = false;

		for (Tile tile : this.activeTiles)
		{
			if (tile.isIn(i, j))
			{
				result = true;
				break;
			}
		}

		if (!result)
		{
			for (Tile tile : this.finishedTiles)
			{
				if (tile.isIn(i, j))
				{
					result = true;
					break;
				}
			}
		}

		return result;
	}

	private Tile createTile(TileType type, int i, int j)
	{
		Tile result = null;

		switch (type)
		{
			case NORMAL_ARROW_UP:
				result = new NormalTileArrowUp(i, j);
				break;
			case NORMAL_ARROW_DOWN:
				result = new NormalTileArrowDown(i, j);
				break;
			case NORMAL_ARROW_LEFT:
				result = new NormalTileArrowLeft(i, j);
				break;
			case NORMAL_ARROW_RIGHT:
				result = new NormalTileArrowRight(i, j);
				break;
			case FAKE_ARROW_UP:
				result = new FakeTileArrowUp(i, j);
				break;
			case FAKE_ARROW_DOWN:
				result = new FakeTileArrowDown(i, j);
				break;
			case FAKE_ARROW_LEFT:
				result = new FakeTileArrowLeft(i, j);
				break;
			case FAKE_ARROW_RIGHT:
				result = new FakeTileArrowRight(i, j);
				break;
		}
		
		return result;
	}
	
	public void update(float delta, int positionLocation, int colorLocation, InputEvent input)
	{
		synchronized (this.listLock)
		{
			Tile[] actives = getActiveTileList();
			Tile[] finished = getFinishedTileList();

			if (!this.finished)
			{
				this.time -= delta;
				updateTimer();
				
				processInput(input);

				for (Tile tile : actives)
				{
					tile.update(delta);
					processActiveTile(tile);
				}
				
				for (Tile tile : finished)
				{
					tile.update(delta);
					processFinishedTile(tile);
				}
			}
			
			for (Tile tile : actives)
			{
				tile.draw(positionLocation, colorLocation);
			}
			
			for (Tile tile : finished)
			{
				tile.draw(positionLocation, colorLocation);
			}
		}
	}

	private Tile[] getActiveTileList()
	{
		Tile[] result = new Tile[this.activeTiles.size()];
		this.activeTiles.toArray(result);

		return result;
	}

	private Tile[] getFinishedTileList()
	{
		Tile[] result = new Tile[this.finishedTiles.size()];
		this.finishedTiles.toArray(result);

		return result;
	}
	
	private void processInput(InputEvent input)
	{
		if (input.isValid())
		{
			Tile tile = getTile(input.x, input.y);
			
			if (tile != null)
			{
				if (tile.acceptsInput(input.type))
				{
					tile.processInput(input.type);
				}
			}
		}
	}

	private void processActiveTile(Tile tile)
	{
		if (tile.isSwipedOk())
		{
			removeTile(tile);
			createNormalTile();
			
			updateScore(1);

			playSound(Resources.Sounds.SWIPE_OK);
			
			increaseDifficulty();
		}
		else if (tile.isSwipedFail())
		{
			removeTile(tile);
			createNormalTile();

			updateScore(-1);
			
			this.activity.vibrate();
		}
		else if (tile.isTimeOut())
		{
			removeTile(tile);
			createNormalTile();

			updateScore(-1);
			
			playSound(Resources.Sounds.SWIPE_FAIL);
		}
		else if (tile.isDisappeared())
		{
			removeTile(tile);
		}
	}

	private void processFinishedTile(Tile tile)
	{
		if (tile.isFinished())
		{
			this.finishedTiles.remove(tile);
		}
	}
	
	private void removeTile(Tile tile)
	{
		this.activeTiles.remove(tile);
		this.finishedTiles.add(tile);
		
		if (tile.isFake())
		{
			createFakeTile();
		}
	}

	private void increaseDifficulty()
	{
		this.difficultyCounter++;

		if (this.difficultyCounter == this.difficultyLimit)
		{
			this.level++;
			this.difficultyCounter = 0;
			this.difficultyLimit += 3;
			
			if ((this.level % 3) == 0)
			{
				createFakeTile();
			}

			if ((this.activeTiles.size() + this.finishedTiles.size()) < Game.MAX_NUMBER_OF_TILES)
			{
				createNormalTile();
			}
		}
	}
	
	private void playSound(String soundPath)
	{
		this.audioManager.playSound(soundPath);
	}
	
	private Tile getTile(int x, int y)
	{
		Tile result = null;

		for (Tile tile : this.activeTiles)
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