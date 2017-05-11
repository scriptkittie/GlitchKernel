package io.laniakia.ui;

import java.awt.Point;
import java.awt.Rectangle;

public class SelectionPoint 
{
	private Point startPoint;
	private Point endPoint;
	private Rectangle selectionRectangle;
	private Rectangle translatedRectangle;
	
	public SelectionPoint(Point startPoint, Point endPoint)
	{
		this.startPoint = startPoint;
		this.endPoint = endPoint;
		
		Rectangle selectionRectangle = new Rectangle(startPoint);
		selectionRectangle.add(endPoint);
		this.selectionRectangle = selectionRectangle;
	}
	
	public SelectionPoint(Rectangle rectangle)
	{
		this.selectionRectangle = rectangle;
	}
	
	public Point getStartPoint() 
	{
		return startPoint;
	}
	public void setStartPoint(Point startPoint) 
	{
		this.startPoint = startPoint;
	}
	public Point getEndPoint() 
	{
		return endPoint;
	}
	public void setEndPoint(Point endPoint) 
	{
		this.endPoint = endPoint;
		selectionRectangle = new Rectangle(startPoint);
		selectionRectangle.add(endPoint);
	}

	public Rectangle getRectangle() 
	{
		return selectionRectangle;
	}

	public void setRectangle(Rectangle rectangle) 
	{
		this.selectionRectangle = rectangle;
	}

	public Rectangle getTranslatedRectangle() 
	{
		return translatedRectangle;
	}

	public void setTranslatedRectangle(Rectangle translatedRectangle) 
	{
		this.translatedRectangle = translatedRectangle;
	}
}
