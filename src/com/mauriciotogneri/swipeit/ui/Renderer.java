package com.mauriciotogneri.swipeit.ui;

import java.util.Random;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import android.content.Context;
import android.graphics.Color;
import android.opengl.GLES20;
import android.opengl.Matrix;
import com.mauriciotogneri.swipeit.R;
import com.mauriciotogneri.swipeit.objects.Square;
import com.mauriciotogneri.swipeit.util.ShaderHelper;
import com.mauriciotogneri.swipeit.util.TextResourceReader;

public class Renderer implements android.opengl.GLSurfaceView.Renderer
{
	private int width = 0;
	private int height = 0;
	
	private static final String U_MATRIX = "u_Matrix";
	private static final String U_COLOR = "u_Color";
	private static final String A_POSITION = "a_Position";
	
	private final Context context;
	
	private final float[] projectionMatrix = new float[16];
	
	private int program;
	private int matrixLocation;
	private int positionLocation;
	private int colorLocation;

	private static final int RESOLUTION_X = 7;
	private static final int RESOLUTION_Y = 11;

	private Square square;
	
	public Renderer(Context context)
	{
		this.context = context;
	}

	public void onSwipeUp(float x, float y)
	{
		float screenX = getScreenX(x);
		float screenY = getScreenY(y);

		if (this.square.isInside(screenX, screenY))
		{
			createSquare();
		}
	}

	public void onSwipeDown(float x, float y)
	{
		float screenX = getScreenX(x);
		float screenY = getScreenY(y);

		if (this.square.isInside(screenX, screenY))
		{
			createSquare();
		}
	}

	public void onSwipeLeft(float x, float y)
	{
		float screenX = getScreenX(x);
		float screenY = getScreenY(y);

		if (this.square.isInside(screenX, screenY))
		{
			createSquare();
		}
	}

	public void onSwipeRight(float x, float y)
	{
		float screenX = getScreenX(x);
		float screenY = getScreenY(y);

		if (this.square.isInside(screenX, screenY))
		{
			createSquare();
		}
	}

	private float getScreenX(float x)
	{
		return x / this.width * Renderer.RESOLUTION_X;
	}
	
	private float getScreenY(float y)
	{
		return Renderer.RESOLUTION_Y - (y / this.height * Renderer.RESOLUTION_Y);
	}
	
	private void createSquare()
	{
		Random random = new Random();
		this.square = new Square(this.positionLocation, this.colorLocation, Color.BLUE, random.nextInt(Renderer.RESOLUTION_X) + 0.5f, random.nextInt(Renderer.RESOLUTION_Y) + 0.5f);
	}

	@Override
	public void onSurfaceCreated(GL10 unused, EGLConfig config)
	{
		GLES20.glClearColor(1f, 0f, 0f, 1f);

		String vertexShaderSource = TextResourceReader.readTextFileFromResource(this.context, R.raw.vertex_shader);
		String fragmentShaderSource = TextResourceReader.readTextFileFromResource(this.context, R.raw.fragment_shader);

		int vertexShader = ShaderHelper.compileVertexShader(vertexShaderSource);
		int fragmentShader = ShaderHelper.compileFragmentShader(fragmentShaderSource);

		this.program = ShaderHelper.linkProgram(vertexShader, fragmentShader);

		GLES20.glUseProgram(this.program);

		this.matrixLocation = GLES20.glGetUniformLocation(this.program, Renderer.U_MATRIX);
		this.colorLocation = GLES20.glGetUniformLocation(this.program, Renderer.U_COLOR);
		this.positionLocation = GLES20.glGetAttribLocation(this.program, Renderer.A_POSITION);
		
		createSquare();

		// gl.glEnable(GL10.GL_BLEND);
		// gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		// gl.glEnable(GL10.GL_DEPTH_TEST);
		// gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		// gl.glEnable(GL10.GL_ALPHA_BITS);
		// gl.glEnable(GL10.GL_MULTISAMPLE);
		// gl.glEnable(GL10.GL_SMOOTH);
		// gl.glShadeModel(GL10.GL_SMOOTH);
		// gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);
		// gl.glHint(GL10.GL_POLYGON_SMOOTH_HINT, GL10.GL_NICEST);
		// gl.glHint(GL10.GL_LINE_SMOOTH_HINT, GL10.GL_NICEST);
		// gl.glHint(GL10.GL_POINT_SMOOTH_HINT, GL10.GL_NICEST);
	}
	
	@Override
	public void onSurfaceChanged(GL10 unused, int width, int height)
	{
		this.width = width;
		this.height = height;

		GLES20.glViewport(0, 0, width, height);

		// float aspectRatio = this.width > this.height ? (float)this.width / (float)this.height :
		// (float)this.height / (float)this.width;

		// Matrix.orthoM(this.projectionMatrix, 0, -Renderer.RESOLUTION_X / 2, Renderer.RESOLUTION_X / 2,
		// -(Renderer.RESOLUTION_Y / 2), (Renderer.RESOLUTION_Y / 2), -1f, 1f);
		Matrix.orthoM(this.projectionMatrix, 0, 0, Renderer.RESOLUTION_X, 0, Renderer.RESOLUTION_Y, -1f, 1f);
	}
	
	@Override
	public void onDrawFrame(GL10 unused)
	{
		GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
		GLES20.glUniformMatrix4fv(this.matrixLocation, 1, false, this.projectionMatrix, 0);
		
		this.square.draw();
	}
}