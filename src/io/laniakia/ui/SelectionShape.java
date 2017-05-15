package io.laniakia.ui;

import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import io.laniakia.util.SelectionTypes;

public class SelectionShape 
{
	private Point startPoint;
	private Point endPoint;
	private Rectangle selectionRectangle;
	private Rectangle translatedRectangle;
	private List<Point> freeHandSelectionPointList;
	private List<Point> freeHandTranslatedSelectionPointList;
	private boolean freeHandSelectionConnected;
	private SelectionTypes selectionType;
	private Polygon translatedPolygon;
	
	public SelectionShape(Point startPoint, Point endPoint, SelectionTypes selectionType)
	{
		this.startPoint = startPoint;
		this.endPoint = endPoint;
		this.freeHandSelectionPointList = new ArrayList<Point>();
		this.freeHandTranslatedSelectionPointList = new ArrayList<Point>();
		this.freeHandSelectionConnected = false;
		this.selectionType = selectionType;
		
		Rectangle selectionRectangle = new Rectangle(startPoint);
		selectionRectangle.add(endPoint);
		this.selectionRectangle = selectionRectangle;
	}
	
	public SelectionShape(Rectangle rectangle)
	{
		this.selectionRectangle = rectangle;
		this.freeHandSelectionPointList = new ArrayList<Point>();
		this.freeHandTranslatedSelectionPointList = new ArrayList<Point>();
		this.selectionType = SelectionTypes.RECTANGLE;
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

	public List<Point> getFreeHandSelectionPointList() 
	{
		return freeHandSelectionPointList;
	}

	public void setFreeHandSelectionPointList(List<Point> freeHandSelectionPointList) 
	{
		this.freeHandSelectionPointList = freeHandSelectionPointList;
	}
	
	public void addFreeHandSelectionPoint(Point selectionPoint)
	{
		this.freeHandSelectionPointList.add(selectionPoint);
	}

	public boolean isFreeHandSelectionConnected() 
	{
		return freeHandSelectionConnected;
	}

	public void setFreeHandSelectionConnected(boolean freeHandSelectionConnected) 
	{
		this.freeHandSelectionConnected = freeHandSelectionConnected;
	}

	public SelectionTypes getSelectionType() 
	{
		return selectionType;
	}

	public void setSelectionType(SelectionTypes selectionType) 
	{
		this.selectionType = selectionType;
	}

	public List<Point> getFreeHandTranslatedSelectionPointList() 
	{
		return freeHandTranslatedSelectionPointList;
	}

	public void setFreeHandTranslatedSelectionPointList(List<Point> freeHandTranslatedSelectionPointList) 
	{
		this.freeHandTranslatedSelectionPointList = freeHandTranslatedSelectionPointList;
	}
	
	public void addTranslatedFreeHandPoint(Point inputPoint)
	{
		this.freeHandTranslatedSelectionPointList.add(inputPoint);
	}

	public Polygon getTranslatedPolygon() {
		return translatedPolygon;
	}

	public void setTranslatedPolygon(Polygon translatedPolygon) {
		this.translatedPolygon = translatedPolygon;
	}
}
