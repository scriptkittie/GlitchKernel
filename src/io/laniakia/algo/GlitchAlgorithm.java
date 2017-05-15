package io.laniakia.algo;

import java.util.HashMap;
import java.util.Map;

import io.laniakia.ui.SelectionShape;
import io.laniakia.util.GlitchTypes;

public abstract class GlitchAlgorithm 
{
	private int iterations;
	private GlitchTypes name;
	private String description;
	private Map<String, Object> pixelGlitchParameters;
	private SelectionShape selectionPoint;
	
	public GlitchAlgorithm()
	{
		this.iterations = 1;
		this.pixelGlitchParameters = new HashMap<String, Object>();
	}
	public GlitchAlgorithm(SelectionShape selectionPoint)
	{
		super();
		this.selectionPoint = selectionPoint;
	}
	
	public abstract byte[] glitchPixels(byte[] inputImageBytes) throws Exception;
	
	public int getIterations() 
	{
		return iterations;
	}
	public void setIterations(int iterations) 
	{
		this.iterations = iterations;
	}
	public GlitchTypes getName() 
	{
		return name;
	}
	public void setName(GlitchTypes name) 
	{
		this.name = name;
	}
	public String getDescription() 
	{
		return description;
	}
	public void setDescription(String description) 
	{
		this.description = description;
	}
	public Map<String, Object> getPixelGlitchParameters() {
		return pixelGlitchParameters;
	}

	public void setPixelGlitchParameters(Map<String, Object> pixelGlitchParameters) {
		this.pixelGlitchParameters = pixelGlitchParameters;
	}
	
	public SelectionShape getSelectionPoint() 
	{
		return selectionPoint;
	}
	public void setSelectionPoint(SelectionShape selectionPoint) 
	{
		this.selectionPoint = selectionPoint;
	}
	
	@Override
	public String toString() 
	{
		return this.name.toString();
	}
}
