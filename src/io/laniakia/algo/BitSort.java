package io.laniakia.algo;

import java.awt.Color;
import java.awt.image.BufferedImage;

import io.laniakia.util.GlitchTypes;
import io.laniakia.util.ImageUtil;

public class BitSort extends GlitchAlgorithm 
{
	public BitSort()
	{
		setName(GlitchTypes.BIT_SORT);
		setDescription("Sort Pixels by randomly shifting bits");
	}
	
	@Override
	public byte[] glitchPixels(byte[] inputImageBytes) throws Exception
	{
		int bitSortVerticalInterval = ((Integer) getPixelGlitchParameters().get("bitSortVerticalInterval")).intValue();
		int bitSortHorizontalInterval = ((Integer) getPixelGlitchParameters().get("bitSortHorizontalInterval")).intValue();
		int bitSortDistortion = ((Integer) getPixelGlitchParameters().get("bitSortDistortion")).intValue();
		BufferedImage inputImage = ImageUtil.getImageFromBytes(inputImageBytes);
		BufferedImage outputImage = ImageUtil.getImageFromBytes(inputImageBytes);
		int offsetHeight = inputImage.getHeight() - bitSortVerticalInterval;
		int offsetWidth = inputImage.getWidth() - bitSortHorizontalInterval;
		for(int i = 0; i < offsetHeight; i++)
		{
			for(int j = 0; j < offsetWidth; j++)
			{
				if((j + bitSortHorizontalInterval) < inputImage.getWidth()
						&& (i + bitSortVerticalInterval) < inputImage.getHeight()
						&& (i + bitSortVerticalInterval) >= 0
						&& j + bitSortHorizontalInterval >= 0
						&& j >= 0 && i >= 0 && j < inputImage.getWidth() && i < inputImage.getHeight())
				{
					Color currentPixel = new Color(inputImage.getRGB(j, i));
					Color nextPixel = new Color(inputImage.getRGB(j + bitSortHorizontalInterval, i + bitSortVerticalInterval));
					if(getHue(currentPixel) > (bitSortDistortion + getHue(nextPixel)))
					{
						outputImage.setRGB(j + bitSortHorizontalInterval, i + bitSortVerticalInterval, currentPixel.getRGB());
						outputImage.setRGB(j, i, nextPixel.getRGB());
					}
				}
			}
		}
		return ImageUtil.getImageBytes(outputImage);
	}
	
	//http://referencesource.microsoft.com/#System.Drawing/commonui/System/Drawing/Color.cs
	public float getHue(Color inputPixel) 
	{
        if (inputPixel.getRed() == inputPixel.getGreen() && inputPixel.getGreen() == inputPixel.getBlue())
        {
        	return 0; 
        }
        
        float r = (float)inputPixel.getRed() / 255.0f;
        float g = (float)inputPixel.getGreen() / 255.0f;
        float b = (float)inputPixel.getBlue() / 255.0f;

        float max, min;
        float delta;
        float hue = 0.0f;
        max = r; 
        min = r;

        if (g > max)
        {
        	max = g;
        }
        if (b > max)
        {
        	 max = b;
        }
        if (g < min)
        {
        	min = g;
        }
        if (b < min)
        {
        	min = b;
        }
        delta = max - min;
        if (r == max) 
        {
            hue = (g - b) / delta;
        }
        else if (g == max) 
        {
            hue = 2 + (b - r) / delta;
        }
        else if (b == max) 
        {
            hue = 4 + (r - g) / delta;
        }
        hue *= 60;
        if (hue < 0.0f) 
        {
            hue += 360.0f;
        }
        return hue;
    }
}
