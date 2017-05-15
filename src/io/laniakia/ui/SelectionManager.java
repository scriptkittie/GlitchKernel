package io.laniakia.ui;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.util.List;

import io.laniakia.algo.GlitchAlgorithm;
import io.laniakia.algo.GlitchController;
import io.laniakia.util.SelectionTypes;

public class SelectionManager 
{
	private GlitchController glitchController;
	private GlitchArt glitchArt;
	
	public SelectionManager(GlitchController glitchController, GlitchArt glitchArt)
	{
		this.glitchArt = glitchArt;
		this.glitchController = glitchController;
	}
	
	public void processImageRenderPaint(Graphics g)
	{
		if(this.glitchArt.jAlgorithmList.getSelectedIndex() >= 0 && this.glitchController.getAlgorithmList().get(this.glitchArt.jAlgorithmList.getSelectedIndex()).getSelectionPoint() != null)
		{
			if(this.glitchArt.selectionType.getSelectedIndex() == 0)
			{
				drawSelectedRectangle(this.glitchController.getAlgorithmList().get(this.glitchArt.jAlgorithmList.getSelectedIndex()).getSelectionPoint(), g);
			}
			else if(this.glitchArt.selectionType.getSelectedIndex() == 1)
			{
				drawFreeHandShape(this.glitchController.getAlgorithmList().get(this.glitchArt.jAlgorithmList.getSelectedIndex()).getSelectionPoint(), g);
			}
		}
	}
	
	public void processMousePress(MouseEvent mouseEvent)
	{
		if(!IsDrawable())
		{
			return;
		}
		
		GlitchAlgorithm ga = this.glitchController.getAlgorithmList().get(this.glitchArt.jAlgorithmList.getSelectedIndex());
		if(this.glitchArt.selectionType.getSelectedIndex() == 0)
		{
			ga.setSelectionPoint(new SelectionShape(mouseEvent.getPoint(), mouseEvent.getPoint(), SelectionTypes.RECTANGLE));
		}
		else if(this.glitchArt.selectionType.getSelectedIndex() == 1)
		{
			ga.setSelectionPoint(new SelectionShape(mouseEvent.getPoint(), mouseEvent.getPoint(), SelectionTypes.FREEHAND));
			ga.getSelectionPoint().addFreeHandSelectionPoint(mouseEvent.getPoint());
		}
	}
	
	public void processMouseDrag(MouseEvent mouseEvent)
	{
		if(!IsDrawable())
		{
			return;
		}
		
		GlitchAlgorithm ga = this.glitchController.getAlgorithmList().get(this.glitchArt.jAlgorithmList.getSelectedIndex());
		boolean limitsBroken = checkDrawableLimits(mouseEvent);
		if(limitsBroken)
		{
			this.glitchArt.lblImageRender.repaint();
			return;
		}
		if(this.glitchArt.selectionType.getSelectedIndex() == 0)
		{
			ga.getSelectionPoint().setEndPoint(mouseEvent.getPoint());
		}
		else if(this.glitchArt.selectionType.getSelectedIndex() == 1)
		{
			if(!ga.getSelectionPoint().isFreeHandSelectionConnected())
			{
				ga.getSelectionPoint().addFreeHandSelectionPoint(mouseEvent.getPoint());
			}
		}
		this.glitchArt.lblImageRender.repaint();
	}
	
	public void processMouseRelease(MouseEvent mouseEvent)
	{
		if(!IsDrawable())
		{
			return;
		}
		
		GlitchAlgorithm ga = this.glitchController.getAlgorithmList().get(this.glitchArt.jAlgorithmList.getSelectedIndex());
		boolean limitsBroken = checkDrawableLimits(mouseEvent);
		if(limitsBroken)
		{
			this.glitchArt.lblImageRender.repaint();
		}
		
		if(this.glitchArt.selectionType.getSelectedIndex() == 0)
		{
			if(ga.getSelectionPoint().getRectangle() != null)
			{
				ga.setSelectionPoint(translatePointsToRealSize(ga.getSelectionPoint()));
			}
		}
		else if(this.glitchArt.selectionType.getSelectedIndex() == 1)
		{
			ga.setSelectionPoint(translatePointsToRealSize(ga.getSelectionPoint()));
			if(!ga.getSelectionPoint().isFreeHandSelectionConnected())
			{
				clearSelectionPoint();
			}
		}
		
		this.glitchArt.lblImageRender.repaint();
	}
	
