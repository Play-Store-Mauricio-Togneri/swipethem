package com.mauriciotogneri.swipethem.objects;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import com.mauriciotogneri.swipethem.R;
import com.mauriciotogneri.swipethem.input.GestureManager;
import com.mauriciotogneri.swipethem.input.InputEvent;
import com.mauriciotogneri.swipethem.input.InputEvent.InputType;
import com.mauriciotogneri.swipethem.util.ShaderHelper;
import com.mauriciotogneri.swipethem.util.TextResourceReader;

public class Renderer implements android.opengl.GLSurfaceView.Renderer
{
	private final Context context;
	private final Game game;
	
	private long startTime;
	
	// OpenGL
	private int width = 0;
	private int height = 0;
	
	private static final String U_MATRIX = "u_Matrix";
	private static final String U_COLOR = "u_Color";
	private static final String A_POSITION = "a_Position";
	
	private final float[] projectionMatrix = new float[16];
	
	private int matrixLocation;
	private int positionLocation;
	private int colorLocation;
	
	// input
	private final Object inputLock = new Object();
	private final InputEvent lastInput = new InputEvent();
	
	// state
	private RendererStatus state = null;
	private final Object stateChangedLock = new Object();
	
	private enum RendererStatus
	{
		RUNNING, IDLE, PAUSED, FINISHED
	}
	
	public Renderer(Context context, Game game, GLSurfaceView surfaceView)
	{
		this.context = context;
		this.game = game;
		this.startTime = System.nanoTime();
		
		surfaceView.setOnTouchListener(new GestureManager()
		{
			@Override
			public void onTapDown(float x, float y)
			{
				setInput(InputType.TAP_DOWN, getScreenX(x), getScreenY(y));
			}
			
			@Override
			public void onTapUp(float x, float y)
			{
				setInput(InputType.TAP_UP, getScreenX(x), getScreenY(y));
			}
			
			@Override
			public void onSwipeUp(float x, float y)
			{
				setInput(InputType.SWIPE_UP, getScreenX(x), getScreenY(y));
			}
			
			@Override
			public void onSwipeDown(float x, float y)
			{
				setInput(InputType.SWIPE_DOWN, getScreenX(x), getScreenY(y));
			}
			
			@Override
			public void onSwipeLeft(float x, float y)
			{
				setInput(InputType.SWIPE_LEFT, getScreenX(x), getScreenY(y));
			}
			
			@Override
			public void onSwipeRight(float x, float y)
			{
				setInput(InputType.SWIPE_RIGHT, getScreenX(x), getScreenY(y));
			}
		});
	}
	
	private void setInput(InputType type, int x, int y)
	{
		synchronized (this.inputLock)
		{
			this.lastInput.set(type, x, y);
		}
	}
	
	private int getScreenX(float x)
	{
		return (int)((x / this.width) * Game.RESOLUTION_X);
	}
	
	private int getScreenY(float y)
	{
		return (int)(Game.RESOLUTION_Y - ((y / this.height) * Game.RESOLUTION_Y));
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
		
		int program = ShaderHelper.linkProgram(vertexShaderSource, fragmentShaderSource);
		GLES20.glUseProgram(program);
		
		this.matrixLocation = GLES20.glGetUniformLocation(program, Renderer.U_MATRIX);
		this.colorLocation = GLES20.glGetUniformLocation(program, Renderer.U_COLOR);
		this.positionLocation = GLES20.glGetAttribLocation(program, Renderer.A_POSITION);
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
		
		this.startTime = System.nanoTime();
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
			float delta = (currentTime - this.startTime) / 1E9f;
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
		}
	}
}