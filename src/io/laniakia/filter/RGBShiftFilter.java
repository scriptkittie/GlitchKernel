package io.laniakia.filter;

import java.awt.Color;
import java.awt.image.BufferedImage;

import com.meltingice.caman.CamanJ;

import io.laniakia.algo.GlitchAlgorithm;
import io.laniakia.util.GlitchTypes;
import io.laniakia.util.ImageUtil;

public class RGBShiftFilter extends GlitchAlgorithm
{
	public RGBShiftFilter()
	{
		setName(GlitchTypes.RGB_SHIFT_FILTER);
		setDescription("Randomize colors by bitshiting RGB values");
	}

	@Override
	public byte[] glitchPixels(byte[] inputImageBytes) throws Exception 
	{
		int rgbShiftColor = ((Integer) getPixelGlitchParameters().get("rgbShiftcolor")).intValue();
		int rgbShiftAmount = ((Integer) getPixelGlitchParameters().get("rgbShiftAmount")).intValue();
		BufferedImage inputImage = ImageUtil.getImageFromBytes(inputImageBytes);
		if(rgbShiftColor == 3)
		{
			rgbShiftAmount = rgbShiftAmount * 5;
			CamanJ caman = new CamanJ(inputImage);
			caman.filter("vibrance").set(rgbShiftAmount);
			inputImage = caman.save();
		}
		else
		{
			for (int i = 0; i < inputImage.getHeight(); i++) 
			{
				for (int j = 0; j < inputImage.getWidth(); j++) 
				{
					Color pixelColor = new Color(inputImage.getRGB(j, i));
					Color shiftedColor = null;
					int shiftFactor = 0;
					if(rgbShiftColor == 0)
					{
						shiftFactor = pixelColor.getRed() + rgbShiftAmount;
						shiftedColor = new Color(normalizeColor(shiftFactor), normalizeColor(pixelColor.getGreen() - rgbShiftAmount), normalizeColor(pixelColor.getBlue() - rgbShiftAmount), pixelColor.getAlpha());
					}
					else if(rgbShiftColor == 1)
					{
						shiftFactor = pixelColor.getGreen() + rgbShiftAmount;
						shiftedColor = new Color(normalizeColor(pixelColor.getRed() - rgbShiftAmount), normalizeColor(shiftFactor), normalizeColor(pixelColor.getBlue() - rgbShiftAmount), pixelColor.getAlpha());
					}
					else if(rgbShiftColor == 2)
					{
						shiftFactor = pixelColor.getBlue() + rgbShiftAmount;
						shiftedColor = new Color(normalizeColor(pixelColor.getRed() - rgbShiftAmount), normalizeColor(pixelColor.getGreen() - rgbShiftAmount), normalizeColor(shiftFactor), pixelColor.getAlpha());
					}
					inputImage.setRGB(j, i, shiftedColor.getRGB());
				}
			}
		}
		return ImageUtil.getImageBytes(inputImage);
	}
	
	private int normalizeColor(int color)
	{
		if(color < 0)
		{
			color = 0;
		}
		else if(color > 255)
		{
			color = 255;
		}
		return color;
	}
}