	private SelectionShape translatePointsToRealSize(SelectionShape selectionPoint)
	{
		float resultH = (float) this.glitchArt.realHeight / this.glitchArt.lblImageRender.getHeight();
		float resultW = (float) this.glitchArt.realWidth / this.glitchArt.lblImageRender.getWidth();
		if(selectionPoint.getSelectionType() == SelectionTypes.FREEHAND && selectionPoint.isFreeHandSelectionConnected())
		{
			for(Point freeHandPoint: selectionPoint.getFreeHandSelectionPointList())
			{
				selectionPoint.addTranslatedFreeHandPoint(new Point(Math.round(freeHandPoint.x * resultW), Math.round(freeHandPoint.y * resultH)));
			}
			Polygon polygon = getPolygonFromPoints(selectionPoint.getFreeHandTranslatedSelectionPointList());
			Rectangle boundedRegion = polygon.getBounds();
			
			if(boundedRegion.height > this.glitchArt.realHeight - 1)
			{
				boundedRegion.height = this.glitchArt.realHeight - 1;
			}
			
			if(boundedRegion.y < 0) 
			{
				boundedRegion.y = 0;
			}
			selectionPoint.setTranslatedRectangle(boundedRegion);
			selectionPoint.setTranslatedPolygon(polygon);
		}
		else if(selectionPoint.getSelectionType() == SelectionTypes.RECTANGLE)
		{
			Point startPoint = new Point(Math.round(selectionPoint.getStartPoint().x * resultW), Math.round(selectionPoint.getStartPoint().y * resultW));
			Point endPoint = new Point(Math.round(selectionPoint.getEndPoint().x * resultH), Math.round(selectionPoint.getEndPoint().y * resultH));
			Rectangle selectionRectangle= new Rectangle(startPoint);
			selectionRectangle.add(endPoint);
			selectionPoint.setTranslatedRectangle(selectionRectangle);
		}
		return selectionPoint;
	}
	
	private boolean checkDrawableLimits(MouseEvent mouseEvent)
	{
		GlitchAlgorithm ga = this.glitchController.getAlgorithmList().get(this.glitchArt.jAlgorithmList.getSelectedIndex());
		if(ga.getSelectionPoint() != null)
		{
			if(ga.getSelectionPoint().getSelectionType() == SelectionTypes.RECTANGLE)
			{
				ga.getSelectionPoint().setEndPoint(mouseEvent.getPoint());
			}
		}
		
		boolean limitsBroken = false;
		if(mouseEvent.getPoint().x > this.glitchArt.lblImageRender.getWidth() - 1)
		{
			if(ga.getSelectionPoint().getSelectionType() == SelectionTypes.RECTANGLE)
			{
				ga.getSelectionPoint().setEndPoint(normalizeEndPoint(new Point(this.glitchArt.lblImageRender.getWidth() - 1, mouseEvent.getY())));
			}
			else
			{
				ga.getSelectionPoint().addFreeHandSelectionPoint(new Point(this.glitchArt.lblImageRender.getWidth() - 1, mouseEvent.getY()));
			}
			limitsBroken = true;
		}
		if(mouseEvent.getPoint().x < 0)
		{
			if(ga.getSelectionPoint().getSelectionType() == SelectionTypes.RECTANGLE)
			{
				ga.getSelectionPoint().setEndPoint(normalizeEndPoint(new Point(0, mouseEvent.getY())));
			}
			else
			{
				ga.getSelectionPoint().addFreeHandSelectionPoint(normalizeEndPoint(new Point(0, mouseEvent.getY())));
			}
			limitsBroken = true;
		}
		if(mouseEvent.getPoint().y > this.glitchArt.lblImageRender.getHeight() - 1)
		{
			if(ga.getSelectionPoint().getSelectionType() == SelectionTypes.RECTANGLE)
			{
				ga.getSelectionPoint().setEndPoint(normalizeEndPoint(new Point(mouseEvent.getX(), this.glitchArt.lblImageRender.getHeight() - 1)));
			}
			else
			{
				ga.getSelectionPoint().addFreeHandSelectionPoint(normalizeEndPoint(new Point(mouseEvent.getX(), this.glitchArt.lblImageRender.getHeight() - 1)));
			}
			limitsBroken = true;
		}
		if(mouseEvent.getPoint().y < 0)
		{
			if(ga.getSelectionPoint().getSelectionType() == SelectionTypes.RECTANGLE)
			{
				ga.getSelectionPoint().setEndPoint(normalizeEndPoint(new Point(mouseEvent.getX(), 0)));
			}
			else
			{
				ga.getSelectionPoint().addFreeHandSelectionPoint(normalizeEndPoint(new Point(mouseEvent.getX(), 0)));
			}
			limitsBroken = true;
		}	
		
		if(ga.getSelectionPoint().getSelectionType() == SelectionTypes.FREEHAND)
		{
			if(ga.getSelectionPoint().getFreeHandSelectionPointList().size() > 1 &&
					ga.getSelectionPoint().getFreeHandSelectionPointList().get(ga.getSelectionPoint().getFreeHandSelectionPointList().size() - 1).equals(ga.getSelectionPoint().getStartPoint()))
			{
				ga.getSelectionPoint().setFreeHandSelectionConnected(true);
			}
		}
		return limitsBroken;
	}
	
