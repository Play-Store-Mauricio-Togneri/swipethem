package com.mauriciotogneri.swipeit.objects;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import com.mauriciotogneri.swipeit.R;
import com.mauriciotogneri.swipeit.input.GestureManager;
import com.mauriciotogneri.swipeit.input.InputEvent;
import com.mauriciotogneri.swipeit.util.ShaderHelper;
import com.mauriciotogneri.swipeit.util.TextResourceReader;

public class Renderer implements android.opengl.GLSurfaceView.Renderer
{
	private final Game game;
	private final GLSurfaceView surfaceView;
	
	private int width = 0;
	private int height = 0;
	
	private long startTime;
	
	private static final String U_MATRIX = "u_Matrix";
	private static final String U_COLOR = "u_Color";
	private static final String A_POSITION = "a_Position";
	
	private final Context context;
	
	private final float[] projectionMatrix = new float[16];
	
	private int program;
	private int matrixLocation;
	private int positionLocation;
	private int colorLocation;
	
	private final Object inputLock = new Object();
	private final InputEvent lastInput = new InputEvent();
	
	// state
	private RendererStatus state = null;
	private final Object stateChangedLock = new Object();

	// engine status
	private enum RendererStatus
	{
		RUNNING, IDLE, PAUSED, FINISHED
	}

	public Renderer(Context context, final Game game, GLSurfaceView surfaceView)
	{
		this.context = context;
		this.game = game;
		this.surfaceView = surfaceView;
		this.startTime = System.nanoTime();
		
		surfaceView.setOnTouchListener(new GestureManager()
		{
			@Override
			public void onTapDown(float x, float y)
			{
				setInput(InputEvent.Type.TAP_DOWN, getScreenX(x), getScreenY(y));
			}
			
			@Override
			public void onTapUp(float x, float y)
			{
				setInput(InputEvent.Type.TAP_UP, getScreenX(x), getScreenY(y));
			}
			
			@Override
			public void onSwipeUp(float x, float y)
			{
				setInput(InputEvent.Type.SWIPE_UP, getScreenX(x), getScreenY(y));
			}
			
			@Override
			public void onSwipeDown(float x, float y)
			{
				setInput(InputEvent.Type.SWIPE_DOWN, getScreenX(x), getScreenY(y));
			}

			@Override
			public void onSwipeLeft(float x, float y)
			{
				setInput(InputEvent.Type.SWIPE_LEFT, getScreenX(x), getScreenY(y));
			}

			@Override
			public void onSwipeRight(float x, float y)
			{
				setInput(InputEvent.Type.SWIPE_RIGHT, getScreenX(x), getScreenY(y));
			}
		});
	}

	private void setInput(InputEvent.Type type, float x, float y)
	{
		synchronized (this.inputLock)
		{
			this.lastInput.set(type, x, y);
		}
	}

	private float getScreenX(float x)
	{
		return x / this.width * Game.RESOLUTION_X;
	}

	private float getScreenY(float y)
	{
		return Game.RESOLUTION_Y - (y / this.height * Game.RESOLUTION_Y);
	}

	@Override
	public void onSurfaceCreated(GL10 unused, EGLConfig config)
	{
		GLES20.glEnable(GLES20.GL_BLEND);
		GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
		GLES20.glBlendFunc(GLES20.GL_ONE, GLES20.GL_ONE_MINUS_SRC_ALPHA);
		
		GLES20.glClearColor(1f, 1f, 1f, 1f);

		String vertexShaderSource = TextResourceReader.readTextFileFromResource(this.context, R.raw.vertex_shader);
		String fragmentShaderSource = TextResourceReader.readTextFileFromResource(this.context, R.raw.fragment_shader);

		this.program = ShaderHelper.linkProgram(vertexShaderSource, fragmentShaderSource);

		GLES20.glUseProgram(this.program);

		this.matrixLocation = GLES20.glGetUniformLocation(this.program, Renderer.U_MATRIX);
		this.colorLocation = GLES20.glGetUniformLocation(this.program, Renderer.U_COLOR);
		this.positionLocation = GLES20.glGetAttribLocation(this.program, Renderer.A_POSITION);
	}
	
	@Override
	public void onSurfaceChanged(GL10 unused, int width, int height)
	{
		this.width = width;
		this.height = height;

		GLES20.glViewport(0, 0, width, height);

		Matrix.orthoM(this.projectionMatrix, 0, 0, Game.RESOLUTION_X, 0, Game.RESOLUTION_Y, -1f, 1f);
		
		synchronized (this.stateChangedLock)
		{
			this.state = RendererStatus.RUNNING;
		}

		// TODO: reload textures
	}

	private void update(float delta)
	{
		GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
		GLES20.glUniformMatrix4fv(this.matrixLocation, 1, false, this.projectionMatrix, 0);
		
		synchronized (this.inputLock)
		{
			this.game.update(delta, this.positionLocation, this.colorLocation, this.lastInput);
			this.lastInput.clear();
		}
	}
	
	@Override
	public void onDrawFrame(GL10 unused)
	{
		RendererStatus status = null;
		
		synchronized (this.stateChangedLock)
		{
			status = this.state;
		}
		
		if (status == RendererStatus.RUNNING)
		{
			long currentTime = System.nanoTime();
			float delta = (currentTime - this.startTime) / 1000000000f;
			this.startTime = currentTime;
			
			// FPS.log(currentTime);
			
			update(delta);
		}
		else if ((status == RendererStatus.PAUSED) || (status == RendererStatus.FINISHED))
		{
			synchronized (this.stateChangedLock)
			{
				this.state = RendererStatus.IDLE;
				this.stateChangedLock.notifyAll();
			}
		}
	}
	
	public void pause(boolean finishing)
	{
		synchronized (this.stateChangedLock)
		{
			if (this.state == RendererStatus.RUNNING)
			{
				if (finishing)
				{
					this.state = RendererStatus.FINISHED;
				}
				else
				{
					this.state = RendererStatus.PAUSED;
				}
				
				while (true)
				{
					try
					{
						this.stateChangedLock.wait();
						break;
					}
					catch (Exception e)
					{
					}
				}
			}

			if (this.surfaceView != null)
			{
				this.surfaceView.onPause();
			}
		}
	}

	public void resume()
	{
		if (this.surfaceView != null)
		{
			this.surfaceView.onResume();
		}
	}
}