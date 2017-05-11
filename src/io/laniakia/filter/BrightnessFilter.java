package io.laniakia.filter;

import java.awt.image.BufferedImage;

import io.laniakia.algo.GlitchAlgorithm;
import io.laniakia.util.GlitchTypes;
import io.laniakia.util.ImageUtil;

public class BrightnessFilter extends GlitchAlgorithm 
{
	public BrightnessFilter()
	{
		setName(GlitchTypes.BRIGHTNESS_FILTER);
		setDescription("Increase or Decrease brightness by RGB shifts.");
	}
	
	@Override
	public byte[] glitchPixels(byte[] inputImageBytes) throws Exception 
	{
		int renderBrightness = ((Integer) getPixelGlitchParameters().get("renderBrightness")).intValue();
		if(renderBrightness < -10 || renderBrightness > 10)
		{
			return null;
		}
		float brightnessValue = 1 + (float) renderBrightness / 10;
		BufferedImage outputImage = ImageUtil.changeBrightness(ImageUtil.getImageFromBytes(inputImageBytes), brightnessValue);
		return ImageUtil.getImageBytes(outputImage);
	}

}