	public Point normalizeEndPoint(Point inputPoint)
	{
		if(inputPoint.y > this.glitchArt.getHeight() - 1)
		{
			inputPoint.y = this.glitchArt.lblImageRender.getHeight() - 1;
		}
		if(inputPoint.x > this.glitchArt.lblImageRender.getWidth() - 1)
		{
			inputPoint.x = this.glitchArt.lblImageRender.getWidth() - 1;
		}
		if(inputPoint.y < 0)
		{
			inputPoint.y = 0;
		}
		if(inputPoint.x < 0)
		{
			inputPoint.x = 0;
		}
		return inputPoint;
	}
	
	public void clearSelectionPoint()
	{
		if(!this.glitchController.getAlgorithmList().isEmpty())
		{
			GlitchAlgorithm ga = this.glitchController.getAlgorithmList().get(this.glitchArt.jAlgorithmList.getSelectedIndex());
			ga.setSelectionPoint(null);
		}
	}
	
	public void drawSelectedRectangle()
	{
		if(this.glitchArt.jAlgorithmList.getSelectedIndex() > 0)
		{
			GlitchAlgorithm ga = this.glitchController.getAlgorithmList().get(this.glitchArt.jAlgorithmList.getSelectedIndex());
			if(ga.getSelectionPoint() != null)
			{
				drawSelectedRectangle(ga.getSelectionPoint(), this.glitchArt.lblImageRender.getGraphics());
			}
		}
	}
	
	private void drawFreeHandShape(SelectionShape selectionPoint, Graphics g)
	{
		Graphics2D g2 = (Graphics2D) g;
		if(selectionPoint.isFreeHandSelectionConnected())
		{
			g2.setComposite(AlphaComposite.SrcOver.derive(0.7f));
			g2.setPaint(new Color(221, 0, 72));
		}
		for(int i = 0; i < selectionPoint.getFreeHandSelectionPointList().size(); i++)
		{
			Point point = selectionPoint.getFreeHandSelectionPointList().get(i);
			if(i < selectionPoint.getFreeHandSelectionPointList().size() - 1)
			{
				Point point2 = selectionPoint.getFreeHandSelectionPointList().get(i + 1);
				if(i == selectionPoint.getFreeHandSelectionPointList().size() - 1 || i == 0)
				{
					g2.setStroke(new BasicStroke(6));
				}
				else
				{
					g2.setStroke(new BasicStroke(2));
				}
				g2.drawLine(point.x, point.y, point2.x, point2.y);
			}
			
		}
		g2.dispose();
	}
	
	private void drawSelectedRectangle(SelectionShape selectionPoint, Graphics g)
	{
		drawRectangle(g, selectionPoint.getStartPoint().x, selectionPoint.getStartPoint().y, selectionPoint.getEndPoint().x, selectionPoint.getEndPoint().y);
	}
	
	private void drawRectangle(Graphics g, int x, int y, int x2, int y2) 
	{
        int px = Math.min(x, x2);
        int py = Math.min(y, y2);
        int pw = Math.abs(x - x2);
        int ph = Math.abs(y - y2);
        g.drawRect(px, py, pw, ph);
    }
	
	public Polygon getPolygonFromPoints(List<Point> pointsList)
	{
		Polygon shapePoly = new Polygon();
		for(Point point : pointsList)
		{
			shapePoly.addPoint(point.x, point.y);
		}
		return shapePoly;
	}
	
	private boolean IsDrawable()
	{
		if(this.glitchController.getAlgorithmList().isEmpty() || this.glitchArt.jAlgorithmList.getSelectedIndex() < 0)
		{
			return false;
		}
		return true;
	}

	public GlitchController getGlitchController() {
		return glitchController;
	}

	public void setGlitchController(GlitchController glitchController) {
		this.glitchController = glitchController;
	}

	public GlitchArt getGlitchArt() {
		return glitchArt;
	}

	public void setGlitchArt(GlitchArt glitchArt) {
		this.glitchArt = glitchArt;
	}
}
