package com.mauriciotogneri.swipeit.shapes;

import java.util.ArrayList;
import java.util.List;
import android.graphics.Color;
import com.mauriciotogneri.swipeit.shapes.figures.arrows.ArrowDown;
import com.mauriciotogneri.swipeit.shapes.figures.arrows.ArrowLeft;
import com.mauriciotogneri.swipeit.shapes.figures.arrows.ArrowRight;
import com.mauriciotogneri.swipeit.shapes.figures.arrows.ArrowUp;
import com.mauriciotogneri.swipeit.shapes.figures.dots.Dot;

public class Figure
{
	private final List<Shape> shapes = new ArrayList<Shape>();

	private Figure()
	{
	}
	
	public static Figure getArrowUp(float x, float y, float width)
	{
		Figure result = new Figure();
		
		result.add(new ArrowUp(x, y, width, Color.WHITE));
		
		return result;
	}
	
	public static Figure getArrowDown(float x, float y, float width)
	{
		Figure result = new Figure();
		
		result.add(new ArrowDown(x, y, width, Color.WHITE));
		
		return result;
	}

	public static Figure getArrowLeft(float x, float y, float width)
	{
		Figure result = new Figure();

		result.add(new ArrowLeft(x, y, width, Color.WHITE));

		return result;
	}

	public static Figure getArrowRight(float x, float y, float width)
	{
		Figure result = new Figure();

		result.add(new ArrowRight(x, y, width, Color.WHITE));

		return result;
	}

	public static Figure getSingleDot(float x, float y, float width)
	{
		Figure result = new Figure();

		result.add(new Dot(x, y, width, Color.WHITE));

		return result;
	}

	public static Figure getDoubleDot(float x, float y, float width, float side)
	{
		Figure result = new Figure();

		result.add(new Dot(x - (side / 2), y, width, Color.WHITE));
		result.add(new Dot(x + (side / 2), y, width, Color.WHITE));

		return result;
	}

	public static Figure getTripleDot(float x, float y, float width, float side)
	{
		Figure result = new Figure();

		result.add(new Dot(x, y + (side / 2), width, Color.WHITE));
		result.add(new Dot(x - (side / 2), y - (side / 2), width, Color.WHITE));
		result.add(new Dot(x + (side / 2), y - (side / 2), width, Color.WHITE));

		return result;
	}

	public static Figure getQuadrupleDot(float x, float y, float width, float side)
	{
		Figure result = new Figure();

		result.add(new Dot(x - (side / 2), y + (side / 2), width, Color.WHITE));
		result.add(new Dot(x - (side / 2), y - (side / 2), width, Color.WHITE));
		result.add(new Dot(x + (side / 2), y + (side / 2), width, Color.WHITE));
		result.add(new Dot(x + (side / 2), y - (side / 2), width, Color.WHITE));

		return result;
	}

	public void add(Shape shape)
	{
		this.shapes.add(shape);
	}

	public void draw(int positionLocation, int colorLocation, float alpha)
	{
		for (Shape shape : this.shapes)
		{
			shape.draw(positionLocation, colorLocation, alpha);
		}
	}
}