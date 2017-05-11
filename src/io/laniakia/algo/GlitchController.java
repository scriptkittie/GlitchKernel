package io.laniakia.algo;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.laniakia.filter.BrightnessFilter;
import io.laniakia.filter.RGBShiftFilter;
import io.laniakia.ui.SelectionPoint;
import io.laniakia.util.GlitchTypes;
import io.laniakia.util.ImageUtil;

public class GlitchController
{
	private static final Logger logger = LogManager.getLogger(GlitchController.class);
	private List<GlitchAlgorithm> algorithmList = new ArrayList<GlitchAlgorithm>();

	@SuppressWarnings("serial")
	private Map<GlitchTypes, Class<? extends GlitchAlgorithm>> mappings = new HashMap<GlitchTypes, Class<? extends GlitchAlgorithm>>() {{
	    //put(GlitchTypes.DATABEND, DataBend.class);
	    put(GlitchTypes.DATA_AS_SOUND, DataAsSound.class);
	    put(GlitchTypes.FRACTAL_PIXEL_DISPERSE, FractalPixelSort.class);
	    put(GlitchTypes.OFFSET_PIXEL_SORT, OffsetPixelSort.class);
	    put(GlitchTypes.GENERIC_PIXEL_SORT_V1, PixelSort.class);
	    put(GlitchTypes.HORIZONTAL_PIXEL_SORT, HorizontalPixelSort.class);
	    put(GlitchTypes.PIXEL_SLICE, PixelSlice.class);
	    put(GlitchTypes.RGB_SHIFT_FILTER, RGBShiftFilter.class);
	    put(GlitchTypes.BRIGHTNESS_FILTER, BrightnessFilter.class);
	    put(GlitchTypes.VERTICAL_PIXEL_GLITCH, VerticalPixelGlitch.class);
	    put(GlitchTypes.BIT_SORT, BitSort.class);
	}};
	
	public GlitchController(List<GlitchAlgorithm> algorithmList)
	{
		this.algorithmList = algorithmList;
	}
	
	public GlitchController()
	{
		
	}
	
	public BufferedImage getGlitchChain(InputStream inputImageStream) throws Exception
	{
		if(inputImageStream == null)
		{
			return null;
		}
		
		BufferedImage inputImage = ImageIO.read(inputImageStream);
		String imageFormat = ImageUtil.getImageType(ImageUtil.getImageBytes(inputImage));
		logger.debug("Image Format: " + imageFormat);
		if(StringUtils.isNotBlank(imageFormat))
		{
			if(imageFormat.equals("image/png"))
			{
				BufferedImage inputImageCopy = new BufferedImage(inputImage.getWidth(), inputImage.getHeight(), BufferedImage.TYPE_INT_RGB);
				inputImageCopy.createGraphics().drawImage(inputImage, 0, 0, Color.WHITE, null);
				inputImage = inputImageCopy;
			}
		}
		byte[] inputImageBytes = ImageUtil.getImageBytes(inputImage);
		byte[] resultImageGlitchedBytes = inputImageBytes.clone();
		for(GlitchAlgorithm algorithm : this.algorithmList)
		{
			logger.debug("Processing for algorithm: " + algorithm.getName());
			for(int i = 0; i < algorithm.getIterations(); i++)
			{
				try 
				{
					if(algorithm.getSelectionPoint() != null)
					{
						Rectangle selectionPoint = algorithm.getSelectionPoint().getTranslatedRectangle();
						BufferedImage fullSizeImage = ImageUtil.getImageFromBytes(resultImageGlitchedBytes);
						BufferedImage subImageGlitch = fullSizeImage.getSubimage(selectionPoint.x, selectionPoint.y, (int)selectionPoint.getWidth(), (int)selectionPoint.getHeight());
						byte[] subImageGlitchedBytes = algorithm.glitchPixels(ImageUtil.getImageBytes(subImageGlitch));
						Graphics g = fullSizeImage.getGraphics();
						g.drawImage(ImageUtil.getImageFromBytes(subImageGlitchedBytes), selectionPoint.x, selectionPoint.y, null);
						g.dispose();
						resultImageGlitchedBytes = ImageUtil.getImageBytes(fullSizeImage);
					}
					else
					{
						resultImageGlitchedBytes = algorithm.glitchPixels(resultImageGlitchedBytes);
					}
					
				}
				catch (Exception e) 
				{
					logger.debug("Error in algorithm: " + e.getMessage());
					e.printStackTrace();
					if(algorithm instanceof OffsetPixelSort && e.getClass().getSimpleName().contains("RasterFormatException"))
					{
						logger.debug("Exception was RasterFormatException, this can be ignored one time...");
						try 
						{
							resultImageGlitchedBytes = algorithm.glitchPixels(resultImageGlitchedBytes);
						} 
						catch (Exception e1) 
						{
							logger.debug("Failed to recover from Raster image processing, skipping...");
							continue;
						}
					}
				}
			}			
		}
		return ImageUtil.getImageFromBytes(resultImageGlitchedBytes);
	}
	
