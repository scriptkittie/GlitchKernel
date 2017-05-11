package io.laniakia.ui;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;

import io.laniakia.algo.GlitchAlgorithm;
import io.laniakia.algo.GlitchController;

public class SelectionPointManager 
{
	private GlitchController glitchController;
	private GlitchArt glitchArt;
	
	public SelectionPointManager(GlitchController glitchController, GlitchArt glitchArt)
	{
		this.glitchArt = glitchArt;
		this.glitchController = glitchController;
	}
	
	public void processImageRenderPaint(Graphics g)
	{
		if(this.glitchArt.jAlgorithmList.getSelectedIndex() >= 0 && this.glitchController.getAlgorithmList().get(this.glitchArt.jAlgorithmList.getSelectedIndex()).getSelectionPoint() != null)
		{
			drawSelectedRectangle(this.glitchController.getAlgorithmList().get(this.glitchArt.jAlgorithmList.getSelectedIndex()).getSelectionPoint(), g);
		}
	}
	
	public void processMousePress(MouseEvent mouseEvent)
	{
		if(!IsDrawable())
		{
			return;
		}
		
		GlitchAlgorithm ga = this.glitchController.getAlgorithmList().get(this.glitchArt.jAlgorithmList.getSelectedIndex());
		ga.setSelectionPoint(new SelectionPoint(mouseEvent.getPoint(), mouseEvent.getPoint()));
	}
	
	public void processMouseDrag(MouseEvent mouseEvent)
	{
		if(!IsDrawable())
		{
			return;
		}
		
		boolean limitsBroken = checkDrawableLimits(mouseEvent);
		
		if(limitsBroken)
		{
			this.glitchArt.lblImageRender.repaint();
			return;
		}
		GlitchAlgorithm ga = this.glitchController.getAlgorithmList().get(this.glitchArt.jAlgorithmList.getSelectedIndex());
		ga.getSelectionPoint().setEndPoint(mouseEvent.getPoint());
		this.glitchArt.lblImageRender.repaint();
	}
	
	public void processMouseRelease(MouseEvent mouseEvent)
	{
		if(!IsDrawable())
		{
			return;
		}
		
		boolean limitsBroken = checkDrawableLimits(mouseEvent);
		if(limitsBroken)
		{
			this.glitchArt.lblImageRender.repaint();
		}
		GlitchAlgorithm ga = this.glitchController.getAlgorithmList().get(this.glitchArt.jAlgorithmList.getSelectedIndex());
		
		if(ga.getSelectionPoint().getRectangle() != null)
		{
			ga.setSelectionPoint(translatePointsToRealSize(ga.getSelectionPoint()));
		}
	}
	
	private SelectionPoint translatePointsToRealSize(SelectionPoint selectionPoint)
	{
		float resultH = (float) this.glitchArt.realHeight / this.glitchArt.lblImageRender.getHeight();
		float resultW = (float) this.glitchArt.realWidth / this.glitchArt.lblImageRender.getWidth();
		Point startPoint = new Point(Math.round(selectionPoint.getStartPoint().x * resultW), Math.round(selectionPoint.getStartPoint().y * resultW));
		Point endPoint = new Point(Math.round(selectionPoint.getEndPoint().x * resultH), Math.round(selectionPoint.getEndPoint().y * resultH));
		Rectangle selectionRectangle= new Rectangle(startPoint);
		selectionRectangle.add(endPoint);
		selectionPoint.setTranslatedRectangle(selectionRectangle);
		return selectionPoint;
	}
	
	private boolean checkDrawableLimits(MouseEvent mouseEvent)
	{
		GlitchAlgorithm ga = this.glitchController.getAlgorithmList().get(this.glitchArt.jAlgorithmList.getSelectedIndex());
		if(ga.getSelectionPoint() != null)
		{
			ga.getSelectionPoint().setEndPoint(mouseEvent.getPoint());
		}
		
		boolean limitsBroken = false;
		if(mouseEvent.getPoint().x > this.glitchArt.lblImageRender.getWidth() - 1)
		{
			ga.getSelectionPoint().setEndPoint(normalizeEndPoint(new Point(this.glitchArt.lblImageRender.getWidth() - 1, mouseEvent.getY())));
			limitsBroken = true;
		}
		if(mouseEvent.getPoint().x < 0)
		{
			ga.getSelectionPoint().setEndPoint(normalizeEndPoint(new Point(0, mouseEvent.getY())));
			limitsBroken = true;
		}
		if(mouseEvent.getPoint().y > this.glitchArt.lblImageRender.getHeight() - 1)
		{
			ga.getSelectionPoint().setEndPoint(normalizeEndPoint(new Point(mouseEvent.getX(), this.glitchArt.lblImageRender.getHeight() - 1)));
			limitsBroken = true;
		}
		if(mouseEvent.getPoint().y < 0)
		{
			ga.getSelectionPoint().setEndPoint(normalizeEndPoint(new Point(mouseEvent.getX(), 0)));
			limitsBroken = true;
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
	
	private void drawSelectedRectangle(SelectionPoint selectionPoint, Graphics g)
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
