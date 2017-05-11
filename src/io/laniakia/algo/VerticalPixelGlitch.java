package io.laniakia.algo;

import java.awt.Color;
import java.awt.image.BufferedImage;

import io.laniakia.util.GlitchTypes;
import io.laniakia.util.ImageUtil;

public class VerticalPixelGlitch extends GlitchAlgorithm 
{
	public VerticalPixelGlitch()
	{
		setName(GlitchTypes.VERTICAL_PIXEL_GLITCH);
		setDescription("Glitch RGB value of pixels in a simple vertical fashion");
	}
    
	@Override
	public byte[] glitchPixels(byte[] inputImageBytes) throws Exception
	{
		int distortion = ((Integer) getPixelGlitchParameters().get("distortionAmount")).intValue();
		BufferedImage inputImage = ImageUtil.getImageFromBytes(inputImageBytes);
		BufferedImage outputImage = new BufferedImage(inputImage.getWidth(), inputImage.getHeight(), BufferedImage.TYPE_INT_RGB);
		
		boolean newVerticalPixelColumn = false;
		int currentPixelPosition = -1;
		int currentPixelRowIndex = -1;
		float distortionAmount = (float) distortion / 100;
        for (int w = 0; w < inputImage.getWidth(); w++)
        {
            newVerticalPixelColumn = true;
            Color[] verticalPixelArray = new Color[inputImage.getHeight()];
            currentPixelRowIndex = 0;
            for (int h = 0; h < inputImage.getHeight(); h++)
            {
                Color currentPixel = new Color(inputImage.getRGB(w, h));
                if (GetBrightness(currentPixel) > distortionAmount)
                {
                    outputImage.setRGB(w, h, currentPixel.getRGB());
                    if (!newVerticalPixelColumn)
                    {
                    	sortPixelsInColumn(verticalPixelArray, 0, currentPixelRowIndex - 1);
                    	for (int j = 0; j < currentPixelRowIndex; j++) 
                		{
                			outputImage.setRGB(w, j + currentPixelPosition, verticalPixelArray[j].getRGB());
                		}
                    	verticalPixelArray = new Color[inputImage.getHeight()];
                    	currentPixelRowIndex = 0;
                    	newVerticalPixelColumn = true;
                    }
                }
                else if (GetBrightness(currentPixel) <= distortionAmount)
                {
                    if (newVerticalPixelColumn)
                    {
                        currentPixelPosition = h;
                        newVerticalPixelColumn = false;
                    }
                    verticalPixelArray[currentPixelRowIndex++] = currentPixel;
                }
            }

            if (!newVerticalPixelColumn)
            {
                sortPixelsInColumn(verticalPixelArray, 0, currentPixelRowIndex - 1);
            	for (int i = 0; i < currentPixelRowIndex; i++) 
        		{
        			outputImage.setRGB(w, i + currentPixelPosition, verticalPixelArray[i].getRGB());
        		}
            }
        }
		return ImageUtil.getImageBytes(outputImage);
	}


	private void sortPixelsInColumn(Color[] pixelArray, int leftPixel, int rightPixel) 
	{
		int leftSidePixel = leftPixel;
		int rightSidePixel = rightPixel;
		float halfBrightness = GetBrightness(pixelArray[(leftPixel + rightPixel) / 2]);
		while (leftSidePixel <= rightSidePixel)
		{
			while (GetBrightness(pixelArray[leftSidePixel]) < halfBrightness) 
			{
				leftSidePixel++;
			}
			while (GetBrightness(pixelArray[rightSidePixel]) > halfBrightness)
			{
				rightSidePixel--;
			}
			if (leftSidePixel <= rightSidePixel)
			{
				Color currentPixel = pixelArray[leftSidePixel];
				pixelArray[leftSidePixel] = pixelArray[rightSidePixel];
				pixelArray[rightSidePixel] = currentPixel;
				rightSidePixel--;
				leftSidePixel++;
			}
		}
		if (leftPixel < rightSidePixel)
		{
			sortPixelsInColumn(pixelArray, leftPixel, rightSidePixel);
		}
		if (leftSidePixel < rightPixel) 
		{
			sortPixelsInColumn(pixelArray, leftSidePixel, rightPixel);
		}
	}

	//http://referencesource.microsoft.com/#System.Drawing/commonui/System/Drawing/Color.cs
	private float GetBrightness(Color inputColor)
	{
	    float r = (float)inputColor.getRed() / 255.0f;
	    float g = (float)inputColor.getGreen() / 255.0f;
	    float b = (float)inputColor.getBlue() / 255.0f;
	    float max, min;
	    max = r; min = r;
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
	    return (max + min) / 2;
	}
}