	public void clearSelectionPoints()
	{
		for(GlitchAlgorithm algorithm : this.algorithmList)
		{
			algorithm.setSelectionPoint(null);
		}
	}
	
	public GlitchAlgorithm[] getGlitchAlgorithmChainList()
	{
		return this.algorithmList.toArray(new GlitchAlgorithm[algorithmList.size()]);
	}
	
	public GlitchAlgorithm addAlgorithm(GlitchTypes glitchType, Map<String, Object> glitchAlgorithmParameters) throws Exception
	{
		GlitchAlgorithm ga = mappings.get(glitchType).newInstance();
		ga.setPixelGlitchParameters(glitchAlgorithmParameters);
		addAlgorithm(ga);
		return ga;
	}
	
	public boolean containsAlgorithmOfType(GlitchTypes glitchType)
	{
		for(GlitchAlgorithm ga : this.algorithmList)
		{
			if(ga.getName() == glitchType)
			{
				return true;
			}
		}
		return false;
	}
	
	public void insertAlgorithmAtIndex(int index, GlitchTypes glitchType, Map<String, Object> glitchAlgorithmParameters) throws Exception
	{
		GlitchAlgorithm ga = mappings.get(glitchType).newInstance();
		ga.setPixelGlitchParameters(glitchAlgorithmParameters);
		insertAlgorithmAtIndex(index, ga);
	}
	
	public void insertAlgorithmAtIndex(int index, GlitchAlgorithm algorithm)
	{
		this.algorithmList.add(index, algorithm);
	}
	
	public void addAlgorithm(GlitchAlgorithm algorithm)
	{
		this.algorithmList.add(algorithm);
	}
	
	public void removeAlgorithm(int index)
	{
		if(!this.algorithmList.isEmpty())
		{
			this.algorithmList.remove(index);
		}
	}
	public void moveAlgorithmAtIndex(int sourcePosition, int targetPosition)
	{
		logger.debug("Shifting algorithm in list. Source=" + sourcePosition + ", Target=" + targetPosition);
		if (sourcePosition <= targetPosition) 
		{
			Collections.rotate(this.algorithmList.subList(sourcePosition, targetPosition + 1), -1);
		} 
		else 
		{
			Collections.rotate(this.algorithmList.subList(targetPosition, sourcePosition + 1), 1);
		}
	}
	
	public List<SelectionPoint> getSelectionPointList()
	{
		List<SelectionPoint> selectionPointList = new ArrayList<SelectionPoint>();
		for(GlitchAlgorithm algorithm : this.algorithmList)
		{
			selectionPointList.add(algorithm.getSelectionPoint());
		}
		return selectionPointList;
	}
	
	public List<GlitchAlgorithm> getAlgorithmList()
	{
		return this.algorithmList;
	}
}
